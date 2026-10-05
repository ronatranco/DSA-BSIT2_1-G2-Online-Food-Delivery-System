/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import java.awt.*;
import javax.swing.*;
/**
 *
 * @author clairetan
 */
public class CustomerDashboard extends JPanel {

    private MainFrame mainFrame;
    private JButton btnLogout;
    private JButton btnBrowseMenu;
    private JButton btnCart;
    private JButton btnTrackOrder;

    public CustomerDashboard(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(230, 57, 70)); // GrabPanda Red
        headerPanel.setPreferredSize(new Dimension(1024, 70));

        JLabel titleLabel = new JLabel("Customer Portal - GrabPanda");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        headerPanel.add(titleLabel);

        // Center Action Grid
        JPanel contentPanel = new JPanel(new GridLayout(2, 2, 30, 30));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(60, 100, 60, 100));

        btnBrowseMenu = new JButton("Browse Restaurants & Menu");
        btnCart = new JButton("View Cart & Checkout");
        btnTrackOrder = new JButton("Track Order Status");
        btnLogout = new JButton("Logout");

        Font btnFont = new Font("SansSerif", Font.PLAIN, 18);
        btnBrowseMenu.setFont(btnFont);
        btnCart.setFont(btnFont);
        btnTrackOrder.setFont(btnFont);
        btnLogout.setFont(btnFont);

        contentPanel.add(btnBrowseMenu);
        contentPanel.add(btnCart);
        contentPanel.add(btnTrackOrder);
        contentPanel.add(btnLogout);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    public JButton getBtnLogout() {
        return btnLogout;
    }

    public JButton getBtnBrowseMenu() {
        return btnBrowseMenu;
    }

    public JButton getBtnCart() {
        return btnCart;
    }

    public JButton getBtnTrackOrder() {
        return btnTrackOrder;
    }
}