package masterkey.service;

import masterkey.domain.Backup;

import java.io.File;

public class BackupService {
    private final DatabaseService databaseService;

    public BackupService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public Backup backup(File backupFile) {
        return databaseService.backupDatabase(backupFile);
    }
}
