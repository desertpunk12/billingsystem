package models;

/**
 * Created by dpunk12 on 5/3/2017.
 */
public class Fee {

    private String feecode;
    private String feedesc;
    private String amt;

    public String getFeecode() {
        return feecode;
    }

    public void setFeecode(String feecode) {
        this.feecode = feecode;
    }

    public String getFeedesc() {
        return feedesc;
    }

    public void setFeedesc(String feedesc) {
        this.feedesc = feedesc;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public Fee(String feecode, String feedesc, String amt) {

        this.feecode = feecode;
        this.feedesc = feedesc;
        this.amt = amt;
    }
}
