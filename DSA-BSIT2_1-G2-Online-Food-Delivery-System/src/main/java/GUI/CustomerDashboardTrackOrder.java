package GUI;


import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.*;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author klara
 */
public class CustomerDashboardTrackOrder extends JPanel{
    private Color PURPLE_DARK   = new Color(74, 20, 140);
    private Color YELLOW_ACCENT = new Color(255, 193, 7);
    private Color TEXT_DARK     = new Color(30, 30, 30);
    private Color COLOR_WHITE   = new Color(255, 255, 255);
    private Color COLOR_MUTED   = new Color(120, 120, 120);
    private Font HEADER_FONT  = new Font("Segoe UI", Font.BOLD, 26);
    
    private JButton btnExitTrackOrder;
    
    public CustomerDashboardTrackOrder() {
        setLayout(null);
        setBackground(COLOR_WHITE);
        setBounds(1024, 0, 944, 720); 
        setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, YELLOW_ACCENT));
        
        btnExitTrackOrder = new JButton("X");
        btnExitTrackOrder.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnExitTrackOrder.setForeground(YELLOW_ACCENT);
        btnExitTrackOrder.setBounds(715, 30, 50, 30);
        btnExitTrackOrder.setContentAreaFilled(false);
        btnExitTrackOrder.setBorderPainted(false);
        btnExitTrackOrder.setFocusPainted(true);
        add(btnExitTrackOrder);

        JLabel lblTitle = new JLabel("Track your order");
        lblTitle.setFont(HEADER_FONT);
        lblTitle.setForeground(PURPLE_DARK);
        lblTitle.setBounds(35, 33, 250, 35);
        add(lblTitle);
        
    }

    public void animateOpen() {
        setBounds(200, 0, 824, 720); 
    }

    public void animateClose() {
        setBounds(1024, 0, 824, 720); 
    }

    public void addProfileActions(ActionListener listener) {
        btnExitTrackOrder.addActionListener(listener);
        
    }

    public JButton getCloseButton() {
        return btnExitTrackOrder;
    }
}

