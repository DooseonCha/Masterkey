package masterkey.ui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;

public class StartPanel extends JPanel {
    private final MainFrame mainFrame;

    public StartPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        buildUi();
    }

    private void buildUi() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(60, 120, 60, 120));

        JLabel titleLabel = new JLabel("MasterKey", JLabel.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(36f));

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 120;
        gbc.ipady = 12;
        gbc.gridy = 0;

        JButton createButton = new JButton("Create Database");
        JButton openButton = new JButton("Open Database");
        JButton exitButton = new JButton("Exit");

        createButton.addActionListener(e -> createDatabase());
        openButton.addActionListener(e -> openDatabase());
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(createButton, gbc);
        gbc.gridy++;
        buttonPanel.add(openButton, gbc);
        gbc.gridy++;
        buttonPanel.add(exitButton, gbc);

        add(titleLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }

    private void createDatabase() {
        String databaseName = JOptionPane.showInputDialog(this, "Database name:", "Create Database", JOptionPane.PLAIN_MESSAGE);
        if (databaseName == null || databaseName.isBlank()) {
            return;
        }

        JPasswordField passwordField = new JPasswordField();
        int result = JOptionPane.showConfirmDialog(this, passwordField, "Master Password", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String password = new String(passwordField.getPassword());
        if (password.length() < 4) {
            mainFrame.showError("Invalid Password", "Master password must be at least 4 characters.");
            return;
        }

        JFileChooser chooser = new JFileChooser(new File("file"));
        chooser.setDialogTitle("Save Database File");
        chooser.setSelectedFile(new File(databaseName + ".mkdb"));
        chooser.setFileFilter(new FileNameExtensionFilter("MasterKey Database (*.mkdb)", "mkdb"));

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = ensureMkdbExtension(chooser.getSelectedFile());
            mainFrame.createDatabase(databaseName, file, password);
        }
    }

    private void openDatabase() {
        JFileChooser chooser = new JFileChooser(new File("file"));
        chooser.setDialogTitle("Open Database File");
        chooser.setFileFilter(new FileNameExtensionFilter("MasterKey Database (*.mkdb)", "mkdb"));

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            mainFrame.showUnlock(chooser.getSelectedFile());
        }
    }

    private File ensureMkdbExtension(File file) {
        if (file.getName().toLowerCase().endsWith(".mkdb")) {
            return file;
        }
        return new File(file.getParentFile(), file.getName() + ".mkdb");
    }
}
