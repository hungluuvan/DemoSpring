package com.mor.backend.services;

import com.mor.backend.entity.Product;
import com.mor.backend.payload.request.ProductRequest;
import com.mor.backend.payload.response.PaginationResponse;
import com.mor.backend.payload.response.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface ProductService {
    Optional<Product> detailProduct(long id);

    void deleteProduct(Long id);

    PaginationResponse getAllProduct(String name, int size, int page);

    ProductResponse createProduct(ProductRequest product, MultipartFile imageProduct);

    ProductResponse updateProduct(long id, ProductRequest productRequest, MultipartFile imageProduct) throws IOException;
}
