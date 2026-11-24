package utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseUtils {

    private static final String CONFIG_FILE = "database.properties";
    private static final String EXAMPLE_CONFIG_FILE = "database.properties.example";
    private static final Properties PROPS = loadProperties();

    private DatabaseUtils(){
        throw new UnsupportedOperationException("Utility klasa");
    }

    private static Properties loadProperties() {
        ClassLoader loader = DatabaseUtils.class.getClassLoader();
        InputStream config = loader.getResourceAsStream(CONFIG_FILE);
        InputStream input = config != null ? config : loader.getResourceAsStream(EXAMPLE_CONFIG_FILE);
        if (input == null) {
            throw new IllegalStateException("Nije moguće pronaći " + CONFIG_FILE);
        }

        Properties props = new Properties();
        try (input) {
            props.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Greška pri čitanju konfiguracije baze", e);
        }
        return props;
    }

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(
                PROPS.getProperty("db.url"),
                PROPS.getProperty("db.user"),
                PROPS.getProperty("db.password")
        );
    }

    public static void initSchema() throws SQLException {
        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            statement.execute("RUNSCRIPT FROM 'classpath:/schema.sql' CHARSET 'UTF-8'");
        }
    }
}
