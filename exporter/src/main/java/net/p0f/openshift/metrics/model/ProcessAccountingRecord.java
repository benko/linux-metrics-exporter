package net.p0f.openshift.metrics.model;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@CsvRecord(separator = ",")
public class ProcessAccountingRecord {
    // Expected input source from "sa -ajlp", meaning:
    //   -a = all records
    //   -j = use seconds instead of minutes
    //   -l = separate columns for system & user time
    //   -p = show paging information (faults & swaps)
    // Reporting time slice defaults to 10s but can be configured.
    //
    // Typical record:
    //   1  0.01re  0.00u  0.01s  231min  0maj  0swp  sadc

    // Assigned.
    String hostName;
    @DataField(pos = 8)
    String processName;
    @DataField(pos = 1)
    int numCalls;
    @DataField(pos = 2)
    float elapsedTime;
    @DataField(pos = 3)
    float userTime;
    @DataField(pos = 4)
    float systemTime;
    @DataField(pos = 5)
    int minFaults;
    @DataField(pos = 6)
    int majFaults;
    @DataField(pos = 7)
    int swapEvents;

    public ProcessAccountingRecord() {
        try {
            this.hostName = InetAddress.getLocalHost().getCanonicalHostName();
        } catch (UnknownHostException uhe) {
            this.hostName = "UNRESOLVABLE";
        }
    }
    public String getHostName() {
        return hostName;
    }
    public void setHostName(String hn) {
        this.hostName = hn;
    }
    public String getProcessName() {
        return processName;
    }
    public void setProcessName(String pn) {
        this.processName = pn;
    }
    public int getNumCalls() {
        return numCalls;
    }
    public void setNumCalls(int numCalls) {
        this.numCalls = numCalls;
    }
    public float getElapsedTime() {
        return elapsedTime;
    }
    public void setElapsedTime(float elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
    public float getUserTime() {
        return userTime;
    }
    public void setUserTime(float userTime) {
        this.userTime = userTime;
    }
    public float getSystemTime() {
        return systemTime;
    }
    public void setSystemTime(float systemTime) {
        this.systemTime = systemTime;
    }
    public int getMinFaults() {
        return minFaults;
    }
    public void setMinFaults(int minFaults) {
        this.minFaults = minFaults;
    }
    public int getMajFaults() {
        return majFaults;
    }
    public void setMajFaults(int majFaults) {
        this.majFaults = majFaults;
    }
    public int getSwapEvents() {
        return swapEvents;
    }
    public void setSwapEvents(int swapEvents) {
        this.swapEvents = swapEvents;
    }
    @Override
    public String toString() {
        return "ProcessAccountingRecord ["
                + "hostName=" + hostName
                + ", processName=" + processName
                + ", numCalls=" + numCalls
                + ", elapsedTime=" + elapsedTime
                + ", systemTime=" + systemTime
                + ", userTime=" + userTime
                + ", majFaults=" + majFaults
                + ", minFaults=" + minFaults
                + ", swapEvents=" + swapEvents
                + "]";
    }
}
