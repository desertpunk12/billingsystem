package classess;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import utils.DB;
import utils.ERR;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dpunk12 on 3/25/2017.
 */
public class AssessmentDefaultFees {


    private List<AssessmentFee> feeList;

    private String studid;
    private String sy;
    private char sem;

    private boolean isNightClass;
    private int yrlvl;

    public AssessmentDefaultFees(String studid, String sy, char sem, int yrlvl, boolean isNightClass){
        this.studid=studid;
        this.sy=sy;;
        this.sem=sem;
        this.yrlvl=yrlvl;
        this.isNightClass=isNightClass;

        feeList = new ArrayList<>();
        try {
            fetchData();
        } catch (SQLException e) {
            e.printStackTrace();
            }
    }

    public void add(AssessmentFee fee){
        feeList.add(fee);
    }

    private void fetchData() throws SQLException{
        if(studid == null || studid.equals(""))
            ERR.pr("studid is null or empty!");

        fetchData(studid,sy,sem,yrlvl,isNightClass);

    }

    public void fetchData(String studid,String sy, char sem, int yrlvl,boolean isNightClass) throws SQLException{
        //TODO: Default Fees
        String query = "SELECT feecode,feedesc,yrlvl"+yrlvl+"_"+(isNightClass?"night":"day")+
                " FROM srgb.miscfeematrix JOIN srgb.fees USING(feecode)\n" +
                "  WHERE enrol=TRUE AND sy='"+sy+"' AND sem='"+sem+"'\n" +
                "UNION\n" +
                "SELECT 'TUITIONFEE'::CHAR(12) AS feecode,'Tuition Fee'::VARCHAR(30) AS feedesc, " +
                "SUM(subjlec*yrlvl"+yrlvl+"_"+(isNightClass?"night":"day")+") tuition FROM srgb.semstudent\n" +
                "  JOIN srgb.registration reg USING(studid,sy,sem)\n" +
                "  JOIN srgb.subject sub USING(subjcode)\n" +
                "  JOIN srgb.tuitionmatrix tui ON (progcode=studmajor AND reg.sy=tui.sy AND reg.sem=tui.sem)\n" +
                "  WHERE studid='"+studid+"' AND reg.sy='"+sy+"' AND reg.sem='"+sem+"';\n";

        System.out.println(query);

        ResultSet rs = DB.query(query);

        while (rs.next()){
            feeList.add(new AssessmentFee(rs.getString(1),rs.getDouble(3),rs.getString(2)));
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

    public List<AssessmentFee> getFeeList(){
        return feeList;
    }


}
