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

import fr.smile.main.Patch;
import fr.smile.main.Simulation;

public class fPatches extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel;
	private JPanel listPanel;
	private JButton bBack, bDownload;
	private JCheckBox cdownload;
	private JLabel lReboot;

	private fInstructions instructions;

	private Map<JCheckBox, ActionButton> checkBoxMap;

	public fPatches(final Simulation simu) {

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 900, 600);

		this.checkBoxMap = new HashMap<>();

		instructions = new fInstructions();

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
			JButton bInstruction = new JButton("Instructions");
			ActionButton bAction = new ActionButton(p);
			JCheckBox cbCheck = new JCheckBox();
			JLabel lPatch = new JLabel(p.toString());
			lPatch.setSize(300, lPatch.getHeight());

			bAction.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					((ActionButton)e.getSource()).doAction();
				}
			});

			bInstruction.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (simu.getClustered()) {
						instructions.setInstructions(p.getInstructionsCluster()
								+ "<br/><hr/><br/" + p.getInstructions());
					} else {
						instructions.setInstructions(p.getInstructions());
					}
					instructions.setTitle("Instructions Patch " + p.toString());
					instructions.setVisible(true);
				}
			});
			listPanel.add(lPatch);

			if (p.isProblem()) {
				JButton bWarning = new JButton("Warning !");
				bWarning.setBackground(Color.ORANGE);
				bWarning.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JOptionPane
								.showMessageDialog(
										null,
										new JLabel(
												"<html><body style='width: 400px; text-align: justify; text-justify: inter-word;'>"
														+ p.getWarning()
														+ "</body></html>"));
					}
				});
				listPanel.add(bWarning);
			} else {
				listPanel.add(new JLabel());
			}

			listPanel.add(bInstruction);
			listPanel.add(bAction);
			listPanel.add(cbCheck);
			
			if(index==1) {
				nbCols = listPanel.getComponentCount();
			}

			if (p.getReboot()) {
				int nbCompInit = listPanel.getComponentCount();
				lReboot = new JLabel("You need to reboot your Jahia install after upgrading to "+p.getEndVersion());
				lReboot.setBackground(Color.YELLOW);
				lReboot.setOpaque(true);
				listPanel.add(lReboot);
				int added = listPanel.getComponentCount()-nbCompInit;
				for (int i = added; i < nbCols; i++) {
					listPanel.add(new JLabel());
				}
			}

			checkBoxMap.put(cbCheck, bAction);
		}

		SpringUtilities.makeCompactGridRight(listPanel,// parent
				simu.getSteps() + simu.getReboots(), nbCols, // rows, cols
				5, 5, // initX, initY
				5, 5, // xPad, yPad
				4); // number of cols to push right

		JScrollPane scrollPane = new JScrollPane(listPanel,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(listPanel.getBounds());
		contentPanel.add(scrollPane);

		bBack = new JButton("<< Back");
		bBack.setBounds(538, 534, 117, 25);
		bBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				goBack(evt);
			}
		});
		contentPanel.add(bBack);

		bDownload = new JButton("Download Selected");
		bDownload.setBounds(661, 534, 180, 25);
		bDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				runDownload();
			}
		});
		contentPanel.add(bDownload);

		cdownload = new JCheckBox();
		cdownload.setBounds(846, 534, 25, 25);
		cdownload.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JCheckBox cb = (JCheckBox) e.getSource();
				for (JCheckBox c : checkBoxMap.keySet()) {
					c.setSelected(cb.isSelected());
				}
			}
		});
		contentPanel.add(cdownload);
	}

	public void goBack(ActionEvent evt) {
		this.setVisible(false);
	}

	private void runDownload() {
		for (JCheckBox c : checkBoxMap.keySet()) {
			if (c.isSelected()) {
				checkBoxMap.get(c).doDownload();
			}
		}
	}
}
