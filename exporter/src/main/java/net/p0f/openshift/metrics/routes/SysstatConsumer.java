package net.p0f.openshift.metrics.routes;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import net.p0f.openshift.metrics.exporter.SysstatMetrics;
import net.p0f.openshift.metrics.model.SysstatMeasurement;

@ApplicationScoped
public class SysstatConsumer extends RouteBuilder {
    @ConfigProperty(defaultValue = "/metrics", name = "exporter.data.path")
    String dataPath;

    @Override
    public void configure() throws Exception {
        from("file:" + dataPath + "?" +
                "fileName=sysstat-dump.json&" +
                "readLock=changed&" +
                "readLockCheckInterval=250&" +
                "move=done/${date:now:yyyyMMdd}/sysstat-${date:now:yyyyMMdd-HHmmss}")
            .routeId("sysstat-reader")
            .log(LoggingLevel.DEBUG, "Original Sysstat Payload: ${body}")
            /*
             * Need to transform:
             * - sysstat.hosts[0].nodename -> sysstat.hosts[0].statistics[0].hostname
             * - sysstat.hosts[0].number-of-cpus -> sysstat.hosts[0].statistics[0].num-cpus
             * 
             * Need to drop:
             * - everything outside statistics[0]
             * 
             * See:
             *   https://github.com/bazaarvoice/jolt
             *   https://github.com/apache/camel/blob/main/components/camel-jolt/src/test/resources/org/apache/camel/component/jolt/firstSample/spec.json
             */
            .to("jolt:net/p0f/openshift/metrics/routes/transformSysstat.json" +
                                    "?inputType=JsonString" +
                                    "&outputType=JsonString" +
                                    "&transformDsl=Chainr")
            .log(LoggingLevel.DEBUG, "Transformed Sysstat Json: ${body}")
            .unmarshal(new JacksonDataFormat(SysstatMeasurement.class))
            .log(LoggingLevel.INFO, "Unmarshaled Sysstat: ${body}")
            .setHeader("X-Is-Record-Valid", method(SysstatMetrics.class, "isRecordValid"))
            .log(LoggingLevel.DEBUG, "Validity check: ${header.X-Is-Record-Valid}")
            .choice()
                .when(header("X-Is-Record-Valid").isEqualTo(false))
                    .log(LoggingLevel.WARN, "Illegal record: ${body}")
                .otherwise()
                    .to("bean:sysstatMetrics?method=processMetricRecord&scope=Request")
            .endChoice();
    }
}
