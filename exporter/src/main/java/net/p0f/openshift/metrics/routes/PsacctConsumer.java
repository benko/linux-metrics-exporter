package net.p0f.openshift.metrics.routes;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import net.p0f.openshift.metrics.model.ProcessAccountingRecord;
import net.p0f.openshift.metrics.processor.PsacctToCsv;

@ApplicationScoped
public class PsacctConsumer extends RouteBuilder {
    @ConfigProperty(defaultValue = "/metrics", name = "exporter.data.path")
    String dataPath;

    @Override
    public void configure() throws Exception {
        PsacctToCsv toCsv = new PsacctToCsv();
        DataFormat fromCsv = new BindyCsvDataFormat(ProcessAccountingRecord.class);
        from("file:" + dataPath + "?" +
                "fileName=psacct-dump-all&" +
                "readLock=changed&" +
                "readLockCheckInterval=250&" +
                "move=done/${date:now:yyyyMMdd}/psacct-${date:now:yyyyMMdd-HHmmss}")
            .routeId("psacct-reader")
            .log(LoggingLevel.DEBUG, "Original Psacct Payload: ${body}")
            // Reset current gauge status upon receiving a new message.
            .wireTap("direct:resetPsacct")
            // Split, unmarshall, and account for, all new records.
            .split().tokenize("\n").parallelProcessing()
            .log(LoggingLevel.DEBUG, "Split Psacct Record: ${body}")
            .setHeader("X-Is-Record-Valid", method(PsacctToCsv.class, "isRecordValid"))
            .log(LoggingLevel.DEBUG, "Validity check: ${header.X-Is-Record-Valid}")
            .choice()
                .when(bodyAs(String.class).isEqualTo(""))
                    .log(LoggingLevel.DEBUG, "Skipping empty record.")
                .when(header("X-Is-Record-Valid").isEqualTo(false))
                    .log(LoggingLevel.WARN, "Illegal record: ${body}")
                .otherwise()
                    .process(toCsv)
                    .log(LoggingLevel.DEBUG, "Transformed Psacct CSV: ${body}")
                    .unmarshal(fromCsv)
                    .log(LoggingLevel.INFO, "Unmarshaled Psacct: ${body}")
                    .to("seda:psacct")
            .endChoice();
    }
}
