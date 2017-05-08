package models;

/**
 * Created by dpunk12 on 5/3/2017.
 */
public class SchoolYear implements Model{

    private Sem[] sems;
    private String schoolYear;

    public SchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    @Override
    public void fetch() {

    }
}
