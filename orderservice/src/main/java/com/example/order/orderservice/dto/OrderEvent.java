package com.example.order.orderservice.dto; // Change package name to match your local service layout

import java.math.BigDecimal;

public class OrderEvent {
    private Long orderId;
    private Long userId;
    private Long restaurantId;
    private String status;
    private BigDecimal totalAmount;

    // 🛠️ MANDATORY: Empty constructor for Jackson Deserialization
    public OrderEvent() {}

    // Getters and Setters
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
}
