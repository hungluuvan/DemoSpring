package com.mor.backend.repositories;

import com.mor.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findById(long id);

    List<Product> findByNameContaining(String name);
}
