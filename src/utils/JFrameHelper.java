package utils;


import javax.swing.*;

/**
 * Created by dpunk12 on 3/25/2017.
 */
public class JFrameHelper {


    public static void show(JFrame frame, JPanel pnl, String title){
        show(frame,pnl,title,false);
    }

    public static void show(JFrame frame, JPanel pnl, String title, boolean maximized){
        frame.setTitle(title);
        if(maximized)
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setContentPane(pnl);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
}
