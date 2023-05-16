package com.mor.backend.repositories;

import com.mor.backend.entity.OrderProduct;
import com.mor.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderProduct,Long> {
    OrderProduct findById(long id);
    List<OrderProduct> findByUser(User user);

}
