/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package GUI;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author klara
 */
public class CustomerDashboardRestaurantMenus extends JPanel {
    private Color BG_LIGHT = new Color(248, 245, 250);
    private Color PURPLE_DARK = new Color(74, 20, 140);
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
        
        setupDynamicMenuElements();
    }
    
    private void setupDynamicMenuElements() {
        if (restaurantName.equalsIgnoreCase("McDollibee")) {

            System.out.println("To be added...");
            
        } else if (restaurantName.equalsIgnoreCase("Brain Cells Milk Tea")) {

            System.out.println("To be added...");
            
        } else if (restaurantName.equalsIgnoreCase("Mang Pinaasa")) {

            System.out.println("To be added...");
        }
    }
}