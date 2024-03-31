package com.company;

import lombok.Getter;
import lombok.Setter;

import java.io.Console;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Main Class for Client Side
 */
public class Client {
    // ex-> localhost
    private final String hostname;
    private final int port;
    @Setter
    @Getter
    private String userName;

    /**
     * Default Constructor
     */
    public Client() {
        this.hostname = "localhost";
        this.port = 3000;
    }

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
        int port = Integer.parseInt(console.readLine("\nEnter port:"));

        Client client = new Client(hostname, port);

        client.init();
    }

    /**
     * Initialize the client socket
     */
    public void init() {
        try {
            // gives the IP address of the host
            InetAddress address = InetAddress.getByName(hostname);
            // socket constructor of type (InetAdress, portnumber)
            Socket socket = new Socket(address, port);

            System.out.println("Connected to the chat server");

            // separate threads for reading and writing msgs
            new ReadThread(socket, this).start();
            new WriteThread(socket, this).start();

        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }

    }
}
