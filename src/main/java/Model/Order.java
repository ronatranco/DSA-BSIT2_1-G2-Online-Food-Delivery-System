
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author RJD 
 * 
 */
public class Order {
    
    private String orderId;
    private String customerId;
    private String restaurantId;
    private List<OrderItem> orderItems;
    private String status;

    public Order(String orderId, String customerId, String restaurantId, List<OrderItem> orderItems, String status) {
        this.orderId = orderId;
        this.customerId = customerId;
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

    // 2. Business Logic & Calculations
    public void addOrderItem(OrderItem item) {
        if (item != null) {
            // Check if item already exists, increment quantity if so
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

    // 2. Calculations
    public double calculateSubtotal() {
        double subtotal = 0.0;
        for (OrderItem item : orderItems) {
            subtotal += item.calculateLineTotal();
        }
        return subtotal;
    }

    // 2.1 Calculate Items
    public int getTotalItemCount() {
        int count = 0;
        for (OrderItem item : orderItems) {
            count += item.getQuantity();
        }
        return count;
    }

    // 2.2 Calculate Total, Delivery Fee, and Vouchers 
    public double calculateFinalTotal(double deliveryFee, double voucherDiscount) {
        double subtotal = calculateSubtotal();
        double total = subtotal + deliveryFee - voucherDiscount;
        return Math.max(0.0, total);
    }

    // 2.3 Order Validation 
    public boolean isValidOrder() {
        return restaurantId != null && !restaurantId.trim().isEmpty() && 
               orderItems != null && !orderItems.isEmpty();
    }
    
    // 3. OrderItem 
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
        

        //4. OrderItem Getters & Setters
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
    
    // 5. Order History
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

    // 5. Order History Getter & Setter
        public String getStoreName() { return storeName; }
        public String getLocation() { return location; }
        public String getOrderId() { return orderId; }
        public String getOrderDate() { return orderDate; }
        public String getDeliveryDate() { return deliveryDate; }
        public String getPaymentMop() { return paymentMop; }
        public String getStatus() { return status; }
        public String getTotalPaid() { return totalPaid; }
        public List<OrderItem> getItems() { return items; }
    }
    }