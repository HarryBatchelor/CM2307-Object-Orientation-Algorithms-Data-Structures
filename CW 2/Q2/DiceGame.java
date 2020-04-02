import java.io.*;
import java.util.*;


public class DiceGame implements GameInterface {

  // The BufferedReader used throughout
  public static BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

  // The random number generator used throughout
  public static RandomInterface r =new LinearCongruentialGenerator();

  // Variable(s) used in the dice game methods
  public static HashSet<Integer> numbersRolled=new HashSet<Integer>();

    public void initialiseGame() {
        // The initialisation phase:

        // Actually there isn't anything to do here
    }

    public void mainGame() {
        // The main game:

        // Let the user roll the dice twice
        for (int i=0; i<2; i++) {
            System.out.println("Hit <RETURN> to roll the dice");

            try {br.readLine();}
            catch (IOException e){System.out.println(e);}

            int diceRoll=(int)(r.next() * 6) + 1;

            System.out.println("You rolled " + diceRoll);
            numbersRolled.add(new Integer(diceRoll));
        }

        // Display the numbers rolled
        System.out.println("Numbers rolled: " + numbersRolled);
    }

    public void declareGameWinner() {
        // Declare the winner:

        // User wins if at least one of the dice rolls is a 1
        if (numbersRolled.contains(new Integer(1))) {
            System.out.println("You won!");
        }
        else System.out.println("You lost!");
    }


}
