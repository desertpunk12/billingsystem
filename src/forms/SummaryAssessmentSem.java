package forms;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by dpunk12 on 11/13/2016.
 */
public class SummaryAssessmentSem {
    private JTable tblData;
    private JPanel pnlMain;
    private JLabel lblSem;


    private String sy;
    private char sem;



    public SummaryAssessmentSem(String sy,char sem){
        this.sy = sy;
        this.sem = sem;

        lblSem.setText((sem=='1')?"First Semester":(sem=='2')?"Second Semester":"Summer");
        pnlMain.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);

                pnlMain.setBorder(new LineBorder(Color.RED));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);

                pnlMain.setBorder(null);
            }
        });
    }

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
