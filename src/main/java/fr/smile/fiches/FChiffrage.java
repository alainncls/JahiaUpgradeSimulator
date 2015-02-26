package fr.smile.fiches;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import fr.smile.models.Simulation;

public class FChiffrage extends JDialog implements PropertyChangeListener {
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel;

	private JScrollPane spChiffrage;
	private JEditorPane epChiffrage;
	private JButton backButton, bCalculate;
	private JLabel lUO, lTJM;
	private JFormattedTextField ftfUO, ftfTJM;

	private Simulation simulation;

	public FChiffrage(Simulation simul) {
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

		bCalculate = new JButton("Calculate");
		bCalculate.setBounds(137, 534, 117, 25);
		bCalculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				propertyChange(null);
			}
		});
		contentPanel.add(bCalculate);

		lUO = new JLabel("Valeur de l'UO");
		lUO.setBounds(5, 405, 117, 15);
		contentPanel.add(lUO);

		NumberFormat uOFormat = NumberFormat.getNumberInstance();
		uOFormat.setMinimumFractionDigits(1);

		ftfUO = new JFormattedTextField(uOFormat);
		ftfUO.setBounds(125, 405, 92, 24);
		ftfUO.setColumns(10);
		ftfUO.setValue(simulation.getuO());
		ftfUO.addPropertyChangeListener("value", this);
		contentPanel.add(ftfUO);

		lTJM = new JLabel("Valeur du TJM");
		lTJM.setBounds(230, 405, 117, 15);
		contentPanel.add(lTJM);

		ftfTJM = new JFormattedTextField(NumberFormat.getCurrencyInstance());
		ftfTJM.setBounds(344, 405, 92, 24);
		ftfTJM.setColumns(10);
		ftfTJM.setValue(simulation.gettJM());
		ftfTJM.addPropertyChangeListener("value", this);
		contentPanel.add(ftfTJM);

		epChiffrage = new JEditorPane("text/html", simulation.getChiffrageHtml());
		epChiffrage.setEditable(false);
		spChiffrage = new JScrollPane(epChiffrage);
		spChiffrage.setBounds(5, 5, 881, 400);
		contentPanel.add(spChiffrage);
	}

	public void propertyChange(PropertyChangeEvent e) {
		if (e != null) {
			Object source = e.getSource();
			if (source == ftfUO) {
				float UO = ((Number) ftfUO.getValue()).floatValue();
				simulation.calculateTotalDuration(UO);
				simulation.calculateCost(((Number) ftfTJM.getValue())
						.intValue());
			} else if (source == ftfTJM) {
				int TJM = ((Number) ftfTJM.getValue()).intValue();
				simulation.calculateCost(TJM);
			}
			epChiffrage.setText(simulation.getChiffrageHtml());
		}
	}

	public void goBack(ActionEvent evt) {
		this.setVisible(false);
	}
}
