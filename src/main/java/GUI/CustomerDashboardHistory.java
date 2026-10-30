/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import Model.Order.OrderData;
import Model.Order.OrderItem;
import Model.Order.OrderStore;

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
        // 1. Main Panel
        setLayout(null);
        setBackground(BG_LIGHT);
        setBounds(1024, 0, 824, 720);
        setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, YELLOW_ACCENT));

        // 2. Header & Search Bar
        // 2.1 Title & Subtitle
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

        // 2.2 Exit Button
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
        btnExitHistory.setMargin(new Insets(0, 0, 0, 0));         
        btnExitHistory.setBorder(BorderFactory.createEmptyBorder());
        btnExitHistory.setHorizontalAlignment(SwingConstants.CENTER);
        btnExitHistory.setBounds(750, 24, 45, 35);
        btnExitHistory.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(btnExitHistory);

        // 2.3 Search Box
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

        // 2.4 Search Icon
        lblSearchIcon = new JLabel("🔍", SwingConstants.CENTER);
        lblSearchIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        lblSearchIcon.setForeground(TEXT_MUTED);
        lblSearchIcon.setBounds(14, 11, 20, 20);
        searchBox.add(lblSearchIcon);

        // 2.5 Search Field & Placeholder
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

        // 2.6 Live Search Listener
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
        createFilterTab("Cancelled", 290, 96, 115);

        setActiveFilterTab(filterButtons.get(0));

        // 4. Container Panel & Scroll Pane
        // 4.1 Container Panel
        containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        containerPanel.setOpaque(false);
        containerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // 4.2 Scroll Pane
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

        // 4.3 Auto-Refresh When an Order Changes
        OrderStore.get().addListener(this::refreshOrders);
       
        filterOrders("All Orders");
    }
    
    // 5. Order Refresh 
    public void refreshOrders() {
        allOrders.clear();
        allOrders.addAll(OrderStore.get().getHistory());

        String activeCategory = "All Orders";
        for (JButton btn : filterButtons) {
            if (btn.getClientProperty("active") != null && (boolean) btn.getClientProperty("active")) {
                activeCategory = btn.getText();
                break;
            }
        }
        filterOrders(activeCategory);
    }
    
    // 6. Filter Tabs Methods
    // 6.1 Create Filter Tab
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

    // 6.2 Set Active Tab
    private void setActiveFilterTab(JButton activeBtn) {
        for (JButton btn : filterButtons) {
            boolean active = (btn == activeBtn);
            btn.putClientProperty("active", active);
            btn.setForeground(active ? TEXT_DARK : PURPLE_VIBRANT);
            btn.repaint();
        }
    }

    // 7. Order Filtering & Registration
    // 7.1 Register Order
    public void registerOrder(String storeName, String location, String orderId, String orderDate, String deliveryDate, String paymentMop, String status, String totalPaid, List<OrderItem> items) {
        allOrders.add(new OrderData(storeName, location, orderId, orderDate, deliveryDate, paymentMop, status, totalPaid, items));
        filterOrders("All Orders");
    }

    // 7.2 Filter by Category
    private void filterOrders(String category) {
        String query = (txtSearch != null) ? txtSearch.getText() : "";
        if (query.equals("Search order, restaurant or item...")) {
            query = "";
        }
        filterOrders(category, query);
    }

    // 7.3 Filter by Category & Search
    private void filterOrders(String category, String searchQuery) {
        containerPanel.removeAll();
        String query = searchQuery.toLowerCase().trim();
        int shown = 0;

        for (int i = allOrders.size() - 1; i >= 0; i--) {
            OrderData order = allOrders.get(i);

            boolean matchesCategory = category.equals("All Orders")
                    || (category.equals("Completed") && order.getStatus().equalsIgnoreCase("Delivered"))
                    || (category.equals("Cancelled") && order.getStatus().equalsIgnoreCase("Cancelled"));

            if (matchesCategory && matchesSearch(order, query)) {
                containerPanel.add(createOrderCardPanel(order));
                containerPanel.add(Box.createRigidArea(new Dimension(0, 16)));
                shown++;
            }
        }

        if (shown == 0) {
            JLabel empty = new JLabel("No orders found.");
            empty.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            empty.setForeground(TEXT_MUTED);
            empty.setAlignmentX(Component.LEFT_ALIGNMENT);
            containerPanel.add(empty);
        }

        containerPanel.revalidate();
        containerPanel.repaint();
    }

    // 7.4 Search Matching 
    private boolean matchesSearch(OrderData order, String query) {
        if (query.isEmpty()) return true;

        StringBuilder sb = new StringBuilder();
        sb.append(order.getStoreName()).append(' ')
          .append(order.getLocation()).append(' ')
          .append(order.getOrderId()).append(' ')
          .append(order.getOrderDate()).append(' ')
          .append(order.getDeliveryDate()).append(' ')
          .append(order.getPaymentMop()).append(' ')
          .append(order.getStatus()).append(' ')
          .append(order.getTotalPaid()).append(' ');
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                sb.append(item.getItemName()).append(' ');
            }
        }

        String haystack = sb.toString().toLowerCase().replace("#", "");
        return haystack.contains(query.replace("#", ""));
    }

    // 8. Order Card Creation
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

        // 8.1 Left Column
        JPanel leftPnl = new JPanel(null);
        leftPnl.setOpaque(false);
        leftPnl.setBounds(16, 16, 200, 153);

        // 8.1.1 Store & Location
        JLabel storeLbl = new JLabel(order.getStoreName());
        storeLbl.setFont(new Font("Segoe UI", Font.BOLD, 17));
        storeLbl.setForeground(PURPLE_DARK);
        storeLbl.setBounds(0, 0, 200, 22);
        leftPnl.add(storeLbl);

        JLabel locLbl = new JLabel("📍  " + order.getLocation());
        locLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        locLbl.setForeground(TEXT_MUTED);
        locLbl.setBounds(0, 24, 200, 18);
        leftPnl.add(locLbl);

        // 8.1.2 Divider & Item List
        JSeparator cardSep = new JSeparator();
        cardSep.setBounds(0, 46, 200, 1);
        cardSep.setForeground(DIVIDER_COLOR);
        cardSep.setBackground(DIVIDER_COLOR);
        leftPnl.add(cardSep);

        int yOffset = 50;
        if (order.getItems() != null) {
            int maxItems = Math.min(order.getItems().size(), 2); 
            for (int i = 0; i < maxItems; i++) {
                OrderItem item = order.getItems().get(i);
                
                JLabel badge = new JLabel(item.getQuantity() + "×", SwingConstants.CENTER) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(YELLOW_LIGHT);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                        g2.setColor(YELLOW_BORDER);
                        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
                        g2.dispose();
                        super.paintComponent(g);
                    }
                };
                badge.setBounds(10, yOffset, 26, 18);
                badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
                badge.setForeground(AMBER_TEXT);
                leftPnl.add(badge);

                JLabel itemNameLbl = new JLabel(item.getItemName());
                itemNameLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                itemNameLbl.setForeground(TEXT_DARK);
                itemNameLbl.setBounds(42, yOffset, 155, 18);
                leftPnl.add(itemNameLbl);

                yOffset += 22; 
            }
        }

        // 8.1.3 Total Paid 
        JLabel totalTitleLbl = new JLabel("TOTAL PAID");
        totalTitleLbl.setFont(new Font("Segoe UI", Font.BOLD, 9));
        totalTitleLbl.setForeground(TEXT_DARK);
        totalTitleLbl.setBounds(0, 114, 120, 12);
        leftPnl.add(totalTitleLbl);

        JLabel totalValLbl = new JLabel(order.getTotalPaid());
        totalValLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalValLbl.setForeground(YELLOW_BRIGHT);
        totalValLbl.setBounds(0, 126, 180, 22);
        leftPnl.add(totalValLbl);

        card.add(leftPnl);

        // 8.2 Center Column
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
        centerPnl.setBounds(220, 16, 265, 153);

        addMetaItem(centerPnl, "📋", "ORDER ID", order.getOrderId(), 10, 2);
        addMetaItem(centerPnl, "📅", "ORDER DATE", order.getOrderDate(), 10, 40);
        addMetaItem(centerPnl, "🛵", "DELIVERY DATE", order.getDeliveryDate(), 10, 78);
        addMetaItem(centerPnl, "💳", "PAYMENT", order.getPaymentMop(), 10, 116);

        card.add(centerPnl);

        // 8.3 Right Column
        JPanel rightPnl = new JPanel(null);
        rightPnl.setOpaque(false);
        rightPnl.setBounds(490, 16, 194, 153);

        // 8.3.1 Status Badge
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

        // 8.3.2 View Details Button
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

        // 8.3.3 Re-Order Button
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

        reorderBtn.addActionListener(e -> reOrderToCheckout(order));

        rightPnl.add(reorderBtn);
        card.add(rightPnl);

        return card;
    }


    // 9. Helper Methods
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
        lblValue.setBounds(x + 32, y + 12, 225, 15);
        panel.add(lblValue);
    }

    // 9.1 Re-Order -> Checkout (with a brand new Order ID)
    private void reOrderToCheckout(OrderData past) {
        Container parent = this.getParent();
        if (parent == null || past.getItems() == null || past.getItems().isEmpty()) return;

        List<OrderItem> copiedItems = new ArrayList<>();
        for (OrderItem item : past.getItems()) {
            copiedItems.add(new OrderItem(item.getItemName(), item.getQuantity(),
                    item.getUnitPrice(), item.getSpecialInstructions()));
        }

        String name = "", phone = "", address = "";
        Container root = parent;
        while (root != null && !(root instanceof CustomerDashboard)) {
            root = root.getParent();
        }
        if (root instanceof CustomerDashboard) {
            CustomerDashboard dash = (CustomerDashboard) root;
            String first = dash.getSavedFirstName();
            String last = dash.getSavedLastName();
            if ((first != null && !first.isEmpty()) || (last != null && !last.isEmpty())) {
                name = ((first != null ? first : "") + " " + (last != null ? last : "")).trim();
            }
            if (dash.getSavedMobileNumber() != null) phone = dash.getSavedMobileNumber();
            if (dash.getSavedFullAddress() != null) address = dash.getSavedFullAddress();
        }
        if (address.isEmpty() && past.getLocation() != null && !past.getLocation().equals("Pickup")) {
            address = past.getLocation();
        }

        String stamp = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MMdd-HHmmss-SSS"));
        String newOrderId = "ORD-CUST001-" + stamp;

        Model.Order newOrder = new Model.Order(newOrderId, "CUST-001", name, phone, address,
                past.getStoreName(), copiedItems, "PENDING");

        CustomerDashboardCheckout checkout = new CustomerDashboardCheckout(newOrder, past.getLocation(), past.getPaymentMop());
        checkout.setBounds(468, 0, 340, 720);
        checkout.setReturnToCartOnBack(false);   // Back should return to History, not the cart

        final java.awt.event.AWTEventListener[] watcher = new java.awt.event.AWTEventListener[1];
        final java.awt.event.HierarchyListener[] hierarchy = new java.awt.event.HierarchyListener[1];
        final Runnable closeCheckout = () -> {
            if (watcher[0] != null) {
                Toolkit.getDefaultToolkit().removeAWTEventListener(watcher[0]);
                watcher[0] = null;
            }
            if (hierarchy[0] != null) {
                CustomerDashboardHistory.this.removeHierarchyListener(hierarchy[0]);
                hierarchy[0] = null;
            }
            Rectangle area = checkout.getBounds();
            checkout.setVisible(false);
            CustomerDashboardHistory.this.remove(checkout);
            CustomerDashboardHistory.this.repaint(area);
        };

        watcher[0] = ev -> {
            if (ev.getID() != java.awt.event.MouseEvent.MOUSE_PRESSED
                    || !(ev.getSource() instanceof Component)) return;
            Component src = (Component) ev.getSource();
            Window checkoutWindow = SwingUtilities.getWindowAncestor(checkout);
            if (checkoutWindow == null || SwingUtilities.getWindowAncestor(src) != checkoutWindow) return; 
            if (SwingUtilities.isDescendingFrom(src, checkout)) return;                                   
            if (SwingUtilities.isDescendingFrom(src, CustomerDashboardHistory.this)) return;           
            boolean clickable = src instanceof AbstractButton
                    || src.getCursor().getType() == Cursor.HAND_CURSOR;
            if (clickable) {
                closeCheckout.run();
            }
        };
        Toolkit.getDefaultToolkit().addAWTEventListener(watcher[0], AWTEvent.MOUSE_EVENT_MASK);

        hierarchy[0] = ev -> {
            if ((ev.getChangeFlags() & java.awt.event.HierarchyEvent.SHOWING_CHANGED) != 0
                    && !CustomerDashboardHistory.this.isShowing()) {
                closeCheckout.run();
            }
        };
        addHierarchyListener(hierarchy[0]);

        checkout.setOnBackToCartCallback(closeCheckout);

        checkout.setOnOrderPlacedCallback(placed -> {
            closeCheckout.run();
            refreshOrders();
        });

        add(checkout);
        setComponentZOrder(checkout, 0);  
        checkout.setVisible(true);
        revalidate();
        repaint(checkout.getBounds());
    }
    
    @Override
    public boolean isOptimizedDrawingEnabled() {
        return false;
    }

    
    // 10. Panel Actions 
    public void animateOpen() {
        refreshOrders();
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