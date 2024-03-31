package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Seperate thread for reading and displaying the message
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
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!socket.isInputShutdown()) {
            try {
                String response = reader.readLine();
                System.out.println("\n" + response);

                // prints the username after displaying the server's message
                if (client.getUserName() != null) {
                    System.out.print("->(" + client.getUserName() + "): ");
                }
            } catch (IOException ex) {
                System.out.println("Error in reading" + ex.getMessage());
                ex.printStackTrace();
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