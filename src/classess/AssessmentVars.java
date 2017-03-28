package classess;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class AssessmentVars {


    private Student student;

    private String studid;

    public AssessmentVars(String studid){
        this.studid = studid;
        student = new Student(studid);
    }

}
