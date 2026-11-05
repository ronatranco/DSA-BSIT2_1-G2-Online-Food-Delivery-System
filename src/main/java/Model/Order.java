/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

/**
 * @author RJD 
 * 
 */
public class Order {

    private String orderId;
    private String customerId;
    private String customerName;
    private String customerPhone;
    private String deliveryAddress;
    private String restaurantId;
    private List<OrderItem> orderItems;
    private String status;

    public Order(String orderId, String customerId, String customerName, String customerPhone,
                 String deliveryAddress, String restaurantId, List<OrderItem> orderItems, String status) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.deliveryAddress = deliveryAddress;
        this.restaurantId = restaurantId;
        this.orderItems = orderItems != null ? orderItems : new ArrayList<>();
        this.status = status != null ? status : "PENDING";
    }
    
    // 1. Getters & Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getRestaurantId() { return restaurantId; }
    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }

    public List<OrderItem> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems != null ? orderItems : new ArrayList<>(); }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    // 2. Business Logic & Calculations
    public void addOrderItem(OrderItem item) {
        if (item != null) {
            for (OrderItem existing : orderItems) {
                if (existing.getItemName().equalsIgnoreCase(item.getItemName())) {
                    existing.setQuantity(existing.getQuantity() + item.getQuantity());
                    return;
                }
            }
            this.orderItems.add(item);
        }
    }
    
    public void removeOrderItem(OrderItem item) {
        this.orderItems.remove(item);
    }
    
    public Order(String orderId, String customerId, String restaurantId, List<OrderItem> items, String status) {
    this(orderId, customerId, "User", "", "", restaurantId, items, status);
    }

    // 3. Calculations
    public double calculateSubtotal() {
        double subtotal = 0.0;
        for (OrderItem item : orderItems) {
            subtotal += item.calculateLineTotal();
        }
        return subtotal;
    }

    // 3.1 Calculate Items
    public int getTotalItemCount() {
        int count = 0;
        for (OrderItem item : orderItems) {
            count += item.getQuantity();
        }
        return count;
    }

    // 3.2 Calculate Total, Delivery Fee, and Vouchers 
    public double calculateFinalTotal(double deliveryFee, double voucherDiscount) {
        double subtotal = calculateSubtotal();
        double total = subtotal + deliveryFee - voucherDiscount;
        return Math.max(0.0, total);
    }

    // 3.3 Order Validation 
    public boolean isValidOrder() {
        return restaurantId != null && !restaurantId.trim().isEmpty() && 
               orderItems != null && !orderItems.isEmpty();
    }
    
    // 4. OrderItem 
    public static class OrderItem {
        private String itemName;
        private int quantity;
        private double unitPrice;
        private String specialInstructions;

        public OrderItem(String itemName, int quantity, double unitPrice) {
            this(itemName, quantity, unitPrice, "");
        }

        public OrderItem(String itemName, int quantity, double unitPrice, String specialInstructions) {
            this.itemName = itemName;
            this.quantity = Math.max(1, quantity); 
            this.unitPrice = Math.max(0.0, unitPrice);
            this.specialInstructions = specialInstructions != null ? specialInstructions : "";
        }

        //5. OrderItem Getters & Setters
        public String getItemName() { return itemName; }
        public void setItemName(String itemName) { this.itemName = itemName; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = Math.max(1, quantity); }

        public double getUnitPrice() { return unitPrice; }
        public void setUnitPrice(double unitPrice) { this.unitPrice = Math.max(0.0, unitPrice); }

        public String getSpecialInstructions() { return specialInstructions; }
        public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions != null ? specialInstructions : ""; }

        public double calculateLineTotal() {
            return this.unitPrice * this.quantity;
        }
    }
    
    // 6. Order History
    public static class OrderData {
        private String storeName;
        private String location;
        private String orderId;
        private String orderDate;
        private String deliveryDate;
        private String paymentMop;
        private String status;
        private String totalPaid;
        private List<OrderItem> items;
    
    public OrderData(String storeName, String location, String orderId, String orderDate, 
                         String deliveryDate, String paymentMop, String status, String totalPaid, List<OrderItem> items) {
            this.storeName = storeName;
            this.location = location;
            this.orderId = orderId;
            this.orderDate = orderDate;
            this.deliveryDate = deliveryDate;
            this.paymentMop = paymentMop;
            this.status = status;
            this.totalPaid = totalPaid;
            this.items = items != null ? items : new ArrayList<>();
        }

    // 7. Order History Getter & Setter
        public String getStoreName() { return storeName; }
        public String getLocation() { return location; }
        public String getOrderId() { return orderId; }
        public String getOrderDate() { return orderDate; }
        public String getDeliveryDate() { return deliveryDate; }
        public String getPaymentMop() { return paymentMop; }
        public String getStatus() { return status; }
        public String getTotalPaid() { return totalPaid; }
        public List<OrderItem> getItems() { return items; }
       

        public void setStatus(String status) { this.status = status; }
        public void setDeliveryDate(String deliveryDate) { this.deliveryDate = deliveryDate; }
        
    }

    // 8. Order Store
    public static class OrderStore {

        // 8.1 Status Constants
        public static final String PENDING = "Pending";
        public static final String PREPARING = "Preparing";
        public static final String OUT_FOR_DELIVERY = "Out for Delivery";
        public static final String DELIVERED = "Delivered";
        public static final String RECEIVED = "Received";
        public static final String CANCELLED = "Cancelled";

        private static final OrderStore INSTANCE = new OrderStore();
        public static OrderStore get() { return INSTANCE; }

        private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("MMM d, yyyy · h:mm a");

        // 8.1.1 Link
        private static class Link {
            final OrderData customerOrder;
            final Restaurant restaurant;
            final Restaurant.Order restaurantOrder;
            Link(OrderData customerOrder, Restaurant restaurant, Restaurant.Order restaurantOrder) {
                this.customerOrder = customerOrder;
                this.restaurant = restaurant;
                this.restaurantOrder = restaurantOrder;
            }
        }

        // 8.1.2 Store Data
        private final List<OrderData> orders = new ArrayList<>();
        private final List<Runnable> listeners = new ArrayList<>();
        private final List<Link> links = new ArrayList<>();
        private final List<Restaurant> hooked = new ArrayList<>();
        private int nextId = 1;

        private OrderStore() {}

        // 8.2 Place Order
        public OrderData place(Order order, String storeName, String location,
                               String paymentMop, double finalTotal) {
            for (OrderData existing : orders) {
                if (existing.getOrderId().equals(order.getOrderId())) return existing;
            }

            // 8.3 Order ID
            String id = order.getOrderId();
            if (id == null || id.trim().isEmpty()) {
                id = String.format("#ORD-%d-%04d", LocalDate.now().getYear(), nextId++);
                order.setOrderId(id);
            }

            OrderData data = new OrderData(
                    storeName, location, id,
                    LocalDateTime.now().format(FMT),
                    "—",
                    paymentMop,
                    PENDING,
                    String.format("₱%.2f", finalTotal),
                    order.getOrderItems());

            orders.add(data);
            sendToRestaurant(order, data, finalTotal, paymentMop);
            fireChanged();
            return data;
        }

        // 8.4 Status
        public void updateStatus(String orderId, String newStatus) {
            for (OrderData o : orders) {
                if (o.getOrderId().equals(orderId)) {
                    applyStatus(o, newStatus);
                    if (CANCELLED.equalsIgnoreCase(newStatus)) {
                        Link link = findLink(o);
                        if (link != null && !link.restaurantOrder.isFinal()) {
                            link.restaurant.setOrderStatus(link.restaurantOrder, Restaurant.Order.CANCELLED);
                        }
                    }
                }
            }
            fireChanged();
        }

        // 8.5 Order Received
        public boolean markReceived(String orderId) {
            for (OrderData o : orders) {
                if (o.getOrderId().equals(orderId) && DELIVERED.equalsIgnoreCase(o.getStatus())) {
                    applyStatus(o, RECEIVED);
                    fireChanged();
                    return true;
                }
            }
            return false;
        }

        // 8.6 Track Order
        public List<OrderData> getActive() {
            return orders.stream()
                    .filter(o -> !o.getStatus().equalsIgnoreCase(RECEIVED)
                              && !o.getStatus().equalsIgnoreCase(CANCELLED))
                    .collect(Collectors.toList());
        }

        // 8.7 Order History
        public List<OrderData> getHistory() {
            return orders.stream()
                    .filter(o -> o.getStatus().equalsIgnoreCase(RECEIVED)
                              || o.getStatus().equalsIgnoreCase(CANCELLED))
                    .collect(Collectors.toList());
        }

        // 8.8 Order Delivered
        public List<OrderData> getDelivered() {
            return orders.stream()
                    .filter(o -> o.getStatus().equalsIgnoreCase(RECEIVED))
                    .collect(Collectors.toList());
        }

        // 8.9 Listeners
        public void addListener(Runnable r) { listeners.add(r); }

        private void fireChanged() { new ArrayList<>(listeners).forEach(Runnable::run); }

        // 8.10 Connect Customer <-> Restaurant 
        // 8.10.1 Apply Status
        private void applyStatus(OrderData o, String status) {
            o.setStatus(status);
            if (DELIVERED.equalsIgnoreCase(status)) {
                o.setDeliveryDate(LocalDateTime.now().format(FMT));
            }
        }

        // 8.10.2 Find Link
        private Link findLink(OrderData o) {
            for (Link link : links) {
                if (link.customerOrder == o) return link;
            }
            return null;
        }

        // 8.10.3 Restaurant Brand
        private Restaurant.Brand brandFor(String storeName) {
            if (storeName == null) return null;
            if (storeName.equalsIgnoreCase("McDollibee")) return Restaurant.Brand.MCDOLLIBEE;
            if (storeName.equalsIgnoreCase("Brain Cells Milk Tea")) return Restaurant.Brand.BRAIN_CELLS_MILK_TEA;
            if (storeName.equalsIgnoreCase("Mang Pinaasa")) return Restaurant.Brand.MANG_PINAASA;
            return null;
        }

        // 8.10.4 Send Order to Restaurant
        private void sendToRestaurant(Order order, OrderData data, double total, String payment) {
            Restaurant.Brand brand = brandFor(data.getStoreName());
            if (brand == null) return;

            Restaurant resto = Restaurant.get(brand);
            if (!hooked.contains(resto)) {
                hooked.add(resto);
                resto.addListener(this::syncFromRestaurants);
            }

            StringBuilder items = new StringBuilder();
            for (OrderItem it : order.getOrderItems()) {
                if (items.length() > 0) items.append(", ");
                items.append(it.getItemName());
                if (it.getQuantity() > 1) items.append(" x").append(it.getQuantity());
            }
            String customer = order.getCustomerName();
            if (customer == null || customer.trim().isEmpty()) customer = "Customer";

            Restaurant.Order ro = resto.addOrder(customer, items.toString(), total, payment);
            links.add(new Link(data, resto, ro));
        }

        // 8.10.5 Sync Status
        private void syncFromRestaurants() {
            boolean changed = false;
            for (Link link : links) {
                OrderData o = link.customerOrder;
                String current = o.getStatus();
                if (RECEIVED.equalsIgnoreCase(current) || CANCELLED.equalsIgnoreCase(current)) continue;

                String target = toCustomerStatus(link.restaurantOrder.status);
                if (!current.equalsIgnoreCase(target)) {
                    applyStatus(o, target);
                    changed = true;
                }
            }
            if (changed) fireChanged();
        }

        // 8.10.6 Convert Status
        private String toCustomerStatus(String restaurantStatus) {
            if (Restaurant.Order.PREPARING.equals(restaurantStatus)) return PREPARING;
            if (Restaurant.Order.READY.equals(restaurantStatus)) return OUT_FOR_DELIVERY;
            if (Restaurant.Order.COMPLETED.equals(restaurantStatus)) return DELIVERED;
            if (Restaurant.Order.CANCELLED.equals(restaurantStatus)
                    || Restaurant.Order.FAILED.equals(restaurantStatus)) return CANCELLED;
            return PENDING;
        }
    }
}