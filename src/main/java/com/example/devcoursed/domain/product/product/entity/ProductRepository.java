package com.example.devcoursed.domain.product.product.entity;

import com.example.devcoursed.domain.product.product.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long> {
}
