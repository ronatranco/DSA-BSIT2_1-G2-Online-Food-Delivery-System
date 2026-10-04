/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MainFrame extends JFrame implements ActionListener {

    private JPanel mainContainer;
    private CardLayout cardLayout;

    private StartPanel panelStart;
    private AuthPanel panelAuth;
    private RoleSelectionPanel panelRoleSelection;
    private SignupPanel panelSignup;
    private CustomerLoginPanel panelCustomerLogin;
    private RestaurantLoginPanel panelRestaurantLogin;    
    private AdminLoginPanel panelAdminLogin;

    // Hardcoded Credentials para sa Demo/Testing
    private final String VALID_CUSTOMER_USER = "customer1";
    private final String VALID_CUSTOMER_PASS = "customer123";
    private final String VALID_RESTAURANT_USER = "restaurant1";
    private final String VALID_RESTAURANT_PASS = "restaurant123";
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
        panelRestaurantLogin = new RestaurantLoginPanel(this);        
        panelAdminLogin = new AdminLoginPanel(this);

        // Adding to cardLayout container (Inalis na ang duplicate ng panelRoleSelection)
        mainContainer.add(panelStart, "START");
        mainContainer.add(panelAuth, "AUTH");
        mainContainer.add(panelRoleSelection, "ROLE_SELECTION");
        mainContainer.add(panelSignup, "SIGNUP");
        mainContainer.add(panelCustomerLogin, "CUSTOMER_LOGIN");
        mainContainer.add(panelRestaurantLogin, "RESTAURANT_LOGIN");
        mainContainer.add(panelAdminLogin, "ADMIN_LOGIN");

        // Register ActionListeners
        registerActionListeners();

        cardLayout.show(mainContainer, "START");
    }

    private void registerActionListeners() {
        // Start Panel
        panelStart.btnStart.addActionListener(this);

        // Auth Panel
        panelAuth.btnNavLogin.addActionListener(this);
        panelAuth.btnNavRegister.addActionListener(this);

        // Role Selection Panel
        panelRoleSelection.btnRoleCustomer.addActionListener(this);
        panelRoleSelection.btnRoleRestaurant.addActionListener(this);
        panelRoleSelection.btnRoleAdmin.addActionListener(this);
        panelRoleSelection.btnRoleBack.addActionListener(this);

        // Signup Panel
        panelSignup.btnRegister.addActionListener(this);
        panelSignup.btnAlreadyHaveAccount.addActionListener(this);

        // Customer Login Panel
        panelCustomerLogin.btnCustomerLogin.addActionListener(this);
        panelCustomerLogin.btnForgotPassword.addActionListener(this);
        panelCustomerLogin.btnDontHaveAccount.addActionListener(this);
        panelCustomerLogin.btnCustomerLoginBack.addActionListener(this);

      // Restaurant Login Panel
        panelRestaurantLogin.btnRestaurantLogin.addActionListener(this);
        panelRestaurantLogin.btnRestaurantLoginBack.addActionListener(this);

        // Admin Login Panel
        panelAdminLogin.btnAdminLogin.addActionListener(this);
        panelAdminLogin.btnAdminLoginBack.addActionListener(this);
    }

    public void showCard(String cardName) {
        cardLayout.show(mainContainer, cardName);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        // 1. START & AUTH PANEL NAVIGATION
        if (source == panelStart.btnStart) {
            cardLayout.show(mainContainer, "AUTH");
        } 
        else if (source == panelAuth.btnNavLogin) {
            cardLayout.show(mainContainer, "ROLE_SELECTION");
        } 
        else if (source == panelAuth.btnNavRegister) {
            cardLayout.show(mainContainer, "SIGNUP");
        }

        // 2. ROLE SELECTION NAVIGATION
        else if (source == panelRoleSelection.btnRoleCustomer) {
            cardLayout.show(mainContainer, "CUSTOMER_LOGIN");
        } 
        else if (source == panelRoleSelection.btnRoleRestaurant) {
            cardLayout.show(mainContainer, "RESTAURANT_LOGIN");
        } 
        else if (source == panelRoleSelection.btnRoleAdmin) {
            cardLayout.show(mainContainer, "ADMIN_LOGIN");
        } 
        else if (source == panelRoleSelection.btnRoleBack) {
            cardLayout.show(mainContainer, "AUTH");
        }

        // 3. CROSS-NAVIGATION
        else if (source == panelSignup.btnAlreadyHaveAccount) {
            cardLayout.show(mainContainer, "ROLE_SELECTION");
        }
        else if (source == panelCustomerLogin.btnDontHaveAccount) {
            cardLayout.show(mainContainer, "SIGNUP");
        }

        // 4. GO BACK BUTTONS
        else if (source == panelCustomerLogin.btnCustomerLoginBack || 
                 source == panelRestaurantLogin.btnRestaurantLoginBack || 
                 source == panelAdminLogin.btnAdminLoginBack) {
            cardLayout.show(mainContainer, "ROLE_SELECTION");
        }

        // 5. FORGOT PASSWORD
        else if (source == panelCustomerLogin.btnForgotPassword) {
            panelCustomerLogin.txtLoginEmailOrUser.setText("");
            panelCustomerLogin.txtLoginPassword.setText("");
            if (panelCustomerLogin.chkLoginSavePassword != null) {
                panelCustomerLogin.chkLoginSavePassword.setSelected(false);
            }
            JOptionPane.showMessageDialog(this, "Text fields have been reset. Please enter your credentials again.", "Forgot Password", JOptionPane.INFORMATION_MESSAGE);
        }

        // 6. CUSTOMER LOGIN
        else if (source == panelCustomerLogin.btnCustomerLogin) {
            String identifier = panelCustomerLogin.txtLoginEmailOrUser.getText().trim();
            String password = new String(panelCustomerLogin.txtLoginPassword.getPassword());

            if (identifier.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please input a valid email/username and password!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (identifier.equals(VALID_CUSTOMER_USER) && password.equals(VALID_CUSTOMER_PASS)) {
                String saveMsg = (panelCustomerLogin.chkLoginSavePassword != null && panelCustomerLogin.chkLoginSavePassword.isSelected()) ? "\n(Password saved locally)" : "";
                JOptionPane.showMessageDialog(this, "Login Successful! Welcome Customer." + saveMsg, "Success", JOptionPane.INFORMATION_MESSAGE);

                panelCustomerLogin.txtLoginEmailOrUser.setText("");
                panelCustomerLogin.txtLoginPassword.setText("");
                if (panelCustomerLogin.chkLoginSavePassword != null) {
                    panelCustomerLogin.chkLoginSavePassword.setSelected(false);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Email/Username or Password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }

        // 7. RESTAURANT LOGIN
        else if (source == panelRestaurantLogin.btnRestaurantLogin) {
            String identifier = panelRestaurantLogin.txtRestaurantUser.getText().trim();
            String password = new String(panelRestaurantLogin.txtRestaurantPass.getPassword());

            if (identifier.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please input a valid username and password!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (identifier.equals(VALID_RESTAURANT_USER) && password.equals(VALID_RESTAURANT_PASS)) {
                String saveMsg = (panelRestaurantLogin.chkRestaurantSavePass != null && panelRestaurantLogin.chkRestaurantSavePass.isSelected()) ? "\n(Password saved locally)" : "";
                JOptionPane.showMessageDialog(this, "Login Successful! Welcome Restaurant." + saveMsg, "Success", JOptionPane.INFORMATION_MESSAGE);
                panelRestaurantLogin.txtRestaurantUser.setText("");
                panelRestaurantLogin.txtRestaurantPass.setText("");
                if (panelRestaurantLogin.chkRestaurantSavePass != null) {
                    panelRestaurantLogin.chkRestaurantSavePass.setSelected(false);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }

        // 8. ADMIN LOGIN
        else if (source == panelAdminLogin.btnAdminLogin) {
            String identifier = panelAdminLogin.txtAdminUser.getText().trim();
            String password = new String(panelAdminLogin.txtAdminPass.getPassword());

            if (identifier.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please input a valid username and password!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (identifier.equals(VALID_ADMIN_USER) && password.equals(VALID_ADMIN_PASS)) {
                String saveMsg = (panelAdminLogin.chkAdminSavePass != null && panelAdminLogin.chkAdminSavePass.isSelected()) ? "\n(Password saved locally)" : "";
                JOptionPane.showMessageDialog(this, "Login Successful! Welcome Admin." + saveMsg, "Success", JOptionPane.INFORMATION_MESSAGE);
                panelAdminLogin.txtAdminUser.setText("");
                panelAdminLogin.txtAdminPass.setText("");
                if (panelAdminLogin.chkAdminSavePass != null) {
                    panelAdminLogin.chkAdminSavePass.setSelected(false);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }

        // 9. REGISTRATION VALIDATION
        else if (source == panelSignup.btnRegister) {
            String name = panelSignup.txtName.getText().trim();
            String email = panelSignup.txtEmail.getText().trim();
            String phone = panelSignup.txtPhone.getText().trim();
            String password = new String(panelSignup.txtPassword.getPassword());
            String confirmPassword = new String(panelSignup.txtConfirmPassword.getPassword());

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields before registering!", "Registration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!email.toLowerCase().endsWith("@gmail.com")) {
                JOptionPane.showMessageDialog(this, "Email must be a valid @gmail.com address!", "Invalid Email", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!phone.matches("^\\d{11}$")) {
                JOptionPane.showMessageDialog(this, "Phone number must contain numbers only and exactly 11 digits!", "Invalid Phone Number", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match!", "Password Mismatch", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String savedMsg = (panelSignup.chkSavePassword != null && panelSignup.chkSavePassword.isSelected()) ? "\n(Password saved locally)" : "";
            JOptionPane.showMessageDialog(this, "Account successfully created!" + savedMsg, "Success", JOptionPane.INFORMATION_MESSAGE);

            panelSignup.txtName.setText("");
            panelSignup.txtEmail.setText("");
            panelSignup.txtPhone.setText("");
            panelSignup.txtPassword.setText("");
            panelSignup.txtConfirmPassword.setText("");
            if (panelSignup.chkSavePassword != null) {
                panelSignup.chkSavePassword.setSelected(false);
            }

            cardLayout.show(mainContainer, "ROLE_SELECTION");
        }
    }

    

}