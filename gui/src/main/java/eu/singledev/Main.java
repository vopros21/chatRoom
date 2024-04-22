package eu.singledev;

import eu.singledev.utils.HintTextField;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Chat Room");
        Container pane = frame.getContentPane();

        // Create a new JButton Exit
        JButton button = getjButton(frame);

        // Create a new JTextArea
        JTextArea textArea = new JTextArea("Hello World!");
        textArea.setEditable(false);

        HintTextField input = new HintTextField("Enter text here...");
        input.addActionListener(e -> {
            // Update the JTextArea when Enter is pressed
            textArea.setText(input.getText());
        });

        // Create a new JButton Send
        JButton buttonSend = new JButton("Send");
        buttonSend.setBounds(100, 50, 100, 30);
        // Add an action listener to the button
        buttonSend.addActionListener(e -> {
            // Exit the application when the button is clicked
            textArea.setText(input.getText());
        });


        // Add the text field to the JFrame
        pane.add(textArea, BorderLayout.CENTER);
        Container inputButtonsLane = new Container();
        inputButtonsLane.setLayout(new BorderLayout());
        inputButtonsLane.add(input, BorderLayout.CENTER);
        // Add the button to the JFrame
        Container buttons = new Container();
        buttons.setLayout(new FlowLayout());
        buttons.add(buttonSend);
        buttons.add(button);
        inputButtonsLane.add(buttons, BorderLayout.LINE_END);
        pane.add(inputButtonsLane, BorderLayout.PAGE_END);

        // Create a menu bar
        JMenuBar menuBar = getMenuBar(frame);

        // Set the menu bar to the frame
        frame.setJMenuBar(menuBar);

        // Set the size of the JFrame
        frame.setMinimumSize(new Dimension(500, 300));

        // Center the JFrame on the screen
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Show the JFrame
        frame.setVisible(true);
    }

    private static JButton getjButton(JFrame frame) {
        JButton button = new JButton("Exit");
        button.setBounds(100, 50, 100, 30);
        // Add an action listener to the button
        button.addActionListener(e -> {
            // Exit the application when the button is clicked
            int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Already leaving?!", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        return button;
    }

    private static JMenuBar getMenuBar(JFrame frame) {
        JMenuBar menuBar = new JMenuBar();
        // Create a menu Settings
        JMenu menuSettings = new JMenu("Settings");
        JMenuItem miSettings = new JMenuItem("Settings");
        miSettings.addActionListener(e -> {
            // Show a dialog when the menu item is clicked
            JOptionPane.showMessageDialog(frame, "Settings");
        });

        JMenuItem miExit = new JMenuItem("Exit");
        miExit.addActionListener(e -> {
            // Show exit confirmation dialog when the menu item is clicked
            int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Already leaving?!", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        // Add menu items to menu
        menuSettings.add(miSettings);
        menuSettings.addSeparator();
        menuSettings.add(miExit);

        // Create a menu Help
        JMenu menuHelp = new JMenu("Help");
        JMenuItem miAbout = new JMenuItem("About");
        miAbout.addActionListener(e -> {
            // Show a dialog when the menu item is clicked
            JOptionPane.showMessageDialog(frame, "About");
        });
        menuHelp.add(miAbout);

        // Add menu to menu bar
        menuBar.add(menuSettings);
        menuBar.add(menuHelp);
        return menuBar;
    }
}