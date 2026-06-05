package masterkey.ui;

import masterkey.domain.Entry;
import masterkey.service.PasswordService;

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
    private final MainFrame owner;
    private final PasswordService passwordService;
    private final JTextField siteNameField;
    private final JTextField siteUrlField;
    private final JTextField userIdField;
    private final JPasswordField passwordField;
    private boolean saved;

    public EntryFormDialog(MainFrame owner, PasswordService passwordService) {
        this(owner, passwordService, null);
    }

    public EntryFormDialog(MainFrame owner, PasswordService passwordService, Entry entry) {
        super(owner, entry == null ? "엔트리 추가" : "엔트리 수정", true);
        this.owner = owner;
        this.passwordService = passwordService;
        this.siteNameField = new JTextField(24);
        this.siteUrlField = new JTextField(24);
        this.userIdField = new JTextField(24);
        this.passwordField = new JPasswordField(24);
        this.saved = false;

        if (entry != null) {
            siteNameField.setText(entry.getSiteName());
            siteUrlField.setText(entry.getSiteUrl());
            userIdField.setText(entry.getUserId());
            passwordField.setText(entry.getPassword().getPasswordValue());
        }

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

        addRow(formPanel, gbc, "사이트 이름:", siteNameField);
        addRow(formPanel, gbc, "사이트 URL:", siteUrlField);
        addRow(formPanel, gbc, "사용자 ID:", userIdField);

        gbc.gridx = 0;
        formPanel.add(new JLabel("비밀번호:"), gbc);
        gbc.gridx = 1;
        JPanel passwordPanel = new JPanel();
        JButton generateButton = new JButton("비밀번호 생성");
        generateButton.addActionListener(e -> openPasswordGenerator());
        passwordPanel.add(passwordField);
        passwordPanel.add(generateButton);
        formPanel.add(passwordPanel, gbc);
        gbc.gridy++;

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("저장");
        JButton cancelButton = new JButton("취소");

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

    private void openPasswordGenerator() {
        PasswordGeneratorDialog dialog = new PasswordGeneratorDialog(owner, passwordService, generatedPassword -> {
            passwordField.setText(generatedPassword);
        });
        dialog.setVisible(true);
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
