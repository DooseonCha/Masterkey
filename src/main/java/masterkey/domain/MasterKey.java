package masterkey.domain;

import masterkey.storage.CryptoUtil;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;

public class MasterKey implements Serializable {
    private static final long serialVersionUID = 1L;

    private String passwordHash;
    private byte[] salt;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public MasterKey(String password) {
        this.salt = CryptoUtil.generateSalt();
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = this.createdAt;
        setMasterPassword(password);
    }

    public void setMasterPassword(String password) {
        this.passwordHash = CryptoUtil.hashPassword(password, salt);
        this.modifiedAt = LocalDateTime.now();
    }

    public boolean verifyPassword(String inputPassword) {
        String inputHash = CryptoUtil.hashPassword(inputPassword, salt);
        return passwordHash != null && passwordHash.equals(inputHash);
    }

    public boolean changeMasterPassword(String oldPassword, String newPassword) {
        if (!verifyPassword(oldPassword)) {
            return false;
        }
        setMasterPassword(newPassword);
        return true;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public byte[] getSalt() {
        return Arrays.copyOf(salt, salt.length);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }
}
