package fr.smile.fiches;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;

@SuppressWarnings("serial")
public class BackButton extends JButton {

    private JDialog dialog;

    public BackButton(JDialog dialog) {
        this.dialog = dialog;
        this.setText("<< Back");
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                goBack();
            }
        });
    }

    public void goBack() {
        dialog.setVisible(false);
    }
}
