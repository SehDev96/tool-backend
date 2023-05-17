package com.map.toolbackend.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import java.sql.Timestamp;

public class RequestInterceptor implements HandlerInterceptor {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getRequestURI().equalsIgnoreCase("/app/postcode/distance")){
            log.info("===========================REQUEST BEGIN===========================");
            log.info("URI      : " + request.getRequestURI());
            log.info("Postcode1: " + request.getParameter("postcode1"));
            log.info("Postcode2: " + request.getParameter("postcode2"));
            log.info("Timestamp: "+ new Timestamp(System.currentTimeMillis()));
            log.info("==========================REQUEST END==============================");
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
