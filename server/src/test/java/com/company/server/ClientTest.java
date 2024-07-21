package com.company.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mike Kostenko on 01/03/2024
 */
public class ClientTest {

    @Test
    public void testUserCanBeAddedToClient() {
        Client client = new Client("localhost", 3000);
        client.setChatUser(new ChatUser(null, "Mike", null));
        assertEquals("Mike", client.getClientName());
    }
}
