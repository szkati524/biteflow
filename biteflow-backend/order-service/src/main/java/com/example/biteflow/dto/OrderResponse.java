package com.example.biteflow.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(Long id, String customerName, double totalAmount, String status, LocalDateTime createdAt,
                            List<OrderItemResponse> item) {
}
