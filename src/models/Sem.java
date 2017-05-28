package models;

import utils.DB;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by dpunk12 on 5/3/2017.
 */
public class Sem {

    private String studid;
    private String sy;
    private char sem;
    private double remainingBalance;
    private double totalAssessment;


    private ArrayList<String> paidOrnos;
    private ArrayList<Date> paidPaydates;
    private ArrayList<Double> paidAmounts;

    public Sem(String studid,String sy,char sem, double remainingBalance, double totalAssessment) throws SQLException {
        this.studid=studid;
        this.sy=sy;
        this.sem = sem;
        this.remainingBalance = remainingBalance;
        this.totalAssessment = totalAssessment;

        paidOrnos = new ArrayList<>();
        paidPaydates = new ArrayList<>();
        paidAmounts = new ArrayList<>();

        fetchPaids();
    }

    private void fetchPaids() throws SQLException{
        String query = "SELECT orno,sum(amt) AS  payed,paydate FROM srgb.collection_details JOIN srgb.collection_header USING(orno)\n" +
                "WHERE studid='"+studid+"' AND sy='"+sy+"' AND sem='"+sem+"' GROUP BY orno,sy,sem,paydate ORDER BY sy,sem;";
        System.out.println(query);
        ResultSet rs = DB.query(query);
        while (rs.next()){
            paidOrnos.add(rs.getString(1));
            paidAmounts.add(rs.getDouble(2));
            paidPaydates.add(rs.getDate(3));
        }
    }

    public double getTotalAmountPaid(){
        double total = 0;
        for (double paid: paidAmounts)
            total+=paid;
        return total;
    }

    public ArrayList<String> getPaidOrnos() {
        return paidOrnos;
    }

    public ArrayList<Date> getPaidPaydates() {
        return paidPaydates;
    }

    public ArrayList<Double> getPaidAmounts() {
        return paidAmounts;
    }

    public char getSem() {
        return sem;
    }

    public void setSem(char sem) {
        this.sem = sem;
    }

    public double getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(double remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public double getTotalAssessment() {
        return totalAssessment;
    }

    public void setTotalAssessment(double totalAssessment) {
        this.totalAssessment = totalAssessment;
    }

}
