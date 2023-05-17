package com.map.toolbackend.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

public class RequestInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getRequestURI().equalsIgnoreCase("/app/postcode/distance")){
            System.out.println("===========================REQUEST BEGIN===========================");
            System.out.println("URI      : " + request.getRequestURI());
            System.out.println("Postcode1: " + request.getParameter("postcode1"));
            System.out.println("Postcode2: " + request.getParameter("postcode2"));
            System.out.println("Timestamp: "+ new Timestamp(System.currentTimeMillis()));
            System.out.println("==========================REQUEST END==============================");
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
