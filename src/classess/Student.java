package classess;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.CharPredicateCache;
import utils.DB;
import utils.ERR;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Student {

    private String studid, fname,lname,mname,scholarship,course;
    private int yrlvl;
    private boolean nightclass;

    private String sy;
    private char sem;
    private AssessmentSubjects subjects;
    private AssessmentFees fees;

    private double remainingBalance;
    private double totalAssessment;

    public Student(String studid,String sy,char sem,double remainingBalance, double totalAssessment){
        this.studid = studid;
        this.sy = sy;
        this.sem = sem;
        this.remainingBalance = remainingBalance;
        this.totalAssessment = totalAssessment;
        fetchData();

        subjects = new AssessmentSubjects(studid,sy,sem);
        fees = new AssessmentFees(studid,sy,sem);
    }
    
    public JRBeanCollectionDataSource getSubjectsDataSource(){
        return subjects.getDataSource();
    }

    public JRBeanCollectionDataSource getFeesDataSource(){
        return fees.getDataSource();
    }

    public String getGenDate(){
        return fees.getGendate();
    }

    public void setBasicInfo(String[] vals){
	    this.studid = vals[0];
	    this.fname = vals[1];
        this.mname = vals[2];
	    this.lname = vals[3];
	    this.course = vals[4];
	    this.yrlvl = Integer.parseInt(vals[5]);
	    this.scholarship = vals[6];
    }

    public void printBasicInfo(){
        System.out.printf("Student ID: %s\nName: %s\nCourse: %s\nYear Level: %s\nScholarship: %s\n",studid,getFullName(),course,yrlvl,scholarship);
    }

    private void fetchData(){
        if(studid == null || studid.equals(""))
            ERR.pr("studid is null or empty!");

        try {
            fetchData(studid);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void fetchData(String studid) throws SQLException{
        String query = "SELECT studlastname,studmidname,studfirstname,studlevel,studmajor,nightclass,schdesc FROM\n" +
                "  srgb.semstudent ss JOIN srgb.student USING(studid) JOIN srgb.scholar ON (schcode=scholarstatus)\n" +
                "  WHERE studid='"+studid+"' AND sy='"+sy+"' AND sem='"+sem+"';";

        System.out.println(query);
        ResultSet rs = DB.query(query);
        if(!rs.next()){
            System.out.println("Error student id cannot be found!!!!!!!!!");
            return;
        }
        this.lname = rs.getString(1);
        this.mname = rs.getString(2);
        this.fname = rs.getString(3);
        this.yrlvl = rs.getInt(4);
        this.course = rs.getString(5);
        this.nightclass = rs.getBoolean(6);
        this.scholarship = rs.getString(7);

        //TODO: remove this latur
//        setBasicInfo(new String[]{studid, "Pete Christian","Cabungcag", "Reyes", "BSIT", "IV", "Faculty Dependent"});
//        remainingBalance = 0.00D;
//        printBasicInfo();


    }



    //<editor-fold defaultstate="collapsed" desc="Getters" >
    public boolean isNightClass(){
        return nightclass;
    }

    public String getFullName(){
        return fname+" "+mname.toUpperCase().charAt(0)+". "+lname;
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

    public Integer getYrlvl(){
        return yrlvl;
    }

    public String getSy(){
        return sy;
    }

    public char getSem(){
        return sem;
    }

    public Double getRemainingBalance() {
        return remainingBalance;
    }

    public Double getTotalAssessment(){
        return totalAssessment;
    }

    public void setRemainingBalance(Double remainingBalance) {
        this.remainingBalance = remainingBalance;
    }
    //</editor-fold>

}
