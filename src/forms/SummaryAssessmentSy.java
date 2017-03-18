package forms;

import javax.swing.*;

/**
 * Created by dpunk12 on 11/11/2016.
 */
public class SummaryAssessmentSy {
    private JLabel lblSchoolYear;
    private JPanel pnlSems;
    private JPanel pnlSy;

    private String schoolYear;


    public SummaryAssessmentSy(String schoolyear){
        this.schoolYear = schoolyear;

    }


    private void insertSems(){
//        TODO: process inserting of sems for specific semsesters
    }


    private void test(){

    }

    public JPanel getPanel(){
        return pnlSy;
    }
}
