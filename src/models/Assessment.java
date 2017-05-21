package models;

import java.util.ArrayList;

/**
 * Created by dpunk12 on 5/3/2017.
 */
public class Assessment {

    private String name;
    private String studid;
    private String yrLevel;
    private String course;
    private String date;
    private String sy;
    private char sem;
    private String scolarship;

    private ArrayList<Subject> subjects;
    private ArrayList<Fee> fees;
    private double remainingBalance;
    private double totlaAssess;


    public Assessment(String studid, String sy, char sem){
        this.studid = studid;
        this.sy = sy;
        this.sem = sem;

        subjects = new ArrayList<>();
        fees = new ArrayList<>();

    }

    private void fetchSubjects(){
        String query = "SELECT subject.subjcode,subjlec_units,subjlab_units, CAST(subjcredit as REAL) as subjcred_units,\n" +
                "  srgb.unitsload('"+studid+"','"+sy+"','"+sem+"'), srgb.registration.section from srgb.subject, srgb.registration\n" +
                "where srgb.registration.subjcode=subject.subjcode AND studid='"+studid+"' and sy='"+sy+"' and srgb.registration.sem='"+sem+"';";
        System.out.println(query);
    }

    private void fetchFees(){
        String query = "";

    }

}
