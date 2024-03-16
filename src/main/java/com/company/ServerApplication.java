package com.company;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServerApplication {
    private ServerSocket serverSocket;

    public static void main(String[] args) {
        ServerApplication server = new ServerApplication();
        server.start(5555);
    }

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
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

        public ClientHandler(Socket socket) {
            clientSocket = socket;
        }

        public void run() {
            try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 DataInputStream in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()))) {
                char dataType = in.readChar();
                while (dataType != 0) {
                    if (dataType == 's') {
                        String input = readString(in);
                        if (".".equals(input)) {
                            out.println("Tchao!");
                            break;
                        }
                        out.println(input);
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
    }
}