package com.mor.backend.controllers;


import com.mor.backend.entity.Product;
import com.mor.backend.payload.request.ProductRequest;
import com.mor.backend.payload.response.ObjectResponse;
import com.mor.backend.payload.response.PaginationResponse;
import com.mor.backend.payload.response.ProductResponse;
import com.mor.backend.services.ProductService;
import com.mor.backend.util.ImageUpload;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor
@SecurityRequirement(name = "demo")
@RequestMapping("/api/v1/")
public class ProductController {

    private final ProductService productService;
    private final ImageUpload imageUpload;

    @GetMapping("products")
    public ResponseEntity<ObjectResponse> getAllProduct(@RequestParam(defaultValue = "") String name
            , @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        try {
            PaginationResponse products = productService.getAllProduct(name, size, page);
            return new ResponseEntity<>(new ObjectResponse("200", "Success", products), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ObjectResponse("500", "Error", ""), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
    @GetMapping("/products/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = imageUpload.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("products/{id}")
    public ResponseEntity<?> getRetrieveProduct(@PathVariable("id") long id) {
        Optional<Product> product = productService.detailProduct(id);
        return product.map(value -> new ResponseEntity<>(new ObjectResponse("200", "OK", value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(new ObjectResponse("204", "OK", ""), HttpStatus.NO_CONTENT));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("products")
    public ResponseEntity<?> createProduct(@RequestParam(value = "imageProduct", required = false) MultipartFile imageProduct, @Valid @ModelAttribute ProductRequest productRequest
    ) {
        ProductResponse product = productService
                .createProduct(productRequest, imageProduct);
        return new ResponseEntity<>(new ObjectResponse("201", "Success", product), HttpStatus.CREATED);

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("products/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") long id, @RequestParam(value = "imageProduct") MultipartFile imageProduct, @Valid @ModelAttribute ProductRequest productRequest) throws IOException {
        ProductResponse result = productService.updateProduct(id, productRequest, imageProduct);
        return new ResponseEntity<>(new ObjectResponse("200", "OK", result), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
