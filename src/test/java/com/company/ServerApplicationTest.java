package com.company;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mike Kostenko on 01/03/2024
 */
public class ServerApplicationTest {

    @Test
    public void givenClient1_whenServerResponds_thenCorrect() {
        ClientApplication client1 = new ClientApplication();
        client1.startConnection("127.0.0.1", 5555);
        String msg1 = client1.sendStringMessage("hello");
        String msg2 = client1.sendStringMessage("world");
        String terminate = client1.sendStringMessage(".");

        assertEquals("hello", msg1);
        assertEquals("world", msg2);
        assertEquals("Tchao!", terminate);
    }

    @Test
    public void givenClient2_whenServerResponds_thenCorrect() {
        ClientApplication client2 = new ClientApplication();
        client2.startConnection("127.0.0.1", 5555);
        String msg1 = client2.sendStringMessage("hello");
        String msg2 = client2.sendStringMessage("world");
        String terminate = client2.sendStringMessage(".");

        assertEquals("hello", msg1);
        assertEquals("world", msg2);
        assertEquals("Tchao!", terminate);
    }

    @Test
    public void serverUnderstandTLVFormatAndAnswer() {
        ClientApplication client = new ClientApplication();
        client.startConnection("127.0.0.1", 5555);
        String msg1 = client.sendStringMessage("Hello World");
        assertEquals("Hello World", msg1);
    }
}
