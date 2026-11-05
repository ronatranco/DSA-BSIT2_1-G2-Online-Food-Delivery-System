/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to edit this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import Model.Admin;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * @author rownavanana
 */
public class AdminDashboard extends JPanel implements MouseListener {
    
    private MainFrame mainFrame;
    private Admin controller;
    private Color PURPLE_DARK = new Color(74, 20, 140);
    private Color YELLOW_ACCENT = new Color(255, 193, 7);
    private Color BG_LIGHT = new Color(248, 245, 250);
    private Color TEXT_DARK = new Color(30, 30, 30);
    private Color COLOR_WHITE = new Color(255, 255, 255);
    private Color COLOR_MUTED = new Color(120, 120, 120);

    private JPanel pnlMainBg, leftBannerNav, pnlsearchBar, pnlOverviewCard, pnlProfileCard;
    private JButton btnNavLogoPanda, btnNavHome, btnNavCustomers, btnNavRestaurants, btnNavProfile, btnNavExit;
    private JButton btnDelete, btnBan, btnRefresh;
    private JTextField txtSearchBar;
    private JLabel lblName, lblHome, lblCustomers, lblRestaurants, lblProfile, lblExit, lblSearchIcon, lblDateRn, lblLogoTitle;
    private JLabel lblValCustomers, lblValRestaurants;
    
    private JTable dataTable;
    private DefaultTableModel tableModel;
    private JScrollPane tableScrollPane;

    // animation nung hover hover sa side
    private javax.swing.Timer animationTimer;
    private double preciseWidth = 80.0;
    private int currentWidth = 80;
    private final int MIN_WIDTH = 80;
    private final int MAX_WIDTH = 200;
    private boolean targetsExpand = false;
    private long animationStartTime;
    private double animationDuration = 280.0;
    private double startWidth;

    public AdminDashboard(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.controller = new Admin();
        this.controller.setAdminDashboard(this);
        
        setLayout(null); 
        setBounds(0, 0, 1024, 720);

        pnlMainBg = new JPanel() {
            @Override
            public boolean isOptimizedDrawingEnabled() {
                return false;
            }
        };
        pnlMainBg.setLayout(null);
        pnlMainBg.setBackground(BG_LIGHT);
        pnlMainBg.setBounds(0, 0, 1024, 720);

        leftBannerNav = new JPanel();
        leftBannerNav.setLayout(null);
        leftBannerNav.setBackground(PURPLE_DARK);
        leftBannerNav.setBounds(0, 0, 80, 720);
        
        lblName = new JLabel("Hello, Admin!");
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblName.setForeground(PURPLE_DARK);
        lblName.setBounds(110, 34, 500, 35);
        pnlMainBg.add(lblName); 
        
        pnlsearchBar = new JPanel();
        pnlsearchBar.setLayout(null);
        pnlsearchBar.setBackground(COLOR_WHITE);
        pnlsearchBar.setBorder(BorderFactory.createLineBorder(YELLOW_ACCENT));
        pnlsearchBar.setBounds(110, 80, 420, 35);

        lblSearchIcon = new JLabel("🔍", SwingConstants.CENTER);
        lblSearchIcon.setForeground(PURPLE_DARK);
        lblSearchIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        lblSearchIcon.setBounds(5, 4, 30, 35);
        pnlsearchBar.add(lblSearchIcon);

        txtSearchBar = new JTextField("Search system accounts...");
        txtSearchBar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearchBar.setForeground(COLOR_MUTED);
        txtSearchBar.setBackground(COLOR_WHITE);
        txtSearchBar.setBorder(null);
        txtSearchBar.setBounds(40, 3, 375, 31);
        pnlsearchBar.add(txtSearchBar);
        pnlMainBg.add(pnlsearchBar);
        
        LocalDate today = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMMM d, yyyy" + " |");
        String formattedDate = today.format(format);

        lblDateRn = new JLabel(formattedDate, SwingConstants.RIGHT);
        lblDateRn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblDateRn.setForeground(PURPLE_DARK);
        lblDateRn.setBounds(794, 38, 180, 35);
        pnlMainBg.add(lblDateRn);

        btnNavLogoPanda = new JButton("🛡️");
        btnNavLogoPanda.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        btnNavLogoPanda.setForeground(YELLOW_ACCENT);
        btnNavLogoPanda.setBounds(0, 30, 80, 40);
        btnNavLogoPanda.setContentAreaFilled(false);
        btnNavLogoPanda.setBorderPainted(false);
        btnNavLogoPanda.setFocusPainted(false);
        leftBannerNav.add(btnNavLogoPanda);

        lblLogoTitle = new JLabel("GrabPandaAdmin");
        lblLogoTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblLogoTitle.setForeground(COLOR_WHITE);
        lblLogoTitle.setBounds(60, 28, 130, 40);
        lblLogoTitle.setVisible(false);
        leftBannerNav.add(lblLogoTitle);

        btnNavHome = new JButton("📊");
        btnNavHome.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        btnNavHome.setForeground(YELLOW_ACCENT);
        btnNavHome.setBounds(0, 130, 80, 45);
        btnNavHome.setContentAreaFilled(false);
        btnNavHome.setBorderPainted(false);
        btnNavHome.setFocusPainted(false); // Removes white focus border
        leftBannerNav.add(btnNavHome);
        btnNavHome.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        lblHome = new JLabel("Dashboard");
        lblHome.setFont(new Font("Segoe UI", Font.BOLD, 19));
        lblHome.setForeground(COLOR_WHITE);
        lblHome.setBounds(60, 129, 130, 45);
        lblHome.setVisible(false);
        leftBannerNav.add(lblHome);
        lblHome.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnNavCustomers = new JButton("👥");
        btnNavCustomers.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        btnNavCustomers.setForeground(YELLOW_ACCENT);
        btnNavCustomers.setBounds(0, 195, 80, 45);
        btnNavCustomers.setContentAreaFilled(false);
        btnNavCustomers.setBorderPainted(false);
        btnNavCustomers.setFocusPainted(false); // Removes white focus border
        leftBannerNav.add(btnNavCustomers);
        btnNavCustomers.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        lblCustomers = new JLabel("Customers");
        lblCustomers.setFont(new Font("Segoe UI", Font.BOLD, 19));
        lblCustomers.setForeground(COLOR_WHITE);
        lblCustomers.setBounds(60, 192, 130, 45);
        lblCustomers.setVisible(false);
        leftBannerNav.add(lblCustomers);
        lblCustomers.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnNavRestaurants = new JButton("🍽️");
        btnNavRestaurants.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        btnNavRestaurants.setForeground(YELLOW_ACCENT);
        btnNavRestaurants.setBounds(0, 260, 80, 45);
        btnNavRestaurants.setContentAreaFilled(false);
        btnNavRestaurants.setBorderPainted(false);
        btnNavRestaurants.setFocusPainted(false); 
        leftBannerNav.add(btnNavRestaurants);
        btnNavRestaurants.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        lblRestaurants = new JLabel("Restaurants");
        lblRestaurants.setFont(new Font("Segoe UI", Font.BOLD, 19));
        lblRestaurants.setForeground(COLOR_WHITE);
        lblRestaurants.setBounds(60, 258, 130, 45);
        lblRestaurants.setVisible(false);
        leftBannerNav.add(lblRestaurants);
        lblRestaurants.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnNavProfile = new JButton("👤");
        btnNavProfile.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        btnNavProfile.setForeground(YELLOW_ACCENT);
        btnNavProfile.setBounds(0, 325, 80, 45);
        btnNavProfile.setContentAreaFilled(false);
        btnNavProfile.setBorderPainted(false);
        btnNavProfile.setFocusPainted(false); 
        leftBannerNav.add(btnNavProfile);
        btnNavProfile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        lblProfile = new JLabel("Admin Profile");
        lblProfile.setFont(new Font("Segoe UI", Font.BOLD, 19));
        lblProfile.setForeground(COLOR_WHITE);
        lblProfile.setBounds(60, 322, 130, 45);
        lblProfile.setVisible(false);
        leftBannerNav.add(lblProfile);
        lblProfile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnNavExit = new JButton("[←");
        btnNavExit.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        btnNavExit.setForeground(YELLOW_ACCENT);
        btnNavExit.setBounds(0, 620, 80, 45);
        btnNavExit.setContentAreaFilled(false);
        btnNavExit.setBorderPainted(false);
        btnNavExit.setFocusPainted(false); // Removes white focus border
        leftBannerNav.add(btnNavExit);
        btnNavExit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        lblExit = new JLabel("Logout");
        lblExit.setFont(new Font("Segoe UI", Font.BOLD, 19));
        lblExit.setForeground(COLOR_WHITE);
        lblExit.setBounds(70, 617, 110, 45);
        lblExit.setVisible(false);
        leftBannerNav.add(lblExit);
        lblExit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        pnlMainBg.add(leftBannerNav);
        
        pnlOverviewCard = new JPanel();
        pnlOverviewCard.setLayout(null);
        pnlOverviewCard.setBounds(110, 140, 880, 520);
        pnlOverviewCard.setOpaque(false);

        JPanel card1 = createMetricCard("Total Customers", lblValCustomers = new JLabel("0", SwingConstants.CENTER), 50, 50);
        JPanel card2 = createMetricCard("Total Restaurants", lblValRestaurants = new JLabel("0", SwingConstants.CENTER), 350, 50);
        pnlOverviewCard.add(card1);
        pnlOverviewCard.add(card2);
        pnlMainBg.add(pnlOverviewCard);

        pnlProfileCard = new JPanel();
        pnlProfileCard.setLayout(null);
        pnlProfileCard.setBounds(110, 140, 880, 520);
        pnlProfileCard.setOpaque(false);

        JLabel lblProfileTitle = new JLabel("Administrator Profile & Full Access Control");
        lblProfileTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblProfileTitle.setForeground(PURPLE_DARK);
        lblProfileTitle.setBounds(0, 0, 500, 30);
        pnlProfileCard.add(lblProfileTitle);

        JLabel lblUserLabel = new JLabel("Username: ");
        lblUserLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblUserLabel.setForeground(TEXT_DARK);
        lblUserLabel.setBounds(0, 50, 100, 25);
        pnlProfileCard.add(lblUserLabel);

        JLabel lblUserVal = new JLabel("admin");
        lblUserVal.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblUserVal.setForeground(TEXT_DARK);
        lblUserVal.setBounds(95, 50, 300, 25);
        pnlProfileCard.add(lblUserVal);

        JLabel lblRoleLabel = new JLabel("Role: ");
        lblRoleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblRoleLabel.setForeground(TEXT_DARK);
        lblRoleLabel.setBounds(0, 80, 100, 25);
        pnlProfileCard.add(lblRoleLabel);

        JLabel lblRoleVal = new JLabel("System Administrator");
        lblRoleVal.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblRoleVal.setForeground(TEXT_DARK);
        lblRoleVal.setBounds(50, 80, 300, 25);
        pnlProfileCard.add(lblRoleVal);

        JLabel lblPermLabel = new JLabel("Permissions: ");
        lblPermLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblPermLabel.setForeground(TEXT_DARK);
        lblPermLabel.setBounds(0, 110, 100, 25);
        pnlProfileCard.add(lblPermLabel);

        JLabel lblPermVal = new JLabel("Full Access (View Passwords, Manage Accounts, Ban/Delete Users & Restaurants)");
        lblPermVal.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblPermVal.setForeground(TEXT_DARK);
        lblPermVal.setBounds(100, 110, 750, 25);
        pnlProfileCard.add(lblPermVal);

        pnlProfileCard.setVisible(false);
        pnlMainBg.add(pnlProfileCard);

        tableModel = new DefaultTableModel();
        dataTable = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? COLOR_WHITE : new Color(250, 248, 252));
                    c.setForeground(TEXT_DARK);
                } else {
                    c.setBackground(new Color(230, 220, 245));
                    c.setForeground(PURPLE_DARK);
                }
                return c;
            }
        };
        
        dataTable.setRowHeight(34);
        dataTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dataTable.setShowVerticalLines(true);
        dataTable.setGridColor(new Color(225, 220, 235));
        
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                lbl.setBackground(PURPLE_DARK);
                lbl.setForeground(COLOR_WHITE);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
                lbl.setHorizontalAlignment(JLabel.CENTER);
                lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, YELLOW_ACCENT));
                return lbl;
            }
        };
        
        JTableHeader header = dataTable.getTableHeader();
        header.setDefaultRenderer(headerRenderer);
        header.setPreferredSize(new Dimension(0, 40));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        dataTable.setDefaultRenderer(Object.class, centerRenderer);

        tableScrollPane = new JScrollPane(dataTable);
        tableScrollPane.setBounds(110, 140, 880, 460);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(PURPLE_DARK, 1));
        tableScrollPane.setVisible(false);
        pnlMainBg.add(tableScrollPane);

        btnDelete = new JButton("Delete Account");
        styleButton(btnDelete, new Color(220, 53, 69), 110, 620, 150, 35);
        btnDelete.setVisible(false);
        pnlMainBg.add(btnDelete);

        btnBan = new JButton("Ban / Inactive");
        styleButton(btnBan, new Color(255, 140, 0), 270, 620, 150, 35);
        btnBan.setVisible(false);
        pnlMainBg.add(btnBan);

        btnRefresh = new JButton("Refresh Data");
        styleButton(btnRefresh, PURPLE_DARK, 430, 620, 150, 35);
        btnRefresh.setVisible(false);
        pnlMainBg.add(btnRefresh);

        btnNavLogoPanda.addActionListener(controller);
        btnNavHome.addActionListener(controller);
        btnNavCustomers.addActionListener(controller);
        btnNavRestaurants.addActionListener(e -> controller.actionProfilesOrResto(e));
        btnNavProfile.addActionListener(controller);
        btnNavExit.addActionListener(controller);

        leftBannerNav.addMouseListener(this);
        btnNavLogoPanda.addMouseListener(this);
        btnNavHome.addMouseListener(this);
        btnNavCustomers.addMouseListener(this);
        btnNavRestaurants.addMouseListener(this);
        btnNavProfile.addMouseListener(this);
        btnNavExit.addMouseListener(this);
        
        lblLogoTitle.addMouseListener(this);
        lblHome.addMouseListener(this);
        lblCustomers.addMouseListener(this);
        lblRestaurants.addMouseListener(this);
        lblProfile.addMouseListener(this);
        lblExit.addMouseListener(this);

        btnDelete.addActionListener(e -> controller.deleteSelectedRecord());
        btnBan.addActionListener(e -> controller.banSelectedRecord());
        btnRefresh.addActionListener(e -> controller.refreshCurrentView());

        pnlMainBg.setComponentZOrder(leftBannerNav, 0);
        pnlMainBg.revalidate();
        pnlMainBg.repaint();
        add(pnlMainBg);
        
        if (controller != null) {
            controller.loadOverviewData();
        }
    }

    private JPanel createMetricCard(String title, JLabel valueLbl, int x, int y) {
        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBackground(COLOR_WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 210, 230), 1));
        card.setBounds(x, y, 260, 140);

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(COLOR_MUTED);
        lblTitle.setBounds(10, 20, 240, 25);
        card.add(lblTitle);

        valueLbl.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLbl.setForeground(PURPLE_DARK);
        valueLbl.setBounds(10, 60, 240, 50);
        card.add(valueLbl);
        return card;
    }

    private void styleButton(JButton btn, Color bg, int x, int y, int w, int h) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(COLOR_WHITE);
        btn.setBackground(bg);
        btn.setBounds(x, y, w, h);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object source = e.getSource();
        if (source == lblHome || source == btnNavHome) {
            btnNavHome.doClick();
        } else if (source == lblCustomers || source == btnNavCustomers) {
            btnNavCustomers.doClick();
        } else if (source == lblRestaurants || source == btnNavRestaurants) {
            btnNavRestaurants.doClick();
        } else if (source == lblProfile || source == btnNavProfile) {
            btnNavProfile.doClick();
        } else if (source == lblExit || source == btnNavExit) {
            btnNavExit.doClick();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Object source = e.getSource();
        if (source instanceof JLabel) {
            JLabel label = (JLabel) source;
            if (label == lblHome || label == lblCustomers || label == lblRestaurants || label == lblProfile || label == lblExit || label == lblLogoTitle) {
                label.setForeground(new Color(255, 230, 128)); // Yellow highlight on hover
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
            JLabel label = (JLabel) source;
            if (label == lblHome || label == lblCustomers || label == lblRestaurants || label == lblProfile || label == lblExit || label == lblLogoTitle) {
                label.setForeground(COLOR_WHITE);
            }
        }
        
        Point p = SwingUtilities.convertPoint((Component) source, e.getPoint(), leftBannerNav);
        if (!leftBannerNav.contains(p)) {
            if (targetsExpand) {
                targetsExpand = false;
                setupAnimationTracking();
            }
        }
    }

    private void setupAnimationTracking() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        animationStartTime = System.currentTimeMillis();
        startWidth = preciseWidth;
        startAnimation();
    }

    private void startAnimation() {
        animationTimer = new javax.swing.Timer(4, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long elapsed = System.currentTimeMillis() - animationStartTime;
                double progress = Math.min(1.0, elapsed / animationDuration);
                double easeOutProgress = 1.0 - Math.pow(1.0 - progress, 3);

                int targetWidth = targetsExpand ? MAX_WIDTH : MIN_WIDTH;
                preciseWidth = startWidth + (targetWidth - startWidth) * easeOutProgress;
                currentWidth = (int) Math.round(preciseWidth);

                if (targetsExpand) {
                    if (progress >= 1.0 || currentWidth >= MAX_WIDTH) {
                        currentWidth = MAX_WIDTH;
                        preciseWidth = MAX_WIDTH;
                        setLabelsVisible(true);
                        animationTimer.stop();
                    }
                } else {
                    if (lblHome.isVisible()) {
                        setLabelsVisible(false);
                    }
                    if (progress >= 1.0 || currentWidth <= MIN_WIDTH) {
                        currentWidth = MIN_WIDTH;
                        preciseWidth = MIN_WIDTH;
                        animationTimer.stop();
                    }
                }
                applyWidthLayout(currentWidth);
            }
        });
        animationTimer.start();
    }

    private void applyWidthLayout(int width) {
        leftBannerNav.setBounds(0, 0, width, 720);
        pnlMainBg.repaint();
    }

    public void setLabelsVisible(boolean visible) {
        lblLogoTitle.setVisible(visible);
        lblHome.setVisible(visible);
        lblCustomers.setVisible(visible);
        lblRestaurants.setVisible(visible);
        lblProfile.setVisible(visible);
        lblExit.setVisible(visible);
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}

    public JButton getBtnNavOverview() { return btnNavHome; }
    public JButton getBtnNavUsers() { return btnNavCustomers; }
    public JButton getBtnNavRestaurants() { return btnNavRestaurants; }
    public JButton getBtnNavOrders() { return btnNavProfile; } 
    public JButton getBtnDeleteSelectedUser() { return btnDelete; }
    public JButton getBtnRefreshData() { return btnRefresh; }
    public JButton getBtnBanSelectedUser() { return btnBan; }
    public JButton getBtnLogout() { return btnNavExit; }
    
    public JLabel getLblName() { return lblName; }
    public JLabel getLblDashboardTitle() { return lblName; }
    public JPanel getCardMetricsContainer() { return pnlOverviewCard; }
    public JPanel getPnlProfileCard() { return pnlProfileCard; }
    public JScrollPane getTableScrollPane() { return tableScrollPane; }
    
    public JLabel getLblValUsers() { return lblValCustomers; }
    public JLabel getLblValRestaurants() { return lblValRestaurants; }
    public JLabel getLblValOrders() { return new JLabel(); } 
    public JLabel getLblValRevenue() { return new JLabel(); } 
    
    public DefaultTableModel getTableModel() { return tableModel; }
    public JTable getDataTable() { return dataTable; }
    public JTextField getTxtSearchBar() { return txtSearchBar; }
    public MainFrame getMainFrame() { return mainFrame; }
}