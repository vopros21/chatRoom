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
        String msg1 = client1.sendMessage("hello");
        String msg2 = client1.sendMessage("world");
        String terminate = client1.sendMessage(".");

        assertEquals(msg1, "hello");
        assertEquals(msg2, "world");
        assertEquals(terminate, "Tchao!");
    }

    @Test
    public void givenClient2_whenServerResponds_thenCorrect() {
        ClientApplication client2 = new ClientApplication();
        client2.startConnection("127.0.0.1", 5555);
        String msg1 = client2.sendMessage("hello");
        String msg2 = client2.sendMessage("world");
        String terminate = client2.sendMessage(".");

        assertEquals(msg1, "hello");
        assertEquals(msg2, "world");
        assertEquals(terminate, "Tchao!");
    }
}
