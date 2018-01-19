import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class Appointment {
    private int id;

    public Appointment() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO appointment VALUES ();", Statement.RETURN_GENERATED_KEYS);
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        id = rs.getInt(1);
    }

    public Appointment(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Patient getPatient() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT patientID FROM appointment WHERE id = ?;");
        ps.setInt(1, id);

        ResultSet res = ps.executeQuery();
        res.next();
        int patientId = res.getInt(1);
        return new Patient(patientId);
    }

    public void setPatient(int patient) throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE appointment SET patientID = ? WHERE id = ?;");
        ps.setDouble(1, patient);
        ps.setInt(2, id);

        ps.executeUpdate();
    }

    public void setPatient(Patient patient) throws SQLException {
        setPatient(patient.getId());
    }

    public User getPartner() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT partnerID FROM appointment WHERE id = ?;");
        ps.setInt(1, id);

        ResultSet res = ps.executeQuery();
        res.next();
        int partnerId = res.getInt(1);
        return new User(partnerId);
    }

    public void setPartner(int partner) throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE appointment SET partnerID = ? WHERE id = ?;");
        ps.setDouble(1, partner);
        ps.setInt(2, id);

        ps.executeUpdate();
    }

    public void setPartner(User partner) throws SQLException {
        setPartner(partner.getId());
    }

    public Date getStartTime() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT startTime FROM appointment WHERE id = ?;");
        ps.setInt(1, id);

        ResultSet res = ps.executeQuery();
        res.next();
        return res.getTimestamp(1);
    }

    public void setStartTime(long timestamp) throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE appointment SET startTime = FROM_UNIXTIME(?) WHERE id = ?;");
        ps.setLong(1, timestamp);
        ps.setInt(2, id);

        ps.executeUpdate();
    }

    public Date getEndTime() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT endTime FROM appointment WHERE id = ?;");
        ps.setInt(1, id);

        ResultSet res = ps.executeQuery();
        res.next();
        return res.getTimestamp(1);
    }

    public void setEndTime(long timestamp) throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE appointment SET endTime = FROM_UNIXTIME(?) WHERE id = ?;");
        ps.setLong(1, timestamp);
        ps.setInt(2, id);

        ps.executeUpdate();
    }

    public Treatment getTreatment() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT treatment FROM appointment WHERE id = ?;");
        ps.setInt(1, id);

        ResultSet res = ps.executeQuery();
        res.next();
        return new Treatment(res.getInt(1));
    }

    public void setTreatment(int treatment) throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE appointment SET treatment = ? WHERE id = ?;");
        ps.setLong(1, treatment);
        ps.setInt(2, id);

        ps.executeUpdate();
    }

    public void setTreatment(Treatment treatment) throws SQLException {
        setTreatment(treatment.getId());
    }
    
    public void setTreatmentDone(boolean done) throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE appointment SET treatmentDone = ? WHERE id = ?;");
        ps.setBoolean(1, done);
        ps.setInt(2, id);

        ps.executeUpdate();
    }
    
    public boolean getTreatmentDone() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT treatmentDone FROM appointment WHERE id = ?;");
        ps.setInt(1, id);

        ResultSet res = ps.executeQuery();
        res.next();
        return res.getBoolean(1);
    }
    
    public void deleteFromDatabase() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM appointment WHERE id = ?;");
        ps.setInt(1, id);

        ps.executeUpdate();
    }

    public static Appointment findAtTime(long timestamp) throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT id FROM appointment WHERE FROM_UNIXTIME(?) BETWEEN startTime AND endTime;");
        ps.setLong(1, timestamp);

        ResultSet res = ps.executeQuery();
        if (res.next()) {
            int appointment = res.getInt(1);
            return new Appointment(appointment);
        }

        return null;
    }

    public static Appointment findAtTimeFiltered(long timestamp, User u) throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT id FROM appointment WHERE FROM_UNIXTIME(?) BETWEEN startTime AND endTime AND partnerID = ?;");
        ps.setLong(1, timestamp);
        ps.setInt(2, u.getId());

        ResultSet res = ps.executeQuery();
        if (res.next()) {
            int appointment = res.getInt(1);
            return new Appointment(appointment);
        }

        return null;
    }
}