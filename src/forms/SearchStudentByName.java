package forms;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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

        txtSearchStudentName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode()==KeyEvent.VK_ENTER){
                    btnSearchStudent.doClick();
                }
            }
        });

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
