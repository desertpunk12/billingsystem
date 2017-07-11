package forms;

import javax.swing.*;
import java.awt.event.*;

public class EditSelectedFeeMisc extends JDialog {
    private JPanel contentPane;
    private JButton btnOk;
    private JButton btnCancel;
    private JTextField txtFeecode;
    private JTextField txtSy;
    private JTextField txtSem;
    private JTextField txtDay1;
    private JTextField txtDay2;
    private JTextField txtDay3;
    private JTextField txtDay4;
    private JTextField txtDay5;
    private JTextField txtNight1;
    private JTextField txtNight2;
    private JTextField txtNight3;
    private JTextField txtNight4;
    private JTextField txtNight5;

    public EditSelectedFeeMisc() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(btnOk);
        setTitle("Edit Selected Fee");

        listeners();

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        pack();
        setVisible(true);
    }

    private void listeners() {
        btnOk.addActionListener(e ->
                onOK());

        btnCancel.addActionListener(e ->
                onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


}
