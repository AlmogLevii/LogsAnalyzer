package ReverseDNS;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class ReverseDNSService3Party implements ReverseDNSService {
    @Override
    public String getDomain(String ip) {
        String domain = "";
        try {
            domain = InetAddress.getByName(ip).getCanonicalHostName();
        } catch (UnknownHostException ex) {
            System.out.printf("Not able tp find domain according to this ip %s%n", ip);
            System.out.println(Arrays.toString(ex.getStackTrace()));
        }
        return domain;
    }
}
