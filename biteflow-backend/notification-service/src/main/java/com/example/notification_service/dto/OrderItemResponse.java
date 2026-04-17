package com.example.notification_service.dto;

public record OrderItemResponse(Long productId,int quantity,double priceAtPurchase) {
}
