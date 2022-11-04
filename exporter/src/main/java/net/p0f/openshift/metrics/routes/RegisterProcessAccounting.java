package net.p0f.openshift.metrics.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class RegisterProcessAccounting extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("seda:psacct")
            .routeId("psacct-dispatch")
            .log(LoggingLevel.DEBUG, "Sending ${body.processName}@${body.hostName} for processing.")
            .to("bean:processAccountingMetrics?method=registerRecord&scope=Request");
    }
}
