/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
/**
 *
 * @author rownavanana
 */

public class AuthPanel extends JPanel {
    private Color PURPLE_DARK = new Color(74, 20, 140);
    private Color YELLOW_ACCENT = new Color(255, 193, 7);
    private Color BG_LIGHT = new Color(248, 245, 250);
    private Color TEXT_DARK = new Color(30, 30, 30);

    private Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public JButton btnNavLogin, btnNavRegister;

    public AuthPanel(ActionListener listener) {
        setLayout(null);
        setBackground(BG_LIGHT);

        // Left Banner
        GradientPanel leftBannerAuth = new GradientPanel();
        leftBannerAuth.setBounds(0, 0, 480, 720);
        JLabel logoAuth = new JLabel("🐼", SwingConstants.CENTER);
        logoAuth.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 140));
        logoAuth.setBounds(20, 140, 440, 180);
        leftBannerAuth.add(logoAuth);
        JLabel titleAuth = new JLabel("GrabPanda", SwingConstants.CENTER);
        titleAuth.setFont(new Font("Segoe UI", Font.BOLD, 38));
        titleAuth.setForeground(YELLOW_ACCENT);
        titleAuth.setBounds(20, 330, 440, 45);
        leftBannerAuth.add(titleAuth);
        JLabel subAuth = new JLabel("Fast & Fresh Food Delivery", SwingConstants.CENTER);
        subAuth.setFont(new Font("Segoe UI", Font.BOLD, 16));
        subAuth.setForeground(Color.WHITE);
        subAuth.setBounds(20, 380, 440, 30);
        leftBannerAuth.add(subAuth);
        add(leftBannerAuth);

        // Right Panel Options
        JPanel rightPanelAuth = new JPanel(null);
        rightPanelAuth.setBounds(480, 0, 544, 720);
        rightPanelAuth.setBackground(BG_LIGHT);

        JLabel lblWelcome = new JLabel("Delivery po!", SwingConstants.LEFT);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblWelcome.setForeground(PURPLE_DARK);
        lblWelcome.setBounds(60, 140, 400, 45);
        rightPanelAuth.add(lblWelcome);

        JLabel subWelcome = new JLabel("Please select an option to access your account");
        subWelcome.setFont(REGULAR_FONT);
        subWelcome.setForeground(TEXT_DARK);
        subWelcome.setBounds(60, 190, 400, 25);
        rightPanelAuth.add(subWelcome);

        btnNavLogin = new JButton("LOGIN");
        btnNavLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnNavLogin.setBackground(PURPLE_DARK);
        btnNavLogin.setForeground(Color.WHITE);
        btnNavLogin.setFocusPainted(false);
        btnNavLogin.setBounds(60, 250, 380, 50);
        btnNavLogin.addActionListener(listener);
        rightPanelAuth.add(btnNavLogin);

        btnNavRegister = new JButton("SIGN UP");
        btnNavRegister.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnNavRegister.setBackground(YELLOW_ACCENT);
        btnNavRegister.setForeground(Color.BLACK);
        btnNavRegister.setFocusPainted(false);
        btnNavRegister.setBounds(60, 315, 380, 50);
        btnNavRegister.addActionListener(listener);
        rightPanelAuth.add(btnNavRegister);

        add(rightPanelAuth);
    }
}
