package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static final String URL = "jdbc:mariadb://localhost:3306/tool_manager";
    private static final String USER = "root";
    private static final String PASSWORD = "sofia123";

    public static Connection getConnection() throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", USER);
        props.setProperty("password", PASSWORD);
        props.setProperty("disabledAuthenticationPlugins", "gssapi,auth_gssapi_client");

        return DriverManager.getConnection(URL, props);
    }
}
