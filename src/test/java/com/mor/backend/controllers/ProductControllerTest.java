package com.mor.backend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mor.backend.entity.Product;
import com.mor.backend.payload.request.ProductRequest;
import com.mor.backend.payload.response.ObjectResponse;
import com.mor.backend.payload.response.ProductResponse;
import com.mor.backend.repositories.ProductRepository;
import com.mor.backend.services.ProductService;
import com.mor.backend.services.impl.ProductServiceImpl;
import com.mor.backend.util.ImageUpload;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

@ContextConfiguration(classes = {ProductController.class})
@ExtendWith(SpringExtension.class)
class ProductControllerTest {
    @Autowired
    private ProductController productController;

    @MockBean
    private ProductService productService;

    /**
     * Method under test: {@link ProductController#getAllProduct(String)}
     */
    @Test
    void testGetAllProduct() throws Exception {
        when(productService.getAllProduct(Mockito.<String>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/products");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"errorCode\":\"204\",\"errorMessage\":\"Don't have any products\",\"data\":[]}"));
    }

    /**
     * Method under test: {@link ProductController#getAllProduct(String)}
     */
    @Test
    void testGetAllProduct2() throws Exception {
        ArrayList<ProductResponse> productResponseList = new ArrayList<>();
        productResponseList
                .add(new ProductResponse(1L, "?", "?", "The characteristics of someone or something", "?", "?"));
        when(productService.getAllProduct(Mockito.<String>any())).thenReturn(productResponseList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/products");
        MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"errorCode\":\"200\",\"errorMessage\":\"Success\",\"data\":[{\"id\":1,\"name\":\"?\",\"price\":\"?\",\"description\":\"The"
                                        + " characteristics of someone or something\",\"madeIn\":\"?\",\"image\":\"?\"}]}"));
    }

    /**
     * Method under test: {@link ProductController#updateProduct(long, MultipartFile, ProductRequest)}
     */
    @Test
    void testUpdateProduct() throws IOException {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   org.springframework.web.multipart.MultipartException: Current request is not a multipart request
        //       at javax.servlet.http.HttpServlet.service(HttpServlet.java:684)
        //       at javax.servlet.http.HttpServlet.service(HttpServlet.java:764)
        //   See https://diff.blue/R013 to resolve this issue.

        Product product = new Product();
        product.setDescription("The characteristics of someone or something");
        product.setId(1L);
        product.setImage("Image");
        product.setMadeIn("Made In");
        product.setName("Name");
        product.setPrice(10.0d);

        Product product2 = new Product();
        product2.setDescription("The characteristics of someone or something");
        product2.setId(1L);
        product2.setImage("Image");
        product2.setMadeIn("Made In");
        product2.setName("Name");
        product2.setPrice(10.0d);
        ProductRepository productRepository = mock(ProductRepository.class);
        when(productRepository.save(Mockito.<Product>any())).thenReturn(product2);
        when(productRepository.findById(anyLong())).thenReturn(product);
        ImageUpload imageUpload = new ImageUpload();
        ProductController productController = new ProductController(
                new ProductServiceImpl(productRepository, imageUpload, new ModelMapper()));
        MockMultipartFile imageProduct = new MockMultipartFile("Name",
                new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8")));

        ResponseEntity<?> actualUpdateProductResult = productController.updateProduct(1L, imageProduct,
                new ProductRequest("Name", 10.0d, "The characteristics of someone or something", "Made In"));
        assertTrue(actualUpdateProductResult.hasBody());
        assertTrue(actualUpdateProductResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.OK, actualUpdateProductResult.getStatusCode());
        Object data = ((ObjectResponse) actualUpdateProductResult.getBody()).getData();
        assertTrue(data instanceof ProductResponse);
        assertEquals("200", ((ObjectResponse) actualUpdateProductResult.getBody()).getErrorCode());
        assertEquals("OK", ((ObjectResponse) actualUpdateProductResult.getBody()).getErrorMessage());
        assertEquals("10.0", ((ProductResponse) data).getPrice());
        assertEquals("Name", ((ProductResponse) data).getName());
        assertEquals("Made In", ((ProductResponse) data).getMadeIn());
        assertEquals("QVhBWEFYQVg=", ((ProductResponse) data).getImage());
        assertEquals(1L, ((ProductResponse) data).getId());
        assertEquals("The characteristics of someone or something", ((ProductResponse) data).getDescription());
        verify(productRepository).findById(anyLong());
        verify(productRepository).save(Mockito.<Product>any());
    }


    /**
     * Method under test: {@link ProductController#createProduct(MultipartFile, ProductRequest)}
     */
    @Test
    void testCreateProduct() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/products");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link ProductController#deleteProduct(long)}
     */
    @Test
    void testDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/products/{id}", 1L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link ProductController#deleteProduct(long)}
     */
    @Test
    void testDeleteProduct2() throws Exception {
        doNothing().when(productService).deleteProduct(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/products/{id}", 1L);
        requestBuilder.characterEncoding("Encoding");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link ProductController#getRetrieveProduct(long)}
     */
    @Test
    void testGetRetrieveProduct() throws Exception {
        Product product = new Product();
        product.setDescription("The characteristics of someone or something");
        product.setId(1L);
        product.setImage("Image");
        product.setMadeIn("Made In");
        product.setName("Name");
        product.setPrice(10.0d);
        Optional<Product> ofResult = Optional.of(product);
        when(productService.detailProduct(anyLong())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/products/{id}", 1L);
        MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"errorCode\":\"200\",\"errorMessage\":\"OK\",\"data\":{\"id\":1,\"name\":\"Name\",\"price\":10.0,\"description\":\"The"
                                        + " characteristics of someone or something\",\"madeIn\":\"Made In\",\"image\":\"Image\"}}"));
    }

    /**
     * Method under test: {@link ProductController#getRetrieveProduct(long)}
     */
    @Test
    void testGetRetrieveProduct2() throws Exception {
        when(productService.detailProduct(anyLong())).thenReturn(Optional.empty());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/products/{id}", 1L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"errorCode\":\"204\",\"errorMessage\":\"OK\",\"data\":\"\"}"));
    }
}

