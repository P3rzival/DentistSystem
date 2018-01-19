
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Connection instance;
    private static String connectionString = "jdbc:mysql://stusql.dcs.shef.ac.uk/team001?user=team001&password=d9f246f5";
    // private static String connectionString = "jdbc:mysql://127.0.0.1/dentist?user=root&password=com2002";

    public static Connection getConnection() throws SQLException {
        if (instance == null) {
            instance = DriverManager.getConnection(connectionString);
        }

        return instance;
    }
}
