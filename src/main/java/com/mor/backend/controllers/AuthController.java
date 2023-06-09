package com.mor.backend.controllers;

import com.github.seratch.jslack.api.methods.SlackApiException;
import com.mor.backend.common.AuthProvider;
import com.mor.backend.common.ERole;
import com.mor.backend.entity.RefreshToken;
import com.mor.backend.entity.Role;
import com.mor.backend.entity.User;
import com.mor.backend.exeptions.TokenRefreshException;
import com.mor.backend.payload.request.LoginRequest;
import com.mor.backend.payload.request.SignupRequest;
import com.mor.backend.payload.request.TokenRefreshRequest;
import com.mor.backend.payload.response.JwtResponse;
import com.mor.backend.payload.response.MessageResponse;
import com.mor.backend.payload.response.ObjectResponse;
import com.mor.backend.payload.response.TokenRefreshResponse;
import com.mor.backend.repositories.RoleRepository;
import com.mor.backend.repositories.UserRepository;
import com.mor.backend.services.CartService;
import com.mor.backend.services.EmailService;
import com.mor.backend.services.SlackService;
import com.mor.backend.services.impl.RefreshTokenService;
import com.mor.backend.services.impl.UserDetailsImpl;
import com.mor.backend.util.jwt.Jwt;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final String channelId ="C059WAL6PT4";
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;

    private final Jwt jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final CartService cartService;
    private final EmailService emailService;
    private final SlackService slackService;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getUsername(), userDetails.getEmail(), roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws SlackApiException, IOException {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }
        // Create new user's account
        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(), signUpRequest.getName(), encoder.encode(signUpRequest.getPassword()), signUpRequest.getIsAdmin(), AuthProvider.local);

        Set<Role> roles = new HashSet<>();

        Role userRole;
        if (signUpRequest.getIsAdmin()) {
            userRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        } else {
            userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        }
        roles.add(userRole);
        user.setRoles(roles);
        userRepository.save(user);
        cartService.createCartWithCurrentUser(user);
        emailService.sendEmail(user.getEmail(),"test mail","Welcome to mail");
        slackService.sendMessage(channelId,":pepe-saber: User : " + user.getName() + " registered :pepe-saber:");
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<TokenRefreshResponse> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken).map(refreshTokenService::verifyExpiration).map(RefreshToken::getUser).map(user -> {
            String token = jwtUtils.generateTokenFromUsername(user.getUsername());
            return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
        }).orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
    }

    @GetMapping("/google")
    public ResponseEntity<ObjectResponse> googleLogin(@RequestParam("access_token") String token) {

        return new ResponseEntity<>(new ObjectResponse("200", "", token), HttpStatus.OK);
    }
}
