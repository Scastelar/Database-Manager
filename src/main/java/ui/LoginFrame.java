package ui;

import static util.DBConnection.getConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("Login");
        setSize(250, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5,1,5,5));

        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton loginBtn = new JButton("Entrar");
        JButton registerBtn = new JButton("Crear cuenta");
        JLabel message = new JLabel("", SwingConstants.CENTER);

        panel.add(new JLabel("Usuario"));
        panel.add(userField);
        panel.add(new JLabel("Contraseña"));
        panel.add(passField);
        panel.add(loginBtn);
        panel.add(registerBtn);
        panel.add(message);

        add(panel);

        loginBtn.addActionListener(e -> {
            try {
                Connection conn = getConnection();
                String sql = "SELECT * FROM usuarios WHERE username=? AND password_hash=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, userField.getText());
                ps.setString(2, new String(passField.getPassword()));

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    message.setText("Login correcto");
                } else {
                    message.setText("Credenciales incorrectas");
                }

                conn.close();

            } catch (Exception ex) {
                message.setText("Error conexión");
            }
        });

        setVisible(true);
    }
}
