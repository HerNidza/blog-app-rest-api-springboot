package com.nikola.blog.service.impl;

import com.nikola.blog.entity.Role;
import com.nikola.blog.entity.User;
import com.nikola.blog.exception.BlogAPIException;
import com.nikola.blog.payload.LoginDto;
import com.nikola.blog.payload.RegisterDto;
import com.nikola.blog.repository.RoleRepository;
import com.nikola.blog.repository.UserRepository;
import com.nikola.blog.security.JwtTokenProvider;
import com.nikola.blog.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    public AuthServiceImpl(AuthenticationManager theAuthenticationManager,
                           UserRepository theUserRepository,
                           RoleRepository theRoleRepository,
                           PasswordEncoder thePasswordEncoder,
                           JwtTokenProvider theTokenProvider) {
        authenticationManager = theAuthenticationManager;
        userRepository = theUserRepository;
        roleRepository = theRoleRepository;
        passwordEncoder = thePasswordEncoder;
        jwtTokenProvider = theTokenProvider;
    }

    @Override
    public String login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        return token;
    }

    @Override
    public String register(RegisterDto registerDto) {
        // Add check for username exists in database
        if (userRepository.existsByUsername(registerDto.getUsername())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Username is already taken");
        }

        // Add check for email exists in database
        if (userRepository.existsByEmail(registerDto.getEmail())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Email is already taken");
        }
        User user = new User();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);

        return "User registered successfully!";
    }
}
