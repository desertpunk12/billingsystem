package forms;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by dpunk12 on 11/3/2016.
 */
public class Assessment {

   private boolean isAdmin;

    private JFrame frame;
    private JPanel panel1;
    private JSplitPane splitPane;

    private JMenuBar menuBar;

    public Assessment(boolean isAdmin) {
       this.isAdmin = isAdmin;
        System.out.println(isAdmin);
    }

    public void show(){
        frame = new JFrame("DOSCST Student's Billing System");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        SwingUtilities.invokeLater(()-> splitPane.setDividerLocation(0.6));
    }

    private JMenuBar addMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        JMenu mFile = new JMenu("File");
        menuBar.add(mFile);
        System.out.println(menuBar==null);
        return menuBar;
    }


}
