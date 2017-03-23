package forms;

import classess.Student;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.swing.JRViewer;
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
import java.util.HashMap;

public class Assessment {

    private boolean isAdmin;
    private Student currentStudent;


    //<editor-fold defaultstate="collapsed" desc="UI Declarations">
    private JFrame frame;
    private JPanel panel1;
    private JSplitPane splitPane;
    private JLabel lblName;
    private JTabbedPane tabPreviousAccounts;
    private JFormattedTextField txtSearchStudentIdNumber;
    private JButton btnSearchStudentIdNumber;
    private JButton btnSearchStudentByName;
    private JLabel btnLogout;
    private JButton btnFees;
    private JPanel pnlSummary;
    private JTabbedPane tabbedPane1;
    private JPanel pnlAssessmentView;
    private JButton editButton;
    //</editor-fold>

    public Assessment(boolean isAdmin) {
        this.isAdmin = isAdmin;
        System.out.println(isAdmin);
        init();
        listeners();
        btnSearchStudentByName.setFocusable(false);//should change this latur
        btnFees.setFocusable(false);//should change this latur
        txtSearchStudentIdNumber.requestFocus();
    }

    public void viewReport(String studid){
        try {
            String sourceFile = "";
            HashMap m = new HashMap();

            JasperReport jr = JasperCompileManager.compileReport(sourceFile);
            JasperPrint jp = JasperFillManager.fillReport(jr,m,DB.getConnection());
            JRViewer pnlAssessmentReport = new JRViewer(jp);
            SwingUtilities.invokeLater(()->{
                pnlAssessmentView.add(pnlAssessmentReport);
                pnlAssessmentView.updateUI();
                pnlAssessmentView.repaint();
            });

        } catch (JRException | SQLException e) {
            e.printStackTrace();
        }
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

    //<editor-fold defaultstate="collapsed" desc="Printables">
    //Needs to change latur
    public void showAssessment(){
        String studid = txtSearchStudentIdNumber.getValue().toString();

        currentStudent = new Student(studid);
        currentStudent.setBasicInfo(new String[]{studid, "Pete Christian", "Reyes", "BSIT", "IV", "Faculty Dependent"});
        currentStudent.printValuesToConsole();

//        viewReport(studid);

        //TODO: remove this shit
        DefaultTableModel model = new DefaultTableModel(new String[][]{
                {"Wingo","Ongiw"},
                {"Hello","Heeelo"}
        },new String[]{"Hello","World"});




        //TODO: removthis shit
        showSummaryAssessment();
    }



    public void showPermit(){

    }


    public void showClearance(){

    }
    //</editor-fold>

    public void showSummaryAssessment(){
        SummaryAssessmentSy sssy = new SummaryAssessmentSy("2013 - 2014");

        sssy.attach(pnlSummary);
    }

    private void init(){
        setName();
    }

    //<editor-fold defaultstate=collapsed desc="No Change Needed">

    private void setName(){
        try {
            ResultSet rs = DB.query("SELECT current_user");
            lblName.setText(DB.getOneStringFromResultSet(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }//Sets the name of the current user


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
    //</editor-fold>

}
