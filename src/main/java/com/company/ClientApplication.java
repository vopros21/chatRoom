package com.company;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author Mike Kostenko on 05/03/2024
 */
public class ClientApplication {
    private Socket clientSocket;
    private DataOutputStream out;
    private BufferedReader in;

    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            System.out.println("Exception in server: " + e);
        }
    }

    public String sendStringMessage(String msg) {
        try {
            char type = 's';
            int length = msg.length();
            byte[] dataInBytes = msg.getBytes(StandardCharsets.UTF_8);
            out.writeChar(type);
            out.writeInt(length);
            out.write(dataInBytes);
            return in.readLine();
        } catch (IOException e) {
            System.out.println("Send message exception: " + e);
            return e.getLocalizedMessage();
        }
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public static void main(String[] args) {
        ClientApplication client = new ClientApplication();
        client.startConnection("localhost", 5555);
        Console console = System.console();
        while (true) {
            String msg = console.readLine();
            String answer = client.sendStringMessage(msg);
            System.out.println("Server: " + answer);
            if ("Tchao!".equals(answer)) {
                break;
            }
        }
    }
}
