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
 */
public class RestaurantLoginPanel extends JPanel {
    private Color PURPLE_DARK = new Color(74, 20, 140);
    private Color YELLOW_ACCENT = new Color(255, 193, 7);
    private Color BG_LIGHT = new Color(248, 245, 250);
    private Color TEXT_DARK = new Color(30, 30, 30);

    private Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public JTextField txtRestaurantUser;
    public JPasswordField txtRestaurantPass;
    public JCheckBox chkRestaurantSavePass;
    public JButton btnRestaurantLogin, btnRestaurantForgotPassword, btnRestaurantLoginBack;
    private MainFrame mainFrame;

    public RestaurantLoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setLayout(null);
        setBackground(BG_LIGHT);

        GradientPanel leftBannerRestaurant = new GradientPanel();
        leftBannerRestaurant.setBounds(0, 0, 480, 720);
        JLabel logoRestaurant = new JLabel("🐼", SwingConstants.CENTER);
        logoRestaurant.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 140));
        logoRestaurant.setBounds(20, 140, 440, 180);
        leftBannerRestaurant.add(logoRestaurant);
        JLabel titleRestaurantBanner = new JLabel("GrabPanda", SwingConstants.CENTER);
        titleRestaurantBanner.setFont(new Font("Segoe UI", Font.BOLD, 38));
        titleRestaurantBanner.setForeground(YELLOW_ACCENT);
        titleRestaurantBanner.setBounds(20, 330, 440, 45);
        leftBannerRestaurant.add(titleRestaurantBanner);
        JLabel subRestaurantBanner = new JLabel("Fast & Fresh Food Delivery", SwingConstants.CENTER);
        subRestaurantBanner.setFont(new Font("Segoe UI", Font.BOLD, 16));
        subRestaurantBanner.setForeground(Color.WHITE);
        subRestaurantBanner.setBounds(20, 380, 440, 30);
        leftBannerRestaurant.add(subRestaurantBanner);
        add(leftBannerRestaurant);

        JPanel rightPanelRestaurant = new JPanel(null);
        rightPanelRestaurant.setBounds(480, 0, 544, 720);
        rightPanelRestaurant.setBackground(BG_LIGHT);

        JLabel lblRestaurantTitle = new JLabel("Restaurant Login", SwingConstants.LEFT);
        lblRestaurantTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblRestaurantTitle.setForeground(PURPLE_DARK);
        lblRestaurantTitle.setBounds(60, 100, 400, 45);
        rightPanelRestaurant.add(lblRestaurantTitle);

        JLabel subRestaurantTitle = new JLabel("Please enter your restaurant details to log in");
        subRestaurantTitle.setFont(REGULAR_FONT);
        subRestaurantTitle.setForeground(TEXT_DARK);
        subRestaurantTitle.setBounds(60, 145, 400, 25);
        rightPanelRestaurant.add(subRestaurantTitle);

        JLabel lblRestaurantUser = new JLabel("Username");
        lblRestaurantUser.setFont(REGULAR_FONT);
        lblRestaurantUser.setBounds(60, 195, 380, 20);
        txtRestaurantUser = new JTextField();
        txtRestaurantUser.setBounds(60, 220, 380, 38);
        rightPanelRestaurant.add(lblRestaurantUser);
        rightPanelRestaurant.add(txtRestaurantUser);

        JLabel lblRestaurantPass = new JLabel("Password");
        lblRestaurantPass.setFont(REGULAR_FONT);
        lblRestaurantPass.setBounds(60, 270, 380, 20);
        txtRestaurantPass = new JPasswordField();
        JPanel passPanelRestaurant = createPasswordFieldWithEye(txtRestaurantPass);
        passPanelRestaurant.setBounds(60, 295, 380, 38);
        rightPanelRestaurant.add(lblRestaurantPass);
        rightPanelRestaurant.add(passPanelRestaurant);

        chkRestaurantSavePass = new JCheckBox("Save Password");
        chkRestaurantSavePass.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkRestaurantSavePass.setBackground(BG_LIGHT);
        chkRestaurantSavePass.setBounds(60, 340, 150, 20);
        chkRestaurantSavePass.setFocusPainted(false);
        rightPanelRestaurant.add(chkRestaurantSavePass);

        btnRestaurantForgotPassword = new JButton("Forgot password?");
        btnRestaurantForgotPassword.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRestaurantForgotPassword.setForeground(PURPLE_DARK);
        btnRestaurantForgotPassword.setContentAreaFilled(false);
        btnRestaurantForgotPassword.setBorderPainted(false);
        btnRestaurantForgotPassword.setFocusPainted(false);
        btnRestaurantForgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRestaurantForgotPassword.setBounds(300, 338, 140, 25);
        rightPanelRestaurant.add(btnRestaurantForgotPassword);

        btnRestaurantLogin = new JButton("Login");
        btnRestaurantLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnRestaurantLogin.setBackground(YELLOW_ACCENT);
        btnRestaurantLogin.setForeground(Color.BLACK);
        btnRestaurantLogin.setFocusPainted(false);
        btnRestaurantLogin.setBounds(60, 385, 380, 48);
        rightPanelRestaurant.add(btnRestaurantLogin);

        btnRestaurantLoginBack = new JButton("← Go Back");
        btnRestaurantLoginBack.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRestaurantLoginBack.setForeground(PURPLE_DARK);
        btnRestaurantLoginBack.setContentAreaFilled(false);
        btnRestaurantLoginBack.setBorderPainted(false);
        btnRestaurantLoginBack.setFocusPainted(false);
        btnRestaurantLoginBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRestaurantLoginBack.setBounds(50, 445, 120, 30);
        rightPanelRestaurant.add(btnRestaurantLoginBack);

        add(rightPanelRestaurant);
    }

    private JPanel createPasswordFieldWithEye(JPasswordField passField) {
        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.setBackground(Color.WHITE);
        fieldPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        passField.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        passField.setBackground(Color.WHITE);

        JToggleButton btnEye = new JToggleButton("👁");
        btnEye.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 16));
        btnEye.setPreferredSize(new Dimension(45, 32));
        btnEye.setMargin(new Insets(0, 0, 0, 0));
        btnEye.setContentAreaFilled(false);
        btnEye.setBorderPainted(false);
        btnEye.setFocusPainted(false);
        btnEye.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnEye.addActionListener(e -> {
            if (btnEye.isSelected()) {
                passField.setEchoChar((char) 0);
            } else {
                passField.setEchoChar('•');
            }
        });

        fieldPanel.add(passField, BorderLayout.CENTER);
        fieldPanel.add(btnEye, BorderLayout.EAST);
        return fieldPanel;
    }
}