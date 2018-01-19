import java.sql.SQLException;
import java.util.Date;

public class DentistApplication {
    public static User currentUser;
    public static MainWindow mw;
    public static Date selectedDate;

    public static void main(String[] args) throws SQLException {
        new ConnectScreen().setVisible(true);

        WelcomeScreen ws = new WelcomeScreen();
        ws.setVisible(true);

        currentUser = ws.getUser();

        mw = new MainWindow();
        mw.setVisible(true);
    }
}