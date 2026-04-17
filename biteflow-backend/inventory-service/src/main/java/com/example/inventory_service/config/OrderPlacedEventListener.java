package com.example.inventory_service.config;

import com.example.inventory_service.dto.OrderPlacedEvent;
import com.example.inventory_service.entity.Inventory;
import com.example.inventory_service.repository.InventoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class OrderPlacedEventListener {
    private final InventoryRepository inventoryRepository;

    @RabbitListener(queues = "orderQueue")
    public void handleOrderPlacedEvent(OrderPlacedEvent event){
        log.info("Odebrano zamówienie nr {}.Aktualizuje stany magazynowe dla: {} ",
                event.orderId(),event.skuCodes());
        event.skuCodes().forEach(skuCode -> {
            log.info("Szukam w bazie SKU: '{}' ",skuCode);
            Inventory inventory = inventoryRepository.findBySkuCode(skuCode)
                    .orElseThrow(() -> new RuntimeException("Nie znaleziono produktu w magazynie: " + skuCode)  );
            inventory.setQuantity(inventory.getQuantity() -1);
            inventoryRepository.save(inventory);
log.info("Pomyślnie odjęto stan dla: {}",skuCode);
        });
log.info("Stany magazynowe zostały zaktualizowane!");
    }
}
