package com.example.inventory_service.dto;

import java.util.List;

public record OrderPlacedEvent(Long orderId,
                               List<String> skuCodes) {
}
