package com.mor.backend.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mor.backend.entity.Address;
import com.mor.backend.common.AuthProvider;
import com.mor.backend.entity.User;
import com.mor.backend.exeptions.NotFoundException;
import com.mor.backend.payload.request.AddressRequest;
import com.mor.backend.payload.response.AddressResponse;
import com.mor.backend.repositories.AddressRepository;
import com.mor.backend.repositories.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {AddressServiceImpl.class})
@ExtendWith(SpringExtension.class)
class AddressServiceImplTest {
    @MockBean
    private AddressRepository addressRepository;

    @Autowired
    private AddressServiceImpl addressServiceImpl;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private UserRepository userRepository;

    /**
     * Method under test: {@link AddressServiceImpl#createAddress(AddressRequest, String)}
     */
    @Test
    void testCreateAddress() {
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
        when(addressRepository.save(Mockito.<Address>any())).thenReturn(address);
        AddressResponse addressResponse = new AddressResponse(1L, "Street Name", "Oxford", "GB", "Oxford", "Ward");

        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<AddressResponse>>any())).thenReturn(addressResponse);

        User user2 = new User();
        user2.setAddresses(new ArrayList<>());
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setIsAdmin(true);
        user2.setName("Name");
        user2.setPassword("iloveyou");
        user2.setProvider(AuthProvider.local);
        user2.setProviderId("42");
        user2.setRoles(new HashSet<>());
        user2.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user2);
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(ofResult);
        assertSame(addressResponse, addressServiceImpl
                .createAddress(new AddressRequest("Street Name", "Oxford", "GB", "Oxford", "Ward"), "janedoe"));
        verify(addressRepository).save(Mockito.<Address>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<AddressResponse>>any());
        verify(userRepository).findByUsername(Mockito.<String>any());
    }

    /**
     * Method under test: {@link AddressServiceImpl#createAddress(AddressRequest, String)}
     */
    @Test
    void testCreateAddress2() {
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
        when(addressRepository.save(Mockito.<Address>any())).thenReturn(address);
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<Object>>any())).thenReturn("Map");
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<AddressResponse>>any()))
                .thenReturn(new AddressResponse(1L, "Street Name", "Oxford", "GB", "Oxford", "Ward"));
        when(userRepository.findByUsername(Mockito.<String>any())).thenThrow(new NotFoundException("An error occurred"));
        assertThrows(NotFoundException.class, () -> addressServiceImpl
                .createAddress(new AddressRequest("Street Name", "Oxford", "GB", "Oxford", "Ward"), "janedoe"));
//        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<Object>>any());
        verify(userRepository).findByUsername(Mockito.<String>any());
    }

    /**
     * Method under test: {@link AddressServiceImpl#getAddressByUser(String)}
     */
    @Test
    void testGetAddressByUser() {
        when(addressRepository.findAllByUser(Mockito.<User>any())).thenReturn(new ArrayList<>());

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
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(ofResult);
        assertTrue(addressServiceImpl.getAddressByUser("janedoe").isEmpty());
        verify(addressRepository).findAllByUser(Mockito.<User>any());
        verify(userRepository).findByUsername(Mockito.<String>any());
    }

    /**
     * Method under test: {@link AddressServiceImpl#getAddressByUser(String)}
     */
    @Test
    void testGetAddressByUser2() {
        when(addressRepository.findAllByUser(Mockito.<User>any())).thenThrow(new NotFoundException("An error occurred"));

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
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(ofResult);
        assertThrows(NotFoundException.class, () -> addressServiceImpl.getAddressByUser("janedoe"));
        verify(addressRepository).findAllByUser(Mockito.<User>any());
        verify(userRepository).findByUsername(Mockito.<String>any());
    }

    /**
     * Method under test: {@link AddressServiceImpl#getAddressByUser(String)}
     */
    @Test
    void testGetAddressByUser3() {
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

        ArrayList<Address> addressList = new ArrayList<>();
        addressList.add(address);
        when(addressRepository.findAllByUser(Mockito.<User>any())).thenReturn(addressList);
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<AddressResponse>>any()))
                .thenReturn(new AddressResponse(1L, "Street Name", "Oxford", "GB", "Oxford", "Ward"));

        User user2 = new User();
        user2.setAddresses(new ArrayList<>());
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setIsAdmin(true);
        user2.setName("Name");
        user2.setPassword("iloveyou");
        user2.setProvider(AuthProvider.local);
        user2.setProviderId("42");
        user2.setRoles(new HashSet<>());
        user2.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user2);
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(ofResult);
        assertEquals(1, addressServiceImpl.getAddressByUser("janedoe").size());
        verify(addressRepository).findAllByUser(Mockito.<User>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<AddressResponse>>any());
        verify(userRepository).findByUsername(Mockito.<String>any());
    }

    /**
     * Method under test: {@link AddressServiceImpl#getAddressByUser(String)}
     */
    @Test
    void testGetAddressByUser4() {
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

        User user2 = new User();
        user2.setAddresses(new ArrayList<>());
        user2.setEmail("john.smith@example.org");
        user2.setId(2L);
        user2.setIsAdmin(false);
        user2.setName("42");
        user2.setPassword("Password");
        user2.setProvider(AuthProvider.google);
        user2.setProviderId("Provider Id");
        user2.setRoles(new HashSet<>());
        user2.setUsername("Username");

        Address address2 = new Address();
        address2.setCity("London");
        address2.setCountry("GBR");
        address2.setId(2L);
        address2.setStreetName("com.mor.backend.entity.Address");
        address2.setTown("Town");
        address2.setUser(user2);
        address2.setWard("com.mor.backend.entity.Address");

        ArrayList<Address> addressList = new ArrayList<>();
        addressList.add(address2);
        addressList.add(address);
        when(addressRepository.findAllByUser(Mockito.<User>any())).thenReturn(addressList);
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<AddressResponse>>any()))
                .thenReturn(new AddressResponse(1L, "Street Name", "Oxford", "GB", "Oxford", "Ward"));

        User user3 = new User();
        user3.setAddresses(new ArrayList<>());
        user3.setEmail("jane.doe@example.org");
        user3.setId(1L);
        user3.setIsAdmin(true);
        user3.setName("Name");
        user3.setPassword("iloveyou");
        user3.setProvider(AuthProvider.local);
        user3.setProviderId("42");
        user3.setRoles(new HashSet<>());
        user3.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user3);
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(ofResult);
        assertEquals(2, addressServiceImpl.getAddressByUser("janedoe").size());
        verify(addressRepository).findAllByUser(Mockito.<User>any());
        verify(modelMapper, atLeast(1)).map(Mockito.<Object>any(), Mockito.<Class<AddressResponse>>any());
        verify(userRepository).findByUsername(Mockito.<String>any());
    }

    /**
     * Method under test: {@link AddressServiceImpl#getAddressByUser(String)}
     */
    @Test
    void testGetAddressByUser5() {
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

        ArrayList<Address> addressList = new ArrayList<>();
        addressList.add(address);
        when(addressRepository.findAllByUser(Mockito.<User>any())).thenReturn(addressList);
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<Object>>any())).thenReturn("Map");
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<AddressResponse>>any()))
                .thenReturn(new AddressResponse(1L, "Street Name", "Oxford", "GB", "Oxford", "Ward"));
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(Optional.empty());
        assertNull(addressServiceImpl.getAddressByUser("janedoe"));
//        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<Object>>any());
        verify(userRepository).findByUsername(Mockito.<String>any());
    }

    /**
     * Method under test: {@link AddressServiceImpl#updateAddress(long, AddressRequest)}
     */
    @Test
    void testUpdateAddress() {
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

        User user2 = new User();
        user2.setAddresses(new ArrayList<>());
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setIsAdmin(true);
        user2.setName("Name");
        user2.setPassword("iloveyou");
        user2.setProvider(AuthProvider.local);
        user2.setProviderId("42");
        user2.setRoles(new HashSet<>());
        user2.setUsername("janedoe");

        Address address2 = new Address();
        address2.setCity("Oxford");
        address2.setCountry("GB");
        address2.setId(1L);
        address2.setStreetName("Street Name");
        address2.setTown("Oxford");
        address2.setUser(user2);
        address2.setWard("Ward");
        when(addressRepository.save(Mockito.<Address>any())).thenReturn(address2);
        when(addressRepository.findById(anyLong())).thenReturn(address);
        AddressResponse addressResponse = new AddressResponse(1L, "Street Name", "Oxford", "GB", "Oxford", "Ward");

        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<AddressResponse>>any())).thenReturn(addressResponse);
        assertSame(addressResponse,
                addressServiceImpl.updateAddress(1L, new AddressRequest("Street Name", "Oxford", "GB", "Oxford", "Ward")));
        verify(addressRepository).findById(anyLong());
        verify(addressRepository).save(Mockito.<Address>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<AddressResponse>>any());
    }

    /**
     * Method under test: {@link AddressServiceImpl#updateAddress(long, AddressRequest)}
     */
    @Test
    void testUpdateAddress2() {
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

        User user2 = new User();
        user2.setAddresses(new ArrayList<>());
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setIsAdmin(true);
        user2.setName("Name");
        user2.setPassword("iloveyou");
        user2.setProvider(AuthProvider.local);
        user2.setProviderId("42");
        user2.setRoles(new HashSet<>());
        user2.setUsername("janedoe");

        Address address2 = new Address();
        address2.setCity("Oxford");
        address2.setCountry("GB");
        address2.setId(1L);
        address2.setStreetName("Street Name");
        address2.setTown("Oxford");
        address2.setUser(user2);
        address2.setWard("Ward");
        when(addressRepository.save(Mockito.<Address>any())).thenReturn(address2);
        when(addressRepository.findById(anyLong())).thenReturn(address);
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<AddressResponse>>any()))
                .thenThrow(new NotFoundException("An error occurred"));
        assertThrows(NotFoundException.class, () -> addressServiceImpl.updateAddress(1L,
                new AddressRequest("Street Name", "Oxford", "GB", "Oxford", "Ward")));
        verify(addressRepository).findById(anyLong());
        verify(addressRepository).save(Mockito.<Address>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<AddressResponse>>any());
    }

    /**
     * Method under test: {@link AddressServiceImpl#detailAddress(Long)}
     */
    @Test
    void testDetailAddress() {
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
        when(addressRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        Optional<Address> actualDetailAddressResult = addressServiceImpl.detailAddress(1L);
        assertSame(ofResult, actualDetailAddressResult);
        assertTrue(actualDetailAddressResult.isPresent());
        verify(addressRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link AddressServiceImpl#detailAddress(Long)}
     */
    @Test
    void testDetailAddress2() {
        when(addressRepository.findById(Mockito.<Long>any())).thenThrow(new NotFoundException("An error occurred"));
        assertThrows(NotFoundException.class, () -> addressServiceImpl.detailAddress(1L));
        verify(addressRepository).findById(Mockito.<Long>any());
    }
}

