package masterkey.domain;

import java.io.Serializable;
import java.security.SecureRandom;
import java.time.LocalDateTime;

public class Password implements Serializable {
    private static final long serialVersionUID = 1L;

    private String passwordValue;
    private int strengthLevel;
    private LocalDateTime createdAt;
    private LocalDateTime lastChangedAt;

    public Password(String passwordValue) {
        this.passwordValue = passwordValue == null ? "" : passwordValue;
        this.createdAt = LocalDateTime.now();
        this.lastChangedAt = this.createdAt;
        this.strengthLevel = evaluateStrength();
    }

    public static Password generatePassword(PasswordPolicy policy) {
        if (policy == null || !policy.validatePolicy()) {
            throw new IllegalArgumentException("Invalid password policy.");
        }

        String availableCharacters = policy.getAvailableCharacters();
        SecureRandom random = new SecureRandom();
        StringBuilder value = new StringBuilder();

        for (int i = 0; i < policy.getLength(); i++) {
            int index = random.nextInt(availableCharacters.length());
            value.append(availableCharacters.charAt(index));
        }

        return new Password(value.toString());
    }

    public int evaluateStrength() {
        int score = 0;

        if (passwordValue.length() >= 8) score++;
        if (passwordValue.length() >= 12) score++;
        if (passwordValue.matches(".*[A-Z].*")) score++;
        if (passwordValue.matches(".*[a-z].*")) score++;
        if (passwordValue.matches(".*[0-9].*")) score++;
        if (passwordValue.matches(".*[^A-Za-z0-9].*")) score++;

        this.strengthLevel = Math.min(score, 5);
        return strengthLevel;
    }

    public void updatePassword(String newValue) {
        this.passwordValue = newValue == null ? "" : newValue;
        this.lastChangedAt = LocalDateTime.now();
        this.strengthLevel = evaluateStrength();
    }

    public boolean isValid(PasswordPolicy policy) {
        if (policy == null || passwordValue == null) {
            return false;
        }
        return passwordValue.length() >= Math.min(policy.getLength(), 4);
    }

    public String getPasswordValue() {
        return passwordValue;
    }

    public int getStrengthLevel() {
        return strengthLevel;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastChangedAt() {
        return lastChangedAt;
    }

    @Override
    public String toString() {
        return "********";
    }
}
