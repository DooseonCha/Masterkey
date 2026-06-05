package masterkey.domain;

import masterkey.storage.CryptoUtil;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Database implements Serializable {
    private static final long serialVersionUID = 1L;

    private String databaseName;
    private String filePath;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private transient boolean opened;

    private MasterKey masterKey;
    private List<Entry> entries;
    private List<Backup> backups;
    private UserSettings userSettings;

    private byte[] recoverySalt;
    private byte[] recoveryIv;
    private byte[] encryptedMasterPasswordByRecovery;

    public Database(String databaseName, String filePath, String masterPassword) {
        this.databaseName = databaseName == null ? "MasterKey Database" : databaseName;
        this.filePath = filePath == null ? "" : filePath;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = this.createdAt;
        this.opened = true;
        this.masterKey = new MasterKey(masterPassword);
        this.entries = new ArrayList<>();
        this.backups = new ArrayList<>();
        this.userSettings = new UserSettings();
    }

    public static Database createDatabase(String databaseName, String filePath, String masterPassword) {
        if (databaseName == null || databaseName.isBlank()) {
            throw new IllegalArgumentException("데이터베이스 이름을 입력해야 합니다.");
        }
        if (masterPassword == null || masterPassword.length() < 4) {
            throw new IllegalArgumentException("마스터 비밀번호는 4자 이상이어야 합니다.");
        }
        return new Database(databaseName, filePath, masterPassword);
    }

    public boolean openDatabase(String inputPassword) {
        opened = masterKey != null && masterKey.verifyPassword(inputPassword);
        return opened;
    }

    public void closeDatabase() {
        opened = false;
    }

    public void addEntry(Entry entry) {
        if (entry != null) {
            entries.add(entry);
            touch();
        }
    }

    public boolean updateEntry(Entry updatedEntry) {
        if (updatedEntry == null) {
            return false;
        }

        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getEntryId().equals(updatedEntry.getEntryId())) {
                entries.set(i, updatedEntry);
                touch();
                return true;
            }
        }
        return false;
    }

    public boolean deleteEntry(String entryId) {
        boolean removed = entries.removeIf(entry -> entry.getEntryId().equals(entryId));
        if (removed) {
            touch();
        }
        return removed;
    }

    public Entry findEntry(String entryId) {
        if (entryId == null) {
            return null;
        }
        return entries.stream()
                .filter(entry -> entry.getEntryId().equals(entryId))
                .findFirst()
                .orElse(null);
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public boolean saveDatabase() {
        touch();
        return true;
    }

    public Backup backupDatabase(String backupPath) {
        String fileName = databaseName + "_" + System.currentTimeMillis() + ".backup";
        Backup backup = new Backup(backupPath, fileName);
        backups.add(backup);
        touch();
        return backup;
    }


    public void configureRecoveryKey(String recoveryKey, String masterPassword) {
        if (recoveryKey == null || recoveryKey.isBlank()) {
            this.recoverySalt = null;
            this.recoveryIv = null;
            this.encryptedMasterPasswordByRecovery = null;
            return;
        }

        this.recoverySalt = CryptoUtil.generateSalt();
        this.recoveryIv = CryptoUtil.generateIv();
        this.encryptedMasterPasswordByRecovery = CryptoUtil.encryptString(
                masterPassword,
                recoveryKey,
                recoverySalt,
                recoveryIv
        );
        touch();
    }

    public boolean hasRecoveryKey() {
        return recoverySalt != null
                && recoveryIv != null
                && encryptedMasterPasswordByRecovery != null
                && recoverySalt.length > 0
                && recoveryIv.length > 0
                && encryptedMasterPasswordByRecovery.length > 0;
    }

    public byte[] getRecoverySalt() {
        return recoverySalt == null ? new byte[0] : Arrays.copyOf(recoverySalt, recoverySalt.length);
    }

    public byte[] getRecoveryIv() {
        return recoveryIv == null ? new byte[0] : Arrays.copyOf(recoveryIv, recoveryIv.length);
    }

    public byte[] getEncryptedMasterPasswordByRecovery() {
        return encryptedMasterPasswordByRecovery == null
                ? new byte[0]
                : Arrays.copyOf(encryptedMasterPasswordByRecovery, encryptedMasterPasswordByRecovery.length);
    }

    private void touch() {
        modifiedAt = LocalDateTime.now();
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath == null ? "" : filePath;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public MasterKey getMasterKey() {
        return masterKey;
    }

    public List<Backup> getBackups() {
        return backups;
    }

    public UserSettings getUserSettings() {
        return userSettings;
    }
}
