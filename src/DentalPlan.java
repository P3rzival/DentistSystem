import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DentalPlan {
    private int id;

    public DentalPlan(int id) {
        this.id = id;
    }

    public DentalPlan(String name) throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT id FROM plan WHERE name = ?;");
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
        PreparedStatement ps = conn.prepareStatement("SELECT name FROM plan WHERE id = ?;");
        ps.setInt(1, id);

        ResultSet res = ps.executeQuery();
        res.next();
        return res.getString(1);
    }
    
    public int getCheckupLeft() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT checkUps FROM plan WHERE id = ?;");
        ps.setInt(1, id);

        ResultSet res = ps.executeQuery();
        res.next();
        return res.getInt(1);
    }
    
    public int getRepairLeft() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT checkUps FROM plan WHERE id = ?;");
        ps.setInt(1, id);

        ResultSet res = ps.executeQuery();
        res.next();
        return res.getInt(1);
    }
    
    public int getHygieneLeft() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT checkUps FROM plan WHERE id = ?;");
        ps.setInt(1, id);

        ResultSet res = ps.executeQuery();
        res.next();
        return res.getInt(1);
    }

    public static List<DentalPlan> getDentalPlans() throws SQLException {
        ArrayList<DentalPlan> plans = new ArrayList<DentalPlan>();

        Connection conn = Database.getConnection();
        ResultSet res = conn.createStatement().executeQuery("SELECT id FROM plan;");

        while (res.next()) {
            DentalPlan dp = new DentalPlan(res.getInt(1));
            plans.add(dp);
        }

        return plans;
    }
}
