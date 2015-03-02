package fr.smile.fiches;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;

public class BackButton {
    public BackButton() {

    }

    public JButton getBackButton(final JDialog dialog) {
        JButton backButton = new JButton("<< Back");
        backButton.setBounds(15, 534, 117, 25);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                goBack(dialog);
            }
        });
        return backButton;
    }

    public void goBack(JDialog dialog) {
        dialog.setVisible(false);
    }
}
