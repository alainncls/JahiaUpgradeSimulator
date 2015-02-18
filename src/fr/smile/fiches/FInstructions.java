package fr.smile.fiches;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class FInstructions extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String PREFIX = "<html><body>";
	private static final String SUFFIX = "</body></html>";
	private final JPanel contentPanel;

	private JScrollPane spInstructions;
	private JEditorPane epInstructions;
	private JButton backButton;

	public FInstructions() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 900, 600);

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

		epInstructions = new JEditorPane("text/html", "");
		epInstructions.setEditable(false);
		spInstructions = new JScrollPane(epInstructions);
		spInstructions.setBounds(5, 5, 881, 517);
		contentPanel.add(spInstructions);

	}

	public void goBack(ActionEvent evt) {
		this.setVisible(false);
	}

	public void setInstructions(String ins) {
		epInstructions.setText(PREFIX + ins + SUFFIX);
		epInstructions.setCaretPosition(0);
	}
}
