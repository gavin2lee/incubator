package com.lachesis.mnisqm.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Paul Xu
 * @since 1.0.0
 */
public class SimpleLogging {
    private static Logger LOG = LoggerFactory.getLogger(SimpleLogging.class);

    public static void error(String message) {
        LOG.error(message);
    }

    public static void error(String message, Throwable e) {
        LOG.error(message, e);
    }
}
