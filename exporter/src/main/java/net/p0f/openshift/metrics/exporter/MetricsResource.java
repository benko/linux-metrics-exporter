package net.p0f.openshift.metrics.exporter;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/metrics")
public class MetricsResource {
    @GET
    @Path("/version")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "MetricsExporter v1.0.0";
    }
}