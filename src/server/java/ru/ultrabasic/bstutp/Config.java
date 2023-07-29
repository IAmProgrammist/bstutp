package ru.ultrabasic.bstutp;

import jakarta.servlet.ServletContext;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Config {
    public static String SQL_URL;
    public static String SQL_NAME;
    public static String SQL_PASSWORD;
    public static int TESTS_IN_PAGE;
    public static long SESSION_KEY_EXPIRATION_YEARS;

    static {
        try {
            Properties properties = new Properties();
            properties.load(Config.class.getClassLoader().getResourceAsStream("config.properties"));

            SQL_URL = properties.getProperty("sql_url", "");
            SQL_NAME = properties.getProperty("sql_name", "");
            TESTS_IN_PAGE = Integer.parseInt(properties.getProperty("tests_in_page", ""));
            SQL_PASSWORD = properties.getProperty("sql_password", "");
            SESSION_KEY_EXPIRATION_YEARS = Long.valueOf(properties.getProperty("session_key_expiration_years", "2"));
        } catch (IOException e) {
            Logger.getLogger("Config").log(Level.WARNING, "Unable to load config, so shutting down");
            System.exit(0);
        }
    }
}
