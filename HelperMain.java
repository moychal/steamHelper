// Michael Shintaku
// Started: 10/31/14
// Objective: Aids in my virtual items trading with general computing, value storage
// 			  message prompts, etc.

import java.io.*;
import java.util.*;


public class HelperMain {

    //To do: improve GUI, make program executable?
    public static void main (String args[]) throws FileNotFoundException  {

        Scanner input = new Scanner(System.in);
        // executes program through gui
        HelperWindow window = new HelperWindow();

        //System.out.print("Classified helper?: y/n: ");
        //if (input.nextLine().contains("y")) {
        //    classifiedHelper(input);
        //}

        //roundTester(input);
    }
    /*
    //todo: not rounding/preserving precision correctly
    public static void classifiedHelper(Scanner input) {
        boolean done = false;
        while (!done) {
            System.out.print("Price in keys: ");
            double num = Double.parseDouble(input.nextLine());
            int wholeNum = (int) num;
            System.out.println(wholeNum + " keys and " + (int) (num * 100 - (wholeNum * 100)) / 100.0 * keyToRef + " Refined");
            //*100 b/c imprecision in doubles, cast to int to truncate to only two decimal places, no rounding, need to do.
            System.out.print("End?  Type \"y\" ");
            done = input.nextLine().toLowerCase().contains("y");
        }
    }
    */
}
