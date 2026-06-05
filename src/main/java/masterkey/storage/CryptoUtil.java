package masterkey.storage;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public final class CryptoUtil {
    private static final int SALT_LENGTH = 16;
    private static final int IV_LENGTH = 12;
    private static final int KEY_LENGTH = 256;
    private static final int HASH_ITERATIONS = 100_000;
    private static final int GCM_TAG_LENGTH = 128;

    private CryptoUtil() {
    }

    public static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    public static byte[] generateIv() {
        byte[] iv = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    public static String hashPassword(String password, byte[] salt) {
        try {
            byte[] hash = deriveKeyBytes(password, salt, HASH_ITERATIONS, KEY_LENGTH);
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new IllegalStateException("Password hashing failed.", e);
        }
    }

    public static SecretKeySpec deriveAesKey(String password, byte[] salt) {
        try {
            byte[] key = deriveKeyBytes(password, salt, HASH_ITERATIONS, KEY_LENGTH);
            return new SecretKeySpec(key, "AES");
        } catch (Exception e) {
            throw new IllegalStateException("Key derivation failed.", e);
        }
    }

    private static byte[] deriveKeyBytes(String password, byte[] salt, int iterations, int keyLength) throws Exception {
        char[] chars = password == null ? new char[0] : password.toCharArray();
        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, keyLength);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] result = factory.generateSecret(spec).getEncoded();
        Arrays.fill(chars, '\0');
        spec.clearPassword();
        return result;
    }

    public static byte[] encrypt(byte[] plainBytes, String password, byte[] salt, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, deriveAesKey(password, salt), new GCMParameterSpec(GCM_TAG_LENGTH, iv));
            return cipher.doFinal(plainBytes);
        } catch (Exception e) {
            throw new IllegalStateException("Encryption failed.", e);
        }
    }

    public static byte[] decrypt(byte[] encryptedBytes, String password, byte[] salt, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, deriveAesKey(password, salt), new GCMParameterSpec(GCM_TAG_LENGTH, iv));
            return cipher.doFinal(encryptedBytes);
        } catch (Exception e) {
            throw new IllegalArgumentException("Decryption failed. Check the master password or file.", e);
        }
    }

    public static boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) {
            return false;
        }
        return MessageDigest.isEqual(a.getBytes(StandardCharsets.UTF_8), b.getBytes(StandardCharsets.UTF_8));
    }
}
