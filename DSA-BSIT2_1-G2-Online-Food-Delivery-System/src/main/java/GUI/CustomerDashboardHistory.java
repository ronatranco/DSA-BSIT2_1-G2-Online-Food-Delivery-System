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
public class CustomerDashboardHistory extends JPanel{
    private Color PURPLE_DARK   = new Color(74, 20, 140);
    private Color YELLOW_ACCENT = new Color(255, 193, 7);
    private Color TEXT_DARK     = new Color(30, 30, 30);
    private Color COLOR_WHITE   = new Color(255, 255, 255);
    private Color COLOR_MUTED   = new Color(120, 120, 120);
    private Font HEADER_FONT  = new Font("Segoe UI", Font.BOLD, 26);
    
    private JButton btnExitHistory;
    
    public CustomerDashboardHistory() {
        setLayout(null);
        setBackground(COLOR_WHITE);
        setBounds(1024, 0, 944, 720); 
        setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, YELLOW_ACCENT));
        
        // FIX 1: Initializing the missing close 'X' button so it exists!
        btnExitHistory = new JButton("X");
        btnExitHistory.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnExitHistory.setForeground(YELLOW_ACCENT);
        btnExitHistory.setBounds(715, 30, 50, 30);
        btnExitHistory.setContentAreaFilled(false);
        btnExitHistory.setBorderPainted(false);
        btnExitHistory.setFocusPainted(true);
        add(btnExitHistory);

        JLabel lblTitle = new JLabel("Order History");
        lblTitle.setFont(HEADER_FONT);
        lblTitle.setForeground(PURPLE_DARK);
        lblTitle.setBounds(35, 33, 210, 35);
        add(lblTitle);
     
    }

    public void animateOpen() {
        setBounds(200, 0, 824, 720); 
    }

    public void animateClose() {
        setBounds(1024, 0, 824, 720); 
    }

    public void addProfileActions(ActionListener listener) {
        btnExitHistory.addActionListener(listener);
        
    }

    public JButton getCloseButton() {
        return btnExitHistory;
    }
}

