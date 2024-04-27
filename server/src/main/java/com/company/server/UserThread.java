package com.company.server;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    @Getter
    private final ChatUser user;

    /**
     * Parametrized Constructor
     *
     * @param socket: User Socket
     * @param server: Main Server
     */
    public UserThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        this.user = new ChatUser(UUID.randomUUID(), "Anonymous", new Date());
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
            this.user.setName(reader.readLine());
            server.addUser(user);

            // message send by server to all user, except current one
            String serverMessage = "New user connected: " + user.getName();
            log.info(serverMessage);
            server.broadcast(serverMessage, this);

            // client messages

            // read messages send by current client
            while (socket.isConnected() && !socket.isClosed()) {
                UserMessage clientMessage = new UserMessage(UUID.randomUUID(), reader.readLine(), user, new Date());
                if (clientMessage.getMessage() == null) {
                    break;
                }
                server.broadcast(clientMessage, this);
                log.info("Broadcast message: {} -> {}", user.getName(), clientMessage.getMessage());
            }

        } catch (IOException ex) {
            log.error("Error in UserThread: {}", ex.getMessage());
            log.error(ex.toString());
        } finally {
            server.removeUser(user, this);

            try {
                socket.close();
            } catch (IOException e) {
                log.error("Error in closing socket: {}", e.getMessage());
            }
            String serverMessage = user.getName() + " has quit.";
            server.broadcast(serverMessage, this);
        }
    }

    /**
     * Sends a list of online users to the newly connected user.
     */
    void printUsers() {
        if (server.hasUsers()) {
            writer.println("Connected users: " + getConnectedUsers());
        } else {
            writer.println("No other users connected");
        }
    }

    private List<String> getConnectedUsers() {
        return server.getConnectedUsers().stream().map(ChatUser::getName).collect(Collectors.toList());
    }

    /**
     * Sends a message to the client. Used in Server class's broadcasting method
     */
    void sendMessage(String message) {
        writer.println(message);
    }
}
