package forms;

import javax.swing.*;
import javax.swing.table.TableModel;

/**
 * Created by dpunk12 on 11/13/2016.
 */
public class SummaryAssessmentSem {
    private JTable tblData;
    private JPanel pnlMain;


    public void setTblDataModel(TableModel model){
        tblData.setModel(model);
    }

    public JPanel getPnlMain(){
        if(pnlMain == null)
            System.out.println("Main panel is null");
        else
            return pnlMain;

        return null;
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


}
