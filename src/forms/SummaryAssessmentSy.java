package forms;

import models.SchoolYear;
import models.Sem;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by dpunk12 on 11/11/2016.
 */
public class SummaryAssessmentSy {
    private JPanel pnlMain;

    private SchoolYear schoolYear;


    public SummaryAssessmentSy(SchoolYear schoolYear){
        this.schoolYear = schoolYear;
        pnlMain.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.BLACK),"School Year: "+schoolYear.getSchoolYear()));
        insertSems();
    }


    private void insertSems(){
//        TODO: process inserting of sems for specific semsesters
        ArrayList<SummaryAssessmentSem> semsUI = new ArrayList<>();
        for (Sem sem : schoolYear.getSems()) {
            semsUI.add(new SummaryAssessmentSem(schoolYear.getSchoolYear(),sem));
        }

        for (SummaryAssessmentSem sem : semsUI) {
            sem.attach(pnlMain);
        }
        //remove this shit
//        SummaryAssessmentSem sssem1 = new SummaryAssessmentSem(schoolYear,'s');
//        SummaryAssessmentSem sssem2 = new SummaryAssessmentSem(schoolYear,'2');
//        SummaryAssessmentSem sssem3 = new SummaryAssessmentSem(schoolYear,'1');
//
//        sssem1.attach(pnlMain);
//        sssem2.attach(pnlMain);
//        sssem3.attach(pnlMain);
    }
    public void addSem(Sem sem){
        SummaryAssessmentSem sssem = new SummaryAssessmentSem(schoolYear.getSchoolYear(),sem);
        sssem.attach(pnlMain);
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
