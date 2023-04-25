package com.mor.backend.controllers;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mor.backend.common.AuthProvider;
import com.mor.backend.entity.RefreshToken;
import com.mor.backend.entity.User;
import com.mor.backend.exeptions.TokenRefreshException;
import com.mor.backend.payload.request.LoginRequest;
import com.mor.backend.payload.request.SignupRequest;
import com.mor.backend.payload.request.TokenRefreshRequest;
import com.mor.backend.repositories.RoleRepository;
import com.mor.backend.repositories.UserRepository;
import com.mor.backend.services.impl.RefreshTokenService;
import com.mor.backend.services.impl.UserDetailsImpl;
import com.mor.backend.util.jwt.Jwt;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {AuthController.class})
@ExtendWith(SpringExtension.class)
class AuthControllerTest {
    @Autowired
    private AuthController authController;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private Jwt jwt;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private RefreshTokenService refreshTokenService;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserRepository userRepository;

    /**
     * Method under test: {@link AuthController#authenticateUser(LoginRequest)}
     */
    @Test
    void testAuthenticateUser() throws Exception {
        when(jwt.generateJwtToken(Mockito.<Authentication>any())).thenReturn("ABC123");
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenThrow(new TokenRefreshException("0123456789ABCDEF", "0123456789ABCDEF"));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("iloveyou");
        loginRequest.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(loginRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authController).build().perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    /**
     * Method under test: {@link AuthController#authenticateUser(LoginRequest)}
     */
    @Test
    void testAuthenticateUser2() throws Exception {
        when(jwt.generateJwtToken(Mockito.<Authentication>any())).thenReturn("ABC123");

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

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiryDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        refreshToken.setId(1L);
        refreshToken.setToken("ABC123");
        refreshToken.setUser(user);
        when(refreshTokenService.createRefreshToken(Mockito.<Long>any())).thenReturn(refreshToken);
        when(authenticationManager.authenticate(Mockito.<Authentication>any())).thenReturn(new TestingAuthenticationToken(
                new UserDetailsImpl(1L, "janedoe", "jane.doe@example.org", "iloveyou", new ArrayList<>()), "Credentials"));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("iloveyou");
        loginRequest.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(loginRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"accessToken\":\"ABC123\",\"refreshToken\":\"ABC123\",\"username\":\"janedoe\",\"email\":\"jane.doe@example.org\","
                                        + "\"roles\":[]}"));
    }

    /**
     * Method under test: {@link AuthController#authenticateUser(LoginRequest)}
     */
    @Test
    void testAuthenticateUser3() throws Exception {
        when(jwt.generateJwtToken(Mockito.<Authentication>any())).thenReturn("ABC123");

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

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiryDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        refreshToken.setId(1L);
        refreshToken.setToken("ABC123");
        refreshToken.setUser(user);
        when(refreshTokenService.createRefreshToken(Mockito.<Long>any())).thenReturn(refreshToken);
        when(authenticationManager.authenticate(Mockito.<Authentication>any())).thenReturn(new TestingAuthenticationToken(
                new UserDetailsImpl(1L, "janedoe", "jane.doe@example.org", "iloveyou", new ArrayList<>()), "Credentials"));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("");
        loginRequest.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(loginRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link AuthController#authenticateUser(LoginRequest)}
     */
    @Test
    void testAuthenticateUser4() throws Exception {
        when(jwt.generateJwtToken(Mockito.<Authentication>any())).thenReturn("ABC123");
        when(refreshTokenService.createRefreshToken(Mockito.<Long>any()))
                .thenThrow(new TokenRefreshException("0123456789ABCDEF", "0123456789ABCDEF"));
        when(authenticationManager.authenticate(Mockito.<Authentication>any())).thenReturn(new TestingAuthenticationToken(
                new UserDetailsImpl(1L, "janedoe", "jane.doe@example.org", "iloveyou", new ArrayList<>()), "Credentials"));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("iloveyou");
        loginRequest.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(loginRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    /**
     * Method under test: {@link AuthController#googleLogin(String)}
     */
    @Test
    void testGoogleLogin() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/auth/google")
                .param("access_token", "foo");
        MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"errorCode\":\"200\",\"errorMessage\":\"\",\"data\":\"foo\"}"));
    }

    /**
     * Method under test: {@link AuthController#refreshtoken(TokenRefreshRequest)}
     */
    @Test
    void testRefreshtoken() throws Exception {
        when(jwt.generateTokenFromUsername(Mockito.<String>any())).thenReturn("janedoe");

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

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiryDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        refreshToken.setId(1L);
        refreshToken.setToken("ABC123");
        refreshToken.setUser(user);
        Optional<RefreshToken> ofResult = Optional.of(refreshToken);

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

        RefreshToken refreshToken2 = new RefreshToken();
        refreshToken2.setExpiryDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        refreshToken2.setId(1L);
        refreshToken2.setToken("ABC123");
        refreshToken2.setUser(user2);
        when(refreshTokenService.verifyExpiration(Mockito.<RefreshToken>any())).thenReturn(refreshToken2);
        when(refreshTokenService.findByToken(Mockito.<String>any())).thenReturn(ofResult);

        TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest();
        tokenRefreshRequest.setRefreshToken("ABC123");
        String content = (new ObjectMapper()).writeValueAsString(tokenRefreshRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/auth/refreshtoken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"accessToken\":\"janedoe\",\"refreshToken\":\"ABC123\"}"));
    }

    /**
     * Method under test: {@link AuthController#refreshtoken(TokenRefreshRequest)}
     */
    @Test
    void testRefreshtoken2() throws Exception {
        when(jwt.generateTokenFromUsername(Mockito.<String>any()))
                .thenThrow(new TokenRefreshException("0123456789ABCDEF", "0123456789ABCDEF"));

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

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiryDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        refreshToken.setId(1L);
        refreshToken.setToken("ABC123");
        refreshToken.setUser(user);
        Optional<RefreshToken> ofResult = Optional.of(refreshToken);

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

        RefreshToken refreshToken2 = new RefreshToken();
        refreshToken2.setExpiryDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        refreshToken2.setId(1L);
        refreshToken2.setToken("ABC123");
        refreshToken2.setUser(user2);
        when(refreshTokenService.verifyExpiration(Mockito.<RefreshToken>any())).thenReturn(refreshToken2);
        when(refreshTokenService.findByToken(Mockito.<String>any())).thenReturn(ofResult);

        TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest();
        tokenRefreshRequest.setRefreshToken("ABC123");
        String content = (new ObjectMapper()).writeValueAsString(tokenRefreshRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/auth/refreshtoken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    /**
     * Method under test: {@link AuthController#refreshtoken(TokenRefreshRequest)}
     */
    @Test
    void testRefreshtoken3() throws Exception {
        when(jwt.generateTokenFromUsername(Mockito.<String>any())).thenReturn("janedoe");

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

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiryDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        refreshToken.setId(1L);
        refreshToken.setToken("ABC123");
        refreshToken.setUser(user);
        when(refreshTokenService.verifyExpiration(Mockito.<RefreshToken>any())).thenReturn(refreshToken);
        when(refreshTokenService.findByToken(Mockito.<String>any())).thenReturn(Optional.empty());

        TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest();
        tokenRefreshRequest.setRefreshToken("ABC123");
        String content = (new ObjectMapper()).writeValueAsString(tokenRefreshRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/auth/refreshtoken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    /**
     * Method under test: {@link AuthController#registerUser(SignupRequest)}
     */
    @Test
    void testRegisterUser() throws Exception {
        when(userRepository.existsByEmail(Mockito.<String>any())).thenReturn(true);
        when(userRepository.existsByUsername(Mockito.<String>any())).thenReturn(true);

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("jane.doe@example.org");
        signupRequest.setIsAdmin(true);
        signupRequest.setName("Name");
        signupRequest.setPassword("iloveyou");
        signupRequest.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(signupRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"message\":\"Error: Username is already taken!\"}"));
    }

    /**
     * Method under test: {@link AuthController#registerUser(SignupRequest)}
     */
    @Test
    void testRegisterUser2() throws Exception {
        when(userRepository.existsByEmail(Mockito.<String>any())).thenReturn(true);
        when(userRepository.existsByUsername(Mockito.<String>any())).thenReturn(false);

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("jane.doe@example.org");
        signupRequest.setIsAdmin(true);
        signupRequest.setName("Name");
        signupRequest.setPassword("iloveyou");
        signupRequest.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(signupRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"message\":\"Error: Email is already in use!\"}"));
    }
}

