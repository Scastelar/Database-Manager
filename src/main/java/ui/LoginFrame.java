package ui;

import static util.DBConnection.getConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.border.EmptyBorder;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("Login");
        setSize(300, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //FAF0CA
        JPanel panel = new JPanel(new GridLayout(8, 5, 5, 10));
        panel.setBackground(new Color(193, 219, 232)); //0D3B66
        panel.setForeground(new Color(67, 48, 46));
        panel.setBorder(new EmptyBorder(10, 30, 10, 30));

        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton loginBtn = new JButton("Entrar");
        JButton registerBtn = new JButton("Crear cuenta");
        JLabel message = new JLabel("", SwingConstants.CENTER);

        loginBtn.setBackground(new Color(255, 241, 181));
        registerBtn.setBackground(new Color(193, 219, 232));
        registerBtn.setBorder(null);

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

        registerBtn.addActionListener(e -> {
            dispose();
            new RegisterFrame();
        });

        setVisible(true);
    }
}
