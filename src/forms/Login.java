package forms;

import utils.DB;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by dpunk12 on 10/28/2016.
 */
public class Login {
    private JPanel panel1;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    private JFrame frame;

    public Login() {
        listeners();
    }


    public void show(){
        frame = new JFrame("DOSCST Student's Billing System");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        txtUsername.setText("postgres");//Temporary
        txtPassword.setText("xx");//Temporary


        txtPassword.requestFocus();
    }

    private void listeners(){

       txtUsername.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()== KeyEvent.VK_ENTER){
                    btnLogin.doClick();
                }
            }
        });
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()== KeyEvent.VK_ENTER){
                    btnLogin.doClick();
                }
            }
        });

        btnLogin.addActionListener(e -> {
            if(txtUsername.getText().isEmpty() || txtPassword.getPassword().length==0){
                JOptionPane.showMessageDialog(null,"Please Enter Your Username and Password");
                return;
            }
            if(DB.tryLogin(txtUsername.getText(),String.valueOf(txtPassword.getPassword()))){
                try {
                    ResultSet rs = DB.callf("public.isassessadmin", "");
                    rs.next();
                    boolean isAdmin = rs.getBoolean(1);
                    new Assessment(isAdmin).show();
                    frame.dispose();

                }catch (SQLException e1){
                    e1.printStackTrace();
                }

            }else{
                System.out.println("Login Failed Either No Connection or Login Details are Wrong!");
                JOptionPane.showMessageDialog(null,"Login Failed Either No Connection or Login Details are Wrong!");
            }
        });
    }


    public static void main(String[] args) {


        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }

        new Login().show();

        try {
            DB.getConnection();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
}
