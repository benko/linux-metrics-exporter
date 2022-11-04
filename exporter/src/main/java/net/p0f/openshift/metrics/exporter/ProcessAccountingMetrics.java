package net.p0f.openshift.metrics.exporter;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import net.p0f.openshift.metrics.model.ProcessAccountingRecord;

@ApplicationScoped
@Named("processAccountingMetrics")
public class ProcessAccountingMetrics {
    static final Logger LOG = Logger.getLogger(ProcessAccountingMetrics.class.getName());

    @Inject
    MeterRegistry mr;

    HashMap<String, Counter> totalInvocationCount = new HashMap<>();
    HashMap<String, AtomicInteger> invocationCount = new HashMap<>();
    HashMap<String, AtomicInteger> totalTime = new HashMap<>();
    HashMap<String, AtomicInteger> userTime = new HashMap<>();
    HashMap<String, AtomicInteger> systemTime = new HashMap<>();
    HashMap<String, AtomicInteger> majFaults = new HashMap<>();
    HashMap<String, AtomicInteger> minFaults = new HashMap<>();
    HashMap<String, AtomicInteger> swapEvents = new HashMap<>();

    public void registerRecord(ProcessAccountingRecord par) {
        LOG.fine("Got record: " + par);
        String hn = par.getHostName();
        String pn = par.getProcessName();
        String key = pn + "@" + hn;

        Tags tags = Tags.of("host", hn, "process", pn);

        if (!this.totalInvocationCount.containsKey(key)) {
            LOG.fine("Registering psacct.invocation.total for " + key);
            this.totalInvocationCount.put(key,
                    Counter.builder("psacct.invocation.total")
                    .tags(tags)
                    .register(this.mr));
        }
        this.totalInvocationCount.get(key).increment(par.getNumCalls());

        if (!this.invocationCount.containsKey(key)) {
            LOG.fine("Registering psacct.invocation.count for " + key);
            AtomicInteger g = new AtomicInteger(0);
            this.invocationCount.put(key, g);
            this.mr.gauge("psacct.invocation.count", tags, g);
        }
        this.invocationCount.get(key).set(par.getNumCalls());

        if (!this.totalTime.containsKey(key)) {
            LOG.fine("Registering psacct.time.elapsed for " + key);
            AtomicInteger g = new AtomicInteger(0);
            this.totalTime.put(key, g);
            this.mr.gauge("psacct.time.elapsed", tags, g);
        }
        // Needs to be in ms because there's no AtomicFloat.
        this.totalTime.get(key).set(Float.valueOf(par.getElapsedTime() * 1000).intValue());

        if (!this.userTime.containsKey(key)) {
            LOG.fine("Registering psacct.time.user for " + key);
            AtomicInteger g = new AtomicInteger(0);
            this.userTime.put(key, g);
            this.mr.gauge("psacct.time.user", tags, g);
        }
        // Needs to be in ms because there's no AtomicFloat.
        this.userTime.get(key).set(Float.valueOf(par.getUserTime() * 1000).intValue());

        if (!this.systemTime.containsKey(key)) {
            LOG.fine("Registering psacct.time.system for " + key);
            AtomicInteger g = new AtomicInteger(0);
            this.systemTime.put(key, g);
            this.mr.gauge("psacct.time.system", tags, g);
        }
        // Needs to be in ms because there's no AtomicFloat.
        this.systemTime.get(key).set(Float.valueOf(par.getSystemTime() * 1000).intValue());

        if (!this.majFaults.containsKey(key)) {
            LOG.fine("Registering psacct.vm.fault.major for " + key);
            AtomicInteger g = new AtomicInteger(0);
            this.majFaults.put(key, g);
            this.mr.gauge("psacct.vm.fault.major", tags, g);
        }
        this.majFaults.get(key).set(par.getMajFaults());

        if (!this.minFaults.containsKey(key)) {
            LOG.fine("Registering psacct.vm.fault.minor for " + key);
            AtomicInteger g = new AtomicInteger(0);
            this.minFaults.put(key, g);
            this.mr.gauge("psacct.vm.fault.minor", tags, g);
        }
        this.minFaults.get(key).set(par.getMinFaults());

        if (!this.swapEvents.containsKey(key)) {
            LOG.fine("Registering psacct.vm.swap.events for " + key);
            AtomicInteger g = new AtomicInteger(0);
            this.swapEvents.put(key, g);
            this.mr.gauge("psacct.vm.swap.events", tags, g);
        }
        this.swapEvents.get(key).set(par.getSwapEvents());
    }

    public void resetGauges() {
        LOG.fine("Resetting all gauges for a new metric.");

        LOG.finer("Resetting psacct.invocation.count...");
        for (String x : this.invocationCount.keySet()) {
            LOG.finest("Resetting gauge psacct.invocation.count " + x);
            this.invocationCount.get(x).set(0);
        }

        LOG.finer("Resetting psacct.time.elapsed...");
        for (String x : this.totalTime.keySet()) {
            LOG.finest("Resetting gauge psacct.time.elapsed " + x);
            this.totalTime.get(x).set(0);
        }

        LOG.finer("Resetting psacct.time.user...");
        for (String x : this.userTime.keySet()) {
            LOG.finest("Resetting gauge psacct.time.user " + x);
            this.userTime.get(x).set(0);
        }

        LOG.finer("Resetting psacct.time.system...");
        for (String x : this.systemTime.keySet()) {
            LOG.finest("Resetting gauge psacct.time.system " + x);
            this.systemTime.get(x).set(0);
        }

        LOG.finer("Resetting psacct.vm.fault.major...");
        for (String x : this.systemTime.keySet()) {
            LOG.finest("Resetting gauge psacct.vm.fault.major " + x);
            this.majFaults.get(x).set(0);
        }

        LOG.finer("Resetting psacct.vm.fault.minor...");
        for (String x : this.systemTime.keySet()) {
            LOG.finest("Resetting gauge psacct.vm.fault.minor " + x);
            this.minFaults.get(x).set(0);
        }

        LOG.finer("Resetting psacct.vm.swap.events...");
        for (String x : this.systemTime.keySet()) {
            LOG.finest("Resetting gauge psacct.vm.swap.events " + x);
            this.swapEvents.get(x).set(0);
        }
    }
}
