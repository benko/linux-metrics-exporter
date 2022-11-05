package net.p0f.openshift.metrics.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SysstatMeasurement implements Serializable {
    // Getting data from "sadf -s t0 -e t0+td -j -- -A" where
    //   t0 = time of last loop start (start of reporting period)
    //   td = report snapshot duration (defaults to 10s; end of reporting period)
    //   -j = produce JSON report
    //   -A = sar -A (aka "-bBdFHSvwWy -I SUM -m ALL -n ALL -q ALL -r ALL -u ALL"), where
    //      GLOBAL STATS
    //      -b = I/O and xfrate (tps, rtps, wtps, dtps /discard/, bread/s, bwrtn/s, bdscd/s)
    //      -B = paging statistics (pgin/s, pgout/s, fault/s, Mflt/s, free/s, scank/s, scand/s, steal/s, vmeff%)
    //      -d = activity per blkdev (tps, rkB/s, wkB/s, dkB/s, areq-sz, aqu-sz, await, %util)
    //      -F = mount stats (MBfree, MBused, %used, %unpriv-used, Ifree, Iused, %Iused)
    //      -H = hugepage stats (kbfree, kbused, %used, kbrsrvd, kbsurplus)
    //      -S = swap stats (kbfree, kbused, %used, kbcached, %cached)
    //      -v = kernel table stats (dentry-unused, file-nr, inode-nr, pty-nr)
    //      -w = task creation & switching (proc/s, csw/s)
    //      -W = swapping stats (swpin/s, swpout/s)
    //      -y = ttydev stats (rcvintr/s, xmtintr/s, framerr/s, prtyerr/s, brk/s, ovrun/s)
    //      PER-ITEM STATS
    //      -I SUM = all interrupts (NOT COLLECTED BY sadc)
    //      -m ALL = power management stats (NOT COLLECTED BY sadc)
    //      -n ALL = network interface stats (ALL meaning DEV, EDEV, FC, ICMP, EICMP, ICMP6, EICMP6,
    //                  IP, EIP, IP6, EIP6, NFS, NFSD, SOCK, SOCK6, SOFT, TCP, ETCP, UDP and UDP6)
    //      -q ALL = load and pressure stats (%scpu + 10/60/300wnd /stalled/, %sio + wnd /lost to IO/,
    //                  %fio + wnd /stalled to IO/, runq-sz, plist-sz, ldavg-1/5/15, blocked,
    //                  %smem + wnd /stalled to mem/, %fmem + wnd /waiting for mem/)
    //      -r ALL = memory utilisation (kbfree, kbavail, %used, kbbuf, kbcach, kbcommit, %commit,
    //                  kbact, kbinact, kbdirty, kbanonpg, kbslab, kbkstack, kbpgtbl, kbvmused)
    //      -u ALL = cpu utilisation (%user, %usr w/o vproc, %nice, %system, %sys w/o intr, %iowait,
    //                  %steal, %irq, %soft, %guest, %gnice, %idle)
    // Reporting time slice defaults to 10s but can be changed.
    //
    // Typical record is too long to show here but look at annotations.
    // Also look at SysstatConsumer to see the necessary transformations.
    private static final Logger LOG = Logger.getLogger(SysstatMeasurement.class.getName());

    @JsonProperty("hostname")
    String hostname;
    @JsonProperty("num-cpus")
    int numCpus;

    @JsonProperty("cpu-load")
    List<CpuLoad> cpuLoad;
    @JsonProperty("process-and-context-switch")
    ProcessAndContextSwitch processAndContextSwitch;
    @JsonProperty("swap-pages")
    SwapPages swapPages;
    @JsonProperty("paging")
    Paging paging;
    @JsonProperty("io")
    Io io;
    @JsonProperty("memory")
    Memory memory;
    @JsonProperty("hugepages")
    Hugepages hugepages;
    @JsonProperty("kernel")
    Kernel kernel;
    @JsonProperty("queue")
    Queue queue;
    @JsonProperty("disk")
    List<Disk> disk;
    @JsonProperty("network")
    Network network;
    @JsonProperty("psi")
    Psi psi;

    public String getHostname() {
        return hostname;
    }
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
    public int getNumCpus() {
        return numCpus;
    }
    public void setNumCpus(int numCpus) {
        this.numCpus = numCpus;
    }
    public List<CpuLoad> getCpuLoad() {
        return cpuLoad;
    }
    public void setCpuLoad(List<CpuLoad> cpuLoad) {
        this.cpuLoad = cpuLoad;
    }
    public ProcessAndContextSwitch getProcessAndContextSwitch() {
        return processAndContextSwitch;
    }
    public void setProcessAndContextSwitch(ProcessAndContextSwitch processAndContextSwitch) {
        this.processAndContextSwitch = processAndContextSwitch;
    }
    public SwapPages getSwapPages() {
        return swapPages;
    }
    public void setSwapPages(SwapPages swapPages) {
        this.swapPages = swapPages;
    }
    public Paging getPaging() {
        return paging;
    }
    public void setPaging(Paging paging) {
        this.paging = paging;
    }
    public Io getIo() {
        return io;
    }
    public void setIo(Io io) {
        this.io = io;
    }
    public Memory getMemory() {
        return memory;
    }
    public void setMemory(Memory memory) {
        this.memory = memory;
    }
    public Hugepages getHugepages() {
        return hugepages;
    }
    public void setHugepages(Hugepages hugepages) {
        this.hugepages = hugepages;
    }
    public Kernel getKernel() {
        return kernel;
    }
    public void setKernel(Kernel kernel) {
        this.kernel = kernel;
    }
    public Queue getQueue() {
        return queue;
    }
    public void setQueue(Queue queue) {
        this.queue = queue;
    }
    public List<Disk> getDisk() {
        return disk;
    }
    public void setDisk(List<Disk> disk) {
        this.disk = disk;
    }
    public Network getNetwork() {
        return network;
    }
    public void setNetwork(Network network) {
        this.network = network;
    }
    public Psi getPsi() {
        return psi;
    }
    public void setPsi(Psi psi) {
        this.psi = psi;
    }
    @Override
    public String toString() {
        return "SysstatMeasurement [cpuLoad=" + cpuLoad + ", disk=" + disk + ", hostname=" + hostname + ", hugepages="
                + hugepages + ", io=" + io + ", kernel=" + kernel + ", memory=" + memory + ", network=" + network
                + ", numCpus=" + numCpus + ", paging=" + paging + ", processAndContextSwitch=" + processAndContextSwitch
                + ", psi=" + psi + ", queue=" + queue + ", swapPages=" + swapPages + "]";
    }
    public void clone(SysstatMeasurement sm) {
        this.hostname = sm.hostname;
        this.numCpus = sm.numCpus;
        for (int x = 0; x < sm.cpuLoad.size(); x++) {
            if (this.cpuLoad.size() <= x) {
                // should not happen - handle metrics registration in this case!
                LOG.warning("Number of CPUs different between cloned object and myself! " +
                            "Might need to update metrics trackers?");
                this.cpuLoad.set(x, new CpuLoad());
            }
            this.cpuLoad.get(x).clone(sm.cpuLoad.get(x));
        }
        this.processAndContextSwitch.clone(sm.processAndContextSwitch);
        this.swapPages.clone(sm.swapPages);
        this.paging.clone(sm.paging);
        this.io.clone(sm.io);
        this.memory.clone(sm.memory);
        this.hugepages.clone(sm.hugepages);
        this.kernel.clone(sm.kernel);
        this.queue.clone(sm.queue);
        for (int x = 0; x < sm.disk.size(); x++) {
            if (this.disk.size() <= x) {
                // should not happen - handle metrics registration in this case!
                LOG.warning("Number of disk devices different between cloned object and myself! " +
                            "Might need to update metrics trackers?");
                this.disk.set(x, new Disk());
            }
            this.disk.get(x).clone(sm.disk.get(x));
        }
        this.network.clone(sm.network);
        this.psi.clone(sm.psi);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CpuLoad {
        // NOTE: not including "user", which is "usr + guest"
        // NOTE: not including "system", which is "sys + irq + soft"
        @JsonProperty
        String cpu;
        @JsonProperty
        float usr;
        @JsonProperty
        float nice;
        @JsonProperty
        float sys;
        @JsonProperty
        float iowait;
        @JsonProperty
        float steal;
        @JsonProperty
        float irq;
        @JsonProperty
        float soft;
        @JsonProperty
        float guest;
        @JsonProperty
        float gnice;
        @JsonProperty
        float idle;
        public String getCpu() {
            return cpu;
        }
        public void setCpu(String cpu) {
            this.cpu = cpu;
        }
        public float getUsr() {
            return usr;
        }
        public void setUsr(float usr) {
            this.usr = usr;
        }
        public float getNice() {
            return nice;
        }
        public void setNice(float nice) {
            this.nice = nice;
        }
        public float getSys() {
            return sys;
        }
        public void setSys(float sys) {
            this.sys = sys;
        }
        public float getIowait() {
            return iowait;
        }
        public void setIowait(float iowait) {
            this.iowait = iowait;
        }
        public float getSteal() {
            return steal;
        }
        public void setSteal(float steal) {
            this.steal = steal;
        }
        public float getIrq() {
            return irq;
        }
        public void setIrq(float irq) {
            this.irq = irq;
        }
        public float getSoft() {
            return soft;
        }
        public void setSoft(float soft) {
            this.soft = soft;
        }
        public float getGuest() {
            return guest;
        }
        public void setGuest(float guest) {
            this.guest = guest;
        }
        public float getGnice() {
            return gnice;
        }
        public void setGnice(float gnice) {
            this.gnice = gnice;
        }
        public float getIdle() {
            return idle;
        }
        public void setIdle(float idle) {
            this.idle = idle;
        }
        @Override
        public String toString() {
            return "CpuLoad [cpu=" + cpu + ", gnice=" + gnice + ", guest=" + guest + ", idle=" + idle + ", iowait="
                    + iowait + ", irq=" + irq + ", nice=" + nice + ", soft=" + soft + ", steal=" + steal + ", sys="
                    + sys + ", usr=" + usr + "]";
        }
        public void clone(CpuLoad cl) {
            this.cpu = cl.cpu;
            this.usr = cl.usr;
            this.nice = cl.nice;
            this.sys = cl.sys;
            this.iowait = cl.iowait;
            this.steal = cl.steal;
            this.irq = cl.irq;
            this.soft = cl.soft;
            this.guest = cl.guest;
            this.gnice = cl.gnice;
            this.idle = cl.idle;
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProcessAndContextSwitch {
        @JsonProperty
        float proc;
        @JsonProperty
        float cswch;
        public float getProc() {
            return proc;
        }
        public void setProc(float proc) {
            this.proc = proc;
        }
        public float getCswch() {
            return cswch;
        }
        public void setCswch(float cswch) {
            this.cswch = cswch;
        }
        @Override
        public String toString() {
            return "ProcessAndContextSwitch [cswch=" + cswch + ", proc=" + proc + "]";
        }
        public void clone(ProcessAndContextSwitch pc) {
            this.proc = pc.proc;
            this.cswch = pc.cswch;
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SwapPages {
        @JsonProperty
        float pswpin;
        @JsonProperty
        float pswpout;
        public float getPswpin() {
            return pswpin;
        }
        public void setPswpin(float pswpin) {
            this.pswpin = pswpin;
        }
        public float getPswpout() {
            return pswpout;
        }
        public void setPswpout(float pswpout) {
            this.pswpout = pswpout;
        }
        @Override
        public String toString() {
            return "SwapPages [pswpin=" + pswpin + ", pswpout=" + pswpout + "]";
        }
        public void clone(SwapPages sp) {
            this.pswpin = sp.pswpin;
            this.pswpout = sp.pswpout;
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Paging {
        @JsonProperty
        float pgpgin;
        @JsonProperty
        float pgpgout;
        @JsonProperty
        float fault;
        @JsonProperty
        float majflt;
        @JsonProperty
        float pgfree;
        @JsonProperty
        float pgscank;
        @JsonProperty
        float pgscand;
        @JsonProperty
        float pgsteal;
        @JsonProperty("vmeff-percent")
        float vmeffPercent;
        public float getPgpgin() {
            return pgpgin;
        }
        public void setPgpgin(float pgpgin) {
            this.pgpgin = pgpgin;
        }
        public float getPgpgout() {
            return pgpgout;
        }
        public void setPgpgout(float pgpgout) {
            this.pgpgout = pgpgout;
        }
        public float getFault() {
            return fault;
        }
        public void setFault(float fault) {
            this.fault = fault;
        }
        public float getMajflt() {
            return majflt;
        }
        public void setMajflt(float majflt) {
            this.majflt = majflt;
        }
        public float getPgfree() {
            return pgfree;
        }
        public void setPgfree(float pgfree) {
            this.pgfree = pgfree;
        }
        public float getPgscank() {
            return pgscank;
        }
        public void setPgscank(float pgscank) {
            this.pgscank = pgscank;
        }
        public float getPgscand() {
            return pgscand;
        }
        public void setPgscand(float pgscand) {
            this.pgscand = pgscand;
        }
        public float getPgsteal() {
            return pgsteal;
        }
        public void setPgsteal(float pgsteal) {
            this.pgsteal = pgsteal;
        }
        public float getVmeffPercent() {
            return vmeffPercent;
        }
        public void setVmeffPercent(float vmeffPercent) {
            this.vmeffPercent = vmeffPercent;
        }
        @Override
        public String toString() {
            return "Paging [fault=" + fault + ", majflt=" + majflt + ", pgfree=" + pgfree + ", pgpgin=" + pgpgin
                    + ", pgpgout=" + pgpgout + ", pgscand=" + pgscand + ", pgscank=" + pgscank + ", pgsteal=" + pgsteal
                    + ", vmeffPercent=" + vmeffPercent + "]";
        }
        public void clone(Paging pg) {
            this.pgpgin = pg.pgpgin;
            this.pgpgout = pg.pgpgout;
            this.fault = pg.fault;
            this.majflt = pg.majflt;
            this.pgfree = pg.pgfree;
            this.pgscank = pg.pgscank;
            this.pgscand = pg.pgscand;
            this.pgsteal = pg.pgsteal;
            this.vmeffPercent = pg.vmeffPercent;
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Io {
        @JsonProperty
        float tps;
        @JsonProperty("io-reads")
        IoReads ioReads;
        @JsonProperty("io-writes")
        IoWrites ioWrites;
        @JsonProperty("io-discard")
        IoDiscard ioDiscard;
        public Io() {
            this.ioReads = new IoReads();
            this.ioWrites = new IoWrites();
            this.ioDiscard = new IoDiscard();
        }
        public float getTps() {
            return tps;
        }
        public void setTps(float tps) {
            this.tps = tps;
        }
        public IoReads getIoReads() {
            return ioReads;
        }
        public void setIoReads(IoReads ioReads) {
            this.ioReads = ioReads;
        }
        public IoWrites getIoWrites() {
            return ioWrites;
        }
        public void setIoWrites(IoWrites ioWrites) {
            this.ioWrites = ioWrites;
        }
        public IoDiscard getIoDiscard() {
            return ioDiscard;
        }
        public void setIoDiscard(IoDiscard ioDiscard) {
            this.ioDiscard = ioDiscard;
        }
        @Override
        public String toString() {
            return "Io [ioDiscard=" + ioDiscard + ", ioReads=" + ioReads + ", ioWrites=" + ioWrites + ", tps=" + tps
                    + "]";
        }
        public void clone(Io io) {
            this.tps = io.tps;
            this.ioReads.clone(io.ioReads);
            this.ioWrites.clone(io.ioWrites);
            this.ioDiscard.clone(io.ioDiscard);
        }
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class IoReads {
            @JsonProperty
            float rtps;
            @JsonProperty
            float bread;
            public float getRtps() {
                return rtps;
            }
            public void setRtps(float rtps) {
                this.rtps = rtps;
            }
            public float getBread() {
                return bread;
            }
            public void setBread(float bread) {
                this.bread = bread;
            }
            @Override
            public String toString() {
                return "IoReads [bread=" + bread + ", rtps=" + rtps + "]";
            }
            public void clone(IoReads ir) {
                this.rtps = ir.rtps;
                this.bread = ir.bread;
            }
        }
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class IoWrites {
            @JsonProperty
            float wtps;
            @JsonProperty
            float bwrtn;
            public float getWtps() {
                return wtps;
            }
            public void setWtps(float wtps) {
                this.wtps = wtps;
            }
            public float getBwrtn() {
                return bwrtn;
            }
            public void setBwrtn(float bwrtn) {
                this.bwrtn = bwrtn;
            }
            @Override
            public String toString() {
                return "IoWrites [bwrtn=" + bwrtn + ", wtps=" + wtps + "]";
            }
            public void clone(IoWrites iw) {
                this.wtps = iw.wtps;
                this.bwrtn = iw.bwrtn;
            }
        }
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class IoDiscard {
            @JsonProperty
            float dtps;
            @JsonProperty
            float bdscd;
            public float getDtps() {
                return dtps;
            }
            public void setDtps(float dtps) {
                this.dtps = dtps;
            }
            public float getBdscd() {
                return bdscd;
            }
            public void setBdscd(float bdscd) {
                this.bdscd = bdscd;
            }
            @Override
            public String toString() {
                return "IoDiscard [bdscd=" + bdscd + ", dtps=" + dtps + "]";
            }
            public void clone(IoDiscard id) {
                this.dtps = id.dtps;
                this.bdscd = id.bdscd;
            }
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Memory {
        @JsonProperty
        int memfree;
        @JsonProperty
        int avail;
        @JsonProperty
        int memused;
        @JsonProperty("memused-percent")
        float memusedPercent;
        @JsonProperty
        int buffers;
        @JsonProperty
        int cached;
        @JsonProperty
        int commit;
        @JsonProperty("commit-percent")
        float commitPercent;
        @JsonProperty
        int active;
        @JsonProperty
        int inactive;
        @JsonProperty
        int dirty;
        @JsonProperty
        int anonpg;
        @JsonProperty
        int slab;
        @JsonProperty
        int kstack;
        @JsonProperty
        int pgtbl;
        @JsonProperty
        int vmused;
        @JsonProperty
        int swpfree;
        @JsonProperty
        int swpused;
        @JsonProperty("swpused-percent")
        float swpusedPercent;
        @JsonProperty
        int swpcad;
        @JsonProperty("swpcad-percent")
        float swpcadPercent;
        public int getMemfree() {
            return memfree;
        }
        public void setMemfree(int memfree) {
            this.memfree = memfree;
        }
        public int getAvail() {
            return avail;
        }
        public void setAvail(int avail) {
            this.avail = avail;
        }
        public int getMemused() {
            return memused;
        }
        public void setMemused(int memused) {
            this.memused = memused;
        }
        public float getMemusedPercent() {
            return memusedPercent;
        }
        public void setMemusedPercent(float memusedPercent) {
            this.memusedPercent = memusedPercent;
        }
        public int getBuffers() {
            return buffers;
        }
        public void setBuffers(int buffers) {
            this.buffers = buffers;
        }
        public int getCached() {
            return cached;
        }
        public void setCached(int cached) {
            this.cached = cached;
        }
        public int getCommit() {
            return commit;
        }
        public void setCommit(int commit) {
            this.commit = commit;
        }
        public float getCommitPercent() {
            return commitPercent;
        }
        public void setCommitPercent(float commitPercent) {
            this.commitPercent = commitPercent;
        }
        public int getActive() {
            return active;
        }
        public void setActive(int active) {
            this.active = active;
        }
        public int getInactive() {
            return inactive;
        }
        public void setInactive(int inactive) {
            this.inactive = inactive;
        }
        public int getDirty() {
            return dirty;
        }
        public void setDirty(int dirty) {
            this.dirty = dirty;
        }
        public int getAnonpg() {
            return anonpg;
        }
        public void setAnonpg(int anonpg) {
            this.anonpg = anonpg;
        }
        public int getSlab() {
            return slab;
        }
        public void setSlab(int slab) {
            this.slab = slab;
        }
        public int getKstack() {
            return kstack;
        }
        public void setKstack(int kstack) {
            this.kstack = kstack;
        }
        public int getPgtbl() {
            return pgtbl;
        }
        public void setPgtbl(int pgtbl) {
            this.pgtbl = pgtbl;
        }
        public int getVmused() {
            return vmused;
        }
        public void setVmused(int vmused) {
            this.vmused = vmused;
        }
        public int getSwpfree() {
            return swpfree;
        }
        public void setSwpfree(int swpfree) {
            this.swpfree = swpfree;
        }
        public int getSwpused() {
            return swpused;
        }
        public void setSwpused(int swpused) {
            this.swpused = swpused;
        }
        public float getSwpusedPercent() {
            return swpusedPercent;
        }
        public void setSwpusedPercent(float swpusedPercent) {
            this.swpusedPercent = swpusedPercent;
        }
        public int getSwpcad() {
            return swpcad;
        }
        public void setSwpcad(int swpcad) {
            this.swpcad = swpcad;
        }
        public float getSwpcadPercent() {
            return swpcadPercent;
        }
        public void setSwpcadPercent(float swpcadPercent) {
            this.swpcadPercent = swpcadPercent;
        }
        @Override
        public String toString() {
            return "Memory [active=" + active + ", anonpg=" + anonpg + ", avail=" + avail + ", buffers=" + buffers
                    + ", cached=" + cached + ", commit=" + commit + ", commitPercent=" + commitPercent + ", dirty="
                    + dirty + ", inactive=" + inactive + ", kstack=" + kstack + ", memfree=" + memfree + ", memused="
                    + memused + ", memusedPercent=" + memusedPercent + ", pgtbl=" + pgtbl + ", slab=" + slab
                    + ", swpcad=" + swpcad + ", swpcadPercent=" + swpcadPercent + ", swpfree=" + swpfree + ", swpused="
                    + swpused + ", swpusedPercent=" + swpusedPercent + ", vmused=" + vmused + "]";
        }
        public void clone(Memory m) {
            this.memfree = m.memfree;
            this.avail = m.avail;
            this.memused = m.memused;
            this.memusedPercent = m.memusedPercent;
            this.buffers = m.buffers;
            this.cached = m.cached;
            this.commit = m.commit;
            this.commitPercent = m.commitPercent;
            this.active = m.active;
            this.inactive = m.inactive;
            this.dirty = m.dirty;
            this.anonpg = m.anonpg;
            this.slab = m.slab;
            this.kstack = m.kstack;
            this.pgtbl = m.pgtbl;
            this.vmused = m.vmused;
            this.swpfree = m.swpfree;
            this.swpused = m.swpused;
            this.swpusedPercent = m.swpusedPercent;
            this.swpcad = m.swpcad;
            this.swpcadPercent = m.swpcadPercent;
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Hugepages {
        @JsonProperty
        int hugfree;
        @JsonProperty
        int hugused;
        @JsonProperty("hugused-percent")
        float hugusedPercent;
        @JsonProperty
        int hugrsvd;
        @JsonProperty
        int hugsurp;
        public int getHugfree() {
            return hugfree;
        }
        public void setHugfree(int hugfree) {
            this.hugfree = hugfree;
        }
        public int getHugused() {
            return hugused;
        }
        public void setHugused(int hugused) {
            this.hugused = hugused;
        }
        public float getHugusedPercent() {
            return hugusedPercent;
        }
        public void setHugusedPercent(float hugusedPercent) {
            this.hugusedPercent = hugusedPercent;
        }
        public int getHugrsvd() {
            return hugrsvd;
        }
        public void setHugrsvd(int hugrsvd) {
            this.hugrsvd = hugrsvd;
        }
        public int getHugsurp() {
            return hugsurp;
        }
        public void setHugsurp(int hugsurp) {
            this.hugsurp = hugsurp;
        }
        @Override
        public String toString() {
            return "Hugepages [hugfree=" + hugfree + ", hugrsvd=" + hugrsvd + ", hugsurp=" + hugsurp + ", hugused="
                    + hugused + ", hugusedPercent=" + hugusedPercent + "]";
        }
        public void clone(Hugepages h) {
            this.hugfree = h.hugfree;
            this.hugused = h.hugused;
            this.hugusedPercent = h.hugusedPercent;
            this.hugrsvd = h.hugrsvd;
            this.hugsurp = h.hugsurp;
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Kernel {
        @JsonProperty
        int dentunusd;
        @JsonProperty("file-nr")
        int fileNr;
        @JsonProperty("inode-nr")
        int inodeNr;
        @JsonProperty("pty-nr")
        int ptyNr;
        public int getDentunusd() {
            return dentunusd;
        }
        public void setDentunusd(int dentunusd) {
            this.dentunusd = dentunusd;
        }
        public int getFileNr() {
            return fileNr;
        }
        public void setFileNr(int fileNr) {
            this.fileNr = fileNr;
        }
        public int getInodeNr() {
            return inodeNr;
        }
        public void setInodeNr(int inodeNr) {
            this.inodeNr = inodeNr;
        }
        public int getPtyNr() {
            return ptyNr;
        }
        public void setPtyNr(int ptyNr) {
            this.ptyNr = ptyNr;
        }
        @Override
        public String toString() {
            return "Kernel [dentunusd=" + dentunusd + ", fileNr=" + fileNr + ", inodeNr=" + inodeNr + ", ptyNr=" + ptyNr
                    + "]";
        }
        public void clone(Kernel k) {
            this.dentunusd = k.dentunusd;
            this.fileNr = k.fileNr;
            this.inodeNr = k.inodeNr;
            this.ptyNr = k.ptyNr;
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Queue {
        @JsonProperty("runq-sz")
        int runqSz;
        @JsonProperty("plist-sz")
        int plistSz;
        @JsonProperty("ldavg-1")
        float ldavg1;
        @JsonProperty("ldavg-5")
        float ldavg5;
        @JsonProperty("ldavg-15")
        float ldavg15;
        @JsonProperty
        int blocked;
        public int getRunqSz() {
            return runqSz;
        }
        public void setRunqSz(int runqSz) {
            this.runqSz = runqSz;
        }
        public int getPlistSz() {
            return plistSz;
        }
        public void setPlistSz(int plistSz) {
            this.plistSz = plistSz;
        }
        public float getLdavg1() {
            return ldavg1;
        }
        public void setLdavg1(float ldavg1) {
            this.ldavg1 = ldavg1;
        }
        public float getLdavg5() {
            return ldavg5;
        }
        public void setLdavg5(float ldavg5) {
            this.ldavg5 = ldavg5;
        }
        public float getLdavg15() {
            return ldavg15;
        }
        public void setLdavg15(float ldavg15) {
            this.ldavg15 = ldavg15;
        }
        public int getBlocked() {
            return blocked;
        }
        public void setBlocked(int blocked) {
            this.blocked = blocked;
        }
        @Override
        public String toString() {
            return "Queue [blocked=" + blocked + ", ldavg1=" + ldavg1 + ", ldavg15=" + ldavg15 + ", ldavg5=" + ldavg5
                    + ", plistSz=" + plistSz + ", runqSz=" + runqSz + "]";
        }
        public void clone(Queue q) {
            this.runqSz = q.runqSz;
            this.plistSz = q.plistSz;
            this.ldavg1 = q.ldavg1;
            this.ldavg5 = q.ldavg5;
            this.ldavg15 = q.ldavg15;
            this.blocked = q.blocked;
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Disk {
        @JsonProperty("disk-device")
        String diskDevice;
        @JsonProperty
        float tps;
        // This is in sectors. Nobody uses sectors.
        // @JsonProperty("rd_sec")
        // float rdSec;
        // @JsonProperty("wr_sec")
        // float wrSec;
        // @JsonProperty("dc_sec")
        // float dcSec;
        @JsonProperty
        float rkB;
        @JsonProperty
        float wkB;
        @JsonProperty
        float dkB;
        // This is in sectors. Nobody uses sectors.
        // @JsonProperty("avgrq-sz")
        // float avgrqSz;
        @JsonProperty("areq-sz")
        float areqSz;
        // This is in sectors. Nobody uses sectors.
        // @JsonProperty("avgqu-sz")
        // float avgquSz;
        @JsonProperty("aqu-sz")
        float aquSz;
        @JsonProperty
        float await;
        @JsonProperty("util-percent")
        float utilPercent;
        public String getDiskDevice() {
            return diskDevice;
        }
        public void setDiskDevice(String diskDevice) {
            this.diskDevice = diskDevice;
        }
        public float getTps() {
            return tps;
        }
        public void setTps(float tps) {
            this.tps = tps;
        }
        public float getRkB() {
            return rkB;
        }
        public void setRkB(float rkB) {
            this.rkB = rkB;
        }
        public float getWkB() {
            return wkB;
        }
        public void setWkB(float wkB) {
            this.wkB = wkB;
        }
        public float getDkB() {
            return dkB;
        }
        public void setDkB(float dkB) {
            this.dkB = dkB;
        }
        public float getAreqSz() {
            return areqSz;
        }
        public void setAreqSz(float areqSz) {
            this.areqSz = areqSz;
        }
        public float getAquSz() {
            return aquSz;
        }
        public void setAquSz(float aquSz) {
            this.aquSz = aquSz;
        }
        public float getAwait() {
            return await;
        }
        public void setAwait(float await) {
            this.await = await;
        }
        public float getUtilPercent() {
            return utilPercent;
        }
        public void setUtilPercent(float utilPercent) {
            this.utilPercent = utilPercent;
        }
        @Override
        public String toString() {
            return "Disk [diskDevice=" + diskDevice + ", aquSz=" + aquSz + ", areqSz=" + areqSz
                    + ", await=" + await + ", tps=" + tps + ", utilPercent=" + utilPercent
                    + ", dkB=" + dkB + ", rkB=" + rkB + ", wkB=" + wkB + "]";
        }
        public void clone(Disk d) {
            this.diskDevice = d.diskDevice;
            this.tps = d.tps;
            this.rkB = d.rkB;
            this.wkB = d.wkB;
            this.dkB = d.dkB;
            this.areqSz = d.areqSz;
            this.aquSz = d.aquSz;
            this.await = d.await;
            this.utilPercent = d.utilPercent;
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Network {
        @JsonProperty("net-dev")
        List<NetDev> netDev;
        @JsonProperty("net-edev")
        List<NetEDev> netEDev;
        @JsonProperty("net-nfs")
        NetNfs netNfs;
        @JsonProperty("net-nfsd")
        NetNfsd netNfsd;
        @JsonProperty("net-sock")
        NetSock netSock;
        @JsonProperty
        List<Softnet> softnet;
        public Network() {
            this.netDev = new ArrayList<>();
            this.netEDev = new ArrayList<>();
            this.netNfs = new NetNfs();
            this.netNfsd = new NetNfsd();
            this.netSock = new NetSock();
            this.softnet = new ArrayList<>();
        }
        public List<NetDev> getNetDev() {
            return netDev;
        }
        public void setNetDev(List<NetDev> netDev) {
            this.netDev = netDev;
        }
        public List<NetEDev> getNetEDev() {
            return netEDev;
        }
        public void setNetEDev(List<NetEDev> netEDev) {
            this.netEDev = netEDev;
        }
        public NetNfs getNetNfs() {
            return netNfs;
        }
        public void setNetNfs(NetNfs netNfs) {
            this.netNfs = netNfs;
        }
        public NetNfsd getNetNfsd() {
            return netNfsd;
        }
        public void setNetNfsd(NetNfsd netNfsd) {
            this.netNfsd = netNfsd;
        }
        public NetSock getNetSock() {
            return netSock;
        }
        public void setNetSock(NetSock netSock) {
            this.netSock = netSock;
        }
        public List<Softnet> getSoftnet() {
            return softnet;
        }
        public void setSoftnet(List<Softnet> softnet) {
            this.softnet = softnet;
        }
        @Override
        public String toString() {
            return "Network [netDev=" + netDev + ", netEDev=" + netEDev + ", netNfs=" + netNfs + ", netNfsd=" + netNfsd
                    + ", netSock=" + netSock + ", softnet=" + softnet + "]";
        }
        public void clone(Network n) {
            for (int x = 0; x < n.netDev.size(); x++) {
                if (this.netDev.size() <= x) {
                    // should not happen - handle metrics registration in this case!
                    LOG.warning("Number of network devices different between cloned object and myself! " +
                                "Might need to update metrics trackers?");
                    this.netDev.set(x, new NetDev());
                }
                this.netDev.get(x).clone(n.netDev.get(x));
            }   
            for (int x = 0; x < n.netEDev.size(); x++) {
                if (this.netEDev.size() <= x) {
                    // should not happen - handle metrics registration in this case!
                    LOG.warning("Number of network devices different between cloned object and myself! " +
                                "Might need to update metrics trackers?");
                    this.netEDev.set(x, new NetEDev());
                }
                this.netEDev.get(x).clone(n.netEDev.get(x));
            }   
            this.netNfs.clone(n.netNfs);
            this.netNfsd.clone(n.netNfsd);
            this.netSock.clone(n.netSock);
            for (int x = 0; x < n.softnet.size(); x++) {
                if (this.softnet.size() <= x) {
                    // should not happen - handle metrics registration in this case!
                    LOG.warning("Number of CPUs different between cloned object and myself! " +
                                "Might need to update metrics trackers?");
                    this.softnet.set(x, new Softnet());
                }
                this.softnet.get(x).clone(n.softnet.get(x));
            }   
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class NetDev {
            @JsonProperty
            String iface;
            @JsonProperty
            float rxpck;
            @JsonProperty
            float txpck;
            @JsonProperty
            float rxkB;
            @JsonProperty
            float txkB;
            @JsonProperty
            float rxcmp;
            @JsonProperty
            float txcmp;
            @JsonProperty
            float rxmcst;
            @JsonProperty("ifutil-percent")
            float ifutilPercent;
            public String getIface() {
                return iface;
            }
            public void setIface(String iface) {
                this.iface = iface;
            }
            public float getRxpck() {
                return rxpck;
            }
            public void setRxpck(float rxpck) {
                this.rxpck = rxpck;
            }
            public float getTxpck() {
                return txpck;
            }
            public void setTxpck(float txpck) {
                this.txpck = txpck;
            }
            public float getRxkB() {
                return rxkB;
            }
            public void setRxkB(float rxkB) {
                this.rxkB = rxkB;
            }
            public float getTxkB() {
                return txkB;
            }
            public void setTxkB(float txkB) {
                this.txkB = txkB;
            }
            public float getRxcmp() {
                return rxcmp;
            }
            public void setRxcmp(float rxcmp) {
                this.rxcmp = rxcmp;
            }
            public float getTxcmp() {
                return txcmp;
            }
            public void setTxcmp(float txcmp) {
                this.txcmp = txcmp;
            }
            public float getRxmcst() {
                return rxmcst;
            }
            public void setRxmcst(float rxmcst) {
                this.rxmcst = rxmcst;
            }
            public float getIfutilPercent() {
                return ifutilPercent;
            }
            public void setIfutilPercent(float ifutilPercent) {
                this.ifutilPercent = ifutilPercent;
            }
            @Override
            public String toString() {
                return "NetDev [iface=" + iface + ", ifutilPercent=" + ifutilPercent + ", rxcmp=" + rxcmp + ", rxkB="
                        + rxkB + ", rxmcst=" + rxmcst + ", rxpck=" + rxpck + ", txcmp=" + txcmp + ", txkB=" + txkB
                        + ", txpck=" + txpck + "]";
            }
            public void clone(NetDev nd) {
                this.iface = nd.iface;
                this.rxpck = nd.rxpck;
                this.txpck = nd.txpck;
                this.rxkB = nd.rxkB;
                this.txkB = nd.txkB;
                this.rxcmp = nd.rxcmp;
                this.txcmp = nd.txcmp;
                this.rxmcst = nd.rxmcst;
                this.ifutilPercent = nd.ifutilPercent;
            }
        }
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class NetEDev {
            @JsonProperty
            String iface;
            @JsonProperty
            float rxerr;
            @JsonProperty
            float txerr;
            @JsonProperty
            float coll;
            @JsonProperty
            float rxdrop;
            @JsonProperty
            float txdrop;
            @JsonProperty
            float txcarr;
            @JsonProperty
            float rxfram;
            @JsonProperty
            float rxfifo;
            @JsonProperty
            float txfifo;
            public String getIface() {
                return iface;
            }
            public void setIface(String iface) {
                this.iface = iface;
            }
            public float getRxerr() {
                return rxerr;
            }
            public void setRxerr(float rxerr) {
                this.rxerr = rxerr;
            }
            public float getTxerr() {
                return txerr;
            }
            public void setTxerr(float txerr) {
                this.txerr = txerr;
            }
            public float getColl() {
                return coll;
            }
            public void setColl(float coll) {
                this.coll = coll;
            }
            public float getRxdrop() {
                return rxdrop;
            }
            public void setRxdrop(float rxdrop) {
                this.rxdrop = rxdrop;
            }
            public float getTxdrop() {
                return txdrop;
            }
            public void setTxdrop(float txdrop) {
                this.txdrop = txdrop;
            }
            public float getTxcarr() {
                return txcarr;
            }
            public void setTxcarr(float txcarr) {
                this.txcarr = txcarr;
            }
            public float getRxfram() {
                return rxfram;
            }
            public void setRxfram(float rxfram) {
                this.rxfram = rxfram;
            }
            public float getRxfifo() {
                return rxfifo;
            }
            public void setRxfifo(float rxfifo) {
                this.rxfifo = rxfifo;
            }
            public float getTxfifo() {
                return txfifo;
            }
            public void setTxfifo(float txfifo) {
                this.txfifo = txfifo;
            }
            @Override
            public String toString() {
                return "NetEDev [coll=" + coll + ", iface=" + iface + ", rxdrop=" + rxdrop + ", rxerr=" + rxerr
                        + ", rxfifo=" + rxfifo + ", rxfram=" + rxfram + ", txcarr=" + txcarr + ", txdrop=" + txdrop
                        + ", txerr=" + txerr + ", txfifo=" + txfifo + "]";
            }
            public void clone(NetEDev ne) {
                this.iface = ne.iface;
                this.rxerr = ne.rxerr;
                this.txerr = ne.txerr;
                this.coll = ne.coll;
                this.rxdrop = ne.rxdrop;
                this.txdrop = ne.txdrop;
                this.txcarr = ne.txcarr;
                this.rxfram = ne.rxfram;
                this.rxfifo = ne.rxfifo;
                this.txfifo = ne.txfifo;
            }
        }
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class NetNfs {
            @JsonProperty
            float call;
            @JsonProperty
            float retrans;
            @JsonProperty
            float read;
            @JsonProperty
            float write;
            @JsonProperty
            float access;
            @JsonProperty
            float getatt;
            public float getCall() {
                return call;
            }
            public void setCall(float call) {
                this.call = call;
            }
            public float getRetrans() {
                return retrans;
            }
            public void setRetrans(float retrans) {
                this.retrans = retrans;
            }
            public float getRead() {
                return read;
            }
            public void setRead(float read) {
                this.read = read;
            }
            public float getWrite() {
                return write;
            }
            public void setWrite(float write) {
                this.write = write;
            }
            public float getAccess() {
                return access;
            }
            public void setAccess(float access) {
                this.access = access;
            }
            public float getGetatt() {
                return getatt;
            }
            public void setGetatt(float getatt) {
                this.getatt = getatt;
            }
            @Override
            public String toString() {
                return "NetNfs [access=" + access + ", call=" + call + ", getatt=" + getatt + ", read=" + read
                        + ", retrans=" + retrans + ", write=" + write + "]";
            }
            public void clone(NetNfs nn) {
                this.call = nn.call;
                this.retrans = nn.retrans;
                this.read = nn.read;
                this.write = nn.write;
                this.access = nn.access;
                this.getatt = nn.getatt;
            }
        }
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class NetNfsd {
            @JsonProperty
            float scall;
            @JsonProperty
            float badcall;
            @JsonProperty
            float packet;
            @JsonProperty
            float udp;
            @JsonProperty
            float tcp;
            @JsonProperty
            float hit;
            @JsonProperty
            float miss;
            @JsonProperty
            float sread;
            @JsonProperty
            float swrite;
            @JsonProperty
            float saccess;
            @JsonProperty
            float sgetatt;
            public float getScall() {
                return scall;
            }
            public void setScall(float scall) {
                this.scall = scall;
            }
            public float getBadcall() {
                return badcall;
            }
            public void setBadcall(float badcall) {
                this.badcall = badcall;
            }
            public float getPacket() {
                return packet;
            }
            public void setPacket(float packet) {
                this.packet = packet;
            }
            public float getUdp() {
                return udp;
            }
            public void setUdp(float udp) {
                this.udp = udp;
            }
            public float getTcp() {
                return tcp;
            }
            public void setTcp(float tcp) {
                this.tcp = tcp;
            }
            public float getHit() {
                return hit;
            }
            public void setHit(float hit) {
                this.hit = hit;
            }
            public float getMiss() {
                return miss;
            }
            public void setMiss(float miss) {
                this.miss = miss;
            }
            public float getSread() {
                return sread;
            }
            public void setSread(float sread) {
                this.sread = sread;
            }
            public float getSwrite() {
                return swrite;
            }
            public void setSwrite(float swrite) {
                this.swrite = swrite;
            }
            public float getSaccess() {
                return saccess;
            }
            public void setSaccess(float saccess) {
                this.saccess = saccess;
            }
            public float getSgetatt() {
                return sgetatt;
            }
            public void setSgetatt(float sgetatt) {
                this.sgetatt = sgetatt;
            }
            @Override
            public String toString() {
                return "NetNfsd [badcall=" + badcall + ", hit=" + hit + ", miss=" + miss + ", packet=" + packet
                        + ", saccess=" + saccess + ", scall=" + scall + ", sgetatt=" + sgetatt + ", sread=" + sread
                        + ", swrite=" + swrite + ", tcp=" + tcp + ", udp=" + udp + "]";
            }
            public void clone(NetNfsd nnd) {
                this.scall = nnd.scall;
                this.badcall = nnd.badcall;
                this.packet = nnd.packet;
                this.udp = nnd.udp;
                this.tcp = nnd.tcp;
                this.hit = nnd.hit;
                this.miss = nnd.miss;
                this.sread = nnd.sread;
                this.swrite = nnd.swrite;
                this.saccess = nnd.saccess;
                this.sgetatt = nnd.sgetatt;
            }
        }
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class NetSock {
            @JsonProperty
            int totsck;
            @JsonProperty
            int tcpsck;
            @JsonProperty
            int udpsck;
            @JsonProperty
            int rawsck;
            @JsonProperty("ip-frag")
            int ipFrag;
            @JsonProperty("tcp-tw")
            int tcpTw;
            public int getTotsck() {
                return totsck;
            }
            public void setTotsck(int totsck) {
                this.totsck = totsck;
            }
            public int getTcpsck() {
                return tcpsck;
            }
            public void setTcpsck(int tcpsck) {
                this.tcpsck = tcpsck;
            }
            public int getUdpsck() {
                return udpsck;
            }
            public void setUdpsck(int udpsck) {
                this.udpsck = udpsck;
            }
            public int getRawsck() {
                return rawsck;
            }
            public void setRawsck(int rawsck) {
                this.rawsck = rawsck;
            }
            public int getIpFrag() {
                return ipFrag;
            }
            public void setIpFrag(int ipFrag) {
                this.ipFrag = ipFrag;
            }
            public int getTcpTw() {
                return tcpTw;
            }
            public void setTcpTw(int tcpTw) {
                this.tcpTw = tcpTw;
            }
            @Override
            public String toString() {
                return "NetSock [ipFrag=" + ipFrag + ", rawsck=" + rawsck + ", tcpTw=" + tcpTw + ", tcpsck=" + tcpsck
                        + ", totsck=" + totsck + ", udpsck=" + udpsck + "]";
            }
            public void clone(NetSock ns) {
                this.totsck = ns.totsck;
                this.tcpsck = ns.tcpsck;
                this.udpsck = ns.udpsck;
                this.rawsck = ns.rawsck;
                this.ipFrag = ns.ipFrag;
                this.tcpTw = ns.tcpTw;
            }
        }
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Softnet {
            @JsonProperty
            String cpu;
            @JsonProperty
            float total;
            @JsonProperty
            float dropd;
            @JsonProperty
            float squeezd;
            @JsonProperty("rx_rps")
            float rxRps;
            @JsonProperty("flw_lim")
            float flwLim;
            public String getCpu() {
                return cpu;
            }
            public void setCpu(String cpu) {
                this.cpu = cpu;
            }
            public float getTotal() {
                return total;
            }
            public void setTotal(float total) {
                this.total = total;
            }
            public float getDropd() {
                return dropd;
            }
            public void setDropd(float dropd) {
                this.dropd = dropd;
            }
            public float getSqueezd() {
                return squeezd;
            }
            public void setSqueezd(float squeezd) {
                this.squeezd = squeezd;
            }
            public float getRxRps() {
                return rxRps;
            }
            public void setRxRps(float rxRps) {
                this.rxRps = rxRps;
            }
            public float getFlwLim() {
                return flwLim;
            }
            public void setFlwLim(float flwLim) {
                this.flwLim = flwLim;
            }
            @Override
            public String toString() {
                return "Softnet [cpu=" + cpu + ", dropd=" + dropd + ", flwLim=" + flwLim + ", rxRps=" + rxRps
                        + ", squeezd=" + squeezd + ", total=" + total + "]";
            }
            public void clone(Softnet s) {
                this.cpu = s.cpu;
                this.total = s.total;
                this.dropd = s.dropd;
                this.squeezd = s.squeezd;
                this.rxRps = s.rxRps;
                this.flwLim = s.flwLim;
            }
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Psi {
        @JsonProperty("psi-cpu")
        PsiCpu psiCpu;
        @JsonProperty("psi-io")
        PsiIoAndMem psiIo;
        @JsonProperty("psi-mem")
        PsiIoAndMem psiMem;
        public Psi() {
            this.psiCpu = new PsiCpu();
            this.psiIo = new PsiIoAndMem();
            this.psiMem = new PsiIoAndMem();
        }
        public PsiCpu getPsiCpu() {
            return psiCpu;
        }
        public void setPsiCpu(PsiCpu psiCpu) {
            this.psiCpu = psiCpu;
        }
        public PsiIoAndMem getPsiIo() {
            return psiIo;
        }
        public void setPsiIo(PsiIoAndMem psiIo) {
            this.psiIo = psiIo;
        }
        public PsiIoAndMem getPsiMem() {
            return psiMem;
        }
        public void setPsiMem(PsiIoAndMem psiMem) {
            this.psiMem = psiMem;
        }
        @Override
        public String toString() {
            return "Psi [psiCpu=" + psiCpu + ", psiIo=" + psiIo + ", psiMem=" + psiMem + "]";
        }
        public void clone(Psi p) {
            this.psiCpu.clone(p.psiCpu);
            this.psiIo.clone(p.psiIo);
            this.psiMem.clone(p.psiMem);
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class PsiCpu {
            @JsonProperty("some_avg10")
            float someAvg10;
            @JsonProperty("some_avg60")
            float someAvg60;
            @JsonProperty("some_avg300")
            float someAvg300;
            @JsonProperty("some_avg")
            float someAvg;
            public float getSomeAvg10() {
                return someAvg10;
            }
            public void setSomeAvg10(float someAvg10) {
                this.someAvg10 = someAvg10;
            }
            public float getSomeAvg60() {
                return someAvg60;
            }
            public void setSomeAvg60(float someAvg60) {
                this.someAvg60 = someAvg60;
            }
            public float getSomeAvg300() {
                return someAvg300;
            }
            public void setSomeAvg300(float someAvg300) {
                this.someAvg300 = someAvg300;
            }
            public float getSomeAvg() {
                return someAvg;
            }
            public void setSomeAvg(float someAvg) {
                this.someAvg = someAvg;
            }
            @Override
            public String toString() {
                return "PsiCpu [someAvg=" + someAvg + ", someAvg10=" + someAvg10 + ", someAvg300=" + someAvg300
                        + ", someAvg60=" + someAvg60 + "]";
            }
            public void clone(PsiCpu p) {
                this.someAvg10 = p.someAvg10;
                this.someAvg60 = p.someAvg60;
                this.someAvg300 = p.someAvg300;
                this.someAvg = p.someAvg;
            }
        }
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class PsiIoAndMem extends PsiCpu {
            @JsonProperty("full_avg10")
            float fullAvg10;
            @JsonProperty("full_avg60")
            float fullAvg60;
            @JsonProperty("full_avg300")
            float fullAvg300;
            @JsonProperty("full_avg")
            float fullAvg;
            public float getFullAvg10() {
                return fullAvg10;
            }
            public void setFullAvg10(float fullAvg10) {
                this.fullAvg10 = fullAvg10;
            }
            public float getFullAvg60() {
                return fullAvg60;
            }
            public void setFullAvg60(float fullAvg60) {
                this.fullAvg60 = fullAvg60;
            }
            public float getFullAvg300() {
                return fullAvg300;
            }
            public void setFullAvg300(float fullAvg300) {
                this.fullAvg300 = fullAvg300;
            }
            public float getFullAvg() {
                return fullAvg;
            }
            public void setFullAvg(float fullAvg) {
                this.fullAvg = fullAvg;
            }
            @Override
            public String toString() {
                return "PsiIoAndMem ["
                        + "someAvg=" + someAvg + ", someAvg10=" + someAvg10
                        + ", someAvg300=" + someAvg300 + ", someAvg60=" + someAvg60
                        + ", fullAvg=" + fullAvg + ", fullAvg10=" + fullAvg10
                        + ", fullAvg300=" + fullAvg300 + ", fullAvg60=" + fullAvg60 + "]";
            }
            public void clone(PsiIoAndMem p) {
                super.clone(p);
                this.fullAvg10 = p.fullAvg10;
                this.fullAvg60 = p.fullAvg60;
                this.fullAvg300 = p.fullAvg300;
                this.fullAvg = p.fullAvg;
            }
        }
    }
}
