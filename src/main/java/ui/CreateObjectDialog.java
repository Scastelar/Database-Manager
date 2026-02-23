/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import Modelos.Conexion;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import util.ResultadoSql;
import util.SqlEditor;

/**
 *
 * @author scastelar
 */
public class CreateObjectDialog extends JDialog {
    private JComboBox<String> tipoCombo;
    private JTextArea ddlArea;
    private Conexion conexion;
    private boolean creado = false;
    
    public CreateObjectDialog(Frame parent, Conexion conexion){
        super(parent, "Crear Objeto",true);
        this.conexion = conexion;
        
        setSize(700,500);
        setLocationRelativeTo(parent);
        initComponents();
    }
    
    
     private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(255, 241, 181));
        
        // Panel superior: Selección de tipo
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topPanel.setBackground(new Color(255, 241, 181));
        
        JLabel tipoLabel = new JLabel("Tipo de Objeto:");
        tipoLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        tipoCombo = new JComboBox<>(new String[]{
            "Tabla",
            "Vista",
            "Procedimiento",
            "Función",
            "Trigger",
            "Secuencia",
            "Índice",
            "Usuario"
        });
        
        tipoCombo.addActionListener(e -> cargarPlantilla());
        
        JButton templateBtn = new JButton("Cargar Plantilla");
        styleButton(templateBtn, new Color(33, 150, 243));
        templateBtn.addActionListener(e -> cargarPlantilla());
        
        topPanel.add(tipoLabel);
        topPanel.add(tipoCombo);
        topPanel.add(templateBtn);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Panel central: Editor DDL
        JPanel editorPanel = new JPanel(new BorderLayout(5, 5));
        editorPanel.setBorder(BorderFactory.createTitledBorder("DDL - Edita el codigo SQL"));
        editorPanel.setBackground(new Color(255, 241, 181));
        
        ddlArea = new JTextArea();
        ddlArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        ddlArea.setTabSize(4);
        ddlArea.setLineWrap(false);
        
        JScrollPane scrollPane = new JScrollPane(ddlArea);
        editorPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(editorPanel, BorderLayout.CENTER);
        
        // Panel inferior: Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setBackground(new Color(255, 241, 181));
        
        JButton createBtn = new JButton("Crear Objeto");
        JButton validateBtn = new JButton("Validar SQL");
        JButton cancelBtn = new JButton("Cancelar");
        
        styleButton(createBtn, new Color(76, 175, 80));
        styleButton(validateBtn, new Color(255, 152, 0));
        styleButton(cancelBtn, new Color(158, 158, 158));
        
        createBtn.addActionListener(e -> crearObjeto());
        validateBtn.addActionListener(e -> validarSQL());
        cancelBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(validateBtn);
        buttonPanel.add(createBtn);
        buttonPanel.add(cancelBtn);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Cargar plantilla inicial
        cargarPlantilla();
    }
    
    private void cargarPlantilla() {
        String tipo = (String) tipoCombo.getSelectedItem();
        String plantilla = "";
        
        switch (tipo) {
            case "Tabla":
                plantilla = "CREATE TABLE nombre_tabla (\n" +
                           "    id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                           "    columna1 VARCHAR(100) NOT NULL,\n" +
                           "    columna2 INT DEFAULT 0,\n" +
                           "    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                           "    INDEX idx_columna1 (columna1)\n" +
                           ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";
                break;
                
            case "Vista":
                plantilla = "CREATE VIEW nombre_vista AS\n" +
                           "SELECT \n" +
                           "    t.columna1,\n" +
                           "    t.columna2\n" +
                           "FROM tabla t\n" +
                           "WHERE condicion = 1;";
                break;
                
            case "Procedimiento":
                plantilla = "DELIMITER //\n\n" +
                           "CREATE PROCEDURE nombre_procedimiento(\n" +
                           "    IN parametro1 INT,\n" +
                           "    OUT parametro2 VARCHAR(100)\n" +
                           ")\n" +
                           "BEGIN\n" +
                           "    -- Lógica del procedimiento\n" +
                           "    SELECT columna INTO parametro2\n" +
                           "    FROM tabla\n" +
                           "    WHERE id = parametro1;\n" +
                           "END//\n\n" +
                           "DELIMITER ;";
                break;
                
            case "Función":
                plantilla = "DELIMITER //\n\n" +
                           "CREATE FUNCTION nombre_funcion(parametro INT)\n" +
                           "RETURNS VARCHAR(100)\n" +
                           "DETERMINISTIC\n" +
                           "BEGIN\n" +
                           "    DECLARE resultado VARCHAR(100);\n" +
                           "    \n" +
                           "    -- Lógica de la función\n" +
                           "    SELECT columna INTO resultado\n" +
                           "    FROM tabla\n" +
                           "    WHERE id = parametro;\n" +
                           "    \n" +
                           "    RETURN resultado;\n" +
                           "END//\n\n" +
                           "DELIMITER ;";
                break;
                
            case "Trigger":
                plantilla = "DELIMITER //\n\n" +
                           "CREATE TRIGGER nombre_trigger\n" +
                           "BEFORE INSERT ON nombre_tabla\n" +
                           "FOR EACH ROW\n" +
                           "BEGIN\n" +
                           "    -- Lógica del trigger\n" +
                           "    SET NEW.fecha_modificacion = NOW();\n" +
                           "END//\n\n" +
                           "DELIMITER ;";
                break;
                
            case "Secuencia":
                plantilla = "-- MariaDB 10.3+\n" +
                           "CREATE SEQUENCE nombre_secuencia\n" +
                           "START WITH 1\n" +
                           "INCREMENT BY 1\n" +
                           "MINVALUE 1\n" +
                           "MAXVALUE 9999999999\n" +
                           "CACHE 1000;";
                break;
                
            case "Índice":
                plantilla = "-- Índice simple\n" +
                           "CREATE INDEX idx_nombre_columna\n" +
                           "ON nombre_tabla (columna);\n\n" +
                           "-- Índice compuesto\n" +
                           "CREATE INDEX idx_columnas_multiples\n" +
                           "ON nombre_tabla (columna1, columna2);\n\n" +
                           "-- Índice único\n" +
                           "CREATE UNIQUE INDEX idx_unico\n" +
                           "ON nombre_tabla (columna_unica);\n\n" +
                           "-- Índice FULLTEXT\n" +
                           "CREATE FULLTEXT INDEX idx_texto\n" +
                           "ON nombre_tabla (columna_texto);";
                break;
                
            case "Usuario":
                plantilla = "-- Crear usuario\n" +
                           "CREATE USER 'nombre_usuario'@'localhost'\n" +
                           "IDENTIFIED BY 'contraseña_segura';\n\n" +
                           "-- Otorgar permisos\n" +
                           "GRANT SELECT, INSERT, UPDATE, DELETE\n" +
                           "ON base_de_datos.*\n" +
                           "TO 'nombre_usuario'@'localhost';\n\n" +
                           "-- Aplicar cambios\n" +
                           "FLUSH PRIVILEGES;";
                break;
        }
        
        ddlArea.setText(plantilla);
        ddlArea.setCaretPosition(0);
    }
    
    private void validarSQL() {
        String sql = ddlArea.getText().trim();
        
        if (sql.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escribe el SQL primero");
            return;
        }
        
        // Validación básica de sintaxis
        if (sql.toUpperCase().contains("CREATE")) {
            JOptionPane.showMessageDialog(this,
                "✓ El SQL parece válido\n\n" +
                "NOTA: La validación completa se hará al ejecutar",
                "Validación",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "⚠ El SQL no contiene una instrucción CREATE\n\n" +
                "Verifica que sea correcto antes de ejecutar",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void crearObjeto() {
        String sql = ddlArea.getText().trim();
        
        if (sql.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escribe el DDL del objeto");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Ejecutar el siguiente SQL?\n\n" + 
            (sql.length() > 200 ? sql.substring(0, 200) + "..." : sql),
            "Confirmar Creación",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        Thread createThread = new Thread(() -> {
            try {
                SwingUtilities.invokeLater(() -> 
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR))
                );
                
                ResultadoSql resultado = SqlEditor.ejecutarConsulta(conexion, sql);
                
                SwingUtilities.invokeLater(() -> {
                    setCursor(Cursor.getDefaultCursor());
                    
                    if (resultado.esExitoso) {
                        creado = true;
                        JOptionPane.showMessageDialog(CreateObjectDialog.this,
                            "✓ Objeto creado exitosamente\n\n" + resultado.mensaje,
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(CreateObjectDialog.this,
                            "Error al crear objeto:\n\n" + resultado.mensaje,
                            "Error SQL",
                            JOptionPane.ERROR_MESSAGE);
                    }
                });
                
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    setCursor(Cursor.getDefaultCursor());
                    JOptionPane.showMessageDialog(CreateObjectDialog.this,
                        "Error: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                });
                e.printStackTrace();
            }
        });
        
        createThread.start();
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
