package classess;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import utils.ERR;

public class Student {

    private String studid, fname,lname,scholarship,course,yrlvl;

    private String sy,sem,date;
    private AssessmentSubjects subjects;
    private AssessmentFees fees;

    private Double remainingBalance;

    public Student(String studid){
        this.studid = studid;
        fetchData();

        subjects = new AssessmentSubjects(studid);
        fees = new AssessmentFees(studid);
    }
    
    public JRBeanCollectionDataSource getSubjectsDataSource(){
        return subjects.getDataSource();
    }

    public JRBeanCollectionDataSource getFeesDataSource(){
        return fees.getDataSource();
    }


    public void setBasicInfo(String[] vals){
	    this.studid = vals[0];
	    this.fname = vals[1];
	    this.lname = vals[2];
	    this.course = vals[3];
	    this.yrlvl = vals[4];
	    this.scholarship = vals[5];
    }

    public void printBasicInfo(){
        System.out.printf("Student ID: %s\nName: %s\nCourse: %s\nYear Level: %s\nScholarship: %s\n",studid,fname+" "+lname,course,yrlvl,scholarship);
    }

    private void fetchData(){
        if(studid == null || studid.equals(""))
            ERR.pr("studid is null or empty!");

        fetchData(studid);

    }

    private void fetchData(String studid){
        String qry = "SELECT * FROM srgb.students";//TODO: Needs to be aligned with the main DB


        //TODO: remove this latur
        setBasicInfo(new String[]{studid, "Pete Christian", "Reyes", "BSIT", "IV", "Faculty Dependent"});
        sem = "Term 1";
        sy = "2016 - 2017";
        date = "8/3/2016";
        remainingBalance = 0.00D;
        printBasicInfo();


    }

    //<editor-fold defaultstate="collapsed" desc="Getters" >
    public String getFullName(){
        return fname+" "+lname;
    }

    public String getStudId(){
        return studid;
    }

    public String getScholarship(){
        return scholarship;
    }

    public String getCourse(){
        return course;
    }

    public String getYrlvl(){
        return yrlvl;
    }

    public String getSy(){
        return sy;
    }

    public String getSem(){
        return sem;
    }

    public String getDate(){
        return date;
    }

    public Double getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(Double remainingBalance) {
        this.remainingBalance = remainingBalance;
    }
    //</editor-fold>

}
