/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package GUI;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
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
    
    private List<JPanel> foodCardsList = new ArrayList<>();
    private String restaurantName;
    private JLabel lblTitle;
    
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
        // SEARCH BAR FOCUS & ACTION LISTENERS
        searchIcon1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchIcon1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                String text = searchBar1.getText().trim();
                if (text.isEmpty() || text.equals("Search in menu")) {
                    searchBar1.setText("Search in menu");
                    searchBar1.setForeground(COLOR_MUTED);
                }   
                    searchMenu(text);
                    requestFocusInWindow();
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
                    searchMenu("");
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
                    searchMenu(text);
                    requestFocusInWindow();
            }
        });  
        
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
            // LABEL 3
            JLabel lblWhatsNew1 = new JLabel("What's New");
            lblWhatsNew1.setFont(new Font("Segoe UI", Font.BOLD, 29));
            lblWhatsNew1.setForeground(PURPLE_DARK);
            lblWhatsNew1.setBounds(40, 390, 200, 30);
            add(lblWhatsNew1);
            // FOOD CARD ROW 1 (POPULAR & IMAGES)
            JPanel card1 = menuResto(40, 205, "src/main/java/gui/images/cdrestaurantmenuchicken.png", "McChicken Joy w/ Rice",
                          "Crispy on the outside, juicy on the inside, and served with fluffy rice and savory gravy.", "₱135");
            JPanel card2 = menuResto(474, 205, "src/main/java/gui/images/cdrestaurantmenuburger.png", "Yum Big Mac Burger",
                          "Two juicy beef patties, legendary special sauce, crisp lettuce, cheese, pickles, and onions on a toasted sesame bun.", "₱120");
            // FOOD CARD ROW 2 (WHAT'S NEW & IMAGES)
            JPanel card3 = menuResto(40, 445, "src/main/java/gui/images/cdrestaurantmenupapie.png", "Peach Apple Pie", 
                          "A warm, crispy crust filled with sweet, gooey peach and apple goodness.", "₱65");
            JPanel card4 = menuResto(474, 445, "", "Unavailable", "Description Unavailable.", "₱");
            add(card1); foodCardsList.add(card1);
            add(card2); foodCardsList.add(card2);
            add(card3); foodCardsList.add(card3);
            add(card4); foodCardsList.add(card4);
            
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
            // LABEL 3
            JLabel lblWhatsNew2 = new JLabel("What's New");
            lblWhatsNew2.setFont(new Font("Segoe UI", Font.BOLD, 29));
            lblWhatsNew2.setForeground(PURPLE_DARK);
            lblWhatsNew2.setBounds(40, 390, 200, 30);
            add(lblWhatsNew2);
            // FOOD CARD ROW 1 (POPULAR)
            JPanel card1 = menuResto(40, 205, "src/main/java/gui/images/cdrestaurantmenumilktea.png", "Watermelon Milk Tea", 
                                     "A refreshing blend of creamy milk tea and sweet, juicy watermelon flavor.", "₱70");
            JPanel card2 = menuResto(474, 205, "src/main/java/gui/images/cdrestaurantmenumatcha.png", "Matcha", 
                                     "Smooth, earthy green tea blended into a rich and creamy treat.", "₱80");
            // FOOD CARD ROW 2 (WHAT'S NEW)
            JPanel card3 = menuResto(40, 445, "src/main/java/gui/images/cdrestaurantmenudarkchoco.png", "Dark Choco", 
                                    "Deeply decadent and velvety chocolate crafted for the ultimate sweet tooth.", "₱70");
            JPanel card4 = menuResto(474, 445, "",  "Unavailable", "Description Unavailable.", "₱");
            add(card1); foodCardsList.add(card1);
            add(card2); foodCardsList.add(card2);
            add(card3); foodCardsList.add(card3);
            add(card4); foodCardsList.add(card4);
            
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
            // LABEL 3
            JLabel lblWhatsNew3 = new JLabel("What's New");
            lblWhatsNew3.setFont(new Font("Segoe UI", Font.BOLD, 29));
            lblWhatsNew3.setForeground(PURPLE_DARK);
            lblWhatsNew3.setBounds(40, 390, 200, 30);
            add(lblWhatsNew3);
            // FOOD CARD ROW 1 (POPULAR)
            JPanel card1 = menuResto(40, 205, "src/main/java/gui/images/cdrestaurantmenupaa.png", "Paa Malaki", 
                                     "Soft, tender bites simmered in a savory, finger-licking-good savory glaze.", "₱165");
            JPanel card2 = menuResto(474, 205, "src/main/java/gui/images/cdrestaurantmenubbq.png", "Pork Bbq", 
                                     "Smoky, skewered grilled pork brushed in a sweet and savory marinade.", "₱140");
            // FOOD CARD ROW 2 (WHAT'S NEW)
            JPanel card3 = menuResto(40, 445, "src/main/java/gui/images/cdrestaurantmenupliempo.png", "Pork Liempo", 
                                     "Juicy grilled pork belly with ultra-crispy skin, served with a tangy vinegar dip.", "₱150");
            JPanel card4 = menuResto(474, 445, "",  "Unavailable", "Description Unavailable.", "₱");
            add(card1); foodCardsList.add(card1);
            add(card2); foodCardsList.add(card2);
            add(card3); foodCardsList.add(card3);
            add(card4); foodCardsList.add(card4);
        }
    }
    // SEARCH MENU BAR
    public void searchMenu(String query) {
        if (query.trim().isEmpty() || query.equals("Search in menu")) {
            for (JPanel card : foodCardsList) {
                card.setVisible(true);
            }
        } else {
            String lowerQuery = query.toLowerCase().trim();
            for (JPanel card : foodCardsList) {
                String foodName = card.getName() != null ? card.getName().toLowerCase() : "";
                if (foodName.contains(lowerQuery) && !foodName.equals("food name")) {
                    card.setVisible(true);
                } else {
                    card.setVisible(false);
                }
            }
        }
        revalidate();
        repaint();
    }
    // HELPER METHOD
    private JPanel menuResto(int x, int y, String imgPath, String foodName, String description, String priceText) {
        // FOOD CARD
        JPanel foodCard = new JPanel(null);
        foodCard.setName(foodName);
        foodCard.setBounds(x, y, 410, 145);
        foodCard.setBackground(COLOR_WHITE);
        foodCard.setBorder(new LineBorder(PURPLE_DARK, 1));
        // FOOD IMAGE CONTAINER
        JPanel imageBlock = new JPanel(new BorderLayout());
        imageBlock.setBounds(15, 15, 130, 85);
        imageBlock.setBackground(new Color(242, 240, 245));
        imageBlock.setBorder(new LineBorder(new Color(225, 222, 230), 1));
        java.io.File imgFile = new java.io.File(imgPath);
        // FOOD IMAGE
        if (imgFile.exists()) {
            java.awt.Image img = new ImageIcon(imgPath).getImage()
                                 .getScaledInstance(130, 85, java.awt.Image.SCALE_SMOOTH);
            JLabel lblImg = new JLabel(new ImageIcon(img));
            imageBlock.add(lblImg, BorderLayout.CENTER);
        } else {
            JLabel lblError = new JLabel("No Image", SwingConstants.CENTER);
            lblError.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            lblError.setForeground(COLOR_MUTED);
            imageBlock.add(lblError, BorderLayout.CENTER);
        }
        foodCard.add(imageBlock);
        // FOOD NAME LABEL
        JLabel lblFoodName = new JLabel(foodName);
        lblFoodName.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblFoodName.setForeground(PURPLE_DARK);
        lblFoodName.setBounds(160, 15, 225, 30);
        foodCard.add(lblFoodName);
        // FOOD DESCRIPTION
        JLabel lblDesc = new JLabel("<html><p style='text-align: justify;'>" + description + "</p></html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDesc.setForeground(COLOR_MUTED);
        lblDesc.setBounds(160, 48, 225, 60);
        lblDesc.setVerticalAlignment(SwingConstants.TOP);
        foodCard.add(lblDesc);
        // PRICE CONTAINER
        JPanel pnlPrice = new JPanel(new BorderLayout());
        pnlPrice.setBounds(15, 112, 65, 18);
        pnlPrice.setBackground(new Color(245, 240, 252));
        pnlPrice.setBorder(new LineBorder(PURPLE_DARK, 1));
        // PRICE TEXT
        JLabel lblPrice = new JLabel(priceText, SwingConstants.CENTER);
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPrice.setForeground(PURPLE_DARK);
        pnlPrice.add(lblPrice, BorderLayout.CENTER);
        foodCard.add(pnlPrice);
        
        // BUTTON
        JLabel btnAdd = new JLabel("ADD TO ORDER", SwingConstants.RIGHT);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAdd.setForeground(YELLOW_ACCENT2);
        btnAdd.setBounds(240, 110, 145, 20);
        btnAdd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // == ADD TO CART LISTENER ==
        btnAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // Prevent adding unavailable items or items without a valid price
                String cleanPrice = priceText.replace("₱", "").trim();
                if (foodName.equalsIgnoreCase("Unavailable") || cleanPrice.isEmpty()) {
                    return;
                }

                Container root = getParent();
                while (root != null && !(root instanceof CustomerDashboard)) {
                    root = root.getParent();
                }
                if (root instanceof CustomerDashboard) {
                    CustomerDashboard dashboard = (CustomerDashboard) root;
                    CustomerDashboardViewCart cart = dashboard.getPnlRightCart();
                    
                    String[][] itemToAdd = { { foodName, "1", cleanPrice } };
                    
                    cart.addRestaurantCards(restaurantName, itemToAdd);
                    cart.updateCartTotals();
                    cart.animateOpen(); // Automatically slides open the cart panel
                }
            }
        }); 
        foodCard.add(btnAdd);
        return foodCard;
            
        }
}