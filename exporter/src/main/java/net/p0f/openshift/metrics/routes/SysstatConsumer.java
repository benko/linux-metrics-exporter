package net.p0f.openshift.metrics.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;

import net.p0f.openshift.metrics.exporter.SysstatMetrics;
import net.p0f.openshift.metrics.model.SysstatMeasurement;

public class SysstatConsumer extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("file:/metrics?" +
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
            .choice()
                .when(method(SysstatMetrics.class, "isRecordValid").not())
                    .log(LoggingLevel.WARN, "Illegal record: ${body}")
                .otherwise()
                    .to("bean:sysstatMetrics?method=processMetricRecord&scope=Request")
            .endChoice();
    }
}
