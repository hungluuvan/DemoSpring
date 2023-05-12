package com.mor.backend.services.impl;

import com.mor.backend.entity.Address;
import com.mor.backend.entity.User;
import com.mor.backend.exeptions.NotFoundException;
import com.mor.backend.payload.request.AddressRequest;
import com.mor.backend.payload.response.AddressResponse;
import com.mor.backend.repositories.AddressRepository;
import com.mor.backend.repositories.UserRepository;
import com.mor.backend.services.AddressService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final ModelMapper mapper;
    private final UserRepository userRepository;


    @Override
    public AddressResponse createAddress(AddressRequest addressRequest, String username) {
        Optional<User> user = userRepository.findByUsername(username);
        Address address = new Address();
        address.setCity(addressRequest.getCity());
        address.setTown(addressRequest.getTown());
        address.setCountry(addressRequest.getCountry());
        address.setWard(addressRequest.getWard());
        address.setStreetName(addressRequest.getStreetName());
        address.setUser(user.orElse(null));
        addressRepository.save(address);
        return mapper.map(address, AddressResponse.class);
    }

    @Override
    public List<AddressResponse> getAddressByUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(value -> addressRepository
                .findAllByUser(value).stream()
                .map(address -> mapper.map(address, AddressResponse.class))
                .collect(Collectors.toList())).orElse(null);
    }

    @Transactional
    @Override
    public AddressResponse updateAddress(long id, AddressRequest addressRequest) {
        Address address = Optional.ofNullable(addressRepository.findById(id)).orElseThrow(() ->
                new NotFoundException("Address with " + id + " not found ")
        );
        address.setStreetName(addressRequest.getStreetName());
        address.setCity(addressRequest.getCity());
        address.setCountry(addressRequest.getCountry());
        address.setTown(addressRequest.getTown());
        address.setWard(address.getWard());
        addressRepository.save(address);
        return mapper.map(address, AddressResponse.class);

    }

    @Override
    public Optional<Address> detailAddress(Long id) {
        return addressRepository.findById(id);
    }


}
