package forms;

import classess.*;
import models.Paid;
import models.SchoolYear;
import models.Sem;
import models.SummaryAssessment;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
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
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private JPanel pnlNewAssessmentView;
    private JTabbedPane tabPane;
    private JButton btnCreateAssessment;
    private JLabel lblPrivilage;
    private JButton btnHideOldAccounts
            ;
    //</editor-fold>

    private boolean isAdmin;

    private Student currentStudent;
    private Student prevStudent;

    private Thread thrdAssessment;
    private Thread thrdPermit;
    private Thread thrdClearance;
    private Thread thrdNewAssessment;
    private volatile boolean running = false;

    private volatile String studid;
    private volatile String sy;
    private volatile char sem;

    private boolean hideOldAccount = false;
    private boolean refresh = false;

//    private JPasswordField hiddenSelectedSem;

    private SummaryAssessment smryAss;
    private AssessmentDefaultFees newAssessFees;
    private Double newoldaccounts;

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
        if(!isAdmin){
            btnFees.setVisible(false);
            lblPrivilage.setText(" as Normal user");
        }
        lblPrivilage.setText(" as Admin");

    }


    private void listeners(){
        btnHideOldAccounts.addActionListener(e -> {
            hideOldAccount = !hideOldAccount;
            try {
                viewNewAssessmentReport();
                viewAssessmentReport();
            } catch (JRException | SQLException e1) {
                e1.printStackTrace();
            }
            if(hideOldAccount)
                btnHideOldAccounts.setText("Show Old Accounts");
            else
                btnHideOldAccounts.setText("Hide Old Accounts");
        });

        btnCreateAssessment.addActionListener(e -> {
            try {
                createAssessment(newAssessFees);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        hiddenSelectedSem.addActionListener(e -> assessStudent(studid) );

        cmbPrevSy.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED){
                summaryAssessmentSchoolYearSelected();
            }
        });

        btnEditAssessment.addActionListener((e)->{
            EditAssessment dialog = new EditAssessment(newAssessFees);
            dialog.pack();
            dialog.setVisible(true);
            try {
                viewNewAssessmentReport();
            } catch (JRException | SQLException e1) {
                e1.printStackTrace();
            }
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
                try {
                    DB.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
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

    private Double getTotalAmountPayable(String studid) throws SQLException{
        String query = "SELECT (allassessmentsamount-totalamountpaidforallsems) as oldaccounts FROM\n" +
                "(SELECT sum(amt) as allassessmentsamount FROM srgb.ass_details WHERE studid='"+studid+"') ass,\n" +
                "(SELECT sum(amt) as totalamountpaidforallsems FROM srgb.collection_details JOIN srgb.collection_header USING(orno) WHERE studid='"+studid+"') paid\n" +
                ";";
        System.out.println(query);

        ResultSet rs = DB.query(query);
        rs.next();
        System.out.println("TOTOTOTOTOOTTAAAAALLLLLL AMOUNNTTT PAYYABLE::"+rs.getDouble(1));
        return rs.getDouble(1);
    }

    private Double getOldAccounts(String studid, String sy, char sem) throws SQLException{
        String query = "SELECT (totalassessment-totalpaid) oldaccount FROM\n" +
                "\n" +
                "  (\n" +
                "    SELECT sum(total) totalassessment\n" +
                "    FROM\n" +
                "      (\n" +
                "        SELECT sum(amt) total\n" +
                "        FROM srgb.ass_details\n" +
                "        WHERE studid = '"+studid+"' AND sy < '"+sy+"'\n" +
                "        UNION\n" +
                "        SELECT sum(amt) total\n" +
                "        FROM srgb.ass_details\n" +
                "        WHERE studid = '"+studid+"' AND sy = '"+sy+"' AND sem < '"+sem+"'\n" +
                "      ) sxs\n" +
                "    ) sx,\n" +
                "  (\n" +
                "    SELECT sum(total) totalpaid\n" +
                "    FROM\n" +
                "    (\n" +
                "        SELECT sum(amt) total\n" +
                "        FROM srgb.collection_details JOIN srgb.collection_header USING (orno)\n" +
                "        WHERE studid='"+studid+"' AND sy < '"+sy+"'\n" +
                "        UNION\n" +
                "        SELECT sum(amt) total\n" +
                "        FROM srgb.collection_details JOIN srgb.collection_header USING (orno)\n" +
                "        WHERE studid = '"+studid+"' AND sy = '"+sy+"' AND sem < '"+sem+"'\n" +
                "    )sys\n" +
                "  ) sy\n" +
                ";\n";
        System.out.println(query);

        ResultSet rs = DB.query(query);
        rs.next();
        System.out.println("OOOOOOLLLLLDDDDD ACCCCOUUUNNNTTTTSSS::::"+rs.getDouble(1));
        return rs.getDouble(1);
    }

    private void applyScholarshipDiscount(AssessmentDefaultFees assdef,ArrayList<AssessmentFee> labasdef,String schcode) throws SQLException{
        String query = "select tuition_disc,lab_disc,misc_disc,specialprogram from srgb.scholar where schcode='"+schcode+"';";
        ResultSet rs = DB.query(query);
        if(!rs.next()){
            System.out.println("SCHOLARSHIP WITH THAT CODE NOT FOUND!!!!!!");
            return;
        }
        double tui_disc = rs.getInt(1);
        double lab_disc = rs.getInt(2);
        double misc_disc = rs.getInt(3);
        double specialprogram = rs.getInt(4);

        System.out.println("TUITION disc:"+tui_disc);

        if(tui_disc!=0 || misc_disc!=0) {
            for (int i = 0; i < assdef.getFeeList().size(); i++) {
                String feecode = assdef.getFeeList().get(i).getCode();
                System.out.println(feecode);
                double amt = assdef.getFeeList().get(i).getAmount();
                if (tui_disc != 0 && feecode.contains("TUITIONFEE")) {
                    amt = amt - (amt * (tui_disc / 100));
                    System.out.println("TUTION NEW AMOUNT AFETER SCHOLAR CALCULATION : " + amt + " , scohlarship discount is " + tui_disc + " adn scholarship code is " + schcode);
                    assdef.getFeeList().get(i).setAmount(amt);
                } else if(misc_disc!= 0 && !feecode.contains("INSURANCE")){
                    amt = amt - (amt * (misc_disc / 100));
                    System.out.println(feecode+" NEW AMOUNT AFETER SCHOLAR CALCULATION : " + amt + " , scohlarship discount is " + tui_disc + " adn scholarship code is " + schcode);
                    assdef.getFeeList().get(i).setAmount(amt);
                }
            }
        }

        if(lab_disc!=0){
            for (int i = 0; i < labasdef.size(); i++) {
                String feecode = labasdef.get(i).getCode();
                double amt = assdef.getFeeList().get(i).getAmount();
                amt = amt - (amt * (lab_disc/100));
                System.out.println(feecode+" NEW AMOUNT AFETER SCHOLAR CALCULATION : " + amt + " , scohlarship discount is " + tui_disc + " adn scholarship code is " + schcode);
                labasdef.get(i).setAmount(amt);
            }
        }




    }

    private ArrayList<AssessmentFee> getLabFees(String studid,String sy,char sem,int yrlvl, boolean isNightClass) throws SQLException{
        String query="SELECT subjcode,feecode,feedesc,subjlab,subjcredit,feedist,yrlvl"+yrlvl+"_" + (isNightClass?"night":"day") +
                "\n  samt FROM srgb.registration\n" +
                "  LEFT JOIN srgb.subject sub USING (subjcode)\n" +
                "  LEFT JOIN srgb.deptlab dep ON(dep.deptcode=sub.subjdept)\n" +
                "  LEFT JOIN srgb.fees fee USING(feecode)\n" +
                "  LEFT JOIN srgb.labmatrix USING(sy,sem,subjcode)\n" +
                "  WHERE studid='"+studid+"' AND sy='"+sy+"' AND sem='"+sem+"' AND subjlab IS NOT NULL AND subjlab != 0\n" +
                "   ORDER BY feecode,feedesc;";

        System.out.println(query);
        ArrayList<AssessmentFee> fees = new ArrayList<>();
        ArrayList<AssessmentLabFee> labfees = new ArrayList<>();
        ResultSet rs = DB.query(query);
        if(rs.next()){
            labfees.add(new AssessmentLabFee(rs.getString(1),rs.getString(2),rs.getString(3),rs.getDouble(4),rs.getDouble(5),rs.getString(6),rs.getDouble(7)));
        }else{
            System.out.println("No Lab Fees");
            return null;
        }

        while (rs.next()){
            labfees.add(new AssessmentLabFee(rs.getString(1),rs.getString(2),rs.getString(3),rs.getDouble(4),rs.getDouble(5),rs.getString(6),rs.getDouble(7)));
        }
        String fc = labfees.get(0).getFeecode();
        double amt = labfees.get(0).getAmt();
        for (int i = 1; i < labfees.size(); i++) {
            if(fc.equals(labfees.get(i).getFeecode())){
                amt+=labfees.get(i).getAmt();
                continue;
            }
            fees.add(new AssessmentFee(fc,amt,labfees.get(i-1).getFeedesc()));
            fc = labfees.get(i).getFeecode();
            amt = labfees.get(i).getAmt();
        }


        return fees;
    }

    private synchronized void viewNewAssessmentReport() throws JRFillInterruptedException, JRException, SQLException{
//        running = true;
        //CHECK if student already has assessment for the current sem
        String query = "SELECT * FROM srgb.ass_header JOIN srgb.semester USING(sy,sem) WHERE current AND studid='"+studid+"';";
        if(DB.query(query).next()) {
            pnlNewAssessmentView.add(new JLabel("Student already has assessment for the current semester"));
            return;
        }


        String srcFileCompiled = "src/jasperforms/COB.jasper";
        HashMap<String,Object> m = new HashMap<>();
        m.put("studid",studid);


        query = "SELECT sy,sem,studlevel,studmajor,nightclass,schcode,schdesc,studfullname2 FROM\n" +
                "   srgb.semester \n" +
                "  LEFT JOIN srgb.semstudent USING(sy,sem)\n" +
                "   LEFT JOIN srgb.student USING(studid)\n" +
                "    LEFT JOIN srgb.scholar ON (schcode=scholarstatus)\n" +
                "  where current AND studid='"+studid+"';";
        System.out.println(query);
        ResultSet rs = DB.query(query);
        if(!rs.next()){
            System.out.println("No Semester is currenttt??? This is a server Error");
            return;
        }
        String curSy=rs.getString(1);
        char curSem=rs.getString(2).charAt(0);
        int yrlvl=rs.getInt(3);
        String course=rs.getString(4);
        boolean isNightClass=rs.getBoolean(5);
        String scholarCode=rs.getString(6);
        String scholarship=rs.getString(7);
        String fullname=rs.getString(8);
        m.put("name",fullname);
        m.put("sysem","Term "+curSem+" "+curSy);
        m.put("scholarship",scholarship);
        m.put("yrandcourse","Year "+yrlvl+" in "+course);
        AssessmentSubjects subjects = new AssessmentSubjects(studid,curSy,curSem);
//        if(newAssessFees==null)
            newAssessFees = new AssessmentDefaultFees(studid,curSy,curSem,yrlvl,isNightClass);
        //Add lab fee
        ArrayList<AssessmentFee> labasdef = getLabFees(studid,curSy,curSem,yrlvl,isNightClass);
        applyScholarshipDiscount(newAssessFees,labasdef,scholarCode);
        if(labasdef!=null)
            for (AssessmentFee asf: labasdef ) {
                newAssessFees.add(asf);
            }
        System.out.println("**********************************************************");
        for (AssessmentFee popqw : newAssessFees.getFeeList()) {
            System.out.println(popqw.getDesc());
        }
        System.out.println("**********************************************************");

        m.put("subjectsDataSource",subjects.getDataSource());
        m.put("feesDataSource",newAssessFees.getDataSource());//TODO: generate the default fees also account in the edit and the scholarship
//        if(newoldaccounts==null)
            newoldaccounts = getOldAccounts(studid,curSy,curSem);
        if(!hideOldAccount){
            m.put("oldaccounts",new DecimalFormat("#,##0.00").format(newoldaccounts));
            m.put("totalamountpayable",newoldaccounts+newAssessFees.getTotalAssessment());
        }else {
            m.put("oldaccounts","");
            m.put("totalamountpayable", newAssessFees.getTotalAssessment());
        }

        viewReport(pnlNewAssessmentView,srcFileCompiled,m,true);
    }

    private void createAssessment(AssessmentDefaultFees assdef) throws SQLException{
        String query = "";

        query = "INSERT INTO srgb.ass_header (SELECT sy,sem,'"+studid+"',NOW() FROM srgb.semester WHERE current);";
        System.out.println(query);
        DB.query(query,true);
        for (int i = 0; i < assdef.getFeeList().size(); i++) {
            query = "INSERT INTO srgb.ass_details (SELECT sy,sem,'"+studid+"' studid,'"+assdef.getFeeList().get(i).getCode()+"' feecode,"+assdef.getFeeList().get(i).getAmount()+" amt FROM srgb.semester where current);";
            System.out.println(query);
            DB.query(query,true);
        }
        refresh = true;
        showSummaryAssessment();
        System.out.println("Successfully cReted new assessment");
        JOptionPane.showMessageDialog(frame,"Successfully Created Assessment");
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
//        m.put("totalamountpayable",getTotalAmountPayable(currentStudent.getStudId())+currentStudent.getRemainingBalance());
        Double  oldaccounts = getOldAccounts(currentStudent.getStudId(),sy,sem);
        if(!hideOldAccount) {
            m.put("oldaccounts", new DecimalFormat("#,##0.00").format(oldaccounts));
            m.put("totalamountpayable", oldaccounts + currentStudent.getRemainingBalance());
        }else {
            m.put("oldaccounts","");
            m.put("totalamountpayable", currentStudent.getRemainingBalance());
        }
        m.put("scholarship",currentStudent.getScholarship());
        m.put("paidDataSource",fetchAssessmentPaidDataSource(currentStudent.getStudId(),sy,sem));
        System.out.println("NEW REPORTNGGG!");
        viewReport(pnlAssessmentView,srcFileCompiled,m,true);
    }

    private JRBeanCollectionDataSource fetchAssessmentPaidDataSource(String studid,String sy, char sem) throws SQLException{
        String query = "SELECT orno,sum(amt) AS  payed,paydate FROM srgb.collection_details JOIN srgb.collection_header USING(orno)\n" +
                "  WHERE studid='"+studid+"' AND sy='"+sy+"' AND sem='"+sem+"' GROUP BY orno,sy,sem,paydate ORDER BY sy,sem;\n";
        ResultSet rs = DB.query(query);
        ArrayList<Paid> paids = new ArrayList<>();
        SimpleDateFormat dfrmt = new SimpleDateFormat("MM/dd/yyyy");
        while(rs.next()){
            paids.add(new Paid(rs.getString(1),dfrmt.format(rs.getDate(3)),rs.getDouble(2)));
        }

        return new JRBeanCollectionDataSource(paids,false);
    }


    private synchronized void viewPermitReport() throws JRFillInterruptedException, JRException, SQLException{
        running = true;
        String srcFileCompiled = "src/jasperforms/Permit.jasper";
        HashMap<String,Object> m = new HashMap<>();
        m.put("name",currentStudent.getFullName());
        m.put("studid",currentStudent.getStudId());
        m.put("yrlvlcourse",currentStudent.getCourse().replace(" ","")+" "+currentStudent.getYrlvl());
        m.put("sysem","Term "+sem+" "+sy);
        m.put("subjectsDataSource1",currentStudent.getSubjectsDataSource());
        m.put("subjectsDataSource2",currentStudent.getSubjectsDataSource());
        m.put("scholarship",currentStudent.getScholarship());
        m.put("minimumamount", currentStudent.getRemainingBalance()/3);
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



    private void showSummaryAssessment(){
        newAssessFees = null;
        hideOldAccount = false;


        String tmpstudid = txtSearchStudentIdNumber.getText();
        if(tmpstudid.equals(studid) && !refresh)
            return;

        refresh = false;
        studid = tmpstudid;
        pnlNewAssessmentView.removeAll();
        pnlAssessmentView.removeAll();
        pnlPermitView.removeAll();
        pnlClearanceView.removeAll();

        pnlNewAssessmentView.updateUI();
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

        try {
            viewNewAssessmentReport();
        } catch (JRException | SQLException e) {
            e.printStackTrace();
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
        pnlNewAssessmentView.setLayout(new BoxLayout(pnlNewAssessmentView,BoxLayout.PAGE_AXIS));
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
