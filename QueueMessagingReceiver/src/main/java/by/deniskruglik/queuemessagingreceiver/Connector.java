package by.deniskruglik.queuemessagingreceiver;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Connector {
    private final static String DB_PROPERTIES_PATH = "./src/main/resources/db.properties";
    private final static String DB_HOST_KEY = "db_host";
    private final static String DB_NAME_KEY = "db_name";
    private final static String DB_USER_KEY = "db_user";
    private final static String DB_PASSWORD_KEY = "db_password";
    private final static String DB_PORT_KEY = "db_port";

    public static Connection getConnection() {
        Properties properties = new Properties();
        Reader reader = null;
        try {
            reader = new FileReader(DB_PROPERTIES_PATH);
            properties.load(reader);
            String host = properties.getProperty(DB_HOST_KEY);
            String dbName = properties.getProperty(DB_NAME_KEY);
            String user = properties.getProperty(DB_USER_KEY);
            String password = properties.getProperty(DB_PASSWORD_KEY);
            String port = properties.getProperty(DB_PORT_KEY);
            return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + dbName + "?serverTimezone=UTC", user, password);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to create connection source", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
