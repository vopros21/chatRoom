package com.company;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class ServerApplication {
    private static final Logger log = LoggerFactory.getLogger(ServerApplication.class);
    private ServerSocket serverSocket;
    private boolean stop;
    private final Set<ClientHandler> clients = new HashSet<>();
    /**
     * -- GETTER --
     * return userNames
     */
    @Getter
    private final Set<String> userNames = new HashSet<>();

    public static void main(String[] args) {
        ServerApplication server = new ServerApplication();
        server.start(5555);
    }

    public void start(int port) {
        log.info("Starting app with opened port: " + port);
        try {
            serverSocket = new ServerSocket(port);
            while (!stop) {
                ClientHandler client = new ClientHandler(serverSocket.accept(), this);
                clients.add(client);
                client.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void broadcast(String message, ClientHandler excludeClient) {
        for (ClientHandler current : clients) {
            current.sendMessage(message);
//            if (current != excludeClient) {
//                current.sendMessage(message);
//            }
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * add userName to the list of online users
     */
    public void addUserName(String userName) {
        userNames.add(userName);
    }

    /**
     * return true if server already has connected users
     * false otherwise
     */
    public boolean hasUsers() {
        return !this.userNames.isEmpty();
    }
}