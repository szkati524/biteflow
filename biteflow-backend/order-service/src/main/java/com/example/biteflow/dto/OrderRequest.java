package com.example.biteflow.dto;

import java.util.List;

public record OrderRequest(String customerName, List<String> skuCodes) {
}
