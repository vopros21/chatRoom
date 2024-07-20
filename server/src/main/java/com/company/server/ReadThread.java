package com.company.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Separate thread for reading and displaying the message
 */
public class ReadThread extends Thread {
    private final Socket socket;
    private final Client client;
    private BufferedReader reader;

    public ReadThread(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;

        try {
            InputStream input = this.socket.getInputStream();
            this.reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
        }
    }

    @Override
    public void run() {
        while (!socket.isInputShutdown()) {
            try {
                String response = reader.readLine();
                if (response.startsWith("[")) {
                    String userName = response.split(":")[0].substring(1, response.split(":")[0].length() - 1);
                    String coloredUserName = UserNamePainter.getUserColor(userName) + userName + UserNamePainter.getPOSTFIX();
                    response = response.replaceFirst(userName, coloredUserName);
                }
                System.out.println("\n" + response);

                // prints the username after displaying the server's message
                if (client.getClientName() != null) {
                    System.out.print("->(" + client.getClientName() + "): ");
                }
            } catch (IOException ex) {
                System.err.println("Error in reading" + ex.getMessage());
                break;
            }
        }

        try {
            socket.close();
            reader.close();
        } catch (IOException ex) {

            System.out.println("Error writing to server: " + ex.getMessage());
        }
    }
}