package com.map.toolbackend.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.map.toolbackend.JwtUtils;
import com.map.toolbackend.model.ErrorResponseModel;
import com.map.toolbackend.model.LoginRequest;
import com.map.toolbackend.model.ResponseModel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AppAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils = new JwtUtils();

    public AppAuthenticationFilter(AuthenticationManager authenticationManager){
        this.setFilterProcessesUrl("/app/authenticate");
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        LoginRequest loginRequest;
        try{
            loginRequest = new ObjectMapper().readValue(request.getInputStream(),LoginRequest.class);
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();

        String accessToken = jwtUtils.generateAccessToken(user);

        Map<String,Object> tokens = new HashMap<>();
        tokens.put("access_token",accessToken);
        response.setContentType("application/json");

        new ObjectMapper().writeValue(response.getOutputStream(), new ResponseModel(
                HttpStatus.OK.value(),
                "User Authentication Successful!",
                tokens
        ));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        new ObjectMapper().writeValue(response.getOutputStream(), new ErrorResponseModel(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                "Authentication Failed!"
        ));
    }

}
