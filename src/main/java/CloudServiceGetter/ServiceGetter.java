package CloudServiceGetter;

import DataModels.ServiceInfo;

public interface ServiceGetter {
    ServiceInfo getServiceInfo(String domain);
}
