package GUI;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 *
 * @author klara
 */
public class CustomerDashboardProfile extends JPanel {
    private Color PURPLE_DARK   = new Color(74, 20, 140);
    private Color YELLOW_ACCENT = new Color(255, 193, 7);
    private Color TEXT_DARK     = new Color(30, 30, 30);
    private Color COLOR_WHITE   = new Color(255, 255, 255);
    private Color COLOR_MUTED   = new Color(120, 120, 120);
    private Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private Font HEADER_FONT  = new Font("Segoe UI", Font.BOLD, 26);

    private JTextField txtFirstName, txtFullName, txtFullAdd, txtNumber, txtEmail, txtCPw, txtNPw;
    private JButton btnSaveInfo, btnSaveEmail, btnSavePassword, btnDelAcc;
    private JScrollPane scrollPane;
    
    public CustomerDashboardProfile() {
        setLayout(null);
        setBackground(COLOR_WHITE);
        setBounds(1024, 0, 944, 720); 
        setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, YELLOW_ACCENT));
        
        JPanel pnlMainProfile = new JPanel();
        pnlMainProfile.setLayout(null);
        pnlMainProfile.setBackground(COLOR_WHITE);
        pnlMainProfile.setPreferredSize(new Dimension(824, 1300)); 
        
        // == MY PROFILE ==
        JLabel lblProfile = new JLabel("Your Information");
        lblProfile.setFont(HEADER_FONT);
        lblProfile.setForeground(PURPLE_DARK);
        lblProfile.setBounds(85, 77, 250, 35); 
        pnlMainProfile.add(lblProfile);
        // PROFILE ICON
        JLabel lblInfoIcon = new JLabel("\u24D8");
        lblInfoIcon.setFont(new Font("Dialog", Font.PLAIN, 30));
        lblInfoIcon.setForeground(PURPLE_DARK);
        lblInfoIcon.setBounds(310, 79, 30, 30);
        pnlMainProfile.add(lblInfoIcon);
        // FIRST NAME FIELD
        pnlOutlineField pnlFirstName = new pnlOutlineField("First Name");
        pnlFirstName.setBounds(85, 130, 300, 55);
        txtFirstName = pnlFirstName.getTextField();
        pnlMainProfile.add(pnlFirstName);
        // LAST NAME FIELD
        pnlOutlineField pnlFullName = new pnlOutlineField("Last Name");
        pnlFullName.setBounds(402, 130, 300, 55);
        txtFullName = pnlFullName.getTextField();
        pnlMainProfile.add(pnlFullName);
        // ADDRESS FIELD
        pnlOutlineField pnlAddress = new pnlOutlineField("Full Address");
        pnlAddress.setBounds(85, 200, 300, 55);
        txtFullAdd = pnlAddress.getTextField();
        pnlMainProfile.add(pnlAddress);
        // NUMBER FIELD
        pnlOutlineField pnlNumber = new pnlOutlineField("Mobile Number");
        pnlNumber.setBounds(402, 200, 300, 55);
        txtNumber = pnlNumber.getTextField();
        pnlMainProfile.add(pnlNumber);
        // SAVE BTN INFO
        btnSaveInfo = styleBtnSave("Save");
        btnSaveInfo.setBounds(85, 270, 100, 35);
        pnlMainProfile.add(btnSaveInfo);
        // SEPARATOR LINE
        JPanel pnlPLine = new JPanel();
        pnlPLine.setBackground(PURPLE_DARK);
        pnlPLine.setBounds(85, 330, 615, 2); 
        pnlMainProfile.add(pnlPLine);
        // == EMAIL SECTION ==
        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(HEADER_FONT);
        lblEmail.setForeground(PURPLE_DARK);
        lblEmail.setBounds(85, 350, 250, 35); 
        pnlMainProfile.add(lblEmail);
        // EMAIL FIELD
        pnlOutlineField pnlEmail = new pnlOutlineField("Email Address");
        pnlEmail.setBounds(85, 400, 615, 55);
        txtEmail = pnlEmail.getTextField();
        pnlMainProfile.add(pnlEmail);
        // SAVE BTN EMAIL
        btnSaveEmail = styleBtnSave("Save");
        btnSaveEmail.setBounds(85, 470, 100, 35);
        pnlMainProfile.add(btnSaveEmail);
        // SEPARATOR LINE
        JPanel pnlELine = new JPanel();
        pnlELine.setBackground(PURPLE_DARK);
        pnlELine.setBounds(85, 530, 615, 2); 
        pnlMainProfile.add(pnlELine);
        // == PASSWORD ==
        JLabel lblPw = new JLabel("Password");
        lblPw.setFont(HEADER_FONT);
        lblPw.setForeground(PURPLE_DARK);
        lblPw.setBounds(85, 550, 250, 35); 
        pnlMainProfile.add(lblPw);
        // CURRENT PW FIELD
        pnlOutlineField pnlCPw = new pnlOutlineField("Current Password");
        pnlCPw.setBounds(85, 600, 615, 55);
        txtCPw = pnlCPw.getTextField();
        pnlMainProfile.add(pnlCPw);
        // NEW PW FIELD
        pnlOutlineField pnlNPw = new pnlOutlineField("New Password");
        pnlNPw.setBounds(85, 670, 615, 55);
        txtNPw = pnlNPw.getTextField();
        pnlMainProfile.add(pnlNPw);
        // SAVE BTN PW
        btnSavePassword = styleBtnSave("Save");
        btnSavePassword.setBounds(85, 740, 100, 35);
        pnlMainProfile.add(btnSavePassword);
        // SEPARATOR LINE
        JPanel pnlPwLine = new JPanel();
        pnlPwLine.setBackground(PURPLE_DARK);
        pnlPwLine.setBounds(85, 800, 615, 2); 
        pnlMainProfile.add(pnlPwLine);
        // == PAYMENTS ==
        JLabel lblPay = new JLabel("Payment Method");
        lblPay.setFont(HEADER_FONT);
        lblPay.setForeground(PURPLE_DARK);
        lblPay.setBounds(85, 820, 250, 35); 
        pnlMainProfile.add(lblPay);
        // == PAYMENT SUBTITLE ==
        JLabel lblPayST = new JLabel("You have no saved payment options yet.");
        lblPayST.setFont(REGULAR_FONT);
        lblPayST.setForeground(PURPLE_DARK);
        lblPayST.setBounds(85, 860, 280, 35); 
        pnlMainProfile.add(lblPayST);
        // SEPARATOR LINE
        JPanel pnlPayLine = new JPanel();
        pnlPayLine.setBackground(PURPLE_DARK);
        pnlPayLine.setBounds(85, 910, 615, 2); 
        pnlMainProfile.add(pnlPayLine);
        // == ACC ==
        JLabel lblAcc = new JLabel("Account Management");
        lblAcc.setFont(HEADER_FONT);
        lblAcc.setForeground(PURPLE_DARK);
        lblAcc.setBounds(85, 930, 280, 35); 
        pnlMainProfile.add(lblAcc);
        // == ACC SUBTITLE ==
        JLabel lblAccST = new JLabel("You can delete your account and personal data associated with it");
        lblAccST.setFont(REGULAR_FONT);
        lblAccST.setForeground(PURPLE_DARK);
        lblAccST.setBounds(85, 970, 420, 35); 
        pnlMainProfile.add(lblAccST);
        // BTN DELETE ACC
        btnDelAcc = styleBtnSave("Delete my account");
        btnDelAcc.setForeground(COLOR_WHITE);
        btnDelAcc.setBackground(PURPLE_DARK);
        btnDelAcc.setBounds(85, 1020, 180, 35);
        pnlMainProfile.add(btnDelAcc);
        // SCROLL PANE
        scrollPane = new JScrollPane(pnlMainProfile);
        scrollPane.setBorder(null); 
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); 
        scrollPane.setBounds(0, 0, 824, 800);
        add(scrollPane, BorderLayout.CENTER);
    }

    // HELPER METHOD FOR SAVE BUTTONS
    private JButton styleBtnSave(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(PURPLE_DARK);
        btn.setBackground(YELLOW_ACCENT);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); 
        return btn;
    }
    public void animateOpen() {
        setBounds(200, 0, 824, 720); 
    }

    public void animateClose() {
        setBounds(1024, 0, 824, 720); 
    }
    public String getFirstNameText() { 
        return txtFirstName.getText().trim();
    }
    public JTextField getTxtFirstName() { 
        return txtFirstName;
    }   
    public void addProfileActions(ActionListener listener) {
        txtFirstName.addActionListener(listener); 
        btnSaveInfo.addActionListener(listener); 
        btnSaveEmail.addActionListener(listener); 
        btnSavePassword.addActionListener(listener); 
    }
    public JButton getBtnSaveInfo() { return btnSaveInfo; }
    public JButton getBtnSaveEmail() { return btnSaveEmail; }
    public JButton getBtnSavePassword() { return btnSavePassword; }

    private static class pnlOutlineField extends JPanel {
        private final JTextField internalField;
        public pnlOutlineField(String labelTitle) {
            setLayout(new BorderLayout());
            setBackground(Color.WHITE);

            TitledBorder titledBorder = BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(new Color(255, 193, 7), 1, true), 
                    labelTitle
            );
            titledBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 12));
            titledBorder.setTitleColor(new Color(74, 20, 140));
            titledBorder.setTitleJustification(TitledBorder.LEFT);
            titledBorder.setTitlePosition(TitledBorder.TOP);
            setBorder(titledBorder);

            internalField = new JTextField();
            internalField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            internalField.setForeground(new Color(40, 40, 40));
            internalField.setBackground(Color.WHITE);
            internalField.setBorder(BorderFactory.createEmptyBorder(2, 12, 2, 12)); 

            add(internalField, BorderLayout.CENTER);
        }

        public JTextField getTextField() {
            return internalField;
        }
    }
}
