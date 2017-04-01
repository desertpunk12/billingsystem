package forms;

import utils.JFrameHelper;

import javax.swing.*;

/**
 * Created by dpunk12 on 3/24/2017.
 */
public class EditAssessmentView {

    private JTextField txtStudName;
    private JTextField textField2;
    private JTextField txtyrandcourse;
    private JTable table1;
    private JTable table2;
    private JTextField textField5;
    private JTextField textField6;
    private JButton saveButton;
    private JTextField textField7;
    private JPanel pnlMain;

    private JFrame frame;


    public void show(){
        JFrameHelper.show(frame,pnlMain,"Edit Assessment");
    }


}
