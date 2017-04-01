package forms;

import utils.JFrameHelper;

import javax.swing.*;

/**
 * Created by dpunk12 on 3/24/2017.
 */
public class EditAssessment {

    private JPanel pnlMain;
    private JTable table1;

    private JFrame frame;


    public void show(){
        frame = new JFrame();
        JFrameHelper.show(frame,pnlMain,"Edit Assessment");
    }


}
