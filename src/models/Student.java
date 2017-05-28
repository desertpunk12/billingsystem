package models;

/**
 * Created by dpunk12 on 5/3/2017.
 */
public class Student {

    private SummaryAssessment summaryAssessment;//checked DONE???

    //Reports
    private Assessment assessment;
    private Permit permit;
    private Clearance clearance;

    private String firstName;
    private String middleName;
    private String lastName;

    private String idNumber;


    public String getFullName(){
        return firstName+" "+middleName.toUpperCase().charAt(0)+" "+lastName;
    }

    public String getIdNumber(){
        return idNumber;
    }


}
