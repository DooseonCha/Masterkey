package masterkey.domain;

import java.io.Serializable;

public class PasswordPolicy implements Serializable {
    private static final long serialVersionUID = 1L;

    private int length;
    private String excludedCharacters;
    private PasswordCharacterOptions characterOptions;

    public PasswordPolicy() {
        this(16, "", new PasswordCharacterOptions());
    }

    public PasswordPolicy(int length, String excludedCharacters, PasswordCharacterOptions characterOptions) {
        this.length = length;
        this.excludedCharacters = excludedCharacters == null ? "" : excludedCharacters;
        this.characterOptions = characterOptions == null ? new PasswordCharacterOptions() : characterOptions;
    }

    public boolean validatePolicy() {
        return length >= 4 && length <= 128 && characterOptions != null && characterOptions.hasAnyOptionEnabled()
                && !getAvailableCharacters().isEmpty();
    }

    public String getAvailableCharacters() {
        StringBuilder chars = new StringBuilder();

        if (characterOptions.isUseUppercase()) {
            chars.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        }
        if (characterOptions.isUseLowercase()) {
            chars.append("abcdefghijklmnopqrstuvwxyz");
        }
        if (characterOptions.isUseNumbers()) {
            chars.append("0123456789");
        }
        if (characterOptions.isUseSpecialCharacters()) {
            chars.append("!@#$%^&*()-_=+[]{};:,.<>?");
        }

        String excluded = excludedCharacters == null ? "" : excludedCharacters;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < chars.length(); i++) {
            char c = chars.charAt(i);
            if (excluded.indexOf(c) < 0) {
                result.append(c);
            }
        }

        return result.toString();
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getExcludedCharacters() {
        return excludedCharacters;
    }

    public void setExcludedCharacters(String excludedCharacters) {
        this.excludedCharacters = excludedCharacters == null ? "" : excludedCharacters;
    }

    public PasswordCharacterOptions getCharacterOptions() {
        return characterOptions;
    }

    public void setCharacterOptions(PasswordCharacterOptions characterOptions) {
        this.characterOptions = characterOptions == null ? new PasswordCharacterOptions() : characterOptions;
    }
}
