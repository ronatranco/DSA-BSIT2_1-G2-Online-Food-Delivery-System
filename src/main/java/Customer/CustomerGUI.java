/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Customer;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
/**
 *
 * @author klara
 */
public class CustomerGUI extends JPanel{
private Color PURPLE_DARK = new Color(74, 20, 140);
    private Color YELLOW_ACCENT = new Color(255, 193, 7);
    private Color BG_LIGHT = new Color(248, 245, 250);
    private Color TEXT_DARK = new Color(30, 30, 30);

    private Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public JTextField txtSearch;
    public JButton btnFilter;
    public JButton[] btnRestaurants = new JButton[5];
    public JButton btnNavHome, btnNavOrders, btnNavCart, btnNavProfile;

    public CustomerGUI (ActionListener listener) {
        setLayout(null);
        setBackground(BG_LIGHT);

        // --- Top Search Bar Container ---
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBounds(60, 40, 904, 50);
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));

        JLabel lblSearchIcon = new JLabel("🔍");
        lblSearchIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));

        txtSearch = new JTextField("What would u like to eat?");
        txtSearch.setFont(REGULAR_FONT);
        txtSearch.setForeground(Color.GRAY);
        txtSearch.setBorder(null);

        btnFilter = new JButton("≡");
        btnFilter.setFont(new Font("Segoe UI", Font.BOLD, 22));
        btnFilter.setContentAreaFilled(false);
        btnFilter.setBorderPainted(false);
        btnFilter.setFocusPainted(false);
        btnFilter.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFilter.addActionListener(listener);

        searchPanel.add(lblSearchIcon, BorderLayout.WEST);
        searchPanel.add(txtSearch, BorderLayout.CENTER);
        searchPanel.add(btnFilter, BorderLayout.EAST);
        add(searchPanel);

        // --- Restaurants Title ---
        JLabel lblRestaurantsTitle = new JLabel("RESTAURANTS");
        lblRestaurantsTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblRestaurantsTitle.setForeground(TEXT_DARK);
        lblRestaurantsTitle.setBounds(60, 180, 400, 45);
        add(lblRestaurantsTitle);

        // --- Horizontal List of 5 Restaurants ---
        int startX = 60;
        int boxWidth = 150;
        int gap = 38;
        for (int i = 0; i < 5; i++) {
            btnRestaurants[i] = new JButton("Restaurant " + (i + 1));
            btnRestaurants[i].setBounds(startX + i * (boxWidth + gap), 250, boxWidth, boxWidth);
            btnRestaurants[i].setBackground(Color.WHITE);
            btnRestaurants[i].setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnRestaurants[i].setForeground(PURPLE_DARK);
            btnRestaurants[i].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
            btnRestaurants[i].setFocusPainted(false);
            btnRestaurants[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnRestaurants[i].addActionListener(listener);
            add(btnRestaurants[i]);
        }

        // --- Bottom Navigation Bar Container ---
        JPanel bottomNavPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        bottomNavPanel.setBounds(60, 600, 904, 60);
        bottomNavPanel.setBackground(Color.WHITE);
        bottomNavPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        btnNavHome = createNavButton("🏠 Home");
        btnNavOrders = createNavButton("📍 Orders");
        btnNavCart = createNavButton("🛒 Cart");
        btnNavProfile = createNavButton("👤 Profile");

        btnNavHome.addActionListener(listener);
        btnNavOrders.addActionListener(listener);
        btnNavCart.addActionListener(listener);
        btnNavProfile.addActionListener(listener);

        bottomNavPanel.add(btnNavHome);
        bottomNavPanel.add(btnNavOrders);
        bottomNavPanel.add(btnNavCart);
        bottomNavPanel.add(btnNavProfile);

        add(bottomNavPanel);
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        btn.setForeground(PURPLE_DARK);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
