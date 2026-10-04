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
public class DriverLoginPanel extends JPanel {
    private Color PURPLE_DARK = new Color(74, 20, 140);
    private Color YELLOW_ACCENT = new Color(255, 193, 7);
    private Color BG_LIGHT = new Color(248, 245, 250);
    private Color TEXT_DARK = new Color(30, 30, 30);

    private Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public JTextField txtDriverUser;
    public JPasswordField txtDriverPass;
    public JCheckBox chkDriverSavePass;
    public JButton btnDriverLogin, btnDriverLoginBack;
    private MainFrame mainFrame;

    public DriverLoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setLayout(null);
        setBackground(BG_LIGHT);

        GradientPanel leftBannerDriver = new GradientPanel();
        leftBannerDriver.setBounds(0, 0, 480, 720);
        JLabel logoDriver = new JLabel("🐼", SwingConstants.CENTER);
        logoDriver.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 140));
        logoDriver.setBounds(20, 140, 440, 180);
        leftBannerDriver.add(logoDriver);
        JLabel titleDriverBanner = new JLabel("GrabPanda", SwingConstants.CENTER);
        titleDriverBanner.setFont(new Font("Segoe UI", Font.BOLD, 38));
        titleDriverBanner.setForeground(YELLOW_ACCENT);
        titleDriverBanner.setBounds(20, 330, 440, 45);
        leftBannerDriver.add(titleDriverBanner);
        JLabel subDriverBanner = new JLabel("Fast & Fresh Food Delivery", SwingConstants.CENTER);
        subDriverBanner.setFont(new Font("Segoe UI", Font.BOLD, 16));
        subDriverBanner.setForeground(Color.WHITE);
        subDriverBanner.setBounds(20, 380, 440, 30);
        leftBannerDriver.add(subDriverBanner);
        add(leftBannerDriver);

        JPanel rightPanelDriver = new JPanel(null);
        rightPanelDriver.setBounds(480, 0, 544, 720);
        rightPanelDriver.setBackground(BG_LIGHT);

        JLabel lblDriverTitle = new JLabel("Driver Login", SwingConstants.LEFT);
        lblDriverTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblDriverTitle.setForeground(PURPLE_DARK);
        lblDriverTitle.setBounds(60, 100, 400, 45);
        rightPanelDriver.add(lblDriverTitle);

        JLabel subDriverTitle = new JLabel("Please enter your driver details to log in");
        subDriverTitle.setFont(REGULAR_FONT);
        subDriverTitle.setForeground(TEXT_DARK);
        subDriverTitle.setBounds(60, 145, 400, 25);
        rightPanelDriver.add(subDriverTitle);

        JLabel lblDriverUser = new JLabel("Username");
        lblDriverUser.setFont(REGULAR_FONT);
        lblDriverUser.setBounds(60, 195, 380, 20);
        txtDriverUser = new JTextField();
        txtDriverUser.setBounds(60, 220, 380, 38);
        rightPanelDriver.add(lblDriverUser);
        rightPanelDriver.add(txtDriverUser);

        JLabel lblDriverPass = new JLabel("Password");
        lblDriverPass.setFont(REGULAR_FONT);
        lblDriverPass.setBounds(60, 270, 380, 20);
        txtDriverPass = new JPasswordField();
        JPanel passPanelDriver = createPasswordFieldWithEye(txtDriverPass);
        passPanelDriver.setBounds(60, 295, 380, 38);
        rightPanelDriver.add(lblDriverPass);
        rightPanelDriver.add(passPanelDriver);

        chkDriverSavePass = new JCheckBox("Save Password");
        chkDriverSavePass.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkDriverSavePass.setBackground(BG_LIGHT);
        chkDriverSavePass.setBounds(60, 340, 150, 20);
        chkDriverSavePass.setFocusPainted(false);
        rightPanelDriver.add(chkDriverSavePass);

        btnDriverLogin = new JButton("Login");
        btnDriverLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnDriverLogin.setBackground(YELLOW_ACCENT);
        btnDriverLogin.setForeground(Color.BLACK);
        btnDriverLogin.setFocusPainted(false);
        btnDriverLogin.setBounds(60, 385, 380, 48);
        rightPanelDriver.add(btnDriverLogin);

        btnDriverLoginBack = new JButton("← Go Back");
        btnDriverLoginBack.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnDriverLoginBack.setForeground(PURPLE_DARK);
        btnDriverLoginBack.setContentAreaFilled(false);
        btnDriverLoginBack.setBorderPainted(false);
        btnDriverLoginBack.setFocusPainted(false);
        btnDriverLoginBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDriverLoginBack.setBounds(50, 445, 120, 30);
        rightPanelDriver.add(btnDriverLoginBack);

        add(rightPanelDriver);
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