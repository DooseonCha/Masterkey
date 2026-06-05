package masterkey.service;

import masterkey.domain.Database;
import masterkey.domain.Entry;
import masterkey.domain.UserSettings;

import java.util.ArrayList;
import java.util.List;

public class AlertService {
    private final DatabaseService databaseService;

    public AlertService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public List<Entry> getExpiredEntries() {
        Database database = databaseService.getCurrentDatabase();
        List<Entry> expiredEntries = new ArrayList<>();

        if (database == null) {
            return expiredEntries;
        }

        UserSettings settings = database.getUserSettings();
        if (!settings.isAlertEnabled()) {
            return expiredEntries;
        }

        for (Entry entry : database.getEntries()) {
            if (entry.isPasswordExpired(settings)) {
                expiredEntries.add(entry);
            }
        }

        return expiredEntries;
    }

    public void updateAlertSettings(boolean enabled, int periodDays) {
        Database database = databaseService.getCurrentDatabase();
        if (database == null) {
            throw new IllegalStateException("Database is not opened.");
        }

        UserSettings settings = database.getUserSettings();
        settings.setPasswordChangePeriod(periodDays);

        if (enabled) {
            settings.enableAlert();
        } else {
            settings.disableAlert();
        }

        databaseService.saveDatabase();
    }

    public void updateAutoBackup(boolean enabled) {
        Database database = databaseService.getCurrentDatabase();
        if (database == null) {
            throw new IllegalStateException("Database is not opened.");
        }

        if (enabled) {
            database.getUserSettings().enableAutoBackup();
        } else {
            database.getUserSettings().disableAutoBackup();
        }

        databaseService.saveDatabase();
    }
}
