package com.company;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mike Kostenko on 25/04/2024
 */
class ServerTest {
    private static Server server;

    @BeforeAll
    static void setUp() {
        server = new Server(3000);
    }

    @Test
    void testServer() {
        assertNotNull(server);
        assertEquals(3000, server.getPort());
    }

    @Test
    void testNewServerHasNoUsers() {
        server = new Server(3000);
        assertEquals(0, server.getConnectedUsers().size());
        assertFalse(server.hasUsers());
    }

    @Test
    void testAddUser() {
        server.addUser(new ChatUser(null, "Mike", null));
        assertEquals(1, server.getConnectedUsers().size());
        assertTrue(server.hasUsers());
    }

    @Test
    void testRemoveUser() {
        ChatUser user = new ChatUser(null, "Mike", null);
        server.addUser(user);
        server.removeUser(user, null);
        assertEquals(0, server.getConnectedUsers().size());
        assertFalse(server.hasUsers());
    }
}
