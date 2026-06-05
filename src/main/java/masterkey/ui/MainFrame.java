package masterkey.ui;

import masterkey.domain.Database;
import masterkey.service.AlertService;
import masterkey.service.BackupService;
import masterkey.service.DatabaseService;
import masterkey.service.EntryService;
import masterkey.service.PasswordService;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;

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
        setApplicationIcon();

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
        setMinimumSize(new Dimension(1250, 720));
        setSize(1250, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClosing();
            }
        });
        showStart();
    }

    private void setApplicationIcon() {
        URL iconUrl = getClass().getResource("/icon.png");
        if (iconUrl != null) {
            setIconImage(new ImageIcon(iconUrl).getImage());
        }
    }

    private void handleWindowClosing() {
        if (databaseService.hasOpenedDatabase()) {
            closeFile();
            return;
        }
        dispose();
        System.exit(0);
    }

    public void showStart() {
        selectedDatabaseFile = null;
        setTitle("MasterKey");
        cardLayout.show(rootPanel, "start");
    }

    public void showUnlock(File file) {
        this.selectedDatabaseFile = file;
        unlockPanel.setDatabaseFile(file);
        cardLayout.show(rootPanel, "unlock");
    }

    public void createDatabase(String databaseName, File file, String masterPassword, String recoveryKey) {
        try {
            Database database = databaseService.createDatabase(databaseName, file, masterPassword, recoveryKey);
            entryListPanel.refreshTable();
            setTitle("MasterKey - " + database.getDatabaseName());
            cardLayout.show(rootPanel, "entries");
        } catch (Exception e) {
            showError("데이터베이스 생성 실패", e.getMessage());
        }
    }

    public void openDatabase(String masterPassword) {
        try {
            if (selectedDatabaseFile == null) {
                throw new IllegalStateException("데이터베이스 파일이 선택되지 않았습니다.");
            }
            if (masterPassword == null || masterPassword.length() < 4) {
                showError("비밀번호 오류", "마스터 비밀번호는 4자 이상이어야 합니다.");
                return;
            }

            Database database = databaseService.openDatabase(selectedDatabaseFile, masterPassword);
            entryListPanel.refreshTable();
            setTitle("MasterKey - " + database.getDatabaseName());
            cardLayout.show(rootPanel, "entries");
        } catch (Exception e) {
            showError("데이터베이스 열기 실패", "마스터 비밀번호가 틀렸거나 파일이 올바르지 않습니다.");
        }
    }

    public void openDatabaseWithRecoveryKey(String recoveryKey) {
        try {
            if (selectedDatabaseFile == null) {
                throw new IllegalStateException("데이터베이스 파일이 선택되지 않았습니다.");
            }
            if (recoveryKey == null || recoveryKey.isBlank()) {
                showError("복구키 오류", "복구키를 입력해야 합니다.");
                return;
            }

            Database database = databaseService.openDatabaseWithRecoveryKey(selectedDatabaseFile, recoveryKey.trim());
            entryListPanel.refreshTable();
            setTitle("MasterKey - " + database.getDatabaseName());
            cardLayout.show(rootPanel, "entries");
        } catch (Exception e) {
            showError("복구 실패", "복구키가 올바르지 않거나 복구키가 설정되지 않은 파일입니다.");
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
