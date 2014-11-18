import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by Michael on 11/16/2014.
 */
//todo: click a button to save screenshots, and have those screenshots archived with names corresponding
    // to customers
    // enable an entire relaunch one the user updates(so only 1 window/instance of HelperWindow running).
public class HelperWindow {
    //Constants for the window/gui
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 400;

    //References
    private JPanel panel;
    private JLabel prompt;
    private JLabel keyPrice;
    private JLabel keyToRef;
    private JLabel idLabel;
    private JLabel keyLabel;
    private JTextField textFieldKeysUSD;
    private JTextField textFieldKeysRef;
    private JTextField textFieldID;
    private JTextField textFieldKeys;
    private JButton buttonUpdate;
    private JButton buttonPrice;
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
        String initPrompt = "Ok, where did you see my listing, how many keys do you want, "
                + "do you have any rep threads, and from what e-mail will you be sending from?";
        prompt = new JLabel(initPrompt);
        copyToClipboard(initPrompt);
        HelperMain.loadPrices();
        keyPrice = new JLabel("Key Price in USD: " + HelperMain.keyPrice);
        keyToRef = new JLabel("Key Price in Refined: " + HelperMain.keyToRef);
        textFieldKeysUSD = new JTextField(3);
        textFieldKeysRef = new JTextField(4);
        buttonUpdate = new JButton("Update Prices");
        buttonUpdate.addActionListener(new HelperWindowListener());

        idLabel = new JLabel("Enter steam 64id: ");
        textFieldID = new JTextField(20);
        keyLabel = new JLabel("Number of keys:");
        textFieldKeys = new JTextField(20);
        buttonPrice = new JButton("Calculate:");
        buttonPrice.addActionListener(new HelperWindowListener());

        buttonRepSource = new JButton("SourceOP Rep");
        buttonRepSource.addActionListener(new HelperWindowListener());
        buttonRep = new JButton("Normal Rep");
        buttonRep.addActionListener(new HelperWindowListener());

        panel = new JPanel();
        panel.add(prompt);
        panel.add(keyPrice);
        panel.add(keyToRef);
        panel.add(textFieldKeysUSD);
        panel.add(textFieldKeysRef);
        panel.add(buttonUpdate);
        panel.add(idLabel);
        panel.add(textFieldID);
        panel.add(keyLabel);
        panel.add(textFieldKeys);
        panel.add(buttonPrice);
        panel.add(buttonRepSource);
        panel.add(buttonRep);
    }

    // needs to refresh table with loaded prices
    private class HelperWindowListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == buttonPrice) {
                String id = textFieldID.getText();
                double keyAmount = Double.parseDouble(textFieldKeys.getText());
                String output = HelperMain.question(id, keyAmount);
                copyToClipboard(output);
                JOptionPane.showMessageDialog(null, output);
            } else if (e.getSource() == buttonUpdate) {
                double usd = Double.parseDouble(textFieldKeysUSD.getText());
                double ref = Double.parseDouble(textFieldKeysRef.getText());
                String output = "updated";
                try {
                    HelperMain.updatePrices(usd, ref);
                    HelperMain.loadPrices();
                    JOptionPane.showMessageDialog(null, output);
                    new HelperWindow();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
            } else if (e.getSource() == buttonRepSource) {
                String output = HelperMain.leaveRep(true);
                copyToClipboard(output);
                JOptionPane.showMessageDialog(null, output);
            } else {//if (e.getSource() == buttonRep) {
                String output = HelperMain.leaveRep(false);
                copyToClipboard(output);
                JOptionPane.showMessageDialog(null, output);
            }
        }
    }

}
