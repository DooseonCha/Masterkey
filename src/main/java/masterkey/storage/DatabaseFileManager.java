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
    private static final String MAGIC = "MKDB1";

    public void save(Database database, File file, String masterPassword) {
        if (database == null) {
            throw new IllegalArgumentException("Database is required.");
        }
        if (file == null) {
            throw new IllegalArgumentException("File is required.");
        }

        try {
            database.setFilePath(file.getAbsolutePath());

            byte[] plainBytes = serialize(database);
            byte[] salt = database.getMasterKey().getSalt();
            byte[] iv = CryptoUtil.generateIv();
            byte[] encryptedBytes = CryptoUtil.encrypt(plainBytes, masterPassword, salt, iv);

            if (file.getParentFile() != null) {
                Files.createDirectories(file.getParentFile().toPath());
            }

            try (DataOutputStream out = new DataOutputStream(Files.newOutputStream(file.toPath()))) {
                out.writeUTF(MAGIC);
                out.writeInt(salt.length);
                out.write(salt);
                out.writeInt(iv.length);
                out.write(iv);
                out.writeInt(encryptedBytes.length);
                out.write(encryptedBytes);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to save database.", e);
        }
    }

    public Database load(File file, String masterPassword) {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("Database file does not exist.");
        }

        try (DataInputStream in = new DataInputStream(Files.newInputStream(file.toPath()))) {
            String magic = in.readUTF();
            if (!MAGIC.equals(magic)) {
                throw new IllegalArgumentException("Invalid database file.");
            }

            byte[] salt = new byte[in.readInt()];
            in.readFully(salt);

            byte[] iv = new byte[in.readInt()];
            in.readFully(iv);

            byte[] encryptedBytes = new byte[in.readInt()];
            in.readFully(encryptedBytes);

            byte[] plainBytes = CryptoUtil.decrypt(encryptedBytes, masterPassword, salt, iv);
            Database database = deserialize(plainBytes);
            if (!database.getMasterKey().verifyPassword(masterPassword)) {
                throw new IllegalArgumentException("Invalid master password.");
            }
            database.setOpened(true);
            database.setFilePath(file.getAbsolutePath());
            return database;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to open database.", e);
        }
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
                throw new IllegalArgumentException("Invalid database content.");
            }
            return database;
        }
    }
}
