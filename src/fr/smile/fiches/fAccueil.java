package fr.smile.fiches;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.regex.Pattern;

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
	private JScrollPane spResult;
	private JTextPane tpResult;
	private JRadioButton rbClustered;
	private JRadioButton rbStandalone;
	private ButtonGroup bGroup;
	private JButton bPatches;

	private fPatches patches;

	private Simulation simul;
	private String startVersion;
	private String endVersion;
	private String detectedVersion;
	private List<String> listVersions;

	private String context;
	private String jahiaFolder;

	public fAccueil(String[] args) {

		context = args.length >= 2 ? args[1] : "ROOT";
		jahiaFolder = args.length >= 1 ? args[0] : "./";
		if (!jahiaFolder.endsWith("/")) {
			jahiaFolder += "/";
		}

		listVersions = VersionsReader.getInstance().getVersions();
		detectedVersion = detectJahiaVersion();

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

		lStart = new JLabel("Current version");
		lStart.setBounds(12, 56, 149, 15);
		contentPane.add(lStart);

		cbStart = new JComboBox<String>();
		cbStart.setModel(new DefaultComboBoxModel(listVersions.subList(0,
				listVersions.size() - 1).toArray()));
		cbStart.setBounds(161, 51, 92, 24);
		contentPane.add(cbStart);

		lEnd = new JLabel("Target version");
		lEnd.setBounds(12, 92, 149, 15);
		contentPane.add(lEnd);

		cbEnd = new JComboBox<String>();
		cbEnd.setModel(new DefaultComboBoxModel(listVersions.subList(1,
				listVersions.size()).toArray()));
		cbEnd.setBounds(161, 87, 92, 24);
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

		if (simul.getError() != "") {
			JOptionPane.showMessageDialog(null, simul.getError(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} else {
			String result = simul.toString();

			tpResult.setText(result);
			lPredicted.setText(Integer.toString(simul.getSteps())
					+ " predicted steps");
			lAvoid.setText(Integer.toString(simul.getStepsA())
					+ " avoided steps");
			lProblems.setText(Integer.toString(simul.getStepsP())
					+ " steps to check");

			pGreen.setVisible(true);
			pOrange.setVisible(true);
			pRed.setVisible(true);
			lPredicted.setVisible(true);
			lAvoid.setVisible(true);
			lProblems.setVisible(true);
			bPatches.setVisible(true);

			patches = new fPatches(simul.getListPatches());
		}
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
		patches.setVisible(true);
	}

	@SuppressWarnings("finally")
	private String detectJahiaVersion() {
		String version = null;
		try {
			// File[] files = listFilesMatching(new File("./"),
			// "jahia-impl-(\\d\\.){4}jar");
			File[] files = listFilesMatching(new File(jahiaFolder
					+ "/tomcat/webapps/" + context + "/WEB-INF/lib/"),
					"jahia-impl-(\\d\\.){4}jar");
			version = files[0].getName().substring(11, 18);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println("Fail to detect");
		} finally {
			return version;
		}

	}

	public File[] listFilesMatching(File root, String regex) {
		if (!root.isDirectory()) {
			throw new IllegalArgumentException(root + " is no directory.");
		}
		final Pattern p = Pattern.compile(regex); // careful: could also throw
													// an exception!
		return root.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return p.matcher(file.getName()).matches();
			}
		});
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
