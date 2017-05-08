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

    public SummaryAssessment() {
        schoolYears = new ArrayList<>();
    }

    public void fetch(int studid) {
        //Get SchoolYears
        String query = "SELECT sy,sem, totalpay,totalbalance FROM\n" +
                "  (SELECT sy,sem,(total_pay+total_orbrkdwn) as totalpay FROM\n" +
                "  (SELECT sy,sem,orno,sum((coalesce(regfee,0.00)+coalesce(labfee,0.00)+coalesce(compfee,0.00)+coalesce(libfee,0.00)+coalesce(athlfee,0.00)+coalesce(medfee,0.00)+coalesce(spubfee,0.00)+coalesce(sgovfee,0.00)+coalesce(studentfee,0.00)+coalesce(tuitionfee,0.00)+coalesce(latefee,0.00)+coalesce(idfee,0.00)+coalesce(guidancefee,0.00)+coalesce(facilitiesfee,0.00))) as total_pay\n" +
                "                  FROM payment\n" +
                "                  WHERE paycode='CE' AND studid='"+studid+"' GROUP BY sy,sem,orno) as pp\n" +
                "  LEFT JOIN\n" +
                "  (SELECT sy,sem,orno,sum(coalesce(amount,0.00)) as total_orbrkdwn\n" +
                "                  FROM or_brkdown LEFT JOIN payment USING(orno)\n" +
                "                  WHERE or_brkdown.studid = '"+studid+"' GROUP BY sy,sem,orno) as oo USING(sy,sem,orno)) as ppp\n" +
                "LEFT JOIN (SELECT sy,sem,(total_assess-(total_pay+total_orbrkdwn)) as totalbalance from\n" +
                "  (SELECT sy,sem,studid,orno,sum((coalesce(regfee,0.00)+coalesce(labfee,0.00)+coalesce(compfee,0.00)+coalesce(libfee,0.00)+coalesce(athlfee,0.00)+coalesce(medfee,0.00)+coalesce(spubfee,0.00)+coalesce(sgovfee,0.00)+coalesce(studentfee,0.00)+coalesce(tuitionfee,0.00)+coalesce(latefee,0.00)+coalesce(idfee,0.00)+coalesce(guidancefee,0.00)+coalesce(facilitiesfee,0.00))) as total_pay\n" +
                "                  FROM payment\n" +
                "                  WHERE paycode='CE' AND studid='"+studid+"' GROUP BY orno) as pp\n" +
                "  LEFT JOIN\n" +
                "  (SELECT sy,sem,or_brkdown.studid,orno,sum(coalesce(amount,0.00)) as total_orbrkdwn\n" +
                "                  FROM or_brkdown LEFT JOIN payment USING(orno)\n" +
                "                  WHERE or_brkdown.studid = '"+studid+"' GROUP BY sy,sem,or_brkdown.studid,orno) as oo USING(sy,sem,studid)\n" +
                "  LEFT JOIN\n" +
                "  (SELECT sy,sem,studid,(coalesce(regfee,0.00)+coalesce(labfee,0.00)+coalesce(compfee,0.00)+coalesce(libfee,0.00)+coalesce(athlfee,0.00)+coalesce(medfee,0.00)+coalesce(spubfee,0.00)+coalesce(sgovfee,0.00)+coalesce(studentfee,0.00)+coalesce(tuitionfee,0.00)+coalesce(latefee,0.00)+coalesce(idfee,0.00)+coalesce(guidancefee,0.00)+coalesce(facilitiesfee,0.00)) as total_assess\n" +
                "                  FROM assessment) as aa USING (sy,sem,studid)) as bbb USING(sy,sem);";

        System.out.println(query);

        try {
            processQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void processQuery(String query) throws SQLException {
        ResultSet rs = DB.query(query);
        int columnCount = rs.getMetaData().getColumnCount();
        String[] data = new String[columnCount];
        String currentSY = null;
        while(rs.next()){
            for (int i = 0; i < columnCount; i++) {
                data[i] = rs.getString(i);
            }

            //Appointing data to variables
            String sy = data[0];
            char sem = data[1].charAt(0);
            double totalBalance = Double.parseDouble(data[3]);
            double totalPayed = Double.parseDouble(data[2]);

            int currentSYIndex = -1;

            //Assigning variables data to models
            //Also adds new SchoolYear if the current query row sy is different fro the currentSY
            if(currentSY == null || currentSY.equals("") || currentSY != sy) {
                currentSY = sy;
                schoolYears.add(new SchoolYear(sy));
                currentSYIndex++;
            }

            //Adds new Sem
            schoolYears.get(currentSYIndex).getSems().add(new Sem(sem,totalBalance,totalPayed));


        }
    }
}
