package com.company;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * For making separate thread for each user
 *
 * @author Mike Kostenko on 31/03/2024
 */
public class UserThread extends Thread {
    private static final Logger log = LoggerFactory.getLogger(Server.class);
    // User Socket
    private final Socket socket;
    // Main Server
    private final Server server;
    private PrintWriter writer;
    private String userName;

    /**
     * Parametrized Constructor
     *
     * @param socket: User Socket
     * @param server: Main Server
     */
    public UserThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        this.userName = "Anonymous";
    }

    /**
     * Overriding run() method of Thread Class
     */
    @Override
    public void run() {
        try (
                // autocloseable
                // used to read from socket's InputStream, data is fed using WriteThread.java
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            // write to socket's output stream
            this.writer = new PrintWriter(socket.getOutputStream(), true);

            // prints connected users
            printUsers();

            // First line given by client is username(See WriteThread.java)
            this.userName = reader.readLine();
            server.addUserName(userName);

            // message send by server to all user, except current one
            String serverMessage = "New user connected: " + userName;
            log.info(serverMessage);
            server.broadcast(serverMessage, this);

            // client messages
            String clientMessage;

            // read messages send by current client
            while (socket.isConnected() && !socket.isClosed()) {
                clientMessage = reader.readLine();
                serverMessage = "["
                        + UserNamePainter.getUserColor(userName)
                        + userName
                        + UserNamePainter.getPOSTFIX()
                        + "]: " + clientMessage;
                server.broadcast(serverMessage, this);
                log.info("Broadcast message: {} -> {}", userName, clientMessage);
            }

        } catch (IOException ex) {
            log.error("Error in UserThread: {}", ex.getMessage());
            log.error(ex.toString());
        } finally {
            server.removeUser(userName, this);

            try {
                socket.close();
            } catch (IOException e) {
                log.error("Error in closing socket: {}", e.getMessage());
            }
            String serverMessage = userName + " has quit.";
            log.info(serverMessage);
            server.broadcast(serverMessage, this);
        }
    }

    /**
     * Sends a list of online users to the newly connected user.
     */
    void printUsers() {
        if (server.hasUsers()) {
            writer.println("Connected users: " + server.getUserNames());
        } else {
            writer.println("No other users connected");
        }
    }

    /**
     * Sends a message to the client. Used in Server class's broadcasting method
     */
    void sendMessage(String message) {
        writer.println(message);
    }
}