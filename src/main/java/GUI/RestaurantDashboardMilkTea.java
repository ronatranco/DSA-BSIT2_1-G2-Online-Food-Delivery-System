package GUI;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.time.*;
import java.time.format.TextStyle;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;
import Model.Restaurant;
import Model.Restaurant.MoneyFormat;

public class RestaurantDashboardMilkTea extends JPanel {
   static Restaurant DATA = Restaurant.get(Restaurant.Brand.BRAIN_CELLS_MILK_TEA);
   private final Restaurant data = DATA;

   private static final int NAV_COLLAPSED = 80;
   private static final int NAV_EXPANDED = 240;
   private static final int PAGE_X = 90;
   private static final int PAGE_W = 934;
   private static final int PAGE_H = 630;

   private final JPanel pnlMainBg;
   private final JPanel leftBannerNav = new JPanel();
   private final JPanel homePanel = new JPanel(new BorderLayout());
   private final JLabel welcome = new JLabel();
   private final UIStyle.Chip statusChip = new UIStyle.Chip();
   private final UIStyle.Notice homeNotice = new UIStyle.Notice();
   private JPanel content = new JPanel();
   private JLabel lblLogoPanda;
   private JLabel lblRestaurant;
   private NavButton btnNavExit;
   private final Map<String, JComponent> pages = new LinkedHashMap<>();
   private final Map<String, NavButton> navButtons = new LinkedHashMap<>();
   private final List<NavButton> allNav = new ArrayList<>();

   public RestaurantDashboardMilkTea(MainFrame frame) {
      setLayout(null);
      setBounds(0, 0, 1024, 720);

      pnlMainBg = new JPanel() {
         @Override
         public boolean isOptimizedDrawingEnabled() {
            return false;
         }
      };
      pnlMainBg.setLayout(null);
      pnlMainBg.setBackground(UIStyle.BG_LIGHT);
      pnlMainBg.setBounds(0, 0, 1024, 720);

      buildNav();
      buildHome();

      addPage("home", homePanel);
      addPage("store", new StoreStatusPage());
      addPage("hours", new HoursPage());
      addPage("menu", new FoodItemsPage());
      addPage("incoming", new IncomingOrdersPage());
      addPage("orders", new OrderStatusPage());
      addPage("profile", new ProfilePage());

      pnlMainBg.add(leftBannerNav);
      pnlMainBg.setComponentZOrder(leftBannerNav, 0);
      add(pnlMainBg);

      data.addListener(this::refresh);
      data.startScheduler();
      refresh();
      showPage("home");
   }

   public JButton getBtnLogout() {
      return btnNavExit;
   }

   private void addPage(String key, JComponent page) {
      page.setBounds(PAGE_X, 0, PAGE_W, PAGE_H);
      page.setVisible(false);
      pnlMainBg.add(page);
      pages.put(key, page);
   }

   private void showPage(String key) {
      JComponent target = pages.get(key);
      for (Map.Entry<String, JComponent> entry : pages.entrySet()) {
         entry.getValue().setVisible(entry.getKey().equals(key));
      }
      for (Map.Entry<String, NavButton> entry : navButtons.entrySet()) {
         entry.getValue().setActive(entry.getKey().equals(key));
      }
      if (target instanceof Page) {
         ((Page)target).onShow();
      } else if (target == homePanel) {
         homeNotice.clear();
         refresh();
      }
      pnlMainBg.revalidate();
      pnlMainBg.repaint();
   }

   private void buildNav() {
      leftBannerNav.setLayout(null);
      leftBannerNav.setBackground(UIStyle.PURPLE_DARK);
      leftBannerNav.setBounds(0, 0, NAV_COLLAPSED, 720);

      JLabel panda = new JLabel("\uD83D\uDC3C", SwingConstants.CENTER);
      panda.setFont(UIStyle.emojiFont(Font.PLAIN, 28));
      panda.setForeground(UIStyle.YELLOW_ACCENT);
      panda.setBounds(0, 24, NAV_COLLAPSED, 44);
      leftBannerNav.add(panda);

      lblLogoPanda = new JLabel("Brain Cells Milk Tea");
      lblLogoPanda.setFont(UIStyle.font(Font.BOLD, 13));
      lblLogoPanda.setForeground(UIStyle.WHITE);
      lblLogoPanda.setBounds(68, 24, NAV_EXPANDED - 76, 26);
      lblLogoPanda.setVisible(false);
      leftBannerNav.add(lblLogoPanda);

      lblRestaurant = new JLabel("RESTAURANT");
      lblRestaurant.setFont(UIStyle.font(Font.BOLD, 11));
      lblRestaurant.setForeground(UIStyle.YELLOW_ACCENT);
      lblRestaurant.setBounds(68, 48, 120, 18);
      lblRestaurant.setVisible(false);
      leftBannerNav.add(lblRestaurant);

      String[][] items = {
         {"home", "\uD83C\uDFE0", "Dashboard"},
         {"store", "\uD83C\uDFEC", "Store Status"},
         {"hours", "\uD83D\uDD52", "Operating Hours"},
         {"menu", "\uD83C\uDF7D", "Menu Items"},
         {"incoming", "\uD83D\uDCE5", "Incoming Orders"},
         {"orders", "\uD83D\uDCCB", "Order Status"},
         {"profile", "\uD83C\uDFEA", "Restaurant Profile"}
      };
      int y = 105;
      for (String[] item : items) {
         String key = item[0];
         NavButton button = new NavButton(item[1], item[2]);
         button.setBounds(0, y, NAV_COLLAPSED, 45);
         button.addActionListener((e) -> showPage(key));
         leftBannerNav.add(button);
         navButtons.put(key, button);
         allNav.add(button);
         y += 58;
      }

      btnNavExit = new NavButton("[\u2190", "Logout");
      btnNavExit.setBounds(0, 560, NAV_COLLAPSED, 45);
      leftBannerNav.add(btnNavExit);
      allNav.add(btnNavExit);

      MouseAdapter hover = new MouseAdapter() {
         @Override
         public void mouseEntered(MouseEvent e) {
            setNavExpanded(true);
         }

         @Override
         public void mouseExited(MouseEvent e) {
            PointerInfo info = MouseInfo.getPointerInfo();
            if (info == null) {
               setNavExpanded(false);
               return;
            }
            Point p = info.getLocation();
            SwingUtilities.convertPointFromScreen(p, leftBannerNav);
            if (!leftBannerNav.contains(p)) {
               setNavExpanded(false);
            }
         }
      };
      leftBannerNav.addMouseListener(hover);
      for (Component c : leftBannerNav.getComponents()) {
         c.addMouseListener(hover);
      }
   }

   private void setNavExpanded(boolean expanded) {
      int width = expanded ? NAV_EXPANDED : NAV_COLLAPSED;
      leftBannerNav.setBounds(0, 0, width, 720);
      for (NavButton button : allNav) {
         button.setBounds(0, button.getY(), width, 45);
         button.setExpanded(expanded);
      }
      lblLogoPanda.setVisible(expanded);
      lblRestaurant.setVisible(expanded);
      leftBannerNav.revalidate();
      pnlMainBg.repaint();
   }

   private static class NavButton extends JButton {
      private final String icon;
      private final String label;
      private boolean active;

      NavButton(String icon, String label) {
         super(icon);
         this.icon = icon;
         this.label = label;
         setContentAreaFilled(false);
         setOpaque(false);
         setFocusPainted(false);
         setRolloverEnabled(true);
         setForeground(UIStyle.WHITE);
         setFont(UIStyle.emojiFont(Font.PLAIN, 18));
         setCursor(new Cursor(Cursor.HAND_CURSOR));
         setExpanded(false);
      }

      void setActive(boolean active) {
         this.active = active;
         repaint();
      }

      void setExpanded(boolean expanded) {
         if (expanded) {
            setText(icon + "   " + label);
            setHorizontalAlignment(SwingConstants.LEFT);
            setBorder(BorderFactory.createEmptyBorder(9, 26, 9, 10));
         } else {
            setText(icon);
            setHorizontalAlignment(SwingConstants.CENTER);
            setBorder(BorderFactory.createEmptyBorder(9, 0, 9, 0));
         }
      }

      @Override
      protected void paintComponent(Graphics g) {
         boolean hot = getModel().isRollover() || getModel().isPressed();
         if (active || hot) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setColor(new Color(255, 255, 255, active ? 55 : 35));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(UIStyle.YELLOW_ACCENT);
            g2.fillRect(0, 0, 4, getHeight());
            g2.dispose();
         }
         super.paintComponent(g);
      }
   }

   private void buildHome() {
      homePanel.setBackground(UIStyle.BG_LIGHT);
      JPanel north = new JPanel(new BorderLayout());
      north.setOpaque(false);
      north.add(buildHeader(), BorderLayout.NORTH);
      
      JPanel noticeWrap = new JPanel(new BorderLayout());
      noticeWrap.setOpaque(false);
      noticeWrap.setBorder(BorderFactory.createEmptyBorder(8, 24, 0, 24));
      noticeWrap.add(homeNotice, BorderLayout.CENTER);
      north.add(noticeWrap, BorderLayout.CENTER);
      homePanel.add(north, BorderLayout.NORTH);
      homePanel.add(content, BorderLayout.CENTER);
   }

   private void refresh() {
      welcome.setText("Welcome back, Brain Cells Milk Tea!");
      Restaurant.StoreStatus status = data.getStoreStatus();
      statusChip.set("\u25CF  " + status.getLabel(), UIStyle.storeColor(status));
      homePanel.remove(content);
      content = buildContent();
      homePanel.add(content, BorderLayout.CENTER);
      homePanel.revalidate();
      homePanel.repaint();
   }

   private JPanel buildHeader() {
      JPanel header = new JPanel(new BorderLayout());
      header.setBackground(UIStyle.WHITE);
      header.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, UIStyle.YELLOW_ACCENT), BorderFactory.createEmptyBorder(12, 24, 10, 24)));
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
      JPanel panel = new JPanel(new GridLayout(2, 1, 14, 14));
      panel.setBackground(UIStyle.BG_LIGHT);
      panel.setBorder(BorderFactory.createEmptyBorder(12, 24, 14, 24));
      
      JPanel stats = new JPanel(new GridLayout(1, 4, 15, 0));
      stats.setBackground(UIStyle.BG_LIGHT);
      stats.add(UIStyle.statCard("MENU ITEMS", String.valueOf(data.restaurant.menuItems.size()), data.availableDishCount() + " available", UIStyle.PURPLE_LIGHT));
      stats.add(UIStyle.statCard("ORDERS", String.valueOf(data.orders.size()), "Today's orders", UIStyle.YELLOW_ACCENT));
      stats.add(UIStyle.statCard("PENDING", String.valueOf(data.pendingCount()), "Waiting to be accepted", UIStyle.ORANGE));
      stats.add(UIStyle.statCard("REVENUE", MoneyFormat.peso(data.revenue()), "Today's revenue", UIStyle.GREEN));

      UIStyle.RoundedPanel quick = UIStyle.card(UIStyle.PURPLE_DARK);
      quick.setPadding(14, 26, 16, 24);
      JLabel quickTitle = new JLabel("QUICK ACTIONS");
      quickTitle.setFont(UIStyle.font(Font.BOLD, 18));
      quickTitle.setForeground(UIStyle.TEXT_DARK);
      quickTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
      
      JPanel quickButtons = new JPanel(new GridLayout(2, 3, 12, 12));
      quickButtons.setOpaque(false);
      addQuick(quickButtons, "Manage Menu Items", () -> showPage("menu"));
      addQuick(quickButtons, "Incoming Orders", () -> showPage("incoming"));
      addQuick(quickButtons, "Order Status", () -> showPage("orders"));
      addQuick(quickButtons, "View Analytics", () -> homeNotice.info("Analytics is not available yet."));
      addQuick(quickButtons, "Operating Hours", () -> showPage("hours"));
      addQuick(quickButtons, "Restaurant Profile", () -> showPage("profile"));
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

   static abstract class Page extends JPanel {
      final Restaurant data = DATA;
      final UIStyle.Notice notice = new UIStyle.Notice();

      Page() {
         super(new BorderLayout());
         setOpaque(false);
      }

      void onShow() {
         notice.clear();
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
            case "out for delivery":
            case "delivered":
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

      public static int requireSelection(JTable table, Notice notice, String what) {
         int row = table.getSelectedRow();
         if (row < 0) {
            notice.warn("Please select " + what + " first.");
         }
         return row;
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

      public static JTextArea wrapText(String text, int size, Color color, int width, int height) {
         JTextArea area = new JTextArea(text);
         area.setFont(font(Font.PLAIN, size));
         area.setForeground(color);
         area.setEditable(false);
         area.setFocusable(false);
         area.setOpaque(false);
         area.setLineWrap(true);
         area.setWrapStyleWord(true);
         area.setBorder(null);
         area.setCursor(Cursor.getDefaultCursor());
         area.setPreferredSize(new Dimension(width, height));
         return area;
      }

      public static JPanel page(String icon, String title, JComponent action, Notice notice, JComponent body) {
         JPanel main = new JPanel(new BorderLayout(0, 10));
         main.setBackground(BG_LIGHT);
         main.setBorder(BorderFactory.createEmptyBorder(12, 22, 12, 22));
         
         JPanel header = new JPanel(new BorderLayout());
         header.setBackground(WHITE);
         header.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, YELLOW_ACCENT), BorderFactory.createEmptyBorder(10, 24, 8, 24)));
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
         JPanel top = new JPanel(new BorderLayout(0, 10));
         top.setOpaque(false);
         top.add(header, BorderLayout.NORTH);
         if (notice != null) {
            top.add(notice, BorderLayout.CENTER);
         }
         main.add(top, BorderLayout.NORTH);
         main.add(body, BorderLayout.CENTER);
         return main;
      }

      public static JPanel formView(JLabel title, JComponent body, JComponent buttons, Color accent) {
         RoundedPanel card = card(accent);
         card.setPadding(22, 28, 22, 24);
         title.setFont(font(Font.BOLD, 18));
         title.setForeground(TEXT_DARK);
         title.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));
         buttons.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));
         card.add(title, BorderLayout.NORTH);
         card.add(body, BorderLayout.CENTER);
         card.add(buttons, BorderLayout.SOUTH);
         
         JPanel wrap = new JPanel(new BorderLayout());
         wrap.setOpaque(false);
         wrap.add(card, BorderLayout.NORTH);
         return wrap;
      }

      public static RoundedPanel card(Color accent) {
         RoundedPanel card = new RoundedPanel(18, WHITE, BORDER_GRAY, accent);
         card.setLayout(new BorderLayout());
         return card;
      }

      public static RoundedPanel statCard(String title, String value, String description, Color accent) {
         RoundedPanel card = new RoundedPanel(18, WHITE, BORDER_GRAY, accent);
         card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
         card.setPadding(14, 22, 14, 12);
         
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
         card.setPadding(10, 20, 10, 14);
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

      public static RoundedButton compact(RoundedButton button) {
         button.setBorder(BorderFactory.createEmptyBorder(9, 10, 9, 10));
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

      static class Notice extends JPanel {
         private final JLabel label = new JLabel();
         private final JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
         private final javax.swing.Timer timer;
         private Color color = PURPLE_DARK;

         Notice() {
            super(new BorderLayout(12, 0));
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(9, 18, 9, 12));
            label.setFont(font(Font.BOLD, 13));
            label.setForeground(WHITE);
            buttons.setOpaque(false);
            add(label, BorderLayout.CENTER);
            add(buttons, BorderLayout.EAST);
            timer = new javax.swing.Timer(4500, (e) -> clear());
            timer.setRepeats(false);
            setVisible(false);
         }

         void info(String message) {
            show(message, PURPLE_DARK);
         }

         void success(String message) {
            show(message, GREEN);
         }

         void warn(String message) {
            show(message, ORANGE);
         }

         void error(String message) {
            show(message, RED);
         }

         private void show(String message, Color newColor) {
            timer.stop();
            buttons.removeAll();
            color = newColor;
            label.setText(message);
            setVisible(true);
            timer.restart();
            relayout();
         }

         void confirm(String message, Runnable onYes) {
            timer.stop();
            buttons.removeAll();
            color = PURPLE_DARK;
            label.setText(message);
            RoundedButton yes = button("Yes", YELLOW_ACCENT);
            yes.setForeground(TEXT_DARK);
            yes.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
            yes.addActionListener((e) -> {
               clear();
               onYes.run();
            });
            RoundedButton no = button("No", PURPLE_LIGHT);
            no.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
            no.addActionListener((e) -> clear());
            buttons.add(yes);
            buttons.add(no);
            setVisible(true);
            relayout();
         }

         void clear() {
            timer.stop();
            buttons.removeAll();
            setVisible(false);
            relayout();
         }

         private void relayout() {
            revalidate();
            repaint();
            Container parent = getParent();
            if (parent != null) {
               parent.revalidate();
               parent.repaint();
            }
         }

         @Override
         protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            g2.dispose();
            super.paintComponent(g);
         }
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

   static class StoreStatusPage extends Page {
      private final UIStyle.Chip chip = new UIStyle.Chip();
      private final JTextArea scheduleNote = UIStyle.wrapText("", 12, UIStyle.MUTED, 780, 40);
      private final Map<Restaurant.StoreStatus, UIStyle.RoundedButton> buttons = new EnumMap<>(Restaurant.StoreStatus.class);

      StoreStatusPage() {
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
         
         JPanel noteWrap = new JPanel(new BorderLayout());
         noteWrap.setOpaque(false);
         noteWrap.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));
         noteWrap.add(scheduleNote, BorderLayout.CENTER);
         UIStyle.RoundedPanel card = UIStyle.card(UIStyle.PURPLE_LIGHT);
         card.setPadding(22, 30, 22, 24);
         card.add(statusRow, BorderLayout.NORTH);
         card.add(options, BorderLayout.CENTER);
         card.add(noteWrap, BorderLayout.SOUTH);
         
         JPanel body = new JPanel(new BorderLayout());
         body.setOpaque(false);
         body.add(card, BorderLayout.NORTH);
         add(UIStyle.page("\uD83C\uDFEC", "Store Status", null, notice, body), BorderLayout.CENTER);
         data.addListener(this::refresh);
         refresh();
      }

      private JPanel optionCard(String icon, Restaurant.StoreStatus status, String description) {
         Color color = UIStyle.storeColor(status);
         UIStyle.RoundedPanel body = new UIStyle.RoundedPanel(18, UIStyle.BG_LIGHT, UIStyle.BORDER_GRAY, color);
         body.setLayout(new BorderLayout(0, 12));
         body.setPadding(18, 22, 18, 16);
         
         JLabel heading = new JLabel(icon + "  " + status.getLabel());
         heading.setFont(UIStyle.emojiFont(Font.BOLD, 18));
         heading.setForeground(UIStyle.TEXT_DARK);
         
         JTextArea text = UIStyle.wrapText(description, 13, UIStyle.MUTED, 180, 84);
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
            ? "Auto schedule is ON: the status changes by itself at each day's opening and closing time. A change you make here stays until the next scheduled opening or closing."
            : "Auto schedule is OFF: the status only changes when you change it here.");
      }

      @Override
      void onShow() {
         super.onShow();
         refresh();
      }
   }

   static class HoursPage extends Page {
      private final JCheckBox autoBox = new JCheckBox("Automatically open and close the store using this schedule");
      private final JCheckBox[] dayEnabled = new JCheckBox[7];
      private final JSpinner[] openTime = new JSpinner[7];
      private final JSpinner[] closeTime = new JSpinner[7];
      private final JLabel nowLabel = new JLabel();

      HoursPage() {
         autoBox.setSelected(data.isAutoSchedule());
         autoBox.setFont(UIStyle.font(Font.PLAIN, 14));
         autoBox.setOpaque(false);
         JTextArea hint = UIStyle.wrapText("Set the opening and closing time for each day. If the closing time is earlier than the opening time, the store closes after midnight.", 12, UIStyle.MUTED, 780, 34);
         JPanel top = new JPanel(new BorderLayout(0, 6));
         top.setOpaque(false);
         top.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
         top.add(autoBox, BorderLayout.NORTH);
         top.add(hint, BorderLayout.CENTER);

         JPanel grid = new JPanel(new GridLayout(0, 4, 15, 8));
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
         bottom.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));
         bottom.add(UIStyle.buttonRow(save, copyMonday), BorderLayout.NORTH);
         bottom.add(nowLabel, BorderLayout.CENTER);

         UIStyle.RoundedPanel card = UIStyle.card(UIStyle.YELLOW_ACCENT);
         card.setPadding(18, 30, 18, 24);
         card.add(top, BorderLayout.NORTH);
         card.add(gridWrap, BorderLayout.CENTER);
         card.add(bottom, BorderLayout.SOUTH);
         JPanel body = new JPanel(new BorderLayout());
         body.setOpaque(false);
         body.add(card, BorderLayout.NORTH);
         add(UIStyle.page("\uD83D\uDD52", "Operating Hours", null, notice, body), BorderLayout.CENTER);
         updateNowLabel();
      }

      private Date dateOf(LocalTime time) {
         return Date.from(LocalDate.now().atTime(time).atZone(ZoneId.systemDefault()).toInstant());
      }

      private JSpinner timeSpinner(LocalTime time) {
         JSpinner spinner = new JSpinner(new SpinnerDateModel(dateOf(time), null, null, Calendar.MINUTE));
         spinner.setEditor(new JSpinner.DateEditor(spinner, "hh:mm a"));
         spinner.setFont(UIStyle.font(Font.PLAIN, 14));
         return spinner;
      }

      private LocalTime toTime(JSpinner spinner) {
         return ((Date)spinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalTime().withSecond(0).withNano(0);
      }

      private void loadFromData() {
         autoBox.setSelected(data.isAutoSchedule());
         for (int i = 0; i < 7; ++i) {
            Restaurant.DaySchedule day = data.schedule.get(i);
            dayEnabled[i].setSelected(day.enabled);
            openTime[i].setValue(dateOf(day.open));
            closeTime[i].setValue(dateOf(day.close));
            updateRow(i);
         }
         updateNowLabel();
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
         notice.info("Monday's hours were copied to all days. Click Save Schedule to keep them.");
      }

      private void saveSchedule() {
         for (int i = 0; i < 7; ++i) {
            if (dayEnabled[i].isSelected() && toTime(openTime[i]).equals(toTime(closeTime[i]))) {
               String dayName = data.schedule.get(i).day.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
               notice.warn(dayName + ": the opening and closing time can't be the same.");
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
         notice.success("Schedule saved.");
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

      @Override
      void onShow() {
         super.onShow();
         loadFromData();
      }
   }

   static class IncomingOrdersPage extends Page {
      private final JTable table = UIStyle.createTable(UIStyle.ORDER_COLUMNS);
      private final DefaultTableModel model = (DefaultTableModel)table.getModel();
      private final List<Restaurant.Order> shown = new ArrayList<>();
      private final JLabel storeNote = new JLabel();

      IncomingOrdersPage() {
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
         add(UIStyle.page("\uD83D\uDCE5", "Incoming Orders", simulate, notice, body), BorderLayout.CENTER);
         data.addListener(this::refresh);
         refresh();
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
         int row = UIStyle.requireSelection(table, notice, "an order");
         if (row >= 0) {
            Restaurant.Order order = shown.get(row);
            data.setOrderStatus(order, Restaurant.Order.PREPARING);
            notice.success("Order " + order.id + " accepted.");
         }
      }

      private void rejectSelected() {
         int row = UIStyle.requireSelection(table, notice, "an order");
         if (row >= 0) {
            Restaurant.Order order = shown.get(row);
            notice.confirm("Reject order " + order.id + " from " + order.customer + "?", () -> data.setOrderStatus(order, Restaurant.Order.CANCELLED));
         }
      }

      private void simulateOrder() {
         if (data.simulateIncomingOrder() == null) {
            if (data.getStoreStatus() == Restaurant.StoreStatus.OPEN) {
               notice.warn("There are no available menu items to order.");
            } else {
               notice.warn("The store status is \"" + data.getStoreStatus().getLabel() + "\", so new orders are not accepted.");
            }
         }
      }

      @Override
      void onShow() {
         super.onShow();
         refresh();
      }
   }

   static class OrderStatusPage extends Page {
      private final JTable table = UIStyle.createTable(UIStyle.ORDER_COLUMNS);
      private final DefaultTableModel model = (DefaultTableModel)table.getModel();
      private final List<Restaurant.Order> shown = new ArrayList<>();
      private final JTextField search = UIStyle.styledField("");
      private final JComboBox<String> filter = new JComboBox<>(new String[]{"All", "Pending", "Preparing", "Out for Delivery", "Delivered", "Cancelled", "Failed"});

      OrderStatusPage() {
         search.setColumns(14);
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
         
         JButton ready = UIStyle.button("Mark Out for Delivery", UIStyle.PURPLE_DARK);
         ready.addActionListener((e) -> changeStatus(Restaurant.Order.READY));
         
         JButton completed = UIStyle.button("Mark Delivered", UIStyle.GREEN);
         completed.addActionListener((e) -> changeStatus(Restaurant.Order.COMPLETED));
         
         JButton cancel = UIStyle.button("Cancel Order", UIStyle.RED);
         cancel.addActionListener((e) -> changeStatus(Restaurant.Order.CANCELLED));
         
         JPanel body = new JPanel(new BorderLayout(0, 12));
         body.setOpaque(false);
         body.add(filterBar, BorderLayout.NORTH);
         body.add(UIStyle.tableCard(table), BorderLayout.CENTER);
         body.add(UIStyle.buttonRow(preparing, ready, completed, cancel), BorderLayout.SOUTH);
         add(UIStyle.page("\uD83D\uDCCB", "Order Status", null, notice, body), BorderLayout.CENTER);
         data.addListener(this::refresh);
         refresh();
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
         int row = UIStyle.requireSelection(table, notice, "an order");
         if (row >= 0) {
            Restaurant.Order order = shown.get(row);
            if (order.isFinal()) {
               notice.warn(order.id + " is already " + order.status + " and can't be changed.");
            } else {
               data.setOrderStatus(order, newStatus);
               notice.success(order.id + " is now " + newStatus + ".");
            }
         }
      }

      @Override
      void onShow() {
         super.onShow();
         refresh();
      }
   }

   static class ProfilePage extends Page {
      private final JTextField restaurantName = UIStyle.styledField("");
      private final JTextField location = UIStyle.styledField("");
      private final JTextField ownerName = UIStyle.styledField("");
      private final JTextField contact = UIStyle.styledField("");
      private final JTextField cuisine = UIStyle.styledField("");

      ProfilePage() {
         loadFields();
         JButton save = UIStyle.primaryButton("Save Changes");
         save.addActionListener((e) -> saveProfile());
         JButton undo = UIStyle.button("Undo Changes", UIStyle.PURPLE_DARK);
         undo.addActionListener((e) -> {
            loadFields();
            notice.info("Changes were undone.");
         });
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
         add(UIStyle.page("\uD83C\uDFEA", "Restaurant Profile", null, notice, body), BorderLayout.CENTER);
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
            notice.warn("Restaurant name and location can't be empty.");
            return;
         }
         Restaurant.Restaurantt restaurant = data.restaurant;
         restaurant.name = restaurantName.getText().trim();
         restaurant.location = location.getText().trim();
         restaurant.ownerName = ownerName.getText().trim();
         restaurant.contact = contact.getText().trim();
         restaurant.cuisine = cuisine.getText().trim();
         data.notifyChanged();
         notice.success("Restaurant profile updated successfully!");
      }

      @Override
      void onShow() {
         super.onShow();
         loadFields();
      }
   }

   static class FoodItemsPage extends Page {
      private final JTable table = UIStyle.createTable(new String[]{"Food Name", "Category", "Price", "Availability", "Sold"});
      private final DefaultTableModel model = (DefaultTableModel)table.getModel();
      private final CardLayout cardLayout = new CardLayout();
      private final JPanel cards = new JPanel(cardLayout);
      private String currentCard = "list";

      private final JLabel detailImage = new JLabel("", SwingConstants.CENTER);
      private final JLabel detailName = new JLabel();
      private final JLabel detailMeta = new JLabel();
      private final JLabel detailStatus = new JLabel();
      private final JTextArea detailDescription = UIStyle.wrapText("", 13, UIStyle.TEXT_DARK, 240, 120);

      private final JLabel formTitle = new JLabel();
      private final JTextField nameField = UIStyle.styledField("");
      private final JComboBox<String> categoryBox = new JComboBox<>();
      private final JTextField priceField = UIStyle.styledField("");
      private final JCheckBox availableBox = new JCheckBox("Available for ordering");
      private final JTextArea descriptionArea = new JTextArea(4, 22);
      private final JLabel preview = new JLabel("", SwingConstants.CENTER);
      private final UIStyle.RoundedButton chooseButton = UIStyle.button("Choose Picture", UIStyle.PURPLE_LIGHT);
      private final UIStyle.RoundedButton removeButton = UIStyle.button("Remove Picture", UIStyle.RED);
      private String formImagePath;
      private Restaurant.Dish editing;

      private final JFileChooser chooser = new JFileChooser();
      private Consumer<String> onPicked;
      private String pickReturn = "list";

      FoodItemsPage() {
         JButton addBtn = UIStyle.primaryButton("+ Add Food Item");
         addBtn.addActionListener((e) -> openForm(null));
         
         UIStyle.RoundedButton edit = UIStyle.compact(UIStyle.button("Edit Selected", UIStyle.PURPLE_DARK));
         edit.addActionListener((e) -> editSelected());
         
         UIStyle.RoundedButton toggle = UIStyle.compact(UIStyle.button("Toggle Availability", UIStyle.ORANGE));
         toggle.addActionListener((e) -> toggleSelected());
         
         UIStyle.RoundedButton changePicture = UIStyle.compact(UIStyle.button("Change Picture", UIStyle.PURPLE_LIGHT));
         changePicture.addActionListener((e) -> changePictureOfSelected());
         
         UIStyle.RoundedButton removePicture = UIStyle.compact(UIStyle.button("Remove Picture", UIStyle.RED));
         removePicture.addActionListener((e) -> removePictureOfSelected());
         
         UIStyle.RoundedButton delete = UIStyle.compact(UIStyle.button("Delete Item", UIStyle.RED));
         delete.addActionListener((e) -> deleteSelected());
         
         JPanel actions = new JPanel(new GridLayout(2, 3, 10, 10));
         actions.setOpaque(false);
         actions.add(edit);
         actions.add(toggle);
         actions.add(changePicture);
         actions.add(removePicture);
         actions.add(delete);

         JPanel left = new JPanel(new BorderLayout(0, 12));
         left.setOpaque(false);
         left.add(UIStyle.tableCard(table), BorderLayout.CENTER);
         left.add(actions, BorderLayout.SOUTH);
         
         JPanel listView = new JPanel(new BorderLayout(18, 0));
         listView.setOpaque(false);
         listView.add(left, BorderLayout.CENTER);
         listView.add(buildDetailsCard(), BorderLayout.EAST);

         cards.setOpaque(false);
         cards.add(listView, "list");
         cards.add(buildForm(), "form");
         cards.add(buildPicker(), "image");

         int[] widths = {180, 105, 70, 105, 45};
         for (int i = 0; i < widths.length; ++i) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
         }
         table.getSelectionModel().addListSelectionListener((e) -> {
            if (!e.getValueIsAdjusting()) {
               updateDetails();
            }
         });
         add(UIStyle.page("\uD83C\uDF7D", "Menu Items", addBtn, notice, cards), BorderLayout.CENTER);
         data.addListener(this::refresh);
         refresh();
      }

      private void showCard(String name) {
         currentCard = name;
         cardLayout.show(cards, name);
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
         
         JPanel labels = new JPanel(new GridLayout(3, 1, 0, 6));
         labels.setOpaque(false);
         labels.add(detailName);
         labels.add(detailMeta);
         labels.add(detailStatus);
         
         JPanel text = new JPanel(new BorderLayout(0, 10));
         text.setOpaque(false);
         text.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));
         text.add(labels, BorderLayout.NORTH);
         text.add(detailDescription, BorderLayout.CENTER);
         
         UIStyle.RoundedPanel card = UIStyle.card(UIStyle.YELLOW_ACCENT);
         card.setPadding(20, 26, 20, 18);
         card.setPreferredSize(new Dimension(300, 100));
         card.add(detailImage, BorderLayout.NORTH);
         card.add(text, BorderLayout.CENTER);
         return card;
      }

      private JPanel buildForm() {
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
         availableBox.setOpaque(false);
         availableBox.setFont(UIStyle.font(Font.PLAIN, 14));

         preview.setPreferredSize(new Dimension(190, 190));
         preview.setOpaque(true);
         preview.setBackground(UIStyle.BG_LIGHT);
         preview.setForeground(UIStyle.MUTED);
         preview.setFont(UIStyle.font(Font.PLAIN, 13));
         preview.setBorder(BorderFactory.createLineBorder(UIStyle.BORDER_GRAY));
         chooseButton.addActionListener((e) -> pickImage((picked) -> {
            formImagePath = picked;
            updatePreview();
         }, "form"));
         removeButton.addActionListener((e) -> {
            formImagePath = null;
            updatePreview();
         });
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
         
         JPanel body = new JPanel(new BorderLayout(20, 0));
         body.setOpaque(false);
         body.add(picturePanel, BorderLayout.WEST);
         body.add(right, BorderLayout.CENTER);

         JButton save = UIStyle.primaryButton("Save Item");
         save.addActionListener((e) -> saveForm());
         JButton cancel = UIStyle.button("Cancel", UIStyle.PURPLE_DARK);
         cancel.addActionListener((e) -> {
            notice.clear();
            showCard("list");
         });
         return UIStyle.formView(formTitle, body, UIStyle.buttonRow(save, cancel), UIStyle.YELLOW_ACCENT);
      }

      private JPanel buildPicker() {
         chooser.setFileFilter(new FileNameExtensionFilter("Pictures (png, jpg, jpeg, gif)", "png", "jpg", "jpeg", "gif"));
         chooser.setApproveButtonText("Use Picture");
         chooser.addActionListener((e) -> {
            if (JFileChooser.APPROVE_SELECTION.equals(e.getActionCommand())) {
               File file = chooser.getSelectedFile();
               if (file == null) {
                  notice.warn("Please choose a picture first.");
                  return;
               }
               if (UIStyle.loadImage(file.getAbsolutePath(), 100, 100) == null) {
                  notice.warn("That file couldn't be opened as a picture. Please choose a PNG, JPG or GIF image.");
                  return;
               }
               Consumer<String> callback = onPicked;
               onPicked = null;
               notice.clear();
               if (callback != null) {
                  callback.accept(file.getAbsolutePath());
               }
               showCard(pickReturn);
            } else {
               onPicked = null;
               notice.clear();
               showCard(pickReturn);
            }
         });
         JLabel title = new JLabel("Choose a food picture");
         title.setFont(UIStyle.font(Font.BOLD, 18));
         title.setForeground(UIStyle.TEXT_DARK);
         title.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
         
         UIStyle.RoundedPanel card = UIStyle.card(UIStyle.PURPLE_LIGHT);
         card.setPadding(20, 26, 20, 20);
         card.add(title, BorderLayout.NORTH);
         card.add(chooser, BorderLayout.CENTER);
         return card;
      }

      private void pickImage(Consumer<String> callback, String backTo) {
         onPicked = callback;
         pickReturn = backTo;
         chooser.setSelectedFile(null);
         chooser.rescanCurrentDirectory();
         notice.clear();
         showCard("image");
      }

      private void updatePreview() {
         ImageIcon icon = UIStyle.loadImage(formImagePath, 180, 180);
         preview.setIcon(icon);
         preview.setText(icon != null ? "" : (formImagePath == null ? "No picture" : "Picture not found"));
         chooseButton.setText(formImagePath == null ? "Choose Picture" : "Change Picture");
         removeButton.setVisible(formImagePath != null);
      }

      private void openForm(Restaurant.Dish dish) {
         editing = dish;
         notice.clear();
         formTitle.setText(dish == null ? "Add Food Item" : "Edit Food Item");
         nameField.setText(dish == null ? "" : dish.name);
         categoryBox.setModel(new DefaultComboBoxModel<>(data.categories.toArray(new String[0])));
         categoryBox.setEditable(true);
         if (dish != null) {
            categoryBox.setSelectedItem(dish.category);
         }
         priceField.setText(dish == null ? "" : MoneyFormat.amountText(dish.price));
         availableBox.setSelected(dish == null || dish.available);
         descriptionArea.setText(dish == null ? "" : dish.description);
         formImagePath = dish == null ? null : dish.imagePath;
         updatePreview();
         showCard("form");
      }

      private void saveForm() {
         String name = nameField.getText().trim();
         String category = String.valueOf(categoryBox.getSelectedItem()).trim();
         Double price = MoneyFormat.parseAmount(priceField.getText());
         if (name.isEmpty() || category.isEmpty()) {
            notice.warn("Food name and category are required.");
            return;
         }
         if (price == null || price < 0.0) {
            notice.warn("Please enter a valid price, for example 120 or 99.50.");
            return;
         }
         data.addCategoryIfNew(category);
         Restaurant.Dish target = editing == null ? new Restaurant.Dish(name, category, price, availableBox.isSelected(), 0) : editing;
         target.name = name;
         target.category = category;
         target.price = price;
         target.available = availableBox.isSelected();
         target.description = descriptionArea.getText().trim();
         target.imagePath = formImagePath;
         if (editing == null) {
            data.restaurant.menuItems.add(target);
         }
         boolean wasNew = editing == null;
         editing = null;
         showCard("list");
         data.notifyChanged();
         notice.success("\"" + name + "\" was " + (wasNew ? "added to" : "updated in") + " the menu.");
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
         detailDescription.setText(dish.description.isEmpty() ? "No description yet. Click Edit Selected to add one." : dish.description);
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
         int row = UIStyle.requireSelection(table, notice, "an item");
         if (row >= 0) {
            openForm(data.restaurant.menuItems.get(row));
         }
      }

      private void toggleSelected() {
         int row = UIStyle.requireSelection(table, notice, "an item");
         if (row >= 0) {
            Restaurant.Dish dish = data.restaurant.menuItems.get(row);
            dish.available = !dish.available;
            data.notifyChanged();
            notice.success("\"" + dish.name + "\" is now " + (dish.available ? "available" : "unavailable") + ".");
         }
      }

      private void changePictureOfSelected() {
         int row = UIStyle.requireSelection(table, notice, "an item");
         if (row >= 0) {
            Restaurant.Dish dish = data.restaurant.menuItems.get(row);
            pickImage((path) -> {
               dish.imagePath = path;
               data.notifyChanged();
               notice.success("Picture updated for \"" + dish.name + "\".");
            }, "list");
         }
      }

      private void removePictureOfSelected() {
         int row = UIStyle.requireSelection(table, notice, "an item");
         if (row >= 0) {
            Restaurant.Dish dish = data.restaurant.menuItems.get(row);
            if (dish.imagePath == null) {
               notice.info(dish.name + " has no picture.");
            } else {
               notice.confirm("Remove the picture of \"" + dish.name + "\"?", () -> {
                  dish.imagePath = null;
                  data.notifyChanged();
               });
            }
         }
      }

      private void deleteSelected() {
         int row = UIStyle.requireSelection(table, notice, "an item");
         if (row >= 0) {
            Restaurant.Dish dish = data.restaurant.menuItems.get(row);
            notice.confirm("Delete \"" + dish.name + "\" from the menu?", () -> {
               data.restaurant.menuItems.remove(dish);
               data.notifyChanged();
            });
         }
      }

      @Override
      void onShow() {
         super.onShow();
         showCard("list");
         refresh();
      }
   }
}