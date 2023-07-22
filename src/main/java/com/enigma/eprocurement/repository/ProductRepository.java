package com.enigma.eprocurement.repository;

import com.enigma.eprocurement.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}
