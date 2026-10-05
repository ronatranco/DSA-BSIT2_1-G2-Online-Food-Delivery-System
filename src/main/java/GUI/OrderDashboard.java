/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;


import Model.Order;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
/**
 *
 * @author rownavanana
 * MAIN PANEL FOR ORDER
 * @author chloe de la torre
 */
public class OrderDashboard extends JPanel{
     // Color Palette matching your design profile
    private Color PURPLE_DARK   = new Color(74, 20, 140);
    private Color YELLOW_ACCENT = new Color(255, 193, 7);
    private Color TEXT_DARK     = new Color(30, 30, 30);
    private Color COLOR_WHITE  = new Color(255, 255, 255);
    private Color COLOR_MUTED   = new Color(120, 120, 120);

    private JLabel lblOrderSum, lblTotalBill;
    private JButton btnExitCart, btnCheckout;
    
    private JPanel ContainerPanel;
    private JRadioButton RDDelivery, RDPickup;
    private ButtonGroup bgFulfillmentGroup;
    private Order currentOrder;
            

    public OrderDashboard() {
        setLayout(null);
        setBackground(COLOR_WHITE);
        setBounds(1024, 0, 300, 720); 
        setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, COLOR_MUTED));

        btnExitCart = new JButton("X");
        btnExitCart.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnExitCart.setForeground(YELLOW_ACCENT);
        btnExitCart.setBounds(225, 30, 50, 30);
        btnExitCart.setContentAreaFilled(false);
        btnExitCart.setBorderPainted(false);
        btnExitCart.setFocusPainted(true);
        add(btnExitCart);

        lblOrderSum = new JLabel("Order Summary");
        lblOrderSum.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblOrderSum.setForeground(PURPLE_DARK);
        lblOrderSum.setBounds(20, 30, 200, 30);
        add(lblOrderSum);
        
        ContainerPanel = new JPanel();
        ContainerPanel.setLayout(new BoxLayout(ContainerPanel, BoxLayout.Y_AXIS));
        ContainerPanel.setBackground(COLOR_WHITE);
        
        JScrollPane scrollPane = new JScrollPane(ContainerPanel);
        scrollPane.setBounds(15, 65, 270, 390);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane);
        
        RDDelivery = new JRadioButton("Delivery");
        RDDelivery.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        RDDelivery.setBackground(COLOR_WHITE);
        RDDelivery.setSelected(true);
        RDDelivery.setBounds(20, 465, 110, 25);
        RDDelivery.addActionListener( e -> {
            if (currentOrder !=null) currentOrder.setFulfillmentMethod("Delivery");
        });
        
        

        JLabel lblDivider = new JLabel("--------------------------------------------------------------");
        lblDivider.setForeground(COLOR_MUTED);
        lblDivider.setBounds(20, 535, 260, 15);
        add(lblDivider);

        JLabel lblTotalTitle = new JLabel("Total Amount");
        lblTotalTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotalTitle.setForeground(TEXT_DARK);
        lblTotalTitle.setBounds(20, 555, 120, 25);
        add(lblTotalTitle);

        btnCheckout = new JButton("CHECKOUT");
        btnCheckout.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnCheckout.setForeground(YELLOW_ACCENT);
        btnCheckout.setBackground(PURPLE_DARK);
        btnCheckout.setBounds(20, 610, 250, 45);
        btnCheckout.setBorderPainted(false);
        btnCheckout.setFocusPainted(false);
        add(btnCheckout);
        
        System.out.println("HFAUFHAF");
    }
    
    public void addCartActions(ActionListener listener) {
        btnExitCart.addActionListener(listener);
        btnCheckout.addActionListener(listener);
    }

    public JButton getCloseButton() {
        return btnExitCart;
    
    
}
}
