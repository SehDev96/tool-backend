package com.map.toolbackend;

import com.map.toolbackend.constants.Role;
import com.map.toolbackend.entity.AppUser;
import com.map.toolbackend.service.AppUserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthServiceTests {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @BeforeAll
    void registerUser(){
        AppUser appUser = new AppUser("testuser","testpassword",Role.USER.name());
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUserService.insertUser(appUser);
    }

    @Test
    void testDuplicatedUsername(){
        AppUser appUser = new AppUser("testuser","testpassword123",Role.USER.name());
        DataIntegrityViolationException thrown = Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            appUserService.insertUser(appUser);
        });

        Assertions.assertEquals("could not execute statement; SQL [n/a]; constraint [app_user_username_key]", thrown.getMessage());
    }

    @Test
    void testRegisteredUser(){
        AppUser appUser = appUserService.getUserByUsername("testuser");
        Assertions.assertEquals(appUser.getUsername(),"testuser");
    }

    @Test
    void testValidCredentials(){
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("testuser", "testpassword");
        Authentication authentication = authenticationManager.authenticate(token);
        Assertions.assertTrue(authentication.isAuthenticated());
    }

    @Test
    void testInValidCredentials(){
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("testuser", "testpassword123");

        BadCredentialsException thrown = Assertions.assertThrows(BadCredentialsException.class, () -> {
            authenticationManager.authenticate(token);
        });

        Assertions.assertEquals("Bad credentials", thrown.getMessage());
    }

    @AfterAll
    void clearData(){
        appUserService.removeUser("testuser");
    }

}
