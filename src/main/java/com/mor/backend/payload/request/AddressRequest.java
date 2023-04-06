package com.mor.backend.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {
    @NotBlank
    private String streetName;
    @NotBlank
    private String city;
    @NotBlank
    private String country;
    @NotBlank
    private String town;
    @NotBlank
    private String ward;
}
