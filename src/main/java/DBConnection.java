import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL =
            "jdbc:mariadb://localhost:3306/tool_manager";
    private static final String USER = "root";
    private static final String PASSWORD = "sofia123";

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
