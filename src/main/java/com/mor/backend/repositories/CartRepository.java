package com.mor.backend.repositories;

import com.mor.backend.entity.Cart;
import com.mor.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart getCartByUser(User user);

}
