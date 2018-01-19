import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {
    JPanel menu = new JPanel();
    JPanel fragment = new JPanel();

    public MainWindow() throws SQLException {
        createLayout();
    }

    public void createLayout() throws SQLException {
        menu.setLayout(new GridBagLayout());
        GridBagConstraints cons = new GridBagConstraints();
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.weightx = 1;
        cons.gridx = 0;
        cons.insets = new Insets(10, 5, 0, 0);

        User user = DentistApplication.currentUser;

        menu.setBorder(BorderFactory.createTitledBorder("Application Menu"));

        {
            JButton b = new JButton("Patient List");

            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setTitle("Patient List - Sheffield Dentistry");
                    fragment.removeAll();
                    try {
                        fragment.add(new PatientList());
                    } catch (SQLException e1) {
                    }
                    fragment.revalidate();
                }
            });

            menu.add(b, cons);
        }

        if (user.isSecretary()) {
            JButton b = new JButton("Register Patient");

            b.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    fragment.removeAll();
                    try {
                        fragment.add(new EditPatientScreen(null));
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    fragment.revalidate();
                }
            });

            menu.add(b, cons);
        }

        if (user.isSecretary()) {
            for (User u : User.getPartners()) {
                JButton b = new JButton(u.getName().substring(0, 1).toUpperCase() + u.getName().substring(1) + "'s Calendar");

                b.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        setTitle("Calendar - Sheffield Dentistry");
                        fragment.removeAll();
                        try {
                            fragment.add(new PartnerCalendar(u));
                        } catch (SQLException e1) {
                        }
                        fragment.revalidate();
                    }
                });

                menu.add(b, cons);
            }
        } else {
            JButton b = new JButton("My Calendar");

            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setTitle("Calendar - Sheffield Dentistry");
                    fragment.removeAll();
                    try {
                        fragment.add(new PartnerCalendar(DentistApplication.currentUser));
                    } catch (SQLException e1) {
                    }
                    fragment.revalidate();
                }
            });

            menu.add(b, cons);
        }

        // Add the menu and fragment to the window
        Container contentPane = getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        fragment.setLayout(new GridLayout(0, 1));
        fragment.add(new PatientList());
        setTitle("Patient List - Sheffield Dentistry");

        contentPane.add(menu);
        contentPane.add(fragment);

        layout.putConstraint(SpringLayout.NORTH, fragment, 5, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, fragment, 5, SpringLayout.EAST, menu);
        layout.putConstraint(SpringLayout.EAST, fragment, -5, SpringLayout.EAST, contentPane);
        layout.putConstraint(SpringLayout.SOUTH, fragment, 5, SpringLayout.SOUTH, contentPane);

        setSize(1200, 600);
        // Set minimum window size to ensure all buttons fit
        setMinimumSize(new Dimension(500, 250));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void setFragment(JPanel inner) {
        try {
            fragment.removeAll();
            fragment.add(inner);
            fragment.revalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}