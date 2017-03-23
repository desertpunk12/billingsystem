package classess;

import utils.ERR;

import javax.swing.*;

public class Student {

    private String studid, fname,lname,scholarship,course,yrlvl;


    public Student(String studid){
        this.studid = studid;
        System.out.println("Wingo "+studid);
        getValues();


    }

    public void setBasicInfo(String[] vals){
	    this.studid = vals[0];
	    this.fname = vals[1];
	    this.lname = vals[2];
	    this.course = vals[3];
	    this.yrlvl = vals[4];
	    this.scholarship = vals[5];
    }

    public void printValuesToConsole(){
        System.out.printf("Student ID: %s\nName: %s\nCourse: %s\nYear Level: %s\nScholarship: %s",studid,fname+" "+lname,course,yrlvl,scholarship);
    }

    private String[] getValues(){
        if(studid == null || studid.equals(""))
            ERR.pr("studid is null or empty!");
        else
            return getValues(studid);

        return null;
    }

    private String[] getValues(String studid){
        String qry = "SELECT * FROM srgb.students";//TODO: Needs to be aligned with the main DB


        return null;
    }

    public void setTextFields(JLabel name, JLabel course, JLabel yrlvl, JLabel scholarship){

        SwingUtilities.invokeLater(()->{

        });



    }

    public String getFullName(){
        return fname+" "+lname;
    }




}
