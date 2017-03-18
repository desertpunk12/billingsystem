package classess;

import utils.ERR;

public class Student {

    private String studid, fname,lname,scholarship,course,yrlvl;


    public Student(String studid){
        this.studid = studid;
        getValues();


    }

    public void set(String[] vals){
	    this.studid = vals[0];
	    this.fname = vals[1];
	    this.lname = vals[2];
	    this.course = vals[3];
	    this.yrlvl = vals[4];
	    this.scholarship = vals[5];
    }

    private String[] getValues(){
        if(studid == null || studid.equals(""))
            return getValues(studid);
        else
            ERR.pr("studid is null or empty!");

        return null;
    }

    private String[] getValues(String studid){
        String qry = "SELECT * FROM srgb.students";//TODO: Needs to be aligned with the main DB


        return null;
    }

    public String getFullName(){
        return fname+" "+lname;
    }




}
