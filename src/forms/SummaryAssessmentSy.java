package forms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Created by dpunk12 on 11/11/2016.
 */
public class SummaryAssessmentSy {
    private JLabel lblSchoolYear;
    private JPanel pnlSems;
    private JPanel pnlMain;

    private String schoolYear;


    public SummaryAssessmentSy(String schoolyear){
        this.schoolYear = schoolyear;
        insertSems();
    }


    private void insertSems(){
//        TODO: process inserting of sems for specific semsesters




        //remove this shit

        SummaryAssessmentSem sssem = new SummaryAssessmentSem();
        SummaryAssessmentSem sssem2 = new SummaryAssessmentSem();
        SummaryAssessmentSem sssem3 = new SummaryAssessmentSem();

        DefaultTableModel model = new DefaultTableModel(new String[][]{
                {"Wingo","Ongiw"},
                {"Hello","Heeelo"}
        },new String[]{"Hello","World"});

        sssem.setTblDataModel(model);
        sssem2.setTblDataModel(model);
        sssem3.setTblDataModel(model);

        sssem.attach(pnlSems);
        sssem2.attach(pnlSems);
        sssem3.attach(pnlSems);
    }

    public void attach(JPanel pnl){

        SwingUtilities.invokeLater(()->{
            pnl.setLayout(new BoxLayout(pnl,BoxLayout.PAGE_AXIS));
            pnl.add(pnlMain);
            pnl.updateUI();
            pnl.repaint();
            pnlMain.repaint();
        });

    }


    public JPanel getPnlMain(){
        return pnlMain;
    }


    public void add(Component comp){
        if(comp==null)
            System.out.println("Main panel is null!");
        else
            pnlMain.add(comp);

    }

}
