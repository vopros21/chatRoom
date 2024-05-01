package com.company.gui;

import com.company.gui.utils.HintTextField;
import com.company.server.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIApplication extends JFrame {
    private JTextArea messageArea;
    private JTextField textField;
    private Client client;

    public GUIApplication() {
        super("chatRoom Application");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        add(new JScrollPane(messageArea), BorderLayout.CENTER);

        textField = new HintTextField("Type the message here...");
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField.setText("");
            }
        });
        add(textField, BorderLayout.SOUTH);

        try {
            client = new Client("localhost", 3000);
            client.init();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUIApplication guiApplication = new GUIApplication();
            guiApplication.setVisible(true);
        });
    }
}