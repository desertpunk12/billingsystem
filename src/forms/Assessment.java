package forms;

import classess.Student;
import utils.DB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * Created by dpunk12 on 11/3/2016.
 */
public class Assessment {

   private boolean isAdmin;

    private JFrame frame;
    private JPanel panel1;
    private JSplitPane splitPane;
    private JLabel lblName;
    private JTabbedPane tabPreviousAccounts;
    private JTable tblCurrentAssessment;
    private JFormattedTextField txtSearchStudentIdNumber;
    private JButton btnSearchStudentIdNumber;
    private JButton btnSearchStudentByName;
    private JLabel btnLogout;
    private JButton btnFees;
    private JLabel txtStudentFullname;
    private JLabel txtStudentCourse;
    private JLabel txtStudetScholarship;
    private JLabel txtStudentYearlevel;
    private JPanel pnlSummary;

    private JMenuBar menuBar;

    public Assessment(boolean isAdmin) {
        this.isAdmin = isAdmin;
        System.out.println(isAdmin);
        init();
        listeners();
        btnSearchStudentByName.setFocusable(false);//should change this latur
        btnFees.setFocusable(false);//should change this latur
        txtSearchStudentIdNumber.requestFocus();
    }

    private void listeners(){

        btnFees.addActionListener(e -> new Fees().show());

        btnLogout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogout.setForeground(Color.RED);
            }


            @Override
            public void mouseExited(MouseEvent e){
                btnLogout.setForeground(Color.BLACK);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                new Login().show();
                frame.dispose();
            }

        });

        btnSearchStudentByName.addActionListener(e -> {

            SearchStudentByName ssbn = new SearchStudentByName(txtSearchStudentIdNumber);
            JFrame frame = new JFrame("Search Student By Name");
            frame.setContentPane(ssbn.getMainPanel());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        });

        txtSearchStudentIdNumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ENTER){
                    showAssessment();
                }
            }
        });


        btnSearchStudentIdNumber.addActionListener(e -> {
            showAssessment();
        });

        panel1.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                splitPane.setDividerLocation(0.6);
            }
        });

    }

    //Needs to change latur
    public void showAssessment(){
        Student student = new Student("2013-0008");
        student.set(new String[]{"2013-0008", "Pete Christian", "Reyes", "BSIT", "IV", "Faculty Dependent"});
        student.printValuesToConsole();
        student.setTextFields(txtStudentFullname,txtStudentCourse,txtStudentYearlevel,txtStudetScholarship);


        DefaultTableModel model = new DefaultTableModel(new String[][]{
                {"Wingo","Ongiw"},
                {"Hello","Heeelo"}
        },new String[]{"Hello","World"});

        tblCurrentAssessment.setModel(model);



        //removthis shit
        showSummaryAssessment();
    }

    public void showSummaryAssessment(){
        SummaryAssessmentSy sssy = new SummaryAssessmentSy("2013 - 2014");

        sssy.attach(pnlSummary);
    }

    public void show(){
        frame = new JFrame("DOSCST Student's Billing System");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        try{
            MaskFormatter formatter = new MaskFormatter("#### - ####");
            formatter.setPlaceholderCharacter('0');
            txtSearchStudentIdNumber.setFormatterFactory(new DefaultFormatterFactory(formatter));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(()-> splitPane.setDividerLocation(0.6));
    }

    private JMenuBar addMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        JMenu mFile = new JMenu("File");
        menuBar.add(mFile);
        System.out.println(menuBar==null);
        return menuBar;
    }

    private void init(){
        setName();
    }

    private void setName(){
        try {
            ResultSet rs = DB.query("SELECT current_user");
            lblName.setText(DB.getOneStringFromResultSet(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        tblCurrentAssessment = new JTable(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }
}
