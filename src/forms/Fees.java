package forms;

import utils.DB;
import utils.JFrameHelper;

import javax.swing.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by dpunk12 on 2/2/2017.
 */
public class Fees {
    private JPanel pnlMain;
    private JTextField txtSearch;
    private JButton btnSearch;
    private JComboBox cmbFeetype;
    private JComboBox cmbSy;
    private JComboBox cmbSem;
    private JTable tblFees;
    private JButton btnRemoveDefaultFee;
    private JButton btnAddDefaultFee;

    private JFrame frame;


    private ArrayList<String> sys;


    private String qMis = "SELECT feecode, feedesc, sy, sem, yrlvl1_day,yrlvl2_day,yrlvl3_day,yrlvl4_day,yrlvl5_day,yrlvl1_night,yrlvl2_night,yrlvl3_night,yrlvl4_night,yrlvl5_night FROM srgb.fees JOIN srgb.miscfeematrix using(feecode)";
    private String qAss = "SELECT * FROM srgb.assessmentfee";
    private String qLab = "SELECT * FROM srgb.labmatrix";
    private String qTui = "SELECT * FROM srgb.tuitionmatrix";
    private String qDef = "SELECT sy,sem,feecode, feedesc,yrlvl1_day,yrlvl1_night,yrlvl2_day,yrlvl2_night,yrlvl3_day,yrlvl3_night,yrlvl4_day,yrlvl4_night,yrlvl5_day,yrlvl5_night FROM srgb.fees JOIN srgb.miscfeematrix USING (feecode) WHERE enrol=TRUE";

    private String qDefRemove = "UPDATE srgb.miscfeematrix SET enrol=FALSE WHERE sy='%s' AND sem='%s' AND feecode='%s';";

    private String qSy = "SELECT sy FROM srgb.semester GROUP BY sy ORDER BY sy DESC;";

    private Thread thrdProcessTable;

    public Fees(){
        frame = new JFrame();
        sys = new ArrayList<>();
        tblFees.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        thrdProcessTable = new Thread(()->SwingUtilities.invokeLater(()-> processTableView()));
        thrdProcessTable.start();
        getSchoolYearsUpdateComboBox();
        listeners();

    }

    private void refreshTable(){
        if(thrdProcessTable.isAlive()){
            thrdProcessTable.interrupt();
            System.out.println("Success Interrpted Fees Thread!, " + thrdProcessTable.isAlive());
        }
        thrdProcessTable = new Thread(()->SwingUtilities.invokeLater(()-> processTableView()));
        thrdProcessTable.start();
    }

    private void listeners() {
        cmbFeetype.addItemListener(e -> {
            if(e.getStateChange()==ItemEvent.SELECTED){
                refreshTable();
            }
        });
        cmbSem.addItemListener(e -> {
            if(e.getStateChange()==ItemEvent.SELECTED){
                refreshTable();
            }
        });
        cmbSy.addItemListener(e -> {
            if(e.getStateChange()==ItemEvent.SELECTED){
                refreshTable();
            }
        });
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if(e.getKeyCode()==KeyEvent.VK_ENTER){
                    btnSearch.doClick();
                }
            }
        });
        btnSearch.addActionListener(e -> processTableView(true));

        btnAddDefaultFee.addActionListener(e -> {
            new AddDefaultFees(cmbSy.getSelectedIndex(),cmbSem.getSelectedIndex());
            refreshTable();
        });

        btnRemoveDefaultFee.addActionListener(e -> removeSelectedFeeAsDefault());
    }

    private void getSchoolYearsUpdateComboBox(){
        try{
            ResultSet rs = DB.query(qSy);
            while(rs.next()){
                cmbSy.addItem(rs.getString(1));
            }
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

    private String querySySemWhere(JComboBox cmbSy, JComboBox cmbSem){
        String query = "";
        if(cmbSy.getSelectedIndex()>0 || cmbSem.getSelectedIndex()>0){
                    query += " WHERE";
                    String sy = cmbSy.getSelectedItem().toString();
                    String sem = cmbSem.getSelectedItem().toString();
                    if(!sy.equals("All")){
                        query += " sy='"+sy+"'";
                    }
                    if(!sem.equals("All")){
                        if(!sy.equals("All"))
                            query += " AND";
                        query += " sem='"+sem+"'";
                    }
        }
        return query;
    }

    private void processTableView(){
        processTableView(false);
    }

    private void processTableView(boolean isSearch){
        String query = "";
        ResultSet rs;

        query = processQuery();

        if(isSearch) {
            String searchText = txtSearch.getText().toLowerCase();
            if (cmbSy.getSelectedIndex() == 0 || cmbSem.getSelectedIndex() == 0) {
                query += " WHERE";
            }else{
                query += " AND";
            }
            query += " (lower(feecode) LIKE '%" +searchText+ "%' OR lower(feedesc) LIKE '%" +searchText+ "%');";
            System.out.println("Seach Query: " + query);
        }

        try {
            rs = DB.query(query);
            System.out.println("Succeessfully Executed Query!");
            tblFees.setModel(DB.convertRStoTableModel(rs));
        }catch(Exception e){ e.printStackTrace(); }
    }

    private String processQuery(){
        String query = "";
        btnRemoveDefaultFee.setVisible(false);
        btnAddDefaultFee.setVisible(false);
        switch(cmbFeetype.getSelectedIndex()){
            case 0:
                query = qMis;
                query += querySySemWhere(cmbSy,cmbSem);
                break;
            case 1:
                query = qAss;
                break;
            case 2:
                query = qLab;
                query += querySySemWhere(cmbSy,cmbSem);
                break;
            case 3:
                query = qTui;
                query += querySySemWhere(cmbSy,cmbSem);
                break;
            case 4:
                query = qDef;
                query += querySySemDefaultFees(cmbSy,cmbSem);
                btnRemoveDefaultFee.setVisible(true);
                btnAddDefaultFee.setVisible(true);
                break;
        }

        return query;
    }

    private void removeSelectedFeeAsDefault(){
        if(tblFees.getSelectedRow()==-1) {
            JOptionPane.showMessageDialog(null, "You need to select a fee to delete");
            return;
        }
        String sy = String.valueOf(tblFees.getValueAt(tblFees.getSelectedRow(),0));
        String sem = String.valueOf(tblFees.getValueAt(tblFees.getSelectedRow(),1));
        String feecode = String.valueOf(tblFees.getValueAt(tblFees.getSelectedRow(),2));
        removeSelectedFeeAsDefault(sy,sem,feecode);
    }

    private void removeSelectedFeeAsDefault(String sy,String sem,String feecode){
        String query = String.format(qDefRemove,sy,sem,feecode);
        System.out.println(query);

        try {
            DB.query(query,true);
            System.out.println("Successfuly Remvoed "+feecode+" as a default fee!");
            refreshTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void show(){
        SwingUtilities.invokeLater(()->JFrameHelper.show(frame,pnlMain,"Fees",true));

    }
}
