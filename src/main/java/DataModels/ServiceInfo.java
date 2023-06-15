package DataModels;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServiceInfo {
    String name;
    String domain;
    String risk;
    String country;
    boolean gdpr;
}
