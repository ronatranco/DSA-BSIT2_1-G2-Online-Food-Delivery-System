/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to edit this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import Model.Admin;
import Model.Admin.*;
import Model.Restaurant;
import Model.Restaurant.MoneyFormat;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

/**
 * @author rownavanana and calalang
 */

public class AdminDashboard extends JPanel implements MouseListener {
    private static final Color PURPLE_DARK = AdminUI.PURPLE_DARK;
    private static final Color YELLOW_ACCENT = AdminUI.YELLOW;
    private static final Color BG_LIGHT = AdminUI.BG_LIGHT;
    private static final Color COLOR_WHITE = Color.WHITE;
    private static final Color COLOR_MUTED = AdminUI.MUTED;
    private static final String SEARCH_PLACEHOLDER = "Search this page...";

    private static final int MIN_WIDTH = 80;
    private static final int MAX_WIDTH = 200;

    private static final String[][] PAGES = {
        {"dashboard", "\uD83D\uDCCA", "Dashboard", "Hello, Admin!"},
        {"accounts", "\uD83D\uDC65", "Accounts", "Account Management"},
        {"restaurants", "\uD83C\uDF7D", "Restaurants", "Restaurants & Locations"},
        {"menus", "\uD83D\uDCCB", "Menu Items", "Menu Items Management"},
        {"orders", "\uD83D\uDCE6", "Orders", "Order Management"},
        {"routes", "\uD83D\uDDFA", "Routes", "Delivery Routes"},
        {"reports", "\uD83D\uDCC8", "Reports", "Reports"},
        {"profile", "\uD83D\uDC64", "Profile", "Admin Profile"}
    };

    private final MainFrame mainFrame;
    private final Admin admin = new Admin();

    private final JPanel pnlMainBg = new JPanel() {
        @Override
        public boolean isOptimizedDrawingEnabled() {
            return false;
        }
    };
    private final JPanel leftBannerNav = new JPanel(null);
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel host = new JPanel(cardLayout);
    private final JLabel lblName = new JLabel();
    private final JPanel pnlSearchBar = new JPanel(null);
    private final JTextField txtSearchBar = new JTextField(SEARCH_PLACEHOLDER);
    private boolean placeholderShown = true;
    private boolean settingSearchText;

    private JButton btnNavLogoPanda, btnNavExit;
    private JLabel lblLogoTitle, lblExit;
    private final List<NavButton> navButtons = new ArrayList<>();
    private final List<JLabel> navLabels = new ArrayList<>();
    private final Map<String, AdminUI.Section> sections = new LinkedHashMap<>();
    private final Map<String, NavButton> navByKey = new LinkedHashMap<>();
    private AdminUI.Section active;

    private javax.swing.Timer animationTimer;
    private double preciseWidth = MIN_WIDTH;
    private boolean targetsExpand = false;
    private long animationStartTime;
    private final double animationDuration = 280.0;
    private double startWidth;

    public AdminDashboard(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(null);
        setBounds(0, 0, 1024, 720);

        pnlMainBg.setLayout(null);
        pnlMainBg.setBackground(BG_LIGHT);
        pnlMainBg.setBounds(0, 0, 1024, 720);

        Admin.ensureDistinctRestaurants();
        buildHeader();
        buildSections();
        buildNav();

        pnlMainBg.add(leftBannerNav);
        pnlMainBg.setComponentZOrder(leftBannerNav, 0);
        add(pnlMainBg);

        addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && isShowing() && active != null) {
                active.onShow();
            }
        });
        showPage("dashboard");
    }

    private void buildHeader() {
        lblName.setFont(AdminUI.font(Font.BOLD, 20));
        lblName.setForeground(PURPLE_DARK);
        lblName.setBounds(110, 34, 560, 35);
        pnlMainBg.add(lblName);

        pnlSearchBar.setBackground(COLOR_WHITE);
        pnlSearchBar.setBorder(BorderFactory.createLineBorder(YELLOW_ACCENT));
        pnlSearchBar.setBounds(110, 80, 420, 35);

        JLabel lblSearchIcon = new JLabel("\uD83D\uDD0D", SwingConstants.CENTER);
        lblSearchIcon.setForeground(PURPLE_DARK);
        lblSearchIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        lblSearchIcon.setBounds(5, 4, 30, 30);
        pnlSearchBar.add(lblSearchIcon);

        txtSearchBar.setFont(AdminUI.font(Font.PLAIN, 14));
        txtSearchBar.setForeground(COLOR_MUTED);
        txtSearchBar.setBackground(COLOR_WHITE);
        txtSearchBar.setBorder(null);
        txtSearchBar.setBounds(40, 3, 375, 29);
        txtSearchBar.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (placeholderShown) {
                    setSearchText("", false);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtSearchBar.getText().trim().isEmpty()) {
                    setSearchText(SEARCH_PLACEHOLDER, true);
                }
            }
        });
        txtSearchBar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                applySearch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                applySearch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                applySearch();
            }
        });
        pnlSearchBar.add(txtSearchBar);
        pnlMainBg.add(pnlSearchBar);

        JLabel lblDateRn = new JLabel(LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy" + " |")), SwingConstants.RIGHT);
        lblDateRn.setFont(AdminUI.font(Font.BOLD, 18));
        lblDateRn.setForeground(PURPLE_DARK);
        lblDateRn.setBounds(754, 38, 220, 35);
        pnlMainBg.add(lblDateRn);
    }

    private void setSearchText(String text, boolean placeholder) {
        settingSearchText = true;
        placeholderShown = placeholder;
        txtSearchBar.setText(text);
        txtSearchBar.setForeground(placeholder ? COLOR_MUTED : AdminUI.TEXT_DARK);
        settingSearchText = false;
    }

    private void applySearch() {
        if (settingSearchText) {
            return;
        }
        String query = placeholderShown ? "" : txtSearchBar.getText().trim();
        filter(active == null ? null : active.table(), query);
    }

    @SuppressWarnings("unchecked")
    private static void filter(JTable table, String query) {
        if (table == null || !(table.getRowSorter() instanceof TableRowSorter)) {
            return;
        }
        ((TableRowSorter<TableModel>) table.getRowSorter()).setRowFilter(
                query.isEmpty() ? null : RowFilter.<TableModel, Integer>regexFilter("(?i)" + Pattern.quote(query)));
    }

    private void buildSections() {
        host.setOpaque(false);
        host.setBounds(110, 130, 880, 550);

        sections.put("dashboard", new AdminOverviewPanel(admin));
        sections.put("accounts", new AdminAccountsPanel(admin));
        sections.put("restaurants", new AdminRestaurantsPanel());
        sections.put("menus", new AdminMenuPanel());
        sections.put("orders", new AdminOrdersPanel());
        sections.put("routes", new AdminRoutesPanel());
        sections.put("reports", new AdminReportsPanel());
        sections.put("profile", new AdminProfilePanel(admin));
        for (Map.Entry<String, AdminUI.Section> entry : sections.entrySet()) {
            host.add((JComponent) entry.getValue(), entry.getKey());
        }
        pnlMainBg.add(host);
    }

    private void showPage(String key) {
        AdminUI.Section previous = active;
        if (previous != null) {
            filter(previous.table(), "");
        }
        setSearchText(SEARCH_PLACEHOLDER, true);

        active = sections.get(key);
        for (String[] page : PAGES) {
            if (page[0].equals(key)) {
                lblName.setText(page[3]);
            }
        }
        navByKey.forEach((k, button) -> button.setActive(k.equals(key)));
        cardLayout.show(host, key);
        if (active != null) {
            active.onShow();
        }

        boolean searchable = active != null && active.table() != null;
        pnlSearchBar.setVisible(searchable);
        txtSearchBar.setEnabled(searchable);
    }

    private void buildNav() {
        leftBannerNav.setBackground(PURPLE_DARK);
        leftBannerNav.setBounds(0, 0, MIN_WIDTH, 720);

        btnNavLogoPanda = new JButton("\uD83D\uDEE1\uFE0F");
        btnNavLogoPanda.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        btnNavLogoPanda.setForeground(YELLOW_ACCENT);
        btnNavLogoPanda.setBounds(0, 30, 80, 40);
        flatten(btnNavLogoPanda);
        leftBannerNav.add(btnNavLogoPanda);

        lblLogoTitle = navLabel("GrabPandaAdmin", 62, 28, 135, 40, 17);
        leftBannerNav.add(lblLogoTitle);

        int y = 100;
        for (String[] page : PAGES) {
            String key = page[0];
            NavButton button = new NavButton(page[1]);
            button.setBounds(0, y, MIN_WIDTH, 45);
            button.addActionListener(e -> showPage(key));
            leftBannerNav.add(button);
            navButtons.add(button);
            navByKey.put(key, button);

            JLabel label = navLabel(page[2], 60, y - 1, 135, 45, 16);
            leftBannerNav.add(label);
            navLabels.add(label);
            button.addMouseListener(this);
            label.addMouseListener(this);
            y += 58;
        }

        btnNavExit = new JButton("[\u2190");
        btnNavExit.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        btnNavExit.setForeground(YELLOW_ACCENT);
        btnNavExit.setBounds(0, 620, 80, 45);
        flatten(btnNavExit);
        leftBannerNav.add(btnNavExit);

        lblExit = navLabel("Logout", 70, 617, 110, 45, 16);
        leftBannerNav.add(lblExit);

        leftBannerNav.addMouseListener(this);
        btnNavLogoPanda.addMouseListener(this);
        btnNavExit.addMouseListener(this);
        lblLogoTitle.addMouseListener(this);
        lblExit.addMouseListener(this);
    }

    private JLabel navLabel(String text, int x, int y, int w, int h, int size) {
        JLabel label = new JLabel(text);
        label.setFont(AdminUI.font(Font.BOLD, size));
        label.setForeground(COLOR_WHITE);
        label.setBounds(x, y, w, h);
        label.setVisible(false);
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return label;
    }

    private void flatten(JButton button) {
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private static class NavButton extends JButton {
        private boolean active;
        private boolean hover;

        NavButton(String icon) {
            super(icon);
            setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
            setForeground(YELLOW_ACCENT);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            setHorizontalAlignment(SwingConstants.LEFT);
            setMargin(new Insets(0, 0, 0, 0));
            setBorder(BorderFactory.createEmptyBorder(0, 26, 0, 0));
        }

        void setActive(boolean active) {
            this.active = active;
            repaint();
        }

        void setHover(boolean hover) {
            this.hover = hover;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (active || hover || getModel().isRollover()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(255, 255, 255, active ? 55 : 30));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(YELLOW_ACCENT);
                g2.fillRect(0, 0, 4, getHeight());
                g2.dispose();
            }
            super.paintComponent(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object source = e.getSource();
        for (int i = 0; i < navLabels.size(); i++) {
            if (source == navLabels.get(i)) {
                navButtons.get(i).doClick();
                return;
            }
        }
        if (source == lblExit) {
            btnNavExit.doClick();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() instanceof JLabel) {
            ((JLabel) e.getSource()).setForeground(new Color(255, 230, 128));
            int i = navLabels.indexOf(e.getSource());
            if (i >= 0) {
                navButtons.get(i).setHover(true);
            }
        }
        if (!targetsExpand) {
            targetsExpand = true;
            setupAnimationTracking();
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        Object source = e.getSource();
        if (source instanceof JLabel) {
            ((JLabel) source).setForeground(COLOR_WHITE);
            int i = navLabels.indexOf(source);
            if (i >= 0) {
                navButtons.get(i).setHover(false);
            }
        }
        Point p = SwingUtilities.convertPoint((Component) source, e.getPoint(), leftBannerNav);
        if (!leftBannerNav.contains(p) && targetsExpand) {
            targetsExpand = false;
            setupAnimationTracking();
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}

    private void setupAnimationTracking() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        animationStartTime = System.currentTimeMillis();
        startWidth = preciseWidth;
        animationTimer = new javax.swing.Timer(4, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double progress = Math.min(1.0, (System.currentTimeMillis() - animationStartTime) / animationDuration);
                double eased = 1.0 - Math.pow(1.0 - progress, 3);
                int target = targetsExpand ? MAX_WIDTH : MIN_WIDTH;
                preciseWidth = startWidth + (target - startWidth) * eased;
                int width = (int) Math.round(preciseWidth);

                if (targetsExpand) {
                    if (progress >= 1.0 || width >= MAX_WIDTH) {
                        width = MAX_WIDTH;
                        preciseWidth = MAX_WIDTH;
                        setLabelsVisible(true);
                        animationTimer.stop();
                    }
                } else {
                    if (lblLogoTitle.isVisible()) {
                        setLabelsVisible(false);
                    }
                    if (progress >= 1.0 || width <= MIN_WIDTH) {
                        width = MIN_WIDTH;
                        preciseWidth = MIN_WIDTH;
                        animationTimer.stop();
                    }
                }
                leftBannerNav.setBounds(0, 0, width, 720);
                for (NavButton button : navButtons) {
                    button.setSize(width, button.getHeight());
                }
                pnlMainBg.repaint();
            }
        });
        animationTimer.start();
    }

    public void setLabelsVisible(boolean visible) {
        lblLogoTitle.setVisible(visible);
        lblExit.setVisible(visible);
        for (JLabel label : navLabels) {
            label.setVisible(visible);
        }
    }

    public JButton getBtnLogout() {
        return btnNavExit;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    private static final class AdminUI {
        static final Color PURPLE_DARK = new Color(74, 20, 140);
        static final Color PURPLE_LIGHT = new Color(156, 39, 176);
        static final Color YELLOW = new Color(255, 193, 7);
        static final Color BG_LIGHT = new Color(248, 245, 250);
        static final Color TEXT_DARK = new Color(30, 30, 30);
        static final Color MUTED = new Color(120, 120, 120);
        static final Color RED = new Color(220, 53, 69);
        static final Color ORANGE = new Color(255, 140, 0);
        static final Color GREEN = new Color(40, 167, 69);

        interface Section {
            JTable table();

            void onShow();
        }

        private AdminUI() {
        }

        static Font font(int style, int size) {
            return new Font("Segoe UI", style, size);
        }

        static JButton button(String text, Color background) {
            JButton button = new JButton(text);
            button.setFont(font(Font.BOLD, 13));
            button.setForeground(Color.WHITE);
            button.setBackground(background);
            button.setOpaque(true);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            return button;
        }

        static JLabel label(String text) {
            JLabel label = new JLabel(text);
            label.setFont(font(Font.BOLD, 13));
            label.setForeground(TEXT_DARK);
            return label;
        }

        static JLabel hint(String text) {
            JLabel label = new JLabel(text);
            label.setFont(font(Font.ITALIC, 12));
            label.setForeground(MUTED);
            return label;
        }

        static DefaultTableModel readOnlyModel(String... columns) {
            return new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
        }

        static JTable table(DefaultTableModel model) {
            JTable table = new JTable(model) {
                @Override
                public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                    Component c = super.prepareRenderer(renderer, row, column);
                    if (isRowSelected(row)) {
                        c.setBackground(new Color(230, 220, 245));
                        c.setForeground(PURPLE_DARK);
                    } else {
                        c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(250, 248, 252));
                        c.setForeground(TEXT_DARK);
                    }
                    return c;
                }
            };
            table.setRowHeight(32);
            table.setFont(font(Font.PLAIN, 13));
            table.setShowVerticalLines(true);
            table.setGridColor(new Color(225, 220, 235));
            table.setAutoCreateRowSorter(true);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.setFillsViewportHeight(true);

            DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable t, Object value, boolean selected,
                                                               boolean focus, int row, int column) {
                    JLabel label = (JLabel) super.getTableCellRendererComponent(t, value, selected, focus, row, column);
                    label.setBackground(PURPLE_DARK);
                    label.setForeground(Color.WHITE);
                    label.setFont(font(Font.BOLD, 13));
                    label.setHorizontalAlignment(JLabel.CENTER);
                    label.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, YELLOW));
                    return label;
                }
            };
            JTableHeader header = table.getTableHeader();
            header.setDefaultRenderer(headerRenderer);
            header.setPreferredSize(new Dimension(0, 38));
            header.setReorderingAllowed(false);

            DefaultTableCellRenderer center = new DefaultTableCellRenderer();
            center.setHorizontalAlignment(JLabel.CENTER);
            table.setDefaultRenderer(Object.class, center);
            table.setDefaultRenderer(Integer.class, center);
            table.setDefaultRenderer(Long.class, center);
            return table;
        }

        static void styleCombo(JComboBox<String> box, int width) {
            box.setFont(font(Font.PLAIN, 13));
            box.setBackground(Color.WHITE);
            box.setForeground(TEXT_DARK);
            box.setOpaque(true);
            box.setBorder(BorderFactory.createLineBorder(PURPLE_DARK, 1));
            box.setMaximumRowCount(8);
            box.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            box.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
                @Override
                protected JButton createArrowButton() {
                    JButton arrow = new JButton() {
                        @Override
                        protected void paintComponent(Graphics g) {
                            Graphics2D g2 = (Graphics2D) g.create();
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            g2.setColor(PURPLE_DARK);
                            g2.fillRect(0, 0, getWidth(), getHeight());
                            g2.setColor(YELLOW);
                            int cx = getWidth() / 2, cy = getHeight() / 2;
                            g2.fillPolygon(new int[]{cx - 5, cx + 5, cx}, new int[]{cy - 3, cy - 3, cy + 4}, 3);
                            g2.dispose();
                        }
                    };
                    arrow.setBorder(BorderFactory.createEmptyBorder());
                    arrow.setFocusPainted(false);
                    arrow.setContentAreaFilled(false);
                    arrow.setPreferredSize(new Dimension(30, 30));
                    return arrow;
                }

                @Override
                public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
                    g.setColor(Color.WHITE);
                    g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
                }

                @Override
                protected javax.swing.plaf.basic.ComboPopup createPopup() {
                    javax.swing.plaf.basic.BasicComboPopup popup = new javax.swing.plaf.basic.BasicComboPopup(comboBox);
                    popup.setBorder(BorderFactory.createLineBorder(PURPLE_DARK, 1));
                    return popup;
                }
            });
            box.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                              boolean selected, boolean focus) {
                    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, selected, false);
                    label.setFont(font(Font.PLAIN, 13));
                    label.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
                    label.setOpaque(true);
                    boolean highlight = selected && index >= 0;
                    label.setBackground(highlight ? new Color(230, 220, 245) : Color.WHITE);
                    label.setForeground(highlight ? PURPLE_DARK : TEXT_DARK);
                    return label;
                }
            });
            if (width > 0) {
                box.setPreferredSize(new Dimension(width, 32));
            } else {
                box.setPreferredSize(new Dimension(box.getPreferredSize().width, 32));
            }
        }

        static JScrollPane scroll(JTable table) {
            JScrollPane pane = new JScrollPane(table);
            pane.setBorder(BorderFactory.createLineBorder(PURPLE_DARK, 1));
            pane.getViewport().setBackground(Color.WHITE);
            return pane;
        }

        static JPanel buttonBar(JComponent... components) {
            JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            bar.setOpaque(false);
            for (JComponent component : components) {
                bar.add(component);
            }
            return bar;
        }

        static int selectedModelRow(Component parent, JTable table, String what) {
            int row = table.getSelectedRow();
            if (row < 0) {
                warn(parent, "Please select " + what + " from the table first.");
                return -1;
            }
            return table.convertRowIndexToModel(row);
        }

        static void warn(Component parent, String message) {
            JOptionPane.showMessageDialog(parent, message, "Notice", JOptionPane.WARNING_MESSAGE);
        }

        static void info(Component parent, String message) {
            JOptionPane.showMessageDialog(parent, message, "Done", JOptionPane.INFORMATION_MESSAGE);
        }

        static void error(Component parent, String message) {
            JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
        }

        static boolean confirm(Component parent, String message) {
            return JOptionPane.showConfirmDialog(parent, message, "Please confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
        }

        static boolean form(Component parent, String title, String[] labels, JComponent[] fields) {
            JPanel panel = new JPanel(new GridLayout(labels.length, 2, 10, 10));
            for (int i = 0; i < labels.length; i++) {
                panel.add(label(labels[i]));
                panel.add(fields[i]);
            }
            return JOptionPane.showConfirmDialog(parent, panel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION;
        }
    }

    private static final class AdminOverviewPanel extends JPanel implements AdminUI.Section {
        private final Admin admin;
        private final JLabel customers = value(), restaurantAccounts = value(), orders = value(),
                pending = value(), revenue = value(), dishes = value();

        AdminOverviewPanel(Admin admin) {
            super(new GridLayout(2, 3, 20, 20));
            this.admin = admin;
            setOpaque(false);
            add(card("Total Customers", customers, "Registered customer accounts"));
            add(card("Restaurant Accounts", restaurantAccounts, "Registered restaurant owners"));
            add(card("Total Orders", orders, "Across all restaurants"));
            add(card("Pending Orders", pending, "Waiting to be accepted"));
            add(card("Delivered Revenue", revenue, "From delivered orders"));
            add(card("Available Menu Items", dishes, "Currently orderable"));

            for (Restaurant.Brand brand : Restaurant.Brand.values()) {
                Restaurant.get(brand).addListener(this::refreshRestaurantFigures);
            }
            refreshRestaurantFigures();
        }

        @Override
        public JTable table() {
            return null;
        }

        @Override
        public void onShow() {
            try {
                customers.setText(String.valueOf(admin.countAccounts(Admin.ROLE_CUSTOMER)));
                restaurantAccounts.setText(String.valueOf(admin.countAccounts(Admin.ROLE_RESTAURANT)));
            } catch (SQLException ex) {
                customers.setText("\u2013");
                restaurantAccounts.setText("\u2013");
            }
            refreshRestaurantFigures();
        }

        private void refreshRestaurantFigures() {
            int totalOrders = 0, pendingOrders = 0, availableDishes = 0;
            double delivered = 0;
            for (Restaurant.Brand brand : Restaurant.Brand.values()) {
                Restaurant data = Restaurant.get(brand);
                totalOrders += data.orders.size();
                for (Restaurant.Order o : data.orders) {
                    if (Restaurant.Order.PENDING.equals(o.status)) {
                        pendingOrders++;
                    } else if (Restaurant.Order.COMPLETED.equals(o.status)) {
                        delivered += o.total;
                    }
                }
                for (Restaurant.Dish dish : data.restaurant.menuItems) {
                    if (dish.available) {
                        availableDishes++;
                    }
                }
            }
            orders.setText(String.valueOf(totalOrders));
            pending.setText(String.valueOf(pendingOrders));
            revenue.setText(MoneyFormat.peso(delivered));
            dishes.setText(String.valueOf(availableDishes));
        }

        private static JLabel value() {
            JLabel label = new JLabel("0", SwingConstants.CENTER);
            label.setFont(AdminUI.font(Font.BOLD, 30));
            label.setForeground(AdminUI.PURPLE_DARK);
            return label;
        }

        private static JPanel card(String title, JLabel value, String caption) {
            JPanel card = new JPanel(new BorderLayout(0, 8));
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 4, 0, AdminUI.YELLOW),
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(220, 210, 230), 1),
                            BorderFactory.createEmptyBorder(18, 16, 14, 16))));
            JLabel name = new JLabel(title, SwingConstants.CENTER);
            name.setFont(AdminUI.font(Font.BOLD, 14));
            name.setForeground(AdminUI.MUTED);
            JLabel sub = new JLabel(caption, SwingConstants.CENTER);
            sub.setFont(AdminUI.font(Font.ITALIC, 12));
            sub.setForeground(AdminUI.MUTED);
            card.add(name, BorderLayout.NORTH);
            card.add(value, BorderLayout.CENTER);
            card.add(sub, BorderLayout.SOUTH);
            return card;
        }
    }

    private static final class AdminAccountsPanel extends JPanel implements AdminUI.Section {
        private final Admin admin;
        private final DefaultTableModel model =
                AdminUI.readOnlyModel("ID", "Full Name", "Username", "Email", "Phone", "Address", "Status");
        private final JTable table = AdminUI.table(model);
        private final JComboBox<String> type = new JComboBox<>(new String[]{"Customers", "Restaurants"});
        private final List<Admin.Account> shown = new ArrayList<>();

        AdminAccountsPanel(Admin admin) {
            super(new BorderLayout(0, 12));
            this.admin = admin;
            setOpaque(false);

            AdminUI.styleCombo(type, 170);
            type.addActionListener(e -> refresh());
            JPanel top = AdminUI.buttonBar(AdminUI.label("Show accounts of:"), type);
            top.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

            JButton add = AdminUI.button("Add Account", AdminUI.PURPLE_DARK);
            add.addActionListener(e -> addAccount());
            JButton edit = AdminUI.button("Edit Account", AdminUI.PURPLE_LIGHT);
            edit.addActionListener(e -> editSelected());
            JButton ban = AdminUI.button("Ban", AdminUI.ORANGE);
            ban.addActionListener(e -> setStatusOfSelected(Admin.STATUS_BANNED));
            JButton activate = AdminUI.button("Activate", AdminUI.GREEN);
            activate.addActionListener(e -> setStatusOfSelected(Admin.STATUS_ACTIVE));
            JButton delete = AdminUI.button("Delete", AdminUI.RED);
            delete.addActionListener(e -> deleteSelected());
            JButton refresh = AdminUI.button("Refresh", AdminUI.PURPLE_DARK);
            refresh.addActionListener(e -> refresh());

            add(top, BorderLayout.NORTH);
            add(AdminUI.scroll(table), BorderLayout.CENTER);
            add(AdminUI.buttonBar(add, edit, ban, activate, delete, refresh), BorderLayout.SOUTH);
        }

        @Override
        public JTable table() {
            return table;
        }

        @Override
        public void onShow() {
            refresh();
        }

        private String role() {
            return type.getSelectedIndex() == 0 ? Admin.ROLE_CUSTOMER : Admin.ROLE_RESTAURANT;
        }

        private void refresh() {
            model.setRowCount(0);
            shown.clear();
            try {
                for (Admin.Account a : admin.listAccounts(role())) {
                    shown.add(a);
                    model.addRow(new Object[]{a.id, a.fullName, a.username, a.email, a.phone, a.address, a.status});
                }
            } catch (SQLException ex) {
                AdminUI.error(this, "Could not load accounts: " + ex.getMessage());
            }
        }

        private Admin.Account selected() {
            int row = AdminUI.selectedModelRow(this, table, "an account");
            return row < 0 ? null : shown.get(row);
        }

        private void addAccount() {
            JTextField name = new JTextField(), username = new JTextField(), email = new JTextField(),
                    phone = new JTextField(), address = new JTextField();
            JPasswordField password = new JPasswordField();
            boolean ok = AdminUI.form(this, "Add " + (type.getSelectedIndex() == 0 ? "Customer" : "Restaurant") + " Account",
                    new String[]{"Full name:", "Username:", "Email (@gmail.com):", "Phone (11 digits):", "Address:", "Password:"},
                    new JComponent[]{name, username, email, phone, address, password});
            if (!ok) {
                return;
            }
            String pass = new String(password.getPassword());
            if (username.getText().trim().isEmpty() || pass.isEmpty()) {
                AdminUI.warn(this, "Username and password are required.");
                return;
            }
            if (!valid(name, email, phone, address)) {
                return;
            }
            try {
                admin.addAccount(role(), name.getText().trim(), username.getText().trim(), email.getText().trim(),
                        phone.getText().trim(), address.getText().trim(), pass);
                refresh();
                AdminUI.info(this, "Account created.");
            } catch (SQLException ex) {
                AdminUI.error(this, ex.getErrorCode() == 1062 ? "That username or email already exists."
                        : "Could not create the account: " + ex.getMessage());
            }
        }

        private void editSelected() {
            Admin.Account a = selected();
            if (a == null) {
                return;
            }
            JTextField name = new JTextField(a.fullName), email = new JTextField(a.email),
                    phone = new JTextField(a.phone), address = new JTextField(a.address);
            JTextField username = new JTextField(a.username);
            username.setEditable(false);
            boolean ok = AdminUI.form(this, "Edit Account",
                    new String[]{"Username:", "Full name:", "Email (@gmail.com):", "Phone (11 digits):", "Address:"},
                    new JComponent[]{username, name, email, phone, address});
            if (!ok || !valid(name, email, phone, address)) {
                return;
            }
            try {
                admin.updateAccount(a.id, name.getText().trim(), email.getText().trim(), phone.getText().trim(), address.getText().trim());
                refresh();
                AdminUI.info(this, "Account updated.");
            } catch (SQLException ex) {
                AdminUI.error(this, ex.getErrorCode() == 1062 ? "That email already belongs to another account."
                        : "Could not update the account: " + ex.getMessage());
            }
        }

        private void setStatusOfSelected(String status) {
            Admin.Account a = selected();
            if (a == null) {
                return;
            }
            if (status.equals(a.status)) {
                AdminUI.info(this, a.username + " is already " + status + ".");
                return;
            }
            if (!AdminUI.confirm(this, (Admin.STATUS_BANNED.equals(status) ? "Ban " : "Activate ") + a.username + "?")) {
                return;
            }
            try {
                admin.setStatus(a.id, status);
                refresh();
            } catch (SQLException ex) {
                AdminUI.error(this, "Could not change the status: " + ex.getMessage());
            }
        }

        private void deleteSelected() {
            Admin.Account a = selected();
            if (a == null || !AdminUI.confirm(this, "Permanently delete " + a.username + " (ID " + a.id + ")?")) {
                return;
            }
            try {
                admin.deleteAccount(a.id);
                refresh();
            } catch (SQLException ex) {
                AdminUI.error(this, "Could not delete the account: " + ex.getMessage());
            }
        }

        private boolean valid(JTextField name, JTextField email, JTextField phone, JTextField address) {
            if (name.getText().trim().isEmpty() || email.getText().trim().isEmpty()
                    || phone.getText().trim().isEmpty() || address.getText().trim().isEmpty()) {
                AdminUI.warn(this, "Please fill in all fields.");
                return false;
            }
            if (!email.getText().trim().toLowerCase().endsWith("@gmail.com")) {
                AdminUI.warn(this, "Email must be a @gmail.com address.");
                return false;
            }
            if (!phone.getText().trim().matches("\\d{11}")) {
                AdminUI.warn(this, "Phone number must be 11 digits.");
                return false;
            }
            return true;
        }
    }

    private static final class AdminRestaurantsPanel extends JPanel implements AdminUI.Section {
        private final DefaultTableModel model = AdminUI.readOnlyModel(
                "Restaurant", "Location", "Owner", "Contact", "Cuisine", "Menu Items", "Orders");
        private final JTable table = AdminUI.table(model);
        private final JComboBox<String> picker = new JComboBox<>();
        private final JTextField name = field(), owner = field(), contact = field(), cuisine = field();
        private final JComboBox<String> location = new JComboBox<>(Admin.ZONES);
        private final JLabel notice = AdminUI.hint(" ");
        private boolean loading;

        AdminRestaurantsPanel() {
            super(new BorderLayout(0, 12));
            setOpaque(false);

            AdminUI.styleCombo(picker, 260);
            AdminUI.styleCombo(location, 0);
            picker.addActionListener(e -> {
                if (!loading) {
                    loadFields();
                    say(" ", AdminUI.MUTED);
                }
            });
            table.getSelectionModel().addListSelectionListener(e -> {
                int row = table.getSelectedRow();
                if (!e.getValueIsAdjusting() && !loading && row >= 0) {
                    picker.setSelectedIndex(table.convertRowIndexToModel(row));
                }
            });

            JButton save = AdminUI.button("Save Changes", AdminUI.PURPLE_DARK);
            save.addActionListener(e -> save());
            JButton undo = AdminUI.button("Undo Changes", AdminUI.PURPLE_LIGHT);
            undo.addActionListener(e -> {
                loadFields();
                say("Changes were undone.", AdminUI.MUTED);
            });

            JPanel form = new JPanel(new GridLayout(0, 4, 15, 14));
            form.setOpaque(false);
            form.add(AdminUI.label("Restaurant Name:"));
            form.add(name);
            form.add(AdminUI.label("Location (Zone A\u2013C):"));
            form.add(location);
            form.add(AdminUI.label("Owner Name:"));
            form.add(owner);
            form.add(AdminUI.label("Contact Number:"));
            form.add(contact);
            form.add(AdminUI.label("Cuisine:"));
            form.add(cuisine);
            form.add(new JLabel(""));
            form.add(AdminUI.buttonBar(save, undo));

            JPanel card = new JPanel(new BorderLayout(0, 8));
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 4, 0, AdminUI.YELLOW),
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(220, 210, 230), 1),
                            BorderFactory.createEmptyBorder(16, 20, 12, 20))));
            card.add(form, BorderLayout.CENTER);
            card.add(notice, BorderLayout.SOUTH);

            JPanel top = new JPanel(new BorderLayout(0, 10));
            top.setOpaque(false);
            top.add(AdminUI.buttonBar(AdminUI.label("Restaurant:"), picker), BorderLayout.NORTH);
            top.add(card, BorderLayout.CENTER);

            add(top, BorderLayout.NORTH);
            add(AdminUI.scroll(table), BorderLayout.CENTER);
            add(AdminUI.hint("Restaurants keep managing their own menu and incoming orders; changes made here show up on their dashboard right away."),
                    BorderLayout.SOUTH);

            for (Restaurant.Brand brand : Restaurant.Brand.values()) {
                Restaurant.get(brand).addListener(this::dataChanged);
            }
            dataChanged();
            loadFields();
        }

        private static JTextField field() {
            JTextField f = new JTextField();
            f.setFont(AdminUI.font(Font.PLAIN, 14));
            return f;
        }

        private void say(String message, Color color) {
            notice.setText(message);
            notice.setForeground(color);
        }

        @Override
        public JTable table() {
            return table;
        }

        @Override
        public void onShow() {
            dataChanged();
            loadFields();
            say(" ", AdminUI.MUTED);
        }

        private Restaurant current() {
            return Restaurant.get(Restaurant.Brand.values()[Math.max(0, picker.getSelectedIndex())]);
        }

        private void dataChanged() {
            loading = true;
            int keep = Math.max(0, picker.getSelectedIndex());
            picker.removeAllItems();
            model.setRowCount(0);
            for (Restaurant.Brand brand : Restaurant.Brand.values()) {
                Restaurant data = Restaurant.get(brand);
                Restaurant.Restaurantt r = data.restaurant;
                picker.addItem(r.name);
                model.addRow(new Object[]{r.name, r.location, r.ownerName, r.contact, r.cuisine,
                    r.menuItems.size(), data.orders.size()});
            }
            picker.setSelectedIndex(keep);
            loading = false;
        }

        private void loadFields() {
            Restaurant.Restaurantt r = current().restaurant;
            name.setText(r.name);
            owner.setText(r.ownerName);
            contact.setText(r.contact);
            cuisine.setText(r.cuisine);
            int zone = java.util.Arrays.asList(Admin.ZONES).indexOf(r.location);
            location.setSelectedIndex(zone);
        }

        private void save() {
            if (name.getText().trim().isEmpty()) {
                AdminUI.warn(this, "Restaurant name can't be empty.");
                return;
            }
            if (location.getSelectedIndex() < 0) {
                AdminUI.warn(this, "Please choose the restaurant's location: Zone A, Zone B or Zone C.");
                return;
            }
            Restaurant data = current();
            Restaurant.Restaurantt r = data.restaurant;
            r.name = name.getText().trim();
            r.location = String.valueOf(location.getSelectedItem());
            r.ownerName = owner.getText().trim();
            r.contact = contact.getText().trim();
            r.cuisine = cuisine.getText().trim();
            data.notifyChanged();
            say(r.name + " was updated successfully!", AdminUI.GREEN);
        }
    }

    private static final class AdminMenuPanel extends JPanel implements AdminUI.Section {
        private final DefaultTableModel model =
                AdminUI.readOnlyModel("Food Name", "Category", "Price", "Availability", "Sold");
        private final JTable table = AdminUI.table(model);
        private final JComboBox<String> restaurantBox = new JComboBox<>();
        private boolean loading;

        AdminMenuPanel() {
            super(new BorderLayout(0, 12));
            setOpaque(false);

            AdminUI.styleCombo(restaurantBox, 240);
            restaurantBox.addActionListener(e -> {
                if (!loading) {
                    refresh();
                }
            });

            JButton add = AdminUI.button("Add Item", AdminUI.PURPLE_DARK);
            add.addActionListener(e -> openForm(null));
            JButton edit = AdminUI.button("Edit / Change Price", AdminUI.PURPLE_LIGHT);
            edit.addActionListener(e -> {
                Restaurant.Dish dish = selected();
                if (dish != null) {
                    openForm(dish);
                }
            });
            JButton toggle = AdminUI.button("Toggle Availability", AdminUI.ORANGE);
            toggle.addActionListener(e -> toggleSelected());
            JButton delete = AdminUI.button("Delete Item", AdminUI.RED);
            delete.addActionListener(e -> deleteSelected());

            add(AdminUI.buttonBar(AdminUI.label("Restaurant:"), restaurantBox), BorderLayout.NORTH);
            add(AdminUI.scroll(table), BorderLayout.CENTER);
            add(AdminUI.buttonBar(add, edit, toggle, delete), BorderLayout.SOUTH);

            for (Restaurant.Brand brand : Restaurant.Brand.values()) {
                Restaurant.get(brand).addListener(this::refresh);
            }
            reloadRestaurants();
        }

        @Override
        public JTable table() {
            return table;
        }

        @Override
        public void onShow() {
            reloadRestaurants();
        }

        private Restaurant current() {
            int index = Math.max(0, restaurantBox.getSelectedIndex());
            return Restaurant.get(Restaurant.Brand.values()[index]);
        }

        private void reloadRestaurants() {
            loading = true;
            int keep = Math.max(0, restaurantBox.getSelectedIndex());
            restaurantBox.removeAllItems();
            for (Restaurant.Brand brand : Restaurant.Brand.values()) {
                restaurantBox.addItem(Restaurant.get(brand).restaurant.name);
            }
            restaurantBox.setSelectedIndex(keep);
            loading = false;
            refresh();
        }

        private void refresh() {
            model.setRowCount(0);
            for (Restaurant.Dish dish : current().restaurant.menuItems) {
                model.addRow(new Object[]{dish.name, dish.category, MoneyFormat.peso(dish.price),
                    dish.available ? "Available" : "Unavailable", dish.sold});
            }
        }

        private Restaurant.Dish selected() {
            int row = AdminUI.selectedModelRow(this, table, "a menu item");
            return row < 0 ? null : current().restaurant.menuItems.get(row);
        }

        private void openForm(Restaurant.Dish dish) {
            Restaurant data = current();
            JTextField name = new JTextField(dish == null ? "" : dish.name);
            JComboBox<String> category = new JComboBox<>(data.categories.toArray(new String[0]));
            category.setEditable(true);
            category.setSelectedItem(dish == null ? "" : dish.category);
            JTextField price = new JTextField(dish == null ? "" : MoneyFormat.amountText(dish.price));
            JCheckBox available = new JCheckBox("Available for ordering", dish == null || dish.available);
            JTextField description = new JTextField(dish == null ? "" : dish.description);

            boolean ok = AdminUI.form(this, (dish == null ? "Add Item to " : "Edit Item of ") + data.restaurant.name,
                    new String[]{"Food name:", "Category:", "Price (\u20B1):", "Availability:", "Description:"},
                    new JComponent[]{name, category, price, available, description});
            if (!ok) {
                return;
            }
            String foodName = name.getText().trim();
            String foodCategory = String.valueOf(category.getSelectedItem()).trim();
            Double foodPrice = MoneyFormat.parseAmount(price.getText());
            if (foodName.isEmpty() || foodCategory.isEmpty()) {
                AdminUI.warn(this, "Food name and category are required.");
                return;
            }
            if (foodPrice == null || foodPrice < 0.0) {
                AdminUI.warn(this, "Please enter a valid price, for example 120 or 99.50.");
                return;
            }
            data.addCategoryIfNew(foodCategory);
            Restaurant.Dish target = dish != null ? dish
                    : new Restaurant.Dish(foodName, foodCategory, foodPrice, available.isSelected(), 0);
            target.name = foodName;
            target.category = foodCategory;
            target.price = foodPrice;
            target.available = available.isSelected();
            target.description = description.getText().trim();
            if (dish == null) {
                data.restaurant.menuItems.add(target);
            }
            data.notifyChanged();
        }

        private void toggleSelected() {
            Restaurant.Dish dish = selected();
            if (dish != null) {
                dish.available = !dish.available;
                current().notifyChanged();
            }
        }

        private void deleteSelected() {
            Restaurant.Dish dish = selected();
            if (dish != null && AdminUI.confirm(this, "Delete \"" + dish.name + "\" from the menu?")) {
                current().restaurant.menuItems.remove(dish);
                current().notifyChanged();
            }
        }
    }

    private static final class AdminOrdersPanel extends JPanel implements AdminUI.Section {
        private static final String[] STATUS_FILTER =
                {"All", "Pending", "Preparing", "Out for Delivery", "Delivered", "Cancelled", "Failed"};

        private final DefaultTableModel model = AdminUI.readOnlyModel(
                "Restaurant", "Order ID", "Customer", "Items", "Total", "Payment", "Time", "Status");
        private final JTable table = AdminUI.table(model);
        private final JComboBox<String> restaurantFilter = new JComboBox<>();
        private final JComboBox<String> statusFilter = new JComboBox<>(STATUS_FILTER);
        private final JLabel summary = AdminUI.hint(" ");

        private final List<Restaurant> shownRestaurants = new ArrayList<>();
        private final List<Restaurant.Order> shownOrders = new ArrayList<>();
        private boolean loading;

        AdminOrdersPanel() {
            super(new BorderLayout(0, 12));
            setOpaque(false);

            AdminUI.styleCombo(restaurantFilter, 240);
            AdminUI.styleCombo(statusFilter, 170);
            restaurantFilter.addActionListener(e -> {
                if (!loading) {
                    refresh();
                }
            });
            statusFilter.addActionListener(e -> refresh());

            JPanel filters = AdminUI.buttonBar(AdminUI.label("Restaurant:"), restaurantFilter,
                    AdminUI.label("Status:"), statusFilter);
            JPanel north = new JPanel(new BorderLayout(0, 6));
            north.setOpaque(false);
            north.add(filters, BorderLayout.NORTH);
            north.add(summary, BorderLayout.SOUTH);

            add(north, BorderLayout.NORTH);
            add(AdminUI.scroll(table), BorderLayout.CENTER);

            for (Restaurant.Brand brand : Restaurant.Brand.values()) {
                Restaurant.get(brand).addListener(this::refresh);
            }
            reloadRestaurants();
        }

        @Override
        public JTable table() {
            return table;
        }

        @Override
        public void onShow() {
            reloadRestaurants();
        }

        private void reloadRestaurants() {
            loading = true;
            int keep = Math.max(0, restaurantFilter.getSelectedIndex());
            restaurantFilter.removeAllItems();
            restaurantFilter.addItem("All restaurants");
            for (Restaurant.Brand brand : Restaurant.Brand.values()) {
                restaurantFilter.addItem(Restaurant.get(brand).restaurant.name);
            }
            restaurantFilter.setSelectedIndex(keep);
            loading = false;
            refresh();
        }

        private void refresh() {
            model.setRowCount(0);
            shownRestaurants.clear();
            shownOrders.clear();
            int onlyBrand = restaurantFilter.getSelectedIndex() - 1;
            String status = String.valueOf(statusFilter.getSelectedItem());
            int pending = 0, active = 0, delivered = 0, closedOther = 0;

            Restaurant.Brand[] brands = Restaurant.Brand.values();
            for (int i = 0; i < brands.length; i++) {
                if (onlyBrand >= 0 && onlyBrand != i) {
                    continue;
                }
                Restaurant data = Restaurant.get(brands[i]);
                for (Restaurant.Order o : data.orders) {
                    if (Restaurant.Order.PENDING.equals(o.status)) {
                        pending++;
                    } else if (Restaurant.Order.COMPLETED.equals(o.status)) {
                        delivered++;
                    } else if (o.isFinal()) {
                        closedOther++;
                    } else {
                        active++;
                    }
                    if (!"All".equals(status) && !status.equals(o.status)) {
                        continue;
                    }
                    shownRestaurants.add(data);
                    shownOrders.add(o);
                    model.addRow(new Object[]{data.restaurant.name, o.id, o.customer, o.items,
                        MoneyFormat.peso(o.total), o.payment, o.time, o.status});
                }
            }
            summary.setText("Pending: " + pending + "   \u2022   In progress: " + active + "   \u2022   Delivered: " + delivered
                    + "   \u2022   Cancelled / failed: " + closedOther);
        }
    }

    private static final class AdminRoutesPanel extends JPanel implements AdminUI.Section {
        private final DeliveryGraph graph;
        private final String[] hubNames = new String[Restaurant.Brand.values().length];

        private final DefaultTableModel model = AdminUI.readOnlyModel("From", "To", "Distance (km)", "Est. time (min)");
        private final JTable table = AdminUI.table(model);
        private final List<DeliveryGraph.Road> shown = new ArrayList<>();

        private final JComboBox<String> fromBox = new JComboBox<>();
        private final JComboBox<String> toBox = new JComboBox<>();
        private final JTextArea result = new JTextArea(5, 30);
        private final JLabel coverage = AdminUI.hint(" ");

        AdminRoutesPanel() {
            super(new BorderLayout(0, 12));
            setOpaque(false);

            List<String> names = new ArrayList<>();
            Restaurant.Brand[] brands = Restaurant.Brand.values();
            for (int i = 0; i < brands.length; i++) {
                hubNames[i] = Restaurant.get(brands[i]).restaurant.name;
                names.add(hubNames[i]);
            }
            graph = DeliveryGraph.createDefault(names);

            JButton addLocation = AdminUI.button("Add Location", AdminUI.PURPLE_DARK);
            addLocation.addActionListener(e -> addLocation());
            JButton removeLocation = AdminUI.button("Remove Location", AdminUI.RED);
            removeLocation.addActionListener(e -> removeLocation());
            JButton addRoad = AdminUI.button("Add / Update Road", AdminUI.PURPLE_LIGHT);
            addRoad.addActionListener(e -> addRoad());
            JButton removeRoad = AdminUI.button("Remove Road", AdminUI.ORANGE);
            removeRoad.addActionListener(e -> removeRoad());

            JPanel left = new JPanel(new BorderLayout(0, 10));
            left.setOpaque(false);
            left.add(AdminUI.label("Road network"), BorderLayout.NORTH);
            left.add(AdminUI.scroll(table), BorderLayout.CENTER);
            JPanel leftButtons = new JPanel(new GridLayout(2, 2, 8, 8));
            leftButtons.setOpaque(false);
            leftButtons.add(addLocation);
            leftButtons.add(removeLocation);
            leftButtons.add(addRoad);
            leftButtons.add(removeRoad);
            left.add(leftButtons, BorderLayout.SOUTH);

            AdminUI.styleCombo(fromBox, 0);
            AdminUI.styleCombo(toBox, 0);
            JButton find = AdminUI.button("Find Shortest Route", AdminUI.GREEN);
            find.addActionListener(e -> findRoute());
            JButton nearest = AdminUI.button("Nearest Restaurant to Destination", AdminUI.PURPLE_DARK);
            nearest.addActionListener(e -> findNearestHub());

            result.setEditable(false);
            result.setLineWrap(true);
            result.setWrapStyleWord(true);
            result.setFont(AdminUI.font(Font.PLAIN, 14));
            result.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
            result.setText("Pick a start and a destination, then press a button.");

            JPanel picks = new JPanel(new GridLayout(4, 1, 0, 6));
            picks.setOpaque(false);
            picks.add(AdminUI.label("From (start)"));
            picks.add(fromBox);
            picks.add(AdminUI.label("To (destination)"));
            picks.add(toBox);
            JPanel actions = new JPanel(new GridLayout(2, 1, 0, 8));
            actions.setOpaque(false);
            actions.add(find);
            actions.add(nearest);

            JPanel finder = new JPanel(new BorderLayout(0, 10));
            finder.setOpaque(false);
            finder.add(picks, BorderLayout.NORTH);
            JPanel middle = new JPanel(new BorderLayout(0, 10));
            middle.setOpaque(false);
            middle.add(actions, BorderLayout.NORTH);
            middle.add(new JScrollPane(result), BorderLayout.CENTER);
            finder.add(middle, BorderLayout.CENTER);
            finder.add(coverage, BorderLayout.SOUTH);
            finder.setPreferredSize(new Dimension(330, 100));
            finder.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

            JPanel right = new JPanel(new BorderLayout(0, 10));
            right.setOpaque(false);
            right.add(AdminUI.label("Route finder"), BorderLayout.NORTH);
            right.add(finder, BorderLayout.CENTER);

            JPanel body = new JPanel(new BorderLayout(18, 0));
            body.setOpaque(false);
            body.add(left, BorderLayout.CENTER);
            body.add(right, BorderLayout.EAST);
            add(body, BorderLayout.CENTER);
            refresh();
        }

        @Override
        public JTable table() {
            return table;
        }

        @Override
        public void onShow() {
            syncRestaurantNames();
            refresh();
        }

        private void syncRestaurantNames() {
            Restaurant.Brand[] brands = Restaurant.Brand.values();
            for (int i = 0; i < brands.length; i++) {
                String current = Restaurant.get(brands[i]).restaurant.name;
                if (!current.equals(hubNames[i]) && graph.renameLocation(hubNames[i], current)) {
                    hubNames[i] = current;
                }
            }
        }

        private void refresh() {
            model.setRowCount(0);
            shown.clear();
            for (DeliveryGraph.Road road : graph.roads()) {
                shown.add(road);
                model.addRow(new Object[]{road.a, road.b, String.format("%.1f", road.km),
                    (int) Math.ceil(road.km / DeliveryGraph.AVG_SPEED_KMH * 60.0)});
            }
            Object from = fromBox.getSelectedItem(), to = toBox.getSelectedItem();
            fromBox.removeAllItems();
            toBox.removeAllItems();
            for (String location : graph.locations()) {
                String label = location;
                fromBox.addItem(label);
                toBox.addItem(label);
            }
            if (from != null && graph.hasLocation(String.valueOf(from))) {
                fromBox.setSelectedItem(from);
            }
            if (to != null && graph.hasLocation(String.valueOf(to))) {
                toBox.setSelectedItem(to);
            }
            List<String> cut = graph.unreachableFromHubs();
            coverage.setText(cut.isEmpty() ? "All locations can be reached from a restaurant."
                    : "<html>Not reachable from any restaurant: " + String.join(", ", cut) + "</html>");
            coverage.setForeground(cut.isEmpty() ? AdminUI.GREEN : AdminUI.RED);
        }

        private void findRoute() {
            String from = (String) fromBox.getSelectedItem(), to = (String) toBox.getSelectedItem();
            if (from == null || to == null) {
                return;
            }
            if (from.equals(to)) {
                result.setText("Start and destination are the same place.");
                return;
            }
            DeliveryGraph.Route route = graph.shortestRoute(from, to);
            result.setText(route == null
                    ? "No route exists between " + from + " and " + to + ". Add a road to connect them."
                    : describe(route));
        }

        private void findNearestHub() {
            String to = (String) toBox.getSelectedItem();
            if (to == null) {
                return;
            }
            if (graph.isHub(to)) {
                result.setText(to + " is itself a restaurant.");
                return;
            }
            DeliveryGraph.Route route = graph.nearestHubRoute(to);
            result.setText(route == null ? "No restaurant can currently reach " + to + "."
                    : "Nearest restaurant: " + route.path.get(0) + "\n" + describe(route));
        }

        private String describe(DeliveryGraph.Route route) {
            return route + "\n\nDistance: " + String.format("%.1f", route.km) + " km"
                    + "\nEstimated ride: about " + route.estimatedMinutes() + " min";
        }

        private void addLocation() {
            String name = JOptionPane.showInputDialog(this, "Name of the new delivery zone:", "Add Location", JOptionPane.PLAIN_MESSAGE);
            if (name == null) {
                return;
            }
            name = name.trim();
            if (name.isEmpty()) {
                AdminUI.warn(this, "The name can't be empty.");
            } else if (!graph.addLocation(name, false)) {
                AdminUI.warn(this, "\"" + name + "\" already exists.");
            } else {
                AdminUI.info(this, "\"" + name + "\" added. Connect it with a road so riders can reach it.");
                refresh();
            }
        }

        private void removeLocation() {
            List<String> zones = new ArrayList<>();
            for (String location : graph.locations()) {
                if (!graph.isHub(location)) {
                    zones.add(location);
                }
            }
            if (zones.isEmpty()) {
                AdminUI.warn(this, "There are no delivery zones to remove.");
                return;
            }
            Object choice = JOptionPane.showInputDialog(this, "Remove which delivery zone? Its roads are removed too.",
                    "Remove Location", JOptionPane.PLAIN_MESSAGE, null, zones.toArray(), zones.get(0));
            if (choice != null && AdminUI.confirm(this, "Remove " + choice + " and all its roads?")) {
                graph.removeLocation((String) choice);
                refresh();
            }
        }

        private void addRoad() {
            String[] locations = graph.locations().toArray(new String[0]);
            JComboBox<String> a = new JComboBox<>(locations), b = new JComboBox<>(locations);
            AdminUI.styleCombo(a, 0);
            AdminUI.styleCombo(b, 0);
            JTextField km = new JTextField();
            int row = table.getSelectedRow();
            if (row >= 0) {
                DeliveryGraph.Road road = shown.get(table.convertRowIndexToModel(row));
                a.setSelectedItem(road.a);
                b.setSelectedItem(road.b);
                km.setText(String.format("%.1f", road.km));
            }
            if (!AdminUI.form(this, "Add / Update Road", new String[]{"From:", "To:", "Distance (km):"},
                    new JComponent[]{a, b, km})) {
                return;
            }
            double distance;
            try {
                distance = Double.parseDouble(km.getText().trim());
            } catch (NumberFormatException ex) {
                distance = -1;
            }
            if (a.getSelectedItem().equals(b.getSelectedItem())) {
                AdminUI.warn(this, "A road must connect two different locations.");
            } else if (distance <= 0) {
                AdminUI.warn(this, "Please enter a distance greater than 0, for example 2.5.");
            } else {
                graph.addRoad((String) a.getSelectedItem(), (String) b.getSelectedItem(), distance);
                refresh();
            }
        }

        private void removeRoad() {
            int row = AdminUI.selectedModelRow(this, table, "a road");
            if (row < 0) {
                return;
            }
            DeliveryGraph.Road road = shown.get(row);
            if (AdminUI.confirm(this, "Remove the road " + road.a + " \u2194 " + road.b + "?")) {
                graph.removeRoad(road.a, road.b);
                refresh();
            }
        }
    }

    private static final class AdminReportsPanel extends JPanel implements AdminUI.Section {
        private final DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        private final JTable table = AdminUI.table(model);
        private final JComboBox<String> type =
                new JComboBox<>(new String[]{"Sales Report", "Order History", "Delivery Performance"});
        private final JLabel title = AdminUI.label(" ");
        private final JLabel note = AdminUI.hint(" ");
        private Report current;

        AdminReportsPanel() {
            super(new BorderLayout(0, 12));
            setOpaque(false);

            AdminUI.styleCombo(type, 220);

            JButton generate = AdminUI.button("Generate", AdminUI.PURPLE_DARK);
            generate.addActionListener(e -> generate());

            JPanel top = new JPanel(new BorderLayout(0, 6));
            top.setOpaque(false);
            top.add(AdminUI.buttonBar(AdminUI.label("Report:"), type, generate), BorderLayout.NORTH);
            JPanel heading = new JPanel(new GridLayout(2, 1, 0, 2));
            heading.setOpaque(false);
            heading.add(title);
            heading.add(note);
            top.add(heading, BorderLayout.SOUTH);

            add(top, BorderLayout.NORTH);
            add(AdminUI.scroll(table), BorderLayout.CENTER);
            generate();
        }

        @Override
        public JTable table() {
            return table;
        }

        @Override
        public void onShow() {
            generate();
        }

        private void generate() {
            try {
                switch (type.getSelectedIndex()) {
                    case 1:
                        current = Admin.orderHistoryReport();
                        break;
                    case 2:
                        current = Admin.deliveryPerformanceReport();
                        break;
                    default:
                        current = Admin.salesReport();
                }
            } catch (RuntimeException ex) {
                AdminUI.error(this, "Could not generate the report: " + ex);
                return;
            }
            model.setDataVector(current.rows.toArray(new Object[0][]), current.columns);
            title.setText(current.title + "  \u2014  generated " + java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a")));
            note.setText(current.summary.isEmpty() ? " " : current.summary);
        }
    }

    private static final class AdminProfilePanel extends JPanel implements AdminUI.Section {
        private final Admin admin;
        private Admin.Profile profile = new Admin.Profile();
        private final JTextField fullName = field(), username = field(), role = field(),
                email = field(), phone = field(), address = field();
        private final JLabel notice = AdminUI.hint(" ");

        AdminProfilePanel(Admin admin) {
            super(new BorderLayout(0, 12));
            this.admin = admin;
            setOpaque(false);

            username.setEditable(false);
            role.setEditable(false);
            role.setText("System Administrator");
            username.setBackground(AdminUI.BG_LIGHT);
            role.setBackground(AdminUI.BG_LIGHT);

            JButton save = AdminUI.button("Save Changes", AdminUI.PURPLE_DARK);
            save.addActionListener(e -> save());
            JButton undo = AdminUI.button("Undo Changes", AdminUI.PURPLE_LIGHT);
            undo.addActionListener(e -> {
                loadFields();
                say("Changes were undone.", AdminUI.MUTED);
            });

            JPanel form = new JPanel(new GridLayout(0, 2, 15, 16));
            form.setOpaque(false);
            addRow(form, "Full Name:", fullName);
            addRow(form, "Username:", username);
            addRow(form, "Role:", role);
            addRow(form, "Email (@gmail.com):", email);
            addRow(form, "Phone (11 digits):", phone);
            addRow(form, "Address:", address);
            form.add(new JLabel(""));
            form.add(AdminUI.buttonBar(save, undo));

            JPanel card = new JPanel(new BorderLayout(0, 10));
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 4, 0, AdminUI.YELLOW),
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(220, 210, 230), 1),
                            BorderFactory.createEmptyBorder(22, 28, 20, 28))));
            JLabel heading = new JLabel("Administrator Profile & Full Access Control");
            heading.setFont(AdminUI.font(Font.BOLD, 18));
            heading.setForeground(AdminUI.PURPLE_DARK);
            card.add(heading, BorderLayout.NORTH);
            card.add(form, BorderLayout.CENTER);
            card.add(notice, BorderLayout.SOUTH);

            JPanel footer = new JPanel(new GridLayout(2, 1, 0, 2));
            footer.setOpaque(false);
            footer.add(AdminUI.hint("Permissions: full access to accounts, restaurants & locations, menus, orders, delivery routes and reports."));
            footer.add(AdminUI.hint("Restaurant owners still manage their own incoming orders and menus."));

            JPanel top = new JPanel(new BorderLayout(0, 12));
            top.setOpaque(false);
            top.add(card, BorderLayout.NORTH);
            top.add(footer, BorderLayout.CENTER);
            add(top, BorderLayout.NORTH);
        }

        private static JTextField field() {
            JTextField f = new JTextField();
            f.setFont(AdminUI.font(Font.PLAIN, 14));
            return f;
        }

        private static void addRow(JPanel form, String label, JTextField field) {
            form.add(AdminUI.label(label));
            form.add(field);
        }

        private void say(String message, Color color) {
            notice.setText(message);
            notice.setForeground(color);
        }

        @Override
        public JTable table() {
            return null;
        }

        @Override
        public void onShow() {
            profile = admin.loadProfile();
            loadFields();
            say(" ", AdminUI.MUTED);
        }

        private void loadFields() {
            fullName.setText(profile.fullName);
            username.setText(profile.username);
            email.setText(profile.email);
            phone.setText(profile.phone);
            address.setText(profile.address);
        }

        private void save() {
            String name = fullName.getText().trim(), mail = email.getText().trim(),
                    tel = phone.getText().trim(), addr = address.getText().trim();
            if (name.isEmpty()) {
                AdminUI.warn(this, "Full name can't be empty.");
                return;
            }
            if (!mail.isEmpty() && !mail.toLowerCase().endsWith("@gmail.com")) {
                AdminUI.warn(this, "Email must be a @gmail.com address.");
                return;
            }
            if (!tel.isEmpty() && !tel.matches("\\d{11}")) {
                AdminUI.warn(this, "Phone number must be 11 digits.");
                return;
            }
            Admin.Profile updated = new Admin.Profile();
            updated.id = profile.id;
            updated.username = profile.username;
            updated.fullName = name;
            updated.email = mail;
            updated.phone = tel;
            updated.address = addr;
            try {
                admin.saveProfile(updated);
                profile = updated;
                say(profile.id >= 0 ? "Admin profile updated successfully!"
                        : "Profile updated for this session (no admin account was found in the database to save it to).",
                        AdminUI.GREEN);
            } catch (SQLException ex) {
                AdminUI.error(this, ex.getErrorCode() == 1062 ? "That email already belongs to another account."
                        : "Could not save the profile: " + ex.getMessage());
            }
        }
    }
}