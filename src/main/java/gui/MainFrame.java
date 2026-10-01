/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import Customer.CustomerGUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
/**
 *
 * @author rownavanana
 */

public class MainFrame extends JFrame implements ActionListener {

    private JPanel mainContainer;
    private CardLayout cardLayout;

    private StartPanel panelStart;
    private AuthPanel panelAuth;
    private RoleSelectionPanel panelRoleSelection;
    private SignupPanel panelSignup;
    private CustomerLoginPanel panelCustomerLogin;
    private DriverLoginPanel panelDriverLogin;
    private AdminLoginPanel panelAdminLogin;
    private CustomerGUI panelCustomerGUI;

    // Credentials
    private final String VALID_CUSTOMER_USER = "customer1";
    private final String VALID_CUSTOMER_PASS = "customer123";
    private final String VALID_DRIVER_USER = "driver1";
    private final String VALID_DRIVER_PASS = "driver123";
    private final String VALID_ADMIN_USER = "admin1";
    private final String VALID_ADMIN_PASS = "admin123";

    public MainFrame() {
        setTitle("GrabPanda Food Delivery");
        setSize(1024, 720);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        mainContainer.setBounds(0, 0, 1024, 720);
        add(mainContainer);

        // Instantiating panels
        panelStart = new StartPanel(this);
        panelAuth = new AuthPanel(this);
        panelRoleSelection = new RoleSelectionPanel(this);
        panelSignup = new SignupPanel(this);
        panelCustomerLogin = new CustomerLoginPanel(this);
        panelDriverLogin = new DriverLoginPanel(this);
        panelAdminLogin = new AdminLoginPanel(this);
        panelCustomerGUI = new CustomerGUI(this);

        // Adding to cardLayout container
        mainContainer.add(panelStart, "START");
        mainContainer.add(panelAuth, "AUTH");
        mainContainer.add(panelRoleSelection, "ROLES");
        mainContainer.add(panelSignup, "SIGNUP");
        mainContainer.add(panelCustomerLogin, "CUSTOMER_LOGIN");
        mainContainer.add(panelDriverLogin, "DRIVER_LOGIN");
        mainContainer.add(panelAdminLogin, "ADMIN_LOGIN");
        mainContainer.add(panelCustomerGUI, "CUSTOMER_HOME");

        cardLayout.show(mainContainer, "START");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == panelStart.btnStart) {
            cardLayout.show(mainContainer, "AUTH");
        } 
        else if (source == panelAuth.btnNavLogin || source == panelSignup.btnAlreadyHaveAccount) {
            cardLayout.show(mainContainer, "ROLES");
        } 
        else if (source == panelAuth.btnNavRegister || source == panelCustomerLogin.btnDontHaveAccount) {
            cardLayout.show(mainContainer, "SIGNUP");
        }
        else if (source == panelSignup.btnRegister) {
            String name = panelSignup.txtName.getText().trim();
            String email = panelSignup.txtEmail.getText().trim();
            String phone = panelSignup.txtPhone.getText().trim();
            String password = new String(panelSignup.txtPassword.getPassword());
            String confirmPassword = new String(panelSignup.txtConfirmPassword.getPassword());

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!email.toLowerCase().endsWith("@gmail.com")) {
                JOptionPane.showMessageDialog(this, "Email must be a valid @gmail.com address!", "Invalid Email", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!phone.matches("^\\d{11}$")) {
                JOptionPane.showMessageDialog(this, "Phone number must be exactly 11 digits!", "Invalid Phone Number", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match!", "Password Mismatch", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String savedMsg = panelSignup.chkSavePassword.isSelected() ? "\n(Password saved locally)" : "";
            JOptionPane.showMessageDialog(this, "Account successfully created!" + savedMsg, "Success", JOptionPane.INFORMATION_MESSAGE);
            
            panelSignup.txtName.setText("");
            panelSignup.txtEmail.setText("");
            panelSignup.txtPhone.setText("");
            panelSignup.txtPassword.setText("");
            panelSignup.txtConfirmPassword.setText("");
            panelSignup.chkSavePassword.setSelected(false);

            cardLayout.show(mainContainer, "ROLES");
        }
        else if (source == panelRoleSelection.btnRoleCustomer) {
            cardLayout.show(mainContainer, "CUSTOMER_LOGIN");
        } 
        else if (source == panelRoleSelection.btnRoleDriver) {
            cardLayout.show(mainContainer, "DRIVER_LOGIN");
        } 
        else if (source == panelRoleSelection.btnRoleAdmin) {
            cardLayout.show(mainContainer, "ADMIN_LOGIN");
        }
        else if (source == panelRoleSelection.btnRoleBack) {
            cardLayout.show(mainContainer, "AUTH");
        }
        else if (source == panelCustomerLogin.btnCustomerLogin) {
            String identifier = panelCustomerLogin.txtLoginEmailOrUser.getText().trim();
            String password = new String(panelCustomerLogin.txtLoginPassword.getPassword());

            if (identifier.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (identifier.equals(VALID_CUSTOMER_USER) && password.equals(VALID_CUSTOMER_PASS)) {
                String saveMsg = panelCustomerLogin.chkLoginSavePassword.isSelected() ? "\n(Password saved locally)" : "";
                JOptionPane.showMessageDialog(this, "Login Successful! Welcome Customer." + saveMsg, "Success", JOptionPane.INFORMATION_MESSAGE);
                
                panelCustomerLogin.txtLoginEmailOrUser.setText("");
                panelCustomerLogin.txtLoginPassword.setText("");
                panelCustomerLogin.chkLoginSavePassword.setSelected(false);

                // Switch to Customer Home panel
                cardLayout.show(mainContainer, "CUSTOMER_HOME");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Email/Username or Password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if (source == panelDriverLogin.btnDriverLogin) {
            String identifier = panelDriverLogin.txtDriverUser.getText().trim();
            String password = new String(panelDriverLogin.txtDriverPass.getPassword());

            if (identifier.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (identifier.equals(VALID_DRIVER_USER) && password.equals(VALID_DRIVER_PASS)) {
                String saveMsg = panelDriverLogin.chkDriverSavePass.isSelected() ? "\n(Password saved locally)" : "";
                JOptionPane.showMessageDialog(this, "Login Successful! Welcome Driver." + saveMsg, "Success", JOptionPane.INFORMATION_MESSAGE);
                panelDriverLogin.txtDriverUser.setText("");
                panelDriverLogin.txtDriverPass.setText("");
                panelDriverLogin.chkDriverSavePass.setSelected(false);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if (source == panelAdminLogin.btnAdminLogin) {
            String identifier = panelAdminLogin.txtAdminUser.getText().trim();
            String password = new String(panelAdminLogin.txtAdminPass.getPassword());

            if (identifier.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (identifier.equals(VALID_ADMIN_USER) && password.equals(VALID_ADMIN_PASS)) {
                String saveMsg = panelAdminLogin.chkAdminSavePass.isSelected() ? "\n(Password saved locally)" : "";
                JOptionPane.showMessageDialog(this, "Login Successful! Welcome Admin." + saveMsg, "Success", JOptionPane.INFORMATION_MESSAGE);
                panelAdminLogin.txtAdminUser.setText("");
                panelAdminLogin.txtAdminPass.setText("");
                panelAdminLogin.chkAdminSavePass.setSelected(false);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if (source == panelCustomerLogin.btnForgotPassword) {
            JOptionPane.showMessageDialog(this, "Password reset link sent!", "Forgot Password", JOptionPane.INFORMATION_MESSAGE);
        }
        else if (source == panelCustomerLogin.btnCustomerLoginBack || 
                 source == panelDriverLogin.btnDriverLoginBack || 
                 source == panelAdminLogin.btnAdminLoginBack) {
            cardLayout.show(mainContainer, "ROLES");
        }
        else if (source == panelCustomerGUI.btnNavProfile) {
            int choice = JOptionPane.showConfirmDialog(this, "Do you want to log out?", "Logout", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                cardLayout.show(mainContainer, "ROLES");
            }
        }
    }
}