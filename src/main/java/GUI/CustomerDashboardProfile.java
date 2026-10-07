package GUI;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

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
    private Font HEADER_FONT  = new Font("Segoe UI", Font.BOLD, 26);

    private JButton btnSaveProfile;
    
    public CustomerDashboardProfile() {
        setLayout(null);
        setBackground(COLOR_WHITE);
        setBounds(1024, 0, 944, 720); 
        setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, YELLOW_ACCENT));
        
        JLabel lblTitle = new JLabel("Your Information");
        lblTitle.setFont(HEADER_FONT);
        lblTitle.setForeground(PURPLE_DARK);
        lblTitle.setBounds(35, 33, 250, 35);
        add(lblTitle);
        
        btnSaveProfile = new JButton("SAVE PROFILE");
        btnSaveProfile.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnSaveProfile.setForeground(YELLOW_ACCENT);
        btnSaveProfile.setBackground(PURPLE_DARK);
        btnSaveProfile.setBounds(347, 610, 250, 45);
        add(btnSaveProfile);
    }

    public void animateOpen() {
        setBounds(200, 0, 824, 720); 
    }

    public void animateClose() {
        setBounds(1024, 0, 824, 720); 
    }

    public void addProfileActions(ActionListener listener) {
        btnSaveProfile.addActionListener(listener); 
    }
}
