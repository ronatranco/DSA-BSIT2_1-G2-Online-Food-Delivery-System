
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to edit this license
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
    private JLabel lblSubtotal, lblSubtotalValue, lblDeliveryFee, lblDeliveryFeeValue;
    private JLabel lblVoucherDiscount, lblVoucherDiscountValue, lblTotal, lblTotalValue;
    private JButton btnBackToCart, btnPlaceOrder, btnAddressToggle, btnAddAddress;
    private JPanel ContainerPanel, topSeparator, btmseparator, mainCard, contentPanel, contactBox, addressHeader, addressOptionsPanel;
    private JPanel sepSum, sepMop, cardCOD, cardOnline, cardBank, sepVouch, cardFreeDel, cardShopVouch;
    private JPanel sepDeliv, cardDelivery, cardPickup, sepPay, sepPaySub, headerPanel;
    private JScrollPane scrollPane;
    private JScrollBar verticalbar;
    private RoundCheckBox rbCOD, rbOnline, rbBank, chkVoucherFreeDel, chkVoucherShop, chkDelivery, chkPickup;
    private ButtonGroup bgPayment, bgFulfillment;

    // Callbacks & State
    private java.util.function.Consumer<Order> onOrderPlacedCallback;
    private String selectedAddress = "Home: Blk 12 Lot 5, Greenview Subd., Biñan, Laguna";
    private String selectedMOP = "Cash on Delivery (COD)";
    private String selectedDelivery = "Delivery";
    private double orderSubtotal = 0.0;
    private double deliveryFeeAmount = 40.0;
    private double voucherDiscountAmount = 0.0;
    private int addressIdCounter = 0;
    private List<CustomAddressData> customAddresses = new ArrayList<>();
    private Order currentOrder;

    public void setOnOrderPlacedCallback(java.util.function.Consumer<Order> callback) {
        this.onOrderPlacedCallback = callback;
    }
    
    public CustomerDashboardCheckout() {
        this(new Order("ORD-DEFAULT", "CUST-001", "Default User", "09123456789", "City, Province", "Default Restaurant", new ArrayList<>(), "PENDING"));
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
    this.currentOrder = order;

    if (order != null) {
        double subtotal = order.calculateSubtotal();
        int totalCount = order.getTotalItemCount();
        double finalBill = order.calculateFinalTotal(40.0, 20.0);

        this.orderSubtotal = order.calculateSubtotal();
    }

    initCheckoutUI();
}

    private void initCheckoutUI() {
        // 1. Main Panel Setup
        setLayout(null);
        setBackground(COLOR_WHITE);
        setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0, PURPLE_LIGHT));
        setBounds(0, 0, 300, 720);

        logoPanda = new JLabel("🐼", SwingConstants.CENTER);
        logoPanda.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        logoPanda.setBounds(225, 13, 38, 38);
        add(logoPanda);

        lblCheckoutTitle = new JLabel("Checkout");
        lblCheckoutTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblCheckoutTitle.setForeground(PURPLE_DARK);
        lblCheckoutTitle.setBounds(68, 14, 140, 20);
        add(lblCheckoutTitle);

        lblCheckoutSubtitle = new JLabel("Review & complete order");
        lblCheckoutSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCheckoutSubtitle.setForeground(SUBTITLE_COLOR);
        lblCheckoutSubtitle.setBounds(68, 34, 160, 16);
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
        add(btnBackToCart);

        // 4. Separators
        topSeparator = new JPanel();
        topSeparator.setBounds(11, 58, 263, 1);
        topSeparator.setBackground(PURPLE_DARK);
        add(topSeparator);

        btmseparator = new JPanel();
        btmseparator.setBounds(11, 592, 263, 1);
        btmseparator.setBackground(PURPLE_DARK);
        add(btmseparator);
        
        // 5. Container Panel
        ContainerPanel = new JPanel();
        ContainerPanel.setLayout(new BoxLayout(ContainerPanel, BoxLayout.Y_AXIS));
        ContainerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        ContainerPanel.setBackground(COLOR_WHITE);
        ContainerPanel.setOpaque(false);
        ContainerPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        // 6. Scroll Pane
        scrollPane = new JScrollPane(ContainerPanel);
        scrollPane.setBounds(10, 67, 280, 516);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(COLOR_WHITE);
        scrollPane.setBackground(COLOR_WHITE);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        verticalbar = scrollPane.getVerticalScrollBar();
        verticalbar.setUnitIncrement(4);
        verticalbar.setPreferredSize(new Dimension(1, 0));
        add(scrollPane);

        buildCheckoutContent();

        // 7. Place Order Button & Footer
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
        btnPlaceOrder.setBounds(11, 605, 260, 42);
        add(btnPlaceOrder);

        btnPlaceOrder.addActionListener(e -> {
            boolean proceed = showCustomConfirmDialog("Confirm Order", "Are you sure you want to place your order?");
            if (proceed) {
                if (currentOrder != null) {
                    currentOrder.setStatus("CONFIRMED");

                    if (onOrderPlacedCallback != null) {
                        onOrderPlacedCallback.accept(currentOrder);
                    }
                }
                showCustomWarningDialog("Success!", "Order placed successfully! ID: " + (currentOrder != null ? currentOrder.getOrderId() : ""));
            }
        });

        lblFooterQuote = new JLabel("Thank you for choosing GrabPanda!", SwingConstants.CENTER);
        lblFooterQuote.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblFooterQuote.setForeground(PURPLE_LIGHT);
        lblFooterQuote.setBounds(11, 650, 260, 20);
        add(lblFooterQuote);
    }

    // 8. Checkout Information Card & Form Builder
    private void buildCheckoutContent() {
        ContainerPanel.removeAll();
        ContainerPanel.add(CheckoutInformationCard());
        ContainerPanel.revalidate();
        ContainerPanel.repaint();
    }

    private JPanel CheckoutInformationCard() {
        final JPanel[] infoCardHolder = new JPanel[1];
        final String[] activeSelection = {"Home"};

        contentPanel = new JPanel(null);
        contentPanel.setOpaque(false);

        int baseY = 2;
        
        // 8.1 Contact Details Section
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
        contactBox.setBounds(8, baseY, 224, 44);

        String displayName = (currentOrder != null && currentOrder.getCustomerName() != null) ? currentOrder.getCustomerName() : "User";
        lblName = new JLabel(displayName);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblName.setForeground(TEXT_DARK);
        lblName.setBounds(10, 4, 200, 16);

        String displayPhone = (currentOrder != null && currentOrder.getCustomerPhone() != null) ? currentOrder.getCustomerPhone() : "09123456789";
        lblPhone = new JLabel("📞 " + displayPhone);
        lblPhone.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 10));
        lblPhone.setForeground(SUBTITLE_COLOR);
        lblPhone.setBounds(10, 22, 200, 14);

        contactBox.add(lblName);
        contactBox.add(lblPhone);
        contentPanel.add(contactBox);
        baseY += 52;
        
        // 8.2 Saved Addresses Section
        addressHeader = new JPanel(new BorderLayout());
        addressHeader.setOpaque(false);
        addressHeader.setBounds(8, baseY, 224, 20);

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

        // 8.3 Order Summary Section
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
                lblItem.setBounds(38, 0, 110, 20);
                rowPanel.add(lblItem);

                double lineTotal = orderItem.calculateLineTotal();
                itemsSubtotal += lineTotal;

                JLabel lblPrice = new JLabel(String.format("₱%.2f", lineTotal), SwingConstants.RIGHT);
                lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 11));
                lblPrice.setForeground(TEXT_DARK);
                lblPrice.setBounds(148, 0, 76, 20);
                rowPanel.add(lblPrice);

                if (hasInstructions) {
                    JLabel lblInstructions = new JLabel("• " + orderItem.getSpecialInstructions());
                    lblInstructions.setFont(new Font("Segoe UI", Font.ITALIC, 10));
                    lblInstructions.setForeground(SUBTITLE_COLOR);
                    lblInstructions.setBounds(38, 20, 186, 16);
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

        // 8.4 Mode of Payment Section
        sepMop = new JPanel();
        sepMop.setBackground(BORDER_COLOR);
        contentPanel.add(sepMop);

        lblMopTitle = new JLabel("💳 Mode of Payment (MOP)");
        lblMopTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        lblMopTitle.setForeground(PURPLE_DARK);
        contentPanel.add(lblMopTitle);

        final String[] selectedPayment = {"COD"};
        cardCOD = createOptionCard(8, 0, 224, "Cash on Delivery (COD)", true, selectedPayment, "COD");
        rbCOD = (RoundCheckBox) cardCOD.getClientProperty("checkbox");
        contentPanel.add(cardCOD);

        cardOnline = createOptionCard(8, 0, 224, "Online Payment (GCash)", false, selectedPayment, "Online");
        rbOnline = (RoundCheckBox) cardOnline.getClientProperty("checkbox");
        contentPanel.add(cardOnline);

        cardBank = createOptionCard(8, 0, 224, "Bank Transfer", false, selectedPayment, "Bank");
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

        // 8.5 Vouchers Section
        sepVouch = new JPanel();
        sepVouch.setBackground(BORDER_COLOR);
        contentPanel.add(sepVouch);

        lblVouchTitle = new JLabel("🎟️ Vouchers");
        lblVouchTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        lblVouchTitle.setForeground(PURPLE_DARK);
        contentPanel.add(lblVouchTitle);

        final boolean[] freeDelSelected = {false};
        final boolean[] shopVouchSelected = {false};

        cardFreeDel = createOptionCard(8, 0, 224, "Free Delivery", false, new String[]{"none"}, "none");
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
        badgeFreeDel.setBounds(154, 8, 60, 20);
        cardFreeDel.add(badgeFreeDel);
        contentPanel.add(cardFreeDel);

        cardShopVouch = createOptionCard(8, 0, 224, "Shop Voucher", false, new String[]{"none"}, "none");
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
        badgeShopVouch.setBounds(154, 8, 60, 20);
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

        // 8.6 Mode of Delivery Section
        sepDeliv = new JPanel();
        sepDeliv.setBackground(BORDER_COLOR);
        contentPanel.add(sepDeliv);

        lblDelivTitle = new JLabel("🛵 Mode of Delivery");
        lblDelivTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        lblDelivTitle.setForeground(PURPLE_DARK);
        contentPanel.add(lblDelivTitle);

        final String[] fulfillmentSelection = {"Delivery"};
        cardDelivery = createOptionCard(8, 0, 104, "🛵 Delivery", true, fulfillmentSelection, "Delivery");
        chkDelivery = (RoundCheckBox) cardDelivery.getClientProperty("checkbox");
        contentPanel.add(cardDelivery);

        cardPickup = createOptionCard(120, 0, 104, "🏪 Pickup", false, fulfillmentSelection, "Pickup");
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

        // 8.7 Payment Details Section
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

        double initialTotal = orderSubtotal + deliveryFeeAmount - voucherDiscountAmount;
        lblTotalValue = new JLabel(String.format("₱%.2f", Math.max(0, initialTotal)), SwingConstants.RIGHT);
        lblTotalValue.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTotalValue.setForeground(YELLOW_ACCENT);
        contentPanel.add(lblTotalValue);

        refreshLayout[0] = () -> {
            addressOptionsPanel.removeAll();
            int yOffset = 0;

            if (showHome[0]) {
                JPanel cardHome = createAddressCard(0, yOffset, "🏠 Home", "Blk 12 Lot 5, Greenview", "Home", activeSelection, true, () -> {
                    selectedAddress = "Blk 12 Lot 5, Greenview";
                    addressOptionsPanel.repaint();
                }, () -> {
                    showHome[0] = false;
                    if (activeSelection[0].equals("Home")) {
                        activeSelection[0] = showWork[0] ? "Work" : (!customAddresses.isEmpty() ? customAddresses.get(0).key : "");
                    }
                    refreshLayout[0].run();
                });
                addressOptionsPanel.add(cardHome);
                yOffset += 46;
            }

            if (showWork[0]) {
                JPanel cardWork = createAddressCard(0, yOffset, "💼 Work", "PUP Biñan, San Antonio", "Work", activeSelection, true, () -> {
                    selectedAddress = "PUP Biñan, San Antonio";
                    addressOptionsPanel.repaint();
                }, () -> {
                    showWork[0] = false;
                    if (activeSelection[0].equals("Work")) {
                        activeSelection[0] = showHome[0] ? "Home" : (!customAddresses.isEmpty() ? customAddresses.get(0).key : "");
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
                    customAddresses.remove(custom);
                    if (activeSelection[0].equals(custom.key)) {
                        activeSelection[0] = showHome[0] ? "Home" : (showWork[0] ? "Work" : (!customAddresses.isEmpty() ? customAddresses.get(0).key : ""));
                    }
                    refreshLayout[0].run();
                });
                addressOptionsPanel.add(cardCustom);
                yOffset += 46;
            }

            if (customAddresses.size() < 3) {
                btnAddAddress.setBounds(0, yOffset, 135, 24);
                addressOptionsPanel.add(btnAddAddress);
                yOffset += 32;
            }

            addressOptionsPanel.setBounds(8, addressSectionStartY, 224, yOffset);

            int currentY = addressSectionStartY + yOffset + 14;

            sepSum.setBounds(8, currentY, 226, 1);
            currentY += 16;

            lblSummaryTitle.setBounds(8, currentY, 220, 20);
            currentY += 26;

            for (JPanel row : itemRowPanels) {
                boolean hasSubLabel = row.getComponentCount() > 3;
                int currentH = hasSubLabel ? 36 : 20;
                row.setBounds(8, currentY, 224, currentH);
                currentY += currentH + 6;
            }

            lblOrderSubtotalText.setBounds(8, currentY, 110, 20);
            lblOrderSubtotalPrice.setBounds(118, currentY, 114, 20);
            currentY += 30;

            sepMop.setBounds(8, currentY, 226, 1);
            currentY += 16;
            lblMopTitle.setBounds(8, currentY, 220, 20);
            currentY += 26;
            cardCOD.setBounds(8, currentY, 224, 36);
            currentY += 42;
            cardOnline.setBounds(8, currentY, 224, 36);
            currentY += 42;
            cardBank.setBounds(8, currentY, 224, 36);
            currentY += 46;

            sepVouch.setBounds(8, currentY, 226, 1);
            currentY += 16;
            lblVouchTitle.setBounds(8, currentY, 220, 20);
            currentY += 26;
            cardFreeDel.setBounds(8, currentY, 224, 36);
            currentY += 42;
            cardShopVouch.setBounds(8, currentY, 224, 36);
            currentY += 46;

            sepDeliv.setBounds(8, currentY, 226, 1);
            currentY += 16;
            lblDelivTitle.setBounds(8, currentY, 220, 20);
            currentY += 26;
            cardDelivery.setBounds(8, currentY, 104, 36);
            cardPickup.setBounds(120, currentY, 104, 36);
            currentY += 46;

            sepPay.setBounds(8, currentY, 226, 1);
            currentY += 16;
            lblPayDetailsTitle.setBounds(8, currentY, 220, 20);
            currentY += 26;
            lblSubtotal.setBounds(8, currentY, 110, 18);
            lblSubtotalValue.setBounds(118, currentY, 112, 18);
            currentY += 20;
            lblDeliveryFee.setBounds(8, currentY, 110, 18);
            lblDeliveryFeeValue.setBounds(118, currentY, 112, 18);
            currentY += 20;
            lblVoucherDiscount.setBounds(8, currentY, 110, 18);
            lblVoucherDiscountValue.setBounds(118, currentY, 112, 18);
            currentY += 22;
            sepPaySub.setBounds(8, currentY, 222, 1);
            currentY += 10;
            lblTotal.setBounds(8, currentY, 100, 20);
            lblTotalValue.setBounds(108, currentY, 122, 20);
            currentY += 30;

            int finalTotalHeight = currentY + 10;
            contentPanel.setPreferredSize(new Dimension(240, finalTotalHeight));

            if (infoCardHolder[0] != null) {
                int totalCardHeight = finalTotalHeight + 36;
                infoCardHolder[0].setMaximumSize(new Dimension(260, totalCardHeight));
                infoCardHolder[0].setPreferredSize(new Dimension(260, totalCardHeight));
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
                showCustomWarningDialog("Limit Reached", "You can only add up to 3 custom addresses.");
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
        mainCard.setMaximumSize(new Dimension(260, totalCardHeight));
        mainCard.setPreferredSize(new Dimension(260, totalCardHeight));
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
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 8, 10));
        mainCard.add(contentPanel, BorderLayout.CENTER);

        btnAddressToggle.addActionListener(e -> {
            boolean isExpanded = addressOptionsPanel.isVisible();
            addressOptionsPanel.setVisible(!isExpanded);
            btnAddressToggle.setText(isExpanded ? "▲" : "▼");
            refreshLayout[0].run();
        });

        return mainCard;
    }

    // 9. Option Cards Builder
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

                g2.setColor(isSelected ? OPTION_CARD_SELECTED_BG : COLOR_WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.setColor(isSelected ? YELLOW_ACCENT : BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
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

    // 10. Calculations & Total Updates
    public void updatePaymentSummaryLabels() {
        if (lblSubtotalValue == null || lblTotalValue == null) return;

        double subtotal = currentOrder != null ? currentOrder.calculateSubtotal() : 0.0;
        int totalCount = currentOrder != null ? currentOrder.getTotalItemCount() : 0;
        double finalTotal = currentOrder != null ? currentOrder.calculateFinalTotal(deliveryFeeAmount, voucherDiscountAmount) : 0.0;

        lblSubtotalValue.setText(String.format("₱%.2f", subtotal));
        lblDeliveryFeeValue.setText(String.format("₱%.2f", deliveryFeeAmount));
        lblVoucherDiscountValue.setText(String.format("-₱%.2f", voucherDiscountAmount));
        lblTotalValue.setText(String.format("₱%.2f", finalTotal));
    }
    
    // 11. Custom Round CheckBox
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

    // 12. Custom Confirm Dialog
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
        lblTitle.setBounds(25, 16, 250, 20);
        panel.add(lblTitle);

        JLabel lblMsg = new JLabel(message + " (10s)");
        lblMsg.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMsg.setForeground(TEXT_DARK);
        lblMsg.setBounds(25, 42, 260, 30);
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

        final int[] secondsRemaining = {10};
        Timer countdownTimer = new Timer(1000, null);
        countdownTimer.addActionListener(e -> {
            secondsRemaining[0]--;
            if (secondsRemaining[0] > 0) {
                lblMsg.setText(message + " (" + secondsRemaining[0] + "s)");
            } else {
                confirmed[0] = true;
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
        countdownTimer.start();
        dialog.setVisible(true);
        return confirmed[0];
    }

    // 13. Custom Warning Dialog
    private void showCustomWarningDialog(String title, String message) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), title, true);
        dialog.setUndecorated(true);
        dialog.setSize(340, 150);
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
        lblTitle.setBounds(25, 16, 290, 20);
        panel.add(lblTitle);

        JLabel lblMsg = new JLabel(message);
        lblMsg.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMsg.setForeground(TEXT_DARK);
        lblMsg.setBounds(25, 42, 290, 30);
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
        btnOk.setBounds(235, 95, 75, 30);
        btnOk.addActionListener(e -> dialog.dispose());
        panel.add(btnOk);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    // 14. Custom Input Dialog
    private String showCustomInputDialog(String title, String message) {
        final String[] result = {null};

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
        lblTitle.setBounds(20, 14, 260, 20);
        panel.add(lblTitle);

        JLabel lblMsg = new JLabel(message);
        lblMsg.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblMsg.setForeground(TEXT_DARK);
        lblMsg.setBounds(20, 36, 260, 18);
        panel.add(lblMsg);

        JTextField txtInput = new JTextField();
        txtInput.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtInput.setBounds(20, 58, 260, 28);
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
        btnOk.setBounds(205, 102, 75, 30);

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

    // 15. Address Card Component
    private JPanel createAddressCard(int x, int y, String title, String subtitle, String key, String[] activeSelection, boolean isDeletable, Runnable onSelect, Runnable onDelete) {
        JPanel card = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean isSelected = activeSelection[0].equals(key);

                g2.setColor(isSelected ? OPTION_CARD_SELECTED_BG : COLOR_WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.setColor(isSelected ? YELLOW_ACCENT : BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);

                int cx = 12;
                int cy = getHeight() / 2;
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
        card.setBounds(x, y, 224, 40);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 11));
        lblTitle.setForeground(TEXT_DARK);
        lblTitle.setBounds(34, 4, isDeletable ? 140 : 180, 16);
        card.add(lblTitle);

        JLabel lblSub = new JLabel(subtitle);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        lblSub.setForeground(SUBTITLE_COLOR);
        lblSub.setBounds(34, 20, isDeletable ? 140 : 180, 14);
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
            btnDelete.setBounds(200, 11, 17, 18);
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

    public JButton getBtnBackToCart() { return btnBackToCart; }
    public JButton getBtnPlaceOrder() { return btnPlaceOrder; }
}