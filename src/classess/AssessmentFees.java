package classess;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import utils.DB;
import utils.ERR;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dpunk12 on 3/25/2017.
 */
public class AssessmentFees {


    private List<AssessmentFee> feeList;

    private String gendate;
    private String studid;
    private String sy;
    private char sem;

    public AssessmentFees(String studid,String sy,char sem){
        feeList = new ArrayList<>();
        this.studid=studid;
        this.sy=sy;;
        this.sem=sem;

        try {
            fetchData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fetchData() throws SQLException{
        if(studid == null || studid.equals(""))
            ERR.pr("studid is null or empty!");

        fetchData(studid,sy,sem);

    }

    public void fetchData(String studid,String sy, char sem) throws SQLException{

        String query = "SELECT to_char(gendate,'Month DD, YYYY') as gendate,feecode,amt,feedesc \n" +
                "FROM srgb.ass_details LEFT JOIN srgb.ass_header USING(studid,sy,sem) LEFT JOIN srgb.fees USING(feecode) \n" +
                "WHERE studid='"+studid+"' AND sy='"+sy+"' and sem='"+sem+"';";

        System.out.println(query);

        ResultSet rs = DB.query(query);

        if(rs.next()){
            this.gendate = rs.getString(1);
            feeList.add(new AssessmentFee(rs.getString(2),rs.getDouble(3),rs.getString(4)));
        }

        while (rs.next()){
            feeList.add(new AssessmentFee(rs.getString(2),rs.getDouble(3),rs.getString(4)));
        }



        //TODO: change latur
//        feeList.add(new AssessmentFee("Registration Fee",0.00D));
//        feeList.add(new AssessmentFee("Tuition Fee",0.00D));
//        feeList.add(new AssessmentFee("I.D. Fee",0.00D));
//        feeList.add(new AssessmentFee("Computer Lab & Module",0.00D));
//        feeList.add(new AssessmentFee("Internet Fee",0.00D));
//        feeList.add(new AssessmentFee("Athletics Fee",0.00D));
//        feeList.add(new AssessmentFee("Handbook(New stud/Transferee)",0.00D));
//        feeList.add(new AssessmentFee("CWTS/ROTC",0.00D));
//        feeList.add(new AssessmentFee("Student Activity",0.00D));
//        feeList.add(new AssessmentFee("Library Fee",0.00D));
//        feeList.add(new AssessmentFee("STUDENT GRP INSURANCE",35.00D));
//        feeList.add(new AssessmentFee("Health Services Fee",0.00D));
//        feeList.add(new AssessmentFee("Guidance, Counseling & Testing",0.00D));
//        feeList.add(new AssessmentFee("Fac Impv.Maint. &Dev. Fee",0.00D));
//        feeList.add(new AssessmentFee("SocioCultural&CommunityEngaFee",0.00D));
    }

    public JRBeanCollectionDataSource getDataSource(){
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(feeList,false);

        return dataSource;
    }


    public String getGendate(){
        return gendate;
    }


}
