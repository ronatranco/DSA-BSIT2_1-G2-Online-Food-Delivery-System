/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import Database.AccountDAO;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
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
    
    public JPanel topBannerQuote;
    public JLabel lblRestaurant;
    public JScrollPane scrollPane;
    public JPanel scrollContentPanel, restoLine;

    public CustomerDashboardRestaurant(){
        setBounds(0, 0, 1024, 720); 
        setLayout(null);
        setOpaque(false); 
        
        // TOP BANNER AD
        topBannerQuote = new GradientPanel();
        topBannerQuote.setLayout(null);
        topBannerQuote.setBounds(110, 135, 865, 120);
        add(topBannerQuote);
        topBannerQuote.setBorder(BorderFactory.createLineBorder(YELLOW_ACCENT, 2));
        add(topBannerQuote);
        
        // BANNER AD IMAGE
        java.awt.Image bannerImg = new ImageIcon("src/main/java/gui/images/cdrestaurantbanner.png").getImage()
                                                 .getScaledInstance(510, 85, java.awt.Image.SCALE_SMOOTH);
        JLabel lblBannerImage = new JLabel(new ImageIcon(bannerImg));
        lblBannerImage.setBounds(345, 12, 490, 100);
        topBannerQuote.add(lblBannerImage);
        
        JTextArea lblText = new JTextArea("YOUR HOT DELIVERY ON HIGH-SPEED PRIORITY!");
        lblText.setBounds(40, 25, 360, 70); 
        lblText.setForeground(YELLOW_ACCENT);
        lblText.setFont(new Font("Arial", Font.BOLD, 23)); 
        lblText.setEditable(false);
        lblText.setFocusable(false);
        lblText.setOpaque(false);
        lblText.setLineWrap(true);
        lblText.setWrapStyleWord(true);
        topBannerQuote.add(lblText);
        topBannerQuote.setComponentZOrder(lblText, 0); 
        topBannerQuote.setComponentZOrder(lblBannerImage, 1);
        
        JLabel lblsubText = new JLabel("Order Now >");
        lblsubText.setBounds(40, 80, 390, 30);
        lblsubText.setForeground(Color.WHITE);
        lblsubText.setFont(new Font("Arial", Font.BOLD, 13));
        topBannerQuote.add(lblsubText);
        topBannerQuote.setComponentZOrder(lblsubText, 0); 
        topBannerQuote.setComponentZOrder(lblBannerImage, 1);
        
        // SCROLL PANE
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

        // RESTAURANT
        lblRestaurant = new JLabel("Restaurants");
        lblRestaurant.setFont(TITLE_FONT);
        lblRestaurant.setForeground(PURPLE_DARK);
        lblRestaurant.setBounds(110, 270, 400, 50); 
        add(lblRestaurant); 
        
        restoLine = new JPanel();
        restoLine.setBackground(PURPLE_DARK); 
        restoLine.setBounds(330, 298, 644, 5);
        add(restoLine); 
        
        // LAYER ORDER
        setComponentZOrder(lblRestaurant, 0);
        setComponentZOrder(restoLine, 1);
        setComponentZOrder(scrollPane, 2);
        if (topBannerQuote != null) {
            setComponentZOrder(topBannerQuote, 3);
        }
    }

    // ===== restaurant cards built from the database =====
    public static class RestoCard {
        public final String name;
        public final String area;
        public final JPanel panel;
        public final JLabel lblName;
        public final JLabel lblImage;   // null when the restaurant has no picture
        public final int imageDy;       // image label's y offset from the card's top

        RestoCard(String name, String area, JPanel panel, JLabel lblName, JLabel lblImage, int imageDy) {
            this.name = name;
            this.area = area;
            this.panel = panel;
            this.lblName = lblName;
            this.lblImage = lblImage;
            this.imageDy = imageDy;
        }
    }

    private final List<RestoCard> dynamicCards = new ArrayList<>();
    private static final int CARD_W = 200, CARD_H = 130, CARD_GAP = 20, PER_ROW = 4;
    private int lastBaseX = 110;

    // old pictures, matched by restaurant name: {file, imgW, imgH, labelH, yOffsetFromCardTop}
    private static final java.util.Map<String, Object[]> RESTO_IMAGES = new java.util.HashMap<>();
    static {
        RESTO_IMAGES.put("mcdollibee",           new Object[]{"CDRestaurantMJImg1.png",  170, 190, 190, -110});
        RESTO_IMAGES.put("brain cells milk tea", new Object[]{"CDRestaurantBCMTImg2.png", 150, 140, 150, -68});
        RESTO_IMAGES.put("mang pinaasa",         new Object[]{"CDRestaurantMIImg3.png",  150, 140, 150, -68});
        RESTO_IMAGES.put("kamote korner",        new Object[]{"CDRestaurantKKImg4.png",  150, 140, 150, -68});
        RESTO_IMAGES.put("petsa hut",            new Object[]{"CDRestaurantPHImg5.png",  170, 160, 160, -70});
        RESTO_IMAGES.put("bibang's",             new Object[]{"CDRestaurantBBImg6.png",  100, 140, 160, -70});
        RESTO_IMAGES.put("sinabon",              new Object[]{"CDRestaurantSImg7.png",   140, 150, 160, -70});
        RESTO_IMAGES.put("kuya's fried chicken", new Object[]{"CDRestaurantKFCImg8.png", 160, 150, 160, -70});
    }

    public List<RestoCard> getCards() {
        return dynamicCards;
    }

    public void showRestaurants(List<AccountDAO.RestaurantInfo> list, MouseListener clickListener) {
        for (RestoCard c : dynamicCards) {
            scrollContentPanel.remove(c.panel);
            if (c.lblImage != null) scrollContentPanel.remove(c.lblImage);
        }
        dynamicCards.clear();

        for (AccountDAO.RestaurantInfo r : list) {
            String name = (r.name == null) ? "(unnamed)" : r.name;
            String area = (r.area == null) ? "" : r.area;

            JPanel card = new JPanel();
            card.setLayout(null);
            card.setBackground(COLOR_WHITE);
            card.setBorder(BorderFactory.createLineBorder(PURPLE_DARK, 1));

            JLabel imageLabel = null;
            int imageDy = 0;
            Object[] info = RESTO_IMAGES.get(name.toLowerCase().trim());
            if (info != null) {
                java.awt.Image img = new ImageIcon("src/main/java/gui/images/" + info[0]).getImage()
                        .getScaledInstance((int) info[1], (int) info[2], java.awt.Image.SCALE_SMOOTH);
                imageLabel = new JLabel(new ImageIcon(img));
                imageDy = (int) info[4];
                imageLabel.setSize(230, (int) info[3]);
                scrollContentPanel.add(imageLabel);
                scrollContentPanel.setComponentZOrder(imageLabel, 0); // draw above the card
            } else {
                JLabel icon = new JLabel("\uD83C\uDF7D", SwingConstants.CENTER);
                icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
                icon.setBounds(0, 12, CARD_W, 50);
                card.add(icon);
            }

            JLabel lblName = new JLabel(name, SwingConstants.CENTER);
            lblName.setFont(new Font("Segoe UI", Font.BOLD, 18));
            lblName.setForeground(PURPLE_DARK);
            lblName.setBounds(0, 70, CARD_W, 30);
            addHoverEffect(lblName);
            lblName.addMouseListener(clickListener);
            card.add(lblName);

            JLabel lblArea = new JLabel(area, SwingConstants.CENTER);
            lblArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblArea.setForeground(new Color(150, 145, 170));
            lblArea.setBounds(0, 98, CARD_W, 24);
            card.add(lblArea);

            scrollContentPanel.add(card);
            dynamicCards.add(new RestoCard(name, area, card, lblName, imageLabel, imageDy));
        }
        layoutCards(lastBaseX);
    }

    // places the visible cards in rows of 4, with no gaps
    public void layoutCards(int baseX) {
        lastBaseX = baseX;
        int shown = 0;
        for (RestoCard c : dynamicCards) {
            if (!c.panel.isVisible()) {
                if (c.lblImage != null) c.lblImage.setVisible(false);
                continue;
            }
            int col = shown % PER_ROW;
            int row = shown / PER_ROW;
            int x = baseX + col * (CARD_W + CARD_GAP);
            int y = 113 + row * 217;   // same spacing as the old hardcoded cards (113, 330)
            c.panel.setBounds(x, y, CARD_W, CARD_H);
            if (c.lblImage != null) {
                c.lblImage.setVisible(true);
                c.lblImage.setLocation(x - 14, y + c.imageDy);
            }
            shown++;
        }
        int rows = (shown + PER_ROW - 1) / PER_ROW;
        scrollContentPanel.setPreferredSize(new Dimension(1000, Math.max(605, 113 + rows * 217 + 30)));
        scrollContentPanel.revalidate();
        scrollContentPanel.repaint();
    }

    public void showAll(boolean visible) {
        for (RestoCard c : dynamicCards) c.panel.setVisible(visible);
        layoutCards(lastBaseX);
    }

    public void filterByName(String query) {
        String q = query.toLowerCase().trim();
        for (RestoCard c : dynamicCards) c.panel.setVisible(c.name.toLowerCase().contains(q));
        layoutCards(lastBaseX);
    }

    public void filterByArea(String area) {
        for (RestoCard c : dynamicCards) c.panel.setVisible(c.area.equalsIgnoreCase(area.trim()));
        layoutCards(lastBaseX);
    }

    private void addHoverEffect(JLabel label) {
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setForeground(PURPLE_LIGHT_HOVER);
                if (scrollContentPanel != null) {
                scrollContentPanel.repaint();
            }
        }

            @Override
            public void mouseExited(MouseEvent e) {
                label.setForeground(PURPLE_DARK);
                if (scrollContentPanel != null) {
                scrollContentPanel.repaint();
                }
            }
        });
    }
}
