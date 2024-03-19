package com.company;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class ServerApplication {
    private static final Logger log = LoggerFactory.getLogger(ServerApplication.class);
    private ServerSocket serverSocket;
    private boolean stop;
    private final Set<ClientHandler> clients = new HashSet<>();

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

    private static class ClientHandler extends Thread {
        private final Socket clientSocket;
        private PrintWriter writer;
        private final ServerApplication server;

        public ClientHandler(Socket socket, ServerApplication server) {
            log.info("Creating ClientHandler: " + this);
            clientSocket = socket;
            this.server = server;
        }

        public void run() {
            try (DataInputStream in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()))) {
                writer = new PrintWriter(clientSocket.getOutputStream(), true);
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
    }
}