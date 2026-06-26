package main;

import gui.LoginForm;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            LoginForm login = new LoginForm();
            login.setVisible(true);

        });

    }

}