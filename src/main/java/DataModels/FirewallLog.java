package DataModels;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data
public class FirewallLog {
    Date date;
    String srcIp;
    String destIp;
    String user;
    String domain;
    Direction direction;

    public String getWorkerIp(){
        return this.getDirection().equals(Direction.INBOUND) ? this.getSrcIp() : this.getDestIp();
    }

    public String getCloudServiceIp(){
        return this.getDirection().equals(Direction.OUTBOUND) ? this.getSrcIp() : this.getDestIp();
    }
}
