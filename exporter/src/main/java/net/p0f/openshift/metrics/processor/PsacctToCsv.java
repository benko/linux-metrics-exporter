package net.p0f.openshift.metrics.processor;

import java.util.StringTokenizer;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class PsacctToCsv implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getMessage().setBody(
            exchange.getIn().getBody(String.class)
                    .replaceAll(" +", ",")
                    .replaceFirst("^,", "")
                    .replaceFirst(",$", ",ANONYMOUS")
                    .replaceAll("(re|u|s|min|maj|swp),", ",")
        );
    }

    public static boolean isRecordValid(String body) {
        StringTokenizer st = new StringTokenizer(body, ",");
        return st.countTokens() == 8;
    }
}
