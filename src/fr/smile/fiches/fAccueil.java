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
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import fr.smile.main.Simulation;
import fr.smile.reader.InstructionsReader;
import fr.smile.reader.VersionsReader;

public class fAccueil extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private JLabel lProblems;
	private JLabel lAvoid;
	private JLabel lPredicted;
	private JPanel pRed;
	private JPanel pOrange;
	private JPanel pGreen;
	private JLabel lblT;
	private JComboBox<String> cbStart;
	private JLabel lStart;
	private JComboBox<String> cbEnd;
	private JLabel lEnd;
	private JButton bSimulate;
	private JButton bInstructions;
	private JScrollPane spResult;
	private JTextPane tpResult;
	private JRadioButton rbClustered;
	private JRadioButton rbStandalone;
	private ButtonGroup bGroup;
	private JButton bPatches;

	private fInstructions instructions;
	private fPatches patches;

	private Simulation simul;
	private String startVersion;
	private String endVersion;
	private String installType;
	private List<String> listVersions;

	public fAccueil() {

		listVersions = VersionsReader.getInstance().getVersions();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		pRed = new JPanel();
		pRed.setBackground(Color.RED);
		pRed.setBounds(272, 123, 21, 21);
		contentPane.add(pRed);

		pOrange = new JPanel();
		pOrange.setBackground(Color.ORANGE);
		pOrange.setBounds(272, 89, 21, 21);
		contentPane.add(pOrange);

		pGreen = new JPanel();
		pGreen.setBackground(Color.GREEN);
		pGreen.setBounds(272, 56, 21, 21);
		contentPane.add(pGreen);

		lProblems = new JLabel("");
		lProblems.setBounds(311, 126, 125, 15);
		contentPane.add(lProblems);

		lAvoid = new JLabel("");
		lAvoid.setBounds(311, 92, 125, 15);
		contentPane.add(lAvoid);

		lPredicted = new JLabel("");
		lPredicted.setBounds(311, 59, 125, 15);
		contentPane.add(lPredicted);

		pGreen.setVisible(false);
		pOrange.setVisible(false);
		pRed.setVisible(false);
		lPredicted.setVisible(false);
		lAvoid.setVisible(false);
		lProblems.setVisible(false);

		lblT = new JLabel("Jahia Upgrade Simulator");
		lblT.setBounds(5, 5, 438, 19);
		lblT.setFont(new Font("Dialog", Font.BOLD, 16));
		lblT.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblT);
		
		cbStart = new JComboBox<String>();
		cbStart.setModel(new DefaultComboBoxModel(listVersions.subList(0, listVersions.size()-1).toArray()));
		cbStart.setBounds(161, 51, 92, 24);
		contentPane.add(cbStart);

		lStart = new JLabel("Current version");
		lStart.setBounds(12, 56, 149, 15);
		contentPane.add(lStart);

		cbEnd = new JComboBox<String>();
		cbEnd.setModel(new DefaultComboBoxModel(listVersions.subList(1, listVersions.size()).toArray()));
		cbEnd.setBounds(161, 87, 92, 24);
		cbEnd.setSelectedIndex(cbEnd.getItemCount()-1);
		contentPane.add(cbEnd);
		
		cbStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cbEnd.setModel(new DefaultComboBoxModel(listVersions.subList(cbStart.getSelectedIndex()+1, listVersions.size()).toArray()));
				cbEnd.setSelectedIndex(cbEnd.getItemCount()-1);
			}
		});

		lEnd = new JLabel("Target version");
		lEnd.setBounds(12, 92, 149, 15);
		contentPane.add(lEnd);

		bSimulate = new JButton("Simulate !");
		bSimulate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				bSimulateActionPerformed(arg0);
			}
		});
		bSimulate.setBounds(12, 153, 117, 25);
		bSimulate.setEnabled(false);
		contentPane.add(bSimulate);

		tpResult = new JTextPane();
		tpResult.setBackground(Color.LIGHT_GRAY);
		tpResult.setBounds(5, 193, 438, 66);
		spResult = new JScrollPane(tpResult);
		spResult.setBounds(5, 193, 438, 66);
		contentPane.add(spResult);

		bInstructions = new JButton("Instructions");
		bInstructions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				bInstructionsActionPerformed(arg0);
			}
		});
		bInstructions.setBounds(141, 153, 125, 25);
		bInstructions.setVisible(false);
		contentPane.add(bInstructions);

		bGroup = new ButtonGroup();

		rbClustered = new JRadioButton();
		rbClustered.setText("Clustered");
		rbClustered.setBounds(157, 122, 96, 23);
		rbClustered.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				rbClusteredActionPerformed(evt);
			}
		});
		bGroup.add(rbClustered);
		contentPane.add(rbClustered);

		rbStandalone = new JRadioButton();
		rbStandalone.setText("Standalone");
		rbStandalone.setBounds(22, 122, 117, 23);
		rbStandalone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				rbStandaloneActionPerformed(evt);
			}
		});
		bGroup.add(rbStandalone);
		contentPane.add(rbStandalone);

		bPatches = new JButton("Go to patches >>");
		bPatches.setBounds(278, 153, 157, 25);
		bPatches.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				goPatches(arg0);
			}
		});
		bPatches.setVisible(false);
		contentPane.add(bPatches);
		
	}

	private void bSimulateActionPerformed(ActionEvent evt) {
		startVersion = cbStart.getSelectedItem().toString();
		endVersion = cbEnd.getSelectedItem().toString();

		simul = new Simulation(startVersion, endVersion);
		
		if(simul.getError()!="") {
			JOptionPane.showMessageDialog(null, simul.getError(), "Error", JOptionPane.ERROR_MESSAGE);
		}else{
			String result = simul.toString();

			tpResult.setText(result);
			lPredicted.setText(Integer.toString(simul.getSteps())
					+ " predicted steps");
			lAvoid.setText(Integer.toString(simul.getStepsA()) + " avoided steps");
			lProblems.setText(Integer.toString(simul.getStepsP())
					+ " steps to check");

			pGreen.setVisible(true);
			pOrange.setVisible(true);
			pRed.setVisible(true);
			lPredicted.setVisible(true);
			lAvoid.setVisible(true);
			lProblems.setVisible(true);
			bInstructions.setVisible(true);
			bPatches.setVisible(true);
		}
	}

	private void bInstructionsActionPerformed(ActionEvent evt) {
		instructions = new fInstructions();

		if (rbStandalone.isSelected()) {
			installType = "standalone";
		} else {
			installType = "clustered";
		}
		InstructionsReader.create(installType);
		InstructionsReader insRead = InstructionsReader.getInstance();
		instructions.setInstructions(insRead.getInstructions());
		instructions.setVisible(true);
	}

	private void rbStandaloneActionPerformed(ActionEvent evt) {
		if (!bSimulate.isEnabled()) {
			bSimulate.setEnabled(true);
		}
	}

	private void rbClusteredActionPerformed(ActionEvent evt) {
		if (!bSimulate.isEnabled()) {
			bSimulate.setEnabled(true);
		}
	}

	public void goPatches(ActionEvent evt) {
		patches = new fPatches(simul.getListPatches());
		patches.setVisible(true);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					fAccueil frame = new fAccueil();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
