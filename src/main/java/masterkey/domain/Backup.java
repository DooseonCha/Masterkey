package masterkey.domain;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Backup implements Serializable {
    private static final long serialVersionUID = 1L;

    private String backupId;
    private String backupPath;
    private String backupFileName;
    private LocalDateTime backupDate;

    public Backup(String backupPath, String backupFileName) {
        this.backupId = UUID.randomUUID().toString();
        this.backupPath = backupPath == null ? "" : backupPath;
        this.backupFileName = backupFileName == null ? "" : backupFileName;
        this.backupDate = LocalDateTime.now();
    }

    public boolean validateBackupPath(String path) {
        if (path == null || path.isBlank()) {
            return false;
        }
        File file = new File(path);
        File parent = file.isDirectory() ? file : file.getParentFile();
        return parent != null && parent.exists() && parent.canWrite();
    }

    public String getBackupFilePath() {
        return new File(backupPath, backupFileName).getPath();
    }

    public String getBackupId() {
        return backupId;
    }

    public String getBackupPath() {
        return backupPath;
    }

    public String getBackupFileName() {
        return backupFileName;
    }

    public LocalDateTime getBackupDate() {
        return backupDate;
    }
}
