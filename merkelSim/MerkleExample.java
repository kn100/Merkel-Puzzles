import java.io.IOException;

/**
 * Created by kn100 on 28/03/17.
 */
public class MerkleExample {
    static final String FILENAME = "puzzles.bin";
    public static void main(String[] args) {
        Alice alice = new Alice();
        try {
            alice.init(FILENAME);
        } catch(IOException e) {
            System.err.println("Something went wrong while Alice was writing the file.");
            e.printStackTrace();
        }
        //Alice now has a list of Merkle puzzles and they've been written down in a text file.

        //Bob reads this file in, picks a line at random, cracks it, and stores a particular puzzle number.
        Bob bob = null;
        try {
            bob = new Bob(FILENAME);
        } catch(IOException e) {
            System.err.println("Something went wrong while Bob was parsing the file.");
            e.printStackTrace();
        }

        // Bob sends Alice his puzzle number.
        alice.setPuzzle(bob.getPuzzleNumber());

        //---KEY NEGOTIATION FINISHED---

        //Alice sends ciphertext encrypted with the correct key to Bob. Bob receives and decrypts it.
        //Bob will print what he decrypts.
        bob.receiveAndDecrypt(alice.sendMessage());

    }
}
