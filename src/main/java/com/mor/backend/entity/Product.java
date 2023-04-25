package com.mor.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max = 50)
    private String name;
    @DecimalMin("1.0")
    private double price;
    @NotBlank
    @Size(max = 100)
    private String description;
    @NotBlank
    @Size(max = 50)
    private String madeIn;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String image;

    public Product(String name, double price, String description, String madeIn, String image) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.madeIn = madeIn;
        this.image = image;
    }

}
