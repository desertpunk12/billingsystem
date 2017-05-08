package models;

/**
 * Created by dpunk12 on 5/3/2017.
 */
public class Student implements Model{

    private Assessment assessment;
    private SummaryAssessment summaryAssessment;
    private Permit permit;
    private Clearance clearance;


    @Override
    public void fetch() {
    }



}
