package com.mor.backend.repositories;

import com.mor.backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findById(long id);

    @Query("select p from Product p where UPPER(p.name) like CONCAT('%',?1,'%')")
    Page<Product> findByNameContaining(String name, Pageable pageable);

}
