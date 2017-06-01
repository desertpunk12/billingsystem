package classess;

/**
 * Created by dpunk12 on 3/26/2017.
 */

public class AssessmentSubject {
    private String subject;
    private Double lec;
    private Double lab;
    private Double credunits;
    private String section;

    public AssessmentSubject(String subject, Double lec, Double lab, Double credunits, String section) {
        this.subject = subject;
        this.lec = lec;
        this.lab = lab;
        this.credunits = credunits;
        this.section = section;
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Double getLec() {
        return lec;
    }

    public void setLec(Double lec) {
        this.lec = lec;
    }

    public Double getLab() {
        return lab;
    }

    public void setLab(Double lab) {
        this.lab = lab;
    }

    public Double getCredunits() {
        return credunits;
    }

    public void setCredunits(Double credunits) {
        this.credunits = credunits;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    //</editor-fold>

}
