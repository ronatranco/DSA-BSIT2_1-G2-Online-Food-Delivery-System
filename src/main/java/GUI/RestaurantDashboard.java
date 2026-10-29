/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;
/**
 *
 * @author krist
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.time.*;
import java.time.format.TextStyle;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;
import Model.Restaurant;
import Model.Restaurant.MoneyFormat;

public class RestaurantDashboard extends JPanel {
   static Restaurant DATA = Restaurant.get(Restaurant.Brand.MCDOLLIBEE);
   private Restaurant data = DATA;
   private JLabel welcome = new JLabel();
   private UIStyle.Chip statusChip = new UIStyle.Chip();
   private JPanel mainPanel = new JPanel(new BorderLayout());
   private JPanel content = new JPanel();
   private JButton logoutBtn;

   public RestaurantDashboard(MainFrame frame) {
      setLayout(new BorderLayout());
      mainPanel.setBackground(UIStyle.BG_LIGHT);
      mainPanel.add(buildHeader(), BorderLayout.NORTH);
      mainPanel.add(content, BorderLayout.CENTER);
      add(buildSidebar(), BorderLayout.WEST);
      add(mainPanel, BorderLayout.CENTER);
      data.addListener(this::refresh);
      data.startScheduler();
      refresh();
   }

   public JButton getBtnLogout() {
      return logoutBtn;
   }

   private void refresh() {
      welcome.setText("Welcome back, " + data.restaurant.name + "!");
      Restaurant.StoreStatus status = data.getStoreStatus();
      statusChip.set("\u25CF  " + status.getLabel(), UIStyle.storeColor(status));
      mainPanel.remove(content);
      content = buildContent();
      mainPanel.add(content, BorderLayout.CENTER);
      mainPanel.revalidate();
      mainPanel.repaint();
   }

   private JPanel buildSidebar() {
      JPanel sidebar = new JPanel(new BorderLayout());
      sidebar.setPreferredSize(new Dimension(220, 720));
      sidebar.setBackground(UIStyle.PURPLE_DARK);
      sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 3, UIStyle.PURPLE_LIGHT));
      JPanel logoPanel = new JPanel();
      logoPanel.setBackground(UIStyle.PURPLE_DARK);
      logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
      logoPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIStyle.PURPLE_LIGHT));
      JLabel logo = new JLabel("\uD83D\uDC3C McDollibee");
      logo.setForeground(UIStyle.WHITE);
      logo.setFont(UIStyle.emojiFont(Font.BOLD, 23));
      logo.setAlignmentX(0.5F);
      JLabel restaurantLabel = new JLabel("RESTAURANT");
      restaurantLabel.setForeground(UIStyle.YELLOW_ACCENT);
      restaurantLabel.setFont(UIStyle.font(Font.BOLD, 13));
      restaurantLabel.setAlignmentX(0.5F);
      logoPanel.add(Box.createVerticalStrut(22));
      logoPanel.add(logo);
      logoPanel.add(Box.createVerticalStrut(5));
      logoPanel.add(restaurantLabel);
      logoPanel.add(Box.createVerticalStrut(20));
      sidebar.add(logoPanel, BorderLayout.NORTH);

      JPanel nav = new JPanel();
      nav.setBackground(UIStyle.PURPLE_DARK);
      nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
      nav.add(createButton("\uD83C\uDFE0  Dashboard"));
      nav.add(createSectionLabel("STORE"));
      addNav(nav, "\uD83C\uDFEC  Store Status", StoreStatus::new);
      addNav(nav, "\uD83D\uDD52  Operating Hours", Hoursscheduler::new);
      nav.add(createSectionLabel("MENU"));
      addNav(nav, "\uD83C\uDF7D  Menu Items", FoodItems::new);
      addNav(nav, "\uD83E\uDDE9  Modifier Builder", ModifierBuilder::new);
      nav.add(createSectionLabel("ORDERS"));
      addNav(nav, "\uD83D\uDCE5  Incoming Orders", IncomingOrders::new);
      addNav(nav, "\uD83D\uDCCB  Order Status", OrderStatus::new);
      nav.add(createSectionLabel("PROFILE"));
      addNav(nav, "\uD83C\uDFEA  Restaurant Profile", Restaurantprofile::new);
      sidebar.add(nav, BorderLayout.CENTER);

      // Logout button: MainFrame registers its own listener on this button
      logoutBtn = createButton("Logout");
      JPanel logoutPanel = new JPanel(new BorderLayout());
      logoutPanel.setBackground(UIStyle.PURPLE_DARK);
      logoutPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIStyle.PURPLE_LIGHT), BorderFactory.createEmptyBorder(10, 10, 15, 10)));
      logoutPanel.add(logoutBtn, BorderLayout.CENTER);
      sidebar.add(logoutPanel, BorderLayout.SOUTH);
      return sidebar;
   }

   private void addNav(JPanel nav, String text, Runnable open) {
      JButton button = createButton(text);
      button.addActionListener((e) -> open.run());
      nav.add(button);
   }

   private JPanel buildHeader() {
      JPanel header = new JPanel(new BorderLayout());
      header.setBackground(UIStyle.WHITE);
      header.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, UIStyle.YELLOW_ACCENT), BorderFactory.createEmptyBorder(18, 24, 15, 24)));
      JLabel title = new JLabel("RESTAURANT DASHBOARD");
      title.setFont(UIStyle.font(Font.BOLD, 22));
      title.setForeground(UIStyle.TEXT_DARK);
      welcome.setFont(UIStyle.font(Font.PLAIN, 15));
      welcome.setForeground(UIStyle.MUTED);
      JPanel headerText = new JPanel();
      headerText.setBackground(UIStyle.WHITE);
      headerText.setLayout(new BoxLayout(headerText, BoxLayout.Y_AXIS));
      headerText.add(title);
      headerText.add(Box.createVerticalStrut(5));
      headerText.add(welcome);
      JPanel statusRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
      statusRow.setOpaque(false);
      statusRow.add(statusChip);
      statusRow.add(statusButton("Open", Restaurant.StoreStatus.OPEN));
      statusRow.add(statusButton("Close", Restaurant.StoreStatus.CLOSED));
      statusRow.add(statusButton("Pause", Restaurant.StoreStatus.PAUSED));
      JPanel statusWrap = new JPanel(new GridBagLayout());
      statusWrap.setOpaque(false);
      statusWrap.add(statusRow);
      header.add(headerText, BorderLayout.WEST);
      header.add(statusWrap, BorderLayout.EAST);
      return header;
   }

   private JButton statusButton(String text, Restaurant.StoreStatus status) {
      JButton button = UIStyle.button(text, UIStyle.storeColor(status));
      button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
      button.addActionListener((e) -> data.setStoreStatus(status));
      return button;
   }

   private JPanel buildContent() {
      JPanel panel = new JPanel(new GridLayout(2, 1, 20, 20));
      panel.setBackground(UIStyle.BG_LIGHT);
      panel.setBorder(BorderFactory.createEmptyBorder(25, 24, 25, 24));
      JPanel stats = new JPanel(new GridLayout(1, 4, 15, 0));
      stats.setBackground(UIStyle.BG_LIGHT);
      stats.add(UIStyle.statCard("MENU ITEMS", String.valueOf(data.restaurant.menuItems.size()), data.availableDishCount() + " available", UIStyle.PURPLE_LIGHT));
      stats.add(UIStyle.statCard("ORDERS", String.valueOf(data.orders.size()), "Today's orders", UIStyle.YELLOW_ACCENT));
      stats.add(UIStyle.statCard("PENDING", String.valueOf(data.pendingCount()), "Waiting to be accepted", UIStyle.ORANGE));
      stats.add(UIStyle.statCard("REVENUE", MoneyFormat.peso(data.revenue()), "Today's revenue", UIStyle.GREEN));

      UIStyle.RoundedPanel quick = UIStyle.card(UIStyle.PURPLE_DARK);
      quick.setPadding(20, 26, 24, 24);
      JLabel quickTitle = new JLabel("QUICK ACTIONS");
      quickTitle.setFont(UIStyle.font(Font.BOLD, 18));
      quickTitle.setForeground(UIStyle.TEXT_DARK);
      quickTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
      JPanel quickButtons = new JPanel(new GridLayout(2, 3, 15, 15));
      quickButtons.setOpaque(false);
      addQuick(quickButtons, "Manage Menu Items", FoodItems::new);
      addQuick(quickButtons, "Incoming Orders", IncomingOrders::new);
      addQuick(quickButtons, "Order Status", OrderStatus::new);
      addQuick(quickButtons, "View Analytics", () -> { });
      addQuick(quickButtons, "Operating Hours", Hoursscheduler::new);
      addQuick(quickButtons, "Modifier Builder", ModifierBuilder::new);
      quick.add(quickTitle, BorderLayout.NORTH);
      quick.add(quickButtons, BorderLayout.CENTER);
      panel.add(stats);
      panel.add(quick);
      return panel;
   }

   private void addQuick(JPanel panel, String text, Runnable open) {
      JButton button = UIStyle.primaryButton(text);
      button.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 10));
      button.addActionListener((e) -> open.run());
      panel.add(button);
   }

   private JButton createButton(String text) {
      JButton button = new SidebarButton(text, UIStyle.YELLOW_ACCENT);
      button.setFont(UIStyle.emojiFont(Font.PLAIN, 14));
      button.setForeground(UIStyle.WHITE);
      button.setHorizontalAlignment(SwingConstants.LEFT);
      button.setBorder(BorderFactory.createEmptyBorder(9, 20, 9, 10));
      button.setFocusPainted(false);
      button.setCursor(new Cursor(Cursor.HAND_CURSOR));
      button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
      return button;
   }

   private JLabel createSectionLabel(String text) {
      JLabel label = new JLabel(text);
      label.setFont(UIStyle.font(Font.BOLD, 11));
      label.setForeground(UIStyle.YELLOW_ACCENT);
      label.setBorder(BorderFactory.createEmptyBorder(12, 20, 4, 0));
      label.setAlignmentX(0.0F);
      return label;
   }

   private static class SidebarButton extends JButton {
      private final Color accent;

      SidebarButton(String text, Color accent) {
         super(text);
         this.accent = accent;
         setContentAreaFilled(false);
         setOpaque(false);
      }

      @Override
      protected void paintComponent(Graphics g) {
         if (getModel().isRollover() || getModel().isPressed()) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setColor(new Color(255, 255, 255, 40));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(accent);
            g2.fillRect(0, 0, 4, getHeight());
            g2.dispose();
         }
         super.paintComponent(g);
      }
   }

   static abstract class AppWindow extends JFrame {
      AppWindow(String title, int width, int height) {
         super("GrabPanda - " + title);
         setSize(width, height);
         setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
         setLocationRelativeTo(null);
      }
   }

   static final class UIStyle {
      public static final Color PURPLE_DARK = new Color(74, 20, 140);
      public static final Color PURPLE_LIGHT = new Color(156, 39, 176);
      public static final Color YELLOW_ACCENT = new Color(255, 193, 7);
      public static final Color BG_LIGHT = new Color(248, 245, 250);
      public static final Color TEXT_DARK = new Color(30, 30, 30);
      public static final Color WHITE = Color.WHITE;
      public static final Color MUTED = new Color(110, 110, 110);
      public static final Color BORDER_GRAY = new Color(225, 225, 225);
      public static final Color ROW_ALT = new Color(250, 246, 252);
      public static final Color GRID = new Color(236, 230, 241);
      public static final Color SELECTION = new Color(225, 190, 231);
      public static final Color FIELD_BORDER = new Color(200, 180, 210);
      public static final Color GREEN = new Color(46, 125, 50);
      public static final Color RED = new Color(198, 40, 40);
      public static final Color ORANGE = new Color(230, 126, 0);
      private static final String EMOJI_FAMILY = findEmojiFamily();
      static final String[] ORDER_COLUMNS = {"Order ID", "Customer", "Items", "Total", "Payment", "Time", "Status"};

      private UIStyle() {
      }

      public static Font font(int style, int size) {
         return new Font("Segoe UI", style, size);
      }

      public static Font emojiFont(int style, int size) {
         return new Font(EMOJI_FAMILY, style, size);
      }

      private static String findEmojiFamily() {
         List<String> available = Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
         for (String name : new String[]{"Segoe UI Emoji", "Apple Color Emoji", "Noto Color Emoji"}) {
            if (available.contains(name)) {
               return name;
            }
         }
         return "Dialog";
      }


      static Object[] orderRow(Restaurant.Order o) {
         return new Object[]{o.id, o.customer, o.items, MoneyFormat.peso(o.total), o.payment, o.time, o.status};
      }

      public static Color storeColor(Restaurant.StoreStatus status) {
         switch (status) {
            case OPEN:
               return GREEN;
            case CLOSED:
               return RED;
            default:
               return ORANGE;
         }
      }

      static Color statusColor(String value) {
         switch (value.toLowerCase()) {
            case "available":
            case "active":
            case "paid":
            case "ready":
            case "completed":
               return GREEN;
            case "unavailable":
            case "inactive":
            case "failed":
            case "cancelled":
               return RED;
            case "pending":
            case "preparing":
               return ORANGE;
            default:
               return null;
         }
      }


      public static void warn(Component parent, String message) {
         JOptionPane.showMessageDialog(parent, message, "Please check", JOptionPane.WARNING_MESSAGE);
      }

      public static boolean confirm(Component parent, String message, String title) {
         return JOptionPane.showConfirmDialog(parent, message, title, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
      }

      public static int requireSelection(JTable table, Component parent, String what) {
         int row = table.getSelectedRow();
         if (row < 0) {
            JOptionPane.showMessageDialog(parent, "Please select " + what + " first.", "Nothing selected", JOptionPane.INFORMATION_MESSAGE);
         }
         return row;
      }

      public static void bind(JFrame frame, Runnable refresh) {
         Restaurant data = DATA;
         data.addListener(refresh);
         frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
               data.removeListener(refresh);
            }
         });
      }

      public static void onTextChange(JTextField field, Runnable action) {
         field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
               action.run();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
               action.run();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
               action.run();
            }
         });
      }

      public static ImageIcon loadImage(String path, int maxWidth, int maxHeight) {
         if (path == null || path.isEmpty() || !new File(path).isFile()) {
            return null;
         }
         ImageIcon icon = new ImageIcon(path);
         if (icon.getIconWidth() <= 0 || icon.getIconHeight() <= 0) {
            return null;
         }
         double scale = Math.min((double)maxWidth / icon.getIconWidth(), (double)maxHeight / icon.getIconHeight());
         scale = Math.min(scale, 1.0);
         int w = Math.max(1, (int)(icon.getIconWidth() * scale));
         int h = Math.max(1, (int)(icon.getIconHeight() * scale));
         return new ImageIcon(icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
      }


      public static JPanel page(String icon, String title, JComponent action, JComponent body) {
         JPanel main = new JPanel(new BorderLayout(0, 20));
         main.setBackground(BG_LIGHT);
         main.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
         JPanel header = new JPanel(new BorderLayout());
         header.setBackground(WHITE);
         header.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, YELLOW_ACCENT), BorderFactory.createEmptyBorder(18, 24, 15, 24)));
         JLabel titleLabel = new JLabel(icon + "  " + title);
         titleLabel.setFont(emojiFont(Font.BOLD, 25));
         titleLabel.setForeground(TEXT_DARK);
         header.add(titleLabel, BorderLayout.WEST);
         if (action != null) {
            JPanel actionWrap = new JPanel(new GridBagLayout());
            actionWrap.setOpaque(false);
            actionWrap.add(action);
            header.add(actionWrap, BorderLayout.EAST);
         }
         main.add(header, BorderLayout.NORTH);
         main.add(body, BorderLayout.CENTER);
         return main;
      }

      public static RoundedPanel card(Color accent) {
         RoundedPanel card = new RoundedPanel(18, WHITE, BORDER_GRAY, accent);
         card.setLayout(new BorderLayout());
         return card;
      }

      public static RoundedPanel statCard(String title, String value, String description, Color accent) {
         RoundedPanel card = new RoundedPanel(18, WHITE, BORDER_GRAY, accent);
         card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
         card.setPadding(20, 22, 24, 12);
         JLabel titleLabel = new JLabel(title);
         titleLabel.setFont(font(Font.BOLD, 13));
         titleLabel.setForeground(PURPLE_LIGHT);
         JLabel valueLabel = new JLabel(value);
         valueLabel.setFont(font(Font.BOLD, 26));
         valueLabel.setForeground(TEXT_DARK);
         JLabel descriptionLabel = new JLabel(description);
         descriptionLabel.setFont(font(Font.PLAIN, 12));
         descriptionLabel.setForeground(MUTED);
         card.add(titleLabel);
         card.add(Box.createVerticalStrut(10));
         card.add(valueLabel);
         card.add(Box.createVerticalStrut(5));
         card.add(descriptionLabel);
         return card;
      }

      public static RoundedPanel tableCard(JTable table) {
         RoundedPanel card = card(PURPLE_LIGHT);
         card.setPadding(14, 20, 14, 14);
         JScrollPane scroll = new JScrollPane(table);
         scroll.setBorder(BorderFactory.createLineBorder(BORDER_GRAY));
         scroll.getViewport().setBackground(WHITE);
         card.add(scroll, BorderLayout.CENTER);
         return card;
      }

      public static JTable createTable(String[] columns) {
         DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
               return false;
            }
         };
         JTable table = new JTable(model);
         table.setRowHeight(36);
         table.setFont(font(Font.PLAIN, 14));
         table.setForeground(TEXT_DARK);
         table.setShowVerticalLines(false);
         table.setShowHorizontalLines(true);
         table.setGridColor(GRID);
         table.setSelectionBackground(SELECTION);
         table.setSelectionForeground(TEXT_DARK);
         table.setFillsViewportHeight(true);
         table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
         table.setDefaultRenderer(Object.class, new RowRenderer());
         JTableHeader header = table.getTableHeader();
         header.setReorderingAllowed(false);
         header.setPreferredSize(new Dimension(0, 40));
         header.setDefaultRenderer(new HeaderRenderer());
         return table;
      }

      public static JPanel buttonRow(JComponent... buttons) {
         JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
         row.setOpaque(false);
         for (JComponent button : buttons) {
            row.add(button);
         }
         return row;
      }

      public static JPanel form(String[] labels, JComponent[] fields) {
         JPanel panel = new JPanel(new GridBagLayout());
         panel.setOpaque(false);
         GridBagConstraints gbc = new GridBagConstraints();
         gbc.insets = new Insets(6, 0, 6, 14);
         gbc.anchor = GridBagConstraints.WEST;
         for (int i = 0; i < labels.length; ++i) {
            gbc.gridy = i;
            gbc.gridx = 0;
            gbc.weightx = 0.0;
            gbc.fill = GridBagConstraints.NONE;
            if (!labels[i].isEmpty()) {
               panel.add(fieldLabel(labels[i]), gbc);
            }
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(6, 0, 6, 0);
            if (fields[i] instanceof JTextField && fields[i].getPreferredSize().width < 280) {
               fields[i].setPreferredSize(new Dimension(280, fields[i].getPreferredSize().height));
            }
            panel.add(fields[i], gbc);
            gbc.insets = new Insets(6, 0, 6, 14);
         }
         return panel;
      }


      public static RoundedButton button(String text, Color color) {
         RoundedButton button = new RoundedButton(text, color, 14);
         button.setForeground(WHITE);
         button.setFont(font(Font.BOLD, 13));
         button.setFocusPainted(false);
         button.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
         button.setCursor(new Cursor(Cursor.HAND_CURSOR));
         return button;
      }

      public static RoundedButton primaryButton(String text) {
         RoundedButton button = button(text, PURPLE_LIGHT);
         button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
         return button;
      }

      public static JLabel fieldLabel(String text) {
         JLabel label = new JLabel(text);
         label.setFont(font(Font.BOLD, 14));
         label.setForeground(TEXT_DARK);
         return label;
      }

      public static JTextField styledField(String text) {
         JTextField field = new JTextField(text);
         field.setFont(font(Font.PLAIN, 14));
         field.setBorder(fieldBorder(FIELD_BORDER));
         field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
               field.setBorder(fieldBorder(PURPLE_LIGHT));
            }

            @Override
            public void focusLost(FocusEvent e) {
               field.setBorder(fieldBorder(FIELD_BORDER));
            }
         });
         return field;
      }

      private static Border fieldBorder(Color color) {
         return BorderFactory.createCompoundBorder(new LineBorder(color, 1, true), BorderFactory.createEmptyBorder(8, 12, 8, 12));
      }


      static class RoundedPanel extends JPanel {
         private final int radius;
         private final Color fill;
         private final Color line;
         private final Color accent;

         RoundedPanel(int radius, Color fill, Color line, Color accent) {
            this.radius = radius;
            this.fill = fill;
            this.line = line;
            this.accent = accent;
            setOpaque(false);
         }

         void setPadding(int top, int left, int bottom, int right) {
            setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
         }

         @Override
         protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth() - 1;
            int h = getHeight() - 1;
            Shape shape = new RoundRectangle2D.Float(0, 0, w, h, radius, radius);
            g2.setColor(fill);
            g2.fill(shape);
            g2.setClip(shape);
            g2.setColor(accent);
            g2.fillRect(0, 0, 6, h + 1);
            g2.setClip((Shape)null);
            g2.setColor(line);
            g2.draw(shape);
            g2.dispose();
            super.paintComponent(g);
         }
      }

      static class RoundedButton extends JButton {
         private Color base;
         private final int radius;

         RoundedButton(String text, Color base, int radius) {
            super(text);
            this.base = base;
            this.radius = radius;
            setContentAreaFilled(false);
            setOpaque(false);
            setRolloverEnabled(true);
         }

         void setBase(Color base) {
            this.base = base;
            repaint();
         }

         @Override
         protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            ButtonModel m = getModel();
            g2.setColor(m.isPressed() ? base.darker().darker() : m.isRollover() ? base.darker() : base);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
         }
      }

      static class Chip extends JLabel {
         private Color base = GREEN;

         Chip() {
            setOpaque(false);
            setForeground(WHITE);
            setFont(font(Font.BOLD, 13));
            setBorder(BorderFactory.createEmptyBorder(7, 16, 7, 16));
         }

         void set(String text, Color color) {
            base = color;
            setText(text);
            repaint();
         }

         @Override
         protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(base);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
            g2.dispose();
            super.paintComponent(g);
         }
      }

      static class RowRenderer extends DefaultTableCellRenderer {
         @Override
         public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focus, int row, int column) {
            super.getTableCellRendererComponent(table, value, selected, false, row, column);
            setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
            setBackground(selected ? SELECTION : (row % 2 == 0 ? WHITE : ROW_ALT));
            setForeground(TEXT_DARK);
            setFont(font(Font.PLAIN, 14));
            if (value instanceof String) {
               Color status = statusColor((String)value);
               if (status != null) {
                  setForeground(status);
                  setFont(font(Font.BOLD, 14));
               }
            }
            return this;
         }
      }

      static class HeaderRenderer extends DefaultTableCellRenderer {
         @Override
         public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focus, int row, int column) {
            super.getTableCellRendererComponent(table, value, false, false, row, column);
            setOpaque(true);
            setBackground(PURPLE_DARK);
            setForeground(WHITE);
            setFont(font(Font.BOLD, 13));
            setHorizontalAlignment(SwingConstants.LEFT);
            setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, YELLOW_ACCENT), BorderFactory.createEmptyBorder(0, 12, 0, 12)));
            return this;
         }
      }
   }

   static class StoreStatus extends AppWindow {
      private final Restaurant data = DATA;
      private final UIStyle.Chip chip = new UIStyle.Chip();
      private final JLabel scheduleNote = new JLabel();
      private final Map<Restaurant.StoreStatus, UIStyle.RoundedButton> buttons = new EnumMap<>(Restaurant.StoreStatus.class);

      public StoreStatus() {
         super("Store Status", 820, 480);
         JPanel options = new JPanel(new GridLayout(1, 3, 20, 20));
         options.setOpaque(false);
         options.add(optionCard("\uD83D\uDFE2", Restaurant.StoreStatus.OPEN, "Customers can place new orders."));
         options.add(optionCard("\uD83D\uDD34", Restaurant.StoreStatus.CLOSED, "The store is closed and not taking any orders."));
         options.add(optionCard("\u23F8", Restaurant.StoreStatus.PAUSED, "Temporarily stop new orders (for example when the kitchen is too busy). Current orders continue."));
         JPanel statusRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
         statusRow.setOpaque(false);
         statusRow.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));
         statusRow.add(UIStyle.fieldLabel("Current status:"));
         statusRow.add(chip);
         scheduleNote.setFont(UIStyle.font(Font.PLAIN, 12));
         scheduleNote.setForeground(UIStyle.MUTED);
         scheduleNote.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));
         UIStyle.RoundedPanel card = UIStyle.card(UIStyle.PURPLE_LIGHT);
         card.setPadding(22, 30, 22, 24);
         card.add(statusRow, BorderLayout.NORTH);
         card.add(options, BorderLayout.CENTER);
         card.add(scheduleNote, BorderLayout.SOUTH);
         JPanel body = new JPanel(new BorderLayout());
         body.setOpaque(false);
         body.add(card, BorderLayout.NORTH);
         setContentPane(UIStyle.page("\uD83C\uDFEC", "Store Status", null, body));
         UIStyle.bind(this, this::refresh);
         refresh();
         setVisible(true);
      }

      private JPanel optionCard(String icon, Restaurant.StoreStatus status, String description) {
         Color color = UIStyle.storeColor(status);
         UIStyle.RoundedPanel body = new UIStyle.RoundedPanel(18, UIStyle.BG_LIGHT, UIStyle.BORDER_GRAY, color);
         body.setLayout(new BorderLayout(0, 12));
         body.setPadding(18, 22, 18, 16);
         JLabel heading = new JLabel(icon + "  " + status.getLabel());
         heading.setFont(UIStyle.emojiFont(Font.BOLD, 18));
         heading.setForeground(UIStyle.TEXT_DARK);
         JLabel text = new JLabel("<html><body style='width:150px'>" + description + "</body></html>");
         text.setFont(UIStyle.font(Font.PLAIN, 13));
         text.setForeground(UIStyle.MUTED);
         UIStyle.RoundedButton button = UIStyle.button("", color);
         button.addActionListener((e) -> data.setStoreStatus(status));
         buttons.put(status, button);
         body.add(heading, BorderLayout.NORTH);
         body.add(text, BorderLayout.CENTER);
         body.add(button, BorderLayout.SOUTH);
         return body;
      }

      private String actionText(Restaurant.StoreStatus status) {
         switch (status) {
            case OPEN:
               return "Open Store";
            case CLOSED:
               return "Close Store";
            default:
               return "Pause Orders";
         }
      }

      private void refresh() {
         Restaurant.StoreStatus current = data.getStoreStatus();
         chip.set(current.getLabel(), UIStyle.storeColor(current));
         for (Map.Entry<Restaurant.StoreStatus, UIStyle.RoundedButton> entry : buttons.entrySet()) {
            Restaurant.StoreStatus status = entry.getKey();
            UIStyle.RoundedButton button = entry.getValue();
            boolean active = status == current;
            button.setText(active ? "\u2714  Current" : actionText(status));
            button.setBase(active ? UIStyle.storeColor(status).darker().darker() : UIStyle.storeColor(status));
         }
         scheduleNote.setText(data.isAutoSchedule()
            ? "<html><body style='width:660px'>Auto schedule is ON: the status changes by itself at each day's opening and closing time. A change you make here stays until the next scheduled opening or closing.</body></html>"
            : "<html><body style='width:660px'>Auto schedule is OFF: the status only changes when you change it here.</body></html>");
      }
   }

   static class Hoursscheduler extends AppWindow {
      private final Restaurant data = DATA;
      private final JCheckBox autoBox = new JCheckBox("Automatically open and close the store using this schedule");
      private final JCheckBox[] dayEnabled = new JCheckBox[7];
      private final JSpinner[] openTime = new JSpinner[7];
      private final JSpinner[] closeTime = new JSpinner[7];
      private final JLabel nowLabel = new JLabel();

      public Hoursscheduler() {
         super("Operating Hours", 800, 700);
         autoBox.setSelected(data.isAutoSchedule());
         autoBox.setFont(UIStyle.font(Font.PLAIN, 14));
         autoBox.setOpaque(false);
         JLabel hint = new JLabel("<html>Set the opening and closing time for each day. If the closing time is earlier than the opening time, the store closes after midnight.</html>");
         hint.setFont(UIStyle.font(Font.PLAIN, 12));
         hint.setForeground(UIStyle.MUTED);
         JPanel top = new JPanel(new BorderLayout(0, 6));
         top.setOpaque(false);
         top.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));
         top.add(autoBox, BorderLayout.NORTH);
         top.add(hint, BorderLayout.CENTER);

         JPanel grid = new JPanel(new GridLayout(0, 4, 15, 10));
         grid.setOpaque(false);
         grid.add(UIStyle.fieldLabel("Day"));
         grid.add(UIStyle.fieldLabel("Open this day"));
         grid.add(UIStyle.fieldLabel("Opens at"));
         grid.add(UIStyle.fieldLabel("Closes at"));

         for (int i = 0; i < 7; ++i) {
            Restaurant.DaySchedule day = data.schedule.get(i);
            dayEnabled[i] = new JCheckBox("Open", day.enabled);
            dayEnabled[i].setOpaque(false);
            dayEnabled[i].setFont(UIStyle.font(Font.PLAIN, 14));
            openTime[i] = timeSpinner(day.open);
            closeTime[i] = timeSpinner(day.close);
            int index = i;
            dayEnabled[i].addActionListener((e) -> updateRow(index));
            JLabel dayName = new JLabel(day.day.getDisplayName(TextStyle.FULL, Locale.ENGLISH));
            dayName.setFont(UIStyle.font(Font.PLAIN, 14));
            grid.add(dayName);
            grid.add(dayEnabled[i]);
            grid.add(openTime[i]);
            grid.add(closeTime[i]);
            updateRow(i);
         }

         JPanel gridWrap = new JPanel(new BorderLayout());
         gridWrap.setOpaque(false);
         gridWrap.add(grid, BorderLayout.NORTH);

         JButton save = UIStyle.primaryButton("Save Schedule");
         save.addActionListener((e) -> saveSchedule());
         JButton copyMonday = UIStyle.button("Copy Monday to All Days", UIStyle.PURPLE_DARK);
         copyMonday.addActionListener((e) -> copyMondayToAll());
         nowLabel.setFont(UIStyle.font(Font.PLAIN, 13));
         JPanel bottom = new JPanel(new BorderLayout(0, 10));
         bottom.setOpaque(false);
         bottom.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));
         bottom.add(UIStyle.buttonRow(save, copyMonday), BorderLayout.NORTH);
         bottom.add(nowLabel, BorderLayout.CENTER);

         UIStyle.RoundedPanel card = UIStyle.card(UIStyle.YELLOW_ACCENT);
         card.setPadding(22, 30, 22, 24);
         card.add(top, BorderLayout.NORTH);
         card.add(gridWrap, BorderLayout.CENTER);
         card.add(bottom, BorderLayout.SOUTH);
         JPanel body = new JPanel(new BorderLayout());
         body.setOpaque(false);
         body.add(card, BorderLayout.NORTH);
         setContentPane(UIStyle.page("\uD83D\uDD52", "Operating Hours", null, body));
         updateNowLabel();
         setVisible(true);
      }

      private JSpinner timeSpinner(LocalTime time) {
         Date date = Date.from(LocalDate.now().atTime(time).atZone(ZoneId.systemDefault()).toInstant());
         JSpinner spinner = new JSpinner(new SpinnerDateModel(date, null, null, Calendar.MINUTE));
         spinner.setEditor(new JSpinner.DateEditor(spinner, "hh:mm a"));
         spinner.setFont(UIStyle.font(Font.PLAIN, 14));
         return spinner;
      }

      private LocalTime toTime(JSpinner spinner) {
         return ((Date)spinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalTime().withSecond(0).withNano(0);
      }

      private void updateRow(int index) {
         boolean enabled = dayEnabled[index].isSelected();
         openTime[index].setEnabled(enabled);
         closeTime[index].setEnabled(enabled);
      }

      private void copyMondayToAll() {
         for (int i = 1; i < 7; ++i) {
            dayEnabled[i].setSelected(dayEnabled[0].isSelected());
            openTime[i].setValue(openTime[0].getValue());
            closeTime[i].setValue(closeTime[0].getValue());
            updateRow(i);
         }
      }

      private void saveSchedule() {
         for (int i = 0; i < 7; ++i) {
            if (dayEnabled[i].isSelected() && toTime(openTime[i]).equals(toTime(closeTime[i]))) {
               String dayName = data.schedule.get(i).day.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
               UIStyle.warn(this, dayName + ": the opening and closing time can't be the same.");
               return;
            }
         }
         for (int i = 0; i < 7; ++i) {
            Restaurant.DaySchedule day = data.schedule.get(i);
            day.enabled = dayEnabled[i].isSelected();
            day.open = toTime(openTime[i]);
            day.close = toTime(closeTime[i]);
         }
         data.setAutoSchedule(autoBox.isSelected());
         updateNowLabel();
         JOptionPane.showMessageDialog(this, "Schedule saved.", "Saved", JOptionPane.INFORMATION_MESSAGE);
      }

      private void updateNowLabel() {
         if (!data.isAutoSchedule()) {
            nowLabel.setForeground(UIStyle.MUTED);
            nowLabel.setText("Auto schedule is OFF - the store status is only changed manually.");
         } else {
            boolean open = data.shouldBeOpenNow();
            nowLabel.setForeground(open ? UIStyle.GREEN : UIStyle.RED);
            nowLabel.setText("  By this schedule, the store should be " + (open ? "OPEN" : "CLOSED") + " right now.");
         }
      }
   }

   static class IncomingOrders extends AppWindow {
      private final Restaurant data = DATA;
      private final JTable table = UIStyle.createTable(UIStyle.ORDER_COLUMNS);
      private final DefaultTableModel model = (DefaultTableModel)table.getModel();
      private final List<Restaurant.Order> shown = new ArrayList<>();
      private final JLabel storeNote = new JLabel();

      public IncomingOrders() {
         super("Incoming Orders", 1000, 620);
         JButton simulate = UIStyle.primaryButton("+ Simulate New Order");
         simulate.addActionListener((e) -> simulateOrder());
         JButton accept = UIStyle.button("Accept Order", UIStyle.GREEN);
         accept.addActionListener((e) -> acceptSelected());
         JButton reject = UIStyle.button("Reject Order", UIStyle.RED);
         reject.addActionListener((e) -> rejectSelected());
         storeNote.setFont(UIStyle.font(Font.BOLD, 13));
         JPanel body = new JPanel(new BorderLayout(0, 12));
         body.setOpaque(false);
         body.add(storeNote, BorderLayout.NORTH);
         body.add(UIStyle.tableCard(table), BorderLayout.CENTER);
         body.add(UIStyle.buttonRow(accept, reject), BorderLayout.SOUTH);
         setContentPane(UIStyle.page("\uD83D\uDCE5", "Incoming Orders", simulate, body));
         UIStyle.bind(this, this::refresh);
         refresh();
         setVisible(true);
      }

      private void refresh() {
         shown.clear();
         model.setRowCount(0);
         for (Restaurant.Order order : data.orders) {
            if (Restaurant.Order.PENDING.equals(order.status)) {
               shown.add(order);
               model.addRow(UIStyle.orderRow(order));
            }
         }
         Restaurant.StoreStatus status = data.getStoreStatus();
         storeNote.setForeground(UIStyle.storeColor(status));
         storeNote.setText(status == Restaurant.StoreStatus.OPEN
            ? "\u25CF  Store is open - new orders will appear here."
            : "\u25CF  Store status: " + status.getLabel() + " - new orders are not being accepted.");
      }

      private void acceptSelected() {
         int row = UIStyle.requireSelection(table, this, "an order");
         if (row >= 0) {
            data.setOrderStatus(shown.get(row), Restaurant.Order.PREPARING);
         }
      }

      private void rejectSelected() {
         int row = UIStyle.requireSelection(table, this, "an order");
         if (row >= 0) {
            Restaurant.Order order = shown.get(row);
            if (UIStyle.confirm(this, "Reject order " + order.id + " from " + order.customer + "?", "Reject Order")) {
               data.setOrderStatus(order, Restaurant.Order.CANCELLED);
            }
         }
      }

      private void simulateOrder() {
         if (data.simulateIncomingOrder() == null) {
            if (data.getStoreStatus() == Restaurant.StoreStatus.OPEN) {
               UIStyle.warn(this, "There are no available menu items to order.");
            } else {
               UIStyle.warn(this, "The store status is \"" + data.getStoreStatus().getLabel() + "\", so new orders are not accepted.");
            }
         }
      }
   }

   static class OrderStatus extends AppWindow {
      private final Restaurant data = DATA;
      private final JTable table = UIStyle.createTable(UIStyle.ORDER_COLUMNS);
      private final DefaultTableModel model = (DefaultTableModel)table.getModel();
      private final List<Restaurant.Order> shown = new ArrayList<>();
      private final JTextField search = UIStyle.styledField("");
      private final JComboBox<String> filter = new JComboBox<>(new String[]{"All", "Pending", "Preparing", "Ready", "Completed", "Cancelled", "Failed"});

      public OrderStatus() {
         super("Order Status", 1020, 640);
         search.setColumns(16);
         UIStyle.onTextChange(search, this::refresh);
         filter.addActionListener((e) -> refresh());
         JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
         filterBar.setOpaque(false);
         filterBar.add(UIStyle.fieldLabel("Search (ID or customer):"));
         filterBar.add(search);
         filterBar.add(UIStyle.fieldLabel("Status:"));
         filterBar.add(filter);
         JButton preparing = UIStyle.button("Mark Preparing", UIStyle.ORANGE);
         preparing.addActionListener((e) -> changeStatus(Restaurant.Order.PREPARING));
         JButton ready = UIStyle.button("Mark Ready", UIStyle.PURPLE_DARK);
         ready.addActionListener((e) -> changeStatus(Restaurant.Order.READY));
         JButton completed = UIStyle.button("Mark Completed", UIStyle.GREEN);
         completed.addActionListener((e) -> changeStatus(Restaurant.Order.COMPLETED));
         JButton cancel = UIStyle.button("Cancel Order", UIStyle.RED);
         cancel.addActionListener((e) -> changeStatus(Restaurant.Order.CANCELLED));
         JPanel body = new JPanel(new BorderLayout(0, 12));
         body.setOpaque(false);
         body.add(filterBar, BorderLayout.NORTH);
         body.add(UIStyle.tableCard(table), BorderLayout.CENTER);
         body.add(UIStyle.buttonRow(preparing, ready, completed, cancel), BorderLayout.SOUTH);
         setContentPane(UIStyle.page("\uD83D\uDCCB", "Order Status", null, body));
         UIStyle.bind(this, this::refresh);
         refresh();
         setVisible(true);
      }

      private void refresh() {
         String query = search.getText().trim().toLowerCase();
         String statusFilter = String.valueOf(filter.getSelectedItem());
         shown.clear();
         model.setRowCount(0);
         for (Restaurant.Order order : data.orders) {
            boolean statusMatches = "All".equals(statusFilter) || statusFilter.equals(order.status);
            boolean textMatches = query.isEmpty() || order.id.toLowerCase().contains(query) || order.customer.toLowerCase().contains(query);
            if (statusMatches && textMatches) {
               shown.add(order);
               model.addRow(UIStyle.orderRow(order));
            }
         }
      }

      private void changeStatus(String newStatus) {
         int row = UIStyle.requireSelection(table, this, "an order");
         if (row >= 0) {
            Restaurant.Order order = shown.get(row);
            if (order.isFinal()) {
               UIStyle.warn(this, order.id + " is already " + order.status + " and can't be changed.");
            } else {
               data.setOrderStatus(order, newStatus);
            }
         }
      }
   }

   static class Restaurantprofile extends AppWindow {
      private final Restaurant data = DATA;
      private final JTextField restaurantName = UIStyle.styledField("");
      private final JTextField location = UIStyle.styledField("");
      private final JTextField ownerName = UIStyle.styledField("");
      private final JTextField contact = UIStyle.styledField("");
      private final JTextField cuisine = UIStyle.styledField("");

      public Restaurantprofile() {
         super("Restaurant Profile", 700, 680);
         loadFields();
         JButton save = UIStyle.primaryButton("Save Changes");
         save.addActionListener((e) -> saveProfile());
         JButton undo = UIStyle.button("Undo Changes", UIStyle.PURPLE_DARK);
         undo.addActionListener((e) -> loadFields());
         UIStyle.RoundedPanel card = UIStyle.card(UIStyle.PURPLE_LIGHT);
         card.setPadding(25, 32, 25, 28);
         JPanel form = new JPanel(new GridLayout(0, 2, 15, 18));
         form.setOpaque(false);
         form.add(UIStyle.fieldLabel("Restaurant Name:"));
         form.add(restaurantName);
         form.add(UIStyle.fieldLabel("Location:"));
         form.add(location);
         form.add(UIStyle.fieldLabel("Owner Name:"));
         form.add(ownerName);
         form.add(UIStyle.fieldLabel("Contact Number:"));
         form.add(contact);
         form.add(UIStyle.fieldLabel("Cuisine:"));
         form.add(cuisine);
         form.add(new JLabel(""));
         form.add(UIStyle.buttonRow(save, undo));
         card.add(form, BorderLayout.CENTER);
         JPanel body = new JPanel(new BorderLayout());
         body.setOpaque(false);
         body.add(card, BorderLayout.NORTH);
         setContentPane(UIStyle.page("\uD83C\uDFEA", "Restaurant Profile", null, body));
         setVisible(true);
      }

      private void loadFields() {
         Restaurant.Restaurantt restaurant = data.restaurant;
         restaurantName.setText(restaurant.name);
         location.setText(restaurant.location);
         ownerName.setText(restaurant.ownerName);
         contact.setText(restaurant.contact);
         cuisine.setText(restaurant.cuisine);
      }

      private void saveProfile() {
         if (restaurantName.getText().trim().isEmpty() || location.getText().trim().isEmpty()) {
            UIStyle.warn(this, "Restaurant name and location can't be empty.");
            return;
         }
         Restaurant.Restaurantt restaurant = data.restaurant;
         restaurant.name = restaurantName.getText().trim();
         restaurant.location = location.getText().trim();
         restaurant.ownerName = ownerName.getText().trim();
         restaurant.contact = contact.getText().trim();
         restaurant.cuisine = cuisine.getText().trim();
         data.notifyChanged();
         JOptionPane.showMessageDialog(this, "Restaurant profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
      }
   }

   static class FoodItems extends AppWindow {
      private final Restaurant data = DATA;
      private final JTable table = UIStyle.createTable(new String[]{"Food Name", "Category", "Price", "Availability", "Sold"});
      private final DefaultTableModel model = (DefaultTableModel)table.getModel();
      private final JLabel detailImage = new JLabel("", SwingConstants.CENTER);
      private final JLabel detailName = new JLabel();
      private final JLabel detailMeta = new JLabel();
      private final JLabel detailStatus = new JLabel();
      private final JLabel detailDescription = new JLabel();

      public FoodItems() {
         super("Menu Items", 1230, 680);
         JButton add = UIStyle.primaryButton("+ Add Food Item");
         add.addActionListener((e) -> showItemDialog(null));
         JButton edit = UIStyle.button("Edit Selected", UIStyle.PURPLE_DARK);
         edit.addActionListener((e) -> editSelected());
         JButton toggle = UIStyle.button("Toggle Availability", UIStyle.ORANGE);
         toggle.addActionListener((e) -> toggleSelected());
         JButton changePicture = UIStyle.button("Change Picture", UIStyle.PURPLE_LIGHT);
         changePicture.addActionListener((e) -> changePictureOfSelected());
         JButton removePicture = UIStyle.button("Remove Picture", UIStyle.RED);
         removePicture.addActionListener((e) -> removePictureOfSelected());
         JButton delete = UIStyle.button("Delete Item", UIStyle.RED);
         delete.addActionListener((e) -> deleteSelected());
         JPanel left = new JPanel(new BorderLayout(0, 12));
         left.setOpaque(false);
         left.add(UIStyle.tableCard(table), BorderLayout.CENTER);
         left.add(UIStyle.buttonRow(edit, toggle, changePicture, removePicture, delete), BorderLayout.SOUTH);
         JPanel body = new JPanel(new BorderLayout(18, 0));
         body.setOpaque(false);
         body.add(left, BorderLayout.CENTER);
         body.add(buildDetailsCard(), BorderLayout.EAST);
         setContentPane(UIStyle.page("\uD83C\uDF7D", "Menu Items", add, body));
         table.getSelectionModel().addListSelectionListener((e) -> {
            if (!e.getValueIsAdjusting()) {
               updateDetails();
            }
         });
         UIStyle.bind(this, this::refresh);
         refresh();
         setVisible(true);
      }

      private JPanel buildDetailsCard() {
         detailImage.setPreferredSize(new Dimension(250, 200));
         detailImage.setOpaque(true);
         detailImage.setBackground(UIStyle.BG_LIGHT);
         detailImage.setForeground(UIStyle.MUTED);
         detailImage.setFont(UIStyle.font(Font.PLAIN, 13));
         detailImage.setBorder(BorderFactory.createLineBorder(UIStyle.BORDER_GRAY));
         detailName.setFont(UIStyle.font(Font.BOLD, 20));
         detailName.setForeground(UIStyle.TEXT_DARK);
         detailMeta.setFont(UIStyle.font(Font.PLAIN, 14));
         detailMeta.setForeground(UIStyle.MUTED);
         detailStatus.setFont(UIStyle.font(Font.BOLD, 14));
         detailDescription.setFont(UIStyle.font(Font.PLAIN, 13));
         detailDescription.setForeground(UIStyle.TEXT_DARK);
         detailDescription.setVerticalAlignment(SwingConstants.TOP);
         JPanel text = new JPanel();
         text.setOpaque(false);
         text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
         text.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));
         for (JComponent label : new JComponent[]{detailName, detailMeta, detailStatus, detailDescription}) {
            label.setAlignmentX(0.0F);
         }
         text.add(detailName);
         text.add(Box.createVerticalStrut(6));
         text.add(detailMeta);
         text.add(Box.createVerticalStrut(6));
         text.add(detailStatus);
         text.add(Box.createVerticalStrut(10));
         text.add(detailDescription);
         UIStyle.RoundedPanel card = UIStyle.card(UIStyle.YELLOW_ACCENT);
         card.setPadding(20, 26, 20, 20);
         card.setPreferredSize(new Dimension(320, 100));
         card.add(detailImage, BorderLayout.NORTH);
         card.add(text, BorderLayout.CENTER);
         return card;
      }

      private void updateDetails() {
         int row = table.getSelectedRow();
         if (row < 0 || row >= data.restaurant.menuItems.size()) {
            detailImage.setIcon(null);
            detailImage.setText("No item selected");
            detailName.setText("Select a food item");
            detailMeta.setText("");
            detailStatus.setText("");
            detailDescription.setText("");
            return;
         }
         Restaurant.Dish dish = data.restaurant.menuItems.get(row);
         ImageIcon icon = UIStyle.loadImage(dish.imagePath, 240, 190);
         detailImage.setIcon(icon);
         detailImage.setText(icon != null ? "" : (dish.imagePath == null ? "No picture" : "Picture not found"));
         detailName.setText(dish.name);
         detailMeta.setText(dish.category + "  \u2022  " + MoneyFormat.peso(dish.price));
         detailStatus.setText(dish.available ? "Available" : "Unavailable");
         detailStatus.setForeground(dish.available ? UIStyle.GREEN : UIStyle.RED);
         detailDescription.setText(toHtml(dish.description.isEmpty() ? "No description yet. Click Edit Selected to add one." : dish.description));
      }

      private String toHtml(String text) {
         return "<html><body style='width:230px'>" + text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\n", "<br>") + "</body></html>";
      }

      private void refresh() {
         int selected = table.getSelectedRow();
         model.setRowCount(0);
         for (Restaurant.Dish dish : data.restaurant.menuItems) {
            model.addRow(new Object[]{dish.name, dish.category, MoneyFormat.peso(dish.price), dish.available ? "Available" : "Unavailable", dish.sold});
         }
         if (selected >= 0 && selected < model.getRowCount()) {
            table.setRowSelectionInterval(selected, selected);
         }
         updateDetails();
      }

      private void editSelected() {
         int row = UIStyle.requireSelection(table, this, "an item");
         if (row >= 0) {
            showItemDialog(data.restaurant.menuItems.get(row));
         }
      }

      private void toggleSelected() {
         int row = UIStyle.requireSelection(table, this, "an item");
         if (row >= 0) {
            Restaurant.Dish dish = data.restaurant.menuItems.get(row);
            dish.available = !dish.available;
            data.notifyChanged();
         }
      }

      private void changePictureOfSelected() {
         int row = UIStyle.requireSelection(table, this, "an item");
         if (row >= 0) {
            String path = pickImage(this);
            if (path != null) {
               data.restaurant.menuItems.get(row).imagePath = path;
               data.notifyChanged();
            }
         }
      }

      private void removePictureOfSelected() {
         int row = UIStyle.requireSelection(table, this, "an item");
         if (row >= 0) {
            Restaurant.Dish dish = data.restaurant.menuItems.get(row);
            if (dish.imagePath == null) {
               JOptionPane.showMessageDialog(this, dish.name + " has no picture.", "No picture", JOptionPane.INFORMATION_MESSAGE);
            } else if (UIStyle.confirm(this, "Remove the picture of \"" + dish.name + "\"?", "Remove Picture")) {
               dish.imagePath = null;
               data.notifyChanged();
            }
         }
      }

      private void deleteSelected() {
         int row = UIStyle.requireSelection(table, this, "an item");
         if (row >= 0) {
            Restaurant.Dish dish = data.restaurant.menuItems.get(row);
            if (UIStyle.confirm(this, "Delete \"" + dish.name + "\" from the menu?", "Delete Item")) {
               data.restaurant.menuItems.remove(row);
               data.notifyChanged();
            }
         }
      }

      private String pickImage(Component parent) {
         JFileChooser chooser = new JFileChooser();
         chooser.setDialogTitle("Choose a food picture");
         chooser.setFileFilter(new FileNameExtensionFilter("Pictures (png, jpg, jpeg, gif)", "png", "jpg", "jpeg", "gif"));
         if (chooser.showOpenDialog(parent) != JFileChooser.APPROVE_OPTION) {
            return null;
         }
         File file = chooser.getSelectedFile();
         if (UIStyle.loadImage(file.getAbsolutePath(), 100, 100) == null) {
            UIStyle.warn(parent, "That file couldn't be opened as a picture. Please choose a PNG, JPG or GIF image.");
            return null;
         }
         return file.getAbsolutePath();
      }

      private void showItemDialog(Restaurant.Dish dish) {
         JTextField nameField = UIStyle.styledField(dish == null ? "" : dish.name);
         JComboBox<String> categoryBox = new JComboBox<>(data.categories.toArray(new String[0]));
         categoryBox.setEditable(true);
         if (dish != null) {
            categoryBox.setSelectedItem(dish.category);
         }
         JTextField priceField = UIStyle.styledField(dish == null ? "" : MoneyFormat.amountText(dish.price));
         JCheckBox availableBox = new JCheckBox("Available for ordering", dish == null || dish.available);
         availableBox.setOpaque(false);
         JTextArea descriptionArea = new JTextArea(dish == null ? "" : dish.description, 4, 22);
         descriptionArea.setFont(UIStyle.font(Font.PLAIN, 14));
         descriptionArea.setLineWrap(true);
         descriptionArea.setWrapStyleWord(true);
         descriptionArea.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
         JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
         descriptionScroll.setBorder(BorderFactory.createLineBorder(UIStyle.BORDER_GRAY));
         JPanel descriptionPanel = new JPanel(new BorderLayout(0, 6));
         descriptionPanel.setOpaque(false);
         descriptionPanel.add(UIStyle.fieldLabel("Description:"), BorderLayout.NORTH);
         descriptionPanel.add(descriptionScroll, BorderLayout.CENTER);

         String[] imagePath = {dish == null ? null : dish.imagePath};
         JLabel preview = new JLabel("", SwingConstants.CENTER);
         preview.setPreferredSize(new Dimension(190, 190));
         preview.setOpaque(true);
         preview.setBackground(UIStyle.BG_LIGHT);
         preview.setForeground(UIStyle.MUTED);
         preview.setFont(UIStyle.font(Font.PLAIN, 13));
         preview.setBorder(BorderFactory.createLineBorder(UIStyle.BORDER_GRAY));
         UIStyle.RoundedButton chooseButton = UIStyle.button("Choose Picture", UIStyle.PURPLE_LIGHT);
         UIStyle.RoundedButton removeButton = UIStyle.button("Remove Picture", UIStyle.RED);
         Runnable updatePreview = () -> {
            ImageIcon icon = UIStyle.loadImage(imagePath[0], 180, 180);
            preview.setIcon(icon);
            preview.setText(icon != null ? "" : (imagePath[0] == null ? "No picture" : "Picture not found"));
            chooseButton.setText(imagePath[0] == null ? "Choose Picture" : "Change Picture");
            removeButton.setVisible(imagePath[0] != null);
         };
         chooseButton.addActionListener((e) -> {
            String picked = pickImage(preview);
            if (picked != null) {
               imagePath[0] = picked;
               updatePreview.run();
            }
         });
         removeButton.addActionListener((e) -> {
            imagePath[0] = null;
            updatePreview.run();
         });
         updatePreview.run();
         JPanel pictureButtons = new JPanel(new GridLayout(2, 1, 0, 8));
         pictureButtons.setOpaque(false);
         pictureButtons.add(chooseButton);
         pictureButtons.add(removeButton);
         JPanel picturePanel = new JPanel(new BorderLayout(0, 10));
         picturePanel.setOpaque(false);
         picturePanel.add(preview, BorderLayout.NORTH);
         picturePanel.add(pictureButtons, BorderLayout.CENTER);

         JPanel formPanel = UIStyle.form(new String[]{"Food Name:", "Category:", "Price (\u20B1):", "Availability:"}, new JComponent[]{nameField, categoryBox, priceField, availableBox});
         JPanel right = new JPanel(new BorderLayout(0, 12));
         right.setOpaque(false);
         right.add(formPanel, BorderLayout.NORTH);
         right.add(descriptionPanel, BorderLayout.CENTER);
         JPanel panel = new JPanel(new BorderLayout(20, 0));
         panel.setOpaque(false);
         panel.add(picturePanel, BorderLayout.WEST);
         panel.add(right, BorderLayout.CENTER);

         int result = JOptionPane.showConfirmDialog(this, panel, dish == null ? "Add Food Item" : "Edit Food Item", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
         if (result != JOptionPane.OK_OPTION) {
            return;
         }
         String name = nameField.getText().trim();
         String category = String.valueOf(categoryBox.getSelectedItem()).trim();
         Double price = MoneyFormat.parseAmount(priceField.getText());
         if (name.isEmpty() || category.isEmpty()) {
            UIStyle.warn(this, "Food name and category are required.");
            return;
         }
         if (price == null || price < 0.0) {
            UIStyle.warn(this, "Please enter a valid price, for example 120 or 99.50.");
            return;
         }
         data.addCategoryIfNew(category);
         Restaurant.Dish target = dish == null ? new Restaurant.Dish(name, category, price, availableBox.isSelected(), 0) : dish;
         target.name = name;
         target.category = category;
         target.price = price;
         target.available = availableBox.isSelected();
         target.description = descriptionArea.getText().trim();
         target.imagePath = imagePath[0];
         if (dish == null) {
            data.restaurant.menuItems.add(target);
         }
         data.notifyChanged();
      }
   }

   static class ModifierBuilder extends AppWindow {
      private final Restaurant data = DATA;
      private final DefaultListModel<String> groupModel = new DefaultListModel<>();
      private final JList<String> groupList = new JList<>(groupModel);
      private final JTable optionTable = UIStyle.createTable(new String[]{"Item / Option", "Price Change"});
      private final DefaultTableModel optionModel = (DefaultTableModel)optionTable.getModel();
      private final JLabel groupTitle = new JLabel("Select a group");
      private final JCheckBox requiredBox = new JCheckBox("Customer must choose one");

      public ModifierBuilder() {
         super("Modifier & Menu Builder", 1020, 650);
         groupList.setFont(UIStyle.font(Font.PLAIN, 14));
         groupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
         groupList.setFixedCellHeight(36);
         groupList.setSelectionBackground(UIStyle.SELECTION);
         groupList.setSelectionForeground(UIStyle.TEXT_DARK);
         groupList.addListSelectionListener((e) -> {
            if (!e.getValueIsAdjusting()) {
               loadOptions();
            }
         });
         JButton addGroup = compact(UIStyle.button("+ Group", UIStyle.PURPLE_LIGHT));
         addGroup.addActionListener((e) -> showGroupDialog(null));
         JButton editGroup = compact(UIStyle.button("Edit", UIStyle.PURPLE_DARK));
         editGroup.addActionListener((e) -> {
            Restaurant.ModifierGroup group = selectedGroup();
            if (group == null) {
               pickGroupFirst();
            } else {
               showGroupDialog(group);
            }
         });
         JButton deleteGroup = compact(UIStyle.button("Delete", UIStyle.RED));
         deleteGroup.addActionListener((e) -> deleteGroup());

         JLabel groupsHeading = new JLabel("Modifier Groups");
         groupsHeading.setFont(UIStyle.font(Font.BOLD, 16));
         groupsHeading.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
         JScrollPane groupScroll = new JScrollPane(groupList);
         groupScroll.setBorder(BorderFactory.createLineBorder(UIStyle.BORDER_GRAY));
         JPanel groupButtons = UIStyle.buttonRow(addGroup, editGroup, deleteGroup);
         groupButtons.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
         UIStyle.RoundedPanel left = UIStyle.card(UIStyle.PURPLE_DARK);
         left.setPadding(20, 26, 20, 20);
         left.setPreferredSize(new Dimension(330, 100));
         left.add(groupsHeading, BorderLayout.NORTH);
         left.add(groupScroll, BorderLayout.CENTER);
         left.add(groupButtons, BorderLayout.SOUTH);

         groupTitle.setFont(UIStyle.font(Font.BOLD, 18));
         groupTitle.setForeground(UIStyle.TEXT_DARK);
         requiredBox.setOpaque(false);
         requiredBox.setFont(UIStyle.font(Font.PLAIN, 14));
         requiredBox.addActionListener((e) -> {
            Restaurant.ModifierGroup group = selectedGroup();
            if (group != null) {
               group.required = requiredBox.isSelected();
               data.notifyChanged();
            }
         });
         JPanel rightTop = new JPanel(new BorderLayout());
         rightTop.setOpaque(false);
         rightTop.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
         rightTop.add(groupTitle, BorderLayout.WEST);
         rightTop.add(requiredBox, BorderLayout.EAST);
         JScrollPane optionScroll = new JScrollPane(optionTable);
         optionScroll.setBorder(BorderFactory.createLineBorder(UIStyle.BORDER_GRAY));
         optionScroll.getViewport().setBackground(UIStyle.WHITE);
         JButton addOption = UIStyle.primaryButton("+ Add Item");
         addOption.addActionListener((e) -> showOptionDialog(null));
         JButton editOption = UIStyle.button("Edit", UIStyle.PURPLE_DARK);
         editOption.addActionListener((e) -> editOption());
         JButton removeOption = UIStyle.button("Remove", UIStyle.RED);
         removeOption.addActionListener((e) -> removeOption());
         JPanel optionButtons = UIStyle.buttonRow(addOption, editOption, removeOption);
         optionButtons.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
         UIStyle.RoundedPanel right = UIStyle.card(UIStyle.YELLOW_ACCENT);
         right.setPadding(20, 26, 20, 20);
         right.add(rightTop, BorderLayout.NORTH);
         right.add(optionScroll, BorderLayout.CENTER);
         right.add(optionButtons, BorderLayout.SOUTH);

         JPanel body = new JPanel(new BorderLayout(18, 0));
         body.setOpaque(false);
         body.add(left, BorderLayout.WEST);
         body.add(right, BorderLayout.CENTER);
         setContentPane(UIStyle.page("\uD83E\uDDE9", "Modifier & Menu Builder", null, body));
         UIStyle.bind(this, () -> refreshGroups(groupList.getSelectedIndex()));
         refreshGroups(0);
         setVisible(true);
      }

      private JButton compact(JButton button) {
         button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
         return button;
      }

      private Restaurant.ModifierGroup selectedGroup() {
         int index = groupList.getSelectedIndex();
         return index >= 0 && index < data.modifierGroups.size() ? data.modifierGroups.get(index) : null;
      }

      private void pickGroupFirst() {
         JOptionPane.showMessageDialog(this, "Please select a modifier group first.", "Nothing selected", JOptionPane.INFORMATION_MESSAGE);
      }

      private void refreshGroups(int select) {
         groupModel.clear();
         for (Restaurant.ModifierGroup group : data.modifierGroups) {
            groupModel.addElement(group.name + (group.required ? "  (required)" : "  (optional)"));
         }
         if (select >= 0 && select < groupModel.size()) {
            groupList.setSelectedIndex(select);
         }
         loadOptions();
      }

      private void loadOptions() {
         optionModel.setRowCount(0);
         Restaurant.ModifierGroup group = selectedGroup();
         if (group == null) {
            groupTitle.setText("Select a group");
            requiredBox.setEnabled(false);
            requiredBox.setSelected(false);
            return;
         }
         groupTitle.setText(group.name);
         requiredBox.setEnabled(true);
         requiredBox.setSelected(group.required);
         for (Restaurant.ModifierOption option : group.options) {
            optionModel.addRow(new Object[]{option.name, MoneyFormat.pesoSigned(option.priceChange)});
         }
      }

      private void showGroupDialog(Restaurant.ModifierGroup group) {
         JTextField nameField = UIStyle.styledField(group == null ? "" : group.name);
         JCheckBox requiredCheck = new JCheckBox("Customer must choose one", group != null && group.required);
         requiredCheck.setOpaque(false);
         JPanel panel = UIStyle.form(new String[]{"Group Name:", "Required:"}, new JComponent[]{nameField, requiredCheck});
         int result = JOptionPane.showConfirmDialog(this, panel, group == null ? "Add Modifier Group" : "Edit Modifier Group", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
         if (result != JOptionPane.OK_OPTION) {
            return;
         }
         String name = nameField.getText().trim();
         if (name.isEmpty()) {
            UIStyle.warn(this, "Group name can't be empty.");
         } else if (group == null) {
            data.modifierGroups.add(new Restaurant.ModifierGroup(name, requiredCheck.isSelected()));
            data.notifyChanged();
            groupList.setSelectedIndex(groupModel.size() - 1);
         } else {
            group.name = name;
            group.required = requiredCheck.isSelected();
            data.notifyChanged();
         }
      }

      private void deleteGroup() {
         Restaurant.ModifierGroup group = selectedGroup();
         if (group == null) {
            pickGroupFirst();
         } else if (UIStyle.confirm(this, "Delete the group \"" + group.name + "\" and all its items?", "Delete Group")) {
            data.modifierGroups.remove(group);
            data.notifyChanged();
         }
      }

      private void editOption() {
         Restaurant.ModifierGroup group = selectedGroup();
         if (group == null) {
            pickGroupFirst();
            return;
         }
         int row = UIStyle.requireSelection(optionTable, this, "an item");
         if (row >= 0) {
            showOptionDialog(group.options.get(row));
         }
      }

      private void removeOption() {
         Restaurant.ModifierGroup group = selectedGroup();
         if (group == null) {
            pickGroupFirst();
            return;
         }
         int row = UIStyle.requireSelection(optionTable, this, "an item");
         if (row >= 0) {
            Restaurant.ModifierOption option = group.options.get(row);
            if (UIStyle.confirm(this, "Remove \"" + option.name + "\" from " + group.name + "?", "Remove Item")) {
               group.options.remove(row);
               data.notifyChanged();
            }
         }
      }

      private void showOptionDialog(Restaurant.ModifierOption option) {
         Restaurant.ModifierGroup group = selectedGroup();
         if (group == null) {
            pickGroupFirst();
            return;
         }
         JTextField nameField = UIStyle.styledField(option == null ? "" : option.name);
         JTextField amountField = UIStyle.styledField(option == null ? "0" : MoneyFormat.amountText(option.priceChange));
         JLabel hint = new JLabel("Use a minus sign for a discount, e.g. -10");
         hint.setFont(UIStyle.font(Font.PLAIN, 12));
         hint.setForeground(UIStyle.MUTED);
         JPanel panel = UIStyle.form(new String[]{"Item Name:", "Price Change (\u20B1):", ""}, new JComponent[]{nameField, amountField, hint});
         int result = JOptionPane.showConfirmDialog(this, panel, option == null ? "Add Item to " + group.name : "Edit Item", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
         if (result != JOptionPane.OK_OPTION) {
            return;
         }
         Double amount = MoneyFormat.parseAmount(amountField.getText());
         String name = nameField.getText().trim();
         if (name.isEmpty()) {
            UIStyle.warn(this, "Item name can't be empty.");
         } else if (amount == null) {
            UIStyle.warn(this, "Please enter a valid price change, for example 25, 0 or -10.");
         } else {
            if (option == null) {
               group.options.add(new Restaurant.ModifierOption(name, amount));
            } else {
               option.name = name;
               option.priceChange = amount;
            }
            data.notifyChanged();
         }
      }
   }
}