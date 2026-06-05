package masterkey.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Entry implements Serializable {
    private static final long serialVersionUID = 1L;

    private String entryId;
    private String siteName;
    private String siteUrl;
    private String userId;
    private Password password;
    private List<PasswordHistory> passwordHistories;
    private Alert alert;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public Entry(String siteName, String siteUrl, String userId, Password password) {
        this.entryId = UUID.randomUUID().toString();
        this.siteName = siteName == null ? "" : siteName;
        this.siteUrl = siteUrl == null ? "" : siteUrl;
        this.userId = userId == null ? "" : userId;
        this.password = password == null ? new Password("") : password;
        this.passwordHistories = new ArrayList<>();
        this.alert = new Alert();
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = this.createdAt;
    }

    public void updateEntry(String siteName, String siteUrl, String userId) {
        this.siteName = siteName == null ? "" : siteName;
        this.siteUrl = siteUrl == null ? "" : siteUrl;
        this.userId = userId == null ? "" : userId;
        this.modifiedAt = LocalDateTime.now();
    }

    public void changePassword(Password newPassword) {
        if (newPassword == null) {
            return;
        }

        if (this.password != null && !this.password.getPasswordValue().isBlank()) {
            addPasswordHistory(PasswordHistory.recordPassword(this.password));
        }

        this.password = newPassword;
        this.modifiedAt = LocalDateTime.now();
    }

    public Password getPassword() {
        return password;
    }

    public void addPasswordHistory(PasswordHistory history) {
        if (history != null) {
            passwordHistories.add(history);
        }
    }

    public List<PasswordHistory> getPasswordHistories() {
        return passwordHistories;
    }

    public boolean isPasswordExpired(UserSettings settings) {
        if (settings == null || password == null || password.getLastChangedAt() == null) {
            return false;
        }

        LocalDateTime threshold = password.getLastChangedAt().plusDays(settings.getPasswordChangePeriodDays());
        return LocalDateTime.now().isAfter(threshold);
    }

    public String getEntryId() {
        return entryId;
    }

    public String getSiteName() {
        return siteName;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public String getUserId() {
        return userId;
    }

    public Alert getAlert() {
        return alert;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }
}
