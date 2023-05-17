package com.map.toolbackend.controller;

import com.map.toolbackend.entity.AppUser;
import com.map.toolbackend.model.ErrorResponseModel;
import com.map.toolbackend.model.ResponseModel;
import com.map.toolbackend.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/app")
@RestController
public class AuthController {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AppUser appUser){
        if(appUser.getPassword().isBlank() || appUser.getUsername().isBlank() || appUser.getRole().isBlank()){
            return new ResponseEntity<>(new ErrorResponseModel(
                    HttpStatus.BAD_REQUEST.value(),
                    "Bad request",
                    "Incomplete details"
            ),HttpStatus.BAD_REQUEST);
        }

        if(appUserService.getUserByUsername(appUser.getUsername()) != null){
            return new ResponseEntity<>(new ErrorResponseModel(
                    HttpStatus.CONFLICT.value(),
                    "Bad request",
                    "Username already exists!"
            ),HttpStatus.CONFLICT);
        }


        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUser = appUserService.insertUser(appUser);

        return new ResponseEntity<>(new ResponseModel(
                HttpStatus.OK.value(),
                "Successfully created user",
                appUser
        ),HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getTest(){
        System.out.println("TESTING");
        return new ResponseEntity<>(new ResponseModel(
                HttpStatus.OK.value(),
                "Successfully created user",
                "SUCCESS"
        ),HttpStatus.OK);
    }
}
