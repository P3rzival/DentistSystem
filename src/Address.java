import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Address {

    private int id;

    public Address() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO familyAddress VALUES ();", Statement.RETURN_GENERATED_KEYS);
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        id = rs.getInt(1);
    }

    public Address(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getHouseNumber() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT houseNumber FROM familyAddress WHERE id = ?;");
        ps.setInt(1, id);

        ResultSet res = ps.executeQuery();
        res.next();
        return res.getString(1);
    }

    public void setHouseNumber(String num) throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE familyAddress SET houseNumber = ? WHERE id = ?;");
        ps.setString(1, num);
        ps.setInt(2, id);

        ps.executeUpdate();
    }

    public String getPostCode() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT postCode FROM familyAddress WHERE id = ?;");
        ps.setInt(1, id);

        ResultSet res = ps.executeQuery();
        res.next();
        return res.getString(1);
    }

    public void setPostCode(String code) throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE familyAddress SET postCode = ? WHERE id = ?;");
        ps.setString(1, code);
        ps.setInt(2, id);

        ps.executeUpdate();
    }

    public String getStreetName() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT streetName FROM familyAddress WHERE id = ?;");
        ps.setInt(1, id);

        ResultSet res = ps.executeQuery();
        res.next();
        return res.getString(1);
    }

    public void setStreetName(String name) throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE familyAddress SET streetName = ? WHERE id = ?;");
        ps.setString(1, name);
        ps.setInt(2, id);

        ps.executeUpdate();
    }

    public String getDistrictName() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT districtName FROM familyAddress WHERE id = ?;");
        ps.setInt(1, id);

        ResultSet res = ps.executeQuery();
        res.next();
        return res.getString(1);
    }

    public void setDistrictName(String district) throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE familyAddress SET districtName = ? WHERE id = ?;");
        ps.setString(1, district);
        ps.setInt(2, id);

        ps.executeUpdate();
    }

    public String getCityName() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT cityName FROM familyAddress WHERE id = ?;");
        ps.setInt(1, id);

        ResultSet res = ps.executeQuery();
        res.next();
        return res.getString(1);
    }

    public void setCityName(String district) throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE familyAddress SET cityName = ? WHERE id = ?;");
        ps.setString(1, district);
        ps.setInt(2, id);

        ps.executeUpdate();
    }
}