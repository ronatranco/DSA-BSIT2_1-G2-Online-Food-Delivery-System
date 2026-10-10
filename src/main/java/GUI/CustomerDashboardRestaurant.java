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
    
    // Light Purple variant used for hover transitions
    private Color PURPLE_LIGHT_HOVER = new Color(142, 68, 173);
    
    public JPanel topBannerQuote, pnlRestoCard1, pnlRestoCard2, pnlRestoCard3, pnlRestoCard4, 
                  pnlRestoCard5, pnlRestoCard6, pnlRestoCard7, pnlRestoCard8;
    public JLabel lblRestaurant, lblRestoName1, lblRestoName2, lblRestoName3, lblRestoName4, lblRestoName5, lblRestoName6, 
                  lblRestoName7, lblRestoName8, lblRestoImage1, lblRestoImage2, lblRestoImage3;
    public JScrollPane scrollPane;
    public JPanel scrollContentPanel, restoLine;

    public CustomerDashboardRestaurant(){
        setBounds(0, 0, 1024, 720); 
        setLayout(null);
        setOpaque(false); 
        
        // == TOP BANNER ==
        topBannerQuote = new JPanel();
        topBannerQuote.setLayout(null);
        topBannerQuote.setBackground(PURPLE_DARK);
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
        pnlRestoCard1.setBounds(110, 110, 200, 130); 
        pnlRestoCard1.setBorder(BorderFactory.createLineBorder(YELLOW_ACCENT, 1));
        scrollContentPanel.add(pnlRestoCard1);

        lblRestoName1 = new JLabel("McDollibee", SwingConstants.CENTER);
        lblRestoName1.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblRestoName1.setForeground(PURPLE_DARK);
        lblRestoName1.setBounds(0, 80, 200, 30); 
        addHoverEffect(lblRestoName1);
        pnlRestoCard1.add(lblRestoName1);
        
        // == RESTAURANT 2 (BRAIN CELLS MILK TEA) ==
        
        pnlRestoCard2 = new JPanel();
        pnlRestoCard2.setLayout(null);
        pnlRestoCard2.setBackground(COLOR_WHITE); 
        pnlRestoCard2.setBounds(330, 110, 200, 130); 
        pnlRestoCard2.setBorder(BorderFactory.createLineBorder(YELLOW_ACCENT, 1));
        scrollContentPanel.add(pnlRestoCard2);

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
        pnlRestoCard3.setBounds(550, 110, 200, 130);
        pnlRestoCard3.setBorder(BorderFactory.createLineBorder(YELLOW_ACCENT, 1));
        scrollContentPanel.add(pnlRestoCard3);

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
        pnlRestoCard4.setBounds(770, 110, 200, 130); 
        pnlRestoCard4.setBorder(BorderFactory.createLineBorder(YELLOW_ACCENT, 1));
        scrollContentPanel.add(pnlRestoCard4);

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
        pnlRestoCard5.setBorder(BorderFactory.createLineBorder(YELLOW_ACCENT, 1));
        scrollContentPanel.add(pnlRestoCard5);

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
        pnlRestoCard6.setBorder(BorderFactory.createLineBorder(YELLOW_ACCENT, 1));
        scrollContentPanel.add(pnlRestoCard6);

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
        pnlRestoCard7.setBorder(BorderFactory.createLineBorder(YELLOW_ACCENT, 1));
        scrollContentPanel.add(pnlRestoCard7);

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
        pnlRestoCard8.setBorder(BorderFactory.createLineBorder(YELLOW_ACCENT, 1));
        scrollContentPanel.add(pnlRestoCard8);

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