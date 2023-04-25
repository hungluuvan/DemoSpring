package com.mor.backend.services.impl;

import com.mor.backend.entity.Product;
import com.mor.backend.exeptions.NotFoundException;
import com.mor.backend.payload.request.ProductRequest;
import com.mor.backend.payload.response.ProductResponse;
import com.mor.backend.repositories.ProductRepository;
import com.mor.backend.services.ProductService;
import com.mor.backend.util.ImageUpload;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    private final ImageUpload imageUpload;

    private final ModelMapper mapper;

    @Override
    public Optional<Product> detailProduct(long id) {
        return Optional.ofNullable(Optional.ofNullable(productRepository.findById(id)).orElseThrow(() -> new NotFoundException("Product with id " + id + " not found")));

    }

    @Transactional
    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<ProductResponse> getAllProduct(String name) {
        List<Product> products;
        if (name != null) {
            products = productRepository.findByNameContaining(name);

        } else {
            products = productRepository.findAll();
        }
        return products.stream().map(p -> mapper.map(p, ProductResponse.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest, MultipartFile imageProduct) {
        try {
            Product product = new Product();
            if (imageProduct == null) {
                product.setImage(null);
            } else {
                if (imageUpload.uploadImage(imageProduct)) {
                    System.out.println("Upload successfully");
                }
                product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
            }
            product.setName(productRequest.getName());
            product.setDescription(productRequest.getDescription());
            product.setPrice(productRequest.getPrice());
            product.setMadeIn(productRequest.getMadeIn());
            productRepository.save(product);
            return mapper.map(product, ProductResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    @Override
    public ProductResponse updateProduct(long id, ProductRequest productRequest, MultipartFile imageProduct) throws IOException {
        Product product = Optional.ofNullable(productRepository.findById(id)).orElseThrow(() -> new NotFoundException("Product with id " + id + " not found "));
        if (imageProduct == null) {
            product.setImage(product.getImage());
        } else {
            if (!imageUpload.checkExisted(imageProduct)) {
                imageUpload.uploadImage(imageProduct);
            }
            product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
        }
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setMadeIn(productRequest.getMadeIn());
        productRepository.save(product);
        return mapper.map(product, ProductResponse.class);
    }


}
