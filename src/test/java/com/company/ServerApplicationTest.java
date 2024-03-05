package com.company;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mike Kostenko on 01/03/2024
 */
public class ServerApplicationTest {
    @Test
    public void verifyNoExceptionThrown() throws IOException {
        ServerApplication.main(new String[]{});
    }

    @Test
    public void checkResponseCorrectness() {
        ClientApplication client = new ClientApplication();
        client.startConnection("127.0.0.1", 4321);
        String message = "Hello server!";
        String response = client.sendMessage(message);
        assertEquals(message, response);
    }
}
