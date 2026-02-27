package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Registration extends JFrame {

    private JPanel RegisterPanel;
    private JLabel LOG;
    private JButton logInButton;
    private JPanel panel1;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton registerButton;

    public Registration() {
        setContentPane(RegisterPanel);
        setTitle("Registration");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 350);
        setLocationRelativeTo(null);
        setVisible(true);

    }


    private void createUIComponents() {
        logInButton = new JButton("Log In");
        logInButton.addActionListener(e -> {
            dispose();
            new Login();
        });
        registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserManager userManager = new UserManager();
                String username = textField1.getText();
                String password = new String(passwordField1.getPassword());

                List<User> users = userManager.loadUsers();

                for (User user : users) {
                    if(user.getUsername().equals(username)){
                        JOptionPane.showMessageDialog(null,"Username already exists.");
                        return;
                    }
                }
                User newUser = new User(username,password);
                users.add(newUser);

                userManager.saveUsers(users);
                JOptionPane.showMessageDialog(null, "User registered successfully!");
                dispose();
                ConverterFrame gui = new ConverterFrame(new Requests(),newUser);


            }
        });
    }
}

