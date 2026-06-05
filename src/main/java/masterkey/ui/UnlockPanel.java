package masterkey.ui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;

public class UnlockPanel extends JPanel {
    private final MainFrame mainFrame;
    private final JLabel fileLabel;
    private final JPasswordField passwordField;

    public UnlockPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.fileLabel = new JLabel("No file selected");
        this.passwordField = new JPasswordField(24);
        buildUi();
    }

    private void buildUi() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(80, 120, 80, 120));

        JLabel titleLabel = new JLabel("Unlock Database", JLabel.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(28f));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;

        formPanel.add(new JLabel("Database File:"), gbc);
        gbc.gridx = 1;
        formPanel.add(fileLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Master Password:"), gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        JPanel buttonPanel = new JPanel();
        JButton openButton = new JButton("Open");
        JButton cancelButton = new JButton("Cancel");
        JButton exitButton = new JButton("Exit");

        openButton.addActionListener(e -> {
            mainFrame.openDatabase(new String(passwordField.getPassword()));
            passwordField.setText("");
        });
        cancelButton.addActionListener(e -> mainFrame.showStart());
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(openButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(exitButton);

        add(titleLabel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setDatabaseFile(File file) {
        fileLabel.setText(file == null ? "No file selected" : file.getAbsolutePath());
        passwordField.setText("");
    }
}
