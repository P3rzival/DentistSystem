import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

@SuppressWarnings("serial")
public class CalendarScreen extends JPanel {

    private JTable calendar;
    private JScrollPane container;

    private JButton prevButton = new JButton("<<");
    private JButton nextButton = new JButton(">>");
    private JLabel weekLabel = new JLabel();

    private User user;

    private int selectedRow;
    private int selectedCol;

    private int weekOffset = 0;

    public interface TimeSelectedHandler {
        void onTimeSelected(Date selected);
    }

    private TimeSelectedHandler timeSelectedHandler;

    public CalendarScreen(User filter) throws SQLException {
        this();
        this.user = filter;
    }

    public CalendarScreen() throws SQLException {
        CalendarModel model = new CalendarModel();

        prevButton.addActionListener((e) -> {
            weekOffset -= 1;
            updateWeekOffset();
        });

        nextButton.addActionListener((e) -> {
            weekOffset += 1;
            updateWeekOffset();
        });

        calendar = new JTable(model);
        calendar.setGridColor(Color.BLACK);
        container = new JScrollPane(calendar);

        calendar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = calendar.rowAtPoint(e.getPoint());
                int col = calendar.columnAtPoint(e.getPoint());

                selectedRow = row;
                selectedCol = col;

                if (e.getClickCount() == 1 && row != -1) {
                    try {
                        if (timeSelectedHandler != null) {
                            timeSelectedHandler.onTimeSelected(getSelectedDate());
                        }
                    } catch (ParseException e1) {
                    }
                }
            }
        });
        createLayout();
    }

    public void updateWeekOffset() {
        Calendar cal = getWeekStart();

        cal.add(Calendar.DATE, weekOffset * 7);
        weekLabel.setText("Week " + cal.get(Calendar.WEEK_OF_YEAR));
        calendar.setModel(new CalendarModel());
        calendar.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    public void createLayout() {
        updateWeekOffset();
        weekLabel.setFont(new Font("Serif", Font.PLAIN, 20));

        SpringLayout layout = new SpringLayout();
        setLayout(layout);

        JTableHeader header = calendar.getTableHeader();

        add(prevButton);
        add(nextButton);
        add(weekLabel);
        add(header);
        add(container);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, weekLabel, 0, SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.EAST, prevButton, -15, SpringLayout.WEST, weekLabel);
        layout.putConstraint(SpringLayout.WEST, nextButton, 15, SpringLayout.EAST, weekLabel);

        layout.putConstraint(SpringLayout.NORTH, container, 20, SpringLayout.SOUTH, weekLabel);
        layout.putConstraint(SpringLayout.WEST, container, 0, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, container, 0, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.SOUTH, container, 0, SpringLayout.SOUTH, this);
        layout.putConstraint(SpringLayout.SOUTH, container, -20, SpringLayout.SOUTH, this);

        calendar.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    public class CalendarModel extends DefaultTableModel {
        @Override
        public int getRowCount() {
            // 9-to-5 work day, 30 minute intervals
            return (17 - 9) * 60 / 20;
        }

        @Override
        public String getColumnName(int column) {
            Calendar cal = getWeekStart();

            cal.add(Calendar.DATE, weekOffset * 7);
            cal.add(Calendar.DATE, column - 1);

            if (column == 0) {
                return "Time";
            } else {
                return new SimpleDateFormat("dd-MM-yyyy EEEEE").format(cal.getTime());
            }
        }

        @Override
        public int getColumnCount() {
            return 6;
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        @Override
        public Object getValueAt(int row, int column) {
            if (column == 0) { // Time
                int hour;
                int minute;

                if (row % 3 == 0) {
                    hour = 9 + row / 3;
                    minute = 0;
                } else if (row % 3 == 1) {
                    hour = 9 + row / 3;
                    minute = 2;
                } else {
                    hour = 9 + row / 3;
                    minute = 4;
                }

                return String.format("%d:%d0", hour, minute);
            } else { // Days of the week
                Calendar cal = getWeekStart();

                cal.add(Calendar.DATE, weekOffset * 7);
                cal.add(Calendar.DATE, column);

                cal.add(Calendar.HOUR_OF_DAY, row / 3);
                cal.add(Calendar.HOUR_OF_DAY, 9);

                if (row % 3 == 1) {
                    cal.add(Calendar.MINUTE, 20);
                } else if (row % 3 == 2) {
                    cal.add(Calendar.MINUTE, 40);
                }

                long timestamp = cal.getTime().getTime() / 1000;

                try {
                    if (user == null) {
                        Appointment a = Appointment.findAtTime(timestamp);
                        if (a != null) {
                				return a.getPatient().getFirstname().charAt(0) + "." + a.getPatient().getSurname() + " - " + a.getTreatment().getName();
                        } else {
                            return "";
                        }
                    } else {
                        Appointment a = Appointment.findAtTimeFiltered(timestamp, user);
                        if (a != null) {
                        		if (!(a.getPatient().getFirstname().equals("Holiday"))) {
                        			StringBuilder sb = new StringBuilder();
                        			if (a.getTreatmentDone())
                        				sb.append("DONE - ");
                        			sb.append(a.getPatient().getFirstname().substring(0, 1).toUpperCase() + ".");
                        			sb.append(" ");
                        			sb.append(a.getPatient().getSurname());
                        			sb.append("-");
                        			sb.append(a.getTreatment().getName());
                            		return sb.toString();
                        		}
                        		else {
                        			return a.getPartner().getName().substring(0, 1).toUpperCase() + a.getPartner().getName().substring(1) + " " + 
                    						a.getPatient().getFirstname();
                        		}
                    		} else {
                            return "";
                        }
                    }
                } catch (SQLException e) {
                }
            }
            return super.getValueAt(row, column);

        }
    }

    public static Calendar getWeekStart() {
        Calendar cal = Calendar.getInstance();

        // Set beginning of week
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            cal.add(Calendar.DATE, -1);
        }

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal;
    }

    public Date getSelectedDate() throws ParseException {
        String colName = calendar.getModel().getColumnName(selectedCol + 1);
        String timeName = (String) calendar.getModel().getValueAt(selectedRow, 0);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy EEEEE kk:mm");
        Date parsed = dateFormat.parse(colName + " " + timeName);
        return parsed;
    }

    public void setTimeSelectedHandler(TimeSelectedHandler th) {
        timeSelectedHandler = th;
    }
}
