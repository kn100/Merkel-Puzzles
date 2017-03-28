import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;

/**
 * Puzzle class
 * Created by Kevin Norman and Genalyn Estrada
 * This class stores one single puzzle.
 */
public class Puzzle {
    byte[] padding = new byte[16];
    byte[] puzzleID;
    byte[] key;
    SecretKey encryptionKey;

    /**
     * Puzzle constructor Generates a puzzle with its own unique puzzle encryption/decryption key as well as an internal
     * key to discover after solving the puzzle.
     * @param pID - the ID the puzzle should have.
     */
    public Puzzle(int pID) {
        puzzleID = CryptoLib.smallIntToByteArray(pID);
        SecretKey skey = CryptoLib.generateRandomKey();
        this.key = skey.getEncoded();
        try {
            encryptionKey = CryptoLib.createPuzzleKey();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * getPuzzleData - returns unencrypted version of puzzle in byte[] array.
     * @return byte array: First 16 bytes are 0's (padding), 17th and 18th byte are a 'short', and 19-26 byte are a DES key.
     */
    private byte[] getPuzzleData() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        try {
            outputStream.write(padding);
            outputStream.write(puzzleID);
            outputStream.write(key);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }

    /**
     * Provides an encrypted string that contains within it the puzzle data. Encryption key is deliberately unknown;
     * it was set in the initialisation of the puzzle object. The creator doesn't need to know the key as we have them
     * unencrypted, and the person cracking the puzzle doesn't need to know the key as that would defeat the object.
     * @return ciphertext string
     */
    public String getEncryptedPuzzle() {
        String encrypted = "";
        try {
           encrypted = DESLib.encrypt(CryptoLib.byteArrayToString(getPuzzleData()),encryptionKey);
        } catch(Exception e) {
            System.out.println("Something failed in encryption process...");
            e.printStackTrace();
        }

        return encrypted;
    }

    /**
     * Provides the encryption key stored within the puzzle. Alice does need to know this, since it will be used for
     * message encryption and decryption.
     * @return byte array containing the key.
     */
    public byte[] getKey() {
        return key;
    }
}
