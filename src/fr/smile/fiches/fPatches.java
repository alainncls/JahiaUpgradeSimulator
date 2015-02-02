package fr.smile.fiches;

import fr.smile.main.Patch;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class fPatches extends JDialog {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel;

    private JScrollPane spPatches;
    // private JEditorPane epPatches;

    private JButton backButton;
    

    private List<Patch> listPatches;
    private JList<Object> lPatches;

    public fPatches(List<Patch> listPatches) {

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 900, 600);

        this.listPatches = listPatches;

        contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPanel);
        contentPanel.setLayout(null);

        backButton = new JButton("<< Back");
        backButton.setBounds(769, 534, 117, 25);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                goBack(arg0);
            }
        });
        contentPanel.add(backButton);

        lPatches = new JList(listPatches.toArray());
        lPatches.setBounds(5, 5, 881, 517);
        lPatches.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                if (!arg0.getValueIsAdjusting()) {
                    Patch p = (Patch) lPatches.getSelectedValue();
                    try {
                        Desktop.getDesktop().browse(new URI(p.getUrl()));
                    } catch (URISyntaxException | IOException ex) {
                        // It looks like there's a problem
                    }
                }
            }
        });
        contentPanel.add(lPatches);
    }

    public void goBack(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
    }
}
