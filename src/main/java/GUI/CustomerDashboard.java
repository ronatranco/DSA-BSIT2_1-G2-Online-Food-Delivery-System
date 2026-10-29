/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import Model.Customer;
import Model.Order;
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

    private JPanel pnlMainBg, leftBannerNav, pnlsearchBar, pnlLocationBanner, pnlExpandedLocation;
    private JButton btnExpandLocation, btnNavLogoPanda, btnNavHome, btnNavTrack, btnNavCart, btnNavHistory, btnNavProfile, btnNavExit;
    private JTextField txtSearchBar;
    private JLabel lblPlaceholder, lblLocationText, lblLogoPanda, lblName, lblHome, lblTrack, lblCart, lblHistory, lblProfile, lblExit, lblSearchIcon, lblDateRn;
    private Timer slideTimer;
    private int bannerTargetX = 530; 
    private int bannerCurrentX = 110;
 
    private CustomerDashboardRestaurant pnlRestoCard;
    private CustomerDashboardRestaurantMenus pnlMenuMcDollibee; 
    private CustomerDashboardRestaurantMenus pnlMenuBrainCells;  
    private CustomerDashboardRestaurantMenus pnlMenuMangPinaasa;
    private CustomerDashboardTrackOrder pnlRightTrackOrder;
    private CustomerDashboardHistory pnlRightHistory;
    private CustomerDashboardProfile pnlRightProfile;
    private CustomerDashboardViewCart pnlRightCart;
    
    private String savedFirstName = "";
    private String savedLastName = "";
    private String savedMobileNumber = "";
    private String savedFullAddress = "";
    
    // Complete Profile Pop-up
    private void showProfileWarningDialog(String title, String message) {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog((Frame) parentWindow, title, true);
        dialog.setUndecorated(true);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(parentWindow != null ? parentWindow : this);

    JPanel panel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            g2.setColor(new Color(250, 248, 252)); 
            g2.fillRoundRect(0, 0, w, h, 16, 16);
            g2.setColor(PURPLE_DARK); 
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(0, 0, w - 1, h - 1, 16, 16);
            g2.dispose();
        }
    };  panel.setLayout(null);
        panel.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(PURPLE_DARK);
        lblTitle.setBounds(25, 21, 250, 20);
        panel.add(lblTitle);

        JLabel lblMsg = new JLabel("<html>" + message.replace("\n", "<br>") + "</html>");
        lblMsg.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblMsg.setForeground(TEXT_DARK);
        lblMsg.setBounds(25, 42, 250, 45); 
        panel.add(lblMsg);

    JButton btnOk = new JButton("OK") {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
            g2.dispose();
            super.paintComponent(g);
         }
    };  btnOk.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnOk.setForeground(COLOR_WHITE);
        btnOk.setBackground(PURPLE_DARK);
        btnOk.setOpaque(false);
        btnOk.setContentAreaFilled(false);
        btnOk.setBorderPainted(false);
        btnOk.setFocusPainted(false);
        btnOk.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnOk.setBounds(190, 95, 75, 30);
        
        btnOk.addActionListener(e -> {
            dialog.dispose();
            if (btnNavProfile != null) {
            btnNavProfile.doClick();
            }
        }); panel.add(btnOk);
            dialog.setContentPane(panel);
            dialog.setVisible(true);
    }
    
    public CustomerDashboard(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.controller = new Customer(this);
        
        setLayout(null); 
        setBounds(0, 0, 1024, 720);

        pnlMainBg = new JPanel();
        pnlMainBg.setLayout(null);
        pnlMainBg.setBackground(BG_LIGHT);
        pnlMainBg.setBounds(0, 0, 1024, 720);

        leftBannerNav = new JPanel();
        leftBannerNav.setLayout(null);
        leftBannerNav.setBackground(PURPLE_DARK);
        leftBannerNav.setBounds(0, 0, 80, 720);
        
        lblName = new JLabel("Hello, User!");
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

        // LOCATION 
        pnlLocationBanner = new JPanel();
        pnlLocationBanner.setLayout(null);
        pnlLocationBanner.setBackground(YELLOW_ACCENT);
        pnlLocationBanner.setBounds(530, 80, 0, 35);
        lblLocationText = new JLabel("Your Location", SwingConstants.CENTER);
        lblLocationText.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblLocationText.setForeground(PURPLE_DARK);
        lblLocationText.setBounds(6, 0, 120, 35); 
        pnlLocationBanner.add(lblLocationText);
        // == BUTTON FOR EXPANDER ==
        btnExpandLocation = new JButton(">");
        btnExpandLocation.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnExpandLocation.setForeground(PURPLE_DARK);
        btnExpandLocation.setBounds(101, 3, 50, 25);
        btnExpandLocation.setContentAreaFilled(false);
        btnExpandLocation.setBorderPainted(false);
        btnExpandLocation.setFocusPainted(false);
        btnExpandLocation.setFocusable(true);
        btnExpandLocation.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        pnlLocationBanner.add(btnExpandLocation);
        pnlMainBg.add(pnlLocationBanner);
        // == EXPAND LOCATION PANEL ==
        pnlExpandedLocation = new JPanel();
        pnlExpandedLocation.setLayout(null);
        pnlExpandedLocation.setBackground(COLOR_WHITE);
        pnlExpandedLocation.setBounds(675, 80, 300, 35); 
        pnlExpandedLocation.setVisible(false); 
        // == DROP DOWN LOCATION ==
        JButton btnLocationDropDown = new JButton("▼");
        btnLocationDropDown.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnLocationDropDown.setForeground(PURPLE_DARK);
        btnLocationDropDown.setBounds(230, 5, 45, 25); 
        btnLocationDropDown.setContentAreaFilled(false);
        btnLocationDropDown.setBorderPainted(false);
        btnLocationDropDown.setFocusPainted(false);
        btnLocationDropDown.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLocationDropDown.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                JPopupMenu dropdownMenu = new JPopupMenu();
                
                JMenuItem item1 = new JMenuItem("Location A");
                JMenuItem item2 = new JMenuItem("Location B");
                JMenuItem item3 = new JMenuItem("Location C");
                
                java.awt.event.ActionListener menuListener = new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent ev) {
                        lblPlaceholder.setText(ev.getActionCommand());
                    }
                };
                item1.addActionListener(menuListener);
                item2.addActionListener(menuListener);
                item3.addActionListener(menuListener);
                dropdownMenu.add(item1);
                dropdownMenu.add(item2);
                dropdownMenu.add(item3);
                dropdownMenu.pack(); 
                int pixelBuffer = 35;
                int xOffset = btnLocationDropDown.getWidth() - dropdownMenu.getPreferredSize().width - pixelBuffer;
                dropdownMenu.show(btnLocationDropDown, xOffset, btnLocationDropDown.getHeight());
            }
        });
        
        pnlExpandedLocation.add(btnLocationDropDown);
        pnlMainBg.add(pnlExpandedLocation);
        btnExpandLocation.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                boolean isCurrentlyVisible = pnlExpandedLocation.isVisible();
                pnlExpandedLocation.setVisible(!isCurrentlyVisible);
            }
        });
        Timer slideOutTimer = new Timer(10, new java.awt.event.ActionListener() {
        @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                int currentW = pnlLocationBanner.getWidth();
                if (currentW < 145) {
                    pnlLocationBanner.setSize(Math.min(145, currentW + 15), 35);
                    pnlMainBg.repaint();
            }   else {
                    ((Timer)e.getSource()).stop();
        }
    }
});     Timer slideInTimer = new Timer(10, new java.awt.event.ActionListener() {
        @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                int currentW = pnlLocationBanner.getWidth();
                if (currentW > 0) {
                    pnlLocationBanner.setSize(Math.max(0, currentW - 15), 35);
                    pnlMainBg.repaint();
                    pnlExpandedLocation.setVisible(false);
                    btnExpandLocation.setText(">");
            }   else {
                    ((Timer)e.getSource()).stop();
        }
    }
});
        // DATE DASHBOARD
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
        //RESTO CARD
        pnlRestoCard = new CustomerDashboardRestaurant();
        pnlMainBg.add(pnlRestoCard);
        pnlRestoCard.setVisible(true);
        //RESTO MENU 1
        pnlMenuMcDollibee = new CustomerDashboardRestaurantMenus("McDollibee"); 
        pnlMainBg.add(pnlMenuMcDollibee);
        pnlMenuMcDollibee.setVisible(false); 
        //RESTO MENU 2
        pnlMenuBrainCells = new CustomerDashboardRestaurantMenus("Brain Cells Milk Tea"); 
        pnlMainBg.add(pnlMenuBrainCells);
        pnlMenuBrainCells.setVisible(false);
        //RESTO MENU 3
        pnlMenuMangPinaasa = new CustomerDashboardRestaurantMenus("Mang Pinaasa"); 
        pnlMainBg.add(pnlMenuMangPinaasa);
        pnlMenuMangPinaasa.setVisible(false);
        //VIEW CART
        pnlRightCart = new CustomerDashboardViewCart();
        pnlMainBg.add(pnlRightCart);
        pnlRightCart.addCartActions(controller);
        //TRACK ORDER
        pnlRightTrackOrder = new CustomerDashboardTrackOrder();
        pnlMainBg.add(pnlRightTrackOrder);
        pnlRightTrackOrder.addProfileActions(controller);
        //HISTORY
        pnlRightHistory = new CustomerDashboardHistory();
        pnlMainBg.add(pnlRightHistory);
        pnlRightHistory.addProfileActions(controller);
        //PROFILE
        pnlRightProfile = new CustomerDashboardProfile();
        pnlMainBg.add(pnlRightProfile);
        pnlRightProfile.addProfileActions(controller);
        
        this.controller = new Customer(this);
        pnlRightCart.addCartActions(controller);
        pnlRightTrackOrder.addProfileActions(controller);
        pnlRightHistory.addProfileActions(controller);
        pnlRightProfile.addProfileActions(controller);
        
        pnlMainBg.add(leftBannerNav);
        add(pnlMainBg);

        // == LISTENERS ==
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
                    slideInTimer.stop();  
                    slideOutTimer.start();
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txtSearchBar.getText().trim().isEmpty()) {
                    txtSearchBar.setText("What are you craving today?");
                    txtSearchBar.setForeground(COLOR_MUTED);
                }
                 Component opposite = e.getOppositeComponent();
                if (opposite == btnExpandLocation || opposite == pnlExpandedLocation) {
                    return; 
                }
                    slideOutTimer.stop(); 
                    slideInTimer.start();
            }
        });
        txtSearchBar.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                String text = txtSearchBar.getText().trim();
                if (text.isEmpty() || text.equals("What are you craving today?")) {
                    txtSearchBar.setText("What are you craving today?");
                    txtSearchBar.setForeground(COLOR_MUTED);
                }   controller.searchRestaurants(txtSearchBar.getText());
                    pnlMainBg.requestFocusInWindow();
            }
        });
        // == LAYER ORDER == 
        pnlMainBg.setComponentZOrder(leftBannerNav, 0);       // Left Nav Panel
        pnlMainBg.setComponentZOrder(pnlRightCart, 1);         // Order Summary Panel 
        pnlMainBg.setComponentZOrder(pnlMenuMcDollibee, 2);     // Restaurant 1
        pnlMainBg.setComponentZOrder(pnlMenuBrainCells, 3);     // Restaurant 2
        pnlMainBg.setComponentZOrder(pnlMenuMangPinaasa, 4);    // Restaurant 3
        pnlMainBg.setComponentZOrder(pnlRightProfile, 5);      // Profile Screen Panel
        pnlMainBg.setComponentZOrder(pnlRightTrackOrder, 6);   // Track Order Screen
        pnlMainBg.setComponentZOrder(pnlRightHistory, 7);      // History Panel
        pnlMainBg.setComponentZOrder(pnlsearchBar, 8);         // Search Input Box Container
        pnlMainBg.setComponentZOrder(pnlExpandedLocation, 9);   // Location Expander
        pnlMainBg.setComponentZOrder(pnlLocationBanner, 10);    // Location Banner
        pnlMainBg.setComponentZOrder(lblDateRn, 11);            // Live Date Header Label
        pnlMainBg.setComponentZOrder(pnlRestoCard, 12);         // Bottom Layer
        pnlMainBg.revalidate();
        pnlMainBg.repaint();
        
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                String currentProfileName = pnlRightProfile.getName();
                String currentProfileAddress = pnlRightProfile.getAddress();
                if (currentProfileName == null || currentProfileName.trim().isEmpty() || 
                    currentProfileAddress == null || currentProfileAddress.trim().isEmpty()) {
                    showProfileWarningDialog("Profile Incomplete", "Please update your profile information first.");
                }
            }
        });
    }
        public JLabel getLblHome() {
            return lblHome;
    }   public JLabel getLblCart() {
            return lblCart;
    }   public JLabel getLblTrack() {
            return lblTrack;
    }   public JLabel getLblHistory() {
            return lblHistory;
    }   public JLabel getLblProfile() {
            return lblProfile;
    }   public JLabel getLblExit() {
            return lblExit;
    }   public JLabel getLblDateRn() {
            return lblDateRn;
    }   public JPanel getLeftBannerNav() {
            return leftBannerNav;
    }   public JPanel getSearchBarContainer() {
            return pnlsearchBar;
    }   public CustomerDashboardRestaurant getPnlRestoCard() {
            return pnlRestoCard;
    }   public JTextField getTxtSearchBar() {
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
    }   public JButton getBtnNavProfile() {
            return btnNavProfile;
    }   public JButton getBtnNavTrack() {
            return btnNavTrack;
    }   public JButton getBtnNavHistory() {
         return btnNavHistory;
    }   public JButton getBtnNavHome() {
         return btnNavHome;
    }   public JButton getBtnNavExit() {
         return btnNavExit;
    }   public JButton getBtnNavLogoPanda() {
         return btnNavLogoPanda;
    }   public CustomerDashboardViewCart getPnlRightCart() {
         return pnlRightCart;
    }   public CustomerDashboardProfile getPnlRightProfile() {
         return pnlRightProfile;
    }   public CustomerDashboardTrackOrder getPnlRightTrackOrder() {
         return pnlRightTrackOrder;
    }   public CustomerDashboardHistory getPnlRightHistory() {
            return pnlRightHistory;
    }   public CustomerDashboardRestaurantMenus getPnlMenuMcDollibee() { 
        return pnlMenuMcDollibee;
    }   public CustomerDashboardRestaurantMenus getPnlMenuBrainCells() { 
            return pnlMenuBrainCells;
    }   public CustomerDashboardRestaurantMenus getPnlMenuMangPinaasa() { 
            return pnlMenuMangPinaasa;
    }   public JPanel getPnlMainBg() {
            return pnlMainBg;
    }   public JLabel getLblName() {
            return lblName;
    }   public JButton getBtnLogout() {
            return btnNavExit;
    }   public MainFrame getMainFrame() {
            return mainFrame;
    }
    public String getSavedFirstName() { return savedFirstName; }
    public void setSavedFirstName(String val) { this.savedFirstName = val; }
    public String getSavedLastName() { return savedLastName; }
    public void setSavedLastName(String val) { this.savedLastName = val; }
    public String getSavedMobileNumber() { return savedMobileNumber; }
    public void setSavedMobileNumber(String val) { this.savedMobileNumber = val; }
    public String getSavedFullAddress() { return savedFullAddress; }
    public void setSavedFullAddress(String val) { this.savedFullAddress = val; }
}
