package com.lachesis.mnisqm.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.LocaleUtils;

/**
 * 网站配置工具类，程序启动时会从mnisqm。properties中读取
 *
 * @author Paul Xu.
 * @since 1.0
 */
public class SiteConfiguration {
    private static SiteConfiguration instance;

    private String sentErrorEmail;
    private String siteName;
    private String serverAddress;
    private int serverPort;
    private String noreplyEmail;
    private EmailConfiguration emailConfiguration;
    private DatabaseConfiguration databaseConfiguration;
    private String endecryptPassword;

    private Locale defaultLocale;
    private List<Locale> supportedLanguages;

    public static void loadConfiguration() {
        int serverPort = Integer.parseInt(System.getProperty(ApplicationProperties.MYCOLLAB_PORT, "8080"));
        ApplicationProperties.loadProps();
        instance = new SiteConfiguration();

        instance.sentErrorEmail = ApplicationProperties.getString(ApplicationProperties.ERROR_SENDTO, "support@lachesis-mh.com");
        instance.siteName = ApplicationProperties.getString(ApplicationProperties.SITE_NAME, "lachesis");
        instance.serverAddress = ApplicationProperties.getString(ApplicationProperties.SERVER_ADDRESS, "localhost");
        instance.defaultLocale = toLocale(ApplicationProperties.getString(ApplicationProperties.DEFAULT_LOCALE, "zh_CN"));

        instance.supportedLanguages = getSupportedLocales(ApplicationProperties.getString(ApplicationProperties.LOCALES, "en_US, zh_CN"));

        instance.serverPort = serverPort;

        instance.endecryptPassword = ApplicationProperties.getString(
        		ApplicationProperties.BI_ENDECRYPT_PASSWORD, "esofthead321");

        // load email
        String host = ApplicationProperties.getString(ApplicationProperties.MAIL_SMTPHOST);
        String user = ApplicationProperties.getString(ApplicationProperties.MAIL_USERNAME);
        String password = ApplicationProperties.getString(ApplicationProperties.MAIL_PASSWORD);
        Integer port = Integer.parseInt(ApplicationProperties.getString(ApplicationProperties.MAIL_PORT, "25"));
        Boolean isTls = Boolean.parseBoolean(ApplicationProperties.getString(ApplicationProperties.MAIL_IS_TLS, "false"));
        Boolean isSsl = Boolean.parseBoolean(ApplicationProperties.getString(ApplicationProperties.MAIL_IS_SSL, "false"));
        instance.emailConfiguration = new EmailConfiguration(host, user, password, port, isTls, isSsl);
        instance.noreplyEmail = ApplicationProperties.getString(ApplicationProperties.MAIL_NOREPLY, "support@lachesis-mh.com");

        // load database configuration
        String driverClass = ApplicationProperties.getString(ApplicationProperties.DB_DRIVER_CLASS);
        String dbUrl = ApplicationProperties.getString(ApplicationProperties.DB_URL);
        String dbUser = ApplicationProperties.getString(ApplicationProperties.DB_USERNAME);
        String dbPassword = ApplicationProperties.getString(ApplicationProperties.DB_PASSWORD);
        instance.databaseConfiguration = new DatabaseConfiguration(driverClass, dbUrl, dbUser, dbPassword);

    }

    private static SiteConfiguration getInstance() {
        if (instance == null) {
            loadConfiguration();
        }
        return instance;
    }

    public static DatabaseConfiguration getDatabaseConfiguration() {
        return getInstance().databaseConfiguration;
    }

    public static EmailConfiguration getEmailConfiguration() {
        return getInstance().emailConfiguration;
    }

    public static void setEmailConfiguration(EmailConfiguration conf) {
        getInstance().emailConfiguration = conf;
    }

    public static String getNoReplyEmail() {
        return getInstance().noreplyEmail;
    }

    public static String getDefaultSiteName() {
        return getInstance().siteName;
    }

    public static String getSendErrorEmail() {
        return getInstance().sentErrorEmail;
    }

    public static Locale getDefaultLocale() {
        return getInstance().defaultLocale;
    }

    public static List<Locale> getSupportedLanguages() {
        return getInstance().supportedLanguages;
    }

    public static String getEnDecryptPassword() {
        return getInstance().endecryptPassword;
    }

    public static String getServerAddress() {
        return getInstance().serverAddress;
    }

    public static int getServerPort() {
        return getInstance().serverPort;
    }

    public static Locale toLocale(String language) {
        if (language == null) {
            return Locale.CHINA;
        }

        return LocaleUtils.toLocale(language);
    }

    private static List<Locale> getSupportedLocales(String languageVal) {
        List<Locale> locales = new ArrayList<>();
        /*String[] languages = languageVal.split(",");
        for (String language : languages) {
            Locale locale = toLocale(language.trim());
            if (locale == null) {
                LOG.error("Do not support native language {}", language);
                continue;
            }

            locales.add(locale);
        }*/
        return locales;
    }

}