package com.mor.backend.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Min;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CartRequest {
    @NonNull
    private long productId;
    @Min(1)
    private Integer quantity;
}
