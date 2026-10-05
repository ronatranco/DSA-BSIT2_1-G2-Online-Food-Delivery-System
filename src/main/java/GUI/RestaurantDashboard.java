//dwauda
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import java.awt.*;
import javax.swing.*;
/**
 *
 * @author christiandar
 * commit working?
 */
public class RestaurantDashboard extends JPanel {

    private MainFrame mainFrame;
    private JButton btnLogout;
    private JButton btnManageMenu;
    private JButton btnViewOrders;
    private JButton btnUpdateStatus;

    public RestaurantDashboard(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(29, 53, 87)); // Navy Theme
        headerPanel.setPreferredSize(new Dimension(1024, 70));

        JLabel titleLabel = new JLabel("Restaurant Management Portal");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        headerPanel.add(titleLabel);

        // Center Action Grid
        JPanel contentPanel = new JPanel(new GridLayout(2, 2, 30, 30));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(60, 100, 60, 100));

        btnManageMenu = new JButton("Manage Menu Items");
        btnViewOrders = new JButton("View Incoming Orders");
        btnUpdateStatus = new JButton("Update Delivery Status");
        btnLogout = new JButton("Logout");

        Font btnFont = new Font("SansSerif", Font.PLAIN, 18);
        btnManageMenu.setFont(btnFont);
        btnViewOrders.setFont(btnFont);
        btnUpdateStatus.setFont(btnFont);
        btnLogout.setFont(btnFont);

        contentPanel.add(btnManageMenu);
        contentPanel.add(btnViewOrders);
        contentPanel.add(btnUpdateStatus);
        contentPanel.add(btnLogout);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    public JButton getBtnLogout() {
        return btnLogout;
    }

    public JButton getBtnManageMenu() {
        return btnManageMenu;
    }

    public JButton getBtnViewOrders() {
        return btnViewOrders;
    }

    public JButton getBtnUpdateStatus() {
        return btnUpdateStatus;
    }
}