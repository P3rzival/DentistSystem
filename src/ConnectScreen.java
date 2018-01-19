import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

@SuppressWarnings("serial")
public class ConnectScreen extends JDialog {

    private JProgressBar progressBar = new JProgressBar();
    private JLabel status = new JLabel("Connecting to database...");

    public ConnectScreen() {
        createLayout();
        connectionCheck();
    }

    public void createLayout() {
        getContentPane().setLayout(new BorderLayout(3, 3));
        getContentPane().add(progressBar, BorderLayout.NORTH);
        getContentPane().add(status, BorderLayout.SOUTH);

        progressBar.setIndeterminate(true);

        setTitle("Connecting to dentistry database");
        setSize(400, 200);
        setResizable(false);
        setModal(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null); // Center window
    }

    public void connectionCheck() {
        new Thread(() -> {
            while (true) {
                try {
                    Database.getConnection();
                    break;
                } catch (Exception e) {
                    EventQueue.invokeLater(() -> {
                        String msg = e.getMessage();
                        String multiline = convertToMultiline(msg);
                        status.setText(multiline);
                    });
                }
            }
            dispose();
        }).start();
    }

    public static String convertToMultiline(String text) {
        StringBuilder sb = new StringBuilder("<html>");
        sb.append(text.replaceAll("\n", "<br>"));
        sb.append("</html>");
        return sb.toString();
    }
}
