package forms;

import models.Sem;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static java.lang.System.*;

/**
 * Created by dpunk12 on 11/13/2016.
 */
public class SummaryAssessmentSem {
    private JPanel pnlMain;
    private JLabel lblSem;
    private JLabel lblTotalAssessment;
    private JLabel lblRemainingBalance;
    private JPanel pnlOrNumbers;
    private JPanel pnlPayDates;
    private JPanel pnlAmounts;
    private JLabel lblTotalAmountPaid;


    private String sy;
    private Sem sem;



    public SummaryAssessmentSem(String sy,Sem sem){
        this.sy = sy;
        this.sem = sem;

        lblSem.setText((sem.getSem()=='1')?"First Semester":(sem.getSem()=='2')?"Second Semester":"Summer");
        lblTotalAmountPaid.setText(String.valueOf(sem.getTotalAmountPaid()));
        lblTotalAssessment.setText(String.valueOf(sem.getTotalAssessment()));
        lblRemainingBalance.setText(String.valueOf(sem.getRemainingBalance()));
        if(sem.getRemainingBalance()!=0){
            lblRemainingBalance.setForeground(Color.RED);
        }

        pnlOrNumbers.setLayout(new BoxLayout(pnlOrNumbers,BoxLayout.PAGE_AXIS));
        pnlPayDates.setLayout(new BoxLayout(pnlPayDates,BoxLayout.PAGE_AXIS));
        pnlAmounts.setLayout(new BoxLayout(pnlAmounts,BoxLayout.PAGE_AXIS));

        for (int i = 0; i < sem.getPaidOrnos().size(); i++) {
            pnlOrNumbers.add(new JLabel(sem.getPaidOrnos().get(i)));
            pnlPayDates.add(new JLabel(sem.getPaidPaydates().get(i).toString()));
            pnlAmounts.add(new JLabel(String.valueOf(sem.getPaidAmounts().get(i))));
        }

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

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                triggerNewSemAssessment();
            }
        });
    }

    private void triggerNewSemAssessment(){
        Container mainContainer = pnlMain.getParent().getParent();
        JPasswordField hiddentSelectedSem = null;
        for (int i = 0; i <mainContainer.getComponentCount(); i++) {
            if (mainContainer.getComponent(i) instanceof JPasswordField) {
                hiddentSelectedSem = (JPasswordField) mainContainer.getComponent(i);
                hiddentSelectedSem.setText(sem.getSem() + "");
                hiddentSelectedSem.requestFocus();
                hiddentSelectedSem.getActionListeners()[0].actionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,null));
            }
        }
        if(hiddentSelectedSem==null){
            out.println("ERRORRRRRR:: Sem Selected JTextPasswordField is never found!!!!");
        }
    }


    public JPanel getPnlMain(){
        if(pnlMain == null)
            out.println("Main panel is null");
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
