package classess;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import utils.ERR;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dpunk12 on 3/25/2017.
 */
public class AssessmentFees {


    private List<AssessmentFee> feeList;

    private String studid;

    public AssessmentFees(String studid){
        feeList = new ArrayList<>();
        this.studid=studid;
        fetchData();
    }

    private void fetchData(){
        if(studid == null || studid.equals(""))
            ERR.pr("studid is null or empty!");

        fetchData(studid);

    }

    public void fetchData(String studid){


        //TODO: change latur
        feeList.add(new AssessmentFee("Registration Fee",0.00D));
        feeList.add(new AssessmentFee("Tuition Fee",0.00D));
        feeList.add(new AssessmentFee("I.D. Fee",0.00D));
        feeList.add(new AssessmentFee("Computer Lab & Module",0.00D));
        feeList.add(new AssessmentFee("Internet Fee",0.00D));
        feeList.add(new AssessmentFee("Athletics Fee",0.00D));
        feeList.add(new AssessmentFee("Handbook(New stud/Transferee)",0.00D));
        feeList.add(new AssessmentFee("CWTS/ROTC",0.00D));
        feeList.add(new AssessmentFee("Student Activity",0.00D));
        feeList.add(new AssessmentFee("Library Fee",0.00D));
        feeList.add(new AssessmentFee("STUDENT GRP INSURANCE",35.00D));
        feeList.add(new AssessmentFee("Health Services Fee",0.00D));
        feeList.add(new AssessmentFee("Guidance, Counseling & Testing",0.00D));
        feeList.add(new AssessmentFee("Fac Impv.Maint. &Dev. Fee",0.00D));
        feeList.add(new AssessmentFee("SocioCultural&CommunityEngaFee",0.00D));
    }

    public JRBeanCollectionDataSource getDataSource(){
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(feeList,false);

        return dataSource;
    }



}
