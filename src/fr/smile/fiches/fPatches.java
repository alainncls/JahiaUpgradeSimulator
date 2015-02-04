package fr.smile.fiches;

import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import fr.smile.main.Patch;

public class fPatches extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel;
	private JPanel listPanel;
	private JButton backButton;
	private fInstructions instructions;

	private List<Patch> listPatches;

	public fPatches(List<Patch> listPatches) {

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 900, 600);

		this.listPatches = listPatches;

		instructions = new fInstructions();

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

		listPanel = new JPanel();
		listPanel.setBounds(5, 5, 881, 517);
		listPanel.setLayout(new GridLayout(listPatches.size(), 3, 5, 5));
		for (final Patch p : this.listPatches) {
			JButton bInstruction = new JButton("Instructions");
			JButton bDownload = new JButton("Download");
			JLabel lPatch = new JLabel(p.toString());

			bDownload.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						Desktop.getDesktop().browse(new URI(p.getUrl()));
					} catch (IOException | URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
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
			listPanel.add(bInstruction);
			listPanel.add(bDownload);
		}
		JScrollPane scrollPane = new JScrollPane(listPanel,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(listPanel.getBounds());
		contentPanel.add(scrollPane);
	}

	public void goBack(java.awt.event.ActionEvent evt) {
		this.setVisible(false);
	}
}
