package net.p0f.openshift.metrics.exporter;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import net.p0f.openshift.metrics.model.SysstatMeasurement;

@ApplicationScoped
@Named("sysstatMetrics")
public class SysstatMetrics {
    static final Logger LOG = Logger.getLogger(SysstatMeasurement.class.getName());

    @Inject
    MeterRegistry mr;

    SysstatMeasurement lastMeasurement = null;

    public void processMetricRecord(SysstatMeasurement sm) {
        // Sanity check first.
        if (sm.getCpuLoad() == null ||
                sm.getDisk() == null ||
                sm.getHugepages() == null ||
                sm.getIo() == null ||
                sm.getKernel() == null ||
                sm.getMemory() == null ||
                sm.getNetwork() == null ||
                sm.getPaging() == null ||
                sm.getProcessAndContextSwitch() == null ||
                sm.getPsi() == null ||
                sm.getQueue() == null ||
                sm.getSwapPages() == null) {
            LOG.severe("Some of the sysstat measurement fields are null. Rejecting record.");
            throw new IllegalStateException("Some of the sysstat measurement fields are null. Rejecting record.");
        }

        LOG.fine("Updating sysstat metrics records...");

        if (this.lastMeasurement == null) {
            LOG.fine("Initialising sysstat metrics for " + sm.getHostname());

            this.lastMeasurement = sm;
            Tags tags = Tags.of("host", this.lastMeasurement.getHostname());

            for (SysstatMeasurement.CpuLoad cl : this.lastMeasurement.getCpuLoad()) {
                LOG.fine("Registering CPU metrics for CPU-" + cl.getCpu());
                Tags ctags = Tags.of("host", this.lastMeasurement.getHostname(), "cpu", cl.getCpu());
    
                Gauge.builder("sysstat.cpu.usr", cl::getUsr)
                        .tags(ctags)
                        .register(this.mr);
                Gauge.builder("sysstat.cpu.sys", cl::getSys)
                        .tags(ctags)
                        .register(this.mr);
                Gauge.builder("sysstat.cpu.nice", cl::getNice)
                        .tags(ctags)
                        .register(this.mr);
                Gauge.builder("sysstat.cpu.iowait", cl::getIowait)
                        .tags(ctags)
                        .register(this.mr);
                Gauge.builder("sysstat.cpu.steal", cl::getSteal)
                        .tags(ctags)
                        .register(this.mr);
                Gauge.builder("sysstat.cpu.irq", cl::getIrq)
                        .tags(ctags)
                        .register(this.mr);
                Gauge.builder("sysstat.cpu.soft", cl::getSoft)
                        .tags(ctags)
                        .register(this.mr);
                Gauge.builder("sysstat.cpu.guest", cl::getGuest)
                        .tags(ctags)
                        .register(this.mr);
                Gauge.builder("sysstat.cpu.gnice", cl::getGnice)
                        .tags(ctags)
                        .register(this.mr);
                Gauge.builder("sysstat.cpu.idle", cl::getIdle)
                            .tags(ctags)
                            .register(this.mr);
            }

            LOG.fine("Registering process and context switch metrics");
            SysstatMeasurement.ProcessAndContextSwitch pcs =
                        this.lastMeasurement.getProcessAndContextSwitch();
            Gauge.builder("sysstat.sched.ctxswitch", pcs::getCswch)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.sched.newproc", pcs::getProc)
                        .tags(tags)
                        .register(this.mr);

            LOG.fine("Registering swap metrics");
            SysstatMeasurement.SwapPages sp = this.lastMeasurement.getSwapPages();
            Gauge.builder("sysstat.vm.paging.pg.in", sp::getPswpin)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.vm.paging.pg.out", sp::getPswpout)
                        .tags(tags)
                        .register(this.mr);

            LOG.fine("Registering paging metrics");
            SysstatMeasurement.Paging pg = this.lastMeasurement.getPaging();
            Gauge.builder("sysstat.vm.paging.kb.in", pg::getPgpgin)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.vm.paging.kb.out", pg::getPgpgout)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.vm.fault.total", pg::getFault)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.vm.fault.major", pg::getMajflt)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.vm.pg.freed", pg::getPgfree)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.vm.pg.scan.kswapd", pg::getPgscank)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.vm.pg.scan.direct", pg::getPgscand)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.vm.pg.cache.reclaimed", pg::getPgsteal)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.vm.efficiency.pct", pg::getVmeffPercent)
                        .tags(tags)
                        .register(this.mr);

            LOG.fine("Registering I/O metrics");
            SysstatMeasurement.Io io = this.lastMeasurement.getIo();
            Gauge.builder("sysstat.io.tps.total", io::getTps)
                        .tags(tags)
                        .register(this.mr);
            SysstatMeasurement.Io.IoReads ir = io.getIoReads();
            Gauge.builder("sysstat.io.tps.read", ir::getRtps)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.io.blk.read", ir::getBread)
                        .tags(tags)
                        .register(this.mr);
            SysstatMeasurement.Io.IoWrites iw = io.getIoWrites();
            Gauge.builder("sysstat.io.tps.write", iw::getWtps)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.io.blk.write", iw::getBwrtn)
                        .tags(tags)
                        .register(this.mr);
            SysstatMeasurement.Io.IoDiscard id = io.getIoDiscard();
            Gauge.builder("sysstat.io.tps.discard", id::getDtps)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.io.blk.discard", id::getBdscd)
                        .tags(tags)
                        .register(this.mr);

            LOG.fine("Registering memory metrics");
            SysstatMeasurement.Memory m = this.lastMeasurement.getMemory();
            Gauge.builder("sysstat.mem.kb.free", m::getMemfree)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.mem.kb.avail", m::getAvail)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.mem.kb.used", m::getMemused)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.mem.pct.used", m::getMemusedPercent)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.mem.kb.buf", m::getBuffers)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.mem.kb.cache", m::getCached)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.mem.kb.commit", m::getCommit)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.mem.pct.commit", m::getCommitPercent)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.mem.kb.active", m::getActive)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.mem.kb.inactive", m::getInactive)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.mem.kb.dirty", m::getDirty)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.mem.kb.anonpg", m::getAnonpg)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.mem.kb.slab", m::getSlab)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.mem.kb.kstack", m::getKstack)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.mem.kb.pgtbl", m::getPgtbl)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.mem.kb.vmused", m::getVmused)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.mem.swap.kb.free", m::getSwpfree)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.mem.swap.kb.used", m::getSwpused)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.mem.swap.pct.used", m::getSwpusedPercent)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.mem.swap.kb.cached", m::getSwpcad)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.mem.swap.pct.cached", m::getSwpcadPercent)
                        .tags(tags)
                        .register(this.mr);

            LOG.fine("Registering hugepage metrics");
            SysstatMeasurement.Hugepages hp = this.lastMeasurement.getHugepages();
            Gauge.builder("sysstat.mem.huge.kb.free", hp::getHugfree)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.mem.huge.kb.used", hp::getHugused)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.mem.huge.pct.used", hp::getHugusedPercent)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.mem.huge.kb.reserved", hp::getHugrsvd)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.mem.huge.kb.surplus", hp::getHugsurp)
                        .tags(tags)
                        .register(this.mr);

            LOG.fine("Registering kernel metrics");
            SysstatMeasurement.Kernel k = this.lastMeasurement.getKernel();
            Gauge.builder("sysstat.kernel.dentry.unused", k::getDentunusd)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.kernel.nr.file", k::getFileNr)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.kernel.nr.inode", k::getInodeNr)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.kernel.nr.pty", k::getPtyNr)
                        .tags(tags)
                        .register(this.mr);

            LOG.fine("Registering scheduler metrics");
            SysstatMeasurement.Queue q = this.lastMeasurement.getQueue();
            Gauge.builder("sysstat.sched.sz.runq", q::getRunqSz)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.sched.sz.plist", q::getPlistSz)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.sched.load.1", q::getLdavg1)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.sched.load.5", q::getLdavg5)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.sched.load.15", q::getLdavg15)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.sched.sz.blocked", q::getBlocked)
                        .tags(tags)
                        .register(this.mr);

            for (SysstatMeasurement.Disk dsk : this.lastMeasurement.getDisk()) {
                LOG.fine("Registering blkdev metrics for " + dsk.getDiskDevice());
                Tags dtags = Tags.of("host", this.lastMeasurement.getHostname(), "blkdev", dsk.getDiskDevice());
                Gauge.builder("sysstat.io.dev.tps", dsk::getTps)
                            .tags(dtags)
                            .register(this.mr);
                Gauge.builder("sysstat.io.dev.read", dsk::getRkB)
                            .tags(dtags)
                            .register(this.mr);
                Gauge.builder("sysstat.io.dev.write", dsk::getWkB)
                            .tags(dtags)
                            .register(this.mr);
                Gauge.builder("sysstat.io.dev.discard", dsk::getDkB)
                            .tags(dtags)
                            .register(this.mr);
                Gauge.builder("sysstat.io.dev.queue.kb", dsk::getAreqSz)
                            .tags(dtags)
                            .register(this.mr);
                Gauge.builder("sysstat.io.dev.queue.req", dsk::getAquSz)
                            .tags(dtags)
                            .register(this.mr);
                Gauge.builder("sysstat.io.dev.queue.wait", dsk::getAwait)
                            .tags(dtags)
                            .register(this.mr);
                Gauge.builder("sysstat.io.dev.saturation", dsk::getUtilPercent)
                            .tags(dtags)
                            .register(this.mr);
            }

            LOG.fine("Registering network metrics");
            SysstatMeasurement.Network n = this.lastMeasurement.getNetwork();

            for (SysstatMeasurement.Network.NetDev nd : this.lastMeasurement.getNetwork().getNetDev()) {
                LOG.fine("Registering netdev metrics for interface " + nd.getIface());
                Tags ntags = Tags.of("host", this.lastMeasurement.getHostname(), "iface", nd.getIface());

                Gauge.builder("sysstat.net.if.pkt.recv", nd::getRxpck)
                            .tags(ntags)
                            .register(this.mr);
                Gauge.builder("sysstat.net.if.pkt.xmit", nd::getTxpck)
                            .tags(ntags)
                            .register(this.mr);
                Gauge.builder("sysstat.net.if.kb.recv", nd::getRxkB)
                            .tags(ntags)
                            .register(this.mr);
                Gauge.builder("sysstat.net.if.kb.xmit", nd::getTxkB)
                            .tags(ntags)
                            .register(this.mr);
                Gauge.builder("sysstat.net.if.pkt.compressed.recv", nd::getRxcmp)
                            .tags(ntags)
                            .register(this.mr);
                Gauge.builder("sysstat.net.if.pkt.compressed.xmit", nd::getTxcmp)
                            .tags(ntags)
                            .register(this.mr);
                Gauge.builder("sysstat.net.if.pkt.multicast.recv", nd::getRxmcst)
                            .tags(ntags)
                            .register(this.mr);
                Gauge.builder("sysstat.net.if.saturation", nd::getIfutilPercent)
                            .tags(ntags)
                            .register(this.mr);
            }

            for (SysstatMeasurement.Network.NetEDev ne : this.lastMeasurement.getNetwork().getNetEDev()) {
                LOG.fine("Registering error metrics for interface " + ne.getIface());
                Tags etags = Tags.of("host", this.lastMeasurement.getHostname(), "iface", ne.getIface());
                Gauge.builder("sysstat.net.if.err.recv.total", ne::getRxerr)
                            .tags(etags)
                            .register(this.mr);
                Gauge.builder("sysstat.net.if.err.xmit.total", ne::getTxerr)
                            .tags(etags)
                            .register(this.mr);
                Gauge.builder("sysstat.net.if.err.xmit.collision", ne::getColl)
                            .tags(etags)
                            .register(this.mr);
                Gauge.builder("sysstat.net.if.err.recv.drop", ne::getRxdrop)
                            .tags(etags)
                            .register(this.mr);
                Gauge.builder("sysstat.net.if.err.xmit.drop", ne::getTxdrop)
                            .tags(etags)
                            .register(this.mr);
                Gauge.builder("sysstat.net.if.err.xmit.carrier", ne::getTxcarr)
                            .tags(etags)
                            .register(this.mr);
                Gauge.builder("sysstat.net.if.err.recv.framing", ne::getRxfram)
                            .tags(etags)
                            .register(this.mr);
                Gauge.builder("sysstat.net.if.err.recv.fifo", ne::getRxfifo)
                            .tags(etags)
                            .register(this.mr);
                Gauge.builder("sysstat.net.if.err.xmit.fifo", ne::getTxfifo)
                            .tags(etags)
                            .register(this.mr);
            }

            SysstatMeasurement.Network.NetNfs nn = n.getNetNfs();
            Gauge.builder("sysstat.net.nfs.total", nn::getCall)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.net.nfs.retrans", nn::getRetrans)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.net.nfs.read", nn::getRead)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.net.nfs.write", nn::getWrite)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.net.nfs.access", nn::getAccess)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.net.nfs.getattr", nn::getGetatt)
                        .tags(tags)
                        .register(this.mr);

            SysstatMeasurement.Network.NetNfsd nd = n.getNetNfsd();
            Gauge.builder("sysstat.net.nfsd.total", nd::getScall)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.net.nfsd.error", nd::getBadcall)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.net.nfsd.packets.total", nd::getPacket)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.net.nfsd.packets.udp", nd::getUdp)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.net.nfsd.packets.tcp", nd::getTcp)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.net.nfsd.cache.hit", nd::getHit)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.net.nfsd.cache.miss", nd::getMiss)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.net.nfsd.op.read", nd::getSread)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.net.nfsd.op.write", nd::getSwrite)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.net.nfsd.op.access", nd::getSaccess)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.net.nfsd.op.getattr", nd::getSgetatt)
                        .tags(tags)
                        .register(this.mr);

            SysstatMeasurement.Network.NetSock ns = n.getNetSock();
            Gauge.builder("sysstat.net.sock.total", ns::getTotsck)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.net.sock.tcp", ns::getTcpsck)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.net.sock.udp", ns::getUdpsck)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.net.sock.raw", ns::getRawsck)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.net.ip.fragments", ns::getIpFrag)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.net.sock.timewait", ns::getTcpTw)
                        .tags(tags)
                        .register(this.mr);

            for (SysstatMeasurement.Network.Softnet sn : this.lastMeasurement.getNetwork().getSoftnet()) {
                LOG.fine("Registering network interrupts for CPU " + sn.getCpu());
                Tags stags = Tags.of("host", this.lastMeasurement.getHostname(), "cpu", sn.getCpu());
                Gauge.builder("sysstat.net.frames.total", sn::getTotal)
                            .tags(stags)
                            .register(this.mr);
                Gauge.builder("sysstat.net.frames.drop", sn::getDropd)
                            .tags(stags)
                            .register(this.mr);
                Gauge.builder("sysstat.net.irq.squeeze", sn::getSqueezd)
                            .tags(stags)
                            .register(this.mr);
                Gauge.builder("sysstat.net.irq.recv", sn::getRxRps)
                            .tags(stags)
                            .register(this.mr);
                Gauge.builder("sysstat.net.irq.flowlimit", sn::getFlwLim)
                            .tags(stags)
                            .register(this.mr);
            }

            LOG.fine("Registering pressure-stall metrics");
            SysstatMeasurement.Psi psi = this.lastMeasurement.getPsi();

            SysstatMeasurement.Psi.PsiCpu pc = psi.getPsiCpu();
            Gauge.builder("sysstat.pressure.cpu.some.10", pc::getSomeAvg10)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.pressure.cpu.some.60", pc::getSomeAvg60)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.pressure.cpu.some.300", pc::getSomeAvg300)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.pressure.cpu.some.fromlast", pc::getSomeAvg)
                        .tags(tags)
                        .register(this.mr);

            SysstatMeasurement.Psi.PsiIoAndMem pi = psi.getPsiIo();
            Gauge.builder("sysstat.pressure.io.some.10", pi::getSomeAvg10)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.pressure.io.some.60", pi::getSomeAvg60)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.pressure.io.some.300", pi::getSomeAvg300)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.pressure.io.some.fromlast", pi::getSomeAvg)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.pressure.io.all.10", pi::getFullAvg10)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.pressure.io.all.60", pi::getFullAvg60)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.pressure.io.all.300", pi::getFullAvg300)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.pressure.io.all.fromlast", pi::getFullAvg)
                        .tags(tags)
                        .register(this.mr);

            SysstatMeasurement.Psi.PsiIoAndMem pm = psi.getPsiMem();
            Gauge.builder("sysstat.pressure.mem.some.10", pm::getSomeAvg10)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.pressure.mem.some.60", pm::getSomeAvg60)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.pressure.mem.some.300", pm::getSomeAvg300)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.pressure.mem.some.fromlast", pm::getSomeAvg)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.pressure.mem.all.10", pm::getFullAvg10)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.pressure.mem.all.60", pm::getFullAvg60)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.pressure.mem.all.300", pm::getFullAvg300)
                        .tags(tags)
                        .register(this.mr);
            Gauge.builder("sysstat.pressure.mem.all.fromlast", pm::getFullAvg)
                        .tags(tags)
                        .register(this.mr);
        } else {
            // clone sm into lastMeasurement
            LOG.fine("Cloning new metric into existing one...");
            this.lastMeasurement.clone(sm);
        }
    }

}
