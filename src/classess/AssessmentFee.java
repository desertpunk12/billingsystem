package classess;

/**
 * Created by dpunk12 on 3/26/2017.
 */
public class AssessmentFee{
    private String fee;
    private Double amount;

    AssessmentFee(String fee, double amount) {
        this.fee = fee;
        this.amount = amount;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

}
