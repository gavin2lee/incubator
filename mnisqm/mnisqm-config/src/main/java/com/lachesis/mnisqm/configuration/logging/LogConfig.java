package com.lachesis.mnisqm.configuration.logging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;

import com.lachesis.mnisqm.core.utils.FileUtils;

/**
 * @author Paul xu
 * @since 1.0.0
 */
public class LogConfig {

    public static void initMyCollabLog() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.reset();
        JoranConfigurator configurator = new JoranConfigurator();
        InputStream inputStream = LogConfig.class.getClassLoader().getResourceAsStream("logback-test.xml");
        if (inputStream == null) {
            try {
                File configFile = FileUtils.getDesireFile(System.getProperty("user.dir"), "conf/logback.xml", "src/main/conf/logback.xml");
                if (configFile != null) inputStream = new FileInputStream(configFile);
            } catch (FileNotFoundException e) {
                inputStream = LogConfig.class.getClassLoader().getResourceAsStream("logback.xml");
            }
        }

        if (inputStream != null) {
            try {
                configurator.setContext(loggerContext);
                configurator.doConfigure(inputStream); // loads logback file
            } catch (Exception e) {
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
