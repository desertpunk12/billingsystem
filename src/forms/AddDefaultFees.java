package forms;

import utils.DB;

import javax.swing.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddDefaultFees extends JDialog {
    private JPanel contentPane;
    private JButton btnAccept;
    private JButton btnCancel;
    private JTable tblAddDefaultFee;
    private JComboBox cmbSchoolYear;
    private JComboBox cmbSem;

    private String qSy = "SELECT sy FROM srgb.semester GROUP BY sy ORDER BY sy DESC;";
    private String qNonDef = "SELECT feecode,feedesc,sy,sem FROM srgb.miscfeematrix JOIN srgb.fees USING (feecode) WHERE enrol=FALSE";
    private String qAddDef = "UPDATE srgb.miscfeematrix SET enrol=true WHERE sy='%s' AND sem='%s' AND feecode='%s'";

    private int prevSelectedSy;
    private int prevSelectedSem;


    private Thread thrdProcessTable;

    public AddDefaultFees( int sy,int sem) {
        this.prevSelectedSy = sy;
        this.prevSelectedSem = sem;
        getSchoolYearsUpdateComboBox();
        uiInit();
    }

    private void uiInit() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(btnAccept);
        tblAddDefaultFee.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listeners();
        refreshTable();
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void listeners() {
        cmbSchoolYear.addItemListener(e -> {
            if(e.getStateChange()==ItemEvent.SELECTED){
                refreshTable();
            }
        });
        cmbSem.addItemListener(e -> {
            if(e.getStateChange()==ItemEvent.SELECTED){
                refreshTable();
            }
        });
        btnAccept.addActionListener(e -> onOK());

        btnCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void refreshTable(){
        if(thrdProcessTable!=null && thrdProcessTable.isAlive()){
            thrdProcessTable.interrupt();
            System.out.println("Success Interrpted Fees Thread!, " + thrdProcessTable.isAlive());
        }
        thrdProcessTable = new Thread(()->SwingUtilities.invokeLater(()-> processTable()));
        thrdProcessTable.start();
    }

    private void getSchoolYearsUpdateComboBox(){
        try{
            ResultSet rs = DB.query(qSy);
            while(rs.next()){
                String sy = rs.getString(1);
                cmbSchoolYear.addItem(sy);
            }
            cmbSem.setSelectedIndex(prevSelectedSem);
            cmbSchoolYear.setSelectedIndex(prevSelectedSy);
        }catch (Exception e){e.printStackTrace();}
    }

    private String querySySemDefaultFees(JComboBox cmbSy, JComboBox cmbSem){
        String query="";
        if(cmbSy.getSelectedIndex()>0 || cmbSem.getSelectedIndex()>0){
            String sy = cmbSy.getSelectedItem().toString();
            String sem = cmbSem.getSelectedItem().toString();
            if(!sy.equals("All")){
                query+= " AND sy='"+sy+"'";
            }
            if(!sem.equals("All")){
                query+= " AND sem='"+sem+"'";
            }
        }
        return query;
    }

    private void processTable(){
        String query = qNonDef + querySySemDefaultFees(cmbSchoolYear,cmbSem);
        System.out.println(query);

        SwingUtilities.invokeLater(() -> {
            try{
                ResultSet rs = DB.query(query);
                tblAddDefaultFee.setModel(DB.convertRStoTableModel(rs));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void addSelectedAsDefaultFee(String sy,String sem,String feecode){
        String query = String.format(qAddDef, sy,sem, feecode );
        System.out.println(query);
        try {
            DB.query(query,true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addSelectedAsDefaultFee(){
        String feecode = String.valueOf(tblAddDefaultFee.getValueAt(tblAddDefaultFee.getSelectedRow(),0));
        String sy = String.valueOf(tblAddDefaultFee.getValueAt(tblAddDefaultFee.getSelectedRow(),2));
        String sem = String.valueOf(tblAddDefaultFee.getValueAt(tblAddDefaultFee.getSelectedRow(),3));
        addSelectedAsDefaultFee(sy,sem,feecode);
    }


    private void onOK() {
        // add your code here
        addSelectedAsDefaultFee();

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}
