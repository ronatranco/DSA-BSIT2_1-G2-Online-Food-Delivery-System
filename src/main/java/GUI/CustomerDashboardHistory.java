/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import Model.Order.OrderData;
import Model.Order.OrderItem;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * @author RJD 
 * 
 */
public class CustomerDashboardHistory extends JPanel {

    // Colors
    private final Color PURPLE_DARK = new Color(74, 20, 140);
    private final Color PURPLE_TEXT = new Color(74, 45, 120);
    private final Color PURPLE_VIBRANT = new Color(156, 39, 176);
    private final Color YELLOW_ACCENT = new Color(255, 193, 7);
    private final Color YELLOW_BRIGHT = new Color(255, 200, 0); 
    private final Color YELLOW_LIGHT = new Color(255, 248, 225);
    private final Color YELLOW_BORDER = new Color(240, 210, 100);
    private final Color YELLOW_TEXT = new Color(175, 120, 0);
    private final Color AMBER_TEXT = new Color(160, 110, 0);
    private final Color TEXT_DARK = new Color(30, 30, 30);
    private final Color TEXT_MUTED = new Color(120, 120, 120);
    private final Color COLOR_WHITE = new Color(255, 255, 255);
    private final Color CARD_BORDER = new Color(225, 202, 230);
    private final Color BG_LIGHT = new Color(248, 245, 250);
    private final Color TAB_INACTIVE_BG = new Color(232, 218, 250);
    private final Color DIVIDER_COLOR = new Color(235, 230, 245);
    private final Color SEARCH_BG = new Color(245, 240, 253);
    private final Color DELIVERED_BG = new Color(230, 245, 230);
    private final Color DELIVERED_FG = new Color(46, 125, 50);

    // Declaration
    private JLabel lblTitle, lblSubtitle, lblSearchIcon;
    private JLabel lblStore, lblLoc, lblTotalTitle, lblTotalVal, lblStatusText;
    private JTextField txtSearch;
    private JButton btnViewDetails, btnReOrder, btnExitHistory;
    private JPanel searchBox, containerPanel, leftPanel, centerPanel, rightPanel, statusBadge;
    private JScrollPane scrollPane;
    private JSeparator sep;
    private java.util.function.Consumer<OrderData> onReOrderCallback;
    private final List<OrderData> allOrders = new ArrayList<>();
    private final List<JButton> filterButtons = new ArrayList<>();

    // Callback Setup
    public void setOnReOrderCallback(java.util.function.Consumer<OrderData> callback) {
        this.onReOrderCallback = callback;
    }

    public CustomerDashboardHistory() {
        //1. Main Panel
        setLayout(null);
        setBackground(BG_LIGHT);
        setBounds(1024, 0, 824, 720);
        setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, YELLOW_ACCENT));

        //2. Header & Search Bar
        lblTitle = new JLabel("Order History");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(PURPLE_DARK);
        lblTitle.setBounds(40, 24, 250, 36);
        add(lblTitle);

        lblSubtitle = new JLabel("Check your past orders and their details.");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitle.setForeground(TEXT_MUTED);
        lblSubtitle.setBounds(40, 62, 300, 18);
        add(lblSubtitle);

        btnExitHistory = new JButton("X") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(YELLOW_ACCENT);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnExitHistory.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnExitHistory.setForeground(PURPLE_DARK);
        btnExitHistory.setContentAreaFilled(false);
        btnExitHistory.setBorderPainted(false);
        btnExitHistory.setFocusPainted(false);
        btnExitHistory.setBounds(745, 24, 45, 35);
        btnExitHistory.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(btnExitHistory);

        searchBox = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(SEARCH_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);
                g2.setColor(CARD_BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 28, 28);
                g2.dispose();
            }
        };
        
        searchBox.setOpaque(false);
        searchBox.setBounds(395, 24, 335, 42);
        add(searchBox);

        lblSearchIcon = new JLabel("🔍", SwingConstants.CENTER);
        lblSearchIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        lblSearchIcon.setForeground(TEXT_MUTED);
        lblSearchIcon.setBounds(14, 11, 20, 20);
        searchBox.add(lblSearchIcon);

        String placeholder = "Search order, restaurant or item...";
        txtSearch = new JTextField(placeholder);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtSearch.setForeground(TEXT_MUTED);
        txtSearch.setBorder(null);
        txtSearch.setOpaque(false);
        txtSearch.setBounds(42, 10, 280, 22);
        
        txtSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtSearch.getText().equals(placeholder)) {
                    txtSearch.setText("");
                    txtSearch.setForeground(TEXT_DARK);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setText(placeholder);
                    txtSearch.setForeground(TEXT_MUTED);
                }
            }
        });

        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void updateSearch() {
                String query = txtSearch.getText();
                if (query.equals(placeholder)) query = "";
                String activeCategory = "All Orders";
                for (JButton btn : filterButtons) {
                    if (btn.getClientProperty("active") != null && (boolean) btn.getClientProperty("active")) {
                        activeCategory = btn.getText();
                        break;
                    }
                }
                filterOrders(activeCategory, query);
            }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateSearch(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateSearch(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateSearch(); }
        });

        searchBox.add(txtSearch);

        // 3. Filter Tabs Setup
        createFilterTab("All Orders", 40, 96, 115);
        createFilterTab("Completed", 165, 96, 115);
        createFilterTab("In Progress", 290, 96, 115);
        createFilterTab("Cancelled", 415, 96, 115);

        setActiveFilterTab(filterButtons.get(0));

        // 4. Container Panel & Scroll Pane
        containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        containerPanel.setOpaque(false);
        containerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        scrollPane = new JScrollPane(containerPanel);
        scrollPane.setBounds(40, 146, 744, 530);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BG_LIGHT);
        scrollPane.setBackground(BG_LIGHT);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane);

        List<OrderItem> items1 = new ArrayList<>();
        items1.add(new OrderItem("Burger Steak", 2, 75.0));
        items1.add(new OrderItem("Large Fries", 1, 39.0));
        registerOrder("McDollibee", "McDollibee — Biñan", "#ORD-2026-1048", "Oct 10, 2026 · 12:35 PM", "Oct 10, 2026 · 1:05 PM", "GCash", "Delivered", "₱189.00", items1);

        filterOrders("All Orders");
    }
    
    // 5. Filter Tabs Methods
    private void createFilterTab(String text, int x, int y, int width) {
        JButton btnTab = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                boolean isActive = getClientProperty("active") != null && (boolean) getClientProperty("active");

                g2.setColor(isActive ? YELLOW_ACCENT : TAB_INACTIVE_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnTab.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnTab.setForeground(PURPLE_VIBRANT);
        btnTab.setBounds(x, y, width, 34);
        btnTab.setContentAreaFilled(false);
        btnTab.setBorderPainted(false);
        btnTab.setFocusPainted(false);
        btnTab.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnTab.addActionListener(e -> {
            setActiveFilterTab(btnTab);
            filterOrders(text);
        });

        filterButtons.add(btnTab);
        add(btnTab);
    }

    private void setActiveFilterTab(JButton activeBtn) {
        for (JButton btn : filterButtons) {
            boolean active = (btn == activeBtn);
            btn.putClientProperty("active", active);
            btn.setForeground(active ? TEXT_DARK : PURPLE_VIBRANT);
            btn.repaint();
        }
    }

    // 6. Order Filtering & Registration 
    public void registerOrder(String storeName, String location, String orderId, String orderDate, String deliveryDate, String paymentMop, String status, String totalPaid, List<OrderItem> items) {
        allOrders.add(new OrderData(storeName, location, orderId, orderDate, deliveryDate, paymentMop, status, totalPaid, items));
        filterOrders("All Orders");
    }

    private void filterOrders(String category) {
        String query = (txtSearch != null) ? txtSearch.getText() : "";
        if (query.equals("Search order, restaurant or item...")) {
            query = "";
        }
        filterOrders(category, query);
    }

    private void filterOrders(String category, String searchQuery) {
        containerPanel.removeAll();
        String queryLower = searchQuery.toLowerCase().trim();

        for (int i = allOrders.size() - 1; i >= 0; i--) {
            OrderData order = allOrders.get(i);
            boolean matchesCategory = false;
            if (category.equals("All Orders")) {
                matchesCategory = true;
            } else if (category.equals("Completed") && order.getStatus().equalsIgnoreCase("Delivered")) {
                matchesCategory = true;
            } else if (category.equals("In Progress") && (order.getStatus().equalsIgnoreCase("Out for Delivery") || order.getStatus().equalsIgnoreCase("Pending"))) {
                matchesCategory = true;
            } else if (category.equals("Cancelled") && order.getStatus().equalsIgnoreCase("Cancelled")) {
                matchesCategory = true;
            }

            boolean matchesSearch = queryLower.isEmpty();
            if (!matchesSearch) {
                if (order.getStoreName().toLowerCase().contains(queryLower) ||
                    order.getOrderId().toLowerCase().contains(queryLower) ||
                    order.getPaymentMop().toLowerCase().contains(queryLower) ||
                    order.getStatus().toLowerCase().contains(queryLower)) {
                    matchesSearch = true;
                } else {
                    for (OrderItem item : order.getItems()) {
                        if (item.getItemName().toLowerCase().contains(queryLower)) {
                            matchesSearch = true;
                            break;
                        }
                    }
                }
            }

            if (matchesCategory && matchesSearch) {
                containerPanel.add(createOrderCardPanel(order));
                containerPanel.add(Box.createRigidArea(new Dimension(0, 16)));
            }
        }

        containerPanel.revalidate();
        containerPanel.repaint();
    }

    // 7. Order Card Creation
    private JPanel createOrderCardPanel(OrderData order) {
        JPanel card = new JPanel(null) { 
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(CARD_BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(700, 185));
        card.setMaximumSize(new Dimension(700, 185));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 7.1 Left Column: Store Details & Items (Width: 235px, x: 16)
        JPanel leftPnl = new JPanel(null);
        leftPnl.setOpaque(false);
        leftPnl.setBounds(16, 16, 235, 153);

        JLabel storeLbl = new JLabel(order.getStoreName());
        storeLbl.setFont(new Font("Segoe UI", Font.BOLD, 17));
        storeLbl.setForeground(PURPLE_DARK);
        storeLbl.setBounds(0, 0, 235, 22);
        leftPnl.add(storeLbl);

        JLabel locLbl = new JLabel("📍  " + order.getLocation());
        locLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        locLbl.setForeground(TEXT_MUTED);
        locLbl.setBounds(0, 24, 235, 18);
        leftPnl.add(locLbl);

        JSeparator cardSep = new JSeparator();
        cardSep.setBounds(0, 48, 235, 1);
        cardSep.setForeground(DIVIDER_COLOR);
        cardSep.setBackground(DIVIDER_COLOR);
        leftPnl.add(cardSep);

        int yOffset = 52;
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                JLabel badge = new JLabel(item.getQuantity() + "×", SwingConstants.CENTER) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(YELLOW_LIGHT);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                        g2.setColor(YELLOW_BORDER);
                        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                        g2.dispose();
                        super.paintComponent(g);
                    }
                };
                badge.setBounds(0, yOffset, 30, 18);
                badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
                badge.setForeground(AMBER_TEXT);
                leftPnl.add(badge);

                JLabel itemNameLbl = new JLabel(item.getItemName());
                itemNameLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                itemNameLbl.setForeground(TEXT_DARK);
                itemNameLbl.setBounds(38, yOffset, 190, 18);
                leftPnl.add(itemNameLbl);

                yOffset += 20; 
            }
        }

        JLabel totalTitleLbl = new JLabel("TOTAL PAID");
        totalTitleLbl.setFont(new Font("Segoe UI", Font.BOLD, 9));
        totalTitleLbl.setForeground(TEXT_DARK);
        totalTitleLbl.setBounds(0, 112, 120, 12);
        leftPnl.add(totalTitleLbl);

        JLabel totalValLbl = new JLabel(order.getTotalPaid());
        totalValLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalValLbl.setForeground(YELLOW_BRIGHT);
        totalValLbl.setBounds(0, 124, 180, 22);
        leftPnl.add(totalValLbl);

        card.add(leftPnl);

        // 7.2 Center Column: Order Metadata (Starts at x=265, Width: 215px)
        JPanel centerPnl = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(DIVIDER_COLOR);
                g2.drawLine(0, 10, 0, getHeight() - 10);
                g2.dispose();
            }
        };
        centerPnl.setOpaque(false);
        centerPnl.setBounds(265, 16, 215, 153);

        addMetaItem(centerPnl, "📋", "ORDER ID", order.getOrderId(), 10, 2);
        addMetaItem(centerPnl, "📅", "ORDER DATE", order.getOrderDate(), 10, 40);
        addMetaItem(centerPnl, "🛵", "DELIVERY DATE", order.getDeliveryDate(), 10, 78);
        addMetaItem(centerPnl, "💳", "PAYMENT", order.getPaymentMop(), 10, 116);

        card.add(centerPnl);

        // 7.3 Right Column: Status & Button Actions (Starts at x=490, Width: 194px)
        JPanel rightPnl = new JPanel(null);
        rightPnl.setOpaque(false);
        rightPnl.setBounds(490, 16, 194, 153);

        boolean isDelivered = order.getStatus().equalsIgnoreCase("Delivered");

        JPanel badgePnl = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isDelivered ? DELIVERED_BG : YELLOW_LIGHT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        badgePnl.setBounds(10, 0, 175, 32);
        badgePnl.setOpaque(false);

        JLabel statusTextLbl = new JLabel((isDelivered ? "✔  " : "🛵  ") + order.getStatus(), SwingConstants.CENTER);
        statusTextLbl.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        statusTextLbl.setForeground(isDelivered ? DELIVERED_FG : YELLOW_TEXT);
        statusTextLbl.setBounds(0, 6, 175, 20);
        badgePnl.add(statusTextLbl);
        rightPnl.add(badgePnl);

        JButton detailsBtn = new JButton("View Details  →") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 18, 18));
                g2.setColor(CARD_BORDER);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 18, 18));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        detailsBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        detailsBtn.setForeground(PURPLE_DARK);
        detailsBtn.setContentAreaFilled(false);
        detailsBtn.setBorderPainted(false);
        detailsBtn.setFocusPainted(false);
        detailsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        detailsBtn.setBounds(10, 44, 175, 36);
        rightPnl.add(detailsBtn);

        JButton reorderBtn = new JButton("🔄  Re-Order") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(PURPLE_DARK);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 18, 18));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        reorderBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        reorderBtn.setForeground(COLOR_WHITE);
        reorderBtn.setContentAreaFilled(false);
        reorderBtn.setBorderPainted(false);
        reorderBtn.setFocusPainted(false);
        reorderBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        reorderBtn.setBounds(10, 88, 175, 36);

        reorderBtn.addActionListener(e -> {
            if (onReOrderCallback != null) {
                onReOrderCallback.accept(order);
            }
        });

        rightPnl.add(reorderBtn);
        card.add(rightPnl);

        return card;
    }

    // 8. Helper Methods
    private void addMetaItem(JPanel panel, String icon, String title, String value, int x, int y) {
        JLabel lblIcon = new JLabel(icon, SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        lblIcon.setForeground(PURPLE_TEXT); 
        lblIcon.setBounds(x, y + 2, 24, 24);
        panel.add(lblIcon);

        JLabel lblMetaTitle = new JLabel(title);
        lblMetaTitle.setFont(new Font("Segoe UI", Font.BOLD, 9));
        lblMetaTitle.setForeground(TEXT_DARK);
        lblMetaTitle.setBounds(x + 32, y, 160, 11);
        panel.add(lblMetaTitle);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 11)); 
        lblValue.setForeground(PURPLE_DARK);                   
        lblValue.setBounds(x + 32, y + 12, 180, 15);
        panel.add(lblValue);
    }

    public void animateOpen() {
        setBounds(200, 0, 824, 720);
    }

    public void animateClose() {
        setBounds(1024, 0, 824, 720); 
    }
    
    public void addProfileActions(ActionListener listener) {
        btnExitHistory.addActionListener(listener);
    }

    public JButton getCloseButton() {
        return btnExitHistory;
    }
}