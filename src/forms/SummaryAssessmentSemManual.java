package forms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * Created by dpunk12 on 11/27/2016.
 */
public class SummaryAssessmentSemManual {

    private JPanel mainPanel;
    private JTable table;

    private int sy;
    private int sem;

    public SummaryAssessmentSemManual(int sy, int sem){
        this.sy = sy;
        this.sem = sem;

        table = new JTable();
        table.setModel(processTableModel());

        mainPanel.add(table);
    }

    private TableModel processTableModel(){
        String[] columnTitles  = {"Fee Description","Fee Price", "Amount Paid"};
        String[][] data = new String[columnTitles.length][];

        return new DefaultTableModel(data,columnTitles);
    }



    public JPanel getPanel(){
        return mainPanel;
    }


}
