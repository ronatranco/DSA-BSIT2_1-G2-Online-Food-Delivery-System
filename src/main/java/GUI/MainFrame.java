package GUI;

import Model.Admin;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
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
    private AdminPanelGUI panelAdminGUI;
    
    // DASHBOARD PANELS
    private CustomerDashboard panelCustomerDashboard;
    private RestaurantDashboard panelRestaurantDashboard;

    private Admin adminLogic;

    private final String VALID_CUSTOMER_USER = "customer1";
    private final String VALID_CUSTOMER_PASS = "customer123";
    private final String VALID_RESTAURANT_USER = "restaurant1";
    private final String VALID_RESTAURANT_PASS = "restaurant123";
    private final String VALID_ADMIN_USER = "admin1";
    private final String VALID_ADMIN_PASS = "admin123";

    public MainFrame() {
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

        // Instantiate existing login/auth panels
        panelStart = new StartPanel(this);
        panelAuth = new AuthPanel(this);
        panelRoleSelection = new RoleSelectionPanel(this);
        panelSignup = new SignupPanel(this);
        panelCustomerLogin = new CustomerLoginPanel(this);
        panelRestaurantLogin = new RestaurantLoginPanel(this);        
        panelAdminLogin = new AdminLoginPanel(this);
        panelAdminGUI = new AdminPanelGUI(this);

        // Instantiate new dashboard panels (passing 'this' MainFrame instance)
        panelCustomerDashboard = new CustomerDashboard(this);
        panelRestaurantDashboard = new RestaurantDashboard(this);

        // Register panels as cards in CardLayout
        mainContainer.add(panelStart, "START");
        mainContainer.add(panelAuth, "AUTH");
        mainContainer.add(panelRoleSelection, "ROLE_SELECTION");
        mainContainer.add(panelSignup, "SIGNUP");
        mainContainer.add(panelCustomerLogin, "CUSTOMER_LOGIN");
        mainContainer.add(panelRestaurantLogin, "RESTAURANT_LOGIN");
        mainContainer.add(panelAdminLogin, "ADMIN_LOGIN");
        mainContainer.add(panelAdminGUI, "ADMIN_DASHBOARD");
        
        // Add customer and restaurant dashboards
        mainContainer.add(panelCustomerDashboard, "CUSTOMER_DASHBOARD");
        mainContainer.add(panelRestaurantDashboard, "RESTAURANT_DASHBOARD");

        registerActionListeners();

        showCard("START");
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
        panelRestaurantLogin.btnRestaurantForgotPassword.addActionListener(this);
        panelRestaurantLogin.btnRestaurantLoginBack.addActionListener(this);

        // Admin Login Panel & GUI
        panelAdminLogin.btnAdminLogin.addActionListener(this);
        panelAdminLogin.btnAdminForgotPassword.addActionListener(this);
        panelAdminLogin.btnAdminLoginBack.addActionListener(this);
        if (panelAdminGUI.getBtnLogout() != null) {
            panelAdminGUI.getBtnLogout().addActionListener(this);
        }

        // Dashboard Logout Buttons (Safely registers listeners if not null)
        if (panelCustomerDashboard.getBtnLogout() != null) {
            panelCustomerDashboard.getBtnLogout().addActionListener(this);
        }
        if (panelRestaurantDashboard.getBtnLogout() != null) {
            panelRestaurantDashboard.getBtnLogout().addActionListener(this);
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

        // 3. CROSS-NAVIGATION
        else if (source == panelSignup.btnAlreadyHaveAccount) {
            showCard("ROLE_SELECTION");
        }
        else if (source == panelCustomerLogin.btnDontHaveAccount) {
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

        // 8. CUSTOMER LOGIN -> PROCEEDS TO CUSTOMER_DASHBOARD
        else if (source == panelCustomerLogin.btnCustomerLogin) {
            String identifier = panelCustomerLogin.txtLoginEmailOrUser.getText().trim();
            String password = new String(panelCustomerLogin.txtLoginPassword.getPassword());

            if (identifier.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please input a valid email/username and password!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (identifier.equals(VALID_CUSTOMER_USER) && password.equals(VALID_CUSTOMER_PASS)) {
                panelCustomerLogin.txtLoginEmailOrUser.setText("");
                panelCustomerLogin.txtLoginPassword.setText("");
                if (panelCustomerLogin.chkLoginSavePassword != null) {
                    panelCustomerLogin.chkLoginSavePassword.setSelected(false);
                }

                showCard("CUSTOMER_DASHBOARD");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Email/Username or Password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }

        // 9. RESTAURANT LOGIN -> PROCEEDS TO RESTAURANT_DASHBOARD
        else if (source == panelRestaurantLogin.btnRestaurantLogin) {
            String identifier = panelRestaurantLogin.txtRestaurantUser.getText().trim();
            String password = new String(panelRestaurantLogin.txtRestaurantPass.getPassword());

            if (identifier.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please input a valid username and password!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (identifier.equals(VALID_RESTAURANT_USER) && password.equals(VALID_RESTAURANT_PASS)) {
                panelRestaurantLogin.txtRestaurantUser.setText("");
                panelRestaurantLogin.txtRestaurantPass.setText("");
                if (panelRestaurantLogin.chkRestaurantSavePass != null) {
                    panelRestaurantLogin.chkRestaurantSavePass.setSelected(false);
                }

                showCard("RESTAURANT_DASHBOARD");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }

        // 10. ADMIN LOGIN -> PROCEEDS TO ADMIN_DASHBOARD
        else if (source == panelAdminLogin.btnAdminLogin) {
            String username = panelAdminLogin.txtAdminUser.getText().trim();
            String password = new String(panelAdminLogin.txtAdminPass.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please input a valid username and password!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (username.equals(VALID_ADMIN_USER) && password.equals(VALID_ADMIN_PASS)) {
                panelAdminLogin.txtAdminUser.setText("");
                panelAdminLogin.txtAdminPass.setText("");
                if (panelAdminLogin.chkAdminSavePass != null) {
                    panelAdminLogin.chkAdminSavePass.setSelected(false);
                }

                showCard("ADMIN_DASHBOARD");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }

        // 11. LOGOUT ACTIONS FROM ANY DASHBOARD (RETURNS TO ROLE SELECTION)
        else if ((panelAdminGUI.getBtnLogout() != null && source == panelAdminGUI.getBtnLogout()) || 
                 (panelCustomerDashboard.getBtnLogout() != null && source == panelCustomerDashboard.getBtnLogout()) || 
                 (panelRestaurantDashboard.getBtnLogout() != null && source == panelRestaurantDashboard.getBtnLogout())) {
            showCard("ROLE_SELECTION");
        }

        // 12. REGISTRATION VALIDATION
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

            showCard("ROLE_SELECTION");
        }
    }
}