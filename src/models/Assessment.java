package models;

/**
 * Created by dpunk12 on 5/3/2017.
 */
public class Assessment implements Model{

    private Subject[] subjects;
    private Fee[] fees;
    private double remainingBalance;

    @Override
    public void fetch() {
    }

}
