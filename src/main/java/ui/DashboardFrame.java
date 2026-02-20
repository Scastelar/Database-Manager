package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class DashboardFrame extends JFrame {

    private JPanel contentPanel;
    private CardLayout cardLayout;

    public DashboardFrame(String username) {

        setTitle("Dashboard");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setBackground(new Color(193, 219, 232));

        // toolbar 
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(80, getHeight()));
        sidebar.setLayout(new GridLayout(4,1,10,10));
        sidebar.setBackground(new Color(193, 219, 232));

        JButton connectionsBtn = createIconButton("src\\main\\java\\icons\\gestion-de-bases-de-datos.png", "Gestor de Conexiones");
        JButton sqlBtn = createIconButton("src\\main\\java\\icons\\servidor-sql.png", "Editor SQL");
        JButton homeBtn = createIconButton("src\\main\\java\\icons\\hogar.png", "Inicio");
        JButton logoutBtn = createIconButton("src\\main\\java\\icons\\cerrar-sesion.png", "Cerrar sesión");

        sidebar.add(homeBtn);
        sidebar.add(connectionsBtn);
        sidebar.add(sqlBtn);
        sidebar.add(logoutBtn);

        add(sidebar, BorderLayout.WEST);

        // panel del centro
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        

        JPanel homePanel = createHomePanel(username);
        JPanel connectionsPanel = createConnectionsPanel();
        JPanel sqlPanel = createSqlPanel();

        contentPanel.add(homePanel, "HOME");
        contentPanel.add(connectionsPanel, "CONNECTIONS");
        contentPanel.add(sqlPanel, "SQL");

        add(contentPanel, BorderLayout.CENTER);
        homePanel.setBackground(new Color(255, 241, 181));
        connectionsPanel.setBackground(new Color(255, 241, 181));
        sqlPanel.setBackground(new Color(255, 241, 181));

        homeBtn.addActionListener(e -> cardLayout.show(contentPanel, "HOME"));
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

    // PANELES INTERNOS
    private JPanel createHomePanel(String username) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel welcome = new JLabel("Bienvenido, " + username, SwingConstants.CENTER);
        welcome.setFont(new Font("Arial", Font.BOLD, 24));
        
        panel.add(welcome, BorderLayout.CENTER);
        return panel;
    }


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
        DefaultListModel<String> connectionListModel = new DefaultListModel<>();
        connectionListModel.addElement("MySQL - localhost:3306");
        connectionListModel.addElement("PostgreSQL - localhost:5432");
        connectionListModel.addElement("Oracle - 192.168.1.100:1521");
        
        JList<String> connectionList = new JList<>(connectionListModel);
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
        return panel;
    }


    private JPanel createSqlPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Título
        JLabel titleLabel = new JLabel("Editor SQL", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Panel central dividido en dos: editor y resultados
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(250);
        splitPane.setResizeWeight(0.4);

        // Panel superior: Editor SQL
        JPanel editorPanel = new JPanel(new BorderLayout(5, 5));
        editorPanel.setBorder(new TitledBorder("Consulta SQL"));

        JTextArea sqlTextArea = new JTextArea();
        sqlTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        sqlTextArea.setLineWrap(true);
        sqlTextArea.setWrapStyleWord(true);
        sqlTextArea.setText("-- Escribe tu consulta SQL aquí\nSELECT * FROM tabla;");
        
        JScrollPane editorScrollPane = new JScrollPane(sqlTextArea);
        editorPanel.add(editorScrollPane, BorderLayout.CENTER);

        // Panel de botones del editor
        JPanel editorButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        
        JButton executeBtn = new JButton("▶ Ejecutar");
        JButton clearBtn = new JButton("Limpiar");
        JButton createTableBtn = new JButton("+ Crear Tabla");
        JButton createViewBtn = new JButton("👁 Crear Vista");
        JButton formatBtn = new JButton("Formatear");

        // Estilo de botones
        styleButton(executeBtn, new Color(76, 175, 80));
        styleButton(clearBtn, new Color(158, 158, 158));
        styleButton(createTableBtn, new Color(33, 150, 243));
        styleButton(createViewBtn, new Color(156, 39, 176));
        styleButton(formatBtn, new Color(255, 152, 0));

        editorButtonPanel.add(executeBtn);
        editorButtonPanel.add(clearBtn);
        editorButtonPanel.add(new JSeparator(SwingConstants.VERTICAL));
        editorButtonPanel.add(createTableBtn);
        editorButtonPanel.add(createViewBtn);
        editorButtonPanel.add(formatBtn);

        editorPanel.add(editorButtonPanel, BorderLayout.SOUTH);

        // Panel inferior: Resultados
        JPanel resultsPanel = new JPanel(new BorderLayout(5, 5));
        resultsPanel.setBorder(new TitledBorder("Resultados"));

        // Tabla para mostrar resultados
        String[] columnNames = {"ID", "Nombre", "Email", "Fecha"};
        Object[][] data = {
            {1, "Juan Pérez", "juan@email.com", "2026-01-15"},
            {2, "María García", "maria@email.com", "2026-01-16"},
            {3, "Carlos López", "carlos@email.com", "2026-01-17"}
        };

        JTable resultsTable = new JTable(data, columnNames);
        resultsTable.setFont(new Font("Arial", Font.PLAIN, 12));
        resultsTable.setRowHeight(25);
        resultsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        resultsTable.getTableHeader().setBackground(new Color(193, 219, 232));
        
        JScrollPane resultsScrollPane = new JScrollPane(resultsTable);
        resultsPanel.add(resultsScrollPane, BorderLayout.CENTER);

        // Panel de información de resultados
        JPanel resultsInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel resultsInfoLabel = new JLabel("Filas: 3 | Tiempo: 0.05s");
        resultsInfoLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        resultsInfoPanel.add(resultsInfoLabel);
        resultsPanel.add(resultsInfoPanel, BorderLayout.SOUTH);

        splitPane.setTopComponent(editorPanel);
        splitPane.setBottomComponent(resultsPanel);

        panel.add(splitPane, BorderLayout.CENTER);
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