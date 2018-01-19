import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.table.JTableHeader;

@SuppressWarnings("serial")
public class EditPatientScreen extends JPanel {
    private JTextField name = new JTextField();
    private JTextField surname = new JTextField();
    private JTextField balance = new JTextField();
    private JComboBox<String> planBox = new JComboBox<String>();
    private JButton saveButton = new JButton("Register Patient");
    private JButton deleteButton = new JButton("Delete Patient");
    private ButtonGroup titles = new ButtonGroup();
    private Patient p;
    private JComboBox<String> bookingType = new JComboBox<String>();
    private String[] dAppts = { "Check up", "Treatment", "Teeth clean" };
    private JRadioButton mr = new JRadioButton("Mr.");
    private JRadioButton mrs = new JRadioButton("Mrs.");
    private JRadioButton miss = new JRadioButton("Miss.");
    private JRadioButton ms = new JRadioButton("Ms.");
    private JRadioButton dr = new JRadioButton("Dr.");
    private JLabel selectTitle = new JLabel("Select a title: ");
    private JLabel selectDOB = new JLabel("Select a D.O.B: ");
    private JComboBox<Integer> days = new JComboBox<Integer>();
    private JComboBox<Integer> months = new JComboBox<Integer>();
    private JComboBox<Integer> years = new JComboBox<Integer>();
    private JLabel enterAddress = new JLabel("Please enter you address:");
    private JLabel enterNo = new JLabel("House No.:    ");
    private JLabel enterPC = new JLabel("Post Code:    ");
    private JLabel enterStreet = new JLabel("Street:           ");
    private JLabel enterDistrict = new JLabel("District:         ");
    private JLabel enterCity = new JLabel("City:                ");
    private JLabel nameLabel = new JLabel("Name:             ");
    private JLabel surnameLabel = new JLabel("Surname:            ");
    private JLabel balanceLabel = new JLabel("Balance:");
    private JLabel registerLabel = new JLabel("Select plan: ");
    private JLabel planA = new JLabel("Check-up:");
    private JLabel planALeft = new JLabel("");
    private JLabel planB = new JLabel("Repair:");
    private JLabel planBLeft = new JLabel("");
    private JLabel planC = new JLabel("Hygiene:");
    private JLabel planCLeft = new JLabel("");

    private JTextField houseNo = new JTextField();
    private JTextField postcode = new JTextField();
    private JTextField streetName = new JTextField();
    private JTextField district = new JTextField();
    private JTextField city = new JTextField();
    
    JTable upcomingTable;
    JTable pastTable;

    public EditPatientScreen(Patient p) throws SQLException {
        this.p = p;
        createLayout();

        if (p != null) {
        	
        	switch(p.getTitle()) {
        		case "Mr." :
        			titles.setSelected(mr.getModel(), true);
        			break;
        		case "Mrs." :
        			titles.setSelected(mrs.getModel(), true);
        			break;
        		case "Miss." :
        			titles.setSelected(miss.getModel(), true);
        			break;
        		case "Ms." :
        			titles.setSelected(ms.getModel(), true);
        			break;
        		case "Dr." :
        			titles.setSelected(dr.getModel(), true);
        			break;
        	}
        	
        	//titles.setSelected(p.getTitle(), true);
            name.setText(p.getFirstname());
            surname.setText(p.getSurname());
            balance.setText("" + p.getBalance());
            planBox.setSelectedItem(p.getDentalPlan().getName());
            Date dob = p.getDOB();

            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.setTime(dob);

            days.setSelectedItem(cal.get(Calendar.DAY_OF_MONTH));
            months.setSelectedItem(cal.get(Calendar.MONTH));
            years.setSelectedItem(cal.get(Calendar.YEAR));
            
            planALeft.setText(""+p.getCheckupLeft());
            planBLeft.setText(""+p.getRepairLeft());
            planCLeft.setText(""+p.getHygieneLeft());
            
            Address a = p.getAddress();
            streetName.setText(a.getStreetName());
            postcode.setText(a.getPostCode());
            city.setText(a.getCityName());
            district.setText(a.getDistrictName());
            if (a.getHouseNumber() != null)
            	houseNo.setText("" + a.getHouseNumber());
        }

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Patient p1 = p;

                    if (p == null) {
                        p1 = new Patient();
                    }

                    int day = (int) days.getSelectedItem();
                    int month = (int) months.getSelectedItem();
                    int year = (int) years.getSelectedItem();
	                
            		p1.setTitle(getSelection(titles));
                    p1.setFirstname(name.getText());
                    p1.setSurname(surname.getText());
                    try {
                        p1.setBalance(Double.parseDouble(balance.getText()));
                    } catch (NumberFormatException e1) {
                        p1.setBalance(0.0);
                    }
                    p1.setDOB(day, month, year);

                    DentalPlan newPlan = new DentalPlan((String) planBox.getSelectedItem());
                    if (newPlan.getId() != p1.getDentalPlan().getId()) {
                        p1.setCheckupLeft(newPlan.getCheckupLeft());
                        p1.setRepairLeft(newPlan.getRepairLeft());
                        p1.setHygieneLeft(newPlan.getHygieneLeft());
                    }
                    
                    p1.setDentalPlan(new DentalPlan((String) planBox.getSelectedItem()));
                    
                    // Set patient address
                    Address a = p1.getAddress();
                    a.setCityName(city.getText());
                    a.setStreetName(streetName.getText());
                    a.setPostCode(postcode.getText());
                    a.setDistrictName(district.getText());
                    a.setHouseNumber(houseNo.getText());

                    DentistApplication.mw.setFragment(new PatientList());
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    p.deleteFromDatabase();
                    DentistApplication.mw.setFragment(new PatientList());
                } catch (SQLException e1) {
                }
            }
        });
    }
    
    public String getSelection(ButtonGroup bGrp) {
        for (Enumeration<AbstractButton> buttons = bGrp.getElements(); buttons.hasMoreElements();) {
            AbstractButton b = buttons.nextElement();

            if (b.isSelected()) {
                return b.getText();
            }
        }

        return null;
    }

    /*
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 800);
    }*/

    public void createLayout() throws SQLException {
        JPanel panel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(getWidth(), upcomingTable.getHeight() + pastTable.getHeight() + 950);
            }
        };
        SpringLayout layout = new SpringLayout();
        panel.setLayout(layout);

        {
            String[] colNames = { "Date", "Treatment", "Cost", "Partner" };
    
            Object[][] patients;
            if (p != null) {
                patients = p.getUpcomingAppointments().stream().map((a) -> {
                    try {
                        Object[] values = { a.getStartTime().toGMTString(), a.getTreatment().getName(), a.getTreatment().getCost(), a.getPartner().getName() };
                        return values;
                    } catch (SQLException e) {
                        return null;
                    }
                }).toArray(Object[][]::new);
            } else {
                patients = new Object[][] {};
            }
            upcomingTable = new JTable(patients, colNames);
        }
        
        {
            String[] colNames = { "Date", "Treatment", "Cost", "Partner" };
    
            Object[][] patients;
            if (p != null) {
                patients = p.getPastAppointments().stream().map((a) -> {
                    try {
                    		Object[] values = { a.getStartTime().toGMTString(), a.getTreatment().getName(), a.getTreatment().getCost(), a.getPartner().getName() };
                        return values;
                    } catch (SQLException e) {
                        return null;
                    }
                }).toArray(Object[][]::new);
            } else {
                patients = new Object[][] {};
            }
            pastTable = new JTable(patients, colNames);
        }

        for (String app : dAppts) {
            bookingType.addItem(app);
        }

        for (int date = 1; date <= 31; date++) {
            days.addItem(date);
        }

        for (int month = 1; month <= 12; month++) {
            months.addItem(month);
        }

        for (int year = 1900; year <= 2017; year++) {
            years.addItem(year);
        }

        // radio buttons
        panel.add(selectTitle);
        panel.add(mr);
        panel.add(mrs);
        panel.add(miss);
        panel.add(ms);
        panel.add(dr);

        titles.add(mr);
        titles.add(mrs);
        titles.add(miss);
        titles.add(ms);
        titles.add(dr);

        // parameter labels
        panel.add(nameLabel);
        panel.add(surnameLabel);
        panel.add(selectDOB);
        panel.add(days);
        panel.add(months);
        panel.add(years);

        // address
        panel.add(enterAddress);
        panel.add(enterNo);
        panel.add(enterPC);
        panel.add(enterStreet);
        panel.add(enterDistrict);
        panel.add(enterCity);
        panel.add(houseNo);
        panel.add(postcode);
        panel.add(streetName);
        panel.add(district);
        panel.add(city);

        // health care plan
        panel.add(balanceLabel);
        panel.add(registerLabel);

        // parameters
        panel.add(name);
        panel.add(surname);
        panel.add(balance);

        // drop downs
        panel.add(planBox);

        // buttons
        panel.add(saveButton);
        
        panel.add(planA);
        panel.add(planALeft);
        panel.add(planB);
        panel.add(planBLeft);
        panel.add(planC);
        panel.add(planCLeft);
        
        // Upcoming appointments
        JTableHeader upcomingHeader = upcomingTable.getTableHeader();
        panel.add(upcomingHeader);
        panel.add(upcomingTable);
        
        // Past appointments
        JTableHeader pastHeader = pastTable.getTableHeader();
        panel.add(pastHeader);
        panel.add(pastTable);
        
        // Sections
        JLabel upcomingLabel = new JLabel("Upcoming Appointments");
        JLabel pastLabel = new JLabel("Past Appointments");
        panel.add(upcomingLabel);
        panel.add(pastLabel);

        // delete
        if (this.p != null) {
            saveButton.setText("Submit");
            panel.add(deleteButton);
        }

        for (DentalPlan plan : DentalPlan.getDentalPlans()) {
            planBox.addItem(plan.getName());
        }

        layout.putConstraint(SpringLayout.WEST, selectTitle, 30, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.WEST, nameLabel, 30, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.WEST, surnameLabel, 30, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.WEST, selectDOB, 30, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.WEST, enterAddress, 30, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.WEST, enterNo, 30, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.WEST, enterPC, 30, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.WEST, enterStreet, 30, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.WEST, enterDistrict, 30, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.WEST, enterCity, 30, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.WEST, balanceLabel, 30, SpringLayout.WEST, this);

        layout.putConstraint(SpringLayout.NORTH, selectTitle, 30, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.NORTH, mr, 30, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.NORTH, mrs, 30, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.NORTH, miss, 30, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.NORTH, ms, 30, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.NORTH, dr, 30, SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, mr, 30, SpringLayout.EAST, selectTitle);
        layout.putConstraint(SpringLayout.WEST, mrs, 30, SpringLayout.EAST, mr);
        layout.putConstraint(SpringLayout.WEST, miss, 30, SpringLayout.EAST, mrs);
        layout.putConstraint(SpringLayout.WEST, ms, 30, SpringLayout.EAST, miss);
        layout.putConstraint(SpringLayout.WEST, dr, 30, SpringLayout.EAST, ms);

        // add name label and name input
        layout.putConstraint(SpringLayout.NORTH, nameLabel, 30, SpringLayout.NORTH, selectTitle);
        layout.putConstraint(SpringLayout.NORTH, name, 30, SpringLayout.NORTH, selectTitle);
        layout.putConstraint(SpringLayout.WEST, name, 20, SpringLayout.EAST, nameLabel);
        layout.putConstraint(SpringLayout.EAST, name, -40, SpringLayout.EAST, this);

        // separate surname from name
        layout.putConstraint(SpringLayout.NORTH, surnameLabel, 20, SpringLayout.SOUTH, nameLabel);
        layout.putConstraint(SpringLayout.NORTH, surname, 20, SpringLayout.SOUTH, nameLabel);

        // add surname label and surname input
        layout.putConstraint(SpringLayout.WEST, surname, 0, SpringLayout.WEST, name);
        layout.putConstraint(SpringLayout.EAST, surname, -40, SpringLayout.EAST, this);

        layout.putConstraint(SpringLayout.NORTH, selectDOB, 20, SpringLayout.SOUTH, surnameLabel);
        layout.putConstraint(SpringLayout.NORTH, days, 20, SpringLayout.SOUTH, surnameLabel);
        layout.putConstraint(SpringLayout.NORTH, months, 20, SpringLayout.SOUTH, surnameLabel);
        layout.putConstraint(SpringLayout.NORTH, years, 20, SpringLayout.SOUTH, surnameLabel);
        layout.putConstraint(SpringLayout.WEST, days, 20, SpringLayout.EAST, selectDOB);
        layout.putConstraint(SpringLayout.WEST, months, 20, SpringLayout.EAST, days);
        layout.putConstraint(SpringLayout.WEST, years, 20, SpringLayout.EAST, months);

        layout.putConstraint(SpringLayout.NORTH, enterAddress, 20, SpringLayout.SOUTH, selectDOB);
        layout.putConstraint(SpringLayout.NORTH, houseNo, 20, SpringLayout.SOUTH, enterAddress);
        layout.putConstraint(SpringLayout.EAST, houseNo, -40, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.NORTH, enterNo, 20, SpringLayout.SOUTH, enterAddress);
        layout.putConstraint(SpringLayout.NORTH, postcode, 20, SpringLayout.SOUTH, enterNo);
        layout.putConstraint(SpringLayout.EAST, postcode, -40, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.NORTH, enterPC, 20, SpringLayout.SOUTH, enterNo);
        layout.putConstraint(SpringLayout.NORTH, streetName, 20, SpringLayout.SOUTH, enterPC);
        layout.putConstraint(SpringLayout.EAST, streetName, -40, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.NORTH, enterStreet, 20, SpringLayout.SOUTH, enterPC);
        layout.putConstraint(SpringLayout.NORTH, district, 20, SpringLayout.SOUTH, enterStreet);
        layout.putConstraint(SpringLayout.EAST, district, -40, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.NORTH, enterDistrict, 20, SpringLayout.SOUTH, enterStreet);
        layout.putConstraint(SpringLayout.NORTH, city, 20, SpringLayout.SOUTH, enterDistrict);
        layout.putConstraint(SpringLayout.EAST, city, -40, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.NORTH, enterCity, 20, SpringLayout.SOUTH, enterDistrict);

        layout.putConstraint(SpringLayout.WEST, houseNo, 20, SpringLayout.EAST, enterNo);
        layout.putConstraint(SpringLayout.WEST, postcode, 20, SpringLayout.EAST, enterPC);
        layout.putConstraint(SpringLayout.WEST, streetName, 20, SpringLayout.EAST, enterStreet);
        layout.putConstraint(SpringLayout.WEST, district, 20, SpringLayout.EAST, enterDistrict);
        layout.putConstraint(SpringLayout.WEST, city, 20, SpringLayout.EAST, enterCity);

        // separate balance and name
        layout.putConstraint(SpringLayout.NORTH, balanceLabel, 20, SpringLayout.SOUTH, enterCity);
        layout.putConstraint(SpringLayout.NORTH, balance, 20, SpringLayout.SOUTH, enterCity);

        // add balance label and balance input
        layout.putConstraint(SpringLayout.WEST, balance, 0, SpringLayout.WEST, city);
        layout.putConstraint(SpringLayout.EAST, balance, -40, SpringLayout.EAST, this);

        // separate plan
        layout.putConstraint(SpringLayout.NORTH, registerLabel, 30, SpringLayout.SOUTH, balanceLabel);
        layout.putConstraint(SpringLayout.NORTH, planBox, 5, SpringLayout.SOUTH, registerLabel);
        layout.putConstraint(SpringLayout.NORTH, saveButton, 5, SpringLayout.SOUTH, planBox);

        // add plan
        layout.putConstraint(SpringLayout.WEST, registerLabel, 30, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.WEST, planBox, 20, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.WEST, saveButton, 40, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.WEST, planBox, 30, SpringLayout.WEST, this);
        
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, deleteButton, 0, SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.NORTH, deleteButton, 50, SpringLayout.SOUTH, saveButton);
        layout.putConstraint(SpringLayout.NORTH, planA, 50, SpringLayout.SOUTH, deleteButton);
        layout.putConstraint(SpringLayout.NORTH, planALeft, 50, SpringLayout.SOUTH, deleteButton);
        layout.putConstraint(SpringLayout.WEST, planALeft, 50, SpringLayout.EAST, planA);
        layout.putConstraint(SpringLayout.NORTH, planB, 20, SpringLayout.SOUTH, planA);
        layout.putConstraint(SpringLayout.NORTH, planBLeft, 20, SpringLayout.SOUTH, planALeft);
        layout.putConstraint(SpringLayout.WEST, planBLeft, 50, SpringLayout.EAST, planB);
        layout.putConstraint(SpringLayout.NORTH, planC, 20, SpringLayout.SOUTH, planB);
        layout.putConstraint(SpringLayout.NORTH, planCLeft, 20, SpringLayout.SOUTH, planBLeft);
        layout.putConstraint(SpringLayout.WEST, planCLeft, 50, SpringLayout.EAST, planC);
        
        layout.putConstraint(SpringLayout.NORTH, upcomingLabel, 30, SpringLayout.SOUTH, planC);
        layout.putConstraint(SpringLayout.NORTH, upcomingHeader, 30, SpringLayout.SOUTH, upcomingLabel);
        layout.putConstraint(SpringLayout.NORTH, upcomingTable, 0, SpringLayout.SOUTH, upcomingHeader);
        layout.putConstraint(SpringLayout.WEST, upcomingHeader, 0, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, upcomingHeader, 0, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.WEST, upcomingTable, 0, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, upcomingTable, 0, SpringLayout.EAST, this);
        
        layout.putConstraint(SpringLayout.NORTH, pastLabel, 30, SpringLayout.SOUTH, upcomingTable);
        layout.putConstraint(SpringLayout.NORTH, pastHeader, 30, SpringLayout.SOUTH, pastLabel);
        layout.putConstraint(SpringLayout.NORTH, pastTable, 0, SpringLayout.SOUTH, pastHeader);
        layout.putConstraint(SpringLayout.WEST, pastHeader, 0, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, pastHeader, 0, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.WEST, pastTable, 0, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, pastTable, 0, SpringLayout.EAST, this);
        
        JScrollPane scrollBox = new JScrollPane(panel);
        setLayout(new BorderLayout());
        add(scrollBox, BorderLayout.CENTER);
        DentistApplication.mw.setTitle("Edit Patient - Sheffield Dentistry");
    }
}
