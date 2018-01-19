import java.awt.BorderLayout;
import java.sql.SQLException;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class PartnerCalendar extends JPanel {
    private User partner;
    protected CalendarScreen calendar;

    public PartnerCalendar(User partner) throws SQLException {
        this.partner = partner;

        createLayout();
    }

    public void createLayout() throws SQLException {
        setLayout(new BorderLayout());
        JPanel appts = new JPanel(new BorderLayout());
        calendar = new CalendarScreen(partner);
        appts.add(calendar);

        add(appts, BorderLayout.CENTER);
        if (DentistApplication.currentUser.isSecretary()) {
            BookAppointment buttons = new BookAppointment(partner, this);
            add(buttons, BorderLayout.SOUTH);
        } else {
            try {
            JPanel buttons = new AppointmentDetails(partner, this);
            add(buttons, BorderLayout.SOUTH);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
        }

        setBorder(new EmptyBorder(10, 10, 10, 10));
    }
}
