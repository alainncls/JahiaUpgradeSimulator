package fr.smile.fiches;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;

import fr.smile.main.Patch;
import fr.smile.services.DownloadService;
import fr.smile.services.DownloadTask;
import fr.smile.services.RunnableCompleteListener;

public class fPatches extends JDialog implements RunnableCompleteListener {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel;
	private JPanel listPanel;
	private JButton bBack, bDownload, bWarning;
	private JCheckBox cdownload;

	private fInstructions instructions;

	private Map<Patch, JCheckBox> checkBoxMap;
	private Map<Patch, JButton> buttonMap;

	public fPatches(List<Patch> listPatches) {

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 900, 600);

		this.checkBoxMap = new HashMap<>();
		this.buttonMap = new HashMap<>();

		instructions = new fInstructions();

		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPanel);
		contentPanel.setLayout(null);

		listPanel = new JPanel();
		listPanel.setBounds(5, 5, 881, 517);
		listPanel.setLayout(new SpringLayout());

		for (final Patch p : listPatches) {
			JButton bInstruction = new JButton("Instructions");
			JButton bDownload = new JButton("Download");
			JCheckBox cbCheck = new JCheckBox();
			JLabel lPatch = new JLabel(p.toString());
			lPatch.setSize(300, lPatch.getHeight());

			bDownload.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					runDownload(p);
				}
			});

			bInstruction.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					instructions.setInstructions(p.getInstructions());
					instructions.setTitle("Instructions Patch " + p.toString());
					instructions.setVisible(true);
				}
			});
			listPanel.add(lPatch);

			if (p.isProblem()) {
				bWarning = new JButton("Warning !");
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
			listPanel.add(bDownload);
			listPanel.add(cbCheck);

			checkBoxMap.put(p, cbCheck);
			buttonMap.put(p, bDownload);
		}

		SpringUtilities.makeCompactGridRight(listPanel,// parent
				listPatches.size(), 5, // rows, cols
				5, 5, // initX, initY
				5, 5, // xPad, yPad
				1); //column décalage

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
				for (Patch p : checkBoxMap.keySet()) {
					checkBoxMap.get(p).setSelected(cb.isSelected());
				}
			}
		});
		contentPanel.add(cdownload);
	}

	public void goBack(ActionEvent evt) {
		this.setVisible(false);
	}

	protected void runDownload(Patch p) {
		buttonMap.get(p).setBackground(Color.CYAN);
		buttonMap.get(p).setText("Downloading...");
		DownloadService.getInstance().download(p, this);
	}

	private void runDownload() {
		JCheckBox cb;
		for (Patch p : checkBoxMap.keySet()) {
			cb = checkBoxMap.get(p);
			if (cb.isSelected()) {
				runDownload(p);
			}
		}
	}

	@Override
	public void notifyComplete(Runnable runnable) {
		DownloadTask task;
		Patch p;
		JCheckBox cb;
		if (runnable.getClass() == DownloadTask.class) {
			task = (DownloadTask) runnable;
			p = task.getPatch();
			if (task.getResult() == DownloadTask.OK) {
				buttonMap.get(p).setBackground(Color.GREEN);
				buttonMap.get(p).setText("Finish");
				System.out.println("Download Ended : " + p.toString());
				cb = checkBoxMap.get(p);
				cb.setSelected(false);
			} else {
				buttonMap.get(p).setBackground(Color.RED);
				buttonMap.get(p).setText("Error");
				System.out.println("Download Fail : " + p.toString());
			}
		}
	}
}
