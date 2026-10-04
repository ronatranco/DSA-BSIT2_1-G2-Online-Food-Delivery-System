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
public class SignupPanel extends JPanel {
    private Color PURPLE_DARK = new Color(74, 20, 140);
    private Color YELLOW_ACCENT = new Color(255, 193, 7);
    private Color BG_LIGHT = new Color(248, 245, 250);

    public JTextField txtName, txtUsername, txtEmail, txtPhone, txtAddress;
    public JPasswordField txtPassword, txtConfirmPassword;
    public JCheckBox chkSavePassword;
    public JButton btnRegister, btnAlreadyHaveAccount;
    private MainFrame mainFrame;

    public SignupPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setLayout(null);
        setBackground(BG_LIGHT);

        GradientPanel leftBannerSignup = new GradientPanel();
        leftBannerSignup.setBounds(0, 0, 480, 720);
        JLabel logoSignup = new JLabel("🐼", SwingConstants.CENTER);
        logoSignup.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 140));
        logoSignup.setBounds(20, 140, 440, 180);
        leftBannerSignup.add(logoSignup);
        JLabel titleSignupBanner = new JLabel("GrabPanda", SwingConstants.CENTER);
        titleSignupBanner.setFont(new Font("Segoe UI", Font.BOLD, 38));
        titleSignupBanner.setForeground(YELLOW_ACCENT);
        titleSignupBanner.setBounds(20, 330, 440, 45);
        leftBannerSignup.add(titleSignupBanner);
        JLabel subSignupBanner = new JLabel("Fast & Fresh Food Delivery", SwingConstants.CENTER);
        subSignupBanner.setFont(new Font("Segoe UI", Font.BOLD, 16));
        subSignupBanner.setForeground(Color.WHITE);
        subSignupBanner.setBounds(20, 380, 440, 30);
        leftBannerSignup.add(subSignupBanner);
        add(leftBannerSignup);

        JPanel rightPanelSignup = new JPanel(null);
        rightPanelSignup.setBounds(480, 0, 544, 720);
        rightPanelSignup.setBackground(BG_LIGHT);

        JLabel lblSignupTitle = new JLabel("Create Account", SwingConstants.LEFT);
        lblSignupTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblSignupTitle.setForeground(PURPLE_DARK);
        lblSignupTitle.setBounds(60, 20, 400, 40);
        rightPanelSignup.add(lblSignupTitle);

        // 1. Full Name
        JLabel lblName = new JLabel("Full Name");
        lblName.setBounds(60, 68, 380, 18);
        txtName = new JTextField();
        txtName.setBounds(60, 88, 380, 30);
        rightPanelSignup.add(lblName);
        rightPanelSignup.add(txtName);

        // 2. Username 
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setBounds(60, 122, 380, 18);
        txtUsername = new JTextField();
        txtUsername.setBounds(60, 142, 380, 30);
        rightPanelSignup.add(lblUsername);
        rightPanelSignup.add(txtUsername);

        // 3. Email Address
        JLabel lblEmail = new JLabel("Email Address (@gmail.com)");
        lblEmail.setBounds(60, 176, 380, 18);
        txtEmail = new JTextField();
        txtEmail.setBounds(60, 196, 380, 30);
        rightPanelSignup.add(lblEmail);
        rightPanelSignup.add(txtEmail);

        // 4. Phone Number
        JLabel lblPhone = new JLabel("Phone Number (11 digits, e.g., 09123456789)");
        lblPhone.setBounds(60, 230, 380, 18);
        txtPhone = new JTextField();
        txtPhone.setBounds(60, 250, 380, 30);
        rightPanelSignup.add(lblPhone);
        rightPanelSignup.add(txtPhone);

        // 5. Address
        JLabel lblAddress = new JLabel("Address");
        lblAddress.setBounds(60, 284, 380, 18);
        txtAddress = new JTextField();         
        txtAddress.setBounds(60, 304, 380, 30);   
        rightPanelSignup.add(lblAddress);
        rightPanelSignup.add(txtAddress);

        // 6. Password
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setBounds(60, 338, 380, 18);
        txtPassword = new JPasswordField();
        JPanel passPanelSignup = createPasswordFieldWithEye(txtPassword);
        passPanelSignup.setBounds(60, 358, 380, 30);
        rightPanelSignup.add(lblPassword);
        rightPanelSignup.add(passPanelSignup);

        // 7. Confirm Password
        JLabel lblConfirmPassword = new JLabel("Confirm Password");
        lblConfirmPassword.setBounds(60, 392, 380, 18);
        txtConfirmPassword = new JPasswordField();
        JPanel confirmPassPanelSignup = createPasswordFieldWithEye(txtConfirmPassword);
        confirmPassPanelSignup.setBounds(60, 412, 380, 30);
        rightPanelSignup.add(lblConfirmPassword);
        rightPanelSignup.add(confirmPassPanelSignup);

        // 8. Save Password Checkbox
        chkSavePassword = new JCheckBox("Save Password");
        chkSavePassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkSavePassword.setBackground(BG_LIGHT);
        chkSavePassword.setBounds(60, 448, 150, 20);
        chkSavePassword.setFocusPainted(false);
        rightPanelSignup.add(chkSavePassword);

        // 9. Register Button
        btnRegister = new JButton("REGISTER");
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegister.setBackground(PURPLE_DARK);
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setBounds(60, 480, 380, 42);
        rightPanelSignup.add(btnRegister);

        // 10. Already have an account Button
        btnAlreadyHaveAccount = new JButton("Already have an account?");
        btnAlreadyHaveAccount.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAlreadyHaveAccount.setForeground(PURPLE_DARK);
        btnAlreadyHaveAccount.setContentAreaFilled(false);
        btnAlreadyHaveAccount.setBorderPainted(false);
        btnAlreadyHaveAccount.setFocusPainted(false);
        btnAlreadyHaveAccount.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAlreadyHaveAccount.setBounds(60, 530, 380, 30);
        rightPanelSignup.add(btnAlreadyHaveAccount);

        add(rightPanelSignup);
    }

    private JPanel createPasswordFieldWithEye(JPasswordField passField) {
        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.setBackground(Color.WHITE);
        fieldPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        passField.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        passField.setBackground(Color.WHITE);

        JToggleButton btnEye = new JToggleButton("👁");
        btnEye.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 16));
        btnEye.setPreferredSize(new Dimension(45, 30));
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