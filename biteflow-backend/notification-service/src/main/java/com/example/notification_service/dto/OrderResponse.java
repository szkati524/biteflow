package com.example.notification_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(Long id, String customerName, double totalAmount, String status, LocalDateTime createdAt,
                            List<OrderItemResponse> item) {
}
