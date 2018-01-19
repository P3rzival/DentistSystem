import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class PatientList extends JPanel {
    private JTable patientTable;
    private JScrollPane container;
    private JDialog modal = null;
    private Patient selectedPatient;

    public PatientList() throws SQLException {
        String[] colNames = { "ID", "Name", "Surname", "Balance" };

        Object[][] patients = Patient.getPatients().stream().map((p) -> {
            try {
                Object[] values = { "" + p.getId(), p.getFirstname(), p.getSurname(), "" + p.getBalance() };
                return values;
            } catch (SQLException e) {
                return null;
            }
        }).toArray(Object[][]::new);

        patientTable = new JTable(patients, colNames);
        container = new JScrollPane(patientTable);

        patientTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = patientTable.rowAtPoint(e.getPoint());

                if (e.getClickCount() == 2 && row != -1) {
                    int databaseId = Integer.parseInt((String) patientTable.getModel().getValueAt(row, 0));

                    Patient p = new Patient(databaseId);
                    try {
                        if (modal == null) {
                            DentistApplication.mw.setFragment(new EditPatientScreen(p));
                        } else {
                            selectedPatient = p;
                            modal.setVisible(false);
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        createLayout();
    }

    public void createLayout() {
        setLayout(new BorderLayout());
        add(patientTable.getTableHeader(), BorderLayout.PAGE_START);
        add(container, BorderLayout.CENTER);

        // setTitle("Patient List");
        // setSize(400, 200);
        // setResizable(false);
        // setDefaultCloseOperation(EXIT_ON_CLOSE);
        // setLocationRelativeTo(null); // Center window
    }

    public static Patient modalPatientPicker() throws SQLException {
        JDialog modal = new JDialog();

        modal.setTitle("Please select a patient");
        modal.setSize(500, 500);
        modal.setResizable(false);
        modal.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        modal.setLocationRelativeTo(null);
        modal.setLayout(new GridLayout(1, 1));

        PatientList pl = new PatientList();
        pl.modal = modal;
        modal.add(pl);

        modal.setModal(true);
        modal.setVisible(true);

        return pl.selectedPatient;
    }

}
