package ReverseDNS;

import java.util.HashMap;
import java.util.Map;

public class ReverseDNSSingelton implements ReverseDNSService {
    private final Map<String, String> ipToDomainCache;
    private final ReverseDNSService thirdPartyDNSService;

    private static ReverseDNSSingelton instance;

    public static ReverseDNSSingelton getInstance(ReverseDNSService thirdPartyService) {
        if (ReverseDNSSingelton.instance == null) {
            synchronized (instance) {
                if (ReverseDNSSingelton.instance == null) {
                    instance = new ReverseDNSSingelton(thirdPartyService);
                }
            }
        }

        return ReverseDNSSingelton.instance;
    }

    private ReverseDNSSingelton(ReverseDNSService thirdPartyService) {
        this.thirdPartyDNSService = thirdPartyService;
        this.ipToDomainCache = new HashMap<>();
    }

    @Override
    public String getDomain(String ip) {
        if (ipToDomainCache.containsKey(ip)) return ipToDomainCache.get(ip);

        //expensive operation
        String domain = thirdPartyDNSService.getDomain(ip);
        ipToDomainCache.put(ip, domain);

        return domain;
    }
}
