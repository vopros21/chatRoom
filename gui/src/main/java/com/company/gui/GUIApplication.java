package com.company.gui;

import com.company.gui.utils.HintTextField;

import javax.swing.*;
import java.awt.*;

public class GUIApplication extends JFrame {
    private JTextArea messageArea;
    private JTextField textField;
    private JButton sendButton;
    private JButton exitButton;
    private JLabel connectedTo = new JLabel();
    private ChatClient client;
    private int serverPort = 3000;
    private String hostname = "localhost";

    public GUIApplication() {
        super("chatRoom Application");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        connectedTo.setText("Connected to: %s:%d".formatted(hostname, serverPort));

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(new JSeparator());
        menuBar.add(connectedTo);
        setJMenuBar(menuBar);
        menuBar.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, new Color(0, 0, 0, 0)));

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        add(new JScrollPane(messageArea), BorderLayout.CENTER);

        JPanel lowerPane = new JPanel();
        lowerPane.setLayout(new BorderLayout());
        lowerPane.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, new Color(0, 0, 0, 0)));

        textField = new HintTextField("Type the message here...");
        textField.addActionListener(e -> sendTextMessage());
        lowerPane.add(textField, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        sendButton = new JButton("Send");
        sendButton.addActionListener(e -> sendTextMessage());
        buttonPanel.add(sendButton, BorderLayout.WEST);

        exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> {
            String departureMessage = "User has left the chat";
            client.sendMessage(departureMessage);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
            }
            System.exit(0);
        });
        buttonPanel.add(exitButton, BorderLayout.EAST);

        lowerPane.add(buttonPanel, BorderLayout.EAST);
        add(lowerPane, BorderLayout.SOUTH);

        try {
            this.client = new ChatClient(hostname, serverPort, this::onMessageReceived);
            client.startClient();
        } catch (Exception e) {
            e.printStackTrace();
            // Show input dialog to get hostname
            String inputHostname = JOptionPane.showInputDialog(this, "Enter hostname:", "Connection Failed", JOptionPane.QUESTION_MESSAGE);
            // Show input dialog to get port
            String inputPort = JOptionPane.showInputDialog(this, "Enter port:", "Connection Failed", JOptionPane.QUESTION_MESSAGE);
            if (inputHostname != null && !inputHostname.isEmpty() && inputPort != null && !inputPort.isEmpty()) {
                try {
                    // Parse the port number
                    int port = Integer.parseInt(inputPort);
                    // Try to connect with the new hostname and port
                    this.client = new ChatClient(inputHostname, port, this::onMessageReceived);
                    client.startClient();
                    // Update the connectedTo label
                    connectedTo.setText("Connected to: %s:%d".formatted(inputHostname, port));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error: Invalid hostname or port", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }
        String username = JOptionPane.showInputDialog(this, "Enter your username:", "Username", JOptionPane.QUESTION_MESSAGE);
        client.sendMessage(username);
    }

    private void sendTextMessage() {
        String message = textField.getText();
        client.sendMessage(message);
        textField.setText("");
        messageArea.append("You: " + message + "\n");
    }

    private void onMessageReceived(String message) {
        SwingUtilities.invokeLater(() -> messageArea.append(message + "\n"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUIApplication guiApplication = new GUIApplication();
            guiApplication.setVisible(true);
        });
    }
}