package com.mor.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private double cartTotal;

    public void setCartTotal() {
        if (Optional.ofNullable(this.cartItems).isPresent()) {
            this.cartTotal = cartItems.toArray().length;

        } else {
            this.cartTotal = 0.0;
        }
    }

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> cartItems;
    @JsonIgnore
    @OneToOne(mappedBy = "cart")
    private User user;
}
