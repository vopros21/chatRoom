package com.company;

import org.junit.jupiter.api.Test;

/**
 * @author Mike Kostenko on 01/03/2024
 */
public class ServerApplicationTest {
    @Test
    public void verifyNoExceptionThrown() {
        ServerApplication.main(new String[]{});
    }
}
