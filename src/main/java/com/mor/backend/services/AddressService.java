package com.mor.backend.services;

import com.mor.backend.entity.Address;
import com.mor.backend.payload.request.AddressRequest;
import com.mor.backend.payload.response.AddressResponse;

import java.util.List;
import java.util.Optional;

public interface AddressService {
    AddressResponse createAddress(AddressRequest addressRequest, String user);

    List<AddressResponse> getAddressByUser(String user);

    AddressResponse updateAddress(long id , AddressRequest addressRequest);

    Optional<Address> detailAddress(Long id);
}
