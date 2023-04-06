package com.mor.backend.controllers;

import com.mor.backend.entity.Address;
import com.mor.backend.payload.request.AddressRequest;
import com.mor.backend.payload.response.AddressResponse;
import com.mor.backend.payload.response.ObjectResponse;
import com.mor.backend.services.AddressService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@SecurityRequirement(name = "demo")
@RequestMapping("/api/v1/")
public class AddressController {
    @Autowired
    AddressService addressService;

    @PostMapping("/addresses")
    public ResponseEntity<?> createAddress(HttpServletRequest request, @Valid @RequestBody AddressRequest addressRequest) {
        String username = request.getUserPrincipal().getName();
        AddressResponse address = addressService.createAddress(addressRequest, username);
        return new ResponseEntity<>(new ObjectResponse("201", "Success", address), HttpStatus.CREATED);

    }

    @GetMapping("/addresses")
    public ResponseEntity<?> getAddressByUser(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        List<AddressResponse> addresses = addressService.getAddressByUser(username);
        return new ResponseEntity<>(new ObjectResponse("200", "Success", addresses), HttpStatus.OK);

    }

    @GetMapping("/addresses/{id}")
    public ResponseEntity<?> getDetailAddress(@PathVariable("id") long id) {
        Optional<Address> address = addressService.detailAddress(id);
        return address.map(value -> new ResponseEntity<>(new ObjectResponse("200", "OK", value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(new ObjectResponse("204", "OK", ""), HttpStatus.NO_CONTENT));
    }

    @PutMapping("/addresses/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable("id") long id, @Valid @RequestBody AddressRequest addressRequest) {
        Optional<Address> addressData = addressService.detailAddress(id);
        if (addressData.isPresent()) {
            Address addressChange = addressData.get();
            AddressResponse result = addressService.updateAddress(addressChange, addressRequest);
            return new ResponseEntity<>(new ObjectResponse("200", "OK", result), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ObjectResponse("204", "Not Found", ""), HttpStatus.NOT_FOUND);
        }
    }

}

