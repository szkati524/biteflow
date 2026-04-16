package com.biteflow.productservice.repository;


import com.biteflow.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.*;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
}
