/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import java.awt.*;
import javax.swing.*;
/**
 *
 * @author rownavanana
 */

public class GradientPanel extends JPanel {
    private Color PURPLE_DARK = new Color(74, 20, 140); 
    private Color PURPLE_LIGHT = new Color(156, 39, 176);

    public GradientPanel() {
        setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gp = new GradientPaint(0, 0, PURPLE_LIGHT, getWidth(), getHeight(), PURPLE_DARK);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight()); 
    }
}
