package forms;

import javax.swing.*;

/**
 * Created by dpunk12 on 11/3/2016.
 */
public class Assessment {

   private boolean isAdmin;

    private JFrame frame;
    private JPanel panel1;

    public Assessment(boolean isAdmin) {
       this.isAdmin = isAdmin;
        System.out.println(isAdmin);
    }

    public void show(){
        frame = new JFrame("DOSCST Student's Billing System");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }


}
