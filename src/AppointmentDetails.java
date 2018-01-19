import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class AppointmentDetails extends JPanel {
    private JButton submitButton = new JButton("Submit");
    private JLabel treatmentsGiven = new JLabel();
    private JLabel patientId = new JLabel("Patient");
    private JLabel thisAppointment = new JLabel("Treatment:");
    private Appointment selectedAppointment;
    private User partner;

    public AppointmentDetails(User partner, PartnerCalendar cal) throws SQLException {
        this.partner = partner;
        
        submitButton.addActionListener((e) -> {
            try {
                Appointment a = selectedAppointment;
                Patient p = selectedAppointment.getPatient();
                submitButton.setEnabled(false);
                a.setTreatmentDone(true);
                
                int category = a.getTreatment().getCategory();
                
                if (category == 1) {
                    // Check-up
                    if (p.getCheckupLeft() > 0) {
                        p.setCheckupLeft(p.getCheckupLeft() - 1);
                    } else {
                        p.setBalance(p.getBalance() - a.getTreatment().getCost());
                    }
                } else if (category == 2) {
                    // Repair
                    if (p.getRepairLeft() > 0) {
                        p.setRepairLeft(p.getRepairLeft() - 1);
                    } else {
                        p.setBalance(p.getBalance() - a.getTreatment().getCost());
                    }
                } else if (category == 3) {
                    // Hygiene
                    if (p.getHygieneLeft() > 0) {
                        p.setHygieneLeft(p.getHygieneLeft() - 1);
                    } else {
                        p.setBalance(p.getBalance() - a.getTreatment().getCost());
                    }
                } else {
                    p.setBalance(p.getBalance() - a.getTreatment().getCost());
                }
                
                cal.repaint();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        cal.calendar.setTimeSelectedHandler((Date date) -> {
            try {
                selectedAppointment = Appointment.findAtTimeFiltered(date.getTime() / 1000, partner);
                if (selectedAppointment != null) {
                    patientId.setText(selectedAppointment.getPatient().getFirstname());
                    treatmentsGiven.setText(selectedAppointment.getTreatment().getName());
                    submitButton.setEnabled(!selectedAppointment.getTreatmentDone());
                } else {
                    submitButton.setEnabled(false);
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
        
        createLayout();
    }

    public void createLayout() throws SQLException {

        setLayout(new BorderLayout());
        submitButton.setEnabled(false);

        JPanel buttons = new JPanel(new GridLayout(3, 1));
        JPanel bookButtons = new JPanel(new FlowLayout());
        JPanel cancelButtonRow = new JPanel(new FlowLayout());
        JPanel appointmentInfo = new JPanel(new FlowLayout());
        JLabel next = new JLabel("Next appointment: ");
        
        Appointment nextApt = partner.getNextAppointment();
        StringBuilder sb = new StringBuilder();
        if (nextApt != null) {
            sb.append(nextApt.getPatient().getFirstname());
            sb.append(" ");
            sb.append(nextApt.getPatient().getSurname());
            sb.append(" - ");
            sb.append(nextApt.getTreatment().getName());
        } else {
            sb.append("No future appointments found");
        }
        JLabel details = new JLabel(sb.toString());

        // create appointmentInfo row
        next.setHorizontalAlignment(SwingConstants.LEFT);
        details.setHorizontalAlignment(SwingConstants.RIGHT);
        appointmentInfo.add(next);
        appointmentInfo.add(details);
        
        // create booking row
        bookButtons.add(patientId);
        bookButtons.add(submitButton);

        // create cancel row
        thisAppointment.setHorizontalAlignment(SwingConstants.LEFT);
        treatmentsGiven.setHorizontalAlignment(SwingConstants.CENTER);
        cancelButtonRow.add(thisAppointment);
        cancelButtonRow.add(treatmentsGiven);

        // set rows orientation left -> right
        appointmentInfo.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        bookButtons.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        cancelButtonRow.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        // add rows to grid
        buttons.add(appointmentInfo, SwingConstants.CENTER);
        buttons.add(bookButtons);
        buttons.add(cancelButtonRow);

        add(buttons, BorderLayout.SOUTH);
        setBorder(new EmptyBorder(10, 10, 10, 10));

    }
}
