package com.company.server;

import lombok.Getter;
import lombok.Setter;

import java.io.Console;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Main Class for Client Side
 */
@Getter
public class Client {
    // ex-> localhost
    private final String hostname;
    private final int port;
    @Setter
    private ChatUser chatUser;
    private ReadThread readThread;
    private WriteThread writeThread;

    /**
     * Parametrized Constructor
     *
     * @param hostname: hostname of the server
     * @param port:     port server connected to
     */
    public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public static void main(String[] args) {

        Console console = System.console();

        String hostname = console.readLine("\nEnter hostname:");
        if (hostname.isEmpty()) {
            hostname = "localhost";
        }
        int port;
        try {
            port = Integer.parseInt(console.readLine("\nEnter port:"));
        } catch (NumberFormatException e) {
            port = 3000;
        }

        Client client = new Client(hostname, port);
        client.init();
    }

    public String getClientName() {
        try {
            return chatUser.getName();
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Initialize the client socket
     */
    public void init() {
        try {
            // socket constructor of type (hostname, port number)
            Socket socket = new Socket(hostname, port);

            System.out.println("Connected to the chat server");

            // separate threads for reading and writing messages
            readThread = new ReadThread(socket, this);
            writeThread = new WriteThread(socket, this);
            readThread.start();
            writeThread.start();

        } catch (UnknownHostException ex) {
            System.err.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("I/O Error: " + ex.getMessage());
        }
    }
}
