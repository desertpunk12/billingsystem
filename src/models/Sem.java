package models;

/**
 * Created by dpunk12 on 5/3/2017.
 */
public class Sem {

    private char sem;
    private double remainingBalance;
    private double totalPayed;

    public Sem(char sem, double remainingBalance, double totalPayed) {
        this.sem = sem;
        this.remainingBalance = remainingBalance;
        this.totalPayed = totalPayed;
    }
}
