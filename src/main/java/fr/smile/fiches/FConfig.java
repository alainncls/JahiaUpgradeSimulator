package fr.smile.fiches;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.smile.services.JahiaConfigService;

public class FConfig extends JDialog {
    private static final long serialVersionUID = -5792664708211225238L;

    private static final Logger LOGGER = LoggerFactory.getLogger(FConfig.class);

    private final JPanel contentPanel;

    private JButton bSave, bPath;
    private BackButton backButton;
    private JLabel lTitle, lPath, lContext;
    private JTextField tfPath, tfContext;
    private JFileChooser chooser;

    public FConfig() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);

        contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPanel);
        contentPanel.setLayout(null);

        lTitle = new JLabel("Jahia Configuration");
        lTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lTitle.setBounds(5, 12, 438, 19);
        lTitle.setFont(new Font("Dialog", Font.BOLD, 16));
        contentPanel.add(lTitle);

        backButton = new BackButton(this);
        backButton.setBounds(325, 243, 120, 25);
        contentPanel.add(backButton);

        lPath = new JLabel("Path to Jahia :");
        lPath.setBounds(5, 67, 150, 20);
        contentPanel.add(lPath);

        tfPath = new JTextField(JahiaConfigService.getInstance().getFolder());
        tfPath.setBounds(160, 67, 150, 20);
        tfPath.setEnabled(false);
        contentPanel.add(tfPath);

        bPath = new JButton("Select path");
        bPath.setBounds(315, 67, 120, 20);
        bPath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                getPathToJahia();
            }
        });
        contentPanel.add(bPath);

        lContext = new JLabel("Jahia context :");
        lContext.setBounds(5, 92, 150, 20);
        contentPanel.add(lContext);

        tfContext = new JTextField(JahiaConfigService.getInstance()
                .getContext());
        tfContext.setBounds(160, 92, 150, 20);
        contentPanel.add(tfContext);

        bSave = new JButton("Save config");
        bSave.setBounds(5, 243, 120, 25);
        bSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                saveConfig();
                backButton.goBack();
            }
        });
        contentPanel.add(bSave);
    }

    public void saveConfig() {
        JahiaConfigService.getInstance().setFolder(tfPath.getText());
        JahiaConfigService.getInstance().setContext(tfContext.getText());
    }

    public Path getPathToJahia() {
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(JahiaConfigService.getInstance()
                .getFolder()));
        chooser.setDialogTitle("Choose Jahia directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        // disable the "All files" option
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            LOGGER.info("Selected path : " + chooser.getSelectedFile());
        } else {
            LOGGER.info("No Selection");
        }

        Path path = chooser.getSelectedFile().toPath();
        tfPath.setText(path.toString());
        return path;
    }
}
