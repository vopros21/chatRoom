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
        assertEquals(0, server.getUserNames().size());
        assertFalse(server.hasUsers());
    }

    @Test
    void testAddUser() {
        server.addUserName("Mike");
        assertEquals(1, server.getUserNames().size());
        assertTrue(server.hasUsers());
    }

    @Test
    void testRemoveUser() {
        server.addUserName("Mike");
        server.removeUser("Mike", null);
        assertEquals(0, server.getUserNames().size());
        assertFalse(server.hasUsers());
    }
}
