package com.company;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author Mike Kostenko on 23/03/2024
 */
class ClientHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(ClientHandler.class);
    private final Socket clientSocket;
    private final ServerApplication server;
    @Getter
    private final String userName;
    private PrintWriter writer;

    public ClientHandler(Socket socket, ServerApplication server) {
        log.info("Creating ClientHandler: " + this);
        clientSocket = socket;
        this.server = server;
        this.userName = "Anonymous";
    }

    public void run() {
        try (DataInputStream in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()))) {
            writer = new PrintWriter(clientSocket.getOutputStream(), true);

            printUsers();

            // add username to the list of online users
            server.addUserName(userName);

            char dataType = in.readChar();
            while (dataType != 0) {
                if (dataType == 's') {
                    String userMsg = readString(in);
                    if (".".equals(userMsg)) {
                        writer.println("Tchao!");
                        break;
                    }
                    server.broadcast(userMsg, this);
                }
                try {
                    dataType = in.readChar();
                } catch (Exception e) {
                    dataType = 0;
                }
            }
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String readString(DataInputStream in) throws IOException {
        int length = in.readInt();
        byte[] messageByte = new byte[length];
        boolean end = false;
        StringBuilder dataString = new StringBuilder(length);
        int totalBytesRead = 0;
        while (!end) {
            int currentsBytesRead = in.read(messageByte);
            totalBytesRead += currentsBytesRead;
            if (totalBytesRead <= length) {
                dataString.
                        append(new String(messageByte, 0, currentsBytesRead, StandardCharsets.UTF_8));
            } else {
                dataString.
                        append(new String(messageByte, 0,
                                length - totalBytesRead + currentsBytesRead, StandardCharsets.UTF_8));
            }
            if (dataString.length() >= length) {
                end = true;
            }
        }
        return dataString.toString();
    }

    void sendMessage(String msg) {
        writer.println(msg);
    }

    void printUsers() {
        if (server.hasUsers()) {
            writer.println("No other users connected");
        } else {
            writer.println("Connected users: " + server.getUserNames());
        }
    }
}
