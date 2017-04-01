package classess;


public class AssesssmentView {

    private String studname;
    private String yrandcourse;
    private String currentDate;
    private String semsy;

    private String[][] subjects;
    private String[][] fees;

    private AssessmentVars previous;
    private AssessmentVars current;

    public AssesssmentView() {

    }

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public String getStudname() {
        return studname;
    }

    public void setStudname(String studname) {
        this.studname = studname;
    }

    public String getYrandcourse() {
        return yrandcourse;
    }

    public void setYrandcourse(String yrandcourse) {
        this.yrandcourse = yrandcourse;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getSemsy() {
        return semsy;
    }

    public void setSemsy(String semsy) {
        this.semsy = semsy;
    }

    public String[][] getSubjects() {
        return subjects;
    }

    public void setSubjects(String[][] subjects) {
        this.subjects = subjects;
    }

    public String[][] getFees() {
        return fees;
    }

    public void setFees(String[][] fees) {
        this.fees = fees;
    }

    //</editor-fold>
}
