import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Patient {
    private int id;

    public Patient() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO patient VALUES ();", Statement.RETURN_GENERATED_KEYS);
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        id = rs.getInt(1);
        
        Address a = new Address();
        setAddress(a);
    }

    public Patient(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getFirstname() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT forename FROM patient WHERE id = ?;");
        ps.setInt(1, id);

        ResultSet res = ps.executeQuery();
        res.next();
        return res.getString(1);
    }

    public void setFirstname(String name) throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE patient SET forename = ? WHERE id = ?;");
        ps.setString(1, name);
        ps.setInt(2, id);

        ps.executeUpdate();
    }

    public String getSurname() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT surname FROM patient WHERE id = ?;");
        ps.setInt(1, id);

        ResultSet res = ps.executeQuery();
        res.next();
        return res.getString(1);
    }

    public void setSurname(String surname) throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE patient SET surname = ? WHERE id = ?;");
        ps.setString(1, surname);
        ps.setInt(2, id);

        ps.executeUpdate();
    }

    public double getBalance() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT balance FROM patient WHERE id = ?;");
        ps.setInt(1, id);

        ResultSet res = ps.executeQuery();
        res.next();
        return res.getDouble(1);
    }

    public void setBalance(double balance) throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE patient SET balance = ? WHERE id = ?;");
        ps.setDouble(1, balance);
        ps.setInt(2, id);

        ps.executeUpdate();
    }
    
    public int getCheckupLeft() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT checkupLeft FROM patient WHERE id = ?;");
        ps.setInt(1, id);

        ResultSet res = ps.executeQuery();
        res.next();
        return res.getInt(1);
    }

    public void setCheckupLeft(int left) throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE patient SET checkupLeft = ? WHERE id = ?;");
        ps.setInt(1, left);
        ps.setInt(2, id);

        ps.executeUpdate();
    }
    
    public int getRepairLeft() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT repairLeft FROM patient WHERE id = ?;");
        ps.setInt(1, id);

        ResultSet res = ps.executeQuery();
        res.next();
        return res.getInt(1);
    }

    public void setRepairLeft(int left) throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE patient SET repairLeft = ? WHERE id = ?;");
        ps.setInt(1, left);
        ps.setInt(2, id);

        ps.executeUpdate();
    }
    
    public int getHygieneLeft() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT hygieneLeft FROM patient WHERE id = ?;");
        ps.setInt(1, id);

        ResultSet res = ps.executeQuery();
        res.next();
        return res.getInt(1);
    }

    public void setHygieneLeft(int left) throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE patient SET hygieneLeft = ? WHERE id = ?;");
        ps.setInt(1, left);
        ps.setInt(2, id);

        ps.executeUpdate();
    }

    public String getTitle() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT title FROM patient WHERE id = ?;");
        ps.setInt(1, id);

        ResultSet res = ps.executeQuery();
        res.next();
        return res.getString(1);
    }

    public void setDOB(int day, int month, int year) throws SQLException {
        Connection conn = Database.getConnection();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);

        String date = df.format(cal.getTime());
        System.out.println(date);

        PreparedStatement ps = conn.prepareStatement("UPDATE patient SET dateOfBirth = ? WHERE id = ?;");
        ps.setString(1, date);
        ps.setInt(2, id);

        ps.executeUpdate();
    }

    public Date getDOB() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT dateOfBirth FROM patient WHERE id = ?;");
        ps.setInt(1, id);

        ResultSet res = ps.executeQuery();
        res.next();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        
        try {
            return df.parse(res.getString(1));
        } catch (ParseException e) {
            return null;
        }
    }

    public void setTitle(String title) throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE patient SET title = ? WHERE id = ?;");
        ps.setString(1, title);
        ps.setInt(2, id);

        ps.executeUpdate();
    }

    public DentalPlan getDentalPlan() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT healthCarePlan FROM patient WHERE id = ?;");
        ps.setInt(1, id);

        ResultSet res = ps.executeQuery();
        res.next();
        int planId = res.getInt(1);
        return new DentalPlan(planId);
    }

    public void setDentalPlan(int plan) throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE patient SET healthCarePlan = ? WHERE id = ?;");
        ps.setDouble(1, plan);
        ps.setInt(2, id);

        ps.executeUpdate();
    }

    public void setDentalPlan(DentalPlan plan) throws SQLException {
        setDentalPlan(plan.getId());
    }

    /**
     * Checks the age of the patient and returns if they are an adult or not.
     * 
     * @return If the Patient is a child
     * @throws SQLException
     */
    public boolean isChild() throws SQLException {
        long birth = getDOB().getTime() / 1000;
        long now = Calendar.getInstance().getTimeInMillis() / 1000;

        return (now - birth) > 18 * 365 * 24 * 60 * 60;
    }

    public void deleteFromDatabase() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM patient WHERE id = ?;");
        ps.setInt(1, id);

        ps.executeUpdate();
    }
    
    public Address getAddress() throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT addressId FROM patient WHERE id = ?;");
        ps.setInt(1, id);

        ResultSet res = ps.executeQuery();
        res.next();
        int addressId = res.getInt(1);
        
        if (addressId == 0) {
            setAddress(new Address());
            return getAddress();
        }
        
        return new Address(addressId);
    }
    
    public void setAddress(int addr) throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE patient SET addressId = ? WHERE id = ?;");
        ps.setDouble(1, addr);
        ps.setInt(2, id);

        ps.executeUpdate();
    }

    public void setAddress(Address addr) throws SQLException {
        setAddress(addr.getId());
    }

    public static List<Patient> getPatients() throws SQLException {
        ArrayList<Patient> patients = new ArrayList<Patient>();

        Connection conn = Database.getConnection();
        ResultSet res = conn.createStatement().executeQuery("SELECT id FROM patient;");

        while (res.next()) {
            int id = res.getInt(1);
            Patient p = new Patient(id);
            patients.add(p);
        }

        return patients;
    }
    
    public List<Appointment> getUpcomingAppointments() throws SQLException {
        ArrayList<Appointment> appointments = new ArrayList<Appointment>();
        
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT id FROM appointment WHERE patientID = ? AND treatmentDone = FALSE;");
        ps.setInt(1, id);
        
        ResultSet res = ps.executeQuery();
        while (res.next()) {
            int id = res.getInt(1);
            Appointment a = new Appointment(id);
            appointments.add(a);
        }
        
        return appointments;
    }
    
    public List<Appointment> getPastAppointments() throws SQLException {
        ArrayList<Appointment> appointments = new ArrayList<Appointment>();
        
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT id FROM appointment WHERE patientID = ? AND treatmentDone = TRUE;");
        ps.setInt(1, id);
        
        ResultSet res = ps.executeQuery();
        while (res.next()) {
            int id = res.getInt(1);
            Appointment a = new Appointment(id);
            appointments.add(a);
        }
        
        return appointments;
    }
}