package masterkey.service;

import masterkey.domain.Backup;
import masterkey.domain.Database;
import masterkey.storage.DatabaseFileManager;
import masterkey.storage.CryptoUtil;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatabaseService {
    private final DatabaseFileManager fileManager;
    private Database currentDatabase;
    private File currentFile;
    private String currentMasterPassword;

    public DatabaseService() {
        this.fileManager = new DatabaseFileManager();
    }

    public String generateRecoveryKey() {
        return CryptoUtil.generateRecoveryKey();
    }

    public Database createDatabase(String databaseName, File file, String masterPassword) {
        return createDatabase(databaseName, file, masterPassword, null);
    }

    public Database createDatabase(String databaseName, File file, String masterPassword, String recoveryKey) {
        Database database = Database.createDatabase(databaseName, file.getAbsolutePath(), masterPassword);
        database.configureRecoveryKey(recoveryKey, masterPassword);
        this.currentDatabase = database;
        this.currentFile = file;
        this.currentMasterPassword = masterPassword;
        saveDatabase();
        return database;
    }

    public Database openDatabase(File file, String masterPassword) {
        Database database = fileManager.load(file, masterPassword);
        this.currentDatabase = database;
        this.currentFile = file;
        this.currentMasterPassword = masterPassword;
        return database;
    }

    public Database openDatabaseWithRecoveryKey(File file, String recoveryKey) {
        String recoveredMasterPassword = fileManager.recoverMasterPassword(file, recoveryKey);
        return openDatabase(file, recoveredMasterPassword);
    }

    public void saveDatabase() {
        ensureOpened();
        fileManager.save(currentDatabase, currentFile, currentMasterPassword);
    }

    public void saveDatabaseAndAutoBackup() {
        saveDatabase();
        autoBackupIfEnabled();
    }

    public Backup backupDatabase(File backupFile) {
        ensureOpened();
        if (backupFile == null) {
            throw new IllegalArgumentException("백업 파일이 필요합니다.");
        }

        try {
            saveDatabase();
            if (backupFile.getParentFile() != null) {
                Files.createDirectories(backupFile.getParentFile().toPath());
            }
            Files.copy(currentFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            Backup backup = currentDatabase.backupDatabase(
                    backupFile.getParentFile() == null ? "." : backupFile.getParentFile().getAbsolutePath()
            );
            saveDatabase();
            return backup;
        } catch (Exception e) {
            throw new IllegalStateException("백업에 실패했습니다.", e);
        }
    }

    private void autoBackupIfEnabled() {
        ensureOpened();
        if (currentDatabase == null || currentFile == null || !currentDatabase.getUserSettings().isAutoBackupEnabled()) {
            return;
        }

        try {
            File baseDirectory = currentFile.getParentFile() == null ? new File(".") : currentFile.getParentFile();
            File autoBackupDirectory = new File(baseDirectory, "auto_backup");
            Files.createDirectories(autoBackupDirectory.toPath());

            String databaseName = currentDatabase.getDatabaseName().replaceAll("[^a-zA-Z0-9가-힣_-]", "_");
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            File backupFile = new File(autoBackupDirectory, databaseName + "_auto_" + timestamp + ".mkdb");

            Files.copy(currentFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new IllegalStateException("자동 백업에 실패했습니다.", e);
        }
    }

    public void changeMasterPassword(String recoveryKey, String newPassword) {
        ensureOpened();

        if (newPassword == null || newPassword.length() < 4) {
            throw new IllegalArgumentException("새 마스터 비밀번호는 4자 이상이어야 합니다.");
        }
        if (recoveryKey == null || recoveryKey.isBlank()) {
            throw new IllegalArgumentException("복구키를 입력해야 합니다.");
        }
        if (!currentDatabase.hasRecoveryKey()) {
            throw new IllegalStateException("이 데이터베이스에는 복구키가 설정되어 있지 않습니다.");
        }

        String recoveredMasterPassword;
        try {
            recoveredMasterPassword = CryptoUtil.decryptString(
                    currentDatabase.getEncryptedMasterPasswordByRecovery(),
                    recoveryKey.trim(),
                    currentDatabase.getRecoverySalt(),
                    currentDatabase.getRecoveryIv()
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("복구키가 올바르지 않습니다.");
        }

        boolean changed = currentDatabase.getMasterKey().changeMasterPassword(recoveredMasterPassword, newPassword);
        if (!changed) {
            throw new IllegalArgumentException("마스터 비밀번호 변경에 실패했습니다.");
        }

        currentDatabase.configureRecoveryKey(recoveryKey.trim(), newPassword);
        currentMasterPassword = newPassword;
        saveDatabase();
    }

    public void closeDatabase() {
        if (currentDatabase != null) {
            currentDatabase.closeDatabase();
        }
        currentDatabase = null;
        currentFile = null;
        currentMasterPassword = null;
    }

    public Database getCurrentDatabase() {
        return currentDatabase;
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public String getCurrentMasterPassword() {
        return currentMasterPassword;
    }

    public boolean hasOpenedDatabase() {
        return currentDatabase != null && currentDatabase.isOpened();
    }

    public void ensureOpened() {
        if (!hasOpenedDatabase()) {
            throw new IllegalStateException("데이터베이스가 열려 있지 않습니다.");
        }
    }
}
