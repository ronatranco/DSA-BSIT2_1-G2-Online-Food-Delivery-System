/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package GUI;
/**
 *
 * @author Encrid9
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
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

public class RestaurantDashboard extends JPanel {
   static Restaurant DATA = Restaurant.get(Restaurant.Brand.MCDOLLIBEE);
   private final Restaurant data = DATA;

   private static final int NAV_COLLAPSED = 80, NAV_EXPANDED = 240, PAGE_X = 90, PAGE_W = 934, PAGE_H = 630;

   private final JPanel pnlMainBg;
   private final JPanel leftBannerNav = new JPanel();
   private final JPanel homePanel = new JPanel(new BorderLayout());
   private final JLabel welcome = new JLabel();
   private final JLabel locationLabel = new JLabel();
   private final UIStyle.Notice homeNotice = new UIStyle.Notice();
   private JPanel content = new JPanel();
   private JLabel lblLogoPanda, lblRestaurant;
   private NavButton btnNavExit;
   private final Map<String, JComponent> pages = new LinkedHashMap<>();
   private final Map<String, NavButton> navButtons = new LinkedHashMap<>();
   private final List<NavButton> allNav = new ArrayList<>();

   private javax.swing.Timer navTimer;
   private double navWidth = NAV_COLLAPSED, navStart = NAV_COLLAPSED;
   private long navStartTime;
   private boolean navTarget = false;

   public RestaurantDashboard(MainFrame frame) {
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
      addPage("menu", new FoodItemsPage());
      addPage("incoming", new IncomingOrdersPage());
      addPage("orders", new OrderStatusPage());
      addPage("profile", new ProfilePage());

      pnlMainBg.add(leftBannerNav);
      pnlMainBg.setComponentZOrder(leftBannerNav, 0);
      add(pnlMainBg);
      data.addListener(this::refresh);
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
      pages.forEach((k, p) -> p.setVisible(k.equals(key)));
      navButtons.forEach((k, b) -> b.setActive(k.equals(key)));
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

      lblLogoPanda = new JLabel("McDollibee");
      lblLogoPanda.setFont(UIStyle.font(Font.BOLD, 18));
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
            animateNav(true);
         }

         @Override
         public void mouseExited(MouseEvent e) {
            PointerInfo info = MouseInfo.getPointerInfo();
            if (info == null) {
               animateNav(false);
               return;
            }
            Point p = info.getLocation();
            SwingUtilities.convertPointFromScreen(p, leftBannerNav);
            if (!leftBannerNav.contains(p)) {
               animateNav(false);
            }
         }
      };
      leftBannerNav.addMouseListener(hover);
      for (Component c : leftBannerNav.getComponents()) {
         c.addMouseListener(hover);
      }
   }

   private void animateNav(boolean expand) {
      if (expand == navTarget) {
         return;
      }
      navTarget = expand;
      if (navTimer != null) {
         navTimer.stop();
      }
      if (!expand) {
         setNavLabels(false);
      }
      navStart = navWidth;
      navStartTime = System.currentTimeMillis();
      navTimer = new javax.swing.Timer(4, (e) -> {
         double progress = Math.min(1.0, (System.currentTimeMillis() - navStartTime) / 280.0);
         double eased = 1.0 - Math.pow(1.0 - progress, 3);
         int target = navTarget ? NAV_EXPANDED : NAV_COLLAPSED;
         navWidth = navStart + (target - navStart) * eased;
         applyNavWidth((int)Math.round(navWidth));
         if (progress >= 1.0) {
            navTimer.stop();
            if (navTarget) {
               setNavLabels(true);
            }
         }
      });
      navTimer.start();
   }

   private void applyNavWidth(int width) {
      leftBannerNav.setBounds(0, 0, width, 720);
      for (NavButton button : allNav) {
         button.setBounds(0, button.getY(), width, 45);
      }
      leftBannerNav.revalidate();
      pnlMainBg.repaint();
   }

   private void setNavLabels(boolean expanded) {
      for (NavButton button : allNav) {
         button.setExpanded(expanded);
      }
      lblLogoPanda.setVisible(expanded);
      lblRestaurant.setVisible(expanded);
   }

   private static class NavButton extends JButton {
      private final String icon, label;
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
         setText(expanded ? icon + "   " + label : icon);
         setHorizontalAlignment(expanded ? SwingConstants.LEFT : SwingConstants.CENTER);
         setBorder(expanded ? BorderFactory.createEmptyBorder(9, 26, 9, 10) : BorderFactory.createEmptyBorder(9, 0, 9, 0));
      }

      @Override
      protected void paintComponent(Graphics g) {
         if (active || getModel().isRollover() || getModel().isPressed()) {
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
      welcome.setText("Welcome back, " + data.restaurant.name + "!");
      locationLabel.setText("\uD83D\uDCCD  " + data.restaurant.location);
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
      locationLabel.setFont(UIStyle.emojiFont(Font.BOLD, 14));
      locationLabel.setForeground(UIStyle.PURPLE_LIGHT);
      JPanel headerText = new JPanel();
      headerText.setBackground(UIStyle.WHITE);
      headerText.setLayout(new BoxLayout(headerText, BoxLayout.Y_AXIS));
      headerText.add(title);
      headerText.add(Box.createVerticalStrut(5));
      headerText.add(welcome);
      headerText.add(Box.createVerticalStrut(4));
      headerText.add(locationLabel);
      header.add(headerText, BorderLayout.WEST);
      return header;
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
      JPanel quickButtons = new JPanel(new GridLayout(2, 2, 12, 12));
      quickButtons.setOpaque(false);
      addQuick(quickButtons, "Manage Menu Items", "menu");
      addQuick(quickButtons, "Incoming Orders", "incoming");
      addQuick(quickButtons, "Order Status", "orders");
      addQuick(quickButtons, "Restaurant Profile", "profile");
      quick.add(quickTitle, BorderLayout.NORTH);
      quick.add(quickButtons, BorderLayout.CENTER);
      panel.add(stats);
      panel.add(quick);
      return panel;
   }

   private void addQuick(JPanel panel, String text, String pageKey) {
      JButton button = UIStyle.primaryButton(text);
      button.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 10));
      button.addActionListener((e) -> showPage(pageKey));
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

   static class IncomingOrdersPage extends Page {
      private final JTable table = UIStyle.createTable(UIStyle.ORDER_COLUMNS);
      private final DefaultTableModel model = (DefaultTableModel)table.getModel();
      private final List<Restaurant.Order> shown = new ArrayList<>();

      IncomingOrdersPage() {
         JButton accept = UIStyle.button("Accept Order", UIStyle.GREEN);
         accept.addActionListener((e) -> acceptSelected());
         JButton reject = UIStyle.button("Reject Order", UIStyle.RED);
         reject.addActionListener((e) -> rejectSelected());
         JPanel body = new JPanel(new BorderLayout(0, 12));
         body.setOpaque(false);
         body.add(UIStyle.tableCard(table), BorderLayout.CENTER);
         body.add(UIStyle.buttonRow(accept, reject), BorderLayout.SOUTH);
         add(UIStyle.page("\uD83D\uDCE5", "Incoming Orders", null, notice, body), BorderLayout.CENTER);
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
         JPanel body = new JPanel(new BorderLayout(0, 12));
         body.setOpaque(false);
         body.add(filterBar, BorderLayout.NORTH);
         body.add(UIStyle.tableCard(table), BorderLayout.CENTER);
         body.add(UIStyle.buttonRow(
            statusButton("Mark Preparing", UIStyle.ORANGE, Restaurant.Order.PREPARING),
            statusButton("Mark Out for Delivery", UIStyle.PURPLE_DARK, Restaurant.Order.READY),
            statusButton("Mark Delivered", UIStyle.GREEN, Restaurant.Order.COMPLETED),
            statusButton("Cancel Order", UIStyle.RED, Restaurant.Order.CANCELLED)), BorderLayout.SOUTH);
         add(UIStyle.page("\uD83D\uDCCB", "Order Status", null, notice, body), BorderLayout.CENTER);
         data.addListener(this::refresh);
         refresh();
      }

      private JButton statusButton(String text, Color color, String status) {
         JButton button = UIStyle.button(text, color);
         button.addActionListener((e) -> changeStatus(status));
         return button;
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
            } else if (Restaurant.Order.READY.equals(order.status) && Restaurant.Order.PREPARING.equals(newStatus)){ 
               notice.warn(order.id + " is already Out for Delivery and can't go back to Preparing.");
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
         Restaurant.Restaurantt r = data.restaurant;
         restaurantName.setText(r.name);
         location.setText(r.location);
         ownerName.setText(r.ownerName);
         contact.setText(r.contact);
         cuisine.setText(r.cuisine);
      }

      private void saveProfile() {
         if (restaurantName.getText().trim().isEmpty() || location.getText().trim().isEmpty()) {
            notice.warn("Restaurant name and location can't be empty.");
            return;
         }
         Restaurant.Restaurantt r = data.restaurant;
         r.name = restaurantName.getText().trim();
         r.location = location.getText().trim();
         r.ownerName = ownerName.getText().trim();
         r.contact = contact.getText().trim();
         r.cuisine = cuisine.getText().trim();
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
         JPanel actions = new JPanel(new GridLayout(2, 3, 10, 10));
         actions.setOpaque(false);
         actions.add(action("Edit Selected", UIStyle.PURPLE_DARK, this::editSelected));
         actions.add(action("Toggle Availability", UIStyle.ORANGE, this::toggleSelected));
         actions.add(action("Change Picture", UIStyle.PURPLE_LIGHT, this::changePictureOfSelected));
         actions.add(action("Remove Picture", UIStyle.RED, this::removePictureOfSelected));
         actions.add(action("Delete Item", UIStyle.RED, this::deleteSelected));

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

      private UIStyle.RoundedButton action(String text, Color color, Runnable run) {
         UIStyle.RoundedButton button = UIStyle.compact(UIStyle.button(text, color));
         button.addActionListener((e) -> run.run());
         return button;
      }

      private JPanel buildDetailsCard() {
         detailImage.setPreferredSize(new Dimension(250, 200));
         UIStyle.styleImageBox(detailImage);
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
         UIStyle.styleImageBox(preview);
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
            cardLayout.show(cards, "list");
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
            } else {
               onPicked = null;
               notice.clear();
            }
            cardLayout.show(cards, pickReturn);
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
         cardLayout.show(cards, "image");
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
         cardLayout.show(cards, "form");
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
         boolean wasNew = editing == null;
         Restaurant.Dish target = wasNew ? new Restaurant.Dish(name, category, price, availableBox.isSelected(), 0) : editing;
         target.name = name;
         target.category = category;
         target.price = price;
         target.available = availableBox.isSelected();
         target.description = descriptionArea.getText().trim();
         target.imagePath = formImagePath;
         if (wasNew) {
            data.restaurant.menuItems.add(target);
         }
         editing = null;
         cardLayout.show(cards, "list");
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

      private Restaurant.Dish selectedDish() {
         int row = UIStyle.requireSelection(table, notice, "an item");
         return row >= 0 ? data.restaurant.menuItems.get(row) : null;
      }

      private void editSelected() {
         Restaurant.Dish dish = selectedDish();
         if (dish != null) {
            openForm(dish);
         }
      }

      private void toggleSelected() {
         Restaurant.Dish dish = selectedDish();
         if (dish != null) {
            dish.available = !dish.available;
            data.notifyChanged();
            notice.success("\"" + dish.name + "\" is now " + (dish.available ? "available" : "unavailable") + ".");
         }
      }

      private void changePictureOfSelected() {
         Restaurant.Dish dish = selectedDish();
         if (dish != null) {
            pickImage((path) -> {
               dish.imagePath = path;
               data.notifyChanged();
               notice.success("Picture updated for \"" + dish.name + "\".");
            }, "list");
         }
      }

      private void removePictureOfSelected() {
         Restaurant.Dish dish = selectedDish();
         if (dish == null) {
            return;
         }
         if (dish.imagePath == null) {
            notice.info(dish.name + " has no picture.");
         } else {
            notice.confirm("Remove the picture of \"" + dish.name + "\"?", () -> {
               dish.imagePath = null;
               data.notifyChanged();
            });
         }
      }

      private void deleteSelected() {
         Restaurant.Dish dish = selectedDish();
         if (dish != null) {
            notice.confirm("Delete \"" + dish.name + "\" from the menu?", () -> {
               data.restaurant.menuItems.remove(dish);
               data.notifyChanged();
            });
         }
      }

      @Override
      void onShow() {
         super.onShow();
         cardLayout.show(cards, "list");
         refresh();
      }
   }
   static final class UIStyle {
      static final Color PURPLE_DARK = new Color(74, 20, 140), PURPLE_LIGHT = new Color(156, 39, 176),
         YELLOW_ACCENT = new Color(255, 193, 7), BG_LIGHT = new Color(248, 245, 250), TEXT_DARK = new Color(30, 30, 30),
         WHITE = Color.WHITE, MUTED = new Color(110, 110, 110), BORDER_GRAY = new Color(225, 225, 225),
         ROW_ALT = new Color(250, 246, 252), GRID = new Color(236, 230, 241), SELECTION = new Color(225, 190, 231),
         FIELD_BORDER = new Color(200, 180, 210), GREEN = new Color(46, 125, 50), RED = new Color(198, 40, 40),
         ORANGE = new Color(230, 126, 0);
      private static final String EMOJI_FAMILY = findEmojiFamily();
      static final String[] ORDER_COLUMNS = {"Order ID", "Customer", "Items", "Total", "Payment", "Time", "Status"};

      private UIStyle() {
      }

      static Font font(int style, int size) {
         return new Font("Segoe UI", style, size);
      }

      static Font emojiFont(int style, int size) {
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

      static Color statusColor(String value) {
         switch (value.toLowerCase()) {
            case "available": case "active": case "paid": case "out for delivery": case "delivered":
               return GREEN;
            case "unavailable": case "inactive": case "failed": case "cancelled":
               return RED;
            case "pending": case "preparing":
               return ORANGE;
            default:
               return null;
         }
      }

      static int requireSelection(JTable table, Notice notice, String what) {
         int row = table.getSelectedRow();
         if (row < 0) {
            notice.warn("Please select " + what + " first.");
         }
         return row;
      }

      static void onTextChange(JTextField field, Runnable action) {
         field.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { action.run(); }
            public void removeUpdate(DocumentEvent e) { action.run(); }
            public void changedUpdate(DocumentEvent e) { action.run(); }
         });
      }

      static void styleImageBox(JLabel label) {
         label.setOpaque(true);
         label.setBackground(BG_LIGHT);
         label.setForeground(MUTED);
         label.setFont(font(Font.PLAIN, 13));
         label.setBorder(BorderFactory.createLineBorder(BORDER_GRAY));
      }

      static ImageIcon loadImage(String path, int maxWidth, int maxHeight) {
         if (path == null || path.isEmpty() || !new File(path).isFile()) {
            return null;
         }
         ImageIcon icon = new ImageIcon(path);
         if (icon.getIconWidth() <= 0 || icon.getIconHeight() <= 0) {
            return null;
         }
         double scale = Math.min(1.0, Math.min((double)maxWidth / icon.getIconWidth(), (double)maxHeight / icon.getIconHeight()));
         int w = Math.max(1, (int)(icon.getIconWidth() * scale));
         int h = Math.max(1, (int)(icon.getIconHeight() * scale));
         return new ImageIcon(icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
      }

      static JTextArea wrapText(String text, int size, Color color, int width, int height) {
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

      static JPanel page(String icon, String title, JComponent action, Notice notice, JComponent body) {
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
         top.add(notice, BorderLayout.CENTER);
         main.add(top, BorderLayout.NORTH);
         main.add(body, BorderLayout.CENTER);
         return main;
      }

      static JPanel formView(JLabel title, JComponent body, JComponent buttons, Color accent) {
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

      static RoundedPanel card(Color accent) {
         RoundedPanel card = new RoundedPanel(18, WHITE, BORDER_GRAY, accent);
         card.setLayout(new BorderLayout());
         return card;
      }

      static RoundedPanel statCard(String title, String value, String description, Color accent) {
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

      static RoundedPanel tableCard(JTable table) {
         RoundedPanel card = card(PURPLE_LIGHT);
         card.setPadding(10, 20, 10, 14);
         JScrollPane scroll = new JScrollPane(table);
         scroll.setBorder(BorderFactory.createLineBorder(BORDER_GRAY));
         scroll.getViewport().setBackground(WHITE);
         card.add(scroll, BorderLayout.CENTER);
         return card;
      }

      static JTable createTable(String[] columns) {
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

      static JPanel buttonRow(JComponent... buttons) {
         JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
         row.setOpaque(false);
         for (JComponent button : buttons) {
            row.add(button);
         }
         return row;
      }

      static JPanel form(String[] labels, JComponent[] fields) {
         JPanel panel = new JPanel(new GridBagLayout());
         panel.setOpaque(false);
         GridBagConstraints gbc = new GridBagConstraints();
         gbc.anchor = GridBagConstraints.WEST;
         for (int i = 0; i < labels.length; ++i) {
            gbc.gridy = i;
            gbc.gridx = 0;
            gbc.weightx = 0.0;
            gbc.fill = GridBagConstraints.NONE;
            gbc.insets = new Insets(6, 0, 6, 14);
            panel.add(fieldLabel(labels[i]), gbc);
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(6, 0, 6, 0);
            if (fields[i] instanceof JTextField && fields[i].getPreferredSize().width < 280) {
               fields[i].setPreferredSize(new Dimension(280, fields[i].getPreferredSize().height));
            }
            panel.add(fields[i], gbc);
         }
         return panel;
      }

      static RoundedButton button(String text, Color color) {
         RoundedButton button = new RoundedButton(text, color, 14);
         button.setForeground(WHITE);
         button.setFont(font(Font.BOLD, 13));
         button.setFocusPainted(false);
         button.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
         button.setCursor(new Cursor(Cursor.HAND_CURSOR));
         return button;
      }

      static RoundedButton primaryButton(String text) {
         RoundedButton button = button(text, PURPLE_LIGHT);
         button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
         return button;
      }

      static RoundedButton compact(RoundedButton button) {
         button.setBorder(BorderFactory.createEmptyBorder(9, 10, 9, 10));
         return button;
      }

      static JLabel fieldLabel(String text) {
         JLabel label = new JLabel(text);
         label.setFont(font(Font.BOLD, 14));
         label.setForeground(TEXT_DARK);
         return label;
      }

      static JTextField styledField(String text) {
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

         void info(String message) { show(message, PURPLE_DARK); }
         void success(String message) { show(message, GREEN); }
         void warn(String message) { show(message, ORANGE); }

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
         private final Color fill, line, accent;

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
            int w = getWidth() - 1, h = getHeight() - 1;
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
         private final Color base;
         private final int radius;

         RoundedButton(String text, Color base, int radius) {
            super(text);
            this.base = base;
            this.radius = radius;
            setContentAreaFilled(false);
            setOpaque(false);
            setRolloverEnabled(true);
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
}