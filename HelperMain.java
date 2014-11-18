// Michael Shintaku
// Started: 10/31/14
// Objective: Aids in my virtual items trading with general computing, value storage
// 			  message prompts, etc.

import javax.swing.*;
import java.io.*;
// import java.net.*:
import java.util.*;


public class HelperMain {
	
	//keyPrice and keyToRef fluctuate based on personal preference.
	//"keys" and "Refined" or "Ref" are in-game currencies.
	public static double keyPrice;
	public static double keyToRef;
	
	//Personal user information.
	private static String repTft = "";
	private static String repSource = "";
	private static String repTrust = "";
	private static String email = "michael.shintaku1@gmail.com";
	
	//To do: implement GUI, make program executable?
	public static void main (String args[]) throws FileNotFoundException  {

		Scanner input = new Scanner(System.in);
		/* Executes the program through console
		File settings = new File("settings.txt");
		if (settings.exists()) {
			loadPrices();
			System.out.println("Key price: " + keyPrice + "\n" + "Refined Price: " + keyToRef);
			System.out.print("Update prices?: y/n: ");
			if (input.nextLine().contains("y")) {
				updatePricesHelper(input);
				loadPrices();
			}
		} else {
			updatePricesHelper(input);
			loadPrices();
		}
		System.out.print("PayPal prompt?: y/n: ");
		if (input.nextLine().contains("y")) {
			System.out.print("id64: ");
			String id = input.nextLine();
			System.out.print("Number of keys: ");
			double num = Double.parseDouble(input.nextLine());
			System.out.println(question(id, num));
			System.out.print("SourceOP? y/n: ");
			System.out.println(leaveRep(input.nextLine().contains("y")));
		}
		//*/
		// executes program through gui
		HelperWindow window = new HelperWindow();
		System.out.print("Classified helper?: y/n: ");
		if (input.nextLine().contains("y")) {
			classifiedHelper(input);
		}
		//roundTester(input);
	}

	//used to update the prices via the console.
	public static void updatePricesHelper(Scanner input) throws FileNotFoundException {
		System.out.print("USD Price: ");
		double usd = Double.parseDouble(input.nextLine());
		System.out.print("Refined Price: ");
		double ref = Double.parseDouble(input.nextLine());
		updatePrices(usd, ref);
	}

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
	
	//pre: settings file is THE settings file created by updatePrices
	//post: sets field variables to data saved in settings file.
	public static void loadPrices() throws FileNotFoundException {
		Scanner fileReader = new Scanner(new File("settings.txt"));
		fileReader.next(); //scan past prefix/titles
		keyPrice = fileReader.nextDouble();
		fileReader.next();
		keyToRef = fileReader.nextDouble();
	}

	//post; saves prices passed in to a file named settings.txt
	//ToDo update it from external pricing website
	public static void updatePrices(double usd, double ref)  throws FileNotFoundException {
		PrintStream output = new PrintStream(new File("settings.txt"));
		output.println("keyPrice= " + usd);
		output.println("keyToRef= " + ref);
	}

	//ToDo: launch various websites and search for data?  Might need to use their apis
	public static String question(String id, double amount) {
		double total = round(amount * keyPrice);
		return amount + " keys will be $" + total + ", excluding fees, which are on you.  My account is US based.\n"
		+ "Ok when ready, send $" + total + " (excluding fees) to " + email + ", YOU pay any and all fees, and put this in the notes:\n"
		+ "I, [Real Name], steam user: " + id + " am buying " + amount + " TF2 keys, and understand that this is non-refundable and will not charge-back.";
	}

	public static String leaveRep(boolean sourceOP) {
		if (sourceOP) {
			return "Thanks!  Feel free to leave rep if you want!\n" + repSource +" or " + repTrust;
		} else {
			return "Thanks!  Feel free to leave rep if you want!\n" + repTrust + " or " + repTft;
		}
	}

	//pre: num must be a positive double, a.k.a, the $ they have to pay
	//post: rounds to two decimal places
	public static double round(double num) {
		int wholeDigits = (int) num;
		int decimals = ((int) ((num * 1000 - wholeDigits * 1000) + 5)) / 10; 
		// *1000/1000 because of loss of precision in storing/subtracting double
		// cast removes digits > thousandths place, / 10 reduces to two digits
		return wholeDigits + decimals / 100.0;
	}
	
	//repeatedly test the round method
	public static void roundTester(Scanner input) {
		boolean done = false;
		while (!done) {
			System.out.print("Round this number to two decimal places: ");
			System.out.println("Result: " + round(Double.parseDouble(input.nextLine())));
			System.out.print("End?  Type \"y\" ");
			done = input.nextLine().toLowerCase().contains("y");
		}
	}

}
