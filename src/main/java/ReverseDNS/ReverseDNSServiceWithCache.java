package ReverseDNS;

import java.util.HashMap;
import java.util.Map;

public class ReverseDNSServiceWithCache implements ReverseDNSService{
    private final Map<String,String> ipToDomainCache;
    private final ReverseDNSService thirdPartyDNSService;

    public ReverseDNSServiceWithCache(ReverseDNSService thirdPartyService) {
        this.thirdPartyDNSService  = thirdPartyService;
        this.ipToDomainCache = new HashMap<>();
    }

    @Override
    public String getDomain(String ip) {
        if(ipToDomainCache.containsKey(ip)) return ipToDomainCache.get(ip);

        //expensive operation
        String domain = thirdPartyDNSService.getDomain(ip);
        ipToDomainCache.put(ip,domain);

        return domain;
    }
}
