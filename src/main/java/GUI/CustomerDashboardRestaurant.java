package GUI;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

/**
 * @author klara
 */
public class CustomerDashboardRestaurant extends JPanel {
    private Color PURPLE_DARK = new Color(74, 20, 140);   
    private Color YELLOW_ACCENT = new Color(255, 193, 7);
    private Color TEXT_DARK = new Color(30, 30, 30);    
    private Color COLOR_WHITE = new Color(255, 255, 255);
    private Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 36);
    private Color PURPLE_LIGHT_HOVER = new Color(142, 68, 173);
    
    public JPanel topBannerQuote, pnlRestoCard1, pnlRestoCard2, pnlRestoCard3, pnlRestoCard4, 
                  pnlRestoCard5, pnlRestoCard6, pnlRestoCard7, pnlRestoCard8;
    public JLabel lblRestaurant, lblRestoName1, lblRestoName2, lblRestoName3, lblRestoName4, lblRestoName5, lblRestoName6, 
                  lblRestoName7, lblRestoName8, lblRestoImage1, lblRestoImage2, lblRestoImage3, lblRestoImage4, lblRestoImage5,
                  lblRestoImage6, lblRestoImage7, lblRestoImage8;
    public JScrollPane scrollPane;
    public JPanel scrollContentPanel, restoLine;

    public CustomerDashboardRestaurant(){
        setBounds(0, 0, 1024, 720); 
        setLayout(null);
        setOpaque(false); 
        
        // == TOP BANNER AD ==
        topBannerQuote = new GradientPanel();
        topBannerQuote.setLayout(null);
        topBannerQuote.setBounds(110, 135, 865, 120);
        add(topBannerQuote);
        
        // == SCROLL ==
        scrollContentPanel = new JPanel();
        scrollContentPanel.setLayout(null);
        scrollContentPanel.setOpaque(false);
        scrollContentPanel.setPreferredSize(new Dimension(1000, 605)); 

        scrollPane = new JScrollPane(scrollContentPanel);
        scrollPane.setBounds(0, 320, 1024, 400); 
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        add(scrollPane);

        // == RESTAURANT LABEL ==
        lblRestaurant = new JLabel("Restaurants");
        lblRestaurant.setFont(TITLE_FONT);
        lblRestaurant.setForeground(PURPLE_DARK);
        lblRestaurant.setBounds(110, 270, 400, 50); 
        add(lblRestaurant); 
        
        restoLine = new JPanel();
        restoLine.setBackground(PURPLE_DARK); 
        restoLine.setBounds(330, 298, 644, 5);
        add(restoLine); 
        
        // == LAYER ORDER ==
        setComponentZOrder(lblRestaurant, 0);
        setComponentZOrder(restoLine, 1);
        setComponentZOrder(scrollPane, 2);
        if (topBannerQuote != null) {
            setComponentZOrder(topBannerQuote, 3);
        }
        
        // == RESTAURANT CARDS ==

        // == ROW 1 RESTAURANT ==
        
        // == RESTAURANT 1 (MCDOLLIBEE) ==
        pnlRestoCard1 = new JPanel();
        pnlRestoCard1.setLayout(null);
        pnlRestoCard1.setBackground(COLOR_WHITE); 
        pnlRestoCard1.setBounds(110, 113, 200, 130);
        pnlRestoCard1.setBorder(BorderFactory.createLineBorder(PURPLE_DARK, 1));
        scrollContentPanel.add(pnlRestoCard1);
        
        java.awt.Image restoimg1 = new ImageIcon("src/main/java/gui/images/CDRestaurantMJImg1.png").getImage()
                             .getScaledInstance(170, 190, java.awt.Image.SCALE_SMOOTH);
        lblRestoImage1 = new JLabel(new ImageIcon(restoimg1));
        lblRestoImage1.setBounds(96, 3, 230, 190); 
        scrollContentPanel.add(lblRestoImage1);
        scrollContentPanel.setComponentZOrder(lblRestoImage1, 0);
        scrollContentPanel.revalidate();
        scrollContentPanel.repaint();
        
        lblRestoName1 = new JLabel("McDollibee", SwingConstants.CENTER);
        lblRestoName1.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblRestoName1.setForeground(PURPLE_DARK);
        lblRestoName1.setBounds(0, 79, 200, 30); 
        addHoverEffect(lblRestoName1);
        pnlRestoCard1.add(lblRestoName1);
        
        // == RESTAURANT 2 (BRAIN CELLS MILK TEA) ==
        
        pnlRestoCard2 = new JPanel();
        pnlRestoCard2.setLayout(null);
        pnlRestoCard2.setBackground(COLOR_WHITE); 
        pnlRestoCard2.setBounds(330, 113, 200, 130); 
        pnlRestoCard2.setBorder(BorderFactory.createLineBorder(PURPLE_DARK, 1));
        scrollContentPanel.add(pnlRestoCard2);
        
        java.awt.Image restoimg2 = new ImageIcon("src/main/java/gui/images/CDRestaurantBCMTImg2.png").getImage()
                             .getScaledInstance(150, 140, java.awt.Image.SCALE_SMOOTH);
        lblRestoImage2 = new JLabel(new ImageIcon(restoimg2));
        lblRestoImage2.setBounds(316, 45, 230, 150); 
        scrollContentPanel.add(lblRestoImage2);
        scrollContentPanel.setComponentZOrder(lblRestoImage2, 0);
        scrollContentPanel.revalidate();
        scrollContentPanel.repaint();

        lblRestoName2 = new JLabel("Brain Cells Milk Tea", SwingConstants.CENTER);
        lblRestoName2.setFont(new Font("Segoe UI", Font.BOLD, 18)); 
        lblRestoName2.setForeground(PURPLE_DARK);
        lblRestoName2.setBounds(0, 79, 200, 30); 
        addHoverEffect(lblRestoName2);
        pnlRestoCard2.add(lblRestoName2);
        
        // == RESTAURANT 3 (MANG PINAASA) ==
        
        pnlRestoCard3 = new JPanel();
        pnlRestoCard3.setLayout(null);
        pnlRestoCard3.setBackground(COLOR_WHITE); 
        pnlRestoCard3.setBounds(550, 113, 200, 130);
        pnlRestoCard3.setBorder(BorderFactory.createLineBorder(PURPLE_DARK, 1));
        scrollContentPanel.add(pnlRestoCard3);
        
        java.awt.Image restoimg3 = new ImageIcon("src/main/java/gui/images/CDRestaurantMIImg3.png").getImage()
                             .getScaledInstance(150, 140, java.awt.Image.SCALE_SMOOTH);
        lblRestoImage3 = new JLabel(new ImageIcon(restoimg3));
        lblRestoImage3.setBounds(536, 45, 230, 150); 
        scrollContentPanel.add(lblRestoImage3);
        scrollContentPanel.setComponentZOrder(lblRestoImage3, 0);
        scrollContentPanel.revalidate();
        scrollContentPanel.repaint();

        lblRestoName3 = new JLabel("Mang Pinaasa", SwingConstants.CENTER);
        lblRestoName3.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblRestoName3.setForeground(PURPLE_DARK);
        lblRestoName3.setBounds(0, 78, 200, 30); 
        addHoverEffect(lblRestoName3);
        pnlRestoCard3.add(lblRestoName3);
        
        
        // == RESTAURANT 4 (KAMOTE KORNER) ==
        
        pnlRestoCard4 = new JPanel();
        pnlRestoCard4.setLayout(null);
        pnlRestoCard4.setBackground(COLOR_WHITE); 
        pnlRestoCard4.setBounds(770, 113, 200, 130); 
        pnlRestoCard4.setBorder(BorderFactory.createLineBorder(PURPLE_DARK, 1));
        scrollContentPanel.add(pnlRestoCard4);
        
        java.awt.Image restoimg4 = new ImageIcon("src/main/java/gui/images/CDRestaurantKKImg4.png").getImage()
                             .getScaledInstance(150, 140, java.awt.Image.SCALE_SMOOTH);
        lblRestoImage4 = new JLabel(new ImageIcon(restoimg4));
        lblRestoImage4.setBounds(756, 45, 230, 150); 
        scrollContentPanel.add(lblRestoImage4);
        scrollContentPanel.setComponentZOrder(lblRestoImage4, 0);
        scrollContentPanel.revalidate();
        scrollContentPanel.repaint();

        lblRestoName4 = new JLabel("Kamote Korner", SwingConstants.CENTER);
        lblRestoName4.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblRestoName4.setForeground(PURPLE_DARK);
        lblRestoName4.setBounds(0, 78, 200, 30); 
        addHoverEffect(lblRestoName4);
        pnlRestoCard4.add(lblRestoName4);
        
        // ROW 2 RESTAURANT
        // == RESTAURANT 5 (PETSA HUT) ==
        pnlRestoCard5 = new JPanel();
        pnlRestoCard5.setLayout(null);
        pnlRestoCard5.setBackground(COLOR_WHITE); 
        pnlRestoCard5.setBounds(110, 330, 200, 130); 
        pnlRestoCard5.setBorder(BorderFactory.createLineBorder(PURPLE_DARK, 1));
        scrollContentPanel.add(pnlRestoCard5);
        
        java.awt.Image restoimg5 = new ImageIcon("src/main/java/gui/images/CDRestaurantPHImg5.png").getImage()
                             .getScaledInstance(170, 160, java.awt.Image.SCALE_SMOOTH);
        lblRestoImage5 = new JLabel(new ImageIcon(restoimg5));
        lblRestoImage5.setBounds(96, 260, 230, 160); 
        scrollContentPanel.add(lblRestoImage5);
        scrollContentPanel.setComponentZOrder(lblRestoImage5, 0);
        scrollContentPanel.revalidate();
        scrollContentPanel.repaint();
        
        lblRestoName5 = new JLabel("Petsa Hut", SwingConstants.CENTER);
        lblRestoName5.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblRestoName5.setForeground(PURPLE_DARK);
        lblRestoName5.setBounds(0, 78, 200, 30); 
        addHoverEffect(lblRestoName5);
        pnlRestoCard5.add(lblRestoName5);
        
        // == RESTAURANT 6 (BIBANG'S) ==
        pnlRestoCard6 = new JPanel();
        pnlRestoCard6.setLayout(null);
        pnlRestoCard6.setBackground(COLOR_WHITE); 
        pnlRestoCard6.setBounds(330, 330, 200, 130); 
        pnlRestoCard6.setBorder(BorderFactory.createLineBorder(PURPLE_DARK, 1));
        scrollContentPanel.add(pnlRestoCard6);
        
        java.awt.Image restoimg6 = new ImageIcon("src/main/java/gui/images/CDRestaurantBBImg6.png").getImage()
                             .getScaledInstance(100, 140, java.awt.Image.SCALE_SMOOTH);
        lblRestoImage6 = new JLabel(new ImageIcon(restoimg6));
        lblRestoImage6.setBounds(316, 260, 230, 160); 
        scrollContentPanel.add(lblRestoImage6);
        scrollContentPanel.setComponentZOrder(lblRestoImage6, 0);
        scrollContentPanel.revalidate();
        scrollContentPanel.repaint();
        
        lblRestoName6 = new JLabel("Bibang's", SwingConstants.CENTER);
        lblRestoName6.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblRestoName6.setForeground(PURPLE_DARK);
        lblRestoName6.setBounds(0, 78, 200, 30); 
        addHoverEffect(lblRestoName6);
        pnlRestoCard6.add(lblRestoName6);
        
        // == RESTAURANT 7 (SINABON) ==
        pnlRestoCard7 = new JPanel();
        pnlRestoCard7.setLayout(null);
        pnlRestoCard7.setBackground(COLOR_WHITE); 
        pnlRestoCard7.setBounds(550, 330, 200, 130); 
        pnlRestoCard7.setBorder(BorderFactory.createLineBorder(PURPLE_DARK, 1));
        scrollContentPanel.add(pnlRestoCard7);
        
        java.awt.Image restoimg7 = new ImageIcon("src/main/java/gui/images/CDRestaurantSImg7.png").getImage()
                             .getScaledInstance(140, 150, java.awt.Image.SCALE_SMOOTH);
        lblRestoImage7 = new JLabel(new ImageIcon(restoimg7));
        lblRestoImage7.setBounds(536, 260, 230, 160); 
        scrollContentPanel.add(lblRestoImage7);
        scrollContentPanel.setComponentZOrder(lblRestoImage7, 0);
        scrollContentPanel.revalidate();
        scrollContentPanel.repaint();
        
        lblRestoName7 = new JLabel("Sinabon", SwingConstants.CENTER);
        lblRestoName7.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblRestoName7.setForeground(PURPLE_DARK);
        lblRestoName7.setBounds(0, 78, 200, 30); 
        addHoverEffect(lblRestoName7);
        pnlRestoCard7.add(lblRestoName7);
        
        // == RESTAURANT 8 (KUYA'S FRIED CHICKEN) ==
        pnlRestoCard8 = new JPanel();
        pnlRestoCard8.setLayout(null);
        pnlRestoCard8.setBackground(COLOR_WHITE); 
        pnlRestoCard8.setBounds(770, 330, 200, 130); 
        pnlRestoCard8.setBorder(BorderFactory.createLineBorder(PURPLE_DARK, 1));
        scrollContentPanel.add(pnlRestoCard8);
        
        java.awt.Image restoimg8 = new ImageIcon("src/main/java/gui/images/CDRestaurantKFCImg8.png").getImage()
                             .getScaledInstance(160, 150, java.awt.Image.SCALE_SMOOTH);
        lblRestoImage8 = new JLabel(new ImageIcon(restoimg8));
        lblRestoImage8.setBounds(756, 260, 230, 160); 
        scrollContentPanel.add(lblRestoImage8);
        scrollContentPanel.setComponentZOrder(lblRestoImage8, 0);
        scrollContentPanel.revalidate();
        scrollContentPanel.repaint();
        
        lblRestoName8 = new JLabel("Kuya's Fried Chicken", SwingConstants.CENTER);
        lblRestoName8.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblRestoName8.setForeground(PURPLE_DARK);
        lblRestoName8.setBounds(0, 78, 200, 30); 
        addHoverEffect(lblRestoName8);
        pnlRestoCard8.add(lblRestoName8);
    }

    private void addHoverEffect(JLabel label) {
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setForeground(PURPLE_LIGHT_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label.setForeground(PURPLE_DARK);
            }
        });
    }
}