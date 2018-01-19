import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class BookAppointment extends JPanel {

    private JComboBox<String> treatmentBox = new JComboBox<String>();
    private JButton bookButton = new JButton("Book Appointment");
    private JButton cancelButton = new JButton("Cancel Appointment");
    private JLabel cancel = new JLabel("Cancel selected appointment:");
    private JLabel info = new JLabel("Enter patient ID, appointment info and select a time by double clicking a slot on the calendar");
    protected static JLabel appointmentTime = new JLabel("Select appointment slot");
    private JLabel patientId = new JLabel("Patient");
    private JButton selectPatient = new JButton("Select Patient");
    private final Dimension textField = new Dimension(60, 19);
    private Patient selectedPatient;
    private Date selectedDate;

    public BookAppointment(User partner, PartnerCalendar cal) throws SQLException {
        createLayout();

        selectPatient.addActionListener((e) -> {
            try {
                selectedPatient = PatientList.modalPatientPicker();
                patientId.setText(selectedPatient.getFirstname() + " " + selectedPatient.getSurname());
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
        
        cancelButton.addActionListener((e) -> {
            try {
            	long startTime = selectedDate.getTime() / 1000;
            	Appointment a = Appointment.findAtTimeFiltered(startTime, partner);
            	a.deleteFromDatabase();
            	cal.repaint();
            } catch (SQLException e1) {
                e1.printStackTrace();
            
                
            }
        });

        cal.calendar.setTimeSelectedHandler((Date date) -> {
            SimpleDateFormat sf = new SimpleDateFormat("hh:mm");
            selectedDate = date;
            appointmentTime.setText(sf.format(date));
        });
        
        

        bookButton.addActionListener((e) -> {
            try {
                // Make the appointments 1 minute less in order
                // to avoid spillovers
            	long startTime = selectedDate.getTime() / 1000;
            	if (Appointment.findAtTimeFiltered(startTime, partner) != null) {
            		return;
            	} 
            	
                Appointment a = new Appointment();
                Treatment t = new Treatment((String) treatmentBox.getSelectedItem());

                a.setStartTime(startTime);
                a.setEndTime(startTime + (t.getDuration() - 1) * 60);
                a.setPatient(selectedPatient);
                a.setPartner(partner);
                a.setTreatment(t);
                

                cal.repaint();
            } catch (SQLException e1) {
                e1.printStackTrace();
            
                
            }
        });
    }

    public void createLayout() throws SQLException {
        setLayout(new BorderLayout());

        for (Treatment t : Treatment.getTreatments()) {
            treatmentBox.addItem(t.getName());
        }

        JPanel buttons = new JPanel(new GridLayout(3, 1));
        JPanel bookButtons = new JPanel(new FlowLayout());
        JPanel cancelButtonRow = new JPanel(new FlowLayout());

        patientId.setPreferredSize(textField);
        appointmentTime.setSize(textField);
        info.setHorizontalAlignment(SwingConstants.CENTER);
        // create booking row
        buttons.add(info);
        bookButtons.add(selectPatient);
        bookButtons.add(patientId);
        bookButtons.add(treatmentBox);
        bookButtons.add(appointmentTime);
        bookButtons.add(bookButton);

        // create cancel row
        cancelButtonRow.add(cancel);
        cancelButtonRow.add(cancelButton);

        // set rows orientation left -> right
        bookButtons.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        cancelButton.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        // add rows to grid
        buttons.add(bookButtons);
        buttons.add(cancelButtonRow);

        add(buttons, BorderLayout.SOUTH);
        setBorder(new EmptyBorder(10, 10, 10, 10));
    }
}
