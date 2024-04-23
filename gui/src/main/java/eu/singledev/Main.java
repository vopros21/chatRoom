package eu.singledev;

import eu.singledev.utils.HintTextField;
import eu.singledev.utils.InputValidator;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Main {
    private static JTextField inputHostname;
    private static JTextField inputPort;

    public static String getInputHostname() {
        return inputHostname.getText();
    }

    public static int getInputPort() {
        return Integer.parseInt(inputPort.getText());
    }

    public static void main(String[] args) {
        Runnable runner = () -> {

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
        };
        EventQueue.invokeLater(runner);
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
        JMenuItem miSettings = getMISettings(frame);

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
        menuBar.add(new JSeparator());
        menuBar.add(new JLabel(String.format("Users online: [%d]", 0)));
        return menuBar;
    }

    private static JMenuItem getMISettings(JFrame frame) {
        JMenuItem miSettings = new JMenuItem("Settings");

        miSettings.addActionListener(e -> {
            // Show a dialog when the menu item is clicked
            // Create a custom dialog
            JDialog settingsDialog = new JDialog(frame, "Settings", true);
            settingsDialog.setLayout(new GridLayout(3, 2));

            // Create input fields
            JLabel labelHostname = new JLabel("Connect to:");
            inputHostname = new JTextField();
            JLabel labelPort = new JLabel("Port:");
            inputPort = new JTextField();
            inputPort.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    char c = e.getKeyChar();
                    if (!Character.isDigit(c) || inputPort.getText().length() >= 4) {
                        e.consume();  // Ignore non-digit characters
                    }
                }
            });

            // Add input fields to dialog
            settingsDialog.add(labelHostname);
            settingsDialog.add(inputHostname);
            settingsDialog.add(labelPort);
            settingsDialog.add(inputPort);

            // Add a button to revert changes and close the dialog
            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(event -> settingsDialog.dispose());
            closeButton.setEnabled(false);
            inputHostname.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    updateButton();
                }

                public void removeUpdate(DocumentEvent e) {
                    updateButton();
                }

                public void insertUpdate(DocumentEvent e) {
                    updateButton();
                }

                public void updateButton() {
                    // Update the state of the button based on the text in the text field
                    closeButton.setEnabled(InputValidator.isHostnameValid(getInputHostname()));
                }
            });


            settingsDialog.add(closeButton);
            settingsDialog.setLocationRelativeTo(frame);

            // Set the size of the dialog and make it visible
            settingsDialog.pack();
            settingsDialog.setVisible(true);
        });
        return miSettings;
    }
}