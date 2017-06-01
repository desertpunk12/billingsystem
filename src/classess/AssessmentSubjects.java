package classess;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import utils.DB;
import utils.ERR;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dpunk12 on 3/24/2017.
 */
public class AssessmentSubjects {


    private List<AssessmentSubject> subjectList;

    private String studid;
    private String sy;
    private char sem;
    private double totalUnits;

    public AssessmentSubjects(String studid, String sy, char sem){
        subjectList = new ArrayList<AssessmentSubject>();
        this.studid=studid;
        this.sy=sy;
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


    private void fetchData(String studid, String sy, char sem) throws SQLException{
        String query = "select subject.subjcode,subjlec_units,subjlab_units, CAST(subjcredit as REAL) as subjcred_units,\n" +
                "  srgb.unitsload('"+studid+"','"+sy+"','"+sem+"'), registration.section from srgb.subject, srgb.registration\n" +
                "where srgb.registration.subjcode=subject.subjcode AND studid='"+studid+"' and sy='"+sy+"' and registration.sem='"+sem+"';";

        ResultSet rs = DB.query(query);

        if(rs.next()){
            subjectList.add(new AssessmentSubject(rs.getString(1),rs.getDouble(2),rs.getDouble(3),rs.getDouble(4),rs.getString(6)));
            this.totalUnits = Double.parseDouble(rs.getString(5));
        }else{
            System.out.println("No Subjects");
            return;
        }

        while(rs.next()){
            //code,lecunits,labunits,credunits,totalload,section
            subjectList.add(new AssessmentSubject(rs.getString(1),rs.getDouble(2),rs.getDouble(3),rs.getDouble(4),rs.getString(6)));
        }



        //TODO: remove this latur
//        subjectList.add(new AssessmentSubject("Econ 10",3D,0D,3D));
//        subjectList.add(new AssessmentSubject("IT 141",2D,3D,3D));
//        subjectList.add(new AssessmentSubject("IT 142",2D,3D,3D));
//        subjectList.add(new AssessmentSubject("IT 143",3D,0D,3D));
//        subjectList.add(new AssessmentSubject("IT 151",2D,3D,3D));
//        subjectList.add(new AssessmentSubject("IT 154",2D,3D,3D));
//        subjectList.add(new AssessmentSubject("IT 157",2D,3D,3D));

    }

    public JRBeanCollectionDataSource getDataSource(){

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(subjectList,false);


        return dataSource;
    }


}
