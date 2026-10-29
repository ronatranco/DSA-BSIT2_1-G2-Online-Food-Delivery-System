/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;
/**
 *
 * @author krist
 */

import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import javax.swing.Timer;

public final class Restaurant {
   private static DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);
   private static Map<Brand, Restaurant> INSTANCES = new EnumMap<>(Brand.class);

   public enum Brand {
      MCDOLLIBEE,
      BRAIN_CELLS_MILK_TEA,
      MANG_PINAASA
   }

   public enum StoreStatus {
      OPEN("Open"),
      CLOSED("Closed"),
      PAUSED("Orders Paused");

      private final String label;

      StoreStatus(String label) {
         this.label = label;
      }

      public String getLabel() {
         return label;
      }
   }

   public static class Restaurantt {
      public final String id;
      public String name;
      public String location;
      public final List<Dish> menuItems = new ArrayList<>();
      public String ownerName = "Juan Dela Cruz";
      public String contact = "09123456789";
      public String cuisine = "Filipino";

      public Restaurantt(String id, String name, String location) {
         this.id = id;
         this.name = name;
         this.location = location;
      }
   }

   public static class Dish {
      public String name;
      public String category;
      public double price;
      public boolean available;
      public int sold;
      public String description = "";
      public String imagePath = null;

      public Dish(String name, String category, double price, boolean available, int sold) {
         this.name = name;
         this.category = category;
         this.price = price;
         this.available = available;
         this.sold = sold;
      }
   }

   public static class ModifierOption {
      public String name;
      public double priceChange;

      public ModifierOption(String name, double priceChange) {
         this.name = name;
         this.priceChange = priceChange;
      }
   }

   public static class ModifierGroup {
      public String name;
      public boolean required;
      public final List<ModifierOption> options = new ArrayList<>();

      public ModifierGroup(String name, boolean required) {
         this.name = name;
         this.required = required;
      }
   }

   public static class Order {
      public static final String PENDING = "Pending";
      public static final String PREPARING = "Preparing";
      public static final String READY = "Ready";
      public static final String COMPLETED = "Completed";
      public static final String CANCELLED = "Cancelled";
      public static final String FAILED = "Failed";
      public final String id;
      public final String customer;
      public final String items;
      public final double total;
      public final String payment;
      public final String time;
      public String status;

      public Order(String id, String customer, String items, double total, String payment, String time, String status) {
         this.id = id;
         this.customer = customer;
         this.items = items;
         this.total = total;
         this.payment = payment;
         this.time = time;
         this.status = status;
      }

      public boolean isFinal() {
         return COMPLETED.equals(status) || CANCELLED.equals(status) || FAILED.equals(status);
      }

      public boolean countsAsSale() {
         return !CANCELLED.equals(status) && !FAILED.equals(status);
      }
   }

   public static class DaySchedule {
      public final DayOfWeek day;
      public boolean enabled;
      public LocalTime open;
      public LocalTime close;

      public DaySchedule(DayOfWeek day, boolean enabled, LocalTime open, LocalTime close) {
         this.day = day;
         this.enabled = enabled;
         this.open = open;
         this.close = close;
      }
   }

   public final Restaurantt restaurant = new Restaurantt("REST-001", "McDollibee", "San Pedro, Laguna");
   public final List<String> categories = new ArrayList<>(Arrays.asList("Rice Meals", "Burgers", "Sides"));
   public final List<ModifierGroup> modifierGroups = new ArrayList<>();
   public final List<Order> orders = new ArrayList<>();
   public final List<DaySchedule> schedule = new ArrayList<>();
   private StoreStatus storeStatus = StoreStatus.OPEN;
   private boolean autoSchedule = true;
   private Boolean lastShouldBeOpen = null;
   private int orderCounter = 7;
   private Timer scheduler;
   private final List<Runnable> listeners = new ArrayList<>();
   private final Random random = new Random();

   public static synchronized Restaurant get(Brand brand) {
      return INSTANCES.computeIfAbsent(brand, Restaurant::new);
   }

   private Restaurant(Brand brand) {
      seedMenu(brand);

      ModifierGroup addOns = new ModifierGroup("Add-ons", false);
      addOns.options.add(new ModifierOption("Extra Cheese", 25.0));
      addOns.options.add(new ModifierOption("Extra Egg", 20.0));
      addOns.options.add(new ModifierOption("Extra Sauce", 15.0));
      addOns.options.add(new ModifierOption("Extra Rice", 30.0));
      ModifierGroup size = new ModifierGroup("Size", true);
      size.options.add(new ModifierOption("Regular", 0.0));
      size.options.add(new ModifierOption("Large", 30.0));
      ModifierGroup adjustments = new ModifierGroup("Remove / Less", false);
      adjustments.options.add(new ModifierOption("Less Rice", -10.0));
      adjustments.options.add(new ModifierOption("No Sauce", -5.0));
      adjustments.options.add(new ModifierOption("No Onions", 0.0));
      modifierGroups.add(addOns);
      modifierGroups.add(size);
      modifierGroups.add(adjustments);

      orders.add(new Order("ORD-007", "Anna Lim", "Iced Tea", 60.0, "Paid", "9:55 AM", "Cancelled"));
      orders.add(new Order("ORD-006", "John Cruz", "French Fries x3", 240.0, "Failed", "9:30 AM", "Failed"));
      orders.add(new Order("ORD-005", "Mark Lee", "Beef Burger, Iced Tea", 210.0, "Paid", "10:47 AM", "Pending"));
      orders.add(new Order("ORD-004", "Ana Garcia", "Chicken Rice, Beef Burger, Halo-Halo", 365.0, "Paid", "9:48 AM", "Completed"));
      orders.add(new Order("ORD-003", "Pedro Reyes", "Halo-Halo x2", 190.0, "Cash on Delivery", "10:41 AM", "Pending"));
      orders.add(new Order("ORD-002", "Maria Santos", "Beef Burger x2, French Fries x2", 460.0, "Paid", "10:25 AM", "Ready"));
      orders.add(new Order("ORD-001", "Juan Dela Cruz", "Chicken Rice x2, Iced Tea", 300.0, "Paid", "10:12 AM", "Preparing"));

      for (DayOfWeek day : DayOfWeek.values()) {
         schedule.add(new DaySchedule(day, true, LocalTime.of(8, 0), LocalTime.of(21, 0)));
      }
   }

    private void addDish(String name, String category, double price, int sold, String imageFile) {
      Dish dish = new Dish(name, category, price, true, sold);
      dish.imagePath = imageFile;
      restaurant.menuItems.add(dish);
   }

   private void seedMenu(Brand brand) {
      switch (brand) {
         case BRAIN_CELLS_MILK_TEA:
            addDish("Matcha", "Milk Tea", 135.0, 42, "src/main/java/gui/images/cdrestaurantmenumatcha.png");
            addDish("Dark Choco", "Milk Tea", 120.0, 35, "src/main/java/gui/images/cdrestaurantmenudarkchoco.png");
            addDish("Watermelon Milk Tea", "Milk Tea", 65.0, 51, "src/main/java/gui/images/cdrestaurantmenumilktea.png");
            restaurant.menuItems.get(0).description = "Smooth, earthy green tea blended into a rich and creamy treat.";
            restaurant.menuItems.get(1).description = "Deeply decadent and velvety chocolate crafted for the ultimate sweet tooth.";
            restaurant.menuItems.get(2).description = "A refreshing blend of creamy milk tea and sweet, juicy watermelon flavor.";
            break;
            
         case MANG_PINAASA:
            addDish("Paa Malaki", "Rice Meals", 135.0, 42, "src/main/java/gui/images/cdrestaurantmenupaa.png");
            addDish("Pork BBQ", "Rice Meals", 120.0, 35, "src/main/java/gui/images/cdrestaurantmenubbq.png");
            addDish("Pork Liempo", "Rice Meals", 65.0, 51, "src/main/java/gui/images/cdrestaurantmenupliempo.png");
            restaurant.menuItems.get(0).description = "Soft, tender bites simmered in a savory, finger-licking-good savory glaze.";
            restaurant.menuItems.get(1).description = "Smoky, skewered grilled pork brushed in a sweet and savory marinade.";
            restaurant.menuItems.get(2).description = "Juicy grilled pork belly with ultra-crispy skin, served with a tangy vinegar dip.";
            break;
            
         default:
            addDish("McChicken Joy w/Rice", "Rice Meals", 135.0, 42, "src/main/java/gui/images/cdrestaurantmenuchicken.png");
            addDish("Peach Apple Pie", "Sides", 120.0, 35, "src/main/java/gui/images/cdrestaurantmenupapie.png");
            addDish("Yum Big Mac Burger", "Burgers", 65.0, 51, "src/main/java/gui/images/cdrestaurantmenuburger.png");
            restaurant.menuItems.get(0).description = "Crispy on the outside, juicy on the inside, and served with fluffy rice and savory gravy.";
            restaurant.menuItems.get(1).description = "A warm, crispy crust filled with sweet, gooey peach and apple goodness.";
            restaurant.menuItems.get(2).description = "Two juicy beef patties, legendary special sauce, crisp lettuce, cheese, pickles, and onions on a toasted sesame bun.";
            break;
      }
   }

   public void addListener(Runnable listener) {
      listeners.add(listener);
   }

   public void removeListener(Runnable listener) {
      listeners.remove(listener);
   }

   public void notifyChanged() {
      for (Runnable listener : new ArrayList<>(listeners)) {
         listener.run();
      }
   }

   public StoreStatus getStoreStatus() {
      return storeStatus;
   }

   public void setStoreStatus(StoreStatus status) {
      storeStatus = status;
      notifyChanged();
   }

   public boolean isAutoSchedule() {
      return autoSchedule;
   }

   public void setAutoSchedule(boolean auto) {
      autoSchedule = auto;
      applySchedule();
      notifyChanged();
   }

   public void startScheduler() {
      if (scheduler == null) {
         scheduler = new Timer(30000, (e) -> checkSchedule());
         scheduler.start();
         checkSchedule();
      }
   }

   public void applySchedule() {
      lastShouldBeOpen = null;
      checkSchedule();
   }

   public boolean shouldBeOpenNow() {
      LocalDateTime now = LocalDateTime.now();
      DaySchedule today = schedule.get(now.getDayOfWeek().getValue() - 1);
      if (!today.enabled || today.open.equals(today.close)) {
         return false;
      }
      LocalTime time = now.toLocalTime();
      if (today.close.isAfter(today.open)) {
         return !time.isBefore(today.open) && time.isBefore(today.close);
      }
      return !time.isBefore(today.open) || time.isBefore(today.close);
   }

   private void checkSchedule() {
      if (!autoSchedule) {
         lastShouldBeOpen = null;
         return;
      }
      boolean shouldBeOpen = shouldBeOpenNow();
      if (lastShouldBeOpen == null || lastShouldBeOpen != shouldBeOpen) {
         lastShouldBeOpen = shouldBeOpen;
         setStoreStatus(shouldBeOpen ? StoreStatus.OPEN : StoreStatus.CLOSED);
      }
   }

   public Order addOrder(String customer, String items, double total, String payment) {
      String id = String.format("ORD-%03d", ++orderCounter);
      Order order = new Order(id, customer, items, total, payment, LocalTime.now().format(TIME_FORMAT), Order.PENDING);
      orders.add(0, order);
      notifyChanged();
      return order;
   }

   public void setOrderStatus(Order order, String status) {
      order.status = status;
      notifyChanged();
   }

   public Order simulateIncomingOrder() {
      if (storeStatus != StoreStatus.OPEN) {
         return null;
      }
      List<Dish> pool = new ArrayList<>();
      for (Dish dish : restaurant.menuItems) {
         if (dish.available) {
            pool.add(dish);
         }
      }
      if (pool.isEmpty()) {
         return null;
      }
      Collections.shuffle(pool, random);
      String[] customers = {"Liza Mendoza", "Carlo Bautista", "Joy Ramos", "Miguel Torres", "Bea Villanueva"};
      int lines = 1 + random.nextInt(Math.min(3, pool.size()));
      StringBuilder items = new StringBuilder();
      double total = 0.0;
      for (int i = 0; i < lines; ++i) {
         Dish dish = pool.get(i);
         int qty = 1 + random.nextInt(2);
         if (i > 0) {
            items.append(", ");
         }
         items.append(dish.name);
         if (qty > 1) {
            items.append(" x").append(qty);
         }
         total += dish.price * qty;
         dish.sold += qty;
      }
      String payment = random.nextBoolean() ? "Paid" : "Cash on Delivery";
      return addOrder(customers[random.nextInt(customers.length)], items.toString(), total, payment);
   }

   public int pendingCount() {
      return (int)orders.stream().filter(o -> Order.PENDING.equals(o.status)).count();
   }

   public double revenue() {
      return orders.stream().filter(Order::countsAsSale).mapToDouble(o -> o.total).sum();
   }

   public int availableDishCount() {
      return (int)restaurant.menuItems.stream().filter(d -> d.available).count();
   }

   public void addCategoryIfNew(String category) {
      for (String existing : categories) {
         if (existing.equalsIgnoreCase(category)) {
            return;
         }
      }
      categories.add(category);
   }

   public static final class MoneyFormat {

      private MoneyFormat() {
      }

      public static String peso(double amount) {
         return "\u20B1" + new DecimalFormat(amount == Math.rint(amount) ? "#,##0" : "#,##0.00").format(amount);
      }

      public static String pesoSigned(double amount) {
         if (amount > 0.0) {
            return "+" + peso(amount);
         }
         return amount < 0.0 ? "-" + peso(-amount) : peso(0.0);
      }

      public static String amountText(double amount) {
         return amount == Math.rint(amount) ? String.valueOf((long)amount) : String.valueOf(amount);
      }

      public static Double parseAmount(String text) {
         try {
            return Double.parseDouble(text.trim().replace("\u20B1", "").replace(",", ""));
         } catch (NumberFormatException e) {
            return null;
         }
      }
   }
}