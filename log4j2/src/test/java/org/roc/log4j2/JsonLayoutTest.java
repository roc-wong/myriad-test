package org.roc.log4j2;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.IllegalFormatException;

/**
 * @author roc
 * @since 2020/3/17 16:09
 */
public class JsonLayoutTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonLayoutTest.class);

    @Test
    public void testInfo(){
        LOGGER.info("message={}, value={}", "Hello world", "31");
        LOGGER.warn("message={}, value={}", "Hello world", "31");
        LOGGER.trace("message={}, value={}", "Hello world", "31");
        LOGGER.debug("message={}, value={}", "Hello world", "31");
        LOGGER.error("message={}, value={}", "Hello world", "31" , new NullPointerException("esfads"));
    }
}
