/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author rownavanana
 */
public class AdminPanelGUI extends JPanel {
    private final Color PURPLE_DARK = new Color(74, 20, 140);
    private final Color PURPLE_LIGHT = new Color(156, 39, 176);
    private final Color YELLOW_ACCENT = new Color(255, 193, 7);
    private final Color BG_LIGHT = new Color(248, 245, 250);
    private final Color TEXT_DARK = new Color(33, 33, 33);
    private final Color CARD_BG = Color.WHITE;
    private final Color BORDER_COLOR = new Color(225, 220, 230);

    private MainFrame mainFrame;
    private JButton btnLogout;

    public AdminPanelGUI(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(BG_LIGHT);

        // --- 1. HEADER PANEL ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PURPLE_DARK);
        headerPanel.setPreferredSize(new Dimension(1024, 65));
        headerPanel.setBorder(new EmptyBorder(12, 25, 12, 25));

        JLabel lblAdminHeader = new JLabel("🐼 GrabPanda  |  Admin Dashboard");
        lblAdminHeader.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblAdminHeader.setForeground(YELLOW_ACCENT);
        headerPanel.add(lblAdminHeader, BorderLayout.WEST);

        btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLogout.setBackground(YELLOW_ACCENT);
        btnLogout.setForeground(TEXT_DARK);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(YELLOW_ACCENT, 1),
            BorderFactory.createEmptyBorder(6, 18, 6, 18)
        ));
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        headerPanel.add(btnLogout, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // --- 2. NAVIGATION TABS ---
        JTabbedPane adminTabs = new JTabbedPane(JTabbedPane.TOP);
        adminTabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        adminTabs.setBackground(BG_LIGHT);

        adminTabs.addTab("📊 Dashboard", createDashboardTab());
        adminTabs.addTab("👥 User Management", createUsersTab());
        adminTabs.addTab("🏪 Restaurants", createRestaurantsTab());
        adminTabs.addTab("📦 Order Oversight", createOrdersTab());
        adminTabs.addTab("⚙️ Settings", createSettingsTab());

        add(adminTabs, BorderLayout.CENTER);
    }

    // --- DASHBOARD TAB ---
    private JPanel createDashboardTab() {
        JPanel mainDashboard = new JPanel(new BorderLayout(20, 20));
        mainDashboard.setBackground(BG_LIGHT);
        mainDashboard.setBorder(new EmptyBorder(20, 25, 20, 25));

        // Metric Grid
        JPanel metricsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        metricsPanel.setOpaque(false);

        metricsPanel.add(createMetricCard("Total Revenue", "₱124,500.00", "+14.2% from last week", PURPLE_LIGHT));
        metricsPanel.add(createMetricCard("Active Orders", "18 Ongoing", "4 Pending Pickup", YELLOW_ACCENT));
        metricsPanel.add(createMetricCard("Registered Customers", "1,240 Users", "32 new signups today", PURPLE_DARK));
        metricsPanel.add(createMetricCard("Partner Restaurants", "45 Active", "2 Pending Approvals", PURPLE_LIGHT));

        mainDashboard.add(metricsPanel, BorderLayout.CENTER);

        return mainDashboard;
    }

    private JPanel createMetricCard(String title, String value, String subtext, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 5, 0, 0, accentColor),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(18, 20, 18, 20)
            )
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(new Color(110, 110, 110));

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblValue.setForeground(PURPLE_DARK);

        JLabel lblSubtext = new JLabel(subtext);
        lblSubtext.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblSubtext.setForeground(new Color(130, 130, 130));

        JPanel content = new JPanel(new GridLayout(2, 1, 4, 4));
        content.setOpaque(false);
        content.add(lblValue);
        content.add(lblSubtext);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(content, BorderLayout.CENTER);

        return card;
    }

    // --- USER MANAGEMENT TAB ---
    private JPanel createUsersTab() {
        String[] columns = {"User ID", "Name", "Role", "Email", "Status"};
        Object[][] data = {
            {"USR-101", "Rona Tranco", "Customer", "ronatranco@gmail.com", "Active"},
            {"USR-102", "Claire Tan", "Customer", "clairetan@gmail.com", "Active"},
            {"DRV-201", "Christian Dar", "Driver", "christiandar@gmail.com", "Active"},
            {"USR-103", "Chloe De La Torre", "Driver", "chloeklowi@gmail.com", "Suspended"}
        };

        return createTableSection("Customer & Driver Accounts Management", columns, data, "Add New User", "Suspend Account");
    }

    // --- RESTAURANTS TAB ---
    private JPanel createRestaurantsTab() {
        String[] columns = {"Resto ID", "Restaurant Name", "Owner Name", "Status"};
        Object[][] data = {
            {"RST-01", "Mcdollibee", "Corporate", "Approved"},
            {"RST-02", "Brain Cells Milk Tea", "Mairoh Tocino", "Pending Approval"},
            {"RST-03", "Mang Pinaasa", "Calalang Gideon", "Approved"}
                
        };

        return createTableSection("Restaurant Applications & Approvals", columns, data, "Add Partner", "Approve Application");
    }

    // --- ORDER OVERSIGHT TAB ---
    private JPanel createOrdersTab() {
        String[] columns = {"Order ID", "Customer", "Restaurant", "Total Amount", "Status"};
        Object[][] data = {
            {"ORD-901", "Rona Tranco", "Brain Cells Milk Tea", "₱350.00", "Out for Delivery"},
            {"ORD-902", "Claire Tan", "Mang Pinaasa", "₱180.00", "Preparing"}
        };

        return createTableSection("System-Wide Active Orders Oversight", columns, data, "Filter by Date", "Cancel Order");
    }

    // --- REUSABLE TABLE SECTION BUILDER ---
    private JPanel createTableSection(String sectionTitle, String[] columns, Object[][] data, String primaryBtnText, String secondaryBtnText) {
        JPanel container = new JPanel(new BorderLayout(15, 15));
        container.setBackground(BG_LIGHT);
        container.setBorder(new EmptyBorder(20, 25, 20, 25));

        // Top Toolbar
        JPanel topBar = new JPanel(new BorderLayout(10, 10));
        topBar.setOpaque(false);

        JLabel lblTitle = new JLabel(sectionTitle);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(PURPLE_DARK);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setOpaque(false);

        JTextField txtSearch = new JTextField("Search...", 15);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtSearch.setForeground(Color.GRAY);

        JButton btnPrimary = createStyledButton(primaryBtnText, PURPLE_DARK, Color.WHITE);
        JButton btnSecondary = createStyledButton(secondaryBtnText, Color.WHITE, PURPLE_DARK);

        actionPanel.add(txtSearch);
        actionPanel.add(btnPrimary);
        actionPanel.add(btnSecondary);

        topBar.add(lblTitle, BorderLayout.WEST);
        topBar.add(actionPanel, BorderLayout.EAST);

        // Table Customization
        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Disable editing directly inside cell
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(230, 215, 245));
        table.setSelectionForeground(TEXT_DARK);
        table.setShowGrid(true);
        table.setGridColor(new Color(240, 235, 245));

        // Modern Header Styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(PURPLE_DARK);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 38));
        header.setReorderingAllowed(false);

        // Center Cell Renderer
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        container.add(topBar, BorderLayout.NORTH);
        container.add(scrollPane, BorderLayout.CENTER);

        return container;
    }

    // --- SYSTEM SETTINGS TAB ---
    private JPanel createSettingsTab() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(BG_LIGHT);
        container.setBorder(new EmptyBorder(20, 25, 20, 25));

        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(CARD_BG);
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(25, 30, 25, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Title
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel lblHeader = new JLabel("Platform & Financial Configuration");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblHeader.setForeground(PURPLE_DARK);
        formCard.add(lblHeader, gbc);

        gbc.gridwidth = 1;

        // Commission Rate
        gbc.gridx = 0; gbc.gridy = 1;
        formCard.add(new JLabel("Platform Commission Rate (%):"), gbc);
        gbc.gridx = 1;
        JTextField txtCommission = new JTextField("12.5", 15);
        formCard.add(txtCommission, gbc);

        // Delivery Base Fee
        gbc.gridx = 0; gbc.gridy = 2;
        formCard.add(new JLabel("Base Delivery Fee (₱):"), gbc);
        gbc.gridx = 1;
        JTextField txtBaseFee = new JTextField("49.00", 15);
        formCard.add(txtBaseFee, gbc);

        // Voucher Max Discount
        gbc.gridx = 0; gbc.gridy = 3;
        formCard.add(new JLabel("Max Voucher Discount Limit (₱):"), gbc);
        gbc.gridx = 1;
        JTextField txtVoucherLimit = new JTextField("100.00", 15);
        formCard.add(txtVoucherLimit, gbc);

        // Maintenance Mode Toggle
        gbc.gridx = 0; gbc.gridy = 4;
        formCard.add(new JLabel("System Maintenance Mode:"), gbc);
        gbc.gridx = 1;
        JCheckBox chkMaintenance = new JCheckBox("Enable Maintenance Mode");
        chkMaintenance.setOpaque(false);
        formCard.add(chkMaintenance, gbc);

        // Save Button
        gbc.gridx = 1; gbc.gridy = 5;
        JButton btnSave = createStyledButton("Save Configurations", PURPLE_DARK, Color.WHITE);
        formCard.add(btnSave, gbc);

        container.add(formCard, BorderLayout.NORTH);
        return container;
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PURPLE_DARK, 1),
            BorderFactory.createEmptyBorder(6, 14, 6, 14)
        ));
        return btn;
    }

    public JButton getBtnLogout() { 
        return btnLogout; 
    }
}