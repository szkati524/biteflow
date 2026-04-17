package com.example.inventory_service.service;

import com.example.inventory_service.dto.InventoryResponse;
import com.example.inventory_service.entity.Inventory;
import com.example.inventory_service.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
private final InventoryRepository inventoryRepository;

@Transactional(readOnly=true)
    public List<InventoryResponse> isInStock(List<String> skuCode){
    log.info("sprawdzanie dostępności dla produktu {} ", skuCode);
    return inventoryRepository.findBySkuCodeIn(skuCode).stream()
            .map(inventory -> new InventoryResponse(
                    inventory.getSkuCode(),
                    inventory.getQuantity() > 0
            ))
            .toList();

}
public List<Inventory> getAllInventory(){
    return inventoryRepository.findAll();
}
}
