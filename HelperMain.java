// Michael Shintaku
// Started: 10/31/14
// Objective: Aids in my virtual items trading with general computing, value storage
// 			  message prompts, etc.

import java.net.*;
import java.util.*;

public class HelperMain {
	
	//keyPrice and keyToRef fluctuate based on personal preference.
	public static double keyPrice = 1.90; 
	public static String id64;
	public double keyAmount;
	public static double keyToRef = 89 / 9.0; //9.88 ref
	
	//Personal user information.
	private static String repTft = "";
	private static String repSource = "";
	private static String repTrust = "";
	
	
	
	public static void main (String args[]) {
		Scanner input = new Scanner(System.in);
		updatePrices(input);
		paypalPrompt(input);
		
	}
	
	//post: updates the fields
	//ToDo: save updates from saved files, or update it from external pricing website
	public static void updatePrices(Scanner input) {
		System.out.print("Enter price of keys in $: ");
		keyPrice = input.nextDouble();
		System.out.print("Enter refined to key ratio: ");
		keyToRef = input.nextDouble();
		input.nextLine(); //scans past the white enter space
	}
	
	public static void paypalPrompt(Scanner input) {
		System.out.println("Ok, where did you see my listing, how many keys do you want, "
							+ "do you have any rep threads, and from what e-mail will you be sending from?");
		try {
			String url = "http://steamrep.com";
			java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
		} 
		catch (java.io.IOException e) {
			System.out.println(e.getMessage());
		}
		System.out.print("id64: ");
		id64 = input.nextLine();
		System.out.print("SOP? Type y/n: ");
		boolean SOP = input.nextLine().contains("y");
		System.out.print("Number of keys?: ");
		double amount = input.nextDouble();
		input.nextLine(); //scans past the white enter space
		double total = round(amount * keyPrice);
		System.out.println(amount + " keys will be $" + total
							+ ", excluding fees, which are on you.");
		
		System.out.println("Ok when ready, send $" + total + " (excluding fees) to michael.shintaku1@gmail.com, YOU pay any and all fees, and put this in the notes:");
		System.out.println("I, [Real Name], steam user: " + id64 + " am buying " + amount + " TF2 keys, and understand that this is non-refundable and will not charge-back.");
		System.out.print("Type \"y\" to continue: ");
		if (input.nextLine().contains("y")) {
			if (SOP) {
				System.out.println("Thanks!  Feel free to leave rep if you want!\n"
									+ repSource +" or " + repTrust);
			} else {
				System.out.println("Thanks!  Feel free to leave rep if you want!\n"
									+ repTrust + " or " + repTft);
			}
		}
	}
	
	//pre: num must be a positive double, a.k.a, the $ they have to pay
	//post: rounds to two decimal places
	public static double round(double num) {
		int wholeDigits = (int) num;
		int decimals = ((int) ((num * 1000 - wholeDigits * 1000) + 5)) / 10; 
		// *1000/1000 because of loss of precision in storing double
		// so must convert to int to preserve digits.
		return wholeDigits + decimals / 100.0;
	}
	
	public static void roundTester(Scanner input) {
		boolean done = false;
		while (!done) {
			System.out.print("Round this number to two decimal places: ");
			Scanner scanLine = new Scanner(input.nextLine());
			System.out.println("Result: " + round(scanLine.nextDouble()));
			System.out.print("End?  Type \"y\" ");
			done = input.nextLine().toLowerCase().contains("y");
		}
	}

}
