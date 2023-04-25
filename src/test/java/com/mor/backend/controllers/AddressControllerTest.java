package com.mor.backend.controllers;

import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mor.backend.entity.Address;
import com.mor.backend.common.AuthProvider;
import com.mor.backend.entity.User;
import com.mor.backend.payload.request.AddressRequest;
import com.mor.backend.payload.response.AddressResponse;
import com.mor.backend.services.AddressService;
import com.sun.security.auth.UserPrincipal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {AddressController.class})
@ExtendWith(SpringExtension.class)
class AddressControllerTest {
    @Autowired
    private AddressController addressController;

    @MockBean
    private AddressService addressService;

    /**
     * Method under test: {@link AddressController#createAddress(HttpServletRequest, AddressRequest)}
     */
    @Test
    void testCreateAddress() throws Exception {
        when(addressService.createAddress(Mockito.<AddressRequest>any(), Mockito.<String>any()))
                .thenReturn(new AddressResponse(1L, "Street Name", "Oxford", "GB", "Oxford", "Ward"));
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/api/v1/addresses");
        postResult.principal(new UserPrincipal("principal"));

        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setCity("Oxford");
        addressRequest.setCountry("GB");
        addressRequest.setStreetName("Street Name");
        addressRequest.setTown("Oxford");
        addressRequest.setWard("Ward");
        String content = (new ObjectMapper()).writeValueAsString(addressRequest);
        MockHttpServletRequestBuilder requestBuilder = postResult.contentType(MediaType.APPLICATION_JSON).content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(addressController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"errorCode\":\"201\",\"errorMessage\":\"Success\",\"data\":{\"id\":1,\"streetName\":\"Street Name\",\"city\":\"Oxford"
                                        + "\",\"country\":\"GB\",\"town\":\"Oxford\",\"ward\":\"Ward\"}}"));
    }

    /**
     * Method under test: {@link AddressController#getAddressByUser(HttpServletRequest)}
     */
    @Test
    void testGetAddressByUser() throws Exception {
        when(addressService.getAddressByUser(Mockito.<String>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/addresses");
        requestBuilder.principal(new UserPrincipal("principal"));
        MockMvcBuilders.standaloneSetup(addressController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"errorCode\":\"200\",\"errorMessage\":\"Success\",\"data\":[]}"));
    }

    /**
     * Method under test: {@link AddressController#updateAddress(long, AddressRequest)}
     */
    @Test
    void testUpdateAddress() throws Exception {
        when(addressService.updateAddress(anyLong(), Mockito.<AddressRequest>any()))
                .thenReturn(new AddressResponse(1L, "Street Name", "Oxford", "GB", "Oxford", "Ward"));

        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setCity("Oxford");
        addressRequest.setCountry("GB");
        addressRequest.setStreetName("Street Name");
        addressRequest.setTown("Oxford");
        addressRequest.setWard("Ward");
        String content = (new ObjectMapper()).writeValueAsString(addressRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/addresses/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(addressController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"errorCode\":\"200\",\"errorMessage\":\"OK\",\"data\":{\"id\":1,\"streetName\":\"Street Name\",\"city\":\"Oxford\","
                                        + "\"country\":\"GB\",\"town\":\"Oxford\",\"ward\":\"Ward\"}}"));
    }

    /**
     * Method under test: {@link AddressController#getDetailAddress(long)}
     */
    @Test
    void testGetDetailAddress() throws Exception {
        User user = new User();
        user.setAddresses(new ArrayList<>());
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setIsAdmin(true);
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setProvider(AuthProvider.local);
        user.setProviderId("42");
        user.setRoles(new HashSet<>());
        user.setUsername("janedoe");

        Address address = new Address();
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setId(1L);
        address.setStreetName("Street Name");
        address.setTown("Oxford");
        address.setUser(user);
        address.setWard("Ward");
        Optional<Address> ofResult = Optional.of(address);
        when(addressService.detailAddress(Mockito.<Long>any())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/addresses/{id}", 1L);
        MockMvcBuilders.standaloneSetup(addressController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"errorCode\":\"200\",\"errorMessage\":\"OK\",\"data\":{\"id\":1,\"streetName\":\"Street Name\",\"city\":\"Oxford\","
                                        + "\"country\":\"GB\",\"town\":\"Oxford\",\"ward\":\"Ward\"}}"));
    }

    /**
     * Method under test: {@link AddressController#getDetailAddress(long)}
     */
    @Test
    void testGetDetailAddress2() throws Exception {
        when(addressService.detailAddress(Mockito.<Long>any())).thenReturn(Optional.empty());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/addresses/{id}", 1L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(addressController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"errorCode\":\"204\",\"errorMessage\":\"OK\",\"data\":\"\"}"));
    }
}

