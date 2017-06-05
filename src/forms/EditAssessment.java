package forms;

import classess.AssessmentDefaultFees;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import utils.JFrameHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by dpunk12 on 3/24/2017.
 */
public class EditAssessment extends JDialog{

    private JPanel pnlMain;
    private JTable tblEditFee;
    private JButton btnAddFee;
    private JButton btnAcceptChanges;
    private JButton btnRemoveSelected;

//    private JFrame frame;

    private AssessmentDefaultFees assdeffees;

    public EditAssessment(AssessmentDefaultFees assdeffees){
        setContentPane(pnlMain);
        setModal(true);
        getRootPane().setDefaultButton(btnAcceptChanges);

        this.assdeffees = assdeffees;

        refreshTable();
        listeners();


    }

    private void refreshTable(){
        String[][] data = new String[assdeffees.getFeeList().size()][2];
        for (int i=0;i<assdeffees.getFeeList().size();i++){
            data[i][0] = assdeffees.getFeeList().get(i).getDesc();
            data[i][1] = String.valueOf(assdeffees.getFeeList().get(i).getAmount());
        }


        tblEditFee.setModel(new DefaultTableModel(data,new String[]{"Fee Description","Amount"}));
    }

    private void resetAmounts(){
        for (int i = 0; i < assdeffees.getFeeList().size(); i++) {
            assdeffees.getFeeList().get(i).setAmount(Double.parseDouble(String.valueOf(tblEditFee.getValueAt(i,1))));
        }
    }

    private void listeners() {
        btnAddFee.addActionListener(e -> {
            AddEditFee dialog = new AddEditFee(assdeffees);
            dialog.pack();
            dialog.setVisible(true);
            refreshTable();
        });

        btnRemoveSelected.addActionListener(e -> {
            assdeffees.getFeeList().remove(tblEditFee.getSelectedRow());
            refreshTable();
        });

        btnAcceptChanges.addActionListener(e -> {
            resetAmounts();
            dispose();
//            frame.dispose();
        });
    }


//    public void show(){
//        frame = new JFrame();
//        frame.s
//        JFrameHelper.show(frame,pnlMain,"Edit Assessment");
//        frame.setLocationRelativeTo(null);
//    }


}
