package net.p0f.openshift.metrics.processor;

import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class PsacctToCsv implements Processor {
    private static final Logger LOG = Logger.getLogger(PsacctToCsv.class.getName());

    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getMessage().setBody(
            exchange.getIn().getBody(String.class)
                    .replaceAll(" +", ",")
                    .replaceFirst("^,", "")
                    .replaceFirst(",$", ",ANONYMOUS")
                    .replaceAll("(re|u|s|min|maj|swp),", ",")
        );
        LOG.fine("Converting to CSV: " + exchange.getMessage().getBody(String.class));
    }

    public static boolean isRecordValid(String body) {
        StringTokenizer st = new StringTokenizer(
                                    body.replaceAll(" +", " ").replaceFirst(" $", " x"),
                                    " ");
        LOG.fine("Checking record: \"" + body + "\": got " + st.countTokens() + " tokens.");
        return st.countTokens() == 8;
    }
}
