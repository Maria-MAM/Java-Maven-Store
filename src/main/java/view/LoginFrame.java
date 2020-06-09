package view;

import model.User;
import service.MainService;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class LoginFrame extends JFrame{
    private JPanel panel;
    private JButton loginButton;
    private JButton registerButton;
    private JTextField textField1;
    private JLabel labelUsername;
    private JLabel passLabel;
    private JPasswordField passwordField1;
    public static User userActual = new User();

    public LoginFrame() {
        add(panel);
        loginButton.addActionListener(ev -> login());
        registerButton.addActionListener(ev -> register());
        setLocationRelativeTo(null);
        setSize(400, 400);
        setVisible(true);
        // pentru a pozitiona fereastra mea exact in centru ecranului
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        // pentru a inchide procesul cand inchid frame-ul
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void login() {
        String username = textField1.getText();
        String parola = new String(passwordField1.getPassword());
        Optional<User> user = MainService.getInstance().login(username, parola);
        if (user.isPresent()) {
            userActual = user.get();
            new MainFrame();
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, "User sau parola gresite!");
        }
        cleanFields(textField1, passwordField1);
    }

    public void register() {
        String username = textField1.getText();
        String parola = new String(passwordField1.getPassword());
        User user = new User(username, parola);
        if (MainService.getInstance().inregistrare(user)) {
            JOptionPane.showMessageDialog(null, "Userul a fost inregistrat!");
        } else {
            JOptionPane.showMessageDialog(null, "Userul nu a fost inregistrat!");
        }
        cleanFields(textField1, passwordField1);
    }

    public void cleanFields(JTextField textField, JPasswordField passField) {
        textField.setText("");
        passField.setText("");
    }
}
