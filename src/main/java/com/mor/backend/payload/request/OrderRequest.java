package com.mor.backend.payload.request;

import com.mor.backend.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private Set<Long> cartItems;
    private Address orderAddress;
}
