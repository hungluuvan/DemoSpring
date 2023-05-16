package com.mor.backend.repositories;

import com.mor.backend.entity.CartItem;
import com.mor.backend.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByIdIn(Set<Long> ids);

}
