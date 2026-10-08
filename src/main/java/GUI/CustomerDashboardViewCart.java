package GUI;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class CustomerDashboardViewCart extends JPanel {

    // Color Palette
    private Color PURPLE_DARK   = new Color(74, 20, 140);
    private Color PURPLE_LIGHT = new Color(156, 39, 176);
    private Color YELLOW_ACCENT = new Color(255, 193, 7);
    private Color TEXT_DARK     = new Color(30, 30, 30);
    private Color COLOR_WHITE  = new Color(255, 255, 255);
    private Color COLOR_MUTED   = new Color(120, 120, 120);

    private JLabel lblOrderSum, lblTotalBill;
    private JButton btnExitCart, btnCheckout;

    public CustomerDashboardViewCart() {
        setOpaque(true);
        setLayout(null);
        setBackground(COLOR_WHITE);
        setBounds(1024, 0, 300, 720); 
        setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, PURPLE_DARK));

        btnExitCart = new JButton("X");
        btnExitCart.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnExitCart.setForeground(YELLOW_ACCENT);
        btnExitCart.setBounds(225, 30, 50, 30);
        btnExitCart.setContentAreaFilled(false);
        btnExitCart.setBorderPainted(false);
        btnExitCart.setFocusPainted(true);
        add(btnExitCart);

        lblOrderSum = new JLabel("Order Summary");
        lblOrderSum.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblOrderSum.setForeground(PURPLE_DARK);
        lblOrderSum.setBounds(20, 30, 200, 30);
        add(lblOrderSum);

        JLabel lblDivider = new JLabel("--------------------------------------------------------------");
        lblDivider.setForeground(COLOR_MUTED);
        lblDivider.setBounds(20, 535, 260, 15);
        add(lblDivider);

        JLabel lblTotalTitle = new JLabel("Total Amount");
        lblTotalTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotalTitle.setForeground(TEXT_DARK);
        lblTotalTitle.setBounds(20, 555, 120, 25);
        add(lblTotalTitle);

        btnCheckout = new JButton("CHECKOUT");
        btnCheckout.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnCheckout.setForeground(YELLOW_ACCENT);
        btnCheckout.setBackground(PURPLE_DARK);
        btnCheckout.setBounds(20, 610, 250, 45);
        btnCheckout.setBorderPainted(false);
        btnCheckout.setFocusPainted(false);
        add(btnCheckout);
    }

    public void animateOpen() {
        setBounds(724, 0, 300, 720);
        if (getParent() != null) {
            // SHRINKS RESTO IF VIEWCART IS OPEN
            for (Component comp : getParent().getComponents()) {
                if (comp instanceof CustomerDashboardRestaurant) {
                    comp.setBounds(0, 0, 724, 720);
                }
            }
            getParent().revalidate();
            getParent().repaint();
        }
    }

    public void animateClose() {
        setBounds(1024, 0, 300, 720);
        if (getParent() != null) {
            // RETURN BOUNDS WHEN VIEWCART IS EXITED
            for (Component comp : getParent().getComponents()) {
                if (comp instanceof CustomerDashboardRestaurant) {
                    comp.setBounds(0, 0, 1024, 720);
                }
            }
            getParent().revalidate();
            getParent().repaint();
        }
    }

    public void addCartActions(ActionListener listener) {
        btnExitCart.addActionListener(listener);
        btnCheckout.addActionListener(listener);
    }

    public JButton getCloseButton() {
        return btnExitCart;
    }
}