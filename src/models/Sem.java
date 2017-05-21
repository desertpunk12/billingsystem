package models;

/**
 * Created by dpunk12 on 5/3/2017.
 */
public class Sem {

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

    public double getTotalPayed() {
        return totalPayed;
    }

    public void setTotalPayed(double totalPayed) {
        this.totalPayed = totalPayed;
    }

    private char sem;
    private double remainingBalance;
    private double totalPayed;

    public Sem(char sem, double remainingBalance, double totalPayed) {
        this.sem = sem;
        this.remainingBalance = remainingBalance;
        this.totalPayed = totalPayed;
    }
}
