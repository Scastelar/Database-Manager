package ui;

import Modelos.Conexion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import util.DBManager;

public class CreateDatabaseDialog extends JDialog {
    
    private JTextField nombreDBField;
    private JTextArea infoArea;
    private Conexion conexion;
    private boolean creado = false;

    public CreateDatabaseDialog(Frame parent, Conexion conexion) {
        super(parent, "Crear Base de Datos", true);
        this.conexion = conexion;
        
        initComponents();
    }

    private void initComponents() {
        setSize(400, 300);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(255, 241, 181));

        // Info de conexión
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder("Conexión Actual"));
        infoArea = new JTextArea(3, 20);
        infoArea.setEditable(false);
        infoArea.setText("Servidor: " + conexion.getHost() + ":" + conexion.getPuerto() +
                        "\nTipo: " + conexion.getTipo() +
                        "\nUsuario: " + conexion.getUsuario());
        infoPanel.add(new JScrollPane(infoArea));

        // Campo para nombre de BD
        JPanel inputPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        inputPanel.add(new JLabel("Nombre de la base de datos:"));
        nombreDBField = new JTextField();
        inputPanel.add(nombreDBField);

        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(inputPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(255, 241, 181));

        JButton createBtn = new JButton("Crear");
        JButton cancelBtn = new JButton("Cancelar");

        styleButton(createBtn, new Color(76, 175, 80));
        styleButton(cancelBtn, new Color(158, 158, 158));

        createBtn.addActionListener(e -> crearBaseDatos());
        cancelBtn.addActionListener(e -> dispose());

        buttonPanel.add(createBtn);
        buttonPanel.add(cancelBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void crearBaseDatos() {
        String nombreDB = nombreDBField.getText().trim();
        
        if (nombreDB.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa un nombre para la base de datos");
            return;
        }

        // Validar nombre (solo letras, números y guiones bajos)
        if (!nombreDB.matches("[a-zA-Z0-9_]+")) {
            JOptionPane.showMessageDialog(this, 
                "El nombre solo puede contener letras, números y guiones bajos",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog loadingDialog = new JDialog(this, "Creando...", true);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        loadingDialog.add(progressBar);
        loadingDialog.setSize(200, 80);
        loadingDialog.setLocationRelativeTo(this);

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() {
                return DBManager.crearBaseDatos(conexion, nombreDB);
            }

            @Override
            protected void done() {
                loadingDialog.dispose();
                try {
                    boolean exito = get();
                    if (exito) {
                        creado = true;
                        JOptionPane.showMessageDialog(CreateDatabaseDialog.this,
                            "✓ Base de datos '" + nombreDB + "' creada exitosamente",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(CreateDatabaseDialog.this,
                            "✗ No se pudo crear la base de datos",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(CreateDatabaseDialog.this,
                        "Error: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
        loadingDialog.setVisible(true);
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public boolean isCreado() {
        return creado;
    }
}