package CloudServiceGetter;

import DataModels.ServiceInfo;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.Cleanup;
import lombok.val;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class ServiceGetterFromCSV implements ServiceGetter{

    private final Map<String, ServiceInfo> domainToServiceInfo;
    private final CSVParser csvParser;

    public ServiceGetterFromCSV(Reader reader) throws IOException {
        csvParser = new CSVParserBuilder().withSeparator(',').build();
        @Cleanup CSVReader readerCsv = new CSVReaderBuilder(reader).withCSVParser(csvParser).withSkipLines(1).build();
        this.domainToServiceInfo = convertCsvToMap(readerCsv);
    }

    private Map<String, ServiceInfo> convertCsvToMap(CSVReader csvReader) throws IOException {
        Map<String,ServiceInfo> map = new HashMap<>();
        val data = csvReader.readAll();
        for (String[] service : data) {
            val name = service[0];
            val domain = service[1];
            val risk = service[2];
            val country = service[3];
            val gdpr = service[4].equals("Yes");
            val serviceEntity = new ServiceInfo(name, domain, risk, country, gdpr);
            map.put(domain, serviceEntity);
        }

        return map;
    }

    @Override
    public ServiceInfo getServiceInfo(String domain) {
        //TODO: handle if not exist
        return domainToServiceInfo.get(domain);
    }
}
