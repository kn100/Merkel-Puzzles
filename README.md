# Merkel Puzzle Simulator
Authored by Kevin Norman (787037) and Genalyn Estrada (745720)

## How to compile it
* Open a terminal or command prompt within the merkelSim folder.
* Run ```javac *java``` to compile all the Java files.
* The main method is within MerkelExample.java. ```java MerkelExample```

## Explanation
Upon running it, some fairly self explanatory debug messages will appear. If the final line shows that Bob received and decrypted hello Bob, it is working as expected. 

MerkelExample creates an instance of Alice and Bob. Here, Alice is the originator of puzzles, and Bob is the puzzle solving side of the equation. DESLib is based heavily on an IT Security lab that we completed previously, except we're using DES instead of AES. Puzzles are created using the PuzzleFactory makePuzzles factory method and are stored within Alice. 

If more detail is required, please see the code - it is commented using Javadoc notation.



