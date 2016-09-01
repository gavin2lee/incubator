package com.lachesis.mnisqm.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;

import com.lachesis.mnisqm.core.utils.FileUtils;

/**
 * 此文件定义了所有的常量
 * 程序启动时将从mnisqm.properties 中读取相应的数据.
 *
 * @author Paul Xu.
 * @since 1.0
 */
public class ApplicationProperties {
    private static final String RESOURCE_PROPERTIES = "mnisqm.properties";
    private static final String DECRYPT_PASS = "esofthead321";

    private static Properties properties;

    public static final String MYCOLLAB_PORT = "mnisqm.port";

    public static final String DB_USERNAME = "db.username";
    public static final String DB_PASSWORD = "db.password";
    public static final String DB_DRIVER_CLASS = "db.driverClassName";
    public static final String DB_URL = "db.url";

    public static final String MAIL_SMTPHOST = "mail.smtphost";
    public static final String MAIL_PORT = "mail.port";
    public static final String MAIL_USERNAME = "mail.username";
    public static final String MAIL_PASSWORD = "mail.password";
    public static final String MAIL_IS_TLS = "mail.isTLS";
    public static final String MAIL_IS_SSL = "mail.isSSL";
    public static final String MAIL_NOREPLY = "mail.noreply";

    public static final String ERROR_SENDTO = "error.sendTo";
    public static final String STORAGE_SYSTEM = "storageSystem";

    public static final String LOCALES = "locale.list";
    public static final String DEFAULT_LOCALE = "locale.default";
    public static final String SITE_NAME = "site.name";
    public static final String SERVER_ADDRESS = "server.address";

    public static final String RESOURCE_DOWNLOAD_URL = "resource.downloadUrl";

    public static final String BI_ENDECRYPT_PASSWORD = "endecryptPassword";

    public static final String COPYRIGHT_MSG = "copyright";

    public static void loadProps() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(DECRYPT_PASS);

        properties = new EncryptableProperties(encryptor);
        try {
            File myCollabResourceFile = getAppConfigFile();

            if (myCollabResourceFile != null) {
                try (FileInputStream propsStream = new FileInputStream(myCollabResourceFile)) {
                    properties.load(propsStream);
                }
            } else {
                InputStream propStreams = Thread.currentThread().getContextClassLoader().getResourceAsStream(RESOURCE_PROPERTIES);
                if (propStreams == null) {
                    // Probably we are running testing
                    properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("default-mnisqm-test.properties"));
                }
            }
        } catch (Exception e) {
        }
    }

    public static File getAppConfigFile() {
        return FileUtils.getDesireFile(System.getProperty("user.dir"), "resource/mnisqm.properties", "src/main/resource/mnisqm.properties");
    }

    public static Properties getAppProperties() {
        return properties;
    }

    public static String getString(String key) {
        return getString(key, "");
    }

    public static String getString(String key, String defaultValue) {
        if (properties == null) {
            return defaultValue;
        }
        return properties.getProperty(key, defaultValue);
    }
}
