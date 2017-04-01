package forms;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by dpunk12 on 3/21/2017.
 */
public class DBSettings {
    private JButton btnSave;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField txtDBName;
    private JPanel pnlMain;

    private JFrame frame;


    public DBSettings(){
        File f = new File("dbsettings.ing");
        if(f.exists()){

        }else{
            try {
                f.createNewFile()
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        listeners();
    }


    private void listeners() {
        btnSave.doClick();
        btnSave.addActionListener(e -> System.out.println(txtDBName.getText()));
    }

    public void show(){
        frame = new JFrame("DBSettings");
        frame.setContentPane(new DBSettings().pnlMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }



    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }

        new DBSettings().show();

    }
}
