/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
/**
 *
 * @author rownavanana
 */

public class StartPanel extends JPanel {
    private Color PURPLE_DARK = new Color(74, 20, 140);
    private Color YELLOW_ACCENT = new Color(255, 193, 7);
    private Color BG_LIGHT = new Color(248, 245, 250);
    private Color TEXT_DARK = new Color(30, 30, 30);

    private Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 26);
    private Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public JButton btnStart;

    public StartPanel(ActionListener listener) {
        setLayout(null);
        setBackground(BG_LIGHT);

        JLabel logoStart = new JLabel("🐼", SwingConstants.CENTER);
        logoStart.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 150));
        logoStart.setBounds(312, 70, 400, 190);
        add(logoStart);

        JLabel titleStart = new JLabel("GrabPanda Delivery", SwingConstants.CENTER);
        titleStart.setFont(HEADER_FONT);
        titleStart.setForeground(PURPLE_DARK);
        titleStart.setBounds(262, 270, 500, 40);
        add(titleStart);

        JLabel subtitleStart = new JLabel("Delicious Food Delivered Right To Your Doorstep", SwingConstants.CENTER);
        subtitleStart.setFont(REGULAR_FONT);
        subtitleStart.setForeground(TEXT_DARK);
        subtitleStart.setBounds(262, 315, 500, 30);
        add(subtitleStart);

        btnStart = new JButton("Get Started");
        btnStart.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnStart.setBackground(YELLOW_ACCENT);
        btnStart.setForeground(Color.BLACK);
        btnStart.setFocusPainted(false);
        btnStart.setBounds(382, 380, 260, 55);
        btnStart.addActionListener(listener);
        add(btnStart);
    }
}