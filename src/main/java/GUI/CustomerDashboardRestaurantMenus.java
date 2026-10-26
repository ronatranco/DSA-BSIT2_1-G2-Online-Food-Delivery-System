/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package GUI;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 *
 * @author klara
 */
public class CustomerDashboardRestaurantMenus extends JPanel {
    private Color BG_LIGHT = new Color(248, 245, 250);
    private Color PURPLE_DARK = new Color(74, 20, 140);
    private Color YELLOW_ACCENT = new Color(255, 193, 7); 
    private Color YELLOW_ACCENT2 = new Color(230, 155, 0);
    private Color TEXT_DARK = new Color(30, 30, 30);       
    private Color COLOR_MUTED = new Color(120, 120, 120);
    private Color COLOR_WHITE = new Color(255, 255, 255);
    private Color CARD_BORDER = new Color(210, 205, 215);
    private String restaurantName;
    private JLabel lblTitle;
    
    private JLabel lblItemName1, lblItemPrice1, lblItemName2, lblItemPrice2;
    
    public CustomerDashboardRestaurantMenus(String restaurantName) {
        this.restaurantName = restaurantName;

        setBounds(80, 0, 944, 720); 
        setLayout(null);
        setBackground(BG_LIGHT);
        setOpaque(true);
        setVisible(false);
        
        lblTitle = new JLabel(restaurantName + " Menu");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(PURPLE_DARK);
        lblTitle.setBounds(30, 30, 500, 40);
        add(lblTitle);
        
        // SEARCH BAR
        JPanel searchBarContainer1 = new JPanel(null);
        searchBarContainer1.setBounds(477, 43, 405, 34); 
        searchBarContainer1.setBackground(COLOR_WHITE);
        searchBarContainer1.setBorder(new LineBorder(PURPLE_DARK, 1)); 
        // SEARCH BAR ICON
        JLabel searchIcon1 = new JLabel(" 🔍");
        searchIcon1.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        searchIcon1.setForeground(PURPLE_DARK);
        searchIcon1.setBounds(10, 0, 30, 38);
        searchBarContainer1.add(searchIcon1);
        // SEARCH BAR FIELD
        JTextField searchBar1 = new JTextField("Search in menu");
        searchBar1.setBounds(47, 2, 351, 30);
        searchBar1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchBar1.setForeground(COLOR_MUTED);
        searchBar1.setBackground(COLOR_WHITE);
        searchBar1.setBorder(null); 
        searchBarContainer1.add(searchBar1);
        add(searchBarContainer1);
        // SEARCH BAR FOCUS LISTENER
        searchIcon1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchIcon1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                String text = searchBar1.getText().trim();
                if (text.isEmpty() || text.equals("Search in menu")) {
                    searchBar1.setText("Search in menu");
                    searchBar1.setForeground(COLOR_MUTED);
                }   requestFocusInWindow();
            }
        });
        searchBar1.addFocusListener(new java.awt.event.FocusListener() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchBar1.getText().equals("Search in menu")) {
                    searchBar1.setText("");
                    searchBar1.setForeground(TEXT_DARK);
                }
        }   @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchBar1.getText().trim().isEmpty()) {
                    searchBar1.setText("Search in menu");
                    searchBar1.setForeground(COLOR_MUTED);
                }
            }
        });
        searchBar1.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                String text = searchBar1.getText().trim();
                if (text.isEmpty() || text.equals("Search in menu")) {
                    searchBar1.setText("Search in menu");
                    searchBar1.setForeground(COLOR_MUTED);
                }
                requestFocusInWindow();
            }
        });     searchBarContainer1.add(searchBar1);
                add(searchBarContainer1);
        
        // HEADER SEPARATOR
        JPanel seperateLine = new JPanel();
        seperateLine.setBackground(YELLOW_ACCENT);
        seperateLine.setBounds(40, 90, 842, 3);
        add(seperateLine);
        setupMenuLayout();
    }
    
    private void setupMenuLayout() {
        if (restaurantName.equalsIgnoreCase("McDollibee")) {
            lblTitle.setVisible(false);
            // LABEL 1
            JLabel lblResto1 = new JLabel("McDollibee");
            lblResto1.setFont(new Font("Segoe UI", Font.BOLD, 36));
            lblResto1.setForeground(PURPLE_DARK);
            lblResto1.setBounds(40, 37, 260, 55);
            add(lblResto1);
            // LABEL 2
            JLabel lblPopular1 = new JLabel("Popular");
            lblPopular1.setFont(new Font("Segoe UI", Font.BOLD, 29));
            lblPopular1.setForeground(PURPLE_DARK);
            lblPopular1.setBounds(66, 130, 200, 35);
            add(lblPopular1);
            // LABEL ICON
            JLabel fireIcon1 = new JLabel("🔥");
            fireIcon1.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
            fireIcon1.setForeground(PURPLE_DARK);
            fireIcon1.setBounds(37, 131, 30, 42);
            add(fireIcon1);
            // LABEL 2 SUBTITLE
            JLabel lblPSub1 = new JLabel("Most ordered right neow.");
            lblPSub1.setFont(new Font("Segoe UI", Font.PLAIN, 19));
            lblPSub1.setForeground(COLOR_MUTED);
            lblPSub1.setBounds(40, 160, 300, 30);
            add(lblPSub1);
            // FOOD CARD ROW 1 (POPULAR)
            add(menuResto(40, 205, "Chicken Joy Meal", "₱99.00"));
            add(menuResto(474, 205, "Burger Steak", "₱75.00"));
            // LABEL 3
            JLabel lblWhatsNew1 = new JLabel("What's New");
            lblWhatsNew1.setFont(new Font("Segoe UI", Font.BOLD, 29));
            lblWhatsNew1.setForeground(PURPLE_DARK);
            lblWhatsNew1.setBounds(40, 390, 200, 30);
            add(lblWhatsNew1);
            // FOOD CARD ROW 2 (WHAT'S NEW)
            add(menuResto(40, 445, "Jolly Spaghetti", "₱65.00"));
            add(menuResto(474, 445, "Extra Rice", "₱25.00"));
        
            
        } else if (restaurantName.equalsIgnoreCase("Brain Cells Milk Tea")) {
            lblTitle.setVisible(false);
            // LABEL 1
            JLabel lblResto2 = new JLabel("Brain Cells Milk Tea");
            lblResto2.setFont(new Font("Segoe UI", Font.BOLD, 36));
            lblResto2.setForeground(PURPLE_DARK);
            lblResto2.setBounds(40, 35, 350, 55);
            add(lblResto2);
            // LABEL 2
            JLabel lblPopular2 = new JLabel("Popular");
            lblPopular2.setFont(new Font("Segoe UI", Font.BOLD, 29));
            lblPopular2.setForeground(PURPLE_DARK);
            lblPopular2.setBounds(66, 130, 200, 35);
            add(lblPopular2);
            // LABEL 2 ICON
            JLabel fireIcon2 = new JLabel("🔥");
            fireIcon2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
            fireIcon2.setForeground(PURPLE_DARK);
            fireIcon2.setBounds(37, 131, 30, 42);
            add(fireIcon2);
            // LABEL 2 SUBTITLE
            JLabel lblPSub2 = new JLabel("Most ordered right neow.");
            lblPSub2.setFont(new Font("Segoe UI", Font.PLAIN, 19));
            lblPSub2.setForeground(COLOR_MUTED);
            lblPSub2.setBounds(40, 160, 300, 30);
            add(lblPSub2);
            // FOOD CARD ROW 1 (POPULAR)
            add(menuResto(40, 205, "Wintermelon Milk Tea", "₱110.00"));
            add(menuResto(474, 205, "Cheese Wintermelon", "₱125.00"));
            // LABEL 3
            JLabel lblWhatsNew2 = new JLabel("What's New");
            lblWhatsNew2.setFont(new Font("Segoe UI", Font.BOLD, 29));
            lblWhatsNew2.setForeground(PURPLE_DARK);
            lblWhatsNew2.setBounds(40, 390, 200, 30);
            add(lblWhatsNew2);
            // FOOD CARD ROW 2 (WHAT'S NEW)
            add(menuResto(40, 445, "Dark Choco Matcha", "₱135.00"));    
            add(menuResto(474, 445, "Oreo Overload", "₱140.00"));
            
        } else if (restaurantName.equalsIgnoreCase("Mang Pinaasa")) {
            lblTitle.setVisible(false);
            // LABEL 1
            JLabel lblResto3 = new JLabel("Mang Pinaasa");
            lblResto3.setFont(new Font("Segoe UI", Font.BOLD, 36));
            lblResto3.setForeground(PURPLE_DARK);
            lblResto3.setBounds(40, 35, 350, 55);
            add(lblResto3); 
            // LABEL 2
            JLabel lblPopular3 = new JLabel("Popular");
            lblPopular3.setFont(new Font("Segoe UI", Font.BOLD, 29));
            lblPopular3.setForeground(PURPLE_DARK);
            lblPopular3.setBounds(66, 130, 200, 35);
            add(lblPopular3);
            // LABEL 2 ICON
            JLabel fireIcon3 = new JLabel("🔥");
            fireIcon3.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
            fireIcon3.setForeground(PURPLE_DARK);
            fireIcon3.setBounds(37, 131, 30, 42);
            add(fireIcon3);
            // LABEL 2 SUBTITLE
            JLabel lblPSub3 = new JLabel("Most ordered right neow.");
            lblPSub3.setFont(new Font("Segoe UI", Font.PLAIN, 19));
            lblPSub3.setForeground(COLOR_MUTED);
            lblPSub3.setBounds(40, 160, 300, 30);
            add(lblPSub3);
            // FOOD CARD ROW 1 (POPULAR)
            add(menuResto(40, 205, "Pork Sisig Meal", "₱120.00"));
            add(menuResto(474, 205, "Bangus Sisig", "₱130.00"));
            // LABEL 3
            JLabel lblWhatsNew3 = new JLabel("What's New");
            lblWhatsNew3.setFont(new Font("Segoe UI", Font.BOLD, 29));
            lblWhatsNew3.setForeground(PURPLE_DARK);
            lblWhatsNew3.setBounds(40, 390, 200, 30);
            add(lblWhatsNew3);
            // FOOD CARD ROW 2 (WHAT'S NEW)
            add(menuResto(40, 445, "Bulalo Special", "₱180.00"));
            add(menuResto(474, 445, "Lechon Kawali", "₱160.00"));
        }
    }
    // HELPER METHOD
    private JPanel menuResto(int x, int y, String itemName, String itemPrice) {
        // FOOD CARD
        JPanel foodCard = new JPanel(null);
        foodCard.setBounds(x, y, 410, 145);
        foodCard.setBackground(COLOR_WHITE);
        foodCard.setBorder(new LineBorder(PURPLE_DARK, 1));
        // FOOD IMAGE PLACEHOLDER
        JLabel imageBlock = new JLabel();
        imageBlock.setBounds(15, 15, 120, 85);
        imageBlock.setHorizontalAlignment(SwingConstants.CENTER);
        
        String fileName = itemName.toLowerCase().replace(" ", "_") + ".png";
        ImageIcon loadedIcon = null;
        
        String[] pathsToTry = {
            "src/main/java/GUI/images/" + fileName,
            "GUI/images/" + fileName,
            "/GUI/images/" + fileName
        };
        
        for (String path : pathsToTry) {
            if (path.startsWith("src/")) {
                java.io.File file = new java.io.File(path);
                if (file.exists()) {
                    loadedIcon = new ImageIcon(path);
                    break;
                }
            } else {
                java.net.URL url = getClass().getResource(path);
                if (url != null) {
                    loadedIcon = new ImageIcon(url);
                    break;
                }
            }
        }
        
        if (loadedIcon != null && loadedIcon.getIconWidth() > 0) {
            Image scaledImg = loadedIcon.getImage().getScaledInstance(120, 85, Image.SCALE_SMOOTH);
            imageBlock.setIcon(new ImageIcon(scaledImg));
            imageBlock.setOpaque(false);
        } else {
            imageBlock.setBorder(new LineBorder(new Color(225, 222, 230), 1));
            imageBlock.setBackground(new Color(242, 240, 245));
            imageBlock.setOpaque(true);
        }
        foodCard.add(imageBlock);
        
        // ITEM NAME LABEL 
        JLabel lblItemName = new JLabel(itemName);
        lblItemName.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblItemName.setForeground(TEXT_DARK);
        lblItemName.setBounds(148, 15, 245, 22);
        foodCard.add(lblItemName);
        // DETAILS / PRICE
        JLabel lblPrice = new JLabel(itemPrice);
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPrice.setForeground(PURPLE_DARK);
        lblPrice.setBounds(15, 112, 80, 18);
        foodCard.add(lblPrice);
        // BUTTON
        JLabel btnAdd = new JLabel("ADD TO ORDER", SwingConstants.RIGHT);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAdd.setForeground(YELLOW_ACCENT2);
        btnAdd.setBounds(240, 110, 145, 20);
        btnAdd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        btnAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                Container parent = getParent();
                while (parent != null && !(parent instanceof CustomerDashboard)) {
                    parent = parent.getParent();
                }

                if (parent instanceof CustomerDashboard) {
                    CustomerDashboard dashboard = (CustomerDashboard) parent;
                    
                    String priceClean = itemPrice.replace("₱", "").trim();
                    double priceVal = Double.parseDouble(priceClean);
                    String[][] itemRow = {{ itemName, "1", String.valueOf(priceVal) }};
                    
                    dashboard.getPnlRightCart().addRestaurantCards(restaurantName, itemRow);
                    dashboard.getPnlRightCart().updateCartTotals();
                    dashboard.getPnlRightCart().animateOpen();
                }
            }
        });

        foodCard.add(btnAdd);
        return foodCard;
    }
}