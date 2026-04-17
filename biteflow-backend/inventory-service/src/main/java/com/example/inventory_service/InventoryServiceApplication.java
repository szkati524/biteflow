package com.example.inventory_service;

import com.example.inventory_service.entity.Inventory;
import com.example.inventory_service.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}
	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository){
		return args -> {
			Inventory inventory = new Inventory();
			inventory.setSkuCode("burger_wolowy");
			inventory.setQuantity(100);


			Inventory inventory2 = new Inventory();
			inventory2.setSkuCode("coca_cola");
			inventory2.setQuantity(50);


			Inventory inventory3 = new Inventory();
			inventory3.setSkuCode("pizza_margarita");
			inventory3.setQuantity(50);

			inventoryRepository.save(inventory);
			inventoryRepository.save(inventory2);
			inventoryRepository.save(inventory3);

			System.out.println("Dane startowe Inventory zsynchronizowane z Product Service!");
		};
	}

}
