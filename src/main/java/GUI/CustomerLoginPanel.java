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
public class CustomerLoginPanel extends JPanel {
    private Color PURPLE_DARK = new Color(74, 20, 140);
    private Color YELLOW_ACCENT = new Color(255, 193, 7);
    private Color BG_LIGHT = new Color(248, 245, 250);
    private Color TEXT_DARK = new Color(30, 30, 30);

    private Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public JTextField txtLoginEmailOrUser;
    public JPasswordField txtLoginPassword;
    public JCheckBox chkLoginSavePassword;
    public JButton btnCustomerLogin, btnForgotPassword, btnDontHaveAccount, btnCustomerLoginBack;
    private MainFrame mainFrame;

    public CustomerLoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setLayout(null);
        setBackground(BG_LIGHT);

        GradientPanel leftBannerCustomer = new GradientPanel();
        leftBannerCustomer.setBounds(0, 0, 480, 720);
        JLabel logoCustomer = new JLabel("🐼", SwingConstants.CENTER);
        logoCustomer.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 140));
        logoCustomer.setBounds(20, 140, 440, 180);
        leftBannerCustomer.add(logoCustomer);
        JLabel titleCustBanner = new JLabel("GrabPanda", SwingConstants.CENTER);
        titleCustBanner.setFont(new Font("Segoe UI", Font.BOLD, 38));
        titleCustBanner.setForeground(YELLOW_ACCENT);
        titleCustBanner.setBounds(20, 330, 440, 45);
        leftBannerCustomer.add(titleCustBanner);
        JLabel subCustBanner = new JLabel("Fast & Fresh Food Delivery", SwingConstants.CENTER);
        subCustBanner.setFont(new Font("Segoe UI", Font.BOLD, 16));
        subCustBanner.setForeground(Color.WHITE);
        subCustBanner.setBounds(20, 380, 440, 30);
        leftBannerCustomer.add(subCustBanner);
        add(leftBannerCustomer);

        JPanel rightPanelCustomer = new JPanel(null);
        rightPanelCustomer.setBounds(480, 0, 544, 720);
        rightPanelCustomer.setBackground(BG_LIGHT);

        JLabel lblCustTitle = new JLabel("Customer Login", SwingConstants.LEFT);
        lblCustTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblCustTitle.setForeground(PURPLE_DARK);
        lblCustTitle.setBounds(60, 100, 400, 45);
        rightPanelCustomer.add(lblCustTitle);

        JLabel subCustTitle = new JLabel("Please enter your details to log in");
        subCustTitle.setFont(REGULAR_FONT);
        subCustTitle.setForeground(TEXT_DARK);
        subCustTitle.setBounds(60, 145, 400, 25);
        rightPanelCustomer.add(subCustTitle);

        JLabel lblCustUser = new JLabel("Email or Username");
        lblCustUser.setFont(REGULAR_FONT);
        lblCustUser.setBounds(60, 195, 380, 20);
        txtLoginEmailOrUser = new JTextField();
        txtLoginEmailOrUser.setBounds(60, 220, 380, 38);
        rightPanelCustomer.add(lblCustUser);
        rightPanelCustomer.add(txtLoginEmailOrUser);

        JLabel lblCustPass = new JLabel("Password");
        lblCustPass.setFont(REGULAR_FONT);
        lblCustPass.setBounds(60, 270, 380, 20);
        txtLoginPassword = new JPasswordField();
        JPanel passPanelCust = createPasswordFieldWithEye(txtLoginPassword);
        passPanelCust.setBounds(60, 295, 380, 38);
        rightPanelCustomer.add(lblCustPass);
        rightPanelCustomer.add(passPanelCust);

        chkLoginSavePassword = new JCheckBox("Save Password");
        chkLoginSavePassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkLoginSavePassword.setBackground(BG_LIGHT);
        chkLoginSavePassword.setBounds(60, 340, 150, 20);
        chkLoginSavePassword.setFocusPainted(false);
        rightPanelCustomer.add(chkLoginSavePassword);

        btnForgotPassword = new JButton("Forgot password?");
        btnForgotPassword.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnForgotPassword.setForeground(PURPLE_DARK);
        btnForgotPassword.setContentAreaFilled(false);
        btnForgotPassword.setBorderPainted(false);
        btnForgotPassword.setFocusPainted(false);
        btnForgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnForgotPassword.setBounds(300, 338, 140, 25);
        rightPanelCustomer.add(btnForgotPassword);

        btnCustomerLogin = new JButton("Login");
        btnCustomerLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnCustomerLogin.setBackground(PURPLE_DARK);
        btnCustomerLogin.setForeground(Color.WHITE);
        btnCustomerLogin.setFocusPainted(false);
        btnCustomerLogin.setBounds(60, 385, 380, 48);
        rightPanelCustomer.add(btnCustomerLogin);

        btnDontHaveAccount = new JButton("Don't have acc?");
        btnDontHaveAccount.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnDontHaveAccount.setForeground(PURPLE_DARK);
        btnDontHaveAccount.setContentAreaFilled(false);
        btnDontHaveAccount.setBorderPainted(false);
        btnDontHaveAccount.setFocusPainted(false);
        btnDontHaveAccount.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDontHaveAccount.setBounds(60, 445, 380, 30);
        rightPanelCustomer.add(btnDontHaveAccount);

        btnCustomerLoginBack = new JButton("← Go Back");
        btnCustomerLoginBack.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCustomerLoginBack.setForeground(PURPLE_DARK);
        btnCustomerLoginBack.setContentAreaFilled(false);
        btnCustomerLoginBack.setBorderPainted(false);
        btnCustomerLoginBack.setFocusPainted(false);
        btnCustomerLoginBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCustomerLoginBack.setBounds(50, 490, 120, 30);
        rightPanelCustomer.add(btnCustomerLoginBack);

        add(rightPanelCustomer);
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