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

@SuppressWarnings("serial")
public class FChiffrage extends JDialog implements PropertyChangeListener {

    private final JPanel contentPanel;

    private JScrollPane spChiffrage;
    private JEditorPane epChiffrage;
    private JButton backButton, bCalculate;
    private JLabel luO, ltJm;
    private JFormattedTextField ftfuO, ftftJm;

    private transient Simulation simulation;

    public FChiffrage(Simulation simul) {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 900, 600);

        this.simulation = simul;

        contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPanel);
        contentPanel.setLayout(null);

        BackButton button = new BackButton();
        backButton = button.getBackButton(this);
        backButton.setBounds(15, 534, 117, 25);
        contentPanel.add(backButton);

        bCalculate = new JButton("Calculate");
        bCalculate.setBounds(137, 534, 117, 25);
        bCalculate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                propertyChange(null);
            }
        });
        contentPanel.add(bCalculate);

        luO = new JLabel("Valeur de l'uO");
        luO.setBounds(5, 405, 117, 15);
        contentPanel.add(luO);

        NumberFormat uOFormat = NumberFormat.getNumberInstance();
        uOFormat.setMinimumFractionDigits(1);

        ftfuO = new JFormattedTextField(uOFormat);
        ftfuO.setBounds(125, 405, 92, 24);
        ftfuO.setColumns(10);
        ftfuO.setValue(simulation.getuO());
        ftfuO.addPropertyChangeListener("value", this);
        contentPanel.add(ftfuO);

        ltJm = new JLabel("Valeur du tJm");
        ltJm.setBounds(230, 405, 117, 15);
        contentPanel.add(ltJm);

        ftftJm = new JFormattedTextField(NumberFormat.getCurrencyInstance());
        ftftJm.setBounds(344, 405, 92, 24);
        ftftJm.setColumns(10);
        ftftJm.setValue(simulation.gettJM());
        ftftJm.addPropertyChangeListener("value", this);
        contentPanel.add(ftftJm);

        epChiffrage = new JEditorPane("text/html",
                simulation.getChiffrageHtml());
        epChiffrage.setEditable(false);
        spChiffrage = new JScrollPane(epChiffrage);
        spChiffrage.setBounds(5, 5, 881, 400);
        contentPanel.add(spChiffrage);
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if (e != null) {
            Object source = e.getSource();
            if (source == ftfuO) {
                float uO = ((Number) ftfuO.getValue()).floatValue();
                simulation.calculateTotalDuration(uO);
                simulation.calculateCost(((Number) ftftJm.getValue())
                        .intValue());
            } else if (source == ftftJm) {
                int tJm = ((Number) ftftJm.getValue()).intValue();
                simulation.calculateCost(tJm);
            }
            epChiffrage.setText(simulation.getChiffrageHtml());
        }
    }
}
