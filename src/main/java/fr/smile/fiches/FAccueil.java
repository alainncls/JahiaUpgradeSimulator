package fr.smile.fiches;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.smile.listeners.JahiaConfigServiceListener;
import fr.smile.models.Simulation;
import fr.smile.services.JahiaConfigService;
import fr.smile.services.PatchService;

@SuppressWarnings("serial")
public class FAccueil extends JFrame implements JahiaConfigServiceListener {

    private JPanel contentPane, pRed, pOrange, pGreen;
    private JLabel lProblems, lAvoid, lPredicted, lblT, lStart, lEnd, lReboots,
    lLicences;

    private JComboBox<String> cbStart, cbEnd;

    private JButton bSimulate, bPatches, bChiffrage, bConfig;

    private ButtonGroup bGroup;
    private JRadioButton rbClustered, rbStandalone;

    private FPatches patches;
    private FChiffrage chiffrage;
    private FConfig config;

    private transient Simulation simul;
    private String startVersion, endVersion, detectedVersion;
    private transient List<String> listVersions;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(FAccueil.class);

    public FAccueil() { // jahiaFolder context

        listVersions = PatchService.getInstance().getVersions();
        detectedVersion = JahiaConfigService.getInstance().getVersion();
        JahiaConfigService.getInstance().addListener(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        pRed = new JPanel();
        pRed.setBackground(Color.RED);
        pRed.setBounds(247, 125, 21, 21);
        contentPane.add(pRed);

        pOrange = new JPanel();
        pOrange.setBackground(Color.ORANGE);
        pOrange.setBounds(247, 91, 21, 21);
        contentPane.add(pOrange);

        pGreen = new JPanel();
        pGreen.setBackground(Color.GREEN);
        pGreen.setBounds(247, 58, 21, 21);
        contentPane.add(pGreen);

        lPredicted = new JLabel("");
        lPredicted.setBounds(286, 61, 150, 15);
        contentPane.add(lPredicted);

        lAvoid = new JLabel("");
        lAvoid.setBounds(286, 94, 150, 15);
        contentPane.add(lAvoid);

        lProblems = new JLabel("");
        lProblems.setBounds(286, 128, 150, 15);
        contentPane.add(lProblems);

        lReboots = new JLabel("");
        lReboots.setBounds(286, 158, 150, 15);
        contentPane.add(lReboots);

        lLicences = new JLabel("");
        lLicences.setBounds(286, 188, 150, 15);
        contentPane.add(lLicences);

        pGreen.setVisible(false);
        pOrange.setVisible(false);
        pRed.setVisible(false);
        lPredicted.setVisible(false);
        lAvoid.setVisible(false);
        lReboots.setVisible(false);
        lLicences.setVisible(false);
        lProblems.setVisible(false);

        lblT = new JLabel("Jahia Upgrade Simulator");
        lblT.setBounds(5, 5, 438, 19);
        lblT.setFont(new Font("Dialog", Font.BOLD, 16));
        lblT.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(lblT);

        lStart = new JLabel("Current version");
        lStart.setBounds(12, 58, 117, 15);
        contentPane.add(lStart);

        cbStart = new JComboBox<String>();
        cbStart.setModel(new DefaultComboBoxModel(listVersions.subList(0,
                listVersions.size() - 1).toArray()));
        cbStart.setBounds(137, 53, 92, 24);
        contentPane.add(cbStart);

        lEnd = new JLabel("Target version");
        lEnd.setBounds(12, 94, 117, 15);
        contentPane.add(lEnd);

        cbEnd = new JComboBox<String>();
        cbEnd.setModel(new DefaultComboBoxModel(listVersions.subList(1,
                listVersions.size()).toArray()));
        cbEnd.setBounds(137, 89, 92, 24);
        cbEnd.setSelectedIndex(cbEnd.getItemCount() - 1);
        contentPane.add(cbEnd);

        cbStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cbEnd.setModel(new DefaultComboBoxModel(listVersions.subList(
                        cbStart.getSelectedIndex() + 1, listVersions.size())
                        .toArray()));
                cbEnd.setSelectedIndex(cbEnd.getItemCount() - 1);
            }
        });
        if (detectedVersion != null) {
            cbStart.setSelectedItem(detectedVersion);
        }

        bSimulate = new JButton("Simulate !");
        bSimulate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                bSimulateActionPerformed(

                        );
            }
        });
        bSimulate.setBounds(12, 234, 117, 25);
        bSimulate.setEnabled(false);
        contentPane.add(bSimulate);

        bChiffrage = new JButton("Chiffrage");
        bChiffrage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                bChiffrageActionPerformed();
            }
        });
        bChiffrage.setBounds(130, 234, 117, 25);
        bChiffrage.setEnabled(false);
        contentPane.add(bChiffrage);

        bGroup = new ButtonGroup();

        rbClustered = new JRadioButton();
        rbClustered.setText("Clustered");
        rbClustered.setBounds(157, 158, 96, 23);
        rbClustered.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                enableSimulation();
            }
        });
        bGroup.add(rbClustered);
        contentPane.add(rbClustered);

        rbStandalone = new JRadioButton();
        rbStandalone.setText("Standalone");
        rbStandalone.setBounds(22, 158, 117, 23);
        rbStandalone.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                enableSimulation();
            }
        });
        bGroup.add(rbStandalone);
        contentPane.add(rbStandalone);

        bPatches = new JButton("Go to patches >>");
        bPatches.setBounds(279, 234, 157, 25);
        bPatches.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                goPatches();
            }
        });
        bPatches.setEnabled(false);
        contentPane.add(bPatches);

        bConfig = new JButton("Change config");
        bConfig.setBounds(48, 125, 157, 25);
        bConfig.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                goConfig();
            }
        });
        contentPane.add(bConfig);
        config = new FConfig();
        config.setVisible(false);
    }

    private void bSimulateActionPerformed() {
        startVersion = cbStart.getSelectedItem().toString();
        endVersion = cbEnd.getSelectedItem().toString();

        simul = new Simulation(startVersion, endVersion,
                rbClustered.isSelected());

        if (simul.getError() != "") {
            JOptionPane.showMessageDialog(null, simul.getError(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {

            lPredicted.setText(Integer.toString(simul.getSteps())
                    + " predicted steps");
            lAvoid.setText(Integer.toString(simul.getStepsA())
                    + " avoided steps");
            lProblems.setText(Integer.toString(simul.getStepsP())
                    + " steps to check");
            lReboots.setText(Integer.toString(simul.getReboots())
                    + " reboots needed");
            lLicences.setText(Integer.toString(simul.getLicences())
                    + " licences needed");

            pGreen.setVisible(true);
            pOrange.setVisible(true);
            pRed.setVisible(true);

            lPredicted.setVisible(true);
            lAvoid.setVisible(true);
            lProblems.setVisible(true);
            lReboots.setVisible(true);
            lLicences.setVisible(true);

            bPatches.setEnabled(true);
            bChiffrage.setEnabled(true);

            patches = new FPatches(simul);
            patches.setVisible(false);
            chiffrage = new FChiffrage(simul);
            chiffrage.setVisible(false);
        }
    }

    private void enableSimulation() {
        if (!bSimulate.isEnabled()) {
            bSimulate.setEnabled(true);
        }
    }

    public void goPatches() {
        patches.setVisible(true);
    }

    public void goConfig() {
        if (config == null) {
            LOGGER.error("CONFIG EST NULL");
        }
        config.setVisible(true);
    }

    public void bChiffrageActionPerformed() {
        chiffrage.setVisible(true);
    }

    public static void main() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    FAccueil frame = new FAccueil();
                    frame.setVisible(true);
                } catch (Exception e) {
                    LOGGER.error("", e);
                }
            }
        });
    }

    @Override
    public void notifyVersionChange(String version) {
        cbStart.setSelectedItem(version);
        bPatches.setEnabled(false);
        bChiffrage.setEnabled(false);
    }
}
