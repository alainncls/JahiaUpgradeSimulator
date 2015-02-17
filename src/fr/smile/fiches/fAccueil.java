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

import fr.smile.main.Simulation;
import fr.smile.services.JahiaConfigService;
import fr.smile.services.PatchService;

public class fAccueil extends JFrame {
	private static final long serialVersionUID = 1L;

	private JPanel contentPane, pRed, pOrange, pGreen;
	private JLabel lProblems, lAvoid, lPredicted, lblT, lStart, lEnd, lReboots;

	private JComboBox<String> cbStart, cbEnd;

	private JButton bSimulate, bPatches, bChiffrage;

	private ButtonGroup bGroup;
	private JRadioButton rbClustered, rbStandalone;

	private fPatches patches;
	private fChiffrage chiffrage;

	private Simulation simul;
	private String startVersion, endVersion, detectedVersion, context,
			jahiaFolder;
	private List<String> listVersions;

	public fAccueil(String[] args) { // jahiaFolder context

		context = args.length >= 2 ? args[1] : "ROOT";
		jahiaFolder = args.length >= 1 ? args[0] : "./";
		if (!jahiaFolder.endsWith("/")) {
			jahiaFolder += "/";
		}
		
		JahiaConfigService.getInstance().setFolder(jahiaFolder);
		JahiaConfigService.getInstance().setContext(context);
		JahiaConfigService.getInstance().detectJahiaVersion();

		listVersions = PatchService.getInstance().getVersions();
		detectedVersion = JahiaConfigService.getInstance().getVersion();

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

		pGreen.setVisible(false);
		pOrange.setVisible(false);
		pRed.setVisible(false);
		lPredicted.setVisible(false);
		lAvoid.setVisible(false);
		lReboots.setVisible(false);
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
		if (detectedVersion != null)
			cbStart.setSelectedItem(detectedVersion);

		bSimulate = new JButton("Simulate !");
		bSimulate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				bSimulateActionPerformed(evt);
			}
		});
		bSimulate.setBounds(12, 234, 117, 25);
		bSimulate.setEnabled(false);
		contentPane.add(bSimulate);
		
		bChiffrage = new JButton("Chiffrage");
		bChiffrage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				bChiffrageActionPerformed(evt);
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
			public void actionPerformed(ActionEvent evt) {
				enableSimulation(evt);
			}
		});
		bGroup.add(rbClustered);
		contentPane.add(rbClustered);

		rbStandalone = new JRadioButton();
		rbStandalone.setText("Standalone");
		rbStandalone.setBounds(22, 158, 117, 23);
		rbStandalone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				enableSimulation(evt);
			}
		});
		bGroup.add(rbStandalone);
		contentPane.add(rbStandalone);

		bPatches = new JButton("Go to patches >>");
		bPatches.setBounds(279, 234, 157, 25);
		bPatches.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				goPatches(evt);
			}
		});
		bPatches.setEnabled(false);
		contentPane.add(bPatches);

		/*
		 * Quick init for debug
		 */
		// cbStart.setSelectedItem("6.6.2.6");
		// cbEnd.setSelectedItem("7.0.0.4");
		// rbStandalone.doClick();
		// bSimulate.doClick();
		// bPatches.doClick();

	}

	private void bSimulateActionPerformed(ActionEvent evt) {
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

			pGreen.setVisible(true);
			pOrange.setVisible(true);
			pRed.setVisible(true);
			lPredicted.setVisible(true);
			lAvoid.setVisible(true);
			lProblems.setVisible(true);
			lReboots.setVisible(true);
			bPatches.setEnabled(true);
			bChiffrage.setEnabled(true);

			patches = new fPatches(simul);
			patches.setVisible(false);
			chiffrage = new fChiffrage(simul);
			chiffrage.setVisible(false);
		}
	}

	private void enableSimulation(ActionEvent evt) {
		if (!bSimulate.isEnabled()) {
			bSimulate.setEnabled(true);
		}
	}

	public void goPatches(ActionEvent evt) {
		patches.setVisible(true);
	}
	
	public void bChiffrageActionPerformed(ActionEvent evt) {
		chiffrage.setVisible(true);
	}

	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					fAccueil frame = new fAccueil(args);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
