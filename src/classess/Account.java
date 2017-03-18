package classess;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * Created by dpunk12 on 2/20/2017.
 */
public class Account {

    private int syStart;
    private String[][] data;

    public Account(int syStart){
        this.syStart = syStart;
        data =  new String[0][0];

    }



    public TableModel getFull(){
        String[] columnNames = new String[]{"Fee","Amount"};




        DefaultTableModel model = new DefaultTableModel(data,columnNames);
        return model;
    }

    public TableModel getSummary(){
        String[] columnNames = new String[]{"Fee","Amount"};



        DefaultTableModel model = new DefaultTableModel(data,columnNames);
        return model;
    }




}
