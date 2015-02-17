package fr.smile.fiches;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import fr.smile.main.Simulation;

public class fChiffrage extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel;

	private JScrollPane spChiffrage;
	private JEditorPane epChiffrage;
	private JButton backButton;

	private Simulation simulation;

	public fChiffrage(Simulation simul) {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		
		this.simulation = simul;

		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPanel);
		contentPanel.setLayout(null);

		backButton = new JButton("<< Back");
		backButton.setBounds(15, 534, 117, 25);
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				goBack(arg0);
			}
		});
		contentPanel.add(backButton);

		epChiffrage = new JEditorPane("text/html", simulation.getChiffrage());
		epChiffrage.setEditable(false);
		spChiffrage = new JScrollPane(epChiffrage);
		spChiffrage.setBounds(5, 5, 881, 517);
		contentPanel.add(spChiffrage);

	}

	public void goBack(ActionEvent evt) {
		this.setVisible(false);
	}

	public void setInstructions(String ins) {
		epChiffrage.setText("<html><body>"+ins+"</body></html>");
		epChiffrage.setCaretPosition(0);
	}
}
