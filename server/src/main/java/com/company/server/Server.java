package com.company.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 * Main class for running the server
 */
@Component
@RequiredArgsConstructor
public class Server {
    private static final Logger log = LoggerFactory.getLogger(Server.class);
    // stop the server
    public boolean stop;
    @Getter
    private int port = 3000;
    // stores user threads
    private final Set<UserThread> userThreads = new HashSet<>();
    @Getter
    // stores user objects
    private Set<ChatUser> connectedUsers = new HashSet<>();

    /**
     * Parametrized Constructor
     *
     * @param port: port number to run the server on
     */
//    public Server(int port) {
//        this.port = port;
//        this.stop = false;
//    }

    /**
     * Main Method
     *
     * @param args: command line arguments
     */
//    public static void main(String[] args) {
//        Server server = new Server();
//        log.info("Connected to port -> {}", server.getPort());
//        server.init();
//    }

    /**
     * Initializing the server
     */
    public void init() {
        // ServerSocket class is auto closable
        try (ServerSocket server = new ServerSocket(port)) {
            while (!stop) {
                // waits until a client is connected to the server
                Socket clientSocket = server.accept();
                // separate thread for each client
                UserThread newUser = new UserThread(clientSocket, this);
                // add user to the list
                userThreads.add(newUser);
                log.info("Client Connected. Client Info => socket: {}:{}, name: {}",
                        clientSocket.getInetAddress(),
                        clientSocket.getPort(),
                        newUser.getUser().getName());
                // start the thread
                newUser.start();
            }

        } catch (IOException e) {
            log.error("Port already in use");
            log.error(e.toString());
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

    void broadcast(UserMessage message, UserThread excludeUser) {
        for (UserThread aUser : userThreads) {
            if (aUser != excludeUser) {
                String userName = message.getUser().getName();
                aUser.sendMessage("["
                        + userName
                        + "]: " + message.getMessage());
            }
        }
    }

    /**
     * Add username
     */
    void addUser(ChatUser user) {
        for (ChatUser chatUser : connectedUsers) {
            if (chatUser.getName().equals(user.getName())) {
                //TODO: send message to the user that the name is already taken
                return;
            }
        }
        connectedUsers.add(user);
    }

    /**
     * Remove user
     **/
    void removeUser(ChatUser user, UserThread aUser) {
        boolean removed = connectedUsers.remove(user);
        if (removed) {
            userThreads.remove(aUser);
            log.info("Service message: User '{}' has quit.", user.getName());
            stopIfNoActiveConnections();
        }
    }

    private void stopIfNoActiveConnections() {
        if (connectedUsers.isEmpty() && userThreads.isEmpty()) {
            stop = true;
        }
    }

    /**
     * True if the userNames is not empty
     */
    boolean hasUsers() {
        return !this.connectedUsers.isEmpty();
    }
}