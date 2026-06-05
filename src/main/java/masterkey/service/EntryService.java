package masterkey.service;

import masterkey.domain.Database;
import masterkey.domain.Entry;
import masterkey.domain.Password;

import java.util.ArrayList;
import java.util.List;

public class EntryService {
    private final DatabaseService databaseService;

    public EntryService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public Entry addEntry(String siteName, String siteUrl, String userId, String passwordValue) {
        validateEntry(siteName, userId, passwordValue);

        Entry entry = new Entry(siteName, siteUrl, userId, new Password(passwordValue));
        Database database = databaseService.getCurrentDatabase();
        database.addEntry(entry);
        databaseService.saveDatabaseAndAutoBackup();
        return entry;
    }

    public void updateEntry(String entryId, String siteName, String siteUrl, String userId, String passwordValue) {
        validateEntry(siteName, userId, passwordValue);

        Entry entry = findEntry(entryId);
        if (entry == null) {
            throw new IllegalArgumentException("엔트리를 찾을 수 없습니다.");
        }

        entry.updateEntry(siteName, siteUrl, userId);

        String currentPassword = entry.getPassword() == null ? "" : entry.getPassword().getPasswordValue();
        if (!currentPassword.equals(passwordValue)) {
            entry.changePassword(new Password(passwordValue));
        }

        databaseService.saveDatabaseAndAutoBackup();
    }

    public void deleteEntry(String entryId) {
        Database database = databaseService.getCurrentDatabase();
        boolean deleted = database.deleteEntry(entryId);
        if (!deleted) {
            throw new IllegalArgumentException("엔트리를 찾을 수 없습니다.");
        }
        databaseService.saveDatabaseAndAutoBackup();
    }

    public List<Entry> getEntries() {
        Database database = databaseService.getCurrentDatabase();
        if (database == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(database.getEntries());
    }

    public Entry findEntry(String entryId) {
        Database database = databaseService.getCurrentDatabase();
        return database == null ? null : database.findEntry(entryId);
    }

    private void validateEntry(String siteName, String userId, String passwordValue) {
        if (siteName == null || siteName.isBlank()) {
            throw new IllegalArgumentException("사이트 이름을 입력해야 합니다.");
        }
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("사용자 ID를 입력해야 합니다.");
        }
        if (passwordValue == null || passwordValue.isBlank()) {
            throw new IllegalArgumentException("비밀번호를 입력해야 합니다.");
        }
    }
}
