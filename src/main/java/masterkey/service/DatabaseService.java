package masterkey.service;

import masterkey.domain.Backup;
import masterkey.domain.Database;
import masterkey.storage.DatabaseFileManager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class DatabaseService {
    private final DatabaseFileManager fileManager;
    private Database currentDatabase;
    private File currentFile;
    private String currentMasterPassword;

    public DatabaseService() {
        this.fileManager = new DatabaseFileManager();
    }

    public Database createDatabase(String databaseName, File file, String masterPassword) {
        Database database = Database.createDatabase(databaseName, file.getAbsolutePath(), masterPassword);
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

    public void saveDatabase() {
        ensureOpened();
        fileManager.save(currentDatabase, currentFile, currentMasterPassword);
    }

    public Backup backupDatabase(File backupFile) {
        ensureOpened();
        if (backupFile == null) {
            throw new IllegalArgumentException("Backup file is required.");
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
            throw new IllegalStateException("Backup failed.", e);
        }
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
            throw new IllegalStateException("Database is not opened.");
        }
    }
}
