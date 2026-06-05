package masterkey.ui;

import masterkey.service.PasswordService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

public class PasswordGeneratorDialog extends JDialog {
    private final MainFrame owner;
    private final PasswordService passwordService;
    private final JSpinner lengthSpinner;
    private final JCheckBox uppercaseBox;
    private final JCheckBox lowercaseBox;
    private final JCheckBox numbersBox;
    private final JCheckBox specialBox;
    private final JTextField excludedField;
    private final JTextField resultField;

    public PasswordGeneratorDialog(MainFrame owner, PasswordService passwordService) {
        super(owner, "Password Generator", true);
        this.owner = owner;
        this.passwordService = passwordService;
        this.lengthSpinner = new JSpinner(new SpinnerNumberModel(16, 4, 128, 1));
        this.uppercaseBox = new JCheckBox("Uppercase", true);
        this.lowercaseBox = new JCheckBox("Lowercase", true);
        this.numbersBox = new JCheckBox("Numbers", true);
        this.specialBox = new JCheckBox("Special Characters", true);
        this.excludedField = new JTextField(20);
        this.resultField = new JTextField(28);
        this.resultField.setEditable(false);

        buildUi();
        pack();
        setLocationRelativeTo(owner);
    }

    private void buildUi() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 0;

        gbc.gridx = 0;
        formPanel.add(new JLabel("Length:"), gbc);
        gbc.gridx = 1;
        formPanel.add(lengthSpinner, gbc);
        gbc.gridy++;

        gbc.gridx = 0;
        formPanel.add(new JLabel("Options:"), gbc);
        gbc.gridx = 1;
        JPanel optionPanel = new JPanel();
        optionPanel.add(uppercaseBox);
        optionPanel.add(lowercaseBox);
        optionPanel.add(numbersBox);
        optionPanel.add(specialBox);
        formPanel.add(optionPanel, gbc);
        gbc.gridy++;

        gbc.gridx = 0;
        formPanel.add(new JLabel("Excluded:"), gbc);
        gbc.gridx = 1;
        formPanel.add(excludedField, gbc);
        gbc.gridy++;

        gbc.gridx = 0;
        formPanel.add(new JLabel("Result:"), gbc);
        gbc.gridx = 1;
        formPanel.add(resultField, gbc);

        JPanel buttonPanel = new JPanel();
        JButton generateButton = new JButton("Generate");
        JButton copyButton = new JButton("Copy");
        JButton closeButton = new JButton("Close");

        generateButton.addActionListener(e -> generate());
        copyButton.addActionListener(e -> {
            passwordService.copyToClipboard(resultField.getText());
            owner.showInfo("Copied", "Generated password copied to clipboard.");
        });
        closeButton.addActionListener(e -> dispose());

        buttonPanel.add(generateButton);
        buttonPanel.add(copyButton);
        buttonPanel.add(closeButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void generate() {
        try {
            String password = passwordService.generatePassword(
                    (Integer) lengthSpinner.getValue(),
                    uppercaseBox.isSelected(),
                    lowercaseBox.isSelected(),
                    numbersBox.isSelected(),
                    specialBox.isSelected(),
                    excludedField.getText()
            );
            resultField.setText(password);
        } catch (Exception e) {
            owner.showError("Generate Failed", e.getMessage());
        }
    }
}
