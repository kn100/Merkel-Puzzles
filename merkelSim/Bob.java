import java.io.*;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * Bob class
 * Created by Kevin Norman and Gen Estrada
 * Designed to represent someone who wants to communicate with Alice. Bob will bruteforce one of Alices puzzles, and
 * then will store the puzzle number of the cracked puzzle, as well as the key. Alice knows both of these facts, so
 * Bob just has to transmit the puzzle number to Alice for a key to be successfully negotiated.
 */
public class Bob {
    //the key that Bob cracked
    private byte[] crackedKey;
    //the puzzle number that Bob cracked.
    private int puzzleNumber;

    /**
     * Bob constructor - reads in a file, picks a line of it, attempts to decrypt it via brute force, and stores
     * details about whichever one he cracks.
     * @param filename which file to read in
     * @throws IOException if there was an error reading the file in.
     */
    public Bob(String filename) throws IOException {
        String crackedPuzzle = readPuzzlesAndCrackOne(filename);
        if(crackedPuzzle!=null){
            byte[] crackedPuzzleBytes = CryptoLib.stringToByteArray(crackedPuzzle);

            byte[] puzzleNumBytes = {crackedPuzzleBytes[16],crackedPuzzleBytes[17]};
            puzzleNumber = CryptoLib.byteArrayToSmallInt(puzzleNumBytes);

            crackedKey = Arrays.copyOfRange(crackedPuzzleBytes,18,26);
        }
    }

    /**
     * @return the puzzle that Bob has cracked.
     */
    public int getPuzzleNumber() {
        //System.out.println(CryptoLib.byteArrayToString(crackedKey));
        return puzzleNumber;
    }

    /**
     * Reads in a file containing a list of DES encrypted strings, and picks one of the first 1024 lines. Then calls
     * bruteforce on it.
     * @param filename the file to read from
     * @return decrypted string, null if unsuccessful.
     * @throws IOException if error reading in file.
     */
    private String readPuzzlesAndCrackOne(String filename) throws IOException {
        System.out.println("Bob readPuzzlesAndCrackOne:: Reading in list of puzzles and picking one to crack.");
        SecureRandom random = new SecureRandom();
        int pickedPuzzle = random.nextInt(1024);
        //System.out.println("I picked "+pickedPuzzle);

        BufferedReader br = new BufferedReader(new FileReader(filename));
        //skip all irrelevant lines
        for(int i=0;i<pickedPuzzle-1;i++) {
            br.readLine();
        }
        //return correct line
        return bruteforce(br.readLine());

    }

    /**
     * Cracks a given DES ciphertext, if the first 6 bytes of the key are zero...
     * @param puzzle - A DES encrypted ciphertext
     * @return decrypted string, null if unsuccessful.
     */
    private String bruteforce(String puzzle) {
        System.out.println("Bob Bruteforce:: Bruteforcing a particular ciphertext.");
        byte[] key = new byte[8];
        for(int i=-128;i<128;i++){
            for(int j=-128;j<128;j++) {
                key[0] = (byte)i;
                key[1] = (byte)j;
                //System.out.println(Arrays.toString(key));
                if(checkKey(puzzle,key)){
                    try{
                        return DESLib.decrypt(puzzle,key);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    /**
     * Since we know that all plaintext is prefaced by 16 zeros, we can go ahead and verify that on a decrypted string.
     * if they're all zero, then the key is correct.
     * @param puzzle - a DES encrypted ciphertext
     * @param key - a key to check.
     * @return true if key successfully decrypted ciphertext, false if not.
     */
    private boolean checkKey(String puzzle, byte[] key) {
        try {
            String decrypted = DESLib.decrypt(puzzle,key);
            byte[] decryptedBytes = CryptoLib.stringToByteArray(decrypted);
            for(int i=0;i<15;i++) {
                if(decryptedBytes[i]!=0){
                    return false;
                } else {
                    continue;
                }
            }
            System.out.println("Bob checkKey:: Successfully decrypted.");
            return true;
        } catch(Exception e) {
            //invalid format, not the key. no further checking.
            return false;
        }
    }

    /**
     * Receives ciphertext from Alice and decrypts it. Just prints it here.
     * @param ciphertext - ciphertext encrypted with previously agreed upon key
     */
    public void receiveAndDecrypt(String ciphertext) {
        System.out.println("Bob receiveAndDecrypt:: Received ciphertext from Alice. Decrypting.");
        try{
            System.out.println("Bob receiveAndDecrypt:: "+DESLib.decrypt(ciphertext, crackedKey));
        } catch(Exception e) {
            System.err.println("Something went wrong receiving the message");
            e.printStackTrace();
        }

    }
}
