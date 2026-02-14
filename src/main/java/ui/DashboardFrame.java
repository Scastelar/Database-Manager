package ui;

import javax.swing.*;
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
        contentPanel.setBackground(new Color(193, 219, 232));

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
        
        panel.add(welcome, BorderLayout.CENTER);
        return panel;
    }


    private JPanel createConnectionsPanel() {
        JPanel panel = new JPanel();

        return panel;
    }


    private JPanel createSqlPanel() {
        JPanel panel = new JPanel();
        return panel;
    }
}
