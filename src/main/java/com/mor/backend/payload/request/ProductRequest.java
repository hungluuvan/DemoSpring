package com.mor.backend.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
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

}
