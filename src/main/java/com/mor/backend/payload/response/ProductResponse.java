package com.mor.backend.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private long id;

    private String name;

    private String price;

    private String description;

    private String madeIn;
    private String image;
}
