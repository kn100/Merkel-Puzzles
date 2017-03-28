import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

/**
 * Created by kn100 on 28/03/17.
 */
public class Puzzle {
    byte[] padding = new byte[16];
    byte[] puzzleID;
    byte[] key;
    public Puzzle(int pID) {
        puzzleID = CryptoLib.smallIntToByteArray(pID);
        SecretKey skey = CryptoLib.generateRandomKey();
        this.key = skey.getEncoded();
        System.out.println(CryptoLib.byteArrayToString(key));
    }
}
