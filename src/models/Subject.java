package models;

/**
 * Created by dpunk12 on 5/3/2017.
 */
public class Subject {

    private String subjcode;
    private String subjlecUnits;
    private String subjlabUnits;
    private String subjcredUnits;
    private String subjSection;

    public Subject(String subjcode, String subjlecUnits, String subjlabUnits, String subjcredUnits,String subjSection) {
        this.subjcode = subjcode;
        this.subjlecUnits = subjlecUnits;
        this.subjlabUnits = subjlabUnits;
        this.subjcredUnits = subjcredUnits;
        this.subjSection = subjSection;
    }

    public String getSubjSection() {
        return subjSection;
    }

    public void setSubjSection(String subjSection) {
        this.subjSection = subjSection;
    }

    public String getSubjcode() {
        return subjcode;
    }

    public void setSubjcode(String subjcode) {
        this.subjcode = subjcode;
    }

    public String getSubjlecUnits() {
        return subjlecUnits;
    }

    public void setSubjlecUnits(String subjlecUnits) {
        this.subjlecUnits = subjlecUnits;
    }

    public String getSubjlabUnits() {
        return subjlabUnits;
    }

    public void setSubjlabUnits(String subjlabUnits) {
        this.subjlabUnits = subjlabUnits;
    }

    public String getSubjcredUnits() {
        return subjcredUnits;
    }

    public void setSubjcredUnits(String subjcredUnits) {
        this.subjcredUnits = subjcredUnits;
    }
}
