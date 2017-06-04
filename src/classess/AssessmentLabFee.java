package classess;

/**
 * Created by dpunk12 on 6/5/2017.
 */
public class AssessmentLabFee {

    private String subjode;
    private String feecode;
    private String feedesc;
    private double subjlab;
    private double subjcredit;
    private String feedist;
    private double amt;

    public AssessmentLabFee(String subjode, String feecode, String feedesc, double subjlab,double subjcredit, String feedist, double amt) {
        this.subjode = subjode;
        this.feecode = feecode;
        this.feedesc = feedesc;
        this.subjlab = subjlab;
        this.subjcredit=subjcredit;
        this.feedist = feedist;
        this.amt = amt;
    }

    public String getFeecode(){
        if(feecode==null || feecode.equals(""))
            return "COMPFEE";
        return feecode;
    }

    public String getFeedesc(){
        if(feedesc==null || feedesc.equals(""))
            return "Computer Lab & Module";
        return feedesc;
    }

    public Double getAmt(){
        if(feedist.equals("SUBJECT"))
            return amt;
        if(feedist.equals("LAB HR"))
            return amt*subjcredit;
        if(feedist.equals("UNIT"))
            return amt*subjcredit;
        return null;
    }

}
