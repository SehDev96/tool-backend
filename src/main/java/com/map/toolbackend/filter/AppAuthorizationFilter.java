package com.map.toolbackend.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.map.toolbackend.JwtUtils;
import com.map.toolbackend.model.ErrorResponseModel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class AppAuthorizationFilter extends OncePerRequestFilter {
    private JwtUtils jwtUtils = new JwtUtils();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if(request.getServletPath().equals("/app/authenticate")){
            filterChain.doFilter(request,response);
        } else {
            String authorizationHeader = request.getHeader("Authorization");

            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
                try{
                    String token = authorizationHeader.substring("Bearer ".length());
                    DecodedJWT decodedJWT = jwtUtils.decodeJwt(token);
                    String username = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);

                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

                    for(String role:roles){
                        authorities.add(new SimpleGrantedAuthority(role));
                    }

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(new User(username,username,authorities),null,authorities);

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    filterChain.doFilter(request,response);
                } catch (TokenExpiredException jwtException){
                    System.out.println("Error loggin in: " + jwtException.getMessage());
                    response.setHeader("error",jwtException.getMessage());
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType("application/json");


                    new ObjectMapper().writeValue(response.getOutputStream(), new ErrorResponseModel(
                            HttpStatus.UNAUTHORIZED.value(),
                            "Jwt Token has expired.",
                            authorizationHeader.substring("Bearer ".length())
                    ));


                } catch (Exception exception){
                    response.setHeader("error",exception.getMessage());
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType("application/json");

                    System.out.println(Arrays.toString(exception.getStackTrace()));

                    new ObjectMapper().writeValue(response.getOutputStream(), new ErrorResponseModel(
                            HttpStatus.UNAUTHORIZED.value(),
                            "Error Authentication User",
                            authorizationHeader.substring("Bearer ".length())
                    ));
                }
            } else {
                filterChain.doFilter(request,response);
            }
        }
    }
}
