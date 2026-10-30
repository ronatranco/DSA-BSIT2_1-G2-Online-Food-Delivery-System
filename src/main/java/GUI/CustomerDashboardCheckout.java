/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import Model.Order;
import Model.Order.OrderItem;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 * @author RJD
 * 
 */
public class CustomerDashboardCheckout extends JPanel {

    // COLORS
    private final Color PURPLE_DARK = new Color(74, 20, 140);
    private final Color PURPLE_LIGHT = new Color(156, 39, 176);
    private final Color YELLOW_ACCENT = new Color(255, 193, 7);
    private final Color YELLOW_BRIGHT = new Color(255, 204, 0);
    private final Color TEXT_DARK = new Color(30, 30, 30);
    private final Color COLOR_WHITE = new Color(255, 255, 255);
    private final Color BORDER_COLOR = new Color(225, 202, 230);
    private final Color CARD_BG = new Color(250, 248, 252);
    private final Color SUBTITLE_COLOR = new Color(130, 120, 150);
    private final Color PURPLEBTN_LIGHT = new Color(230, 220, 245);
    private final Color BTN_BACK_HOVER = new Color(240, 190, 0);
    private final Color BTN_BACK_FG = new Color(140, 70, 0);
    private final Color CONTACT_BOX_BG = new Color(252, 250, 254);
    private final Color OPTION_CARD_SELECTED_BG = new Color(254, 250, 235);
    private final Color ITEM_BADGE_BG = new Color(243, 235, 252);
    private final Color DISCOUNT_TEXT_COLOR = new Color(211, 47, 47);
    private final Color DELETE_BTN_BG = new Color(253, 238, 238);

    // UI Declarations
    private JLabel logoPanda, lblCheckoutTitle, lblCheckoutSubtitle, lblFooterQuote;
    private JLabel lblName, lblPhone, lblAddressTitle, lblSummaryTitle;
    private JLabel lblOrderSubtotalText, lblOrderSubtotalPrice, lblMopTitle, lblVouchTitle;
    private JLabel badgeFreeDel, badgeShopVouch, lblDelivTitle, lblPayDetailsTitle;
    private JLabel lblUrgencyTitle, lblUrgencyFee, lblUrgencyFeeValue;
    private JLabel lblSubtotal, lblSubtotalValue, lblDeliveryFee, lblDeliveryFeeValue;
    private JLabel lblVoucherDiscount, lblVoucherDiscountValue, lblTotal, lblTotalValue;
    private JButton btnBackToCart, btnPlaceOrder, btnAddressToggle, btnAddAddress;
    private JPanel ContainerPanel, topSeparator, btmseparator, mainCard, contentPanel, contactBox, addressHeader, addressOptionsPanel;
    private JPanel sepSum, sepMop, cardCOD, cardOnline, cardBank, sepVouch, cardFreeDel, cardShopVouch;
    private JPanel sepDeliv, cardDelivery, cardPickup, sepPay, sepPaySub, headerPanel;
    private JPanel sepUrgency, cardStandard, cardPriority;
    private JScrollPane scrollPane;
    private JScrollBar verticalbar;
    private RoundCheckBox rbCOD, rbOnline, rbBank, chkVoucherFreeDel, chkVoucherShop, chkDelivery, chkPickup, chkStandard, chkPriority;
    private ButtonGroup bgPayment, bgFulfillment, bgUrgency;

    // Callbacks & State
    private java.util.function.Consumer<Order> onOrderPlacedCallback;
    private String selectedAddress = "";
    private String homeAddress = ""; 
    private String selectedMOP = "Cash on Delivery (COD)";
    private String selectedDelivery = "Delivery";
    private double orderSubtotal = 0.0;
    private double deliveryFeeAmount = 40.0;
    private double voucherDiscountAmount = 0.0;
    private static final double PRIORITY_FEE = 30.0; 
    private double urgencyFeeAmount = 0.0;
    private String selectedUrgency = "Standard";
    private int addressIdCounter = 0;
    private List<CustomAddressData> customAddresses = new ArrayList<>();
    private Order currentOrder;
    private Runnable onBackToCartCallback;
    private boolean lastConfirmTimedOut = false;
    private boolean returnToCartOnBack = true;
    private String presetLocation = null;   
    private String presetMop = null;         

    public void setReturnToCartOnBack(boolean value) {
        this.returnToCartOnBack = value;
    }

    public void setOnOrderPlacedCallback(java.util.function.Consumer<Order> callback) {
        this.onOrderPlacedCallback = callback;
    }
    
    public CustomerDashboardCheckout() {
        this(new Order("ORD-DEFAULT", "CUST-001", "Default Restaurant", new ArrayList<>(), "PENDING"));
    }

    private static class CustomAddressData {
        String key;
        String label;
        String address;
        CustomAddressData(String key, String label, String address) {
            this.key = key;
            this.label = label;
            this.address = address;
        }
    }

    public CustomerDashboardCheckout(Order order) {
        this(order, null, null);
    }

    public CustomerDashboardCheckout(Order order, String presetLocation, String presetMop) {
        this.presetLocation = presetLocation;
        this.presetMop = presetMop;
        this.currentOrder = order;

        if (order != null) {
            this.orderSubtotal = order.calculateSubtotal();
        }

        initCheckoutUI();
    }

    private void initCheckoutUI() {
        // 1. Main Panel  
        setLayout(null);
        setBackground(COLOR_WHITE);
        setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0, PURPLE_LIGHT));
        setBounds(668, 0, 340, 720);

        // 2. Header 
        // 2.1 Logo
        logoPanda = new JLabel("🐼", SwingConstants.CENTER);
        logoPanda.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        logoPanda.setBounds(276, 13, 38, 38);
        add(logoPanda);

        // 2.2 Title
        lblCheckoutTitle = new JLabel("Checkout");
        lblCheckoutTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblCheckoutTitle.setForeground(PURPLE_DARK);
        lblCheckoutTitle.setBounds(68, 14, 180, 20);
        add(lblCheckoutTitle);

        // 2.3 Subtitle
        lblCheckoutSubtitle = new JLabel("Review & complete order");
        lblCheckoutSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCheckoutSubtitle.setForeground(SUBTITLE_COLOR);
        lblCheckoutSubtitle.setBounds(68, 34, 180, 16);
        add(lblCheckoutSubtitle);

        // 3. Back to Cart Button
        btnBackToCart = new JButton() {
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
                g2.drawLine(cx - 4, cy, cx + 4, cy);
                g2.drawLine(cx - 4, cy, cx + 1, cy - 4);
                g2.drawLine(cx - 4, cy, cx + 1, cy + 4);
                g2.dispose();
            }
        };

        btnBackToCart.setOpaque(false);
        btnBackToCart.setContentAreaFilled(false);
        btnBackToCart.setBorderPainted(false);
        btnBackToCart.setFocusPainted(false);
        btnBackToCart.setBackground(YELLOW_BRIGHT);
        btnBackToCart.setForeground(BTN_BACK_FG);
        btnBackToCart.setBounds(12, 14, 45, 38);
        btnBackToCart.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnBackToCart.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBackToCart.setBackground(BTN_BACK_HOVER);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBackToCart.setBackground(YELLOW_BRIGHT);
            }
        });
        btnBackToCart.addActionListener(e -> {
            Container parentContainer = getParent();

            if (onBackToCartCallback != null) {
                onBackToCartCallback.run();
            }

            if (parentContainer != null) {
                if (returnToCartOnBack) {
                    for (Component comp : parentContainer.getComponents()) {
                        if (comp instanceof CustomerDashboardViewCart) {
                            comp.setVisible(true);
                            ((CustomerDashboardViewCart) comp).animateOpen();
                        }
                    }
                }
                setVisible(false);
                parentContainer.revalidate();
                parentContainer.repaint();
            }
        });
        add(btnBackToCart);

        // 4. Separators
        // 4.1 Top Separator
        topSeparator = new JPanel();
        topSeparator.setBounds(10, 58, 320, 1);
        topSeparator.setBackground(PURPLE_DARK);
        add(topSeparator);

        // 4.2 Bottom Separator
        btmseparator = new JPanel();
        btmseparator.setBounds(10, 595, 320, 1);
        btmseparator.setBackground(PURPLE_DARK);
        add(btmseparator);
        
        // 5. Container Panel for ScrollPane
        ContainerPanel = new JPanel();
        ContainerPanel.setLayout(new BoxLayout(ContainerPanel, BoxLayout.Y_AXIS));
        ContainerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        ContainerPanel.setBackground(COLOR_WHITE);
        ContainerPanel.setOpaque(false);

        // 6. Scroll Pane
        scrollPane = new JScrollPane(ContainerPanel);
        scrollPane.setBounds(10, 63, 320, 528);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(COLOR_WHITE);
        scrollPane.setBackground(COLOR_WHITE);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        verticalbar = scrollPane.getVerticalScrollBar();
        verticalbar.setUnitIncrement(16);
        verticalbar.setPreferredSize(new Dimension(0, 0)); 

        add(scrollPane);
        buildCheckoutContent();

        // 7. Place Order Button & Footer
        // 7.1 Place Order Button
        btnPlaceOrder = new JButton("🛍️  PLACE ORDER") {
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

        btnPlaceOrder.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        btnPlaceOrder.setForeground(TEXT_DARK);
        btnPlaceOrder.setBackground(YELLOW_BRIGHT);
        btnPlaceOrder.setContentAreaFilled(false);
        btnPlaceOrder.setBorderPainted(false);
        btnPlaceOrder.setFocusPainted(false);
        btnPlaceOrder.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPlaceOrder.setBounds(10, 602, 320, 40);
        add(btnPlaceOrder);

        // 7.2 Place Order Action
        btnPlaceOrder.addActionListener(e -> {
             String orderName  = (currentOrder != null) ? currentOrder.getCustomerName()  : null;
            String orderPhone = (currentOrder != null) ? currentOrder.getCustomerPhone() : null;

            if (orderName == null || orderName.isBlank() || orderPhone == null || orderPhone.isBlank()) {
                showCustomWarningDialog("Incomplete Profile",
                        "Please set your name and\nmobile number in your Profile\nbefore placing an order.");
                return;
            }
            if (!"Pickup".equals(selectedDelivery) && (selectedAddress == null || selectedAddress.trim().isEmpty())) {
                showCustomWarningDialog("No Address", "Please choose a delivery address. \nSet your Home address in your Profile.");
                return;
            }
            boolean proceed = showCustomConfirmDialog("Confirm Order", "Are you sure you want to\nplace your order?");
            if (!proceed && lastConfirmTimedOut) {      
                recordTimedOutOrder();
                return;
            }
            if (proceed) {
                if (currentOrder != null) {
                    currentOrder.setStatus("CONFIRMED");

                    double finalTotal = currentOrder.calculateFinalTotal(
                            deliveryFeeAmount + urgencyFeeAmount, voucherDiscountAmount);
                    String location = "Pickup".equals(selectedDelivery) ? "Pickup" : selectedAddress;
                    Order.OrderStore.get().place(currentOrder, currentOrder.getRestaurantId(),
                            location, selectedMOP, finalTotal);

                    if (onOrderPlacedCallback != null) {
                        onOrderPlacedCallback.accept(currentOrder);
                    }
                }
                showCustomWarningDialog("Success!", "Order placed successfully!\nID: " + (currentOrder != null ? currentOrder.getOrderId() : ""));
            }
        });

        // 7.3 Footer Quote
        lblFooterQuote = new JLabel("Thank you for choosing GrabPanda!", SwingConstants.CENTER);
        lblFooterQuote.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblFooterQuote.setForeground(PURPLE_LIGHT);
        lblFooterQuote.setBounds(10, 646, 320, 20);
        add(lblFooterQuote);
    }
    // 8. Record of Order Time
    private void recordTimedOutOrder() {
        if (currentOrder == null) return;

        double finalTotal = currentOrder.calculateFinalTotal(
                deliveryFeeAmount + urgencyFeeAmount, voucherDiscountAmount);
        String location = "Pickup".equals(selectedDelivery) ? "Pickup" : selectedAddress;

        Order.OrderStore.get().place(currentOrder, currentOrder.getRestaurantId(),
                location, selectedMOP, finalTotal);
        Order.OrderStore.get().updateStatus(currentOrder.getOrderId(), "Cancelled");

        currentOrder.setOrderId(null);   
    }

    // 9. Checkout Information Card & Form Builder
    private void buildCheckoutContent() {
        ContainerPanel.removeAll();
        ContainerPanel.add(CheckoutInformationCard());
        ContainerPanel.revalidate();
        ContainerPanel.repaint();
    }
    // 10. Checkout Info Card
    private JPanel CheckoutInformationCard() {
        final JPanel[] infoCardHolder = new JPanel[1];
        final String[] activeSelection = {"Home"};

        contentPanel = new JPanel(null);
        contentPanel.setOpaque(false);

        int baseY = 2;
        int innerWidth = 304; 

        homeAddress = CustomerDashboardProfile.getSavedHomeAddress();
        if (homeAddress.isEmpty() && currentOrder != null && currentOrder.getDeliveryAddress() != null) {
            homeAddress = currentOrder.getDeliveryAddress().trim();
        }
        selectedAddress = homeAddress;

        // 10.1 Reorder
        if (presetLocation != null && !presetLocation.trim().isEmpty() && !"Pickup".equals(presetLocation)) {
            String prev = presetLocation.trim();
            if (prev.equals(homeAddress)) {
                activeSelection[0] = "Home";
            } else if (prev.equals("PUP Biñan, San Antonio")) {
                activeSelection[0] = "Work";
                selectedAddress = prev;
            } else {
                String key = "Custom_" + (++addressIdCounter);
                customAddresses.add(new CustomAddressData(key, "Previous", prev));
                activeSelection[0] = key;
                selectedAddress = prev;
            }
        }

        String custName  = (currentOrder != null) ? currentOrder.getCustomerName()  : "";
        String custPhone = (currentOrder != null) ? currentOrder.getCustomerPhone() : "";
        
        // 10.2 Contact Details Section
        contactBox = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CONTACT_BOX_BG);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
            }
        };
        contactBox.setOpaque(false);
        contactBox.setBounds(8, baseY, innerWidth, 44);

        lblName = new JLabel(custName == null || custName.isBlank() ? "No name set (add in Profile)" : custName);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblName.setForeground(TEXT_DARK);
        lblName.setBounds(10, 4, innerWidth - 20, 16);

        lblPhone = new JLabel("📞 " + (custPhone == null || custPhone.isBlank() ? "No number set" : custPhone));
        lblPhone.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 10));
        lblPhone.setForeground(SUBTITLE_COLOR);
        lblPhone.setBounds(10, 22, innerWidth - 20, 14);

        contactBox.add(lblName);
        contactBox.add(lblPhone);
        contentPanel.add(contactBox);
        baseY += 52;
        
        // 10.3 Saved Addresses Section
        addressHeader = new JPanel(new BorderLayout());
        addressHeader.setOpaque(false);
        addressHeader.setBounds(8, baseY, innerWidth, 20);

        lblAddressTitle = new JLabel("📍 Saved Addresses");
        lblAddressTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        lblAddressTitle.setForeground(PURPLE_DARK);
        addressHeader.add(lblAddressTitle, BorderLayout.WEST);

        btnAddressToggle = new JButton("▼");
        btnAddressToggle.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        btnAddressToggle.setForeground(YELLOW_ACCENT);
        btnAddressToggle.setContentAreaFilled(false);
        btnAddressToggle.setBorderPainted(false);
        btnAddressToggle.setFocusPainted(false);
        btnAddressToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addressHeader.add(btnAddressToggle, BorderLayout.EAST);
        contentPanel.add(addressHeader);
        baseY += 24;

        addressOptionsPanel = new JPanel(null);
        addressOptionsPanel.setOpaque(false);
        contentPanel.add(addressOptionsPanel);

        final int addressSectionStartY = baseY;

        sepSum = new JPanel();
        sepSum.setBackground(BORDER_COLOR);
        contentPanel.add(sepSum);

        // 10.4 Order Summary Section
        String restaurantName = (currentOrder != null && currentOrder.getRestaurantId() != null) ? currentOrder.getRestaurantId() : "Restaurant";
        lblSummaryTitle = new JLabel("🛍️ Order Summary (" + restaurantName + ")");
        lblSummaryTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        lblSummaryTitle.setForeground(PURPLE_DARK);
        contentPanel.add(lblSummaryTitle);

        btnAddAddress = new JButton("+ Add New Address") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));
                g2.setColor(YELLOW_ACCENT);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnAddAddress.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btnAddAddress.setForeground(PURPLE_DARK);
        btnAddAddress.setContentAreaFilled(false);
        btnAddAddress.setBorderPainted(false);
        btnAddAddress.setFocusPainted(false);
        btnAddAddress.setCursor(new Cursor(Cursor.HAND_CURSOR));

        final boolean[] showHome = {true};
        final boolean[] showWork = {true};
        final Runnable[] refreshLayout = new Runnable[1];

        final Runnable syncSelectedAddress = () -> {
            String key = activeSelection[0];
            if (key.equals("Home")) {
                selectedAddress = homeAddress;
            } else if (key.equals("Work")) {
                selectedAddress = "PUP Biñan, San Antonio";
            } else {
                selectedAddress = "";
                for (CustomAddressData c : customAddresses) {
                    if (c.key.equals(key)) selectedAddress = c.address;
                }
            }
        };

        double itemsSubtotal = 0.0;
        List<JPanel> itemRowPanels = new ArrayList<>();

        if (currentOrder != null && currentOrder.getOrderItems() != null) {
            for (OrderItem orderItem : currentOrder.getOrderItems()) {
                boolean hasInstructions = orderItem.getSpecialInstructions() != null &&
                                          !orderItem.getSpecialInstructions().trim().isEmpty();

                JPanel rowPanel = new JPanel(null);
                rowPanel.setOpaque(false);

                JLabel badge = new JLabel(orderItem.getQuantity() + "×", SwingConstants.CENTER) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(ITEM_BADGE_BG);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                        g2.dispose();
                        super.paintComponent(g);
                    }
                };
                badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
                badge.setForeground(PURPLE_DARK);
                badge.setBounds(0, 0, 32, 20);
                rowPanel.add(badge);

                JLabel lblItem = new JLabel(orderItem.getItemName());
                lblItem.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                lblItem.setForeground(TEXT_DARK);
                lblItem.setBounds(38, 0, innerWidth - 120, 20);
                rowPanel.add(lblItem);

                double lineTotal = orderItem.calculateLineTotal();
                itemsSubtotal += lineTotal;

                JLabel lblPrice = new JLabel(String.format("₱%.2f", lineTotal), SwingConstants.RIGHT);
                lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 11));
                lblPrice.setForeground(TEXT_DARK);
                lblPrice.setBounds(innerWidth - 80, 0, 80, 20);
                rowPanel.add(lblPrice);

                if (hasInstructions) {
                    Font instrFont = new Font("Segoe UI", Font.ITALIC, 10);
                    FontMetrics fm = getFontMetrics(instrFont);

                    String[] lines = wrapText("• " + orderItem.getSpecialInstructions(), fm, innerWidth - 44);

                    JTextArea lblInstructions = new JTextArea(String.join("\n", lines));
                    lblInstructions.setFont(instrFont);
                    lblInstructions.setForeground(SUBTITLE_COLOR);
                    lblInstructions.setEditable(false);
                    lblInstructions.setFocusable(false);
                    lblInstructions.setOpaque(false);
                    lblInstructions.setBorder(null);
                    lblInstructions.setMargin(new Insets(0, 0, 0, 0));
                    lblInstructions.setBounds(38, 20, innerWidth - 40, lines.length * fm.getHeight() + 4);
                    rowPanel.add(lblInstructions);
                }

                contentPanel.add(rowPanel);
                itemRowPanels.add(rowPanel);
            }
        }

        lblOrderSubtotalText = new JLabel("Subtotal");
        lblOrderSubtotalText.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblOrderSubtotalText.setForeground(YELLOW_ACCENT);
        contentPanel.add(lblOrderSubtotalText);

        lblOrderSubtotalPrice = new JLabel(String.format("₱%.2f", itemsSubtotal), SwingConstants.RIGHT);
        lblOrderSubtotalPrice.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblOrderSubtotalPrice.setForeground(YELLOW_ACCENT);
        contentPanel.add(lblOrderSubtotalPrice);

        // 10.5 Mode of Payment Section
        sepMop = new JPanel();
        sepMop.setBackground(BORDER_COLOR);
        contentPanel.add(sepMop);

        lblMopTitle = new JLabel("💳 Mode of Payment (MOP)");
        lblMopTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        lblMopTitle.setForeground(PURPLE_DARK);
        contentPanel.add(lblMopTitle);

        final String[] selectedPayment = {"COD"};
        cardCOD = createOptionCard(8, 0, innerWidth, "Cash on Delivery (COD)", true, selectedPayment, "COD");
        rbCOD = (RoundCheckBox) cardCOD.getClientProperty("checkbox");
        contentPanel.add(cardCOD);

        cardOnline = createOptionCard(8, 0, innerWidth, "Online Payment (GCash)", false, selectedPayment, "Online");
        rbOnline = (RoundCheckBox) cardOnline.getClientProperty("checkbox");
        contentPanel.add(cardOnline);

        cardBank = createOptionCard(8, 0, innerWidth, "Bank Transfer", false, selectedPayment, "Bank");
        rbBank = (RoundCheckBox) cardBank.getClientProperty("checkbox");
        contentPanel.add(cardBank);

        bgPayment = new ButtonGroup();
        bgPayment.add(rbCOD); bgPayment.add(rbOnline); bgPayment.add(rbBank);

        rbCOD.addActionListener(e -> {
            if (rbCOD.isSelected()) {
                selectedPayment[0] = "COD";
                selectedMOP = "Cash on Delivery (COD)";
                cardCOD.repaint(); cardOnline.repaint(); cardBank.repaint();
            }
        });
        rbOnline.addActionListener(e -> {
            if (rbOnline.isSelected()) {
                selectedPayment[0] = "Online";
                selectedMOP = "Online Payment";
                cardCOD.repaint(); cardOnline.repaint(); cardBank.repaint();
            }
        });
        rbBank.addActionListener(e -> {
            if (rbBank.isSelected()) {
                selectedPayment[0] = "Bank";
                selectedMOP = "Bank Transfer";
                cardCOD.repaint(); cardOnline.repaint(); cardBank.repaint();
            }
        });

        // 10.6 Vouchers Section
        sepVouch = new JPanel();
        sepVouch.setBackground(BORDER_COLOR);
        contentPanel.add(sepVouch);

        lblVouchTitle = new JLabel("🎟️ Vouchers");
        lblVouchTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        lblVouchTitle.setForeground(PURPLE_DARK);
        contentPanel.add(lblVouchTitle);

        final boolean[] freeDelSelected = {false};
        final boolean[] shopVouchSelected = {false};

        cardFreeDel = createOptionCard(8, 0, innerWidth, "Free Delivery", false, new String[]{"none"}, "none");
        chkVoucherFreeDel = (RoundCheckBox) cardFreeDel.getClientProperty("checkbox");
        badgeFreeDel = new JLabel("₱40 OFF", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(YELLOW_ACCENT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badgeFreeDel.setFont(new Font("Segoe UI", Font.BOLD, 9));
        badgeFreeDel.setForeground(PURPLE_DARK);
        badgeFreeDel.setBounds(innerWidth - 68, 8, 60, 20);
        cardFreeDel.add(badgeFreeDel);
        contentPanel.add(cardFreeDel);

        cardShopVouch = createOptionCard(8, 0, innerWidth, "Shop Voucher", false, new String[]{"none"}, "none");
        chkVoucherShop = (RoundCheckBox) cardShopVouch.getClientProperty("checkbox");
        badgeShopVouch = new JLabel("₱20 OFF", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(YELLOW_ACCENT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badgeShopVouch.setFont(new Font("Segoe UI", Font.BOLD, 9));
        badgeShopVouch.setForeground(PURPLE_DARK);
        badgeShopVouch.setBounds(innerWidth - 68, 8, 60, 20);
        cardShopVouch.add(badgeShopVouch);
        contentPanel.add(cardShopVouch);

        chkVoucherFreeDel.addActionListener(e -> {
            freeDelSelected[0] = chkVoucherFreeDel.isSelected();
            deliveryFeeAmount = freeDelSelected[0] ? 0.0 : 40.0;
            cardFreeDel.repaint();
            updatePaymentSummaryLabels();
        });

        chkVoucherShop.addActionListener(e -> {
            shopVouchSelected[0] = chkVoucherShop.isSelected();
            voucherDiscountAmount = shopVouchSelected[0] ? 20.0 : 0.0;
            cardShopVouch.repaint();
            updatePaymentSummaryLabels();
        });

        // 10.7 Mode of Delivery Section
        sepDeliv = new JPanel();
        sepDeliv.setBackground(BORDER_COLOR);
        contentPanel.add(sepDeliv);

        lblDelivTitle = new JLabel("🛵 Mode of Delivery");
        lblDelivTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        lblDelivTitle.setForeground(PURPLE_DARK);
        contentPanel.add(lblDelivTitle);

        final String[] fulfillmentSelection = {"Delivery"};
        int halfWidth = (innerWidth - 6) / 2;
        cardDelivery = createOptionCard(8, 0, halfWidth, "🛵 Delivery", true, fulfillmentSelection, "Delivery");
        chkDelivery = (RoundCheckBox) cardDelivery.getClientProperty("checkbox");
        contentPanel.add(cardDelivery);

        cardPickup = createOptionCard(8 + halfWidth + 6, 0, halfWidth, "🏪 Pickup", false, fulfillmentSelection, "Pickup");
        chkPickup = (RoundCheckBox) cardPickup.getClientProperty("checkbox");
        contentPanel.add(cardPickup);

        bgFulfillment = new ButtonGroup();
        bgFulfillment.add(chkDelivery); bgFulfillment.add(chkPickup);

        chkDelivery.addActionListener(e -> {
            if (chkDelivery.isSelected()) {
                fulfillmentSelection[0] = "Delivery";
                selectedDelivery = "Delivery";
                deliveryFeeAmount = freeDelSelected[0] ? 0.0 : 40.0;
                cardDelivery.repaint(); cardPickup.repaint();
                updatePaymentSummaryLabels();
            }
        });

        chkPickup.addActionListener(e -> {
            if (chkPickup.isSelected()) {
                fulfillmentSelection[0] = "Pickup";
                selectedDelivery = "Pickup";
                deliveryFeeAmount = 0.0;
                cardDelivery.repaint(); cardPickup.repaint();
                updatePaymentSummaryLabels();
            }
        });

        // 10.8  Time Urgency Section
        sepUrgency = new JPanel();
        sepUrgency.setBackground(BORDER_COLOR);
        contentPanel.add(sepUrgency);

        lblUrgencyTitle = new JLabel("⏱️ Time Urgency");
        lblUrgencyTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        lblUrgencyTitle.setForeground(PURPLE_DARK);
        contentPanel.add(lblUrgencyTitle);

        final String[] urgencySelection = {"Standard"};

        cardStandard = createOptionCard(8, 0, innerWidth, "🕒 Standard (30-45 mins)", true, urgencySelection, "Standard");
        chkStandard = (RoundCheckBox) cardStandard.getClientProperty("checkbox");
        cardStandard.add(createPriceBadge("FREE", innerWidth));
        contentPanel.add(cardStandard);

        cardPriority = createOptionCard(8, 0, innerWidth, "⚡ Priority (15-25 mins)", false, urgencySelection, "Priority");
        chkPriority = (RoundCheckBox) cardPriority.getClientProperty("checkbox");
        cardPriority.add(createPriceBadge(String.format("+₱%.0f", PRIORITY_FEE), innerWidth));
        contentPanel.add(cardPriority);

        bgUrgency = new ButtonGroup();
        bgUrgency.add(chkStandard); bgUrgency.add(chkPriority);

        chkStandard.addActionListener(e -> {
            if (chkStandard.isSelected()) {
                urgencySelection[0] = "Standard";
                selectedUrgency = "Standard";
                urgencyFeeAmount = 0.0;
                cardStandard.repaint(); cardPriority.repaint();
                updatePaymentSummaryLabels();
            }
        });

        chkPriority.addActionListener(e -> {
            if (chkPriority.isSelected()) {
                urgencySelection[0] = "Priority";
                selectedUrgency = "Priority";
                urgencyFeeAmount = PRIORITY_FEE;
                cardStandard.repaint(); cardPriority.repaint();
                updatePaymentSummaryLabels();
            }
        });

        // 10.9 Payment Details Section
        sepPay = new JPanel();
        sepPay.setBackground(BORDER_COLOR);
        contentPanel.add(sepPay);

        lblPayDetailsTitle = new JLabel("🧾 Payment Details");
        lblPayDetailsTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        lblPayDetailsTitle.setForeground(PURPLE_DARK);
        contentPanel.add(lblPayDetailsTitle);

        lblSubtotal = new JLabel("Subtotal");
        lblSubtotal.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSubtotal.setForeground(TEXT_DARK);
        contentPanel.add(lblSubtotal);

        lblSubtotalValue = new JLabel(String.format("₱%.2f", orderSubtotal), SwingConstants.RIGHT);
        lblSubtotalValue.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblSubtotalValue.setForeground(TEXT_DARK);
        contentPanel.add(lblSubtotalValue);

        lblDeliveryFee = new JLabel("Delivery Fee");
        lblDeliveryFee.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblDeliveryFee.setForeground(TEXT_DARK);
        contentPanel.add(lblDeliveryFee);

        lblDeliveryFeeValue = new JLabel(String.format("₱%.2f", deliveryFeeAmount), SwingConstants.RIGHT);
        lblDeliveryFeeValue.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblDeliveryFeeValue.setForeground(TEXT_DARK);
        contentPanel.add(lblDeliveryFeeValue);

        lblUrgencyFee = new JLabel("Priority Fee");
        lblUrgencyFee.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblUrgencyFee.setForeground(TEXT_DARK);
        contentPanel.add(lblUrgencyFee);

        lblUrgencyFeeValue = new JLabel(String.format("₱%.2f", urgencyFeeAmount), SwingConstants.RIGHT);
        lblUrgencyFeeValue.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblUrgencyFeeValue.setForeground(TEXT_DARK);
        contentPanel.add(lblUrgencyFeeValue);

        lblVoucherDiscount = new JLabel("Voucher Discount");
        lblVoucherDiscount.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblVoucherDiscount.setForeground(TEXT_DARK);
        contentPanel.add(lblVoucherDiscount);

        lblVoucherDiscountValue = new JLabel(String.format("-₱%.2f", voucherDiscountAmount), SwingConstants.RIGHT);
        lblVoucherDiscountValue.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblVoucherDiscountValue.setForeground(DISCOUNT_TEXT_COLOR);
        contentPanel.add(lblVoucherDiscountValue);

        sepPaySub = new JPanel();
        sepPaySub.setBackground(BORDER_COLOR);
        contentPanel.add(sepPaySub);

        lblTotal = new JLabel("Total");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTotal.setForeground(YELLOW_ACCENT);
        contentPanel.add(lblTotal);

        double initialTotal = orderSubtotal + deliveryFeeAmount + urgencyFeeAmount - voucherDiscountAmount;
        lblTotalValue = new JLabel(String.format("₱%.2f", Math.max(0, initialTotal)), SwingConstants.RIGHT);
        lblTotalValue.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTotalValue.setForeground(YELLOW_ACCENT);
        contentPanel.add(lblTotalValue);

        refreshLayout[0] = () -> {
            addressOptionsPanel.removeAll();
            int yOffset = 0;

            if (showHome[0]) {
                String homeText = homeAddress.isEmpty() ? "No address set. Add it in Profile." : homeAddress;
                JPanel cardHome = createAddressCard(0, yOffset, "🏠 Home", homeText, "Home", activeSelection, false, () -> {
                    selectedAddress = homeAddress;
                    addressOptionsPanel.repaint();
                }, null);
                addressOptionsPanel.add(cardHome);
                yOffset += 46;
            }

            if (showWork[0]) {
                JPanel cardWork = createAddressCard(0, yOffset, "💼 Work", "PUP Biñan, San Antonio", "Work", activeSelection, true, () -> {
                    selectedAddress = "PUP Biñan, San Antonio";
                    addressOptionsPanel.repaint();
                }, () -> {
                    if (!showCustomConfirmDialog("Delete Address", "Are you sure you want to\ndelete this address?", false)) return;
                    showWork[0] = false;
                    if (activeSelection[0].equals("Work")) {
                        activeSelection[0] = showHome[0] ? "Home" : (!customAddresses.isEmpty() ? customAddresses.get(0).key : "");
                        syncSelectedAddress.run();
                    }
                    refreshLayout[0].run();
                });
                addressOptionsPanel.add(cardWork);
                yOffset += 46;
            }

            for (CustomAddressData custom : customAddresses) {
                JPanel cardCustom = createAddressCard(0, yOffset, "📍 " + custom.label, custom.address, custom.key, activeSelection, true, () -> {
                    selectedAddress = custom.address;
                    addressOptionsPanel.repaint();
                }, () -> {
                    if (!showCustomConfirmDialog("Delete Address", "Are you sure you want to\ndelete this address?", false)) return;
                    customAddresses.remove(custom);
                    if (activeSelection[0].equals(custom.key)) {
                        activeSelection[0] = showHome[0] ? "Home" : (showWork[0] ? "Work" : (!customAddresses.isEmpty() ? customAddresses.get(0).key : ""));
                        syncSelectedAddress.run();
                    }
                    refreshLayout[0].run();
                });
                addressOptionsPanel.add(cardCustom);
                yOffset += 46;
            }

            if (customAddresses.size() < 3) {
                btnAddAddress.setBounds(0, yOffset, 140, 24);
                addressOptionsPanel.add(btnAddAddress);
                yOffset += 32;
            }

            addressOptionsPanel.setBounds(8, addressSectionStartY, innerWidth, yOffset);

            int currentY = addressSectionStartY + (addressOptionsPanel.isVisible() ? yOffset : 0) + 14;

            sepSum.setBounds(8, currentY, innerWidth, 1);
            currentY += 16;

            lblSummaryTitle.setBounds(8, currentY, innerWidth, 20);
            currentY += 26;

            for (JPanel row : itemRowPanels) {
                boolean hasSubLabel = row.getComponentCount() > 3;
                int currentH = hasSubLabel ? 20 + row.getComponent(3).getHeight() : 20;   
                row.setBounds(8, currentY, innerWidth, currentH);
                currentY += currentH + 6;
            }

            lblOrderSubtotalText.setBounds(8, currentY, 150, 20);
            lblOrderSubtotalPrice.setBounds(innerWidth - 112, currentY, 120, 20);
            currentY += 30;

            sepMop.setBounds(8, currentY, innerWidth, 1);
            currentY += 16;
            lblMopTitle.setBounds(8, currentY, innerWidth, 20);
            currentY += 26;
            cardCOD.setBounds(8, currentY, innerWidth, 36);
            currentY += 42;
            cardOnline.setBounds(8, currentY, innerWidth, 36);
            currentY += 42;
            cardBank.setBounds(8, currentY, innerWidth, 36);
            currentY += 46;

            sepVouch.setBounds(8, currentY, innerWidth, 1);
            currentY += 16;
            lblVouchTitle.setBounds(8, currentY, innerWidth, 20);
            currentY += 26;
            cardFreeDel.setBounds(8, currentY, innerWidth, 36);
            currentY += 42;
            cardShopVouch.setBounds(8, currentY, innerWidth, 36);
            currentY += 46;

            sepDeliv.setBounds(8, currentY, innerWidth, 1);
            currentY += 16;
            lblDelivTitle.setBounds(8, currentY, innerWidth, 20);
            currentY += 26;
            cardDelivery.setBounds(8, currentY, halfWidth, 36);
            cardPickup.setBounds(8 + halfWidth + 6, currentY, halfWidth, 36);
            currentY += 46;

            sepUrgency.setBounds(8, currentY, innerWidth, 1);
            currentY += 16;
            lblUrgencyTitle.setBounds(8, currentY, innerWidth, 20);
            currentY += 26;
            cardStandard.setBounds(8, currentY, innerWidth, 36);
            currentY += 42;
            cardPriority.setBounds(8, currentY, innerWidth, 36);
            currentY += 46;

            sepPay.setBounds(8, currentY, innerWidth, 1);
            currentY += 16;
            lblPayDetailsTitle.setBounds(8, currentY, innerWidth, 20);
            currentY += 26;
            lblSubtotal.setBounds(8, currentY, 150, 18);
            lblSubtotalValue.setBounds(innerWidth - 112, currentY, 120, 18);
            currentY += 20;
            lblDeliveryFee.setBounds(8, currentY, 150, 18);
            lblDeliveryFeeValue.setBounds(innerWidth - 112, currentY, 120, 18);
            currentY += 20;
            lblUrgencyFee.setBounds(8, currentY, 150, 18);
            lblUrgencyFeeValue.setBounds(innerWidth - 112, currentY, 120, 18);
            currentY += 20;
            lblVoucherDiscount.setBounds(8, currentY, 150, 18);
            lblVoucherDiscountValue.setBounds(innerWidth - 112, currentY, 120, 18);
            currentY += 22;
            sepPaySub.setBounds(8, currentY, innerWidth, 1);
            currentY += 10;
            lblTotal.setBounds(8, currentY, 150, 20);
            lblTotalValue.setBounds(innerWidth - 112, currentY, 120, 20);
            currentY += 30;

            int finalTotalHeight = currentY + 10;
            contentPanel.setPreferredSize(new Dimension(innerWidth + 16, finalTotalHeight));

            if (infoCardHolder[0] != null) {
                int totalCardHeight = finalTotalHeight + 36;
                infoCardHolder[0].setMaximumSize(new Dimension(innerWidth + 16, totalCardHeight));
                infoCardHolder[0].setPreferredSize(new Dimension(innerWidth + 16, totalCardHeight));
            }

            addressOptionsPanel.revalidate();
            addressOptionsPanel.repaint();
            contentPanel.revalidate();
            contentPanel.repaint();
            ContainerPanel.revalidate();
            ContainerPanel.repaint();
        };

        btnAddAddress.addActionListener(e -> {
            if (customAddresses.size() >= 3) {
                showCustomWarningDialog("Limit Reached", "You can only add up to 3\ncustom addresses.");
                return;
            }

            String customLabel = showCustomInputDialog("Address Label", "Enter label (e.g., Gym, Apartment):");
            if (customLabel == null || customLabel.trim().isEmpty()) return;

            String newAddress = showCustomInputDialog("Address Details", "Enter full address details:");
            if (newAddress != null && !newAddress.trim().isEmpty()) {
                String uniqueKey = "Custom_" + (++addressIdCounter);
                activeSelection[0] = uniqueKey;
                selectedAddress = newAddress.trim();

                customAddresses.add(new CustomAddressData(uniqueKey, customLabel.trim(), newAddress.trim()));
                refreshLayout[0].run();
            }
        });

        refreshLayout[0].run();

        if ("Pickup".equals(presetLocation)) {
            chkPickup.doClick();
        }
        if (presetMop != null) {
            String m = presetMop.toLowerCase();
            if (m.contains("online") || m.contains("gcash")) {
                rbOnline.doClick();
            } else if (m.contains("bank")) {
                rbBank.doClick();
            }
        }

        mainCard = new JPanel() {
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
                java.awt.geom.Area leftBar = new java.awt.geom.Area(new java.awt.geom.Rectangle2D.Float(0, 0, 5, h));
                leftBar.intersect(cardShape);
                g2.fill(leftBar);

                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, w - 1, h - 1, 16, 16);
                g2.dispose();
            }
        };
        mainCard.setLayout(new BorderLayout());
        mainCard.setOpaque(false);

        int totalCardHeight = contentPanel.getPreferredSize().height + 36;
        mainCard.setMaximumSize(new Dimension(innerWidth + 16, totalCardHeight));
        mainCard.setPreferredSize(new Dimension(innerWidth + 16, totalCardHeight));
        mainCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoCardHolder[0] = mainCard;

        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 6, 10));

        JLabel lblTitle = new JLabel("📋 Checkout Information");
        lblTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        lblTitle.setForeground(PURPLE_DARK);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        mainCard.add(headerPanel, BorderLayout.NORTH);

        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 8));
        mainCard.add(contentPanel, BorderLayout.CENTER);

        btnAddressToggle.addActionListener(e -> {
            boolean isExpanded = addressOptionsPanel.isVisible();
            addressOptionsPanel.setVisible(!isExpanded);
            btnAddressToggle.setText(isExpanded ? "▲" : "▼");
            refreshLayout[0].run();
        });

        return mainCard;
    }

    // 11. Option Cards Builder
    // 11.1 Option Card
    private JPanel createOptionCard(int x, int y, int width, String text, boolean defaultSelected, String[] tracker, String key) {
        JPanel card = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean isSelected = tracker.length > 1 ? tracker[0].equals(key) : false;
                RoundCheckBox rb = (RoundCheckBox) getClientProperty("checkbox");
                if (rb != null && tracker.length <= 1) {
                    isSelected = rb.isSelected();
                }

                int w = getWidth();
                int h = getHeight();
                g2.setColor(isSelected ? OPTION_CARD_SELECTED_BG : COLOR_WHITE);
                g2.fillRoundRect(0, 0, w - 1, h - 1, 10, 10);
                g2.setColor(isSelected ? YELLOW_ACCENT : BORDER_COLOR);
                g2.drawRoundRect(0, 0, w - 1, h - 1, 10, 10);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBounds(x, y, width, 36);

        RoundCheckBox rb = new RoundCheckBox(text);
        rb.setSelected(defaultSelected);
        rb.setBounds(6, 8, width - 14, 20);
        card.add(rb);
        card.putClientProperty("checkbox", rb);

        rb.addItemListener(e -> card.repaint());
        return card;
    }

    // 11.2 Price Badge 
    private JLabel createPriceBadge(String text, int cardWidth) {
        JLabel badge = new JLabel(text, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(YELLOW_ACCENT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setFont(new Font("Segoe UI", Font.BOLD, 9));
        badge.setForeground(PURPLE_DARK);
        badge.setBounds(cardWidth - 68, 8, 60, 20);
        return badge;
    }

    // 11.3 Wrap Text Helper 
    private String[] wrapText(String text, FontMetrics fm, int maxWidth) {
        List<String> lines = new ArrayList<>();
        StringBuilder line = new StringBuilder();

        for (String word : text.trim().split("\\s+")) {
            while (fm.stringWidth(word) > maxWidth) {
                if (line.length() > 0) {
                    lines.add(line.toString());
                    line.setLength(0);
                }
                int cut = 1;
                while (cut < word.length() && fm.stringWidth(word.substring(0, cut + 1)) <= maxWidth) {
                    cut++;
                }
                lines.add(word.substring(0, cut));
                word = word.substring(cut);
            }

            String test = (line.length() == 0) ? word : line + " " + word;
            if (fm.stringWidth(test) <= maxWidth) {
                line.setLength(0);
                line.append(test);
            } else {
                lines.add(line.toString());
                line.setLength(0);
                line.append(word);
            }
        }
        if (line.length() > 0) lines.add(line.toString());
        return lines.toArray(new String[0]);
    }

    // 12. Calculations & Total Updates
    public void updatePaymentSummaryLabels() {
        if (lblSubtotalValue == null || lblTotalValue == null) return;

        double subtotal = currentOrder != null ? currentOrder.calculateSubtotal() : 0.0;
        int totalCount = currentOrder != null ? currentOrder.getTotalItemCount() : 0;
        double finalTotal = currentOrder != null ? currentOrder.calculateFinalTotal(deliveryFeeAmount + urgencyFeeAmount, voucherDiscountAmount) : 0.0;

        lblSubtotalValue.setText(String.format("₱%.2f", subtotal));
        lblDeliveryFeeValue.setText(String.format("₱%.2f", deliveryFeeAmount));
        lblUrgencyFeeValue.setText(String.format("₱%.2f", urgencyFeeAmount));
        lblVoucherDiscountValue.setText(String.format("-₱%.2f", voucherDiscountAmount));
        lblTotalValue.setText(String.format("₱%.2f", finalTotal));
    }
    
    // 13. Custom Round CheckBox
    private class RoundCheckBox extends JCheckBox {
        public RoundCheckBox(String text) {
            super(text);
            initCheckbox();
        }

        private void initCheckbox() {
            setOpaque(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setFont(new Font("Segoe UI Emoji", Font.BOLD, 11));
            setForeground(TEXT_DARK);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

            int size = 13;
            int x = 8;
            int y = (getHeight() - size) / 2;

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

            int textX = x + size + 6;
            int textY = ((getHeight() - fm.getHeight()) / 2 + fm.getAscent());
            gText.drawString(getText(), textX, textY);
            gText.dispose();
        }
    }

    // 14. Wrapped Message Helper 
    private void addWrappedMessage(JPanel msgPanel, String message) {
        Font msgFont = new Font("Segoe UI", Font.PLAIN, 12);
        FontMetrics fm = msgPanel.getFontMetrics(msgFont);
        int maxWidth = 240;

        for (String line : message.split("\n")) {
            StringBuilder current = new StringBuilder();
            for (String word : line.trim().split(" ")) {
                String test = current.length() == 0 ? word : current + " " + word;
                if (fm.stringWidth(test) > maxWidth && current.length() > 0) {
                    JLabel lbl = new JLabel(current.toString(), SwingConstants.CENTER);
                    lbl.setFont(msgFont);
                    lbl.setForeground(TEXT_DARK);
                    lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
                    msgPanel.add(lbl);
                    current = new StringBuilder(word);
                } else {
                    current = new StringBuilder(test);
                }
            }
            JLabel lbl = new JLabel(current.toString(), SwingConstants.CENTER);
            lbl.setFont(msgFont);
            lbl.setForeground(TEXT_DARK);
            lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            msgPanel.add(lbl);
        }
    }

    // 15. Custom Confirm Dialog 
    // 15.1 Confirm Dialog
    private boolean showCustomConfirmDialog(String title, String message) {
        return showCustomConfirmDialog(title, message, true);
    }

    // 15.2 With Countdown
    private boolean showCustomConfirmDialog(String title, String message, boolean useCountdown) {
        final boolean[] confirmed = {false};
        lastConfirmTimedOut = false;

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), title, true);
        dialog.setUndecorated(true);
        dialog.setSize(280, 140);
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

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(PURPLE_DARK);
        lblTitle.setBounds(15, 12, 250, 20);
        panel.add(lblTitle);

        JPanel msgPanel = new JPanel();
        msgPanel.setLayout(new BoxLayout(msgPanel, BoxLayout.Y_AXIS));
        msgPanel.setOpaque(false);
        msgPanel.setBounds(15, 36, 250, 56);

        msgPanel.add(Box.createVerticalGlue());
        addWrappedMessage(msgPanel, message);

        // 15.3 Countdown line 
        JLabel lblCountdown = new JLabel("(20s)", SwingConstants.CENTER);
        lblCountdown.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCountdown.setForeground(TEXT_DARK);
        lblCountdown.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (useCountdown) {
            msgPanel.add(lblCountdown);
        }
        msgPanel.add(Box.createVerticalGlue());
        panel.add(msgPanel);

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
        btnYes.setBounds(40, 100, 90, 28);

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
        btnNo.setBounds(145, 100, 90, 28);

        final int[] secondsRemaining = {20};
        final boolean[] timedOut = {false};
        Timer countdownTimer = new Timer(1000, null);
        countdownTimer.addActionListener(e -> {
            secondsRemaining[0]--;
            if (secondsRemaining[0] > 0) {
                lblCountdown.setText("(" + secondsRemaining[0] + "s)");
            } else {
                timedOut[0] = true;
                confirmed[0] = false;
                countdownTimer.stop();
                dialog.dispose();
            }
        });

        btnYes.addActionListener(e -> {
            countdownTimer.stop();
            confirmed[0] = true;
            dialog.dispose();
        });
        panel.add(btnYes);

        btnNo.addActionListener(e -> {
            countdownTimer.stop();
            confirmed[0] = false;
            dialog.dispose();
        });
        panel.add(btnNo);

        dialog.setContentPane(panel);
        dialog.getRootPane().registerKeyboardAction(
            e -> {
                countdownTimer.stop();
                confirmed[0] = false;
                dialog.dispose();
            },
            KeyStroke.getKeyStroke("ESCAPE"),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        if (useCountdown) {
            countdownTimer.start();
        }
        dialog.setVisible(true);

        if (timedOut[0]) {
            lastConfirmTimedOut = true;
            showCustomWarningDialog("Order Cancelled", "Time's up!\nOrder automatically cancelled.");
            }
        return confirmed[0];
    }

    // 16. Custom Warning Dialog 
    private void showCustomWarningDialog(String title, String message) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), title, true);
        dialog.setUndecorated(true);
        dialog.setSize(280, 140);
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

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(PURPLE_DARK);
        lblTitle.setBounds(15, 12, 250, 20);
        panel.add(lblTitle);

        JPanel msgPanel = new JPanel();
        msgPanel.setLayout(new BoxLayout(msgPanel, BoxLayout.Y_AXIS));
        msgPanel.setOpaque(false);
        msgPanel.setBounds(15, 36, 250, 56);

        msgPanel.add(Box.createVerticalGlue());
        addWrappedMessage(msgPanel, message);
        msgPanel.add(Box.createVerticalGlue());
        panel.add(msgPanel);

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
        btnOk.setBounds(100, 100, 80, 28);
        btnOk.addActionListener(e -> dialog.dispose());
        panel.add(btnOk);

        dialog.setContentPane(panel);
        dialog.getRootPane().registerKeyboardAction(
            e -> dialog.dispose(),
            KeyStroke.getKeyStroke("ESCAPE"),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
        dialog.setVisible(true);
    }

    // 17. Custom Input Dialog
    private String showCustomInputDialog(String title, String message) {
        final String[] result = {null};

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), title, true);
        dialog.setUndecorated(true);
        dialog.setSize(280, 140);
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
                g2.setColor(YELLOW_ACCENT);
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
        lblTitle.setBounds(20, 14, 240, 20);
        panel.add(lblTitle);

        JLabel lblMsg = new JLabel(message);
        lblMsg.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblMsg.setForeground(TEXT_DARK);
        lblMsg.setBounds(20, 36, 240, 18);
        panel.add(lblMsg);

        JTextField txtInput = new JTextField();
        txtInput.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtInput.setBounds(20, 58, 240, 28);
        panel.add(txtInput);

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
        btnOk.setBounds(185, 98, 75, 30);

        btnOk.addActionListener(e -> {
            result[0] = txtInput.getText();
            dialog.dispose();
        });
        panel.add(btnOk);

        txtInput.addActionListener(e -> {
            result[0] = txtInput.getText();
            dialog.dispose();
        });

        dialog.setContentPane(panel);
        dialog.setVisible(true);
        return result[0];
    }

    // 18. Address Card Component
    private JPanel createAddressCard(int x, int y, String title, String subtitle, String key, String[] activeSelection, boolean isDeletable, Runnable onSelect, Runnable onDelete) {
        JPanel card = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                boolean isSelected = activeSelection[0].equals(key);

                g2.setColor(isSelected ? OPTION_CARD_SELECTED_BG : COLOR_WHITE);
                g2.fillRoundRect(0, 0, w - 1, h - 1, 10, 10);
                g2.setColor(isSelected ? YELLOW_ACCENT : BORDER_COLOR);
                g2.drawRoundRect(0, 0, w - 1, h - 1, 10, 10);

                int cx = 12;
                int cy = h / 2;
                int size = 13;

                g2.setColor(COLOR_WHITE);
                g2.fillOval(cx, cy - size / 2, size, size);
                g2.setColor(isSelected ? YELLOW_ACCENT : BORDER_COLOR);
                g2.setStroke(new BasicStroke(isSelected ? 2f : 1f));
                g2.drawOval(cx, cy - size / 2, size, size);

                if (isSelected) {
                    g2.setColor(PURPLE_DARK);
                    g2.fillOval(cx + 3, (cy - size / 2) + 3, size - 6, size - 6);
                }
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBounds(x, y, 304, 40);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 11));
        lblTitle.setForeground(TEXT_DARK);
        lblTitle.setBounds(34, 4, isDeletable ? 220 : 260, 16);
        card.add(lblTitle);

        JLabel lblSub = new JLabel(subtitle);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        lblSub.setForeground(SUBTITLE_COLOR);
        lblSub.setToolTipText(subtitle);
        lblSub.setBounds(34, 20, isDeletable ? 220 : 260, 14);
        card.add(lblSub);

        if (isDeletable && onDelete != null) {
            JButton btnDelete = new JButton("X") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(DELETE_BTN_BG);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 7));
            btnDelete.setForeground(DISCOUNT_TEXT_COLOR);
            btnDelete.setContentAreaFilled(false);
            btnDelete.setBorderPainted(false);
            btnDelete.setFocusPainted(false);
            btnDelete.setMargin(new Insets(0, 0, 0, 0));
            btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnDelete.setBounds(278, 11, 18, 18);
            btnDelete.addActionListener(e -> onDelete.run());
            card.add(btnDelete);
        }

        Runnable selectAction = () -> {
            activeSelection[0] = key;
            if (onSelect != null) onSelect.run();
        };

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) { selectAction.run(); }
        });
        lblTitle.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) { selectAction.run(); }
        });
        lblSub.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) { selectAction.run(); }
        });

        return card;
    }

    // 19. Getters & Callback Setters
    public JButton getBtnBackToCart() { return btnBackToCart; }
    public JButton getBtnPlaceOrder() { return btnPlaceOrder; }
    
    public void setOnBackToCartCallback(Runnable callback) {
        this.onBackToCartCallback = callback;
    }
}