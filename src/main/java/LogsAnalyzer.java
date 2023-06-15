import CloudServiceGetter.ServiceGetter;
import DataModels.Direction;
import DataModels.FirewallLog;
import Filtering.FilterLog;
import ReverseDNS.ReverseDNSService;
import lombok.val;

import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.stream.Stream;

public class LogsAnalyzer implements Runnable {

    private final Map<String, Set<String>> cloudServiceToIps;
    private final Stream<String> streamLogs;
    private final FilterLog filters;
    private final ReverseDNSService reverseDNS;
    private final ServiceGetter serviceGetter;
    private final Writer output;


    //TODO: wrap the parameters with object called LogsAnalyzerParams
    public LogsAnalyzer(Stream<String> streamLogs, FilterLog filters, ReverseDNSService reverseDNS, ServiceGetter serviceGetter, Writer output) {
        cloudServiceToIps = new HashMap<>();
        this.streamLogs = streamLogs;
        this.filters = filters;
        this.reverseDNS = reverseDNS;
        this.serviceGetter = serviceGetter;
        this.output = output;
    }

    @Override
    public void run() {
        Stream<FirewallLog> filteredLogs = streamLogs.map(LogsAnalyzer::parseLog).filter(filters::filter);
        filteredLogs.forEach(this::updateMemo);
        createOutput();
    }

    private static FirewallLog parseLog(String stringLog) {
        //TODO: parse properly using regex grouping
        return new FirewallLog(null, "SRC", "DEST", "USER", "DOMAIN", Direction.INBOUND);
    }

    private void updateMemo(FirewallLog log) {
        val workerIp = log.getWorkerIp();
        val domain = log.getDomain().isEmpty() ? reverseDNS.getDomain(log.getCloudServiceIp()) : log.getDomain();
        val serviceInfo = serviceGetter.getServiceInfo(domain);
        val serviceName = serviceInfo.getName();

        Set<String> ips = cloudServiceToIps.getOrDefault(serviceName, new HashSet<>());
        ips.add(workerIp);
        cloudServiceToIps.put(serviceName, ips);
    }

    private void createOutput() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Set<String>> entry : cloudServiceToIps.entrySet()) {
            val serviceName = entry.getKey();
            val ips = entry.getValue();
            String ipsFormat = String.join(", ", ips);
            sb.append(String.format("%s: %s\n", serviceName, ipsFormat));
            try {
                output.write(sb.toString());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public Writer getOutput() {
        return this.output;
    }
}
