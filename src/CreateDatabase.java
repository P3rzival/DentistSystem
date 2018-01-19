
import java.sql.Connection;
import java.sql.SQLException;

public class CreateDatabase {
    public static void main(String[] args) {
        try {
            // Connect to database
            Connection con = Database.getConnection();

            // Create tables
            String[] tables = {
                    "DROP TABLE IF EXISTS patient;",
                    "DROP TABLE IF EXISTS healthCarePlan;",
                    "DROP TABLE IF EXISTS appointment;",
                    "DROP TABLE IF EXISTS familyAddress;",
                    "DROP TABLE IF EXISTS plan;",
                    "DROP TABLE IF EXISTS treatment;",
                    "DROP TABLE IF EXISTS partner;",
                    "CREATE TABLE patient (id SERIAL PRIMARY KEY, title TEXT, balance DECIMAL(10, 2) DEFAULT '0.0', checkupLeft INT DEFAULT 0, repairLeft INT DEFAULT 0, hygieneLeft INT DEFAULT 0, forename TEXT, surname TEXT, dateOfBirth DATE DEFAULT '1900-01-01', telephone TEXT, healthCarePlan INT DEFAULT 1, addressId INT REFERENCES familyAddress(id));",
                    "CREATE TABLE familyAddress (id SERIAL PRIMARY KEY, houseNumber TEXT, postCode TEXT, streetName TEXT, districtName TEXT, cityName TEXT);",
                    "CREATE TABLE plan (id SERIAL PRIMARY KEY, name TEXT, cost DECIMAL(10, 2), checkUps INT, repairs INT, hygieneVisits INT);",
                    "CREATE TABLE appointment (id SERIAL PRIMARY KEY, startTime TIMESTAMP NULL DEFAULT NULL, endTime TIMESTAMP NULL DEFAULT NULL, partnerID INT REFERENCES partner(id), patientID INT REFERENCES patient(id), treatment INT REFERENCES treatment(id), treatmentDone BOOLEAN DEFAULT FALSE);",
                    "CREATE TABLE partner (id SERIAL PRIMARY KEY, name TEXT, password TEXT NOT NULL, isSecretary BOOLEAN DEFAULT FALSE);",
                    "CREATE TABLE treatment (id SERIAL PRIMARY KEY, name TEXT, cost DECIMAL(10, 2), duration INT, category INT);", };
            for (String table : tables) {
                con.createStatement().executeUpdate(table);
            }

            // Insert test data
            String[] testDatas = {
                    "INSERT INTO partner (name, password, isSecretary) VALUES ('admin', '1234', TRUE);",
                    "INSERT INTO partner (name, password) VALUES ('dentist', 'iloveteeth');",
                    "INSERT INTO partner (name, password) VALUES ('hygienist', '1111');",
                    "INSERT INTO patient (title, forename, surname) VALUES ('Dr.', 'Holiday', 'Booking');",
                    "INSERT INTO patient (title, forename, surname) VALUES ('Dr.', 'Phil', 'Green');",
                    "INSERT INTO patient (title, forename, surname) VALUES ('Mr.', 'Barry', 'Allen');",
                    "INSERT INTO patient (title, forename, surname) VALUES ('Ms.', 'Cisco', 'Ramon');",
                    "INSERT INTO patient (title, forename, surname) VALUES ('Dr.', 'Phil', 'Orange');",
                    "INSERT INTO plan (name) VALUES ('No Plan');",
                    "INSERT INTO plan (name, cost, checkUps, repairs, hygieneVisits) VALUES ('NHS Plan', '0.00', 2, 6, 2);",
                    "INSERT INTO plan (name, cost, checkUps, repairs, hygieneVisits) VALUES ('Maintenance Plan', '15.00', 2, 0, 2);",
                    "INSERT INTO plan (name, cost, checkUps, repairs, hygieneVisits) VALUES ('Oral Health Plan', '21.00', 2, 0, 4);",
                    "INSERT INTO plan (name, cost, checkUps, repairs, hygieneVisits) VALUES ('Dental Repair Plan', '36.00', 2, 2, 2);",
                    "INSERT INTO treatment (name, cost, duration, category) VALUES ('Hygienist Appointment', '45.00', 20, 3);",
                    "INSERT INTO treatment (name, cost, duration, category) VALUES ('Check-up', '30.00', 20, 1);",
                    "INSERT INTO treatment (name, cost, duration, category) VALUES ('Silver Filling', '90.00', 60, 2);",
                    "INSERT INTO treatment (name, cost, duration, category) VALUES ('Composite Filling', '150.00', 60, 2);",
                    "INSERT INTO treatment (name, cost, duration, category) VALUES ('Gold Crown', '500.00', 60, 2);",
                    "INSERT INTO treatment (name, cost, duration) VALUES ('Holiday', '0.00', 480);"
            };
            for (String testData : testDatas) {
                con.createStatement().executeUpdate(testData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
