package com.company;

import lombok.Getter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Main class for running the server
 */
public class Server {
    // stop the server
    public boolean stop;
    /**
     * -- GETTER --
     * get the port number
     */
    @Getter
    private int port;
    // stores usernames
    @Getter
    private Set<String> userNames = new HashSet<>();
    // stores user objects
    private Set<UserThread> userThreads = new HashSet<>();

    /**
     * Default constructor
     */
    public Server() {
        this.port = 3000;
        this.stop = false;
    }

    /**
     * Parametrized Constructor
     *
     * @param port: port number to run the server on
     */
    public Server(int port) {
        this.port = port;
        this.stop = false;
    }

    /**
     * Main Method
     *
     * @param args: command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Enter Port Number:");
        Scanner sc = new Scanner(System.in);
        int port = sc.nextInt();
        Server server = new Server(port);
        System.out.println("Connected to port->" + server.getPort());

        server.init();
        sc.close();
    }

    /**
     * Initializing the server
     */
    public void init() {
        // ServerSocket class is auto closable
        try (ServerSocket server = new ServerSocket(port)) {

            while (!stop) {
                // waits until a client is connected to the server
                Socket clientSocket = server.accept();
                System.out.println("Client Connected");
                System.out.println("Client Info=>");
                // the remote IP address to which this socket is connected
                System.out.println(clientSocket.getInetAddress());
                System.out.println("-----------------------");

                // sperate thread for each client
                UserThread newUser = new UserThread(clientSocket, this);
                // add user to the list
                userThreads.add(newUser);
                // start the thread
                newUser.start();
            }

        } catch (IOException e) {
            System.out.println("Port already in use");

            System.out.println(e);
        }
    }

    /**
     * Delivers message from one user to all other user(s)
     */
    void broadcast(String message, UserThread excludeUser) {
        for (UserThread aUser : userThreads) {
            if (aUser != excludeUser) {
                aUser.sendMessage(message);
            }
        }
    }

    /**
     * Add username
     */
    void addUserName(String userName) {
        userNames.add(userName);
    }

    /**
     * Remove user
     **/
    void removeUser(String userName, UserThread aUser) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            userThreads.remove(aUser);
            System.out.println("The user " + userName + " quit");
        }
    }

    /**
     * True if the userNames is not empty
     */
    boolean hasUsers() {
        return !this.userNames.isEmpty();
    }
}