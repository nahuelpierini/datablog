package com.app.datablog.service.Impl;

import com.app.datablog.security.jwt.JwtUtils;
import com.app.datablog.service.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthUserServiceImpl implements AuthUserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    public Map<String, Object> authenticateUser(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateAccessToken(email);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login successful");
        response.put("email", email);
        response.put("token", token);

        return response;
    }

}
