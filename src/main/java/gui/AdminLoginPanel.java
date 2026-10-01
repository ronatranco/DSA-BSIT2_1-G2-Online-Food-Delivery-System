package gui;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
/**
 *
 * @author rownavanana
 */

public class AdminLoginPanel extends JPanel {
    private Color PURPLE_DARK = new Color(74, 20, 140);
    private Color PURPLE_LIGHT = new Color(156, 39, 176);
    private Color YELLOW_ACCENT = new Color(255, 193, 7);
    private Color BG_LIGHT = new Color(248, 245, 250);
    private Color TEXT_DARK = new Color(30, 30, 30);

    private Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public JTextField txtAdminUser;
    public JPasswordField txtAdminPass;
    public JCheckBox chkAdminSavePass;
    public JButton btnAdminLogin, btnAdminLoginBack;

    public AdminLoginPanel(ActionListener listener) {
        setLayout(null);
        setBackground(BG_LIGHT);

        GradientPanel leftBannerAdmin = new GradientPanel();
        leftBannerAdmin.setBounds(0, 0, 480, 720);
        JLabel logoAdmin = new JLabel("🐼", SwingConstants.CENTER);
        logoAdmin.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 140));
        logoAdmin.setBounds(20, 140, 440, 180);
        leftBannerAdmin.add(logoAdmin);
        JLabel titleAdminBanner = new JLabel("GrabPanda", SwingConstants.CENTER);
        titleAdminBanner.setFont(new Font("Segoe UI", Font.BOLD, 38));
        titleAdminBanner.setForeground(YELLOW_ACCENT);
        titleAdminBanner.setBounds(20, 330, 440, 45);
        leftBannerAdmin.add(titleAdminBanner);
        JLabel subAdminBanner = new JLabel("Fast & Fresh Food Delivery", SwingConstants.CENTER);
        subAdminBanner.setFont(new Font("Segoe UI", Font.BOLD, 16));
        subAdminBanner.setForeground(Color.WHITE);
        subAdminBanner.setBounds(20, 380, 440, 30);
        leftBannerAdmin.add(subAdminBanner);
        add(leftBannerAdmin);

        JPanel rightPanelAdmin = new JPanel(null);
        rightPanelAdmin.setBounds(480, 0, 544, 720);
        rightPanelAdmin.setBackground(BG_LIGHT);

        JLabel lblAdminTitle = new JLabel("Admin Login", SwingConstants.LEFT);
        lblAdminTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblAdminTitle.setForeground(PURPLE_DARK);
        lblAdminTitle.setBounds(60, 100, 400, 45);
        rightPanelAdmin.add(lblAdminTitle);

        JLabel subAdminTitle = new JLabel("Please enter admin credentials");
        subAdminTitle.setFont(REGULAR_FONT);
        subAdminTitle.setForeground(TEXT_DARK);
        subAdminTitle.setBounds(60, 145, 400, 25);
        rightPanelAdmin.add(subAdminTitle);

        JLabel lblAdminUser = new JLabel("Username");
        lblAdminUser.setFont(REGULAR_FONT);
        lblAdminUser.setBounds(60, 195, 380, 20);
        txtAdminUser = new JTextField();
        txtAdminUser.setBounds(60, 220, 380, 38);
        rightPanelAdmin.add(lblAdminUser);
        rightPanelAdmin.add(txtAdminUser);

        JLabel lblAdminPass = new JLabel("Password");
        lblAdminPass.setFont(REGULAR_FONT);
        lblAdminPass.setBounds(60, 270, 380, 20);
        txtAdminPass = new JPasswordField();
        JPanel passPanelAdmin = createPasswordFieldWithEye(txtAdminPass);
        passPanelAdmin.setBounds(60, 295, 380, 38);
        rightPanelAdmin.add(lblAdminPass);
        rightPanelAdmin.add(passPanelAdmin);

        chkAdminSavePass = new JCheckBox("Save Password");
        chkAdminSavePass.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkAdminSavePass.setBackground(BG_LIGHT);
        chkAdminSavePass.setBounds(60, 340, 150, 20);
        chkAdminSavePass.setFocusPainted(false);
        rightPanelAdmin.add(chkAdminSavePass);

        btnAdminLogin = new JButton("Login");
        btnAdminLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnAdminLogin.setBackground(PURPLE_LIGHT);
        btnAdminLogin.setForeground(Color.WHITE);
        btnAdminLogin.setFocusPainted(false);
        btnAdminLogin.setBounds(60, 385, 380, 48);
        btnAdminLogin.addActionListener(listener);
        rightPanelAdmin.add(btnAdminLogin);

        btnAdminLoginBack = new JButton("← Go Back");
        btnAdminLoginBack.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAdminLoginBack.setForeground(PURPLE_DARK);
        btnAdminLoginBack.setContentAreaFilled(false);
        btnAdminLoginBack.setBorderPainted(false);
        btnAdminLoginBack.setFocusPainted(false);
        btnAdminLoginBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdminLoginBack.setBounds(50, 445, 120, 30);
        btnAdminLoginBack.addActionListener(listener);
        rightPanelAdmin.add(btnAdminLoginBack);

        add(rightPanelAdmin);
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