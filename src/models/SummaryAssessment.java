package models;

import utils.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by dpunk12 on 5/3/2017.
 */
public class SummaryAssessment {


    private ArrayList<SchoolYear> schoolYears;
    private String studid;


    public SummaryAssessment(String studid) {
        this.studid=studid;
        schoolYears = new ArrayList<>();
        fetch();
    }

    public void fetch() {
        //Get SchoolYearss

        String query = "SELECT sy,sem,totalASsessment,(totalASsessment-sum(totalpayed)) AS balance FROM\n" +
                "  (\n" +
                "    SELECT sy,sem,sum(amt) AS totalpayed,paydate FROM srgb.collection_details JOIN srgb.collection_header USING(orno) WHERE studid='"+studid+"' GROUP BY sy,sem,paydate\n" +
                "  ) pop\n" +
                "  RIGHT JOIN (\n" +
                "\n" +
                "  SELECT sy,sem,studlevel,sum(amt) AS totalASsessment FROM srgb.ASs_details JOIN srgb.semstudent USING(studid,sy,sem) WHERE studid='"+studid+"' GROUP BY sy,sem,studlevel\n" +
                "  ) pap USING(sy,sem)\n" +
                "GROUP BY sy,sem,totalASsessment ORDER By sy DESC,sem DESC;";


        System.out.println(query);

        try {
            processQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<SchoolYear> getSchoolYears(){
        return schoolYears;
    }

    // Process Query for SchoolYear and Sem(totalpay,totalbalance) classess
    private void processQuery(String query) throws SQLException {
        ResultSet rs = DB.query(query);

        int columnCount = rs.getMetaData().getColumnCount();
        String currentSY = null;

        while(rs.next()){
            String[] columnData = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                columnData[i] = rs.getString(i+1);
            }

            //Appointing columnData to variables
            String sy = columnData[0];
            char sem = columnData[1].charAt(0);
            double totalAssessment = Double.parseDouble(columnData[2]);
            double totalBalance = columnData[3]!=null ? Double.parseDouble(columnData[3]) : totalAssessment;


            //Assigning variables columnData to models
            //Also adds new SchoolYear if the current query row sy is different from the currentSY
            //Check if the current row sy is new
            if(currentSY == null || currentSY.equals("") || !currentSY.equals(sy)) {
                currentSY = sy;
                schoolYears.add(new SchoolYear(studid,sy));
            }

            //Adds new Sem
            getRecentSchoolYear().getSems().add(new Sem(studid,getRecentSchoolYear().getSchoolYear(),sem,totalBalance,totalAssessment));


        }
    }

    public SchoolYear getRecentSchoolYear(){
        return schoolYears.get(schoolYears.size()-1);
    }
}
