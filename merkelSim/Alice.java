import javax.crypto.SecretKey;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * Alice class
 * Created by Kevin Norman and Gen Estrada
 * Alice is a representation of someone who generates puzzles for somebody to solve for key negotiation.
 * TO use, create an instance of Alice and then call its init method to generate the puzzles.
 */
public class Alice {
    //the set of puzzles, becomes available after init is called.
    private Map<Integer,Puzzle> puzzleSet;
    //the puzzle number that is sent to Alice after one of her puzzles is cracked.
    private int puzzleNumber;

    /**
     * init sets Alice up effectively. Alice will make a set of puzzles, and write them down for whoever wants to solve them.
     * @param filename - where Alice should write the puzzles down
     * @throws IOException - if an error occurs when writing the file. Usually due to permissions in the writing directory.
     */
    public void init(String filename) throws IOException{
        puzzleSet = PuzzleFactory.makePuzzles();
        System.out.println("Alice init::Writing puzzles into file");
        FileOutputStream fos;
        fos = new FileOutputStream(filename);
        for (Map.Entry<Integer, Puzzle> entry : puzzleSet.entrySet()) {
            fos.write(entry.getValue().getEncryptedPuzzle().getBytes());
            //write them on new lines
            fos.write("\r\n".getBytes());
        }
        fos.close();
    }

    /**
     * Whoever solves Alices puzzle calls setPuzzle in order to tell Alice which puzzle they have solved.
     * @param pn the puzzle number that was solved.
     */
    public void setPuzzle(int pn) {
        System.out.println("Alice setPuzzle::Received puzzle number from Bob. Storing it.");
        puzzleNumber = pn;
        byte[] key = puzzleSet.get(puzzleNumber).getKey();
    }

    /**
     * Encrypts a given string with previously agreed upon key and returns it.
     * @return encrypted ciphertext, null if failure.
     */
    public String sendMessage() {
        System.out.println("Alice sendMessage:: Sending encrypted ciphertext to Bob.");
        try {
            SecretKey sk = CryptoLib.createKey(puzzleSet.get(puzzleNumber).getKey());
            return DESLib.encrypt("hello Bob",sk);
        } catch(Exception e) {
            System.err.println("Something went wrong sending the message");
        }
        return null;
    }
}
