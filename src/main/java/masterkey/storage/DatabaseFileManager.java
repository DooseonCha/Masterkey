package masterkey.storage;

import masterkey.domain.Database;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;

public class DatabaseFileManager {
    private static final String MAGIC_V1 = "MKDB1";
    private static final String MAGIC_V2 = "MKDB2";

    public void save(Database database, File file, String masterPassword) {
        if (database == null) {
            throw new IllegalArgumentException("데이터베이스 정보가 필요합니다.");
        }
        if (file == null) {
            throw new IllegalArgumentException("파일이 필요합니다.");
        }

        try {
            database.setFilePath(file.getAbsolutePath());

            byte[] plainBytes = serialize(database);
            byte[] masterSalt = database.getMasterKey().getSalt();
            byte[] masterIv = CryptoUtil.generateIv();
            byte[] encryptedDatabase = CryptoUtil.encrypt(plainBytes, masterPassword, masterSalt, masterIv);

            if (file.getParentFile() != null) {
                Files.createDirectories(file.getParentFile().toPath());
            }

            try (DataOutputStream out = new DataOutputStream(Files.newOutputStream(file.toPath()))) {
                if (database.hasRecoveryKey()) {
                    out.writeUTF(MAGIC_V2);
                    writeBytes(out, masterSalt);
                    writeBytes(out, masterIv);
                    writeBytes(out, encryptedDatabase);
                    writeBytes(out, database.getRecoverySalt());
                    writeBytes(out, database.getRecoveryIv());
                    writeBytes(out, database.getEncryptedMasterPasswordByRecovery());
                } else {
                    out.writeUTF(MAGIC_V1);
                    writeBytes(out, masterSalt);
                    writeBytes(out, masterIv);
                    writeBytes(out, encryptedDatabase);
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("데이터베이스 저장에 실패했습니다.", e);
        }
    }

    public Database load(File file, String masterPassword) {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("데이터베이스 파일이 존재하지 않습니다.");
        }

        try (DataInputStream in = new DataInputStream(Files.newInputStream(file.toPath()))) {
            String magic = in.readUTF();
            if (!MAGIC_V1.equals(magic) && !MAGIC_V2.equals(magic)) {
                throw new IllegalArgumentException("올바른 데이터베이스 파일이 아닙니다.");
            }

            byte[] masterSalt = readBytes(in);
            byte[] masterIv = readBytes(in);
            byte[] encryptedDatabase = readBytes(in);

            if (MAGIC_V2.equals(magic)) {
                readBytes(in); // recovery salt
                readBytes(in); // recovery iv
                readBytes(in); // encrypted master password
            }

            byte[] plainBytes = CryptoUtil.decrypt(encryptedDatabase, masterPassword, masterSalt, masterIv);
            Database database = deserialize(plainBytes);
            if (!database.getMasterKey().verifyPassword(masterPassword)) {
                throw new IllegalArgumentException("마스터 비밀번호가 올바르지 않습니다.");
            }
            database.setOpened(true);
            database.setFilePath(file.getAbsolutePath());
            return database;
        } catch (Exception e) {
            throw new IllegalArgumentException("데이터베이스 열기에 실패했습니다.", e);
        }
    }

    public String recoverMasterPassword(File file, String recoveryKey) {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("데이터베이스 파일이 존재하지 않습니다.");
        }
        if (recoveryKey == null || recoveryKey.isBlank()) {
            throw new IllegalArgumentException("복구키를 입력해야 합니다.");
        }

        try (DataInputStream in = new DataInputStream(Files.newInputStream(file.toPath()))) {
            String magic = in.readUTF();
            if (!MAGIC_V2.equals(magic)) {
                throw new IllegalArgumentException("복구키가 설정되지 않은 데이터베이스 파일입니다.");
            }

            readBytes(in); // master salt
            readBytes(in); // master iv
            readBytes(in); // encrypted database

            byte[] recoverySalt = readBytes(in);
            byte[] recoveryIv = readBytes(in);
            byte[] encryptedMasterPassword = readBytes(in);

            return CryptoUtil.decryptString(encryptedMasterPassword, recoveryKey, recoverySalt, recoveryIv);
        } catch (Exception e) {
            throw new IllegalArgumentException("복구키가 올바르지 않거나 복구에 실패했습니다.", e);
        }
    }

    private void writeBytes(DataOutputStream out, byte[] data) throws Exception {
        byte[] safeData = data == null ? new byte[0] : data;
        out.writeInt(safeData.length);
        out.write(safeData);
    }

    private byte[] readBytes(DataInputStream in) throws Exception {
        int length = in.readInt();
        if (length < 0) {
            throw new IllegalArgumentException("파일 형식이 올바르지 않습니다.");
        }
        byte[] data = new byte[length];
        in.readFully(data);
        return data;
    }

    private byte[] serialize(Database database) throws Exception {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        try (ObjectOutputStream objectOut = new ObjectOutputStream(byteOut)) {
            objectOut.writeObject(database);
        }
        return byteOut.toByteArray();
    }

    private Database deserialize(byte[] data) throws Exception {
        try (ObjectInputStream objectIn = new ObjectInputStream(new ByteArrayInputStream(data))) {
            Object object = objectIn.readObject();
            if (!(object instanceof Database database)) {
                throw new IllegalArgumentException("데이터베이스 내용이 올바르지 않습니다.");
            }
            return database;
        }
    }
}
