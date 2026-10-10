/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author rownavanana
 * ito naman yung pagpili nila ng role if customer, driver, or admin sila
 */
public class RoleSelectionPanel extends JPanel {
    private Color PURPLE_DARK = new Color(74, 20, 140);
    private Color PURPLE_LIGHT = new Color(156, 39, 176);
    private Color YELLOW_ACCENT = new Color(255, 193, 7);
    private Color BG_LIGHT = new Color(248, 245, 250);
    private Color TEXT_DARK = new Color(30, 30, 30);

    private Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public JButton btnRoleCustomer, btnRoleRestaurant, btnRoleAdmin, btnRoleBack;
    private MainFrame mainFrame;

    public RoleSelectionPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setLayout(null);
        setBackground(BG_LIGHT);

        GradientPanel leftBannerRole = new GradientPanel();
        leftBannerRole.setBounds(0, 0, 480, 720);
        JLabel logoRole = new JLabel("🐼", SwingConstants.CENTER);
        logoRole.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 140));
        logoRole.setBounds(20, 140, 440, 180);
        leftBannerRole.add(logoRole);
        JLabel titleRoleBanner = new JLabel("GrabPanda", SwingConstants.CENTER);
        titleRoleBanner.setFont(new Font("Segoe UI", Font.BOLD, 38));
        titleRoleBanner.setForeground(YELLOW_ACCENT);
        titleRoleBanner.setBounds(20, 330, 440, 45);
        leftBannerRole.add(titleRoleBanner);
        JLabel subRoleBanner = new JLabel("Fast & Fresh Food Delivery", SwingConstants.CENTER);
        subRoleBanner.setFont(new Font("Segoe UI", Font.BOLD, 16));
        subRoleBanner.setForeground(Color.WHITE);
        subRoleBanner.setBounds(20, 380, 440, 30);
        leftBannerRole.add(subRoleBanner);
        add(leftBannerRole);

        JPanel rightPanelRole = new JPanel(null);
        rightPanelRole.setBounds(480, 0, 544, 720);
        rightPanelRole.setBackground(BG_LIGHT);

        JLabel lblRole = new JLabel("Log In As", SwingConstants.LEFT);
        lblRole.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblRole.setForeground(PURPLE_DARK);
        lblRole.setBounds(60, 120, 400, 45);
        rightPanelRole.add(lblRole);

        JLabel subRole = new JLabel("Select your account type to proceed");
        subRole.setFont(REGULAR_FONT);
        subRole.setForeground(TEXT_DARK);
        subRole.setBounds(60, 170, 400, 25);
        rightPanelRole.add(subRole);

        btnRoleCustomer = new JButton("CUSTOMER");
        btnRoleCustomer.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRoleCustomer.setBackground(PURPLE_DARK);
        btnRoleCustomer.setForeground(Color.WHITE);
        btnRoleCustomer.setFocusPainted(false);
        btnRoleCustomer.setBounds(60, 230, 380, 50);
        rightPanelRole.add(btnRoleCustomer);

        btnRoleRestaurant = new JButton("RESTAURANT");
        btnRoleRestaurant.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRoleRestaurant.setBackground(YELLOW_ACCENT);
        btnRoleRestaurant.setForeground(Color.BLACK);
        btnRoleRestaurant.setFocusPainted(false);
        btnRoleRestaurant.setBounds(60, 295, 380, 50);
        rightPanelRole.add(btnRoleRestaurant);

        btnRoleAdmin = new JButton("ADMIN");
        btnRoleAdmin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRoleAdmin.setBackground(PURPLE_LIGHT);
        btnRoleAdmin.setForeground(Color.WHITE);
        btnRoleAdmin.setFocusPainted(false);
        btnRoleAdmin.setBounds(60, 360, 380, 50);
        rightPanelRole.add(btnRoleAdmin);

        btnRoleBack = new JButton("← Go Back");
        btnRoleBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRoleBack.setForeground(PURPLE_DARK);
        btnRoleBack.setContentAreaFilled(false);
        btnRoleBack.setBorderPainted(false);
        btnRoleBack.setFocusPainted(false);
        btnRoleBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRoleBack.setBounds(50, 425, 120, 30);
        rightPanelRole.add(btnRoleBack);

        add(rightPanelRole);
    }
}