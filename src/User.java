import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;

    public User(int id) {
        this.id = id;
    }

    public User(String username) throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT id FROM partner WHERE name = ?;");
        ps.setString(1, username);

        ResultSet res = ps.executeQuery();
        res.next();
        id = res.getInt(1);
    }

    public int getId() {
        return id;
    }

    public String getPassword() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT password FROM partner WHERE id = ?;");
        ps.setInt(1, id);
        ResultSet res = ps.executeQuery();
        res.next();
        return res.getString(1);
    }

    public String getName() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT name FROM partner WHERE id = ?;");
        ps.setInt(1, id);
        ResultSet res = ps.executeQuery();
        res.next();
        return res.getString(1);
    }

    public boolean isSecretary() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT isSecretary FROM partner WHERE id = ?;");
        ps.setInt(1, id);
        ResultSet res = ps.executeQuery();
        res.next();
        return res.getBoolean(1);
    }
    
    public Appointment getNextAppointment() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT id FROM appointment WHERE treatmentDone = FALSE AND partnerID = ? ORDER BY startTime ASC;");
        ps.setInt(1, id);
        
        ResultSet res = ps.executeQuery();
        if (res.next()) {
            return new Appointment(res.getInt(1));
        } else {
            return null;
        }
    }

    public static List<User> getPartners() throws SQLException {
        ArrayList<User> users = new ArrayList<User>();

        Connection conn = Database.getConnection();
        ResultSet res = conn.createStatement().executeQuery("SELECT id FROM partner WHERE isSecretary = FALSE;");

        while (res.next()) {
            User u = new User(res.getInt(1));
            users.add(u);
        }

        return users;
    }
}
