import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

@SuppressWarnings("serial")
public class WelcomeScreen extends JDialog {

    private JTextField usernameBox = new JTextField();
    private JPasswordField passwordBox = new JPasswordField();
    private JButton loginButton = new JButton("Log in");
    private User user;

    public WelcomeScreen() {
        createLayout();
    }

    public void createLayout() {
        Container contentPane = getContentPane();

        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        JLabel titleLabel = new JLabel("Please enter your details");
        JLabel userLabel = new JLabel("Username: ");
        JLabel passLabel = new JLabel("Password: ");
        loginButton = new JButton("Log in");

        contentPane.add(titleLabel);
        contentPane.add(userLabel);
        contentPane.add(passLabel);
        contentPane.add(usernameBox);
        contentPane.add(passwordBox);
        contentPane.add(loginButton);

        layout.putConstraint(SpringLayout.NORTH, titleLabel, 10, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, titleLabel, 0, SpringLayout.HORIZONTAL_CENTER, contentPane);
        layout.putConstraint(SpringLayout.NORTH, userLabel, 20, SpringLayout.SOUTH, titleLabel);
        layout.putConstraint(SpringLayout.WEST, userLabel, 15, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, passLabel, 10, SpringLayout.SOUTH, userLabel);
        layout.putConstraint(SpringLayout.WEST, passLabel, 15, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.WEST, usernameBox, 10, SpringLayout.EAST, userLabel);
        layout.putConstraint(SpringLayout.NORTH, usernameBox, 0, SpringLayout.NORTH, userLabel);
        layout.putConstraint(SpringLayout.EAST, usernameBox, -10, SpringLayout.EAST, contentPane);
        layout.putConstraint(SpringLayout.WEST, passwordBox, 0, SpringLayout.WEST, usernameBox);
        layout.putConstraint(SpringLayout.NORTH, passwordBox, 0, SpringLayout.NORTH, passLabel);
        layout.putConstraint(SpringLayout.EAST, passwordBox, -10, SpringLayout.EAST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, loginButton, 15, SpringLayout.SOUTH, passLabel);
        layout.putConstraint(SpringLayout.WEST, loginButton, 15, SpringLayout.WEST, contentPane);

        setTitle("Sheffield Dentistry");
        setSize(350, 180);
        setResizable(false);
        setModal(true);
        setLocationRelativeTo(null); // Center window

        KeyListener kl = new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        checkLogin();
                    } catch (SQLException e1) {
                    } // Enter Button to login
                }

            }
        };

        usernameBox.addKeyListener(kl);
        passwordBox.addKeyListener(kl);

        loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    checkLogin();
                } catch (SQLException e1) {
                }
            }
        });
    }

    private void checkLogin() throws SQLException {
        String username = usernameBox.getText();
        String password = passwordBox.getText();

        user = new User(username);
        if (password.equals(user.getPassword())) {
            System.out.println("password correct");
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, "My Goodness, your password is incorrect");
        }
    }

    public User getUser() {
        return user;
    }
}
