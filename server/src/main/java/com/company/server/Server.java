package com.company.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.temporal.ChronoField;
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

    private static Connection conn;

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
    public static void main(String[] args) {
        Server server = new Server();
        log.info("Connected to port -> {}", server.getPort());
        server.init();
    }

    /**
     * Initializing the server
     */
    public void init() {
        // ServerSocket class is auto closable
        try (ServerSocket server = new ServerSocket(port)) {
            String dbUrl = System.getenv("dbUrl");
            conn = DriverManager.getConnection(dbUrl);
            log.info("Connected to the database: {}", conn.getMetaData().getURL());
            createTablesIfNotExist();
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
        } catch (SQLException e) {
            log.error("Error in connecting to the database");
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
            } else {
                logMessage(message);
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

    private void createTablesIfNotExist() {
        String messagesTableSql = """
                CREATE TABLE IF NOT EXISTS messages (
                id text PRIMARY KEY,
                message text NOT NULL,
                userId text NOT NULL,
                time integer NOT NULL
                );""";
        String usersTableSql = """
                CREATE TABLE IF NOT EXISTS users (
                id text PRIMARY KEY,
                name text NOT NULL,
                lastVisited integer NOT NULL
                );""";
        try (var stmt = conn.createStatement()) {
            stmt.execute(messagesTableSql);
            stmt.execute(usersTableSql);
        } catch (SQLException e) {
            log.error("Error in creating the table");
            log.error(e.toString());
        }
    }

    public void logMessage(UserMessage message) {
        String sql = "INSERT INTO messages (id, message, userId, time) VALUES (?, ?, ?, ?)";
        try (var pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, message.getId().toString());
            pstmt.setString(2, message.getMessage());
            pstmt.setString(3, message.getUser().getId().toString());
            pstmt.setLong(4, message.getTime().getLong(ChronoField.INSTANT_SECONDS));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error in logging the message");
            log.error(e.toString());
        }
    }

    public void saveUser(ChatUser user) {
        String sql = "INSERT INTO users (id, name, lastVisited) VALUES (?, ?, ?)";
        try (var pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getId().toString());
            pstmt.setString(2, user.getName());
            pstmt.setLong(3, user.getLastVisit().getLong(ChronoField.INSTANT_SECONDS));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error in saving the user");
            log.error(e.toString());
        }
    }
}