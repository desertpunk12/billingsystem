package models;

import utils.DB;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by dpunk12 on 5/17/2017.
 */
public class ReportsHeader {

    String idNumber;
    String firstName;
    String middleName;
    String lastName;
    String yearrlevel;
    String course;
    String date;
    String sy;
    String sem;
    String scholarship;

    Subject[] subjects;
    Fee[] fees;

    double totalAssessment;
    double remainingBalance;

    public ReportsHeader(String idNumber){
        this.idNumber = idNumber;
        fetch();
    }

    private void fetch(){
        String query = "";
        try{
            processQuery(query);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    private void processQuery(String query) throws SQLException {
        ResultSet rs = DB.query(query);
        int columnCount = rs.getMetaData().getColumnCount();

        while(rs.next()){
            String[] columnData = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                columnData[i] = rs.getString(i);
            }


        }
    }
}
