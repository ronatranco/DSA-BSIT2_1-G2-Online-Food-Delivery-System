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
 * ito yung first panel na makikita ng user
 */
public class StartPanel extends JPanel {
    private Color PURPLE_DARK = new Color(74, 20, 140);
    private Color YELLOW_ACCENT = new Color(255, 193, 7);
    private Color BG_LIGHT = new Color(248, 245, 250);
    private Color TEXT_DARK = new Color(30, 30, 30);

    private Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 26);
    private Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public JButton btnStart;
    private MainFrame mainFrame;

    public StartPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setLayout(null);
        setBackground(BG_LIGHT);

        // Increased height from 200 to 220 and shifted Y slightly up so the ears have plenty of space
        JLabel logoStart = new JLabel("🐼", SwingConstants.CENTER);
        logoStart.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 180));
        logoStart.setBounds(262, 85, 500, 220);
        add(logoStart);

        JLabel titleStart = new JLabel("GrabPanda Delivery", SwingConstants.CENTER);
        titleStart.setFont(HEADER_FONT);
        titleStart.setForeground(PURPLE_DARK);
        titleStart.setBounds(262, 315, 500, 40);
        add(titleStart);

        JLabel subtitleStart = new JLabel("Delicious Food Delivered Right To Your Doorstep", SwingConstants.CENTER);
        subtitleStart.setFont(REGULAR_FONT);
        subtitleStart.setForeground(TEXT_DARK);
        subtitleStart.setBounds(262, 360, 500, 30);
        add(subtitleStart);

        btnStart = new JButton("Get Started");
        btnStart.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnStart.setBackground(YELLOW_ACCENT);
        btnStart.setForeground(Color.BLACK);
        btnStart.setFocusPainted(false);
        btnStart.setBounds(382, 430, 260, 55);
        add(btnStart);
    }
}