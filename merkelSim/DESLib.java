import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.util.Base64;
/**
 * DESLib class
 * Created by Kevin Norman and Gen Estrada - based off code provided by Phil.
 * DESLib provides encryption and decryption helpers for other classes to use. This is based off of a lab.
 */
public class DESLib {
    /**
     * Encrypt function encrypts a given plainText using a secretKey object.
     * @param plainText the text to encrypt
     * @param secretKey the secretKey to use for encryption
     * @return ciphertext
     * @throws Exception if the DES encryption scheme is unavailable somehow.
     */
    public static String encrypt(String plainText, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("DES");
        //Convert plaintext into byte representation
        byte[] plainTextByte = plainText.getBytes();

        //Initialise the cipher to be in encrypt mode, using the given key.
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        //Perform the encryption
        byte[] encryptedByte = cipher.doFinal(plainTextByte);

        //Get a new Base64 (ASCII) encoder and use it to convert ciphertext back to a string
        Base64.Encoder encoder = Base64.getEncoder();
        String encryptedText = encoder.encodeToString(encryptedByte);

        return encryptedText;
    }

    /**
     * Decrypt function decrypts a given plainText using a key - which is transformed into a SecretKey object internally
     * @param encryptedText Ciphertext
     * @param key Byte array with size 8 that stores 56 byte (8 bits for parity) DES key.
     * @return plaintext
     * @throws Exception if DES encryption scheme is unavailable.
     */
    public static String decrypt(String encryptedText, byte[] key) throws Exception {
        SecretKey secretKey = CryptoLib.createKey(key);
        Cipher cipher = Cipher.getInstance("DES");
        //Get a new Base64 (ASCII) decoder and use it to convert ciphertext from a string into bytes
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] encryptedTextByte = decoder.decode(encryptedText);

        //Initialise the cipher to be in decryption mode, using the given key.
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        //Perform the decryption
        byte[] decryptedByte = cipher.doFinal(encryptedTextByte);

        //Convert byte representation of plaintext into a string
        String decryptedText = new String(decryptedByte);

        return decryptedText;
    }
}