package masterkey.domain;

import java.io.Serializable;

public class UserSettings implements Serializable {
    private static final long serialVersionUID = 1L;

    private int passwordChangePeriodDays;
    private boolean alertEnabled;
    private boolean autoBackupEnabled;

    public UserSettings() {
        this.passwordChangePeriodDays = 90;
        this.alertEnabled = true;
        this.autoBackupEnabled = false;
    }

    public void setPasswordChangePeriod(int days) {
        this.passwordChangePeriodDays = Math.max(days, 1);
    }

    public int getPasswordChangePeriodDays() {
        return passwordChangePeriodDays;
    }

    public int getPasswordChangePeriod() {
        return passwordChangePeriodDays;
    }

    public void enableAlert() {
        this.alertEnabled = true;
    }

    public void disableAlert() {
        this.alertEnabled = false;
    }

    public boolean isAlertEnabled() {
        return alertEnabled;
    }

    public void enableAutoBackup() {
        this.autoBackupEnabled = true;
    }

    public void disableAutoBackup() {
        this.autoBackupEnabled = false;
    }

    public boolean isAutoBackupEnabled() {
        return autoBackupEnabled;
    }
}
