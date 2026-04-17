package com.biteflow.productservice.repository;


import com.biteflow.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.*;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findBySkuCodeIn(List<String> skuCodes);
}
