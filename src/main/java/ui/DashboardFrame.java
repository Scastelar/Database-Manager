package ui;

import Modelos.Conexion;
import dao.ConexionDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Arrays;
import javax.swing.table.DefaultTableModel;
import util.DBManager;
import util.Session;

public class DashboardFrame extends JFrame {

    private JPanel contentPanel;
    private CardLayout cardLayout;

    // Lista de bases de datos del sistema que no se pueden modificar
    private static final java.util.List<String> SYSTEM_DATABASES = Arrays.asList(
            "information_schema",
            "mysql",
            "performance_schema",
            "sys"
    );

    public DashboardFrame(String username) {

        setTitle("Dashboard - " + username);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setBackground(new Color(193, 219, 232));

        // toolbar 
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(80, getHeight()));
        sidebar.setLayout(new GridLayout(4, 1, 10, 10));
        sidebar.setBackground(new Color(193, 219, 232));

        JButton connectionsBtn = createIconButton("src\\main\\java\\icons\\gestion-de-bases-de-datos.png", "Gestor de Conexiones");
        JButton sqlBtn = createIconButton("src\\main\\java\\icons\\servidor-sql.png", "Editor SQL");
        JButton logoutBtn = createIconButton("src\\main\\java\\icons\\cerrar-sesion.png", "Cerrar sesión");

        sidebar.add(connectionsBtn);
        sidebar.add(sqlBtn);
        sidebar.add(logoutBtn);

        add(sidebar, BorderLayout.WEST);

        // panel del centro
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        JPanel connectionsPanel = createConnectionsPanel();
        JPanel sqlPanel = createSqlPanel();

        contentPanel.add(connectionsPanel, "CONNECTIONS");
        contentPanel.add(sqlPanel, "SQL");

        add(contentPanel, BorderLayout.CENTER);
        connectionsPanel.setBackground(new Color(255, 241, 181));
        sqlPanel.setBackground(new Color(255, 241, 181));

        connectionsBtn.addActionListener(e -> cardLayout.show(contentPanel, "CONNECTIONS"));
        sqlBtn.addActionListener(e -> cardLayout.show(contentPanel, "SQL"));

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        setVisible(true);
    }

    // crear botones
    private JButton createIconButton(String iconPath, String tooltip) {

        ImageIcon icon = new ImageIcon(iconPath);
        Image scaled = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);

        JButton button = new JButton(new ImageIcon(scaled));
        button.setToolTipText(tooltip);

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);

        return button;
    }

    //verificar si es una base de datos del sistema
    private boolean esBaseDatosSistema(String nombreBD) {
        return SYSTEM_DATABASES.contains(nombreBD.toLowerCase());
    }

    // PANELES INTERNOS
    private JPanel createConnectionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Título
        JLabel titleLabel = new JLabel("Gestor de Conexiones", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Panel central con la lista de conexiones
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));

        // Modelo de lista para las conexiones
        DefaultListModel<Conexion> connectionListModel = new DefaultListModel<>();
        ConexionDAO dao = new ConexionDAO();

        // Cargar conexiones desde la base de datos
        for (Conexion c : dao.obtenerConexionesUsuario()) {
            connectionListModel.addElement(c);
        }

        JList<Conexion> connectionList = new JList<>(connectionListModel);
        connectionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        connectionList.setFont(new Font("Arial", Font.PLAIN, 14));
        connectionList.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JScrollPane scrollPane = new JScrollPane(connectionList);
        scrollPane.setBorder(new TitledBorder("Conexiones Disponibles"));

        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel de botones a la derecha
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 1, 5, 10));
        buttonPanel.setBorder(new EmptyBorder(0, 10, 0, 0));

        JButton newConnectionBtn = new JButton("Nueva Conexión");
        JButton editConnectionBtn = new JButton("Editar");
        JButton deleteConnectionBtn = new JButton("Eliminar");
        JButton testConnectionBtn = new JButton("Probar Conexión");
        JButton connectBtn = new JButton("Conectar");

        // Estilo de botones
        styleButton(newConnectionBtn, new Color(76, 175, 80));
        styleButton(editConnectionBtn, new Color(33, 150, 243));
        styleButton(deleteConnectionBtn, new Color(244, 67, 54));
        styleButton(testConnectionBtn, new Color(255, 152, 0));
        styleButton(connectBtn, new Color(156, 39, 176));

        buttonPanel.add(newConnectionBtn);
        buttonPanel.add(editConnectionBtn);
        buttonPanel.add(deleteConnectionBtn);
        buttonPanel.add(testConnectionBtn);
        buttonPanel.add(connectBtn);

        centerPanel.add(buttonPanel, BorderLayout.EAST);
        panel.add(centerPanel, BorderLayout.CENTER);

        // Panel inferior con información de la conexión seleccionada
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(new TitledBorder("Detalles de Conexión"));
        infoPanel.setPreferredSize(new Dimension(0, 100));

        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setText("Selecciona una conexión para ver los detalles");
        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        infoPanel.add(new JScrollPane(infoArea), BorderLayout.CENTER);

        panel.add(infoPanel, BorderLayout.SOUTH);
        // LISTENERS
        newConnectionBtn.addActionListener(e -> {
            ConnectionDialog dialog = new ConnectionDialog(this, null);
            dialog.setVisible(true);
            if (dialog.isGuardado()) {
                // Recargar lista
                connectionListModel.clear();
                for (Conexion c : dao.obtenerConexionesUsuario()) {
                    connectionListModel.addElement(c);
                }
            }
        });

        editConnectionBtn.addActionListener(e -> {
            Conexion selected = connectionList.getSelectedValue();
            if (selected != null) {
                ConnectionDialog dialog = new ConnectionDialog(this, selected);
                dialog.setVisible(true);
                if (dialog.isGuardado()) {
                    // Recargar lista
                    connectionListModel.clear();
                    for (Conexion c : dao.obtenerConexionesUsuario()) {
                        connectionListModel.addElement(c);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona una conexión primero");
            }
        });

        deleteConnectionBtn.addActionListener(e -> {
            Conexion selected = connectionList.getSelectedValue();
            if (selected != null) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "¿Eliminar la conexión '" + selected.getNombre() + "'?",
                        "Confirmar",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (dao.eliminarConexion(selected.getId())) {
                        connectionListModel.removeElement(selected);
                        JOptionPane.showMessageDialog(this, "Conexión eliminada");
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al eliminar");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona una conexión primero");
            }
        });

        testConnectionBtn.addActionListener(e -> {
            Conexion selected = connectionList.getSelectedValue();
            if (selected != null) {
                if (DBManager.probarConexion(selected)) {
                    JOptionPane.showMessageDialog(this, "Conexión exitosa");
                } else {
                    JOptionPane.showMessageDialog(this, "Error de conexión", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona una conexión primero");
            }
        });

        connectBtn.addActionListener(e -> {
            Conexion selected = connectionList.getSelectedValue();
            if (selected != null) {
                Session.conexionActiva = selected;
                JOptionPane.showMessageDialog(this, "Conectado a: " + selected.getNombre());
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona una conexión primero");
            }
        });

        connectionList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && connectionList.getSelectedValue() != null) {
                Conexion c = connectionList.getSelectedValue();
                infoArea.setText("Nombre: " + c.getNombre()
                        + "\nTipo: " + c.getTipo()
                        + "\nHost: " + c.getHost() + ":" + c.getPuerto()
                        + "\nBase de datos: " + (c.getDatabase().isEmpty() ? "(ninguna)" : c.getDatabase())
                        + "\nUsuario: " + c.getUsuario());
            }
        });

        return panel;
    }

    private JPanel createSqlPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Panel superior con título, info de conexión y selector de BD
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));

        JLabel titleLabel = new JLabel("Editor SQL", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        // Panel con info de conexión y selector de BD
        JPanel connectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JLabel connectionInfoLabel = new JLabel("Sin conexión", SwingConstants.CENTER);
        connectionInfoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        connectionInfoLabel.setForeground(Color.RED);

        JLabel dbLabel = new JLabel("Base de datos:");
        JComboBox<String> databaseComboBox = new JComboBox<>();
        databaseComboBox.setPreferredSize(new Dimension(200, 25));
        databaseComboBox.setEnabled(false);

        JButton refreshDBBtn = new JButton("refrescar");
        refreshDBBtn.setPreferredSize(new Dimension(40, 25));
        refreshDBBtn.setEnabled(false);
        styleButton(refreshDBBtn, new Color(33, 150, 243));

        connectionPanel.add(connectionInfoLabel);
        connectionPanel.add(new JSeparator(SwingConstants.VERTICAL));
        connectionPanel.add(dbLabel);
        connectionPanel.add(databaseComboBox);
        connectionPanel.add(refreshDBBtn);

        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(connectionPanel, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);

        // Panel principal con división horizontal: explorador | editor
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setDividerLocation(200);
        mainSplitPane.setResizeWeight(0.2);

        // PANEL IZQUIERDO: Explorador de tablas
        JPanel explorerPanel = new JPanel(new BorderLayout(5, 5));
        explorerPanel.setBorder(new TitledBorder("Tablas"));

        DefaultListModel<String> tablasListModel = new DefaultListModel<>();
        JList<String> tablasList = new JList<>(tablasListModel);
        tablasList.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane explorerScrollPane = new JScrollPane(tablasList);

        JButton refreshTablesBtn = new JButton("Actualizar");
        styleButton(refreshTablesBtn, new Color(33, 150, 243));

        explorerPanel.add(explorerScrollPane, BorderLayout.CENTER);
        explorerPanel.add(refreshTablesBtn, BorderLayout.SOUTH);

        // PANEL DERECHO: Editor y resultados
        JPanel editorResultPanel = new JPanel(new BorderLayout());

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(250);
        splitPane.setResizeWeight(0.4);

        // Panel superior: Editor SQL
        JPanel editorPanel = new JPanel(new BorderLayout(5, 5));
        editorPanel.setBorder(new TitledBorder("Consulta SQL"));

        JTextArea sqlTextArea = new JTextArea();
        sqlTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        sqlTextArea.setLineWrap(false);
        sqlTextArea.setTabSize(4);
        sqlTextArea.setText("-- Escribe tu consulta SQL aquí\n-- Selecciona una base de datos arriba\n\nSELECT * FROM usuarios;");

        JScrollPane editorScrollPane = new JScrollPane(sqlTextArea);
        editorPanel.add(editorScrollPane, BorderLayout.CENTER);

        // Panel de botones del editor
        JPanel editorButtonPanel = new JPanel(new BorderLayout());
editorButtonPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

// Primera fila: Botones principales
JPanel mainButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

JButton executeBtn = new JButton("Ejecutar");
JButton clearBtn = new JButton("Limpiar");
JButton showTablesBtn = new JButton("Ver Tablas");

styleButton(executeBtn, new Color(76, 175, 80));
styleButton(clearBtn, new Color(158, 158, 158));
styleButton(showTablesBtn, new Color(255, 152, 0));

mainButtonsPanel.add(executeBtn);
mainButtonsPanel.add(clearBtn);
mainButtonsPanel.add(Box.createHorizontalStrut(10)); // Espaciador
mainButtonsPanel.add(showTablesBtn);

// Segunda fila: Botones de gestión de objetos
JPanel objectButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

JButton createTableBtn = new JButton("Nueva Tabla");
JButton createViewBtn = new JButton("Nueva Vista");
JButton objectExplorerBtn = new JButton("Explorador de Objetos");
JButton createObjectBtn = new JButton("Crear Objeto");

styleButton(createTableBtn, new Color(33, 150, 243));
styleButton(createViewBtn, new Color(156, 39, 176));
styleButton(objectExplorerBtn, new Color(103, 58, 183));
styleButton(createObjectBtn, new Color(0, 150, 136));

objectButtonsPanel.add(createTableBtn);
objectButtonsPanel.add(createViewBtn);
objectButtonsPanel.add(Box.createHorizontalStrut(10)); // Espaciador
objectButtonsPanel.add(objectExplorerBtn);
objectButtonsPanel.add(createObjectBtn);

// Panel contenedor para ambas filas
JPanel allButtonsPanel = new JPanel();
allButtonsPanel.setLayout(new BoxLayout(allButtonsPanel, BoxLayout.Y_AXIS));
allButtonsPanel.add(mainButtonsPanel);
allButtonsPanel.add(Box.createVerticalStrut(5)); // Espacio entre filas
allButtonsPanel.add(objectButtonsPanel);

editorButtonPanel.add(allButtonsPanel, BorderLayout.WEST);

editorPanel.add(editorButtonPanel, BorderLayout.SOUTH);

        // Panel inferior: Resultados
        JPanel resultsPanel = new JPanel(new BorderLayout(5, 5));
        resultsPanel.setBorder(new TitledBorder("Resultados"));

        DefaultTableModel tableModel = new DefaultTableModel();
        JTable resultsTable = new JTable(tableModel);
        resultsTable.setFont(new Font("Arial", Font.PLAIN, 12));
        resultsTable.setRowHeight(25);
        resultsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        resultsTable.getTableHeader().setBackground(new Color(193, 219, 232));
        resultsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JScrollPane resultsScrollPane = new JScrollPane(resultsTable);
        resultsPanel.add(resultsScrollPane, BorderLayout.CENTER);

        // Panel de información de resultados
        JPanel resultsInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel resultsInfoLabel = new JLabel("Selecciona una base de datos y ejecuta una consulta");
        resultsInfoLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        resultsInfoPanel.add(resultsInfoLabel);
        resultsPanel.add(resultsInfoPanel, BorderLayout.SOUTH);

        splitPane.setTopComponent(editorPanel);
        splitPane.setBottomComponent(resultsPanel);

        editorResultPanel.add(splitPane, BorderLayout.CENTER);

        mainSplitPane.setLeftComponent(explorerPanel);
        mainSplitPane.setRightComponent(editorResultPanel);

        panel.add(mainSplitPane, BorderLayout.CENTER);

        //cargar bases de datos disponibles
        Runnable cargarBaseDatos = () -> {
            if (Session.conexionActiva == null) {
                return;
            }

            databaseComboBox.removeAllItems();
            Thread loadDBThread = new Thread(() -> {
                java.util.List<String> databases = util.DBManager.listarBaseDatos(Session.conexionActiva);
                SwingUtilities.invokeLater(() -> {
                    for (String db : databases) {
                        databaseComboBox.addItem(db);
                    }

                    // Seleccionar la BD actual si existe
                    if (Session.conexionActiva.getDatabase() != null
                            && !Session.conexionActiva.getDatabase().isEmpty()) {
                        databaseComboBox.setSelectedItem(Session.conexionActiva.getDatabase());
                    }

                    databaseComboBox.setEnabled(true);
                    refreshDBBtn.setEnabled(true);
                });
            });
            loadDBThread.start();
        };

        //cargar tablas
        Runnable cargarTablas = () -> {
            if (Session.conexionActiva == null) {
                JOptionPane.showMessageDialog(panel,
                        "No hay conexión activa\n\nVe al Gestor de Conexiones y conecta a una base de datos",
                        "Sin conexión",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            tablasListModel.clear();
            Thread loadThread = new Thread(() -> {
                java.util.List<String> tablas = util.SqlEditor.listarTablas(Session.conexionActiva);
                SwingUtilities.invokeLater(() -> {
                    for (String tabla : tablas) {
                        tablasListModel.addElement(tabla);
                    }
                });
            });
            loadThread.start();
        };

        // Actualizar info de conexión cuando se muestra el panel
        panel.addAncestorListener(new javax.swing.event.AncestorListener() {
            @Override
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                if (Session.conexionActiva != null) {
                    connectionInfoLabel.setText("Conectado a: " + Session.conexionActiva.getNombre());
                    connectionInfoLabel.setForeground(new Color(76, 175, 80));
                    cargarBaseDatos.run();
                } else {
                    connectionInfoLabel.setText("Sin conexión - Ve al gestor de conexiones");
                    connectionInfoLabel.setForeground(Color.RED);
                    tablasListModel.clear();
                    databaseComboBox.setEnabled(false);
                    refreshDBBtn.setEnabled(false);
                }
            }

            @Override
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }

            @Override
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        objectExplorerBtn.addActionListener(e -> {
            if (Session.conexionActiva == null || Session.conexionActiva.getDatabase() == null
                    || Session.conexionActiva.getDatabase().isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Selecciona una base de datos primero");
                return;
            }

            ObjectExplorerDialog dialog = new ObjectExplorerDialog(DashboardFrame.this, Session.conexionActiva);
            dialog.setVisible(true);
        });

        createObjectBtn.addActionListener(e -> {
            if (Session.conexionActiva == null || Session.conexionActiva.getDatabase() == null
                    || Session.conexionActiva.getDatabase().isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Selecciona una base de datos primero");
                return;
            }

            if (esBaseDatosSistema(Session.conexionActiva.getDatabase())) {
                JOptionPane.showMessageDialog(panel,
                        "OPERACIÓN NO PERMITIDA\n\n"
                        + "No puedes crear objetos en bases de datos del sistema",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            CreateObjectDialog dialog = new CreateObjectDialog(DashboardFrame.this, Session.conexionActiva);
            dialog.setVisible(true);

            if (dialog.isCreado()) {
                cargarTablas.run();
            }
        });

        databaseComboBox.addActionListener(e -> {
            if (Session.conexionActiva == null || databaseComboBox.getSelectedItem() == null) {
                return;
            }

            String selectedDB = (String) databaseComboBox.getSelectedItem();

            // Actualizar la base de datos en la conexión activa
            Session.conexionActiva.setDatabase(selectedDB);
            cargarTablas.run();

            resultsInfoLabel.setText("Base de datos cambiada a: " + selectedDB);
        });

        // Refrescar lista de bases de datos
        refreshDBBtn.addActionListener(e -> cargarBaseDatos.run());

        // Ejecutar consulta
        executeBtn.addActionListener(e -> {
            if (Session.conexionActiva == null) {
                JOptionPane.showMessageDialog(panel,
                        "No hay conexión activa\n\nConecta a una base de datos primero",
                        "Sin conexion",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Verificar que haya una base de datos seleccionada
            if (Session.conexionActiva.getDatabase() == null
                    || Session.conexionActiva.getDatabase().isEmpty()) {
                JOptionPane.showMessageDialog(panel,
                        "Selecciona una base de datos del menú desplegable arriba",
                        "Sin base de datos",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String sql = sqlTextArea.getText().trim();
            if (sql.isEmpty() || sql.equals("-- Escribe tu consulta SQL aqui\n-- Selecciona una base de datos arriba\n\nSELECT * FROM usuarios;")) {
                JOptionPane.showMessageDialog(panel, "Escribe una consulta SQL válida");
                return;
            }

            //Restriccion para no modificar tablas del sistema
            if (esBaseDatosSistema(Session.conexionActiva.getDatabase())) {
                String sqlLower = sql.toLowerCase().trim();

                if (sqlLower.startsWith("create")
                        || sqlLower.startsWith("insert")
                        || sqlLower.startsWith("update")
                        || sqlLower.startsWith("delete")
                        || sqlLower.startsWith("drop")
                        || sqlLower.startsWith("alter")
                        || sqlLower.startsWith("truncate")) {

                    JOptionPane.showMessageDialog(panel,
                            "OPERACIÓN NO PERMITIDA\n\n"
                            + "No puedes modificar la base de datos del sistema:\n"
                            + "'" + Session.conexionActiva.getDatabase() + "'\n\n"
                            + "Solo se permiten consultas de lectura (SELECT, SHOW, DESCRIBE).",
                            "Error: Base de Datos del Sistema",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            resultsInfoLabel.setText("Ejecutando...");

            Thread execThread = new Thread(() -> {
                util.ResultadoSql resultado
                        = util.SqlEditor.ejecutarConsulta(Session.conexionActiva, sql);

                SwingUtilities.invokeLater(() -> {
                    setCursor(Cursor.getDefaultCursor());

                    if (resultado.esExitoso) {
                        if (resultado.tableModel != null) {
                            resultsTable.setModel(resultado.tableModel);
                            resultsInfoLabel.setText(String.format(
                                    "Filas: %d | Tiempo: %.3fs | BD: %s",
                                    resultado.filas,
                                    Session.conexionActiva.getDatabase()));
                        } else {
                            resultsTable.setModel(new DefaultTableModel());
                            resultsInfoLabel.setText(resultado.mensaje);

                            JOptionPane.showMessageDialog(panel,
                                    resultado.mensaje,
                                    "exito!",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                        cargarTablas.run();
                    } else {
                        resultsInfoLabel.setText("Error en la consulta");
                        JOptionPane.showMessageDialog(panel,
                                resultado.mensaje,
                                "Error SQL",
                                JOptionPane.ERROR_MESSAGE);
                    }
                });
            });
            execThread.start();
        });

        // Limpiar editor
        clearBtn.addActionListener(e -> {
            sqlTextArea.setText("");
            resultsTable.setModel(new DefaultTableModel());
            resultsInfoLabel.setText("Editor limpio");
        });

        // Mostrar tablas
        showTablesBtn.addActionListener(e -> {
            sqlTextArea.setText("SHOW TABLES;");
        });

        // Refrescar lista de tablas
        refreshTablesBtn.addActionListener(e -> cargarTablas.run());

        // Doble clic en tabla para hacer SELECT
        tablasList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    String tabla = tablasList.getSelectedValue();
                    if (tabla != null) {
                        sqlTextArea.setText("SELECT * FROM " + tabla + " LIMIT 100;");
                    }
                }
            }
        });

        // Crear tabla
        createTableBtn.addActionListener(e -> {
            if (Session.conexionActiva == null || Session.conexionActiva.getDatabase() == null
                    || Session.conexionActiva.getDatabase().isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Selecciona una base de datos primero");
                return;
            }

            if (esBaseDatosSistema(Session.conexionActiva.getDatabase())) {
                JOptionPane.showMessageDialog(panel,
                        "OPERACIÓN NO PERMITIDA\n\n"
                        + "Operacion no permitida en base de datos del sistema:\n"
                        + "'" + Session.conexionActiva.getDatabase() + "'\n\n"
                        + "Selecciona una base de datos de usuario.",
                        "Error: Base de Datos del Sistema",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String tableName = JOptionPane.showInputDialog(panel, "Nombre de la tabla:");
            if (tableName != null && !tableName.trim().isEmpty()) {
                String createTableSQL = "CREATE TABLE " + tableName + " (\n"
                        + "    id INT PRIMARY KEY AUTO_INCREMENT,\n"
                        + "    nombre VARCHAR(100),\n"
                        + "    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP\n"
                        + ");";
                sqlTextArea.setText(createTableSQL);
            }

        });

        // Crear vista
        createViewBtn.addActionListener(e -> {
            if (Session.conexionActiva == null || Session.conexionActiva.getDatabase() == null
                    || Session.conexionActiva.getDatabase().isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Selecciona una base de datos primero");
                return;
            }

            if (esBaseDatosSistema(Session.conexionActiva.getDatabase())) {
                JOptionPane.showMessageDialog(panel,
                        "OPERACIÓN NO PERMITIDA\n\n"
                        + "Operacion no permitida en base de datos del sistema:\n"
                        + "'" + Session.conexionActiva.getDatabase() + "'\n\n"
                        + "Selecciona una base de datos de usuario.",
                        "Error: Base de Datos del Sistema",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String viewName = JOptionPane.showInputDialog(panel, "Nombre de la vista:");
            if (viewName != null && !viewName.trim().isEmpty()) {
                String createViewSQL = "CREATE VIEW " + viewName + " AS\n"
                        + "SELECT columna1, columna2\n"
                        + "FROM tabla\n"
                        + "WHERE condicion;";
                sqlTextArea.setText(createViewSQL);
            }

        });

        return panel;
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

    }

}
