package GUI;

import Model.Customer;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

/**
 *
 * @author klara
 */
public class CustomerDashboard extends JPanel {
    
    private MainFrame mainFrame;
    private Customer controller;
    private Color PURPLE_DARK = new Color(74, 20, 140);
    private Color YELLOW_ACCENT = new Color(255, 193, 7);
    private Color BG_LIGHT = new Color(248, 245, 250);
    private Color TEXT_DARK = new Color(30, 30, 30);
    private Color COLOR_WHITE = new Color(255, 255, 255);
    private Color COLOR_MUTED = new Color(120, 120, 120);

    private JPanel pnlMainBg, leftBannerNav, pnlsearchBar;
    private JButton btnNavLogoPanda, btnNavHome, btnNavTrack, btnNavCart, btnNavHistory, btnNavProfile, btnNavExit;
    private JTextField txtSearchBar;
    private JLabel lblLogoPanda, lblName, lblHome, lblTrack, lblCart, lblHistory, lblProfile, lblExit, lblSearchIcon, lblDateRn;

    private CustomerDashboardRestaurant pnlRestoCard;
    private CustomerDashboardTrackOrder pnlRightTrackOrder;
    private CustomerDashboardHistory pnlRightHistory;
    private CustomerDashboardProfile pnlRightProfile;
    private CustomerDashboardViewCart pnlRightCart;

    public CustomerDashboard(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(null); 
        setBounds(0, 0, 1024, 720);

        this.controller = new Customer(this);

        pnlMainBg = new JPanel();
        pnlMainBg.setLayout(null);
        pnlMainBg.setBackground(BG_LIGHT);
        pnlMainBg.setBounds(0, 0, 1024, 720);

        leftBannerNav = new JPanel();
        leftBannerNav.setLayout(null);
        leftBannerNav.setBackground(PURPLE_DARK);
        leftBannerNav.setBounds(0, 0, 80, 720);
        
        lblName = new JLabel("Hello, Rona!");
        lblName.setFont( new Font("Segoe UI", Font.BOLD, 18));
        lblName.setForeground(PURPLE_DARK);
        lblName. setBounds(110, 34, 420, 35);
        pnlMainBg.add(lblName); 
        
        // == SEARCH BAR ==
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

        lblSearchIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblSearchIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                String text = txtSearchBar.getText().trim();
                if (text.isEmpty() || text.equals("What are you craving today?")) {
                    txtSearchBar.setText("What are you craving today?");
                    txtSearchBar.setForeground(COLOR_MUTED);
                }
                controller.searchRestaurants(txtSearchBar.getText());
                pnlMainBg.requestFocusInWindow();
            }
        });

        txtSearchBar = new JTextField("What are you craving today?");
        txtSearchBar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearchBar.setForeground(COLOR_MUTED);
        txtSearchBar.setBackground(COLOR_WHITE);
        txtSearchBar.setBorder(null);
        txtSearchBar.setBounds(40, 3, 375, 31);
        pnlsearchBar.add(txtSearchBar);

        pnlMainBg.add(pnlsearchBar);

        // FOR DATE
        LocalDate today = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMMM d, yyyy" + " |");
        String formattedDate = today.format(format);

        lblDateRn = new JLabel(formattedDate, SwingConstants.RIGHT);
        lblDateRn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblDateRn.setForeground(PURPLE_DARK);
        lblDateRn.setBounds(794, 38, 180, 35);
        pnlMainBg.add(lblDateRn);

        // LEFT BANNER FOR NAVIGATION
        btnNavLogoPanda = new JButton("🐼");
        btnNavLogoPanda.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        btnNavLogoPanda.setForeground(YELLOW_ACCENT);
        btnNavLogoPanda.setBounds(0, 30, 80, 40);
        btnNavLogoPanda.setContentAreaFilled(false);
        btnNavLogoPanda.setBorderPainted(false);
        btnNavLogoPanda.setFocusPainted(false);
        leftBannerNav.add(btnNavLogoPanda);

        lblLogoPanda = new JLabel("GrabPanda");
        lblLogoPanda.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblLogoPanda.setForeground(COLOR_WHITE);
        lblLogoPanda.setBounds(60, 28, 110, 40);
        lblLogoPanda.setVisible(false);
        leftBannerNav.add(lblLogoPanda);

        btnNavHome = new JButton("🏠");
        btnNavHome.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        btnNavHome.setForeground(YELLOW_ACCENT);
        btnNavHome.setBounds(0, 130, 80, 45);
        btnNavHome.setContentAreaFilled(false);
        btnNavHome.setBorderPainted(false);
        btnNavHome.setFocusPainted(false);
        leftBannerNav.add(btnNavHome);
        btnNavHome.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        lblHome = new JLabel("Home");
        lblHome.setFont(new Font("Segoe UI", Font.BOLD, 19));
        lblHome.setForeground(COLOR_WHITE);
        lblHome.setBounds(60, 129, 110, 45);
        lblHome.setVisible(false);
        leftBannerNav.add(lblHome);
        lblHome.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnNavCart = new JButton("🛒");
        btnNavCart.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        btnNavCart.setForeground(YELLOW_ACCENT);
        btnNavCart.setBounds(0, 195, 80, 45);
        btnNavCart.setContentAreaFilled(false);
        btnNavCart.setBorderPainted(false);
        btnNavCart.setFocusPainted(false);
        leftBannerNav.add(btnNavCart);
        btnNavCart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        lblCart = new JLabel("View Cart");
        lblCart.setFont(new Font("Segoe UI", Font.BOLD, 19));
        lblCart.setForeground(COLOR_WHITE);
        lblCart.setBounds(60, 192, 110, 45);
        lblCart.setVisible(false);
        leftBannerNav.add(lblCart);
        lblCart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnNavTrack = new JButton("🛵");
        btnNavTrack.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        btnNavTrack.setForeground(YELLOW_ACCENT);
        btnNavTrack.setBounds(0, 260, 80, 45);
        btnNavTrack.setContentAreaFilled(false);
        btnNavTrack.setBorderPainted(false);
        btnNavTrack.setFocusPainted(false);
        leftBannerNav.add(btnNavTrack);
        btnNavTrack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        lblTrack = new JLabel("Track Order");
        lblTrack.setFont(new Font("Segoe UI", Font.BOLD, 19));
        lblTrack.setForeground(COLOR_WHITE);
        lblTrack.setBounds(60, 258, 110, 45);
        lblTrack.setVisible(false);
        leftBannerNav.add(lblTrack);
        lblTrack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnNavHistory = new JButton("📋");
        btnNavHistory.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        btnNavHistory.setForeground(YELLOW_ACCENT);
        btnNavHistory.setBounds(0, 325, 80, 45);
        btnNavHistory.setContentAreaFilled(false);
        btnNavHistory.setBorderPainted(false);
        btnNavHistory.setFocusPainted(false);
        leftBannerNav.add(btnNavHistory);
        btnNavHistory.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        lblHistory = new JLabel("History");
        lblHistory.setFont(new Font("Segoe UI", Font.BOLD, 19));
        lblHistory.setForeground(COLOR_WHITE);
        lblHistory.setBounds(60, 322, 110, 45);
        lblHistory.setVisible(false);
        leftBannerNav.add(lblHistory);
        lblHistory.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnNavProfile = new JButton("👤");
        btnNavProfile.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        btnNavProfile.setForeground(YELLOW_ACCENT);
        btnNavProfile.setBounds(0, 390, 80, 45);
        btnNavProfile.setContentAreaFilled(false);
        btnNavProfile.setBorderPainted(false);
        btnNavProfile.setFocusPainted(false);
        leftBannerNav.add(btnNavProfile);
        btnNavProfile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        lblProfile = new JLabel("My Profile");
        lblProfile.setFont(new Font("Segoe UI", Font.BOLD, 19));
        lblProfile.setForeground(COLOR_WHITE);
        lblProfile.setBounds(60, 387, 110, 45);
        lblProfile.setVisible(false);
        leftBannerNav.add(lblProfile);
        lblProfile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnNavExit = new JButton("[←");
        btnNavExit.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        btnNavExit.setForeground(YELLOW_ACCENT);
        btnNavExit.setBounds(0, 620, 80, 45);
        btnNavExit.setContentAreaFilled(false);
        btnNavExit.setBorderPainted(false);
        btnNavExit.setFocusPainted(false);
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
// == MODEL CONNECTOR ==
        pnlRestoCard = new CustomerDashboardRestaurant();
        pnlMainBg.add(pnlRestoCard);
        pnlRestoCard.setVisible(true);

        pnlRightCart = new CustomerDashboardViewCart();
        pnlMainBg.add(pnlRightCart);
        pnlRightCart.addCartActions(controller);

        pnlRightTrackOrder = new CustomerDashboardTrackOrder();
        pnlMainBg.add(pnlRightTrackOrder);
        pnlRightTrackOrder.addProfileActions(controller);

        pnlRightHistory = new CustomerDashboardHistory();
        pnlMainBg.add(pnlRightHistory);
        pnlRightHistory.addProfileActions(controller);

        pnlRightProfile = new CustomerDashboardProfile();
        pnlMainBg.add(pnlRightProfile);
        pnlRightProfile.addProfileActions(controller);

        pnlMainBg.add(leftBannerNav);
        add(pnlMainBg);

        // --- LISTENERS ---
        btnNavLogoPanda.addActionListener(controller);
        btnNavHome.addActionListener(controller);
        btnNavCart.addActionListener(controller);
        btnNavTrack.addActionListener(controller);
        btnNavHistory.addActionListener(controller);
        btnNavProfile.addActionListener(controller);
        btnNavExit.addActionListener(controller);
        leftBannerNav.addMouseListener(controller);
        btnNavLogoPanda.addMouseListener(controller);
        btnNavHome.addMouseListener(controller);
        btnNavCart.addMouseListener(controller);
        btnNavTrack.addMouseListener(controller);
        btnNavHistory.addMouseListener(controller);
        btnNavProfile.addMouseListener(controller);
        btnNavExit.addMouseListener(controller);
        lblLogoPanda.addMouseListener(controller);
        lblHome.addMouseListener(controller);
        lblCart.addMouseListener(controller);
        lblTrack.addMouseListener(controller);
        lblHistory.addMouseListener(controller);
        lblProfile.addMouseListener(controller);
        lblExit.addMouseListener(controller);

        txtSearchBar.addFocusListener(new java.awt.event.FocusListener() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txtSearchBar.getText().equals("What are you craving today?")) {
                    txtSearchBar.setText("");
                    txtSearchBar.setForeground(TEXT_DARK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txtSearchBar.getText().trim().isEmpty()) {
                    txtSearchBar.setText("What are you craving today?");
                    txtSearchBar.setForeground(COLOR_MUTED);
                }
            }
        });
        txtSearchBar.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                String text = txtSearchBar.getText().trim();
                if (text.isEmpty() || text.equals("What are you craving today?")) {
                    txtSearchBar.setText("What are you craving today?");
                    txtSearchBar.setForeground(COLOR_MUTED);
                }
                controller.searchRestaurants(txtSearchBar.getText());
                pnlMainBg.requestFocusInWindow();
            }
        });
        // == LAYER ORDER == 
        pnlMainBg.setComponentZOrder(leftBannerNav, 0);       // Left Nav Panel
        pnlMainBg.setComponentZOrder(pnlRightCart, 1);         // Order Summary Panel 
        pnlMainBg.setComponentZOrder(pnlRightProfile, 2);      // Profile Screen Panel
        pnlMainBg.setComponentZOrder(pnlRightTrackOrder, 3);   // Track Order Screen
        pnlMainBg.setComponentZOrder(pnlRightHistory, 4);      // History Panel
        pnlMainBg.setComponentZOrder(pnlsearchBar, 5);         // Search Input Box Container
        pnlMainBg.setComponentZOrder(lblDateRn, 6);            // Live Date Header Label
        pnlMainBg.setComponentZOrder(pnlRestoCard, 7);         // Bottom Layer

        pnlMainBg.revalidate();
        pnlMainBg.repaint();
    }

    public JLabel getLblHome() {
        return lblHome;
    }

    public JLabel getLblCart() {
        return lblCart;
    }

    public JLabel getLblTrack() {
        return lblTrack;
    }

    public JLabel getLblHistory() {
        return lblHistory;
    }

    public JLabel getLblProfile() {
        return lblProfile;
    }

    public JLabel getLblExit() {
        return lblExit;
    }

    public JPanel getLeftBannerNav() {
        return leftBannerNav;
    }

    public JPanel getSearchBarContainer() {
        return pnlsearchBar;
    }

    public CustomerDashboardRestaurant getPnlRestoCard() {
        return pnlRestoCard;
    }

    public JTextField getTxtSearchBar() {
        return txtSearchBar;
    }

    public void setLabelsVisible(boolean visible) {
        lblLogoPanda.setVisible(visible);
        lblHome.setVisible(visible);
        lblCart.setVisible(visible);
        lblTrack.setVisible(visible);
        lblHistory.setVisible(visible);
        lblProfile.setVisible(visible);
        lblExit.setVisible(visible);
    }

    public JButton getBtnNavCart() {
        return btnNavCart;
    }

    public JButton getBtnNavProfile() {
        return btnNavProfile;
    }

    public JButton getBtnNavTrack() {
        return btnNavTrack;
    }

    public JButton getBtnNavHistory() {
        return btnNavHistory;
    }

    public JButton getBtnNavHome() {
        return btnNavHome;
    }

    public JButton getBtnNavExit() {
        return btnNavExit;
    }

    public JButton getBtnNavLogoPanda() {
        return btnNavLogoPanda;
    }

    public CustomerDashboardViewCart getPnlRightCart() {
        return pnlRightCart;
    }

    public CustomerDashboardProfile getPnlRightProfile() {
        return pnlRightProfile;
    }

    public CustomerDashboardTrackOrder getPnlRightTrackOrder() {
        return pnlRightTrackOrder;
    }

    public CustomerDashboardHistory getPnlRightHistory() {
        return pnlRightHistory;
    }

    public JPanel getPnlMainBg() {
        return pnlMainBg;
    }
    
    public JLabel getLblName() {
        return lblName;
    }
    
    public JButton getBtnLogout() {
        return btnNavExit;
    }
}
