package fr.smile.fiches;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;

import fr.smile.models.Patch;
import fr.smile.models.Simulation;
import fr.smile.services.JahiaConfigService;

@SuppressWarnings("serial")
public class FPatches extends JDialog {

    private final JPanel contentPanel;
    private JPanel listPanel;
    private JButton backButton, bDownload, bInstruction;
    private JCheckBox cdownload, cbCheck;
    private JLabel lReboot, lLicense, lPatch;
    private ActionButton bAction;

    private FInstructions instructions;

    private transient Map<JCheckBox, ActionButton> checkBoxMap;

    public FPatches(final Simulation simu) {

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 900, 600);

        this.checkBoxMap = new HashMap<>();

        instructions = new FInstructions();

        contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPanel);
        contentPanel.setLayout(null);

        listPanel = new JPanel();
        listPanel.setBounds(5, 5, 881, 517);
        listPanel.setLayout(new SpringLayout());

        int index = 0;
        int nbCols = 0;

        for (final Patch p : simu.getListPatches()) {
            index++;

            lPatch = new JLabel(p.toString());
            lPatch.setSize(300, lPatch.getHeight());
            listPanel.add(lPatch);

            if (p.isProblem()) {
                JButton bWarning = new JButton("Warning !");
                bWarning.setBackground(Color.ORANGE);
                bWarning.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        toggleWarning(p);
                    }
                });
                listPanel.add(bWarning);
            } else {
                listPanel.add(new JLabel());
            }

            if (p.getInstructions(simu.getClustered()) != null) {
                bInstruction = new JButton("Instructions");
                bInstruction.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        toggleInstruction(p, simu.getClustered());
                    }
                });
                listPanel.add(bInstruction);
            } else {
                listPanel.add(new JLabel());
            }

            if (JahiaConfigService.getInstance().getVersion() != null) {

                bAction = new ActionButton(p);
                bAction.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ((ActionButton) e.getSource()).doAction();
                    }
                });
                listPanel.add(bAction);

                cbCheck = new JCheckBox();
                listPanel.add(cbCheck);

                checkBoxMap.put(cbCheck, bAction);
            }

            if (index == 1) {
                nbCols = listPanel.getComponentCount();
            }

            reboot(p, nbCols);
            licence(p, nbCols);

        }

        SpringUtilities.makeCompactGridRight(listPanel,// parent
                simu.getSteps() + simu.getReboots() + simu.getLicences(), // rows
                nbCols, // cols
                5, 5, // initX, initY
                5, 5, // xPad, yPad
                nbCols - 1); // number of cols to push right
        SpringUtilities.addLineSeparator(listPanel, nbCols,
                simu.getListPatches());

        JScrollPane scrollPane = new JScrollPane(listPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(8);
        scrollPane.setBounds(listPanel.getBounds());
        contentPanel.add(scrollPane);

        backButton = new BackButton(this);
        contentPanel.add(backButton);

        actionsToPerform();
    }

    private void actionsToPerform() {
        if (JahiaConfigService.getInstance().getVersion() != null) {
            bDownload = new JButton("Download Selected");
            bDownload.setBounds(661, 534, 180, 25);
            bDownload.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    runDownload();
                }
            });
            contentPanel.add(bDownload);

            cdownload = new JCheckBox();
            cdownload.setBounds(846, 534, 25, 25);
            cdownload.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    toggleCheckBox(evt);
                }
            });
            contentPanel.add(cdownload);
        }
    }

    private void reboot(Patch p, int nbCols) {
        if (p.needReboot()) {
            int nbCompInit = listPanel.getComponentCount();
            lReboot = new JLabel(
                    "You need to reboot your Jahia install after upgrading to "
                            + p.getEndVersion());
            lReboot.setBackground(Color.YELLOW);
            lReboot.setOpaque(true);
            listPanel.add(lReboot);
            int added = listPanel.getComponentCount() - nbCompInit;
            for (int i = added; i < nbCols; i++) {
                listPanel.add(new JLabel());
            }
        }
    }

    private void licence(Patch p, int nbCols) {
        if (p.needLicense()) {
            int nbCompInit = listPanel.getComponentCount();
            lLicense = new JLabel(
                    "You need to get a new license in order to upgrade to "
                            + p.getEndVersion());
            lLicense.setBackground(Color.ORANGE);
            lLicense.setOpaque(true);
            listPanel.add(lLicense);
            int added = listPanel.getComponentCount() - nbCompInit;
            for (int i = added; i < nbCols; i++) {
                listPanel.add(new JLabel());
            }
        }
    }

    private void runDownload() {
        for (JCheckBox c : checkBoxMap.keySet()) {
            if (c.isSelected()) {
                checkBoxMap.get(c).doDownload();
            }
        }
    }

    private void toggleCheckBox(ActionEvent evt) {
        JCheckBox cb = (JCheckBox) evt.getSource();
        for (JCheckBox c : checkBoxMap.keySet()) {
            c.setSelected(cb.isSelected());
        }
    }

    private void toggleWarning(Patch patch) {
        JOptionPane
        .showMessageDialog(
                null,
                new JLabel(
                        "<html><body style='width: 400px; text-align: justify; text-justify: inter-word;'>"
                                + patch.getWarning() + "</body></html>"));
    }

    private void toggleInstruction(Patch patch, Boolean isClustered) {
        instructions.setInstructions(patch.getInstructions(isClustered));
        instructions.setTitle("Instructions Patch " + patch.toString());
        instructions.setVisible(true);
    }
}
