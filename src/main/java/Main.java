import CloudServiceGetter.ServiceGetter;
import CloudServiceGetter.ServiceGetterFromCSV;
import Filtering.CompositeFilterLog;
import Filtering.FilterLog;
import Filtering.IpFilter;
import Filtering.UserFilter;
import ReverseDNS.ReverseDNSService;
import ReverseDNS.ReverseDNSService3Party;
import ReverseDNS.ReverseDNSSingelton;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws IOException {
        String logFileName = "firewall.log";
        String DBFileName = "ServiceDBv1.csv";

        Stream<String> logs = Files.lines(readFileToPath(logFileName));

        CompositeFilterLog userFilters = new CompositeFilterLog(new UserFilter("almog"), new UserFilter("almog", /* includeFilter= */ false));
        FilterLog ipFilter = new IpFilter("1.1.1.1/24");
        FilterLog filters = new CompositeFilterLog(userFilters, ipFilter);

        //when we would use concurrency it will help as
        ReverseDNSService reverseDNS = ReverseDNSSingelton.getInstance(new ReverseDNSService3Party());

        Path DBPath = readFileToPath(DBFileName);
        BufferedReader csvReader = new BufferedReader(new FileReader(DBPath.toFile()));
        ServiceGetter cloudServiceGetter = new ServiceGetterFromCSV(csvReader);

        Writer writer = new PrintWriter(System.out);

        LogsAnalyzer analyzer = new LogsAnalyzer(logs, filters, reverseDNS, cloudServiceGetter, writer);
        analyzer.run();
        Writer output = analyzer.getOutput();
    }

    public static Path readFileToPath(String filePath) {
        return Paths.get(Objects.requireNonNull(Main.class.getClassLoader().getResource(filePath)).getPath());
    }
}