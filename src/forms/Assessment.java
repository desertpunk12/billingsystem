package forms;

import classess.Student;
import models.SchoolYear;
import models.Sem;
import models.SummaryAssessment;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.fill.JRFillInterruptedException;
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
import java.text.DateFormat;
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
    private JButton btnEditAssessment;
    private JScrollPane scrlSummary;
    private JPanel pnlPermitView;
    private JPanel pnlClearanceView;
    private JComboBox cmbPrevSy;
    private JPasswordField hiddenSelectedSem;
    private JButton btnPrintAssessment;
    private JButton btnPrintPermit;
    private JButton btnPrintClearance;
    private JPanel pnlNewAssessment;
    //</editor-fold>

    private boolean isAdmin;

    private Student currentStudent;
    private Student prevStudent;

    private Thread thrdAssessment;
    private Thread thrdPermit;
    private Thread thrdClearance;
    private volatile boolean running = false;

    private volatile String studid;
    private volatile String sy;
    private volatile char sem;

//    private JPasswordField hiddenSelectedSem;

    private SummaryAssessment smryAss;

    public Assessment(boolean isAdmin) {
        this.isAdmin = isAdmin;
        System.out.println(isAdmin);
        init();
        listeners();
//        btnSearchStudentByName.setFocusable(false);//should change this latur
        btnFees.setFocusable(false);//should change this latur
//        hiddenSelectedSem = new JPasswordField();
//        hiddenSelectedSem.setVisible(false);
//        SwingUtilities.invokeLater(()->pnlSummary.add(hiddenSelectedSem));

    }


    private void listeners(){

        hiddenSelectedSem.addActionListener(e -> assessStudent(studid) );

        cmbPrevSy.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED){
                summaryAssessmentSchoolYearSelected();
            }
        });

        btnEditAssessment.addActionListener((e)->{
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

//        btnSearchStudentByName.addActionListener(e -> {
//
//            SearchStudentByName ssbn = new SearchStudentByName(txtSearchStudentIdNumber);
//            JFrame frame = new JFrame("Search Student By Name");
//            frame.setContentPane(ssbn.getMainPanel());
//            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//            frame.pack();
//            frame.setLocationRelativeTo(null);
//            frame.setVisible(true);
//
//        });

        txtSearchStudentIdNumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ENTER){
                    btnSearchStudentIdNumber.doClick();
                }
            }
        });


        btnSearchStudentIdNumber.addActionListener(e -> showSummaryAssessment());

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

    private Double getOldAccounts(String studid) throws SQLException{
        String query = "SELECT (allassessmentsamount-totalamountpaidforallsems) as oldaccounts FROM\n" +
                "(SELECT sum(amt) as allassessmentsamount FROM srgb.ass_details WHERE studid='"+studid+"') ass,\n" +
                "(SELECT sum(amt) as totalamountpaidforallsems FROM srgb.collection_details JOIN srgb.collection_header USING(orno) WHERE studid='"+studid+"') paid\n" +
                ";";
        System.out.println(query);

        ResultSet rs = DB.query(query);
        rs.next();
        return rs.getDouble(1);
    }


    private synchronized void viewAssessmentReport() throws JRFillInterruptedException, JRException, SQLException{
        running = true;
        String srcFileCompiled = "src/jasperforms/COB.jasper";
        HashMap<String,Object> m = new HashMap<>();
        m.put("name",currentStudent.getFullName());
        m.put("studid",currentStudent.getStudId());
        m.put("yrandcourse","Year "+currentStudent.getYrlvl()+" in "+currentStudent.getCourse());
        m.put("sysem","Term "+sem+" "+sy);
        if(currentStudent.getGenDate()!=null) {
            System.out.println(currentStudent.getGenDate());
            m.put("gendate", currentStudent.getGenDate());
        }else System.out.println("GEEEEEEEEEENNNNNNNNNNNNNNNNNNN DAAAAAAAAAAAAAAAATEEEEEEEEEEEEE NNNNNNNNNNNNNNNUUUUUUUUUUUUULLLLL!!!");
        m.put("subjectsDataSource",currentStudent.getSubjectsDataSource());
        m.put("feesDataSource",currentStudent.getFeesDataSource());
        m.put("oldaccounts",getOldAccounts(currentStudent.getStudId()));
        m.put("scholarship",currentStudent.getScholarship());

        viewReport(pnlAssessmentView,srcFileCompiled,m,true);
    }


    private synchronized void viewPermitReport() throws JRFillInterruptedException, JRException, SQLException{
        running = true;
        String srcFileCompiled = "src/jasperforms/Permit.jasper";
        HashMap<String,Object> m = new HashMap<>();
        m.put("name",currentStudent.getFullName());
        m.put("studid",currentStudent.getStudId());
        m.put("yrlvl",""+currentStudent.getYrlvl());
        m.put("sysem","Term "+sem+" "+sy);
        m.put("subjectsDataSource1",currentStudent.getSubjectsDataSource());
        m.put("subjectsDataSource2",currentStudent.getSubjectsDataSource());
        m.put("scholarship",currentStudent.getScholarship());
        m.put("minimumamount", Math.min(currentStudent.getTotalAssessment()/3,currentStudent.getRemainingBalance()));
        m.put("remainingbalance", currentStudent.getRemainingBalance());
        viewReport(pnlPermitView,srcFileCompiled,m,true);
    }

    private synchronized void viewClearanceReport() throws JRFillInterruptedException, JRException, SQLException{
        running = true;
        String srcFileCompiled = "src/jasperforms/Clearance.jasper";
        HashMap<String,Object> m = new HashMap<>();
        m.put("name",currentStudent.getFullName());
        m.put("studid",currentStudent.getStudId());
        m.put("yrlvl",currentStudent.getYrlvl());
        m.put("sy",sy);
        m.put("sem",sem+"");
        m.put("course",currentStudent.getCourse());
        m.put("subjectsDataSource",currentStudent.getSubjectsDataSource());
        m.put("remainingbalance", currentStudent.getRemainingBalance());

        viewReport(pnlClearanceView,srcFileCompiled,m,true);
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

    private void assessStudent(String studid){
        String tmpsy = cmbPrevSy.getSelectedItem().toString();
        char tmpsem = hiddenSelectedSem.getPassword()[0];

        if( tmpsy==null || sem=='0'  ) {
            System.out.println("Parehas"+studid);
            return;
        }

        sy = tmpsy;
        sem = tmpsem;

        if(currentStudent!=null)
            prevStudent = currentStudent;
        //TODO: Please changed this really ugly code!!
        Sem ssemm = smryAss.getSchoolYears().get(cmbPrevSy.getSelectedIndex()).getSem(sem);
        currentStudent = new Student(studid,sy,sem,ssemm.getRemainingBalance(),ssemm.getTotalAssessment());
        System.out.println("Balance::: "+smryAss.getSchoolYears().get(cmbPrevSy.getSelectedIndex()).getSem(sem).getRemainingBalance());

        addLoading(pnlAssessmentView);
        addLoading(pnlPermitView);
        addLoading(pnlClearanceView);

        if(running) {
            thrdAssessment.interrupt();
            thrdPermit.interrupt();
            thrdClearance.interrupt();
            running = false;
        }


        thrdAssessment = new Thread(() -> {try{viewAssessmentReport();}catch (JRFillInterruptedException e){System.out.println("Cancelled jasper view request!");}catch (JRException | SQLException e1){e1.printStackTrace();}});
        thrdAssessment.start();

        thrdPermit = new Thread(() -> {try{viewPermitReport();}catch (JRFillInterruptedException e){System.out.println("Cancelled jasper view request!");}catch (JRException | SQLException e1){e1.printStackTrace();}});
        thrdPermit.start();

        thrdClearance= new Thread(() -> {try{viewClearanceReport();}catch (JRFillInterruptedException e){System.out.println("Cancelled jasper view request!");}catch (JRException | SQLException e1){e1.printStackTrace();}});
        thrdClearance.start();

    }

    private void createNewAssessment(String studid){
        //get the current semester


    }


    private void showSummaryAssessment(){
        String tmpstudid = txtSearchStudentIdNumber.getText();
        if(tmpstudid.equals(studid))
            return;
        studid = tmpstudid;
        pnlAssessmentView.removeAll();
        pnlPermitView.removeAll();
        pnlClearanceView.removeAll();

        pnlAssessmentView.updateUI();
        pnlPermitView.updateUI();
        pnlClearanceView.updateUI();

        smryAss = new SummaryAssessment(studid);
        cmbPrevSy.removeAllItems();
        System.out.println("Removed all combo box years, Now adding the new years!!:::" + smryAss.getSchoolYears().size());
        for(SchoolYear schoolYear : smryAss.getSchoolYears()) {
            System.out.println(schoolYear.getSchoolYear());
            cmbPrevSy.addItem(schoolYear.getSchoolYear());
        }


    }

    private void summaryAssessmentSchoolYearSelected(){
//        pnlSummary.removeAll();
        for (int i = 0; i < pnlSummary.getComponentCount(); i++) {
            if(pnlSummary.getComponent(i) instanceof JPasswordField)
                continue;
            else
                pnlSummary.remove(pnlSummary.getComponent(i));
        }

        SchoolYear sySelected = smryAss.getSchoolYears().get(cmbPrevSy.getSelectedIndex());
        SummaryAssessmentSy sssy = new SummaryAssessmentSy(sySelected);
        sssy.attach(pnlSummary);

//        SwingUtilities.invokeLater(()-> assessStudent(studid));
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
                MaskFormatter formatter = new MaskFormatter("####-####");
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
