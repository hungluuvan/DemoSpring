package com.mor.backend.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponse {
    private long id;
    private String streetName;

    private String city;

    private String country;

    private String town;

    private String ward;
}
