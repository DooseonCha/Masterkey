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

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private CryptoUtil() {
    }

    public static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        SECURE_RANDOM.nextBytes(salt);
        return salt;
    }

    public static byte[] generateIv() {
        byte[] iv = new byte[IV_LENGTH];
        SECURE_RANDOM.nextBytes(iv);
        return iv;
    }

    public static String generateRecoveryKey() {
        byte[] bytes = new byte[24];
        SECURE_RANDOM.nextBytes(bytes);
        String raw = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes).toUpperCase();

        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < raw.length(); i++) {
            if (i > 0 && i % 4 == 0) {
                formatted.append('-');
            }
            formatted.append(raw.charAt(i));
        }
        return formatted.toString();
    }

    public static String hashPassword(String password, byte[] salt) {
        try {
            byte[] hash = deriveKeyBytes(password, salt, HASH_ITERATIONS, KEY_LENGTH);
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new IllegalStateException("비밀번호 해시 처리에 실패했습니다.", e);
        }
    }

    public static SecretKeySpec deriveAesKey(String password, byte[] salt) {
        try {
            byte[] key = deriveKeyBytes(password, salt, HASH_ITERATIONS, KEY_LENGTH);
            return new SecretKeySpec(key, "AES");
        } catch (Exception e) {
            throw new IllegalStateException("암호화 키 생성에 실패했습니다.", e);
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
            throw new IllegalStateException("암호화에 실패했습니다.", e);
        }
    }

    public static byte[] decrypt(byte[] encryptedBytes, String password, byte[] salt, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, deriveAesKey(password, salt), new GCMParameterSpec(GCM_TAG_LENGTH, iv));
            return cipher.doFinal(encryptedBytes);
        } catch (Exception e) {
            throw new IllegalArgumentException("복호화에 실패했습니다. 비밀번호, 복구키 또는 파일을 확인하세요.", e);
        }
    }

    public static byte[] encryptString(String plainText, String password, byte[] salt, byte[] iv) {
        return encrypt(plainText.getBytes(StandardCharsets.UTF_8), password, salt, iv);
    }

    public static String decryptString(byte[] encryptedBytes, String password, byte[] salt, byte[] iv) {
        return new String(decrypt(encryptedBytes, password, salt, iv), StandardCharsets.UTF_8);
    }

    public static boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) {
            return false;
        }
        return MessageDigest.isEqual(a.getBytes(StandardCharsets.UTF_8), b.getBytes(StandardCharsets.UTF_8));
    }
}
