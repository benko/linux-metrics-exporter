package net.p0f.openshift.metrics.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;

import net.p0f.openshift.metrics.model.ProcessAccountingRecord;
import net.p0f.openshift.metrics.processor.PsacctToCsv;

public class PsacctConsumer extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        PsacctToCsv toCsv = new PsacctToCsv();
        DataFormat fromCsv = new BindyCsvDataFormat(ProcessAccountingRecord.class);
        from("file:/metrics?" +
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
            // TODO: See into error-handler ways of avoiding this choice.
            .choice()
                .when(bodyAs(String.class).isEqualTo(""))
                    .log(LoggingLevel.DEBUG, "Skipping empty record.")
                .when(method(PsacctToCsv.class, "isRecordValid").not())
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
