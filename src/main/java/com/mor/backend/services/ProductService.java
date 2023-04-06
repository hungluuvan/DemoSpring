package com.mor.backend.services;

import com.mor.backend.entity.Product;
import com.mor.backend.payload.request.ProductRequest;
import com.mor.backend.payload.response.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    Optional<Product> detailProduct(Long id);

    void deleteProduct(Long id);

    List<ProductResponse> getAllProduct(String name);

    ProductResponse createProduct(ProductRequest product, MultipartFile imageProduct);

    ProductResponse updateProduct(Product product, ProductRequest productRequest, MultipartFile imageProduct) throws IOException;


}
