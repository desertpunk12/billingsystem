package models;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by dpunk12 on 5/3/2017.
 */
public class SchoolYear {

    private ArrayList<Sem> sems;
    private String schoolYear;

    public SchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
        sems = new ArrayList<>();
    }

    public ArrayList<Sem> getSems() {
        return sems;
    }

    public void setSems(ArrayList<Sem> sems) {
        this.sems = sems;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    public Sem getRecentSem(){
        return sems.get(sems.size()-1);
    }

}
