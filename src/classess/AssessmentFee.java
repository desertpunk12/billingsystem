package classess;

import java.sql.Date;

/**
 * Created by dpunk12 on 3/26/2017.
 */
public class AssessmentFee{

    private String code;
    private String desc;
    private Double amount;

    public AssessmentFee(String code, double amount, String desc) {
        this.code = code;
        this.desc = desc;
        this.amount = amount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

}
