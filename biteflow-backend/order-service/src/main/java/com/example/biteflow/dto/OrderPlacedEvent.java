package com.example.biteflow.dto;

import java.util.List;

public record OrderPlacedEvent(Long orderId, List<String> skuCodes) {
}
