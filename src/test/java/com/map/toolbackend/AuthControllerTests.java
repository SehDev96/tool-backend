package com.map.toolbackend;

import com.map.toolbackend.constants.Role;
import com.map.toolbackend.service.AppUserService;
import jakarta.servlet.Filter;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthControllerTests extends AbstractTest{

    @Autowired
    private AppUserService appUserService;


    @Override
    @BeforeAll
    protected void setUp() {
//        super.setUp();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilter(springSecurityFilterChain).build();
    }


    @Test
    void testRegisterUser() throws Exception {
        Map<String,String> userMap = new HashMap<>();
        userMap.put("username",USERNAME);
        userMap.put("password",PASSWORD);
        userMap.put("role",Role.USER.name());
        String inputJson = mapToJson(userMap);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/app/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(HttpStatus.OK.value(), status);


        userMap.put("password","testuser");
        inputJson = mapToJson(userMap);
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/app/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(HttpStatus.CONFLICT.value(), status);
    }

    @Test
    void testUserLogin() throws Exception {
        Map<String,String> userMap = new HashMap<>();
        userMap.put("username",USERNAME);
        userMap.put("password",PASSWORD);
        String inputJson = mapToJson(userMap);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/app/authenticate")
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(HttpStatus.OK.value(), status);
        System.out.println(mvcResult.getResponse().getContentAsString());

        userMap.put("password","testpassword123");
        inputJson = mapToJson(userMap);
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/app/authenticate")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
        status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    }

    @AfterAll
    void cleanUp(){
        appUserService.removeUser(USERNAME);
    }


}
