package masterkey.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class PasswordHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private String historyId;
    private String previousPassword;
    private LocalDateTime changedAt;

    public PasswordHistory(String previousPassword) {
        this.historyId = UUID.randomUUID().toString();
        this.previousPassword = previousPassword == null ? "" : previousPassword;
        this.changedAt = LocalDateTime.now();
    }

    public static PasswordHistory recordPassword(Password password) {
        String value = password == null ? "" : password.getPasswordValue();
        return new PasswordHistory(value);
    }

    public String getHistoryId() {
        return historyId;
    }

    public String getPreviousPassword() {
        return previousPassword;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }
}
