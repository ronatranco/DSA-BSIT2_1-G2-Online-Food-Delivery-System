/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import Model.Order;
import Model.Order.OrderItem;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 * @author RJD
 * 
 */
public class CustomerDashboardViewCart extends JPanel {
    
    // COLORS
    private Color PURPLE_DARK = new Color(74, 20, 140);
    private Color PURPLE_LIGHT = new Color(156, 39, 176);
    private Color YELLOW_ACCENT = new Color(255, 193, 7);
    private Color TEXT_DARK = new Color(30, 30, 30);
    private Color COLOR_WHITE = new Color(255, 255, 255);
    private Color BORDER_COLOR = new Color(225, 202, 230);
    private Color CARD_BG = new Color (250, 248, 252);
    private Color PURPLEBTN_LIGHT = new Color(230, 220, 245);
    private Color CART_GRADIENT_PURPLE = new Color (110, 20, 160);
    private Color CART_GRADIENT_PINK = new Color (175, 30, 140);
    private Color CART_GRADIENT_YELLOW = new Color (255, 195, 35);
    private Color SUBTITLE_COLOR = new Color(130, 120, 150);
    private Color EXIT_BTN_BG = new Color(255, 204, 0);
    private Color EXIT_BTN_HOVER = new Color(240, 190, 0);
    private Color EXIT_BTN_FG = new Color(140, 70, 0);
    private Color REMOVE_BTN_BG = new Color(235, 225, 245);

    // Declarations
    private JLabel lblCartTitle, lblCartSubtitle, lblSubtotal, lblSubtotalCount, lblSubtotalBill, lblFooterQuote;
    private JButton btnExitCart, btnCheckout;
    private JPanel ContainerPanel;
    private JPanel subtotalCard;
    private JCheckBox currentlySelectedRestaurant = null;    

    public CustomerDashboardViewCart(){

       //1. Main Panel
       setLayout(null);
       setBackground(COLOR_WHITE);
       setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0, PURPLE_LIGHT)); 
       setBounds(1024, 0, 300, 720);

       //2. Header
       JPanel cartIconPanel = new JPanel(){
           @Override
           protected void paintComponent(Graphics g){
               super.paintComponent(g);
               Graphics2D g2 = (Graphics2D) g.create();
               g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

               GradientPaint cartGradient = new GradientPaint(
                       2, 2, CART_GRADIENT_PURPLE,
                       48, 53, CART_GRADIENT_PINK
               );
               g2.setPaint(cartGradient);
               g2.setStroke(new BasicStroke(3.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

               g2.draw(new Path2D.Float() {{
                    moveTo(2, 8);
                    lineTo(16, 8);
                    lineTo(22, 38);
                    lineTo(50, 38);
                }});
               g2.draw(new RoundRectangle2D.Float(16, 15, 31, 20, 5, 5));

               g2.setStroke(new BasicStroke(2.0f));
               g2.drawLine(26, 15, 26, 35);
               g2.drawLine(37, 15, 37, 35);
               g2.drawLine(16, 24, 47, 24);

               g2.fill(new Ellipse2D.Float(19, 42, 6.5f, 6.5f));
               g2.fill(new Ellipse2D.Float(40, 42, 6.5f, 6.5f));        

               g2.setColor(CART_GRADIENT_YELLOW);
               g2.setStroke(new BasicStroke(2.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
               g2.drawLine(52, 11, 58, 6);
               g2.drawLine(56, 18, 63, 17);
               g2.drawLine(52, 25, 60, 27);      

               g2.dispose();
           }
        };

        cartIconPanel.setOpaque(false);
        cartIconPanel.setBounds(12, 12, 68, 52);
        add(cartIconPanel);

        lblCartTitle = new JLabel("Your Cart");
        lblCartTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblCartTitle.setForeground(PURPLE_DARK);
        lblCartTitle.setBounds(85, 16, 140, 20);
        add(lblCartTitle);

        lblCartSubtitle = new JLabel("0 restaurants • 0 items");
        lblCartSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCartSubtitle.setForeground(SUBTITLE_COLOR);
        lblCartSubtitle.setBounds(85, 36, 140, 16);
        add(lblCartSubtitle);   

        //3. Exit Cart Button
        btnExitCart = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));

                g2.setColor(getForeground());
                g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                int cx = getWidth() / 2;
                int cy = getHeight() / 2;

                g2.drawLine(cx - 5, cy, cx + 5, cy);
                g2.drawLine(cx + 1, cy - 4, cx + 5, cy);
                g2.drawLine(cx + 1, cy + 4, cx + 5, cy);

                g2.dispose();
            }
        };

        btnExitCart.setOpaque(false);
        btnExitCart.setContentAreaFilled(false);
        btnExitCart.setBorderPainted(false);
        btnExitCart.setFocusPainted(false);
        btnExitCart.setBackground(EXIT_BTN_BG); 
        btnExitCart.setForeground(EXIT_BTN_FG);   
        btnExitCart.setBounds(224, 18, 50, 40);
        btnExitCart.setCursor(new Cursor(Cursor.HAND_CURSOR)); 

        btnExitCart.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnExitCart.setBackground(EXIT_BTN_HOVER);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnExitCart.setBackground(EXIT_BTN_BG);
            }
        });
        add(btnExitCart); 

        //4. Separators
        JSeparator topSeparator = new JSeparator();
        topSeparator.setBounds(12, 65, 263, 2);
        topSeparator.setForeground(PURPLE_DARK);
        add(topSeparator);

        JSeparator btmseparator = new JSeparator();
        btmseparator.setBounds(12, 485, 263, 2);
        btmseparator.setForeground(PURPLE_DARK);
        add(btmseparator);

        //5. Container Panel 
        ContainerPanel = new JPanel();
        ContainerPanel.setLayout(new BoxLayout(ContainerPanel, BoxLayout.Y_AXIS));
        ContainerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        ContainerPanel.setBackground(COLOR_WHITE);
        ContainerPanel.setOpaque(false);
        ContainerPanel.setBorder(BorderFactory.createEmptyBorder(14, 0, 8, 0)); 

        //6. Scroll Pane
        JScrollPane scrollPane = new JScrollPane(ContainerPanel);
        scrollPane.setBounds(10, 76, 280, 403);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(COLOR_WHITE);
        scrollPane.setBackground(COLOR_WHITE); 

        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JScrollBar verticalbar = scrollPane.getVerticalScrollBar();
        verticalbar.setUnitIncrement(4);
        verticalbar.setPreferredSize(new Dimension(1, 0)); 
        add(scrollPane); 

        //7. Subtotal Card
        subtotalCard = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.setColor(BORDER_COLOR);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 16, 16));
                g2.dispose();
            }
        };

        subtotalCard.setOpaque(false);
        subtotalCard.setBounds(14, 495, 260, 52);

        JLabel lblReceiptIcon = new JLabel("🧾", SwingConstants.CENTER);
        lblReceiptIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        lblReceiptIcon.setBounds(14, 8, 36, 36);
        subtotalCard.add(lblReceiptIcon);

        lblSubtotal = new JLabel("Subtotal");
        lblSubtotal.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSubtotal.setForeground(TEXT_DARK);
        lblSubtotal.setBounds(52, 8, 90, 18);
        subtotalCard.add(lblSubtotal);

        lblSubtotalCount = new JLabel("0 items");
        lblSubtotalCount.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSubtotalCount.setForeground(Color.GRAY);
        lblSubtotalCount.setBounds(52, 26, 90, 16);
        subtotalCard.add(lblSubtotalCount);

        lblSubtotalBill = new JLabel("₱0.00", SwingConstants.RIGHT);
        lblSubtotalBill.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblSubtotalBill.setForeground(PURPLE_DARK);
        lblSubtotalBill.setBounds(135, 14, 115, 24);
        subtotalCard.add(lblSubtotalBill);

        add(subtotalCard);

        //8. Checkout Button & Footer
        btnCheckout = new JButton("🛍️  CHECKOUT") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 40, 40));
                g2.dispose();
                super.paintComponent(g); 
            }
        };

        btnCheckout.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        btnCheckout.setForeground(COLOR_WHITE);
        btnCheckout.setBackground(PURPLE_DARK);
        btnCheckout.setContentAreaFilled(false);
        btnCheckout.setBorderPainted(false);
        btnCheckout.setFocusPainted(false);
        btnCheckout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCheckout.setBounds(14, 558, 260, 44);
        add(btnCheckout); 

        btnCheckout.addActionListener(e -> {
            if (currentlySelectedRestaurant == null){
                showCustomWarningDialog("No Selection", "Please select a restaurant to checkout.");
                return;
            }

            boolean proceed = showCustomConfirmDialog("Confirm Checkout", "Do you want to proceed to checkout?");
            if (proceed) {
                Order newOrder = generateOrderFromSelection("CUST-001");

                if (newOrder != null) {
                    Container parent = this.getParent();

                    if (parent != null) {
                        CustomerDashboardCheckout pnlRightCheckout = new CustomerDashboardCheckout(newOrder);
                        pnlRightCheckout.setBounds(724, 0, 300, 720); 

                        parent.add(pnlRightCheckout);
                        parent.remove(this);

                        if (parent instanceof JPanel) {
                            ((JPanel) parent).setComponentZOrder(pnlRightCheckout, 0);
                        }

                        pnlRightCheckout.setVisible(true);

                        // --- CLEAN ORDER PLACED CALLBACK ---
                        pnlRightCheckout.setOnOrderPlacedCallback(placedOrder -> {
                            pnlRightCheckout.setVisible(false);
                            parent.remove(pnlRightCheckout);

                            // 1. Clear cart items and totals
                            ContainerPanel.removeAll();
                            updateCartTotals();

                            // 2. Hide cart panel and reset its position off-screen
                            setVisible(false);
                            setBounds(1024, 0, 300, 720);
                            parent.add(this); 

                            // 3. Bring back date label and restore full-screen widths
                            Container root = parent;
                            while (root != null && !(root instanceof CustomerDashboard)) {
                                root = root.getParent();
                            }
                            if (root instanceof CustomerDashboard) {
                                CustomerDashboard dashboard = (CustomerDashboard) root;
                                dashboard.getLblDateRn().setVisible(true);
                                
                                // --- AUTOMATICALLY REGISTER THE ORDER IN HISTORY ---
                                java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("MMM d, yyyy · h:mm a");
                                String orderDateStr = java.time.LocalDateTime.now().format(dtf);
                                String deliveryDateStr = java.time.LocalDateTime.now().plusMinutes(30).format(dtf);
                                double finalTotal = placedOrder.calculateFinalTotal(40.0, 0.0);
                                
                                dashboard.getPnlRightHistory().registerOrder(
                                    placedOrder.getRestaurantId(),
                                    "Biñan, Laguna",
                                    placedOrder.getOrderId(),
                                    orderDateStr,
                                    deliveryDateStr,
                                    "Cash on Delivery",
                                    "Pending",
                                    String.format("₱%.2f", finalTotal),
                                    placedOrder.getOrderItems()
                                );
                                // --------------------------------------------------

                                for (Component comp : parent.getComponents()) {
                                    if (comp instanceof CustomerDashboardRestaurant) {
                                        comp.setBounds(0, 0, 1024, 720);
                                    }
                                    if (comp instanceof CustomerDashboardRestaurantMenus) {
                                        comp.setBounds(80, 0, 944, 720);
                                    }
                                }
                            }

                            parent.revalidate();
                            parent.repaint();
                        });

                        parent.revalidate();
                        parent.repaint();
                    }
                }
            }
        }); 

        lblFooterQuote = new JLabel("Total price may vary at checkout", SwingConstants.CENTER);
        lblFooterQuote.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblFooterQuote.setForeground(PURPLE_LIGHT);
        lblFooterQuote.setBounds(14, 610, 260, 20);
        add(lblFooterQuote); 
    }

    // 9. Cart & Restaurant Display Logic
    public void CartDisplay(double subtotal) {
        lblSubtotalBill.setText(String.format("₱%.2f", subtotal));
        ContainerPanel.revalidate();
        ContainerPanel.repaint();
    }  

    
    public void addRestaurantCards(String RestaurantName, String[][] items) {

        JPanel existingRestaurantCard = null;
        JCheckBox existingCheckbox = null;

        for (Component comp : ContainerPanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel card = (JPanel) comp;
                for (Component headerComp : card.getComponents()) {
                    if (headerComp instanceof JPanel) {
                        for (Component subHeader : ((JPanel) headerComp).getComponents()) {
                            if (subHeader instanceof JCheckBox) {
                                JCheckBox cb = (JCheckBox) subHeader;
                                if (cb.getText().equalsIgnoreCase(RestaurantName)) {
                                    existingRestaurantCard = card;
                                    existingCheckbox = cb;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if (existingRestaurantCard != null) break;
        }

        if (existingRestaurantCard != null && existingCheckbox != null) {
            for (String[] item : items) {
                String newItemName = item[0];
                int newQty = Integer.parseInt(item[1]);
                double newUnitPrice = Double.parseDouble(item[2]);

                boolean itemFound = false;

                for (Component c : existingRestaurantCard.getComponents()) {
                    if (c instanceof JPanel) {
                        JPanel row = (JPanel) c;
                        if (row.getComponentCount() > 0 && row.getComponent(0) instanceof JPanel) {
                            JPanel topSubRow = (JPanel) row.getComponent(0);
                            for (Component sub : topSubRow.getComponents()) {
                                if (sub instanceof JLabel) {
                                    JLabel lblName = (JLabel) sub;
                                    if (lblName.getText().equalsIgnoreCase(newItemName)) {
                                        itemFound = true;

                                        for (Component rightComp : topSubRow.getComponents()) {
                                            if (rightComp instanceof JPanel) {
                                                JPanel rightContainer = (JPanel) rightComp;
                                                for (Component rc : rightContainer.getComponents()) {
                                                    if (rc instanceof JPanel) {
                                                        JPanel qtyBox = (JPanel) rc;
                                                        for (Component q : qtyBox.getComponents()) {
                                                            if (q instanceof JLabel) {
                                                                JLabel lblQty = (JLabel) q;
                                                                int currentQty = Integer.parseInt(lblQty.getText());
                                                                int updatedQty = currentQty + newQty;
                                                                lblQty.setText(String.valueOf(updatedQty));

                                                                for (Component priceComp : rightContainer.getComponents()) {
                                                                    if (priceComp instanceof JLabel && priceComp != lblName) {
                                                                        ((JLabel) priceComp).setText(String.format("₱%.2f", newUnitPrice * updatedQty));
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        if (itemFound) break;
                    }
                }

                if (!itemFound) {
                    JPanel newItemRow = createItemRow(existingRestaurantCard, newItemName, newQty, newUnitPrice, "");
                    existingRestaurantCard.add(Box.createRigidArea(new Dimension(0, 6)));
                    existingRestaurantCard.add(newItemRow);

                    Dimension currentDim = existingRestaurantCard.getPreferredSize();
                    int addedHeight = 58 + 6;
                    existingRestaurantCard.setPreferredSize(new Dimension(currentDim.width, currentDim.height + addedHeight));
                    existingRestaurantCard.setMaximumSize(new Dimension(currentDim.width, currentDim.height + addedHeight));
                    existingRestaurantCard.setMinimumSize(new Dimension(currentDim.width, currentDim.height + addedHeight));
                }
            }

            updateCartTotals();
            ContainerPanel.revalidate();
            ContainerPanel.repaint();
            return;
        }


        RoundCheckBox ChkBxStore = new RoundCheckBox(RestaurantName);

        JPanel RestaurantCard = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                boolean isSelected = (ChkBxStore == currentlySelectedRestaurant && ChkBxStore.isSelected());

                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, w, h, 16, 16);

                g2.setColor(isSelected ? YELLOW_ACCENT : PURPLE_LIGHT);
                java.awt.geom.Area cardShape = new java.awt.geom.Area(new java.awt.geom.RoundRectangle2D.Float(0, 0, w, h, 16, 16));
                java.awt.geom.Area leftBar = new java.awt.geom.Area(new java.awt.geom.Rectangle2D.Float(0, 0, isSelected ? 6 : 5, h));
                leftBar.intersect(cardShape);
                g2.fill(leftBar);

                g2.setColor(BORDER_COLOR);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, w - 1, h - 1, 16, 16);

                g2.dispose();
            }
        };

        RestaurantCard.setLayout(new BoxLayout(RestaurantCard, BoxLayout.Y_AXIS));
        RestaurantCard.setOpaque(false);
        RestaurantCard.setBorder(BorderFactory.createEmptyBorder(8, 8, 12, 8));
        RestaurantCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        int calculatedHeight = 45 + (items.length * 64);
        RestaurantCard.setPreferredSize(new Dimension(274, calculatedHeight));
        RestaurantCard.setMaximumSize(new Dimension(274, calculatedHeight));
        RestaurantCard.setMinimumSize(new Dimension(274, calculatedHeight));

        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setOpaque(false);
        headerRow.setMaximumSize(new Dimension(260, 28)); 
        headerRow.setPreferredSize(new Dimension(260, 28));

        ChkBxStore.addActionListener(e -> handleRestaurantSelection(ChkBxStore));
        if (currentlySelectedRestaurant == null) {
            ChkBxStore.setSelected(true);
            currentlySelectedRestaurant = ChkBxStore;
        } else {
            ChkBxStore.setSelected(false);
        }

        headerRow.add(ChkBxStore, BorderLayout.WEST);

        JButton btnRemoveStore = new JButton("🗑 Remove") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 30, 30));
                g2.dispose();
                super.paintComponent(g);
            }
        };    

        btnRemoveStore.setOpaque(false);
        btnRemoveStore.setContentAreaFilled(false);
        btnRemoveStore.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 10));
        btnRemoveStore.setForeground(PURPLE_DARK);
        btnRemoveStore.setBackground(REMOVE_BTN_BG);
        btnRemoveStore.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        btnRemoveStore.setFocusPainted(false);
        btnRemoveStore.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnRemoveStore.addActionListener(e -> {
            if (currentlySelectedRestaurant == ChkBxStore) {
                currentlySelectedRestaurant = null;
            }
            ContainerPanel.remove(RestaurantCard);
            ContainerPanel.revalidate();
            ContainerPanel.repaint();
            updateCartTotals();
        });

        headerRow.add(btnRemoveStore, BorderLayout.EAST);
        RestaurantCard.add(headerRow);

        JSeparator RestaurantSeparator = new JSeparator();
        RestaurantSeparator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        RestaurantSeparator.setAlignmentX(Component.CENTER_ALIGNMENT);
        RestaurantSeparator.setForeground(BORDER_COLOR);

        RestaurantCard.add(Box.createRigidArea(new Dimension(0, 6)));
        RestaurantCard.add(RestaurantSeparator);
        RestaurantCard.add(Box.createRigidArea(new Dimension(0, 6)));

        for (int i = 0; i < items.length; i++) {
            String[] item = items[i];
            String itemName = item[0];
            int qty = Integer.parseInt(item[1]);
            double unitPrice = Double.parseDouble(item[2]);

            JPanel itemRow = createItemRow(RestaurantCard, itemName, qty, unitPrice, "");
            RestaurantCard.add(itemRow);

            if (i < items.length - 1) {
                RestaurantCard.add(Box.createRigidArea(new Dimension(0, 6)));
            }
        } 

        ContainerPanel.add(RestaurantCard);
        Component spacer = Box.createRigidArea(new Dimension(0, 12));
        ((JComponent) spacer).setAlignmentX(Component.LEFT_ALIGNMENT);
        ContainerPanel.add(spacer);

        ContainerPanel.revalidate();
        ContainerPanel.repaint();
    }

    //10. Quantity Controls    
    private JPanel createItemRow(JPanel parentCard, String Name, int InitialQuantity, double UnitPrice, String initialInstructions) {
        JPanel Row = new JPanel();
        Row.setLayout(new BoxLayout(Row, BoxLayout.Y_AXIS));
        Row.setOpaque(false);
        Row.setAlignmentX(Component.CENTER_ALIGNMENT);
        Row.setMaximumSize(new Dimension(260, 58));
        Row.setPreferredSize(new Dimension(260, 58));

        JPanel topSubRow = new JPanel(new BorderLayout(5, 0));
        topSubRow.setOpaque(false);
        topSubRow.setMaximumSize(new Dimension(260, 28));
        topSubRow.setPreferredSize(new Dimension(260, 28));

        JLabel lblName = new JLabel(Name);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblName.setForeground(TEXT_DARK);
        topSubRow.add(lblName, BorderLayout.CENTER);

        JPanel rightContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 0));
        rightContainer.setOpaque(false);
        rightContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));    

        JPanel qtyBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g){
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
                g2.dispose();
            }
        };
        qtyBox.setLayout(new GridBagLayout());
        Dimension boxDim = new Dimension(22, 22);
        qtyBox.setPreferredSize(boxDim);
        qtyBox.setMinimumSize(boxDim);
        qtyBox.setMaximumSize(boxDim);
        qtyBox.setOpaque(false);    

        JLabel lblQuantity = new JLabel(String.valueOf(InitialQuantity), SwingConstants.CENTER);
        lblQuantity.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblQuantity.setForeground(TEXT_DARK);
        qtyBox.add(lblQuantity);

        Dimension btnDim = new Dimension(22, 22);

        JButton btnMinus = new JButton("-"){
            @Override
            protected void paintComponent(Graphics g){
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(PURPLEBTN_LIGHT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnMinus.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnMinus.setForeground(PURPLE_DARK);
        btnMinus.setContentAreaFilled(false);
        btnMinus.setBorderPainted(false);
        btnMinus.setFocusPainted(false);
        btnMinus.setMargin(new Insets(0, 0, 0, 0));
        btnMinus.setPreferredSize(btnDim);
        btnMinus.setCursor(new Cursor(Cursor.HAND_CURSOR));    

        JButton btnPlus = new JButton("+") {
            @Override
            protected void paintComponent(Graphics g){
               Graphics2D g2 = (Graphics2D) g.create();
               g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
               g2.setColor(PURPLE_DARK);
               g2.fillOval(0, 0, getWidth(), getHeight());
               g2.dispose();
               super.paintComponent(g);
            }
        }; 
        btnPlus.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnPlus.setForeground(COLOR_WHITE);
        btnPlus.setContentAreaFilled(false);
        btnPlus.setBorderPainted(false);
        btnPlus.setFocusPainted(false);
        btnPlus.setMargin(new Insets(0, 0, 0, 0));
        btnPlus.setPreferredSize(btnDim);
        btnPlus.setCursor(new Cursor(Cursor.HAND_CURSOR));        

        JLabel lblPrice = new JLabel(String.format("₱%.2f", UnitPrice * InitialQuantity), SwingConstants.RIGHT);
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblPrice.setForeground(PURPLE_DARK);
        lblPrice.setPreferredSize(new Dimension(55, 24));

        btnMinus.addActionListener(e -> {
            int current = Integer.parseInt(lblQuantity.getText());
            current--;
            if (current <= 0) {
                boolean confirmDelete = showCustomConfirmDialog("Confirm Deletion", "Are you sure you want to delete this item?");
                if (confirmDelete) {
                    parentCard.remove(Row);
                    parentCard.revalidate();
                    parentCard.repaint();
                } else {
                    lblQuantity.setText("1");
                    lblPrice.setText(String.format("₱%.2f", UnitPrice * 1));
                }
            } else {
                lblQuantity.setText(String.valueOf(current));
                lblPrice.setText(String.format("₱%.2f", UnitPrice * current));
            }
            updateCartTotals();
        });    

        btnPlus.addActionListener(e -> {
            int current = Integer.parseInt(lblQuantity.getText());
            if (current >= 25) {
                showCustomWarningDialog("Order Limit Reached", "Maximum order limit is 25 items per product.");
                return;
            }
            current++;
            lblQuantity.setText(String.valueOf(current));
            lblPrice.setText(String.format("₱%.2f", UnitPrice * current));
            updateCartTotals();
        });

        rightContainer.add(btnMinus);
        rightContainer.add(Box.createRigidArea(new Dimension(2, 0)));
        rightContainer.add(qtyBox);
        rightContainer.add(Box.createRigidArea(new Dimension(2, 0)));
        rightContainer.add(btnPlus);
        rightContainer.add(Box.createRigidArea(new Dimension(4, 0)));
        rightContainer.add(lblPrice);

        topSubRow.add(rightContainer, BorderLayout.EAST);
        Row.add(topSubRow);
        Row.add(Box.createRigidArea(new Dimension(0, 4)));

        String placeholder = "Add special instructions...";
        JTextField txtInstructions = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        txtInstructions.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        txtInstructions.setOpaque(false);
        txtInstructions.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
        txtInstructions.setMaximumSize(new Dimension(260, 24));
        txtInstructions.setPreferredSize(new Dimension(260, 24));

        if (initialInstructions != null && !initialInstructions.isEmpty()) {
            txtInstructions.setText(initialInstructions);
            txtInstructions.setForeground(TEXT_DARK);
        } else {
            txtInstructions.setText(placeholder);
            txtInstructions.setForeground(SUBTITLE_COLOR);
        }

        txtInstructions.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtInstructions.getText().equals(placeholder)) {
                    txtInstructions.setText("");
                    txtInstructions.setForeground(TEXT_DARK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (txtInstructions.getText().trim().isEmpty()) {
                    txtInstructions.setText(placeholder);
                    txtInstructions.setForeground(SUBTITLE_COLOR);
                }
            }
        });

        Row.add(txtInstructions);
        return Row;
    }   

    //11. Cart Calculations 
    public void updateCartTotals() {
        double selectedTotal = 0.0;
        int selectedItemCount = 0;
        int absoluteTotalItems = 0;
        int activeRestaurantsCount = 0;

        for (Component comp : ContainerPanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel restaurantCard = (JPanel) comp;
                activeRestaurantsCount++;

                JCheckBox cardCheckbox = null;
                for (Component headerComp : restaurantCard.getComponents()) {
                    if (headerComp instanceof JPanel) {
                        for (Component subHeader : ((JPanel) headerComp).getComponents()) {
                            if (subHeader instanceof JCheckBox) {
                                cardCheckbox = (JCheckBox) subHeader;
                                break;
                            }
                        }
                    }
                }

                boolean isSelected = (cardCheckbox != null && cardCheckbox == currentlySelectedRestaurant && cardCheckbox.isSelected());

                for (Component c : restaurantCard.getComponents()) {
                    if (c instanceof JPanel) {
                        JPanel row = (JPanel) c;
                        if (row.getComponentCount() > 0 && row.getComponent(0) instanceof JPanel) {
                            JPanel topSubRow = (JPanel) row.getComponent(0);

                            int rowQty = 0;
                            double rowPrice = 0.0;

                            for (Component sub : topSubRow.getComponents()) {
                                if (sub instanceof JPanel) {
                                    JPanel rightContainer = (JPanel) sub;
                                    for (Component rc : rightContainer.getComponents()) {
                                        if (rc instanceof JPanel) {
                                            JPanel qtyBox = (JPanel) rc;
                                            for (Component q : qtyBox.getComponents()) {
                                                if (q instanceof JLabel) {
                                                    try {
                                                        rowQty = Integer.parseInt(((JLabel) q).getText());
                                                    } catch (NumberFormatException ignored) {}
                                                }
                                            }
                                        } else if (rc instanceof JLabel) {
                                            String text = ((JLabel) rc).getText()
                                                    .replace("₱", "")
                                                    .replace(",", "")
                                                    .trim();
                                            try {
                                                rowPrice = Double.parseDouble(text);
                                            } catch (NumberFormatException ignored) {}
                                        }
                                    }
                                }
                            }

                            absoluteTotalItems += rowQty;

                            if (isSelected) {
                                selectedItemCount += rowQty;
                                selectedTotal += rowPrice;
                            }
                        }
                    }
                }
            }
        }
        
        lblSubtotalCount.setText(selectedItemCount + " item" + (selectedItemCount == 1 ? "" : "s"));
        lblSubtotalBill.setText(String.format("₱%.2f", selectedTotal));

        if (lblCartSubtitle != null) {
            lblCartSubtitle.setText(activeRestaurantsCount + " restaurant" + (activeRestaurantsCount == 1 ? "" : "s") + 
                                    " • " + absoluteTotalItems + " item" + (absoluteTotalItems == 1 ? "" : "s"));
        }
    }   

    //12. CheckBox Selection Handling
    private void handleRestaurantSelection(JCheckBox selectedCheckBox) {
        if (selectedCheckBox.isSelected()) {
            if (currentlySelectedRestaurant != null && currentlySelectedRestaurant != selectedCheckBox) {
                showCustomWarningDialog("Single Restaurant Selection", "You can only checkout one restaurant at a time");
                selectedCheckBox.setSelected(false);
                return;
            }
            currentlySelectedRestaurant = selectedCheckBox;
        } else {
            if (currentlySelectedRestaurant == selectedCheckBox) {
                currentlySelectedRestaurant = null;
            }
        }
        updateCartTotals();
        ContainerPanel.repaint(); 
    }
    
    //13. Custom Warning Dialog        
    private void showCustomWarningDialog(String title, String message) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), title, true);
        dialog.setUndecorated(true);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);                        

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth();
                int h = getHeight();
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, w, h, 16, 16);
                g2.setColor(PURPLE_LIGHT);
                java.awt.geom.Area cardShape = new java.awt.geom.Area(new java.awt.geom.RoundRectangle2D.Float(0, 0, w, h, 16, 16));
                java.awt.geom.Area leftBar = new java.awt.geom.Area(new java.awt.geom.Rectangle2D.Float(0, 0, 6, h));
                leftBar.intersect(cardShape);
                g2.fill(leftBar);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, w - 1, h - 1, 16, 16);
                g2.dispose();
            }
        };
        panel.setLayout(null);
        panel.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(PURPLE_DARK);
        lblTitle.setBounds(25, 16, 250, 20);
        panel.add(lblTitle);

        JLabel lblMsg = new JLabel(message);
        lblMsg.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMsg.setForeground(TEXT_DARK);
        lblMsg.setBounds(25, 42, 250, 30);
        panel.add(lblMsg);

        JButton btnOk = new JButton("OK") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnOk.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnOk.setForeground(COLOR_WHITE);
        btnOk.setBackground(PURPLE_DARK);
        btnOk.setOpaque(false);
        btnOk.setContentAreaFilled(false);
        btnOk.setBorderPainted(false);
        btnOk.setFocusPainted(false);
        btnOk.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnOk.setBounds(190, 95, 75, 30);
        btnOk.addActionListener(e -> dialog.dispose());
        panel.add(btnOk);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }                   
        
    //14. Custom Confirm Dialog        
    private boolean showCustomConfirmDialog(String title, String message) {
        final boolean[] confirmed = {false};

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), title, true);
        dialog.setUndecorated(true);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth();
                int h = getHeight();
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, w, h, 16, 16);
                g2.setColor(PURPLE_LIGHT);
                java.awt.geom.Area cardShape = new java.awt.geom.Area(new java.awt.geom.RoundRectangle2D.Float(0, 0, w, h, 16, 16));
                java.awt.geom.Area leftBar = new java.awt.geom.Area(new java.awt.geom.Rectangle2D.Float(0, 0, 6, h));
                leftBar.intersect(cardShape);
                g2.fill(leftBar);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, w - 1, h - 1, 16, 16);
                g2.dispose();
            }
        };
        panel.setLayout(null);
        panel.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(PURPLE_DARK);
        lblTitle.setBounds(25, 16, 250, 20);
        panel.add(lblTitle);

        JLabel lblMsg = new JLabel(message);
        lblMsg.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMsg.setForeground(TEXT_DARK);
        lblMsg.setBounds(25, 42, 250, 30);
        panel.add(lblMsg);

        JButton btnYes = new JButton("Yes") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnYes.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnYes.setForeground(COLOR_WHITE);
        btnYes.setBackground(PURPLE_DARK);
        btnYes.setOpaque(false);
        btnYes.setContentAreaFilled(false);
        btnYes.setBorderPainted(false);
        btnYes.setFocusPainted(false);
        btnYes.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnYes.setBounds(110, 95, 70, 30);
        btnYes.addActionListener(e -> {
            confirmed[0] = true;
            dialog.dispose();
        });
        panel.add(btnYes);

        JButton btnNo = new JButton("No") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnNo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnNo.setForeground(PURPLE_DARK);
        btnNo.setBackground(PURPLEBTN_LIGHT);
        btnNo.setOpaque(false);
        btnNo.setContentAreaFilled(false);
        btnNo.setBorderPainted(false);
        btnNo.setFocusPainted(false);
        btnNo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNo.setBounds(190, 95, 70, 30);
        btnNo.addActionListener(e -> {
            confirmed[0] = false;
            dialog.dispose();
        });
        panel.add(btnNo);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
        return confirmed[0];
    }

    //15. Round CheckBox Component        
    private class RoundCheckBox extends JCheckBox {
        public RoundCheckBox(String text) {
            super(text);
            setOpaque(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setForeground(PURPLE_DARK);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

            int size = 14;
            int y = (getHeight() - size) / 2;
            int x = 2;

            g2.setColor(COLOR_WHITE);
            g2.fillOval(x, y, size, size);

            g2.setColor(isSelected() ? YELLOW_ACCENT : BORDER_COLOR);
            g2.setStroke(new BasicStroke(isSelected() ? 2f : 1f));
            g2.drawOval(x, y, size, size);

            if (isSelected()) {
                g2.setColor(PURPLE_DARK);
                g2.fillOval(x + 3, y + 3, size - 6, size - 6);
            }

            g2.dispose();

            Graphics2D gText = (Graphics2D) g.create();
            gText.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
            gText.setFont(getFont());
            gText.setColor(getForeground());
            FontMetrics fm = gText.getFontMetrics();
            int textY = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
            gText.drawString(getText(), x + size + 8, textY);
            gText.dispose();
        }
    }

    //16. Generate Order From Selected Restaurant
    public Order generateOrderFromSelection(String customerId) {
        if (currentlySelectedRestaurant == null) {
            return null;
        }

        JPanel targetRestaurantCard = null;
        
        for (Component comp : ContainerPanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel card = (JPanel) comp;
                for (Component headerComp : card.getComponents()) {
                    if (headerComp instanceof JPanel) {
                        for (Component subHeader : ((JPanel) headerComp).getComponents()) {
                            if (subHeader == currentlySelectedRestaurant) {
                                targetRestaurantCard = card;
                                break;
                            }
                        }
                    }
                }
            }
        }

        if (targetRestaurantCard == null) {
            return null;
        }

        List<OrderItem> itemsList = new ArrayList<>();

        for (Component c : targetRestaurantCard.getComponents()) {
            if (c instanceof JPanel) {
                JPanel row = (JPanel) c;
                if (row.getComponentCount() > 0 && row.getComponent(0) instanceof JPanel) {
                    JPanel topSubRow = (JPanel) row.getComponent(0);
                    
                    String itemName = "";
                    int rowQty = 0;
                    double unitPrice = 0.0;
                    String instructions = "";

                    for (Component sub : topSubRow.getComponents()) {
                        if (sub instanceof JLabel) {
                            itemName = ((JLabel) sub).getText();
                        } else if (sub instanceof JPanel) {
                            JPanel rightContainer = (JPanel) sub;
                            for (Component rc : rightContainer.getComponents()) {
                                if (rc instanceof JPanel) {
                                    JPanel qtyBox = (JPanel) rc;
                                    for (Component q : qtyBox.getComponents()) {
                                        if (q instanceof JLabel) {
                                            try {
                                                rowQty = Integer.parseInt(((JLabel) q).getText());
                                            } catch (NumberFormatException ignored) {}
                                        }
                                    }
                                } else if (rc instanceof JLabel) {
                                    String priceText = ((JLabel) rc).getText().replace("₱", "").replace(",", "").trim();
                                    try {
                                        double totalLinePrice = Double.parseDouble(priceText);
                                        if (rowQty > 0) {
                                            unitPrice = totalLinePrice / rowQty;
                                        }
                                    } catch (NumberFormatException ignored) {}
                                }
                            }
                        }
                    }

                    for (Component sub : row.getComponents()) {
                        if (sub instanceof JTextField) {
                            String text = ((JTextField) sub).getText().trim();
                            if (!text.isEmpty() && !text.equals("Add special instructions...")) {
                                instructions = text;
                            }
                        }
                    }
                    if (!itemName.isEmpty() && rowQty > 0) {
                        itemsList.add(new OrderItem(itemName, rowQty, unitPrice, instructions));
                    }
                }
            }
        }
            
        String orderId = "ORD-" + System.currentTimeMillis();
        String status = "PENDING";
        String restaurantName = currentlySelectedRestaurant.getText();

        return new Order(orderId, customerId, restaurantName, itemsList, status);
    }

    public void animateOpen() {
        setBounds(724, 0, 300, 720);
        if (getParent() != null) {
            for (Component comp : getParent().getComponents()) {
                if (comp instanceof CustomerDashboardRestaurant) {
                    comp.setBounds(0, 0, 724, 720);
                }
                if (comp instanceof CustomerDashboardRestaurantMenus) {
                    comp.setBounds(80, 0, 644, 720);
                }
            }
            
            Container root = getParent();
            while (root != null && !(root instanceof CustomerDashboard)) {
                root = root.getParent();
            }
            if (root instanceof CustomerDashboard) {
                ((CustomerDashboard) root).getLblDateRn().setVisible(false);
            }

            getParent().revalidate();
            getParent().repaint();
        }
    }

    public void animateClose() {
        setBounds(1024, 0, 300, 720);
        if (getParent() != null) {
            for (Component comp : getParent().getComponents()) {
                if (comp instanceof CustomerDashboardRestaurant) {
                    comp.setBounds(0, 0, 1024, 720);
                }
                if (comp instanceof CustomerDashboardRestaurantMenus) {
                    comp.setBounds(80, 0, 944, 720);
                }
            }

            Container root = getParent();
            while (root != null && !(root instanceof CustomerDashboard)) {
                root = root.getParent();
            }
            if (root instanceof CustomerDashboard) {
                ((CustomerDashboard) root).getLblDateRn().setVisible(true);
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

    public JButton getBtnCheckout() {
        return btnCheckout;
    }
}