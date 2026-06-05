package masterkey.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Alert implements Serializable {
    private static final long serialVersionUID = 1L;

    private String alertId;
    private boolean expired;
    private String message;
    private LocalDateTime checkedAt;

    public Alert() {
        this.alertId = UUID.randomUUID().toString();
        this.expired = false;
        this.message = "";
        this.checkedAt = LocalDateTime.now();
    }

    public boolean checkExpired(Entry entry, UserSettings settings) {
        this.checkedAt = LocalDateTime.now();
        this.expired = entry != null && settings != null && settings.isAlertEnabled() && entry.isPasswordExpired(settings);
        this.message = expired ? generateMessage(entry) : "";
        return expired;
    }

    public String generateMessage(Entry entry) {
        String name = entry == null ? "Unknown" : entry.getSiteName();
        return "Password change period has expired: " + name;
    }

    public void activateAlert() {
        this.expired = true;
    }

    public void clearAlert() {
        this.expired = false;
        this.message = "";
    }

    public boolean isExpired() {
        return expired;
    }

    public String getAlertId() {
        return alertId;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCheckedAt() {
        return checkedAt;
    }
}
