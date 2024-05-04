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
        textField.addActionListener(e -> textField.setText(""));
        lowerPane.add(textField, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        sendButton = new JButton("Send");
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
//            this.client = new Client("localhost", 3000, this::onMessageReceived);
            this.client = new ChatClient(hostname, serverPort, this::onMessageReceived);
            client.startClient();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
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