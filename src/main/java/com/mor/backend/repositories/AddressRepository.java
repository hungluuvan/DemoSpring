package com.mor.backend.repositories;

import com.mor.backend.entity.Address;
import com.mor.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AddressRepository extends JpaRepository<Address, Long> {
    Address findById(long id);
    List<Address> findAllByUser(User user);
}
