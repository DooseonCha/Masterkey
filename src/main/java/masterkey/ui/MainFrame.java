package masterkey.ui;

import masterkey.domain.Database;
import masterkey.service.AlertService;
import masterkey.service.BackupService;
import masterkey.service.DatabaseService;
import masterkey.service.EntryService;
import masterkey.service.PasswordService;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.io.File;

public class MainFrame extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel rootPanel;

    private final DatabaseService databaseService;
    private final EntryService entryService;
    private final PasswordService passwordService;
    private final AlertService alertService;
    private final BackupService backupService;

    private final StartPanel startPanel;
    private final UnlockPanel unlockPanel;
    private final EntryListPanel entryListPanel;

    private File selectedDatabaseFile;

    public MainFrame() {
        super("MasterKey");

        this.databaseService = new DatabaseService();
        this.entryService = new EntryService(databaseService);
        this.passwordService = new PasswordService();
        this.alertService = new AlertService(databaseService);
        this.backupService = new BackupService(databaseService);

        this.cardLayout = new CardLayout();
        this.rootPanel = new JPanel(cardLayout);

        this.startPanel = new StartPanel(this);
        this.unlockPanel = new UnlockPanel(this);
        this.entryListPanel = new EntryListPanel(this, entryService, passwordService, alertService, backupService);

        rootPanel.add(startPanel, "start");
        rootPanel.add(unlockPanel, "unlock");
        rootPanel.add(entryListPanel, "entries");

        setContentPane(rootPanel);
        setSize(980, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        showStart();
    }

    public void showStart() {
        selectedDatabaseFile = null;
        cardLayout.show(rootPanel, "start");
    }

    public void showUnlock(File file) {
        this.selectedDatabaseFile = file;
        unlockPanel.setDatabaseFile(file);
        cardLayout.show(rootPanel, "unlock");
    }

    public void createDatabase(String databaseName, File file, String masterPassword) {
        try {
            Database database = databaseService.createDatabase(databaseName, file, masterPassword);
            entryListPanel.refreshTable();
            setTitle("MasterKey - " + database.getDatabaseName());
            cardLayout.show(rootPanel, "entries");
        } catch (Exception e) {
            showError("Create Database Failed", e.getMessage());
        }
    }

    public void openDatabase(String masterPassword) {
        try {
            if (selectedDatabaseFile == null) {
                throw new IllegalStateException("Database file is not selected.");
            }

            Database database = databaseService.openDatabase(selectedDatabaseFile, masterPassword);
            entryListPanel.refreshTable();
            setTitle("MasterKey - " + database.getDatabaseName());
            cardLayout.show(rootPanel, "entries");
        } catch (Exception e) {
            showError("Open Database Failed", "Master password is incorrect or file is invalid.");
        }
    }

    public void lockDatabase() {
        File currentFile = databaseService.getCurrentFile();
        databaseService.closeDatabase();
        if (currentFile != null) {
            showUnlock(currentFile);
        } else {
            showStart();
        }
    }

    public void closeFile() {
        databaseService.closeDatabase();
        setTitle("MasterKey");
        showStart();
    }

    public DatabaseService getDatabaseService() {
        return databaseService;
    }

    public void showError(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public void showInfo(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
