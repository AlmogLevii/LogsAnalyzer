package Filtering;

import DataModels.Direction;
import DataModels.FirewallLog;
import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressSeqRange;
import inet.ipaddr.IPAddressString;

public class IpFilter extends FilterLog{

    final private IPAddressSeqRange ipRange;

    public IpFilter(String ipRange) {
        IPAddress iPAddress = new IPAddressString(ipRange).getAddress();
        IPAddress startIPAddress = iPAddress.getLower();
        IPAddress endIPAddress = iPAddress.getUpper();
        this.ipRange = startIPAddress.toSequentialRange(endIPAddress);
    }

    public IpFilter(String ipRange,boolean isInclude) {
        this(ipRange);
        this.includeFilter = isInclude;
    }

    @Override
    boolean process(FirewallLog log) {
        return this.ipRange.contains(new IPAddressString(log.getWorkerIp()).getAddress());
    }
}
