package classess;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import utils.ERR;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dpunk12 on 3/24/2017.
 */
public class AssessmentSubjects {


    private List<AssessmentSubject> subjectList;

    private String studid;

    public AssessmentSubjects(String studid){
        subjectList = new ArrayList<AssessmentSubject>();
        this.studid=studid;
        fetchData();

    }

    private void fetchData(){
        if(studid == null || studid.equals(""))
            ERR.pr("studid is null or empty!");

        fetchData(studid);

    }


    private void fetchData(String studid){
        //TODO: remove this latur
        subjectList.add(new AssessmentSubject("Econ 10",3D,0D,3D));
        subjectList.add(new AssessmentSubject("IT 141",2D,3D,3D));
        subjectList.add(new AssessmentSubject("IT 142",2D,3D,3D));
        subjectList.add(new AssessmentSubject("IT 143",3D,0D,3D));
        subjectList.add(new AssessmentSubject("IT 151",2D,3D,3D));
        subjectList.add(new AssessmentSubject("IT 154",2D,3D,3D));
        subjectList.add(new AssessmentSubject("IT 157",2D,3D,3D));

    }

    public JRBeanCollectionDataSource getDataSource(){

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(subjectList,false);


        return dataSource;
    }


}
