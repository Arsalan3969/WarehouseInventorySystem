package gui;

import dao.LoginDAO;
import model.LoggedInUser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginForm extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginForm() {

        setTitle("Warehouse Inventory System");
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Color background = new Color(22, 24, 42);
        Color cardColor = new Color(40, 43, 61);
        Color buttonColor = new Color(45, 108, 223);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(background);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(cardColor);
        card.setPreferredSize(new Dimension(700, 450));
        card.setBorder(new EmptyBorder(35, 50, 35, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ================= TITLE =================

        JLabel title = new JLabel("Warehouse Inventory System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        card.add(title, gbc);

        JLabel subtitle = new JLabel("Inventory Management Login");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitle.setForeground(new Color(200, 200, 200));
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 12, 25, 12);
        card.add(subtitle, gbc);

        // ================= USERNAME =================

        JLabel userLabel = new JLabel("Username");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        gbc.insets = new Insets(10, 12, 10, 12);
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        card.add(userLabel, gbc);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        usernameField.setPreferredSize(new Dimension(380, 42));

        gbc.gridx = 1;
        gbc.weightx = 1;
        card.add(usernameField, gbc);

        // ================= PASSWORD =================

        JLabel passLabel = new JLabel("Password");
        passLabel.setForeground(Color.WHITE);
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.weightx = 0;
        card.add(passLabel, gbc);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        passwordField.setPreferredSize(new Dimension(380, 42));

        gbc.gridx = 1;
        gbc.weightx = 1;
        card.add(passwordField, gbc);

        // ================= LOGIN BUTTON =================

        loginButton = new JButton("LOGIN");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginButton.setBackground(buttonColor);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(35, 170, 10, 170);
        card.add(loginButton, gbc);

        mainPanel.add(card);
        add(mainPanel);

        // ================= LOGIN ACTION =================

        loginButton.addActionListener(e -> {

            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            LoginDAO loginDAO = new LoginDAO();

            LoggedInUser user = loginDAO.login(username, password);

            if (user != null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Welcome, " + user.getFullName() + "!"
                );

                dispose();

                Dashboard dashboard = new Dashboard(user);
                dashboard.setVisible(true);

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid Username or Password!"
                );

            }

        });

    }

    public JTextField getUsernameField() {
        return usernameField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JButton getLoginButton() {
        return loginButton;
    }
}