package forms;

import classess.Student;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.fill.JRFillInterruptedException;
import net.sf.jasperreports.engine.json.expression.member.ObjectConstructionExpression;
import net.sf.jasperreports.swing.JRViewer;
import utils.DB;

import utils.JFrameHelper;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;

public class Assessment {

    //<editor-fold defaultstate="collapsed" desc="UI Declarations">
    private JFrame frame;
    private JPanel pnlMain;
    private JSplitPane splitPane;
    private JLabel lblName;
    private JFormattedTextField txtSearchStudentIdNumber;
    private JButton btnSearchStudentIdNumber;
    private JButton btnSearchStudentByName;
    private JLabel btnLogout;
    private JButton btnFees;
    private JPanel pnlSummary;
    private JPanel pnlAssessmentView;
    private JButton btnEditAssessmentView;
    private JScrollPane scrlSummary;
    private JPanel pnlPermitView;
    private JPanel pnlClearanceView;
    //</editor-fold>

    private boolean isAdmin;

    private Student currentStudent;
    private Student prevStudent;

    private Thread thrdAssessment;
    private Thread thrdPermit;
    private Thread thrdClearance;
    private volatile boolean running = false;
    private volatile String studid;

    public Assessment(boolean isAdmin) {
        this.isAdmin = isAdmin;
        System.out.println(isAdmin);
        init();
        listeners();
        btnSearchStudentByName.setFocusable(false);//should change this latur
        btnFees.setFocusable(false);//should change this latur
    }


    private void listeners(){

        btnEditAssessmentView.addActionListener((e)->{
            EditAssessment editView = new EditAssessment();
            editView.show();
        });

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
                    btnSearchStudentIdNumber.doClick();
                }
            }
        });


        btnSearchStudentIdNumber.addActionListener(e -> assessStudent());

        pnlMain.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                splitPane.setDividerLocation(0.75);
            }
        });

    }

    //<editor-fold defaultstate="collapsed" desc="Assessing">
     private synchronized void viewReport(JPanel pnl, String srcFile, HashMap<String,Object> m, boolean compiled) throws JRFillInterruptedException,SQLException,JRException {
        JasperPrint jp;
        if (compiled)
            jp = JasperFillManager.fillReport(srcFile, m, DB.getConnection());
        else{
            JasperReport jr = JasperCompileManager.compileReport(srcFile);
            jp = JasperFillManager.fillReport(jr, m, DB.getConnection());
        }
        JRViewer pnlReport= new JRViewer(jp);
        pnlReport.setZoomRatio(0.75f);
        SwingUtilities.invokeLater(()-> {
            for(int i=pnl.getComponentCount()-1;i>=0;i--){
                pnl.remove(i);
            }

            pnl.add(pnlReport);
            pnl.updateUI();
            pnl.repaint();
            running = false;
        });

    }


    private synchronized void viewAssessmentReport() throws JRFillInterruptedException{
        try {
            running = true;
            String srcFileCompiled = "src/jasperforms/COB.jasper";
            HashMap<String,Object> m = new HashMap<>();
            m.put("name",currentStudent.getFullName());
            m.put("studid",currentStudent.getStudId());
            m.put("yrandcourse","Year "+currentStudent.getYrlvl()+" in "+currentStudent.getCourse());
            m.put("sysem",currentStudent.getSem()+" "+currentStudent.getSy());
            m.put("curdate",currentStudent.getDate());
            m.put("subjectsDataSource",currentStudent.getSubjectsDataSource());
            m.put("feesDataSource",currentStudent.getFeesDataSource());
            m.put("remainingbalance",currentStudent.getRemainingBalance());
            m.put("scholarship",currentStudent.getScholarship());

            viewReport(pnlAssessmentView,srcFileCompiled,m,true);
        } catch (JRException | SQLException e) {
            e.printStackTrace();
        }
    }


    private synchronized void viewPermitReport() throws JRFillInterruptedException{
        try {
            running = true;
            String srcFileCompiled = "src/jasperforms/Permit.jasper";
            HashMap<String,Object> m = new HashMap<>();
            m.put("name",currentStudent.getFullName());
            m.put("studid",currentStudent.getStudId());
            m.put("yrlvl",currentStudent.getYrlvl());
            m.put("sysem",currentStudent.getSem()+" "+currentStudent.getSy());
            m.put("subjectsDataSource","");
            m.put("remainingbalance",currentStudent.getRemainingBalance());
            m.put("scholarship",currentStudent.getScholarship());
            m.put("minamountpayable","");


            viewReport(pnlPermitView,srcFileCompiled,m,true);
        } catch (JRException | SQLException e) {
            e.printStackTrace();
        }
    }

    private synchronized void viewClearanceReport() throws JRFillInterruptedException{
         try {
            running = true;
            String srcFileCompiled = "src/jasperforms/Clearance.jasper";
            HashMap<String,Object> m = new HashMap<>();
            m.put("name",currentStudent.getFullName());
            m.put("studid",currentStudent.getStudId());
            m.put("yrlvl",currentStudent.getYrlvl());
            m.put("sysem",currentStudent.getSem()+" "+currentStudent.getSy());
            m.put("subjectsDataSource","");
            m.put("remainingbalance",currentStudent.getRemainingBalance());
            m.put("scholarship",currentStudent.getScholarship());
            m.put("minamountpayable","");

            viewReport(pnlClearanceView,srcFileCompiled,m,true);
        } catch (JRException | SQLException e) {
            e.printStackTrace();
        }
    }


    private void assessStudent(){
        String tmpstudid = txtSearchStudentIdNumber.getText();
        if(tmpstudid.equals(studid))
            return;
        studid = tmpstudid;
        if(currentStudent!=null)
            prevStudent = currentStudent;
        currentStudent = new Student(studid);

        addLoading(pnlAssessmentView);
        addLoading(pnlPermitView);
        addLoading(pnlClearanceView);

        if(running) {
            thrdAssessment.interrupt();
            thrdPermit.interrupt();
            thrdClearance.interrupt();
            running = false;
        }

        thrdAssessment = new Thread(() -> {try{viewAssessmentReport();}catch (JRFillInterruptedException e){System.out.println("Cancelled jasper view request!");}});
        thrdAssessment.start();

        thrdPermit = new Thread(() -> {try{viewPermitReport();}catch (JRFillInterruptedException e){System.out.println("Cancelled jasper view request!");}});
        thrdPermit.start();

        thrdClearance= new Thread(() -> {try{viewClearanceReport();}catch (JRFillInterruptedException e){System.out.println("Cancelled jasper view request!");}});
        thrdClearance.start();

        //TODO: removthis shit
        showSummaryAssessment();
    }

    private void addLoading(JPanel pnl){
         if(!running&& pnl.getComponentCount()<2) {

             JLabel lblLoading = new JLabel();
             lblLoading.setText("Loading Report View . . .");
             pnl.add(lblLoading);
             pnl.updateUI();
             pnl.repaint();
         }
    }

    private void showSummaryAssessment(){
        SummaryAssessmentSy sssy = new SummaryAssessmentSy("2013 - 2014");

        sssy.attach(pnlSummary);
    }

    //</editor-fold>

    private void init(){
        frame = new JFrame();

        setName();

        pnlAssessmentView.setLayout(new BoxLayout(pnlAssessmentView,BoxLayout.PAGE_AXIS));
        pnlPermitView.setLayout(new BoxLayout(pnlPermitView,BoxLayout.PAGE_AXIS));
        pnlClearanceView.setLayout(new BoxLayout(pnlClearanceView,BoxLayout.PAGE_AXIS));

        txtSearchStudentIdNumber.requestFocus();
        scrlSummary.getVerticalScrollBar().setUnitIncrement(16);
        txtSearchStudentIdNumber.setToolTipText("You Can Press to move the focus here!");
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


    void show(){
        JFrameHelper.show(frame,pnlMain,"Students Billing System of DOSCST",true);
        SwingUtilities.invokeLater(() -> {
            try{
                MaskFormatter formatter = new MaskFormatter("#### - ####");
                formatter.setPlaceholderCharacter('0');
                txtSearchStudentIdNumber.setFormatterFactory(new DefaultFormatterFactory(formatter));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });


        SwingUtilities.invokeLater(()-> splitPane.setDividerLocation(0.8));
    }
    //</editor-fold>

}
