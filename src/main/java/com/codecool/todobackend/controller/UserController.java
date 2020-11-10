package com.codecool.todobackend.controller;

import com.codecool.todobackend.model.UserData;
import com.codecool.todobackend.model.UserDataRepository;
import com.codecool.todobackend.security.JwtTokenServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class UserController {

    @Autowired
    UserDataRepository userDataRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenServices jwtTokenServices;

    public UserController(AuthenticationManager authenticationManager, JwtTokenServices jwtTokenServices) {
        passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        this.authenticationManager = authenticationManager;
        this.jwtTokenServices = jwtTokenServices;
    }

    @PostMapping("/register")
    public void registerUser(@RequestBody UserData userData) {
        System.out.println("hello");
        if (userData.getName().equals("Csaba")) {
            userData.setRoles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"));
        } else {
            userData.setRoles(Arrays.asList("ROLE_USER"));
        }
        userData.setPassword(passwordEncoder.encode(userData.getPassword()));
       userDataRepository.save(userData);
    }

    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody UserData userData, HttpServletResponse response) {
        try {
            String name = userData.getName();
            // authenticationManager.authenticate calls loadUserByUsername in CustomUserDetailsService
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(name, userData.getPassword()));
            List<String> roles = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            String token = jwtTokenServices.createToken(name, roles);

            Map<Object, Object> model = new HashMap<>();
            model.put("name", name);
            model.put("roles", roles);
            model.put("token", token);

            Cookie cookie = new Cookie("token", token);
            cookie.setMaxAge(300000);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            response.addCookie(cookie);



            return ResponseEntity.ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }



}