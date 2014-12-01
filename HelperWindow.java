import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Created by Michael on 11/16/2014.
 */
//todo: click a button to save screenshots, and have those screenshots archived with names corresponding
// to customers
// enable an entire relaunch one the user updates(so only 1 window/instance of HelperWindow running).
public class HelperWindow {
    //keyPrice and keyToRef fluctuate based on personal preference.
    //"keys" and "Refined" or "Ref" are in-game currencies.
    public double keyPrice;
    public double keyToRef;
    public double budToKey;
    public double totalSum; // total sum of transaction

    //Personal user information.
    private static String repTft = "";
    private static String repSource = "";
    private static String repTrust = "";
    public static String email = "";

    //Constants for the window/gui
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 400;

    //References
    private JPanel panel;
    private JLabel keyPriceLabel;
    private JLabel keyToRefLabel;
    private JLabel budToKeyLabel;
    private JLabel idLabel;
    private JLabel amountLabel;
    private JTextField textFieldBudToKey;
    private JTextField textFieldKeysUSD;
    private JTextField textFieldKeysToRef;
    private JTextField textFieldID;
    private JTextField textFieldBudAmount;
    private JTextField textFieldKeyAmount;
    private JTextField textFieldRefAmount;
    private JButton buttonPrompt;
    private JButton buttonUpdate;
    private JButton buttonCalculate;
    private JButton buttonAffadavit;
    private JButton buttonRepSource;
    private JButton buttonRep;

    public HelperWindow() throws FileNotFoundException {
        JFrame window = new JFrame();
        window.setTitle("Steam Helper");
        window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // build panel
        buildPanel();
        // add panel to frame
        window.add(panel);

        window.setVisible(true);
    }

    public void copyToClipboard(String input) {
        StringSelection stringSelection = new StringSelection(input);
        Clipboard board = Toolkit.getDefaultToolkit().getSystemClipboard();
        board.setContents(stringSelection, null);

    }

    public void buildPanel() throws FileNotFoundException {
        loadPrices();
        buttonPrompt = new JButton("Prompt");
        buttonPrompt.addActionListener(new HelperWindowListener());
        budToKeyLabel = new JLabel("Bud price in keys: " + budToKey);
        keyPriceLabel = new JLabel("Key Price in USD: " + keyPrice);
        keyToRefLabel = new JLabel("Key Price in Refined: " + keyToRef);
        textFieldKeysUSD = new JTextField(4);
        textFieldKeysToRef = new JTextField(4);
        textFieldBudToKey = new JTextField(4);
        buttonUpdate = new JButton("Update Prices");
        buttonUpdate.addActionListener(new HelperWindowListener());

        idLabel = new JLabel("Enter steam 64id: ");
        textFieldID = new JTextField(20);
        amountLabel = new JLabel("Number of buds/keys/ref:");
        textFieldBudAmount = new JTextField(4);
        textFieldKeyAmount = new JTextField(4);
        textFieldRefAmount = new JTextField(4);
        buttonCalculate = new JButton("Calculate:");
        buttonCalculate.addActionListener(new HelperWindowListener());

        buttonAffadavit = new JButton("Affadavit");
        buttonAffadavit.addActionListener(new HelperWindowListener());

        buttonRepSource = new JButton("SourceOP Rep");
        buttonRepSource.addActionListener(new HelperWindowListener());
        buttonRep = new JButton("Normal Rep");
        buttonRep.addActionListener(new HelperWindowListener());

        panel = new JPanel();
        panel.add(buttonPrompt);
        panel.add(budToKeyLabel);
        panel.add(keyPriceLabel);
        panel.add(keyToRefLabel);
        panel.add(textFieldBudToKey);
        panel.add(textFieldKeysUSD);
        panel.add(textFieldKeysToRef);
        panel.add(buttonUpdate);
        panel.add(idLabel);
        panel.add(textFieldID);
        panel.add(amountLabel);
        panel.add(textFieldBudAmount);
        panel.add(textFieldKeyAmount);
        panel.add(textFieldRefAmount);
        panel.add(buttonCalculate);
        panel.add(buttonAffadavit);
        panel.add(buttonRepSource);
        panel.add(buttonRep);
    }

    // needs to refresh table with loaded prices, and fix the extraneous notification from updating price
    private class HelperWindowListener implements ActionListener {

        //parse the double in the textwindow if it exists, if not, set it to 0 automatically
        public double getTextFieldDouble(JTextField input) {
            if (!input.getText().isEmpty()) {
                return Double.parseDouble(input.getText());
            } else {
                return 0;
            }
        }

        public void actionPerformed(ActionEvent e) {
            String output;
            double budAmount = getTextFieldDouble(textFieldBudAmount);
            double keyAmount = getTextFieldDouble(textFieldKeyAmount);
            double refAmount = getTextFieldDouble(textFieldRefAmount);
            if (e.getSource() == buttonCalculate) {
                output = calculate(budAmount, keyAmount, refAmount) + " will be $" + totalSum
                        + " excluding fees, which are on you.  My account is US based.";;
            } else if (e.getSource() == buttonUpdate) {
                // if user inputs 0 or nothing, it doesn't update the price
                // assumed that none of the prices will be $0
                double bud = getTextFieldDouble(textFieldBudToKey);
                if (bud == 0) {
                    bud = budToKey;
                }
                double usd = getTextFieldDouble(textFieldKeysUSD);
                if (usd == 0) {
                    usd = keyPrice;
                }
                double ref = getTextFieldDouble(textFieldKeysToRef);
                if (ref == 0) {
                    ref = keyToRef;
                }
                output = "updated";
                try {
                    updatePrices(bud, usd, ref);
                    loadPrices();
                    new HelperWindow();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
            } else if (e.getSource() == buttonPrompt) {
                output = "Ok, where did you see my listing, how many keys do you want, "
                        + "do you have any rep threads, and from what e-mail will you be sending from?";
            } else if (e.getSource() == buttonAffadavit) {
                output = affadavit(textFieldID.getText(), budAmount, keyAmount, refAmount);
            } else {
                output = leaveRep(e.getSource() == buttonRepSource);
            }
            copyToClipboard(output);
            JOptionPane.showMessageDialog(null, output);
        }
    }

    //pre: settings file is THE settings file created by updatePrices
    //post: sets field variables to data saved in settings file.
    public void loadPrices() throws FileNotFoundException {
        File settings = new File("settings.txt");
        if (settings.exists()) {
            Scanner fileReader = new Scanner(settings);
            fileReader.next(); //scan past prefix/titles
            budToKey = fileReader.nextDouble();
            fileReader.next();
            keyPrice = fileReader.nextDouble();
            fileReader.next();
            keyToRef = fileReader.nextDouble();

        } else {
            updatePrices(0, 0, 0); //need to make this better
        }
    }

    //post; saves prices passed in to a file named settings.txt
    //ToDo update it from external pricing website
    public void updatePrices(double bud, double usd, double ref)  throws FileNotFoundException {
        PrintStream output = new PrintStream(new File("settings.txt"));
        output.println("budToKey= " + bud);
        output.println("keyPrice= " + usd);
        output.println("keyToRef= " + ref);
    }

    //ToDo: launch various websites and search for data?  Might need to use their apis
    // calculates total price and returns the string of the amount of each item being sold
    public String calculate(double budAmount, double keyAmount, double refAmount) {
        totalSum = round((budAmount * budToKey + keyAmount + (refAmount / keyToRef)) * keyPrice);
        int commaCount = 0;
        String bud = "";
        String key = "";
        String ref = "";
        if (budAmount != 0) {
            bud = budAmount + " TF2 earbuds"; //earbuds is the same for both singular/plural
            commaCount++;
        }
        if (keyAmount != 0) {
            key = keyAmount + " TF2 key";
            if (keyAmount > 1) {
                key = key + "s";
            }
            commaCount++;
        }
        if (refAmount != 0) {
            ref = refAmount + " TF2 refined metal";
            commaCount++;
        }
        String result = bud + key + ref;
        if (commaCount == 2) {
            if (!bud.isEmpty()) {
                result = bud + " and " + key + ref;
            }  else {
                result = key + " and " + ref;
            }
        } else if (commaCount == 3) { // commaCount == 3
            result = bud + ", " + key + ", and " + ref;
        }
        return result;
    }

    public String affadavit(String id, double budAmount, double keyAmount, double refAmount) {
        String result = calculate (budAmount, keyAmount, refAmount);
        return "Ok when ready, send $" + totalSum + " (excluding fees) to " + email + ", YOU pay any and all fees, and put this in the notes:\n"
                + "I, [Real Name], steam user: " + id + " am buying " + result + ", and understand that this is non-refundable and will not charge-back.";
    }

    public String leaveRep(boolean sourceOP) {
        if (sourceOP) {
            return "Thanks!  Feel free to leave rep if you want!\n" + repSource +" or " + repTrust;
        } else {
            return "Thanks!  Feel free to leave rep if you want!\n" + repTrust + " or " + repTft;
        }
    }

    //pre: num must be a positive double, a.k.a, the $ they have to pay
    //post: rounds to two decimal places
    public double round(double num) {
        double addFive = (num * 1000) + 5;
        int rounded = (int) (addFive / 10);
        return rounded / 100.0;
    }

    //repeatedly test the round method
    public void roundTester(Scanner input) {
        boolean done = false;
        while (!done) {
            System.out.print("Round this number to two decimal places: ");
            System.out.println("Result: " + round(Double.parseDouble(input.nextLine())));
            System.out.print("End?  Type \"y\" ");
            done = input.nextLine().toLowerCase().contains("y");
        }
    }


}