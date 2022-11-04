package net.p0f.openshift.metrics.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class ResetProcessAccounting extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:resetPsacct")
            .routeId("psacct-reset")
            .log(LoggingLevel.DEBUG, "Resetting psacct gauges for new snapshot.")
            .to("bean:processAccountingMetrics?method=resetGauges&scope=Request");
    }
}
