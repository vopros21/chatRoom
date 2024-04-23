package com.company;

import java.io.Console;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.UUID;

/**
 * Separate thread for writing the message
 */
public class WriteThread extends Thread {
    private final Socket socket;
    private final Client client;
    private PrintWriter writer;

    public WriteThread(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;

        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
        }
    }

    @Override
    public void run() {
        Console console = System.console();

        // First Input is Name
        String userName = console.readLine("\nEnter your name: ");
        client.setChatUser(new ChatUser(UUID.randomUUID(), userName, new Date()));
        writer.println(client.getChatUser().getName());

        String text;
        while (!socket.isOutputShutdown()) {
            // Send message to socket's output stream
            text = console.readLine(":");
            writer.println(text);
        }

        try {
            socket.close();
            writer.close();
        } catch (IOException ex) {
            System.out.println("Error writing to server: " + ex.getMessage());
        }
    }
}