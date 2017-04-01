package forms;

import classess.Student;
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
import java.text.ParseException;
import java.util.HashMap;

public class Assessment {

    private boolean isAdmin;

    //<editor-fold defaultstate="collapsed" desc="UI Declarations">
    private JFrame frame;
    private JPanel pnlMain;
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
    private JButton btnEditAssessmentView;
    private JScrollPane scrlSummary;
    //</editor-fold>

    private Student currentStudent;
    private Student prevStudent;

    private Thread thrdAssessment;
    private volatile boolean running = false;
    private volatile String studid;

    public Assessment(boolean isAdmin) {
        this.isAdmin = isAdmin;
        System.out.println(isAdmin);
        init();
        listeners();
        btnSearchStudentByName.setFocusable(false);//should change this latur
        btnFees.setFocusable(false);//should change this latur
        txtSearchStudentIdNumber.requestFocus();
        scrlSummary.getVerticalScrollBar().setUnitIncrement(16);

        txtSearchStudentIdNumber.setToolTipText("You Can Press to move the focus here!");

    }

    public synchronized void viewReport(String studid) throws JRFillInterruptedException{
        try {
            running = true;
            String sourceFile = "src/jasperforms/COB.jrxml";
            String sourceFileCompiled = "src/jasperforms/COB.jasper";
            HashMap m = new HashMap();
            m.put("name",currentStudent.getFullName());
            m.put("studid",currentStudent.getStudId());
            m.put("yrandcourse","Year "+currentStudent.getYrlvl()+" in "+currentStudent.getCourse());
            m.put("sysem",currentStudent.getSem()+" "+currentStudent.getSy());
            m.put("curdate",currentStudent.getDate());
            m.put("subjectsDataSource",currentStudent.getSubjectsDataSource());
            m.put("feesDataSource",currentStudent.getFeesDataSource());

//            JasperReport jr = JasperCompileManager.compileReport(sourceFile);
            JasperPrint jp = JasperFillManager.fillReport(sourceFileCompiled,m,DB.getConnection());
            JRViewer pnlAssessmentReport = new JRViewer(jp);
            SwingUtilities.invokeLater(()->{

                for(int i=pnlAssessmentView.getComponentCount()-1;i>=0;i--){
                    pnlAssessmentView.remove(i);
                }

                pnlAssessmentView.add(pnlAssessmentReport);
                pnlAssessmentView.updateUI();
                pnlAssessmentView.repaint();
                running = false;
            });

        } catch (JRException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void listeners(){

        btnEditAssessmentView.addActionListener((e)->{
            EditAssessmentView editView = new EditAssessmentView();
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


        btnSearchStudentIdNumber.addActionListener(e -> {
            showAssessment();
        });

        pnlMain.addComponentListener(new ComponentAdapter() {
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
        String tmpstudid = txtSearchStudentIdNumber.getText();
        if(tmpstudid.equals(studid))
            return;

        studid = tmpstudid;


        if(currentStudent!=null)
            prevStudent = currentStudent;
        currentStudent = new Student(studid);
        if(!running && pnlAssessmentView.getComponentCount()<2) {
            JLabel lblLoading = new JLabel();
            lblLoading.setText("Loading Report View . . .");
            pnlAssessmentView.add(lblLoading);
            pnlAssessmentView.updateUI();
            pnlAssessmentView.repaint();
        }
        if(running) {
            thrdAssessment.interrupt();
            running = false;
        }else
//            try {
//                thrdAssessment.join(500);
//                running = false;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

        thrdAssessment = new Thread(() -> {
            try{
                viewReport(studid);
            }catch (JRFillInterruptedException e){
                System.out.println("Cancelled jasper view request!");
            }
        });
        thrdAssessment.start();
        //TODO: removthis shit
        showSummaryAssessment();
    }



    public void showPermit(){

    }


    public void showClearance(){}
    //</editor-fold>

    public void showSummaryAssessment(){
        SummaryAssessmentSy sssy = new SummaryAssessmentSy("2013 - 2014");

        sssy.attach(pnlSummary);
    }

    private void init(){
        setName();
        pnlAssessmentView.setLayout(new BoxLayout(pnlAssessmentView,BoxLayout.PAGE_AXIS));
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


        SwingUtilities.invokeLater(()-> splitPane.setDividerLocation(0.6));
    }
    //</editor-fold>

}
