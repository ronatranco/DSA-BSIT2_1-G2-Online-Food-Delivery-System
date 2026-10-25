/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * @author rownavanana
 */
public class AdminDashboard extends JPanel {
    // Exact requested color palette
    public static final Color PURPLE_DARK   = new Color(74, 20, 140);   // Primary Headers, Primary Buttons, Customer Theme
    public static final Color PURPLE_LIGHT  = new Color(156, 39, 176);  // Secondary Accents, Admin Theme Buttons
    public static final Color YELLOW_ACCENT = new Color(255, 193, 7);    // Highlight Buttons, Driver Theme, Brand Accents
    public static final Color BG_LIGHT      = new Color(248, 245, 250); // General Background for all Panels
    public static final Color TEXT_DARK     = new Color(30, 30, 30);    // Default Body Text Color

    // Exact requested typography
    public static final Font HEADER_FONT  = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font TITLE_FONT   = new Font("Segoe UI", Font.BOLD, 36);
    public static final Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BUTTON_FONT  = new Font("Segoe UI", Font.BOLD, 14);

    // GUI Components
    private JButton btnNavOverview, btnNavUsers, btnNavRestaurants, btnNavOrders, btnLogout;
    private JButton btnDeleteSelectedUser, btnRefreshData;
    private JTable dataTable;
    private DefaultTableModel tableModel;
    private JLabel lblDashboardTitle, lblValUsers, lblValOrders, lblValRevenue;
    private JPanel cardMetricsContainer;

    private MainFrame mainFrame;

    public AdminDashboard(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setLayout(null);
        setBackground(BG_LIGHT);

        JPanel sidebar = new JPanel(null);
        sidebar.setBounds(0, 0, 240, 720);
        sidebar.setBackground(PURPLE_DARK);

        JLabel lblBrand = new JLabel("🐼 GrabPanda", SwingConstants.CENTER);
        lblBrand.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));
        lblBrand.setForeground(YELLOW_ACCENT);
        lblBrand.setBounds(10, 30, 220, 40);
        sidebar.add(lblBrand);

        JLabel lblRole = new JLabel("Admin Control Center", SwingConstants.CENTER);
        lblRole.setFont(REGULAR_FONT);
        lblRole.setForeground(Color.WHITE);
        lblRole.setBounds(10, 70, 220, 20);
        sidebar.add(lblRole);

        btnNavOverview = createSidebarButton("Overview", 140);
        btnNavUsers = createSidebarButton("Manage Users", 195);
        btnNavRestaurants = createSidebarButton("Restaurants", 250);
        btnNavOrders = createSidebarButton("Live Orders", 305);
        
        sidebar.add(btnNavOverview);
        sidebar.add(btnNavUsers);
        sidebar.add(btnNavRestaurants);
        sidebar.add(btnNavOrders);

        btnLogout = new JButton("🚪 Logout");
        btnLogout.setFont(BUTTON_FONT);
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBackground(new Color(198, 40, 40));
        btnLogout.setFocusPainted(false);
        btnLogout.setBounds(20, 630, 200, 40);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sidebar.add(btnLogout);

        add(sidebar);

        JPanel contentArea = new JPanel(null);
        contentArea.setBounds(240, 0, 784, 720);
        contentArea.setBackground(BG_LIGHT);

        lblDashboardTitle = new JLabel("System Overview");
        lblDashboardTitle.setFont(HEADER_FONT);
        lblDashboardTitle.setForeground(PURPLE_DARK);
        lblDashboardTitle.setBounds(30, 30, 450, 40);
        contentArea.add(lblDashboardTitle);

        cardMetricsContainer = new JPanel(null);
        cardMetricsContainer.setBounds(30, 85, 724, 110);
        cardMetricsContainer.setBackground(BG_LIGHT);

        JPanel card1 = createMetricCard("Total Users", lblValUsers = new JLabel("0"), 0, new Color(103, 58, 183));
        JPanel card2 = createMetricCard("Active Orders", lblValOrders = new JLabel("0"), 242, new Color(63, 81, 181));
        JPanel card3 = createMetricCard("Total Revenue", lblValRevenue = new JLabel("₱0.00"), 484, new Color(0, 150, 136));
        
        cardMetricsContainer.add(card1);
        cardMetricsContainer.add(card2);
        cardMetricsContainer.add(card3);
        contentArea.add(cardMetricsContainer);

        btnDeleteSelectedUser = new JButton("🗑 Delete Selected");
        styleActionButton(btnDeleteSelectedUser, 30, 210, 170, 35);
        contentArea.add(btnDeleteSelectedUser);

        btnRefreshData = new JButton("🔄 Refresh");
        styleActionButton(btnRefreshData, 215, 210, 130, 35);
        contentArea.add(btnRefreshData);

        String[] columns = {"ID", "Full Name", "Username", "Email", "Role", "Phone"};
        tableModel = new DefaultTableModel(columns, 0);
        dataTable = new JTable(tableModel);
        dataTable.setFont(REGULAR_FONT);
        dataTable.setRowHeight(25);
        dataTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane tableScrollPane = new JScrollPane(dataTable);
        tableScrollPane.setBounds(30, 260, 724, 420);
        contentArea.add(tableScrollPane);

        add(contentArea);
    }

    private JButton createSidebarButton(String text, int yPos) {
        JButton btn = new JButton(text);
        btn.setFont(REGULAR_FONT);
        btn.setForeground(Color.WHITE);
        btn.setBackground(PURPLE_DARK);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBounds(20, yPos, 200, 40);
        return btn;
    }

    private JPanel createMetricCard(String title, JLabel valueLabel, int x, Color bg) {
        JPanel card = new JPanel(null);
        card.setBounds(x, 0, 240, 100);
        card.setBackground(bg);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(REGULAR_FONT);
        lblTitle.setForeground(new Color(240, 240, 240));
        lblTitle.setBounds(15, 15, 200, 20);
        card.add(lblTitle);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setBounds(15, 40, 200, 35);
        card.add(valueLabel);

        return card;
    }

    private void styleActionButton(JButton btn, int x, int y, int width, int height) {
        btn.setFont(BUTTON_FONT);
        btn.setBackground(PURPLE_LIGHT);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBounds(x, y, width, height);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public JButton getBtnLogout() { return btnLogout; }
    public JButton getBtnNavOverview() { return btnNavOverview; }
    public JButton getBtnNavUsers() { return btnNavUsers; }
    public JButton getBtnNavRestaurants() { return btnNavRestaurants; }
    public JButton getBtnNavOrders() { return btnNavOrders; }
    public JButton getBtnDeleteSelectedUser() { return btnDeleteSelectedUser; }
    public JButton getBtnRefreshData() { return btnRefreshData; }
    public JTable getDataTable() { return dataTable; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JLabel getLblDashboardTitle() { return lblDashboardTitle; }
    public JPanel getCardMetricsContainer() { return cardMetricsContainer; }
    public JLabel getLblValUsers() { return lblValUsers; }
    public JLabel getLblValOrders() { return lblValOrders; }
    public JLabel getLblValRevenue() { return lblValRevenue; }
}