package masterkey.ui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

public class EntryFormDialog extends JDialog {
    private final JTextField siteNameField;
    private final JTextField siteUrlField;
    private final JTextField userIdField;
    private final JPasswordField passwordField;
    private boolean saved;

    public EntryFormDialog(MainFrame owner) {
        super(owner, "Add Entry", true);
        this.siteNameField = new JTextField(24);
        this.siteUrlField = new JTextField(24);
        this.userIdField = new JTextField(24);
        this.passwordField = new JPasswordField(24);
        this.saved = false;

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

        addRow(formPanel, gbc, "Site Name:", siteNameField);
        addRow(formPanel, gbc, "Site URL:", siteUrlField);
        addRow(formPanel, gbc, "User ID:", userIdField);
        addRow(formPanel, gbc, "Password:", passwordField);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            saved = true;
            dispose();
        });
        cancelButton.addActionListener(e -> {
            saved = false;
            dispose();
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, String label, java.awt.Component field) {
        gbc.gridx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
        gbc.gridy++;
    }

    public boolean isSaved() {
        return saved;
    }

    public String getSiteNameValue() {
        return siteNameField.getText();
    }

    public String getSiteUrlValue() {
        return siteUrlField.getText();
    }

    public String getUserIdValue() {
        return userIdField.getText();
    }

    public String getPasswordValue() {
        return new String(passwordField.getPassword());
    }
}
