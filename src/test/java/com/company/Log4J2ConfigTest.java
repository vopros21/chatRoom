package com.company;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Mike Kostenko on 16/03/2024
 */
@SpringBootTest(classes = {Log4J2ConfigTest.class})
public class Log4J2ConfigTest {
    private static final Logger log = LoggerFactory.getLogger(Log4J2ConfigTest.class);

    @Test
    public void TestLoggingLevels() {
        log.trace("Trace message is logged.");
        log.debug("Debug message is logged.");
        log.info("Info message is logged.");
        log.warn("Warn message is logged.");
        try {
            throw new RuntimeException("Unknown error.");
        } catch (Exception e) {
            log.error("Error message is logged: ", e);
        }
    }
}
