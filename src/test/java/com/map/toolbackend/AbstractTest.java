package com.map.toolbackend;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.map.toolbackend.constants.Role;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@WebAppConfiguration
public abstract class AbstractTest {

    protected MockMvc mockMvc;

    protected String USERNAME = "testusername";
    protected String PASSWORD = "testpassword";

    private String accessToken = null;

    protected String getAccessToken() {
        return accessToken;
    }

    protected void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    // Attribute name very important
    protected Filter springSecurityFilterChain;

    protected void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }

    protected Map<String,String> getUserMap(){
        Map<String,String> userMap = new HashMap<>();
        userMap.put("username",USERNAME);
        userMap.put("password",PASSWORD);
        return  userMap;
    }

    protected Map<String,String> getUserMapWithRole(){
        Map<String,String> userMap = new HashMap<>();
        userMap.put("username",USERNAME);
        userMap.put("password",PASSWORD);
        userMap.put("role", Role.USER.name());
        return  userMap;
    }
}
