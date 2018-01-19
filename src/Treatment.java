import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Treatment {
    private int id;

    public Treatment(int id) {
        this.id = id;
    }

    public Treatment(String name) throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT id FROM treatment WHERE name = ?;");
        ps.setString(1, name);

        ResultSet res = ps.executeQuery();
        res.next();
        id = res.getInt(1);
    }

    public int getId() {
        return id;
    }

    public String getName() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT name FROM treatment WHERE id = ?;");
        ps.setInt(1, id);

        ResultSet res = ps.executeQuery();
        res.next();
        return res.getString(1);
    }

    public int getDuration() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT duration FROM treatment WHERE id = ?;");
        ps.setInt(1, id);

        ResultSet res = ps.executeQuery();
        res.next();
        return res.getInt(1);
    }

    public Double getCost() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT cost FROM treatment WHERE id = ?;");
        ps.setInt(1, id);

        ResultSet res = ps.executeQuery();
        res.next();
        return res.getDouble(1);
    }
    
    public int getCategory() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT category FROM treatment WHERE id = ?");
        ps.setInt(1, id);
        
        ResultSet res = ps.executeQuery();
        res.next();
        return res.getInt(1);
    }

    public static List<Treatment> getTreatments() throws SQLException {
        ArrayList<Treatment> treatments = new ArrayList<Treatment>();

        Connection conn = Database.getConnection();
        ResultSet res = conn.createStatement().executeQuery("SELECT id FROM treatment;");

        while (res.next()) {
            Treatment t = new Treatment(res.getInt(1));
            treatments.add(t);
        }

        return treatments;
    }
}