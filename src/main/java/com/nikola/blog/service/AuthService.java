package com.nikola.blog.service;

import com.nikola.blog.payload.LoginDto;
import com.nikola.blog.payload.RegisterDto;

public interface AuthService {
    String login(LoginDto loginDto);
    String register(RegisterDto registerDto);
}
