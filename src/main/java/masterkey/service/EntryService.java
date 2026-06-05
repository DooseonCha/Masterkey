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
        databaseService.saveDatabase();
        return entry;
    }

    public void deleteEntry(String entryId) {
        Database database = databaseService.getCurrentDatabase();
        boolean deleted = database.deleteEntry(entryId);
        if (!deleted) {
            throw new IllegalArgumentException("Entry not found.");
        }
        databaseService.saveDatabase();
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
            throw new IllegalArgumentException("Site name is required.");
        }
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID is required.");
        }
        if (passwordValue == null || passwordValue.isBlank()) {
            throw new IllegalArgumentException("Password is required.");
        }
    }
}
