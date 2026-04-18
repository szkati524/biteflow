package com.example.audit_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class Order {

    private String orderNumber;
    private String customerName;
    private double totalAmount;
    private String status;
    private LocalDateTime createdAt;
}
