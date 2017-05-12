package realmstudy.data;

import java.util.List;

/**
 * Created by developer on 17/4/17.
 */
public class OverAdapterData {
    public int getOver() {
        return over;
    }

    public void setOver(int over) {
        this.over = over;
    }

    public int getTotal_run() {
        return total_run;
    }

    public void setTotal_run(int total_run) {
        this.total_run = total_run;
    }

    public List<String> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List<String> deliveries) {
        this.deliveries = deliveries;
    }

    public String getBatsmans() {
        return batsmans;
    }

    public void setBatsmans(String batsmans) {
        this.batsmans = batsmans;
    }

    public String getBolwers() {
        return bolwers;
    }

    public void setBolwers(String bolwers) {
        this.bolwers = bolwers;
    }

    int over,total_run;
    List<String> deliveries;
    String batsmans;
    String bolwers;
}
