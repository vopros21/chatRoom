package eu.singledev;

import eu.singledev.utils.HintTextField;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("My Window");
        Container pane = frame.getContentPane();

        // Create a new JButton Exit
        JButton button = new JButton("Exit");
        button.setBounds(100, 50, 100, 30);
        // Add an action listener to the button
        button.addActionListener(e -> {
            // Exit the application when the button is clicked
            System.exit(0);
        });

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

        // Set the size of the JFrame
        frame.setMinimumSize(new Dimension(500, 300));

        // Center the JFrame on the screen
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Show the JFrame
        frame.setVisible(true);
    }
}