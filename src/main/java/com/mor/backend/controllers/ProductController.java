package com.mor.backend.controllers;


import com.mor.backend.entity.Product;
import com.mor.backend.payload.request.ProductRequest;
import com.mor.backend.payload.response.ObjectResponse;
import com.mor.backend.payload.response.ProductResponse;
import com.mor.backend.services.ProductService;
import com.mor.backend.services.impl.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@SecurityRequirement(name = "demo")
@RequestMapping("/api/v1/")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @GetMapping("products")
    public ResponseEntity<ObjectResponse> getAllProduct(@RequestParam(required = false) String name) {
        try {

            List<ProductResponse> products = new ArrayList<>(productService.getAllProduct(name));

            if (products.isEmpty()) {
                return new ResponseEntity<>(new ObjectResponse("204", "Don't have any products", products), HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(new ObjectResponse("200", "Success", products), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ObjectResponse("500", "Error", ""), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @GetMapping("products/{id}")
    public ResponseEntity<?> getRetrieveProduct(@PathVariable("id") long id) {
        Optional<Product> product = productService.detailProduct(id);
        return product.map(value -> new ResponseEntity<>(new ObjectResponse("200", "OK", value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(new ObjectResponse("204", "OK", ""), HttpStatus.NO_CONTENT));
    }

    @PostMapping("products")
    public ResponseEntity<?> createProduct(@RequestParam(value = "imageProduct", required = false) MultipartFile imageProduct, @Valid @ModelAttribute ProductRequest productRequest
    ) {
        ProductResponse product = productService
                .createProduct(productRequest, imageProduct);
        return new ResponseEntity<>(new ObjectResponse("201", "Success", product), HttpStatus.CREATED);

    }

    @PutMapping("products/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") long id, @RequestParam(value = "imageProduct") MultipartFile imageProduct, @Valid @ModelAttribute ProductRequest productRequest) throws IOException {
        Optional<Product> productData = productService.detailProduct(id);

        if (productData.isPresent()) {
            Product productChange = productData.get();
            ProductResponse result = productService.updateProduct(productChange, productRequest, imageProduct);
            return new ResponseEntity<>(new ObjectResponse("200", "OK", result), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ObjectResponse("204", "Not Found", ""), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("products/{id}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("id") long id) {
        try {
            productService.deleteProduct(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
