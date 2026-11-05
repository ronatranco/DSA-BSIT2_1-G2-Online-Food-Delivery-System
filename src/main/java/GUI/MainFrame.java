/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package GUI;

import Database.databaseConnection;
import Database.UserAuthentication;
import Database.AccountDAO;
import Model.Admin;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.*;

/**
 * @author rownavanana and calalang
 * CLASS FOR LOGICS AND CARDLAYOUT NAVIGATION
 */
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
    private AdminDashboard panelAdminGUI;
    
    private CustomerDashboard panelCustomerDashboard;
    private RestaurantDashboard panelRestaurantDashboard;
    private RestaurantDashboardMilkTea panelMilkTeaDashboard;
    private RestaurantDashboardMangPinaasa panelPinaasaDashboard;
    private final UserAuthentication userAuth;
    private Admin adminLogic;

    public MainFrame() {
        userAuth = new UserAuthentication();
        setTitle("GrabPanda Food Delivery");
        setSize(1024, 720);

        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        adminLogic = new Admin();

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        add(mainContainer, BorderLayout.CENTER);

        panelStart = new StartPanel(this);
        panelAuth = new AuthPanel(this);
        panelRoleSelection = new RoleSelectionPanel(this);
        panelSignup = new SignupPanel(this);
        panelCustomerLogin = new CustomerLoginPanel(this);
        panelRestaurantLogin = new RestaurantLoginPanel(this);
        panelAdminLogin = new AdminLoginPanel(this);
        panelAdminGUI = new AdminDashboard(this);

        panelRestaurantDashboard = new RestaurantDashboard(this);
        panelCustomerDashboard = new CustomerDashboard(this);
        panelMilkTeaDashboard = new RestaurantDashboardMilkTea(this);
        panelPinaasaDashboard = new RestaurantDashboardMangPinaasa(this);
        
        mainContainer.add(panelStart, "START");
        mainContainer.add(panelAuth, "AUTH");
        mainContainer.add(panelRoleSelection, "ROLE_SELECTION");
        mainContainer.add(panelSignup, "SIGNUP");
        mainContainer.add(panelCustomerLogin, "CUSTOMER_LOGIN");
        mainContainer.add(panelRestaurantLogin, "RESTAURANT_LOGIN");
        mainContainer.add(panelAdminLogin, "ADMIN_LOGIN");
        mainContainer.add(panelAdminGUI, "ADMIN_DASHBOARD");

        mainContainer.add(panelCustomerDashboard, "CUSTOMER_DASHBOARD");
        mainContainer.add(panelRestaurantDashboard, "RESTAURANT_DASHBOARD");
        mainContainer.add(panelMilkTeaDashboard, "RESTAURANT_DASHBOARD_MILKTEA");
        mainContainer.add(panelPinaasaDashboard, "RESTAURANT_DASHBOARD_PINAASA");
        registerActionListeners();

        showCard("START");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
    
    private void registerActionListeners() {
        panelStart.btnStart.addActionListener(this);
        
        panelAuth.btnNavLogin.addActionListener(this);
        panelAuth.btnNavRegister.addActionListener(this);

        panelRoleSelection.btnRoleCustomer.addActionListener(this);
        panelRoleSelection.btnRoleRestaurant.addActionListener(this);
        panelRoleSelection.btnRoleAdmin.addActionListener(this);
        panelRoleSelection.btnRoleBack.addActionListener(this);

        panelSignup.btnRegister.addActionListener(this);
        panelSignup.btnAlreadyHaveAccount.addActionListener(this);

        panelCustomerLogin.btnCustomerLogin.addActionListener(this);
        panelCustomerLogin.btnForgotPassword.addActionListener(this);
        panelCustomerLogin.btnDontHaveAccount.addActionListener(this);
        panelCustomerLogin.btnCustomerLoginBack.addActionListener(this);

        panelRestaurantLogin.btnRestaurantLogin.addActionListener(this);
        panelRestaurantLogin.btnRestaurantForgotPassword.addActionListener(this);
        panelRestaurantLogin.btnRestaurantLoginBack.addActionListener(this);
        panelRestaurantLogin.btnDontHaveAccount.addActionListener(this);

        panelAdminLogin.btnAdminLogin.addActionListener(this);
        panelAdminLogin.btnAdminForgotPassword.addActionListener(this);
        panelAdminLogin.btnAdminLoginBack.addActionListener(this);
        panelAdminLogin.btnDontHaveAccount.addActionListener(this);

        if (panelAdminGUI.getBtnLogout() != null) {
            panelAdminGUI.getBtnLogout().addActionListener(this);
        }

        if (panelCustomerDashboard.getBtnLogout() != null) {
            panelCustomerDashboard.getBtnLogout().addActionListener(this);
        }
        if (panelRestaurantDashboard.getBtnLogout() != null) {
            panelRestaurantDashboard.getBtnLogout().addActionListener(this);
        }
        if (panelMilkTeaDashboard.getBtnLogout() != null) {
            panelMilkTeaDashboard.getBtnLogout().addActionListener(this);
        }
        if (panelPinaasaDashboard.getBtnLogout() != null) {
            panelPinaasaDashboard.getBtnLogout().addActionListener(this);
        }
    }

    public void showCard(String cardName) {
        cardLayout.show(mainContainer, cardName);
        mainContainer.revalidate();
        mainContainer.repaint();
        this.revalidate();
        this.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        // 1. START & AUTH PANEL NAVIGATION
        if (source == panelStart.btnStart) {
            showCard("AUTH");
        }
        else if (source == panelAuth.btnNavLogin) {
            showCard("ROLE_SELECTION");
        }
        else if (source == panelAuth.btnNavRegister) {
            showCard("SIGNUP");
        }

        // 2. ROLE SELECTION NAVIGATION
        else if (source == panelRoleSelection.btnRoleCustomer) {
            showCard("CUSTOMER_LOGIN");
        }
        else if (source == panelRoleSelection.btnRoleRestaurant) {
            showCard("RESTAURANT_LOGIN");
        }
        else if (source == panelRoleSelection.btnRoleAdmin) {
            showCard("ADMIN_LOGIN");
        }
        else if (source == panelRoleSelection.btnRoleBack) {
            showCard("AUTH");
        }

        // 3. CROSS-NAVIGATION & SIGNUP
        else if (source == panelSignup.btnRegister) {
            registerUser();
        }
        else if (source == panelSignup.btnAlreadyHaveAccount) {
            showCard("ROLE_SELECTION");
        }
        else if (source == panelCustomerLogin.btnDontHaveAccount) {
            showCard("SIGNUP");
        }
        else if (source == panelRestaurantLogin.btnDontHaveAccount){
            showCard("SIGNUP");
        }
        else if (source == panelAdminLogin.btnDontHaveAccount){
            showCard("SIGNUP");
        }

        // 4. GO BACK BUTTONS
        else if (source == panelCustomerLogin.btnCustomerLoginBack ||
                 source == panelRestaurantLogin.btnRestaurantLoginBack ||
                 source == panelAdminLogin.btnAdminLoginBack) {
            showCard("ROLE_SELECTION");
        }

        // 5. FORGOT PASSWORD - CUSTOMER
        else if (source == panelCustomerLogin.btnForgotPassword) {
            String user = panelCustomerLogin.txtLoginEmailOrUser.getText().trim();
            String pass = new String(panelCustomerLogin.txtLoginPassword.getPassword());

            if (user.isEmpty() && pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter valid input", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                panelCustomerLogin.txtLoginEmailOrUser.setText("");
                panelCustomerLogin.txtLoginPassword.setText("");
                if (panelCustomerLogin.chkLoginSavePassword != null) {
                    panelCustomerLogin.chkLoginSavePassword.setSelected(false);
                }
                JOptionPane.showMessageDialog(this, "Fields have been cleared. Please enter your credentials again.", "Forgot Password", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        // 6. FORGOT PASSWORD - RESTAURANT
        else if (source == panelRestaurantLogin.btnRestaurantForgotPassword) {
            String user = panelRestaurantLogin.txtRestaurantUser.getText().trim();
            String pass = new String(panelRestaurantLogin.txtRestaurantPass.getPassword());

            if (user.isEmpty() && pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter valid input", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                panelRestaurantLogin.txtRestaurantUser.setText("");
                panelRestaurantLogin.txtRestaurantPass.setText("");
                if (panelRestaurantLogin.chkRestaurantSavePass != null) {
                    panelRestaurantLogin.chkRestaurantSavePass.setSelected(false);
                }
                JOptionPane.showMessageDialog(this, "Fields have been cleared. Please enter your credentials again.", "Forgot Password", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        // 7. FORGOT PASSWORD - ADMIN
        else if (source == panelAdminLogin.btnAdminForgotPassword) {
            String user = panelAdminLogin.txtAdminUser.getText().trim();
            String pass = new String(panelAdminLogin.txtAdminPass.getPassword());

            if (user.isEmpty() && pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter valid input", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                panelAdminLogin.txtAdminUser.setText("");
                panelAdminLogin.txtAdminPass.setText("");
                if (panelAdminLogin.chkAdminSavePass != null) {
                    panelAdminLogin.chkAdminSavePass.setSelected(false);
                }
                JOptionPane.showMessageDialog(this, "Fields have been cleared. Please enter your credentials again.", "Forgot Password", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        // 8. CUSTOMER LOGIN (checks the database)
        else if (source == panelCustomerLogin.btnCustomerLogin) {
            String identifier = panelCustomerLogin.txtLoginEmailOrUser.getText().trim();
            String password = new String(panelCustomerLogin.txtLoginPassword.getPassword());

            if (authenticate(identifier, password, "customer")) {
                AccountDAO.setLogin(identifier); 
                panelCustomerLogin.txtLoginEmailOrUser.setText("");
                panelCustomerLogin.txtLoginPassword.setText("");
                if (panelCustomerLogin.chkLoginSavePassword != null) {
                    panelCustomerLogin.chkLoginSavePassword.setSelected(false);
            }   mainContainer.remove(panelCustomerDashboard);
                panelCustomerDashboard = new CustomerDashboard(this);
                Model.Customer freshController = new Model.Customer(panelCustomerDashboard);
                mainContainer.add(panelCustomerDashboard, "CUSTOMER_DASHBOARD");
                mainContainer.revalidate();
                mainContainer.repaint();
                showCard("CUSTOMER_DASHBOARD");
            }
        }

        // 9. RESTAURANT LOGIN (checks the database)
        else if (source == panelRestaurantLogin.btnRestaurantLogin) {
            String identifier = panelRestaurantLogin.txtRestaurantUser.getText().trim();
            String password = new String(panelRestaurantLogin.txtRestaurantPass.getPassword());

            if (authenticate(identifier, password, "restaurant")) {
                AccountDAO.setLogin(identifier);

                panelRestaurantLogin.txtRestaurantUser.setText("");
                panelRestaurantLogin.txtRestaurantPass.setText("");
                if (panelRestaurantLogin.chkRestaurantSavePass != null) {
                    panelRestaurantLogin.chkRestaurantSavePass.setSelected(false);
                }

                String account = identifier.toLowerCase().replace(" ", "").replace("_", "");
                String card = "RESTAURANT_DASHBOARD";
                Model.Restaurant.Brand brand = Model.Restaurant.Brand.MCDOLLIBEE;   
                if (account.contains("braincell") || account.contains("milktea")) {
                    card = "RESTAURANT_DASHBOARD_MILKTEA";
                    brand = Model.Restaurant.Brand.BRAIN_CELLS_MILK_TEA;            
                } else if (account.contains("pinaasa")) {
                    card = "RESTAURANT_DASHBOARD_PINAASA";
                    brand = Model.Restaurant.Brand.MANG_PINAASA;                    
                }

                Model.Restaurant.get(brand).loadFromAccount(identifier);            
                showCard(card);
            }
        }

        // 10. ADMIN LOGIN (CHECKS THE DATABASE)
        else if (source == panelAdminLogin.btnAdminLogin) {
            String username = panelAdminLogin.txtAdminUser.getText().trim();
            String password = new String(panelAdminLogin.txtAdminPass.getPassword());

            if (authenticate(username, password, "admin")) {
                panelAdminLogin.txtAdminUser.setText("");
                panelAdminLogin.txtAdminPass.setText("");
                if (panelAdminLogin.chkAdminSavePass != null) {
                    panelAdminLogin.chkAdminSavePass.setSelected(false);
                }
                showCard("ADMIN_DASHBOARD");
            }
        }

        // 11. LOGOUT ACTIONS FROM ANY DASHBOARD (RETURNS TO ROLE SELECTION)
        else if ((panelAdminGUI.getBtnLogout() != null && source == panelAdminGUI.getBtnLogout()) ||
                 (panelCustomerDashboard.getBtnLogout() != null && source == panelCustomerDashboard.getBtnLogout()) ||
                 (panelRestaurantDashboard.getBtnLogout() != null && source == panelRestaurantDashboard.getBtnLogout()) ||
                 (panelMilkTeaDashboard.getBtnLogout() != null && source == panelMilkTeaDashboard.getBtnLogout()) ||
                 (panelPinaasaDashboard.getBtnLogout() != null && source == panelPinaasaDashboard.getBtnLogout())) {
            AccountDAO.clear();
            showCard("ROLE_SELECTION");
        }
    }

    private boolean authenticate(String login, String password, String role) {
        if (login.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Invalid Username or Password! \nIf you don't have an account, please create one.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            if (userAuth.authenticate(login, password, role)) {
                return true;
            }// Added error message
            JOptionPane.showMessageDialog(this, "Invalid Username or Password! \nIf you don't have an account, please create one.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
        return false;
    }

    // CHECKS SIGNUP FORM & INSTERTS NEW USER INTO THE DATABASE
    private void registerUser() {
        String name     = panelSignup.txtName.getText().trim();
        String username = panelSignup.txtUsername.getText().trim();
        String email    = panelSignup.txtEmail.getText().trim();
        String phone    = panelSignup.txtPhone.getText().trim();
        String address  = panelSignup.txtAddress.getText().trim();
        String role     = panelSignup.cmbRole.getSelectedItem().toString().toUpperCase();
        String password = new String(panelSignup.txtPassword.getPassword());
        String confirm  = new String(panelSignup.txtConfirmPassword.getPassword());

        if (name.isEmpty() || username.isEmpty() || email.isEmpty()
                || phone.isEmpty() || address.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }
        if (!email.toLowerCase().endsWith("@gmail.com")) {
            JOptionPane.showMessageDialog(this, "Email must be a @gmail.com address.");
            return;
        }
        if (!phone.matches("\\d{11}")) {
            JOptionPane.showMessageDialog(this, "Phone number must be 11 digits.");
            return;
        }
        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.");
            return;
        }

        String sql = "INSERT INTO accounts (full_name, username, email, phone, address, password_hash, role) "
           + "VALUES (?, ?, ?, ?, ?, SHA2(?, 256), ?)";

        try (Connection con = databaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, username);
            ps.setString(3, email);
            ps.setString(4, phone);
            ps.setString(5, address);
            ps.setString(6, password);
            ps.setString(7, role);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Account created!");
            panelSignup.clearFields();
            showCard("ROLE_SELECTION");

        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1062) {
                JOptionPane.showMessageDialog(this, "Username or email already exists.");
            } else {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        }
    }
}