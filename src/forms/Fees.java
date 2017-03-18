package forms;

import javax.swing.*;

/**
 * Created by dpunk12 on 2/2/2017.
 */
public class Fees {
    private JPanel panel1;
    private JTable table1;
    private JButton updateButton;
    private JTextField textField1;
    private JButton searhButton;
    private JButton addButton;

    private JFrame frame;


    public void show(){
        frame = new JFrame("DOSCST Student's Billing System");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}
