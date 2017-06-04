package models;

import java.sql.Date;

/**
 * Created by dpunk12 on 6/4/2017.
 */
public class Paid {

    private String orno;
    private String datepaid;
    private double amount;

    public Paid(String orno, String datepaid, double amount) {
        this.orno = orno;
        this.datepaid = datepaid;
        this.amount = amount;
    }

    public String getOrno() {
        return orno;
    }

    public void setOrno(String orno) {
        this.orno = orno;
    }

    public String getDatepaid() {
        return datepaid;
    }

    public void setDatepaid(String datepaid) {
        this.datepaid = datepaid;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
