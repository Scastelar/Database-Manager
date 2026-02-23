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
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import util.SqlEditor;

/**
 *
 * @author scastelar
 */
public class ObjectExplorerDialog extends JDialog{
    private JTree objectTree;
    private DefaultMutableTreeNode root;
    private JTextArea ddlArea;
    private Conexion conexion;
    
    public ObjectExplorerDialog(Frame parent, Conexion conexion) {
        super(parent, "Explorador de Objetos - " + conexion.getDatabase(), false);
        this.conexion = conexion;
        
        setSize(800, 600);
        setLocationRelativeTo(parent);
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Panel principal dividido
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(300);
        
        // Panel izquierdo: Árbol de objetos
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel titleLabel = new JLabel("Objetos de Base de Datos");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        leftPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Crear árbol
        root = new DefaultMutableTreeNode(conexion.getDatabase());
        objectTree = new JTree(root);
        objectTree.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JScrollPane treeScroll = new JScrollPane(objectTree);
        leftPanel.add(treeScroll, BorderLayout.CENTER);
        
        // Botón refrescar
        JButton refreshBtn = new JButton("Refrescar");
        styleButton(refreshBtn, new Color(33, 150, 243));
        refreshBtn.addActionListener(e -> cargarObjetosBaseDatos());
        leftPanel.add(refreshBtn, BorderLayout.SOUTH);
        
        // Panel derecho: DDL y acciones
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel ddlLabel = new JLabel("DDL del Objeto");
        ddlLabel.setFont(new Font("Arial", Font.BOLD, 14));
        rightPanel.add(ddlLabel, BorderLayout.NORTH);
        
        ddlArea = new JTextArea();
        ddlArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        ddlArea.setEditable(false);
        ddlArea.setLineWrap(true);
        ddlArea.setWrapStyleWord(true);
        
        JScrollPane ddlScroll = new JScrollPane(ddlArea);
        rightPanel.add(ddlScroll, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton exportBtn = new JButton("Exportar DDL");
        JButton copyBtn = new JButton("Copiar");
        JButton executeBtn = new JButton("Ejecutar en Editor");
        
        styleButton(exportBtn, new Color(76, 175, 80));
        styleButton(copyBtn, new Color(255, 152, 0));
        styleButton(executeBtn, new Color(156, 39, 176));
        
        exportBtn.addActionListener(e -> exportarDDL());
        copyBtn.addActionListener(e -> copiarDDL());
        executeBtn.addActionListener(e -> ejecutarEnEditor());
        
        buttonPanel.add(copyBtn);
        buttonPanel.add(exportBtn);
        buttonPanel.add(executeBtn);
        
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        
        add(splitPane, BorderLayout.CENTER);
        
        // Listener para selección en el árbol
        objectTree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selectedNode = 
                (DefaultMutableTreeNode) objectTree.getLastSelectedPathComponent();
            
            if (selectedNode != null && selectedNode.getUserObject() instanceof ObjetoBaseDatos) {
                ObjetoBaseDatos objeto = (ObjetoBaseDatos) selectedNode.getUserObject();
                mostrarDDL(objeto);
            }
        });
        
        // Cargar objetos al iniciar
        cargarObjetosBaseDatos();
    }
    
     private void cargarObjetosBaseDatos() {
        root.removeAllChildren();
        
        Thread loadThread = new Thread(() -> {
            // Tablas
            DefaultMutableTreeNode tablasNode = new DefaultMutableTreeNode("Tablas");
            List<String> tablas = SqlEditor.listarTablas(conexion);
            for (String tabla : tablas) {
                ObjetoBaseDatos obj = new ObjetoBaseDatos(tabla, "TABLA");
                tablasNode.add(new DefaultMutableTreeNode(obj));
            }
            root.add(tablasNode);
            
            // Vistas
            DefaultMutableTreeNode vistasNode = new DefaultMutableTreeNode("Vistas");
            List<String> vistas = SqlEditor.listarVistas(conexion);
            for (String vista : vistas) {
                ObjetoBaseDatos obj = new ObjetoBaseDatos(vista, "VISTA");
                vistasNode.add(new DefaultMutableTreeNode(obj));
            }
            root.add(vistasNode);
            
            // Procedimientos
            DefaultMutableTreeNode procsNode = new DefaultMutableTreeNode("Procedimientos");
            List<String> procs = SqlEditor.listarProcedimientos(conexion);
            for (String proc : procs) {
                ObjetoBaseDatos obj = new ObjetoBaseDatos(proc, "PROCEDIMIENTO");
                procsNode.add(new DefaultMutableTreeNode(obj));
            }
            root.add(procsNode);
            
            // Funciones
            DefaultMutableTreeNode funcsNode = new DefaultMutableTreeNode("Funciones");
            List<String> funcs = SqlEditor.listarFunciones(conexion);
            for (String func : funcs) {
                ObjetoBaseDatos obj = new ObjetoBaseDatos(func, "FUNCION");
                funcsNode.add(new DefaultMutableTreeNode(obj));
            }
            root.add(funcsNode);
            
            // Triggers
            DefaultMutableTreeNode triggersNode = new DefaultMutableTreeNode("Triggers");
            List<String> triggers = SqlEditor.listarTriggers(conexion);
            for (String trigger : triggers) {
                ObjetoBaseDatos obj = new ObjetoBaseDatos(trigger, "TRIGGER");
                triggersNode.add(new DefaultMutableTreeNode(obj));
            }
            root.add(triggersNode);
            
            // Secuencias
            DefaultMutableTreeNode seqsNode = new DefaultMutableTreeNode("Secuencias");
            List<String> seqs = SqlEditor.listarSecuencias(conexion);
            for (String seq : seqs) {
                ObjetoBaseDatos obj = new ObjetoBaseDatos(seq, "SECUENCIA");
                seqsNode.add(new DefaultMutableTreeNode(obj));
            }
            root.add(seqsNode);
            
            // Índices
            DefaultMutableTreeNode indicesNode = new DefaultMutableTreeNode("Indices");
            List<String> indices = SqlEditor.listarIndices(conexion);
            for (String indice : indices) {
                ObjetoBaseDatos obj = new ObjetoBaseDatos(indice, "INDICE");
                indicesNode.add(new DefaultMutableTreeNode(obj));
            }
            root.add(indicesNode);
            
            SwingUtilities.invokeLater(() -> {
                ((DefaultTreeModel) objectTree.getModel()).reload();
                expandirNodos();
            });
        });
        
        loadThread.start();
    }
    
    private void mostrarDDL(ObjetoBaseDatos objeto) {
        Thread ddlThread = new Thread(() -> {
            String ddl = "";
            
            switch (objeto.tipo) {
                case "TABLA":
                    ddl = SqlEditor.obtenerCreateTable(conexion, objeto.nombre);
                    break;
                case "VISTA":
                    ddl = SqlEditor.obtenerCreateView(conexion, objeto.nombre);
                    break;
                case "PROCEDIMIENTO":
                    ddl = SqlEditor.obtenerCreateProcedure(conexion, objeto.nombre);
                    break;
                case "FUNCION":
                    ddl = SqlEditor.obtenerCreateFunction(conexion, objeto.nombre);
                    break;
                case "TRIGGER":
                    ddl = SqlEditor.obtenerCreateTrigger(conexion, objeto.nombre);
                    break;
                case "SECUENCIA":
                    ddl = SqlEditor.obtenerCreateSequence(conexion, objeto.nombre);
                    break;
                case "INDICE":
                    ddl = "-- Ver información del índice en information_schema.STATISTICS\n" +
                          "-- Nombre: " + objeto.nombre;
                    break;
            }
            
            String finalDDL = ddl;
            SwingUtilities.invokeLater(() -> ddlArea.setText(finalDDL));
        });
        
        ddlThread.start();
    }
    
    private void expandirNodos() {
        for (int i = 0; i < objectTree.getRowCount(); i++) {
            objectTree.expandRow(i);
        }
    }
    
    private void exportarDDL() {
        if (ddlArea.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona un objeto primero");
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exportar DDL");
        fileChooser.setSelectedFile(new java.io.File("objeto.sql"));
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.FileWriter writer = new java.io.FileWriter(fileChooser.getSelectedFile());
                writer.write(ddlArea.getText());
                writer.close();
                
                JOptionPane.showMessageDialog(this, "DDL exportado correctamente");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al exportar: " + e.getMessage());
            }
        }
    }
    
    private void copiarDDL() {
        if (ddlArea.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona un objeto primero");
            return;
        }
        
        java.awt.datatransfer.StringSelection stringSelection = 
            new java.awt.datatransfer.StringSelection(ddlArea.getText());
        java.awt.datatransfer.Clipboard clipboard = 
            java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        
        JOptionPane.showMessageDialog(this, "DDL copiado al portapapeles");
    }
    
    private void ejecutarEnEditor() {
        if (ddlArea.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona un objeto primero");
            return;
        }
        
        // Esta funcionalidad se implementaría pasando el DDL al editor SQL
        JOptionPane.showMessageDialog(this, 
            "Funcionalidad disponible: Copia el DDL y pégalo en el editor SQL");
    }
    
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 11));
        button.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    // Clase interna para representar objetos
    private class ObjetoBaseDatos {
        String nombre;
        String tipo;
        
        public ObjetoBaseDatos(String nombre, String tipo) {
            this.nombre = nombre;
            this.tipo = tipo;
        }
        
        @Override
        public String toString() {
            return nombre;
        }
    }
}

