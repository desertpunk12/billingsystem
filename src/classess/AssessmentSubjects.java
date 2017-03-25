package classess;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import utils.DB;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dpunk12 on 3/24/2017.
 */
public class AssessmentSubjects {

    private String studid;

    private List<AssessmentSubject> subjectList;

    public AssessmentSubjects(String studid){
        this.studid=studid;

        subjectList = new ArrayList<AssessmentSubject>();
    }

    public void fetchDataFromDB(){
        subjectList.clear();

    }


    public JRBeanCollectionDataSource getDataSource(){
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(subjectList);

        return dataSource;
    }





    class AssessmentSubject{

        private String subject;
        private String lec;
        private String lab;
        private String credunits;

        public AssessmentSubject(String subject, String lec, String lab, String credunits) {
            this.subject = subject;
            this.lec = lec;
            this.lab = lab;
            this.credunits = credunits;
        }

        //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getLec() {
            return lec;
        }

        public void setLec(String lec) {
            this.lec = lec;
        }

        public String getLab() {
            return lab;
        }

        public void setLab(String lab) {
            this.lab = lab;
        }

        public String getCredunits() {
            return credunits;
        }

        public void setCredunits(String credunits) {
            this.credunits = credunits;
        }

        //</editor-fold>
    }

}
