package forms;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by dpunk12 on 11/12/2016.
 */
public class SearchStudentByName {
    private JTextField txtSearchStudentName;
    private JButton btnSearchStudent;
    private JTable tblStudens;
    private JPanel pnlMain;

    private JTextField parentIdNumberField;

    public SearchStudentByName(JTextField parentIdNumberField){
        this.parentIdNumberField = parentIdNumberField;

        listeners();
    }

    private void listeners(){
        btnSearchStudent.addActionListener(e -> searchStudent());
    }

    public JPanel getMainPanel(){
        return pnlMain;
    }

    private void setParentIdNumberField(){
        parentIdNumberField.setText(String.valueOf(tblStudens.getValueAt(tblStudens.getSelectedRow(),tblStudens.getSelectedColumn())));
    }

    private void searchStudent(){
        String searchName = txtSearchStudentName.getText();
    }

}
