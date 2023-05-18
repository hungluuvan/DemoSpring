package com.mor.backend.services.impl;

import com.mor.backend.controllers.ProductController;
import com.mor.backend.entity.Product;
import com.mor.backend.exeptions.NotFoundException;
import com.mor.backend.payload.request.ProductRequest;
import com.mor.backend.payload.response.PaginationResponse;
import com.mor.backend.payload.response.ProductResponse;
import com.mor.backend.repositories.ProductRepository;
import com.mor.backend.services.ProductService;
import com.mor.backend.util.ImageUpload;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    private final ImageUpload imageUpload;

    private final ModelMapper mapper;

    @Override
    public Optional<Product> detailProduct(long id) {
        log.info("Product id {}", id);
        return Optional.ofNullable(Optional.ofNullable(productRepository.findById(id)).orElseThrow(() -> new NotFoundException("Product with id " + id + " not found")));

    }

    @Transactional
    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public PaginationResponse getAllProduct(String name, int size, int page) {
        Pageable paging = PageRequest.of(page, size);
        Page<Product> result = productRepository.findByNameContaining(name, paging);
        PaginationResponse response = new PaginationResponse();
        response.setContents(result.getContent());
        response.setTotalItems(result.getTotalElements());
        response.setCurrentPage(result.getNumber());
        response.setTotalPages(result.getTotalPages());
        return response;
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest, MultipartFile imageProduct) {
        try {
            Product product = new Product();
            if (imageProduct == null) {
                product.setUrlImage(null);
            } else {
                if (imageUpload.uploadImage(imageProduct)) {
                    log.info("Upload successfully");
                }
                String url = MvcUriComponentsBuilder
                        .fromMethodName(ProductController.class, "getFile", imageProduct.getOriginalFilename()).build().toString();
                product.setUrlImage(url);
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
            product.setUrlImage(product.getUrlImage());
        } else {
            if (!imageUpload.checkExisted(imageProduct)) {
                imageUpload.uploadImage(imageProduct);
                String url = MvcUriComponentsBuilder
                        .fromMethodName(ProductController.class, "getFile", imageProduct.getOriginalFilename()).build().toString();
                product.setUrlImage(url);
            }

        }
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setMadeIn(productRequest.getMadeIn());
        productRepository.save(product);
        return mapper.map(product, ProductResponse.class);
    }


}
