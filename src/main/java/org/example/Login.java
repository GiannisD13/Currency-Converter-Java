package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Login extends JFrame {
    private JPanel LoginPanel;
    private JLabel LOG;
    private JButton signUpButton;
    private JPanel panel1;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton logInButton;

    public Login() {
        setContentPane(LoginPanel);
        setTitle("Registration");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 350);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    private void createUIComponents() {
        signUpButton = new JButton("Sign Up");
        signUpButton.addActionListener(e -> {
            dispose();
            new Registration();
        });
        logInButton = new JButton("Log In");
        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserManager userManager = new UserManager();
                String username = textField1.getText();
                String password = new String(passwordField1.getPassword());

                List<User> users = userManager.loadUsers();

                boolean found = false;

                for (User user : users) {
                    if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                        JOptionPane.showMessageDialog(null, "Welcome back " + username);
                        dispose();
                        ConverterFrame gui = new ConverterFrame(new Requests(), user);
                        found = true;
                        break;
                    }

                    }
                if(!found) {
                    JOptionPane.showMessageDialog(null, "Incorrect username or password");
                    return;
                }
            }
        });

    }
}
