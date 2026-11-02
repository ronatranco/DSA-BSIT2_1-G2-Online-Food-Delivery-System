package GUI;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 * icoconnect pa sa View Order Tracker + Implementation of Priority Queue sa
 * whole app
 *
 * @author klara
 * @author Mairoh Tocino
 */
public class CustomerDashboardTrackOrder extends JPanel {

    private Color PURPLE_DARK = new Color(74, 20, 140);
    private Color YELLOW_ACCENT = new Color(255, 193, 7);
    private Color TEXT_DARK = new Color(30, 30, 30);
    private Color COLOR_WHITE = new Color(255, 255, 255);
    private Color COLOR_MUTED = new Color(120, 120, 120);
    private Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 26);

    private JButton btnExitTrackOrder;

    public CustomerDashboardTrackOrder() {
        setLayout(null);
        setBackground(COLOR_WHITE);
        setBounds(1024, 0, 944, 720);
        setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, YELLOW_ACCENT));

        btnExitTrackOrder = new JButton("X");
        btnExitTrackOrder.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnExitTrackOrder.setForeground(YELLOW_ACCENT);
        btnExitTrackOrder.setBounds(715, 30, 50, 30);
        btnExitTrackOrder.setContentAreaFilled(false);
        btnExitTrackOrder.setBorderPainted(false);
        btnExitTrackOrder.setFocusPainted(true);
        add(btnExitTrackOrder);

        JLabel lblTitle = new JLabel("Track your order");
        lblTitle.setFont(HEADER_FONT);
        lblTitle.setForeground(PURPLE_DARK);
        lblTitle.setBounds(35, 33, 250, 35);
        add(lblTitle);

        String[] columnOrder = {"Restaurant Name", "Order Summary", "Total", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columnOrder, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        //Sample items added to track order
        tableModel.addRow(new Object[]{"McDollibee", "2pc. McChickenJoy Meal", "\u20B1199.00", "Pending"});
        tableModel.addRow(new Object[]{"McDollibee", "2pc. McChickenJoy Meal", "\u20B1199.00", "On the Way"});
        tableModel.addRow(new Object[]{"McDollibee", "2pc. McChickenJoy Meal", "\u20B1199.00", "Delivery Complete"});

        JTable tableOrder = new JTable(tableModel);
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
                        } else if (cellText.contains("On the Way")) {
                            c.setForeground(new Color(218, 165, 32));
                        } else if (cellText.contains("Delivery Complete")) {
                            c.setForeground(new Color(34, 139, 34));
                        }
                    }
                } else {
                    c.setBackground(table.getSelectionBackground());
                    if (value != null && column == 3) {
                        String cellText = value.toString();
                        if (cellText.contains("Pending")) {
                            c.setForeground(Color.RED);
                        } else if (cellText.contains("On the Way")) {
                            c.setForeground(new Color(218, 165, 32));
                        } else if (cellText.contains("Delivery Complete")) {
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

        JScrollPane scrollPane = new JScrollPane(tableOrder);
        scrollPane.setBounds(35, 90, 715, 525);
        scrollPane.getViewport().setBackground(COLOR_WHITE);
        scrollPane.setBorder(BorderFactory.createMatteBorder(1, 2, 2, 2, PURPLE_DARK));

        //changed scrollbar look
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                thumbColor = new Color(200, 200, 200); 
                trackColor = COLOR_WHITE;              
            }

            // Remove the top and bottom arrows
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

    }

    public void animateOpen() {
        setBounds(200, 0, 824, 720);
    }

    public void animateClose() {
        setBounds(1024, 0, 824, 720);
    }

    public void addProfileActions(ActionListener listener) {
        btnExitTrackOrder.addActionListener(listener);

    }

    public JButton getCloseButton() {
        return btnExitTrackOrder;
    }
}
