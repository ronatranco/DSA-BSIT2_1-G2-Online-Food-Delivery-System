package GUI;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import Model.Order.OrderData;
import Model.Order.OrderItem;
import Model.Order.OrderStore;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 * icoconnect pa sa View Order Tracker + Implementation of Priority Queue sa
 * whole app
 *
 * @author Mairoh Tocino
 */
public class CustomerDashboardTrackOrder extends JPanel {

    // 1. Colors, Fonts & Components
    private Color PURPLE_DARK = new Color(74, 20, 140);
    private Color YELLOW_ACCENT = new Color(255, 193, 7);
    private Color TEXT_DARK = new Color(30, 30, 30);
    private Color COLOR_WHITE = new Color(255, 255, 255);
    private Color COLOR_MUTED = new Color(120, 120, 120);
    private Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 26);

    private JButton btnExitTrackOrder;
    private DefaultTableModel tableModel;
    private JTable tableOrder;
    private JButton btnReceived;
    private final List<OrderData> shownOrders = new ArrayList<>();

    public CustomerDashboardTrackOrder() {
        // 2. Panel Setup
        setLayout(null);
        setBackground(COLOR_WHITE);
        setBounds(1024, 0, 944, 720);
        setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, YELLOW_ACCENT));

        // 3. Exit Button
        btnExitTrackOrder = new JButton("X");
        btnExitTrackOrder.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnExitTrackOrder.setForeground(YELLOW_ACCENT);
        btnExitTrackOrder.setBounds(715, 30, 50, 30);
        btnExitTrackOrder.setContentAreaFilled(false);
        btnExitTrackOrder.setBorderPainted(false);
        btnExitTrackOrder.setFocusPainted(true);
        add(btnExitTrackOrder);

        // 4. Title
        JLabel lblTitle = new JLabel("Track your order");
        lblTitle.setFont(HEADER_FONT);
        lblTitle.setForeground(PURPLE_DARK);
        lblTitle.setBounds(35, 33, 250, 35);
        add(lblTitle);

        // 5. Table Model
        String[] columnOrder = {"Restaurant Name", "Order Summary", "Total", "Status"};
        tableModel = new DefaultTableModel(columnOrder, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // 6. Table Styling
        tableOrder = new JTable(tableModel);
        tableOrder.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableOrder.setRowHeight(40);
        tableOrder.setSelectionBackground(new Color(237, 227, 255));
        tableOrder.setSelectionForeground(PURPLE_DARK);
        tableOrder.setShowVerticalLines(false);
        tableOrder.setGridColor(new Color(240, 240, 240));

        tableOrder.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableOrder.getTableHeader().setBackground(PURPLE_DARK);
        tableOrder.getTableHeader().setForeground(YELLOW_ACCENT);
        tableOrder.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, YELLOW_ACCENT));

        Dimension headerSize = tableOrder.getTableHeader().getPreferredSize();
        headerSize.height = 40;
        tableOrder.getTableHeader().setPreferredSize(headerSize);

        // 7. Status Color Renderer
        DefaultTableCellRenderer customRenderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);

                if (column == 3) {
                    c.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
                } else {
                    c.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                }

                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(COLOR_WHITE);
                    } else {
                        c.setBackground(new Color(248, 248, 248));
                    }
                    c.setForeground(TEXT_DARK);
                    if (value != null) {
                        String cellText = value.toString();
                        if (cellText.contains("Pending")) {
                            c.setForeground(Color.RED);
                        } else if (cellText.contains("On the Way") || cellText.contains("Preparing") || cellText.contains("Out for Delivery")) {
                            c.setForeground(new Color(218, 165, 32));
                        } else if (cellText.contains("Delivery Complete") || cellText.contains("Delivered")) {
                            c.setForeground(new Color(34, 139, 34));
                        }
                    }
                } else {
                    c.setBackground(table.getSelectionBackground());
                    if (value != null && column == 3) {
                        String cellText = value.toString();
                        if (cellText.contains("Pending")) {
                            c.setForeground(Color.RED);
                        } else if (cellText.contains("On the Way") || cellText.contains("Preparing") || cellText.contains("Out for Delivery")) {
                            c.setForeground(new Color(218, 165, 32));
                        } else if (cellText.contains("Delivery Complete") || cellText.contains("Delivered")) {
                            c.setForeground(new Color(34, 139, 34));
                        }
                    } else {
                        c.setForeground(table.getSelectionForeground());
                    }
                }
                return c;
            }
        };

        for (int i = 0; i < tableOrder.getColumnCount(); i++) {
            tableOrder.getColumnModel().getColumn(i).setCellRenderer(customRenderer);
        }

        // 8. Scroll Pane
        JScrollPane scrollPane = new JScrollPane(tableOrder);
        scrollPane.setBounds(35, 90, 715, 525);
        scrollPane.getViewport().setBackground(COLOR_WHITE);
        scrollPane.setBorder(BorderFactory.createMatteBorder(1, 2, 2, 2, PURPLE_DARK));

        // 8.1 Scrollbar Look
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                thumbColor = new Color(200, 200, 200); 
                trackColor = COLOR_WHITE;              
            }

            // 8.2 Remove Arrows
            @Override
            protected JButton createDecreaseButton(int o) {
                return createEmptyBtn();
            }

            @Override
            protected JButton createIncreaseButton(int o) {
                return createEmptyBtn();
            }

            private JButton createEmptyBtn() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                return b;
            }
        });

        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));

        add(scrollPane);

        // 9. Order Received Button
        btnReceived = new JButton("\u2714  Order Received");
        btnReceived.setFont(new Font("Segoe UI Emoji", Font.BOLD, 15));
        btnReceived.setForeground(PURPLE_DARK);
        btnReceived.setBackground(YELLOW_ACCENT);
        btnReceived.setFocusPainted(false);
        btnReceived.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReceived.setToolTipText("Confirm that a Delivered order has arrived");
        btnReceived.setBounds(35, 630, 715, 42);
        add(btnReceived);

        // 9.1 Selection Listener
        tableOrder.getSelectionModel().addListSelectionListener(ev -> {
            if (!ev.getValueIsAdjusting()) updateReceivedButton();
        });

        // 9.2 Confirm Received
        btnReceived.addActionListener(ev -> confirmReceived());

        // 10. Auto-Refresh
        OrderStore.get().addListener(() -> SwingUtilities.invokeLater(this::refreshOrders));
        refreshOrders();
    }

    // 11. Refresh Orders 
    public void refreshOrders() {
        String keepId = null;
        int sel = tableOrder.getSelectedRow();
        if (sel >= 0 && sel < shownOrders.size()) {
            keepId = shownOrders.get(sel).getOrderId();
        }
        tableModel.setRowCount(0);
        shownOrders.clear();
        int reselect = -1;
        for (OrderData o : OrderStore.get().getActive()) {
            if (o.getOrderId().equals(keepId)) {
                reselect = shownOrders.size();
            }
            shownOrders.add(o);
            tableModel.addRow(new Object[]{
                o.getStoreName(),
                buildSummary(o),
                o.getTotalPaid(),
                o.getStatus()
            });
        }
        if (reselect >= 0) {
            tableOrder.setRowSelectionInterval(reselect, reselect);
        }
        updateReceivedButton();
    }

    // 11.1 Button look
    private void updateReceivedButton() {
        int row = tableOrder.getSelectedRow();
        boolean ready = row >= 0 && row < shownOrders.size()
                && OrderStore.DELIVERED.equalsIgnoreCase(shownOrders.get(row).getStatus());
        btnReceived.setBackground(ready ? YELLOW_ACCENT : new Color(255, 236, 160));
    }

    // 11.1.1 Order Received click
    private void confirmReceived() {
        OrderData target = null;
        int row = tableOrder.getSelectedRow();
        if (row >= 0 && row < shownOrders.size()) {
            target = shownOrders.get(row);
        } else {
            int delivered = 0;
            for (OrderData o : shownOrders) {
                if (OrderStore.DELIVERED.equalsIgnoreCase(o.getStatus())) {
                    target = o;
                    delivered++;
                }
            }
            if (delivered != 1) {
                showTrackDialog("\u2139", "Order Received", null,
                        delivered == 0 ? "None of your orders has been\ndelivered yet."
                                       : "Please select the delivered\norder you received.",
                        "OK", null);
                return;
            }
        }
        if (!OrderStore.DELIVERED.equalsIgnoreCase(target.getStatus())) {
            showTrackDialog("\u23F3", "Not Delivered Yet", target.getOrderId(),
                    "This order is still \"" + target.getStatus() + "\".\nYou can confirm it once it shows Delivered.",
                    "OK", null);
            return;
        }
        boolean yes = showTrackDialog("\u2714", "Order Received?",
                target.getOrderId() + "  \u2022  " + target.getStoreName(),
                "Did your order arrive?\nIt will be moved to your Order History.",
                "Yes, received", "Not yet");
        if (!yes) return;

        if (OrderStore.get().markReceived(target.getOrderId())) {
            showTrackDialog("\u2714", "Thank you!", target.getOrderId(),
                    "Order received.\nYou can find it in your Order History.",
                    "OK", null);
        } else {
            showTrackDialog("\u26A0", "Something Went Wrong", target.getOrderId(),
                    "This order can't be marked as\nreceived right now.",
                    "OK", null);
        }
    }

    // 11.1.2 Custom Dialog 
    private boolean showTrackDialog(String icon, String title, String subtitle, String message,
                                    String yesText, String noText) {
        final boolean[] result = {false};
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner, title, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);
        try {
            dialog.setBackground(new Color(0, 0, 0, 0));   // lets the rounded corners show
        } catch (UnsupportedOperationException ignored) {
        }
        dialog.setSize(360, 215);
        dialog.setLocationRelativeTo(this);

        JPanel panel = buildDialogCard(icon, title, subtitle, message, yesText, noText, yes -> {
            result[0] = yes;
            dialog.dispose();
        });
        dialog.setContentPane(panel);
        dialog.getRootPane().setDefaultButton((JButton) panel.getClientProperty("yesButton"));
        dialog.getRootPane().registerKeyboardAction(e -> dialog.dispose(),
                KeyStroke.getKeyStroke("ESCAPE"), JComponent.WHEN_IN_FOCUSED_WINDOW);
        dialog.setVisible(true);
        return result[0];
    }

    // 11.1.2.1 Card itself
    private JPanel buildDialogCard(String icon, String title, String subtitle, String message,
                                   String yesText, String noText,
                                   java.util.function.Consumer<Boolean> onChoice) {
        final Color CARD_BG = new Color(250, 248, 252);
        final Color BORDER_COLOR = new Color(225, 202, 230);
        final Color PURPLE_LIGHT = new Color(156, 39, 176);
        final Color BTN_LIGHT = new Color(230, 220, 245);

        JPanel panel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth();
                int h = getHeight();
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, w, h, 20, 20);
                java.awt.geom.Area bar = new java.awt.geom.Area(new java.awt.geom.Rectangle2D.Float(0, 0, 7, h));
                bar.intersect(new java.awt.geom.Area(new java.awt.geom.RoundRectangle2D.Float(0, 0, w, h, 20, 20)));
                g2.setColor(PURPLE_LIGHT);
                g2.fill(bar);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, w - 1, h - 1, 20, 20);
                g2.dispose();
            }
        };
        panel.setOpaque(false);

        // Icon badge
        JLabel badge = new JLabel(icon, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(YELLOW_ACCENT);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setFont(new Font("Segoe UI Symbol", Font.BOLD, 20));
        badge.setForeground(PURPLE_DARK);
        badge.setBounds(26, 22, 44, 44);
        panel.add(badge);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblTitle.setForeground(PURPLE_DARK);
        lblTitle.setBounds(84, 22, 260, 24);
        panel.add(lblTitle);

        if (subtitle != null) {
            JLabel lblSub = new JLabel(subtitle);
            lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblSub.setForeground(COLOR_MUTED);
            lblSub.setBounds(84, 46, 260, 18);
            panel.add(lblSub);
        }

        JPanel msgPanel = new JPanel();
        msgPanel.setLayout(new BoxLayout(msgPanel, BoxLayout.Y_AXIS));
        msgPanel.setOpaque(false);
        msgPanel.setBounds(24, 80, 312, 60);
        msgPanel.add(Box.createVerticalGlue());
        for (String line : message.split("\n")) {
            JLabel lbl = new JLabel(line, SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lbl.setForeground(TEXT_DARK);
            lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            msgPanel.add(lbl);
        }
        msgPanel.add(Box.createVerticalGlue());
        panel.add(msgPanel);

        JButton btnYes = makeDialogButton(yesText, PURPLE_DARK, Color.WHITE);
        JButton btnNo = (noText == null) ? null : makeDialogButton(noText, BTN_LIGHT, PURPLE_DARK);
        int y = 158;
        if (btnNo == null) {
            btnYes.setBounds(105, y, 150, 36);
        } else {
            btnYes.setBounds(25, y, 150, 36);
            btnNo.setBounds(185, y, 150, 36);
        }
        btnYes.addActionListener(e -> onChoice.accept(true));
        panel.add(btnYes);
        if (btnNo != null) {
            btnNo.addActionListener(e -> onChoice.accept(false));
            panel.add(btnNo);
        }
        panel.putClientProperty("yesButton", btnYes);
        panel.setSize(360, 215);
        return panel;
    }

    // 11.1.3 Rounded pill button for dialogs
    private JButton makeDialogButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 18, 18));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // 11.2 Order Summary Text
    private String buildSummary(OrderData o) {
        StringBuilder sb = new StringBuilder();
        for (OrderItem it : o.getItems()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(it.getQuantity()).append("x ").append(it.getItemName());
        }
        return sb.toString();
    }

    // 12. Panel Animation
    public void animateOpen() {
        refreshOrders();
        setBounds(200, 0, 824, 720);
    }

    public void animateClose() {
        setBounds(1024, 0, 824, 720);
    }

    // 13. Listeners & Getters
    public void addProfileActions(ActionListener listener) {
        btnExitTrackOrder.addActionListener(listener);

    }

    public JButton getCloseButton() {
        return btnExitTrackOrder;
    }
}