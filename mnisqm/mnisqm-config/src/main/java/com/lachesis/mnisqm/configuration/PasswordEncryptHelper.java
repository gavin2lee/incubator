package com.lachesis.mnisqm.configuration;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;

/**
 * 加密与解密工具类
 *
 * @author Paul Xu.
 * @since 1.0
 */
public class PasswordEncryptHelper {
    private static StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
    private static BasicTextEncryptor basicTextEncryptor;

    static {
        basicTextEncryptor = new BasicTextEncryptor();
        basicTextEncryptor.setPassword(SiteConfiguration.getEnDecryptPassword());
    }

    /**
     * Encrypt password
     *
     * @param password
     * @return
     */
    public static String encryptSaltPassword(String password) {
        return passwordEncryptor.encryptPassword(password);
    }

    public static String encryptText(String text) {
        return basicTextEncryptor.encrypt(text);
    }

    public static String decryptText(String text) {
        return basicTextEncryptor.decrypt(text);
    }

    /**
     * Check password <code>inputPassword</code> match with
     * <code>expectedPassword</code> in case <code>inputPassword</code> encrypt
     * or not
     *
     * @param inputPassword
     * @param expectedPassword
     * @param isPasswordEncrypt flag to denote <code>inputPassword</code> is encrypted or not
     * @return
     */
    public static boolean checkPassword(String inputPassword,
                                        String expectedPassword, boolean isPasswordEncrypt) {
        if (!isPasswordEncrypt) {
            return passwordEncryptor.checkPassword(inputPassword,
                    expectedPassword);
        } else {
            return inputPassword.equals(expectedPassword);
        }
    }
}
