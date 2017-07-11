package forms;

import classess.AssessmentDefaultFees;
import classess.AssessmentFee;
import utils.DB;

import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.RectangularShape;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddEditFee extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable tblAddFee;

    private AssessmentDefaultFees assdef;

    public AddEditFee(AssessmentDefaultFees assdef) {
        this.assdef=assdef;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Add Fee");

        listeners();

        fetchData();
    }

    private void fetchData(){
        String query = "SELECT feecode,feedesc FROM srgb.fees";

        SwingUtilities.invokeLater(() -> {
            try {
                ResultSet rs = DB.query(query);
                tblAddFee.setModel(DB.convertRStoTableModel(rs));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

    }

    private void listeners() {
        buttonOK.addActionListener(e ->
                onOK());

        buttonCancel.addActionListener(e ->
                onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        String feecode = String.valueOf(tblAddFee.getValueAt(tblAddFee.getSelectedRow(),0));
        String feedesc = String.valueOf(tblAddFee.getValueAt(tblAddFee.getSelectedRow(),1));
        assdef.getFeeList().add(new AssessmentFee(feecode,0D,feedesc));
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}
