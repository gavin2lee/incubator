package com.lachesis.mnisqm.servlet;

import java.util.Properties;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

import com.lachesis.mnisqm.core.UserInvalidInputException;

/**
 * @author Paul Xu.
 * @since 1.0.0
 */
public class InstallUtils {

    public static void checkSMTPConfig(String host, int port, String username,
                                       String password, boolean auth, boolean isStartTls, boolean isSSL) {
        try {
            Properties props = new Properties();
            if (auth) {
                props.setProperty("mail.smtp.auth", "true");
            } else {
                props.setProperty("mail.smtp.auth", "false");
            }
            if (isStartTls) {
                props.setProperty("mail.smtp.starttls.enable", "true");
                props.setProperty("mail.smtp.startssl.enable", "true");
            } else if (isSSL) {
                props.setProperty("mail.smtp.startssl.enable", "false");
                props.setProperty("mail.smtp.ssl.enable", "true");
                props.setProperty("mail.smtp.ssl.socketFactory.fallback", "false");

            }

            Email email = new SimpleEmail();
            email.setHostName(host);
            email.setSmtpPort(port);
            email.setAuthenticator(new DefaultAuthenticator(username, password));
            if (isStartTls) {
                email.setStartTLSEnabled(true);
            } else {
                email.setStartTLSEnabled(false);
            }

            if (isSSL) {
                email.setSSLOnConnect(true);
            } else {
                email.setSSLOnConnect(false);
            }
            email.setFrom(username);
            email.setSubject("mnisqm Test Email");
            email.setMsg("This is a test mail ... :-)");
            email.addTo(username);
            email.send();
        } catch (Exception e) {
            throw new UserInvalidInputException(e);
        }
    }
}
