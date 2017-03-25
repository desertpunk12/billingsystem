package forms;

import utils.JFrameHelper;

import javax.swing.*;

/**
 * Created by dpunk12 on 2/2/2017.
 */
public class Fees {
    private JPanel pnlMain;
    private JTable table1;
    private JButton updateButton;
    private JTextField textField1;
    private JButton searhButton;
    private JButton addButton;

    private JFrame frame;


    public void show(){
        JFrameHelper.show(frame,pnlMain,"Fess",true);
    }
}
