package ui;

import util.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.border.EmptyBorder;

public class RegisterFrame extends JFrame {

    public RegisterFrame() {

        setTitle("Crear Cuenta");
        setSize(300, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(10,5,5,5));
        panel.setBackground(new Color(193, 219, 232)); //0D3B66
        panel.setForeground(new Color(67, 48, 46));
        panel.setBorder(new EmptyBorder(10, 30, 0, 30));

        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JPasswordField confirmField = new JPasswordField();

        JButton registerBtn = new JButton("Registrarse");
        JButton backBtn = new JButton("Volver al Login");
        
        registerBtn.setBackground(new Color(255, 241, 181));
        backBtn.setBackground(new Color(193, 219, 232));
        backBtn.setBorder(null);

        JLabel message = new JLabel("", SwingConstants.CENTER);

        panel.add(new JLabel("Usuario"));
        panel.add(userField);
        panel.add(new JLabel("Contraseña"));
        panel.add(passField);
        panel.add(new JLabel("Confirmar contraseña"));
        panel.add(confirmField);
        panel.add(registerBtn);
        panel.add(backBtn);
        panel.add(message);

        add(panel);

        registerBtn.addActionListener(e -> {

            String username = userField.getText();
            String password = new String(passField.getPassword());
            String confirm = new String(confirmField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                message.setText("Campos vacíos");
                return;
            }

            if (!password.equals(confirm)) {
                message.setText("Las contraseñas no coinciden");
                return;
            }

            try {
                Connection conn = DBConnection.getConnection();

                String sql = "INSERT INTO usuarios (username, password_hash) VALUES (?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);

                ps.setString(1, username);
                ps.setString(2, password); 

                ps.executeUpdate();

                message.setText("Cuenta creada correctamente");

            } catch (Exception ex) {
                message.setText("Usuario ya existe o error DB");
            }
        });

        backBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        setVisible(true);
    }
}
