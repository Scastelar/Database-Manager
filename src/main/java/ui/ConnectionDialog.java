package ui;

import Modelos.Conexion;
import dao.ConexionDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import util.DBManager;

public class ConnectionDialog extends JDialog {

    private JTextField nombreField;
    private JComboBox<String> tipoCombo;
    private JTextField hostField;
    private JTextField puertoField;
    private JTextField databaseField;
    private JTextField usuarioField;
    private JPasswordField passwordField;

    private Conexion conexionEditada;
    private boolean guardado = false;

    public ConnectionDialog(Frame parent, Conexion conexion) {
        super(parent, conexion == null ? "Nueva Conexión" : "Editar Conexión", true);
        this.conexionEditada = conexion;

        initComponents();

        if (conexion != null) {
            cargarDatos(conexion);
        }
    }

    private void initComponents() {
        setSize(450, 500);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(255, 241, 181));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Nombre de la conexión
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Nombre:"), gbc);

        gbc.gridx = 1;
        nombreField = new JTextField(20);
        mainPanel.add(nombreField, gbc);

        // Tipo de base de datos
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Tipo:"), gbc);

        gbc.gridx = 1;
        tipoCombo = new JComboBox<>(new String[]{"MySQL", "MariaDB", "PostgreSQL"});
        tipoCombo.addActionListener(e -> ajustarPuertoPorDefecto());
        mainPanel.add(tipoCombo, gbc);

        // Host
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Host:"), gbc);

        gbc.gridx = 1;
        hostField = new JTextField("localhost");
        mainPanel.add(hostField, gbc);

        // Puerto
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("Puerto:"), gbc);

        gbc.gridx = 1;
        puertoField = new JTextField("3306");
        mainPanel.add(puertoField, gbc);

        // Base de datos
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(new JLabel("Base de Datos:"), gbc);

        gbc.gridx = 1;
        databaseField = new JTextField();
        mainPanel.add(databaseField, gbc);

        // Usuario
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(new JLabel("Usuario:"), gbc);

        gbc.gridx = 1;
        usuarioField = new JTextField();
        mainPanel.add(usuarioField, gbc);

        // Contraseña
        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(new JLabel("Contraseña:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField();
        mainPanel.add(passwordField, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(255, 241, 181));

        JButton testBtn = new JButton("Probar Conexión");
        JButton saveBtn = new JButton("Guardar");
        JButton cancelBtn = new JButton("Cancelar");

        styleButton(testBtn, new Color(255, 152, 0));
        styleButton(saveBtn, new Color(76, 175, 80));
        styleButton(cancelBtn, new Color(158, 158, 158));

        testBtn.addActionListener(e -> probarConexion());
        saveBtn.addActionListener(e -> guardarConexion());
        cancelBtn.addActionListener(e -> dispose());

        buttonPanel.add(testBtn);
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void ajustarPuertoPorDefecto() {
        String tipo = (String) tipoCombo.getSelectedItem();
        if (tipo != null) {
            switch (tipo) {
                case "MySQL":
                case "MariaDB":
                    puertoField.setText("3306");
                    break;
                case "PostgreSQL":
                    puertoField.setText("5432");
                    break;
            }
        }
    }

    private void cargarDatos(Conexion conexion) {
        nombreField.setText(conexion.getNombre());
        tipoCombo.setSelectedItem(conexion.getTipo());
        hostField.setText(conexion.getHost());
        puertoField.setText(String.valueOf(conexion.getPuerto()));
        databaseField.setText(conexion.getDatabase());
        usuarioField.setText(conexion.getUsuario());
        passwordField.setText(conexion.getPassword());
    }

    private void probarConexion() {
    Conexion temp = crearConexionDesdeCampos();
    if (temp == null) return;

    // Debug en consola
    System.out.println("====== PROBANDO CONEXIÓN ======");
    System.out.println("Tipo: " + temp.getTipo());
    System.out.println("Host: " + temp.getHost());
    System.out.println("Puerto: " + temp.getPuerto());
    System.out.println("Base de datos: " + temp.getDatabase());
    System.out.println("Usuario: " + temp.getUsuario());
    System.out.println("JDBC URL: " + temp.getJDBC());
    System.out.println("================================");

    Thread testThread = new Thread(() -> {
        try {
            SwingUtilities.invokeLater(() -> 
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR))
            );
            
            boolean exito = DBManager.probarConexion(temp);
            
            // Restaurar cursor y mostrar resultado
            SwingUtilities.invokeLater(() -> {
                setCursor(Cursor.getDefaultCursor());
                
                if (exito) {
                    JOptionPane.showMessageDialog(ConnectionDialog.this,
                        "Conexión exitosa\n\n" +
                        "Servidor: " + temp.getHost() + ":" + temp.getPuerto() + "\n" +
                        "Tipo: " + temp.getTipo(),
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(ConnectionDialog.this,
                        "No se pudo conectar\n\n" +
                        "Verifica:\n" +
                        "• Servidor activo\n" +
                        "• Usuario y contraseña\n" +
                        "• Puerto " + temp.getPuerto(),
                        "Error de Conexión",
                        JOptionPane.ERROR_MESSAGE);
                }
            });
            
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> {
                setCursor(Cursor.getDefaultCursor());
                JOptionPane.showMessageDialog(ConnectionDialog.this,
                    "Error: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            });
            e.printStackTrace();
        }
    });
    
    testThread.start();
}

    private void guardarConexion() {
        Conexion conexion = crearConexionDesdeCampos();
        if (conexion == null) {
            return;
        }

        System.out.println("====== GUARDANDO CONEXIÓN ======");
        System.out.println("Nombre: " + conexion.getNombre());
        System.out.println("Tipo: " + conexion.getTipo());
        System.out.println("================================");

        ConexionDAO dao = new ConexionDAO();
        boolean exito;

        if (conexionEditada != null) {
            conexion.setId(conexionEditada.getId());
            exito = dao.actualizarConexion(conexion);
        } else {
            exito = dao.guardarConexion(conexion);
        }

        if (exito) {
            guardado = true;
            JOptionPane.showMessageDialog(this,
                    "Conexion guardada correctamente\n\n"
                    + "Nombre: " + conexion.getNombre(), "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar la conexión\n\n"
                    + "Revisa la consola para más detalles","Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private Conexion crearConexionDesdeCampos() {
        if (nombreField.getText().trim().isEmpty()
                || hostField.getText().trim().isEmpty()
                || usuarioField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos obligatorios");
            return null;
        }

        try {
            int puerto = Integer.parseInt(puertoField.getText());
            return new Conexion(
                    nombreField.getText().trim(),
                    ((String) tipoCombo.getSelectedItem()).toLowerCase(),
                    hostField.getText().trim(),
                    puerto,
                    databaseField.getText().trim(),
                    usuarioField.getText().trim(),
                    new String(passwordField.getPassword())
            );
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El puerto debe ser un número válido");
            return null;
        }
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public boolean isGuardado() {
        return guardado;
    }
}
