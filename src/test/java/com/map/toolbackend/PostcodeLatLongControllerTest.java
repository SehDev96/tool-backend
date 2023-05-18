package com.map.toolbackend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.map.toolbackend.service.AppPostcodeLatLongService;
import com.map.toolbackend.service.AppUserService;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostcodeLatLongControllerTest extends AbstractTest{

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AppPostcodeLatLongService appPostcodeLatLongService;

    @Override
    @BeforeAll
    protected void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilter(springSecurityFilterChain).build();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/app/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(getUserMapWithRole()))).andReturn();
        int status = mvcResult.getResponse().getStatus();
        if(status == HttpStatus.OK.value()){
            mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/app/authenticate")
                    .content(mapToJson(getUserMap()))).andReturn();
            status = mvcResult.getResponse().getStatus();
            if(status == HttpStatus.OK.value()){
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString());

                JsonNode payloadNode = jsonNode.get("payload");
                JsonNode accessTokenNode = payloadNode.get("access_token");
                System.out.println("ACCES: "+accessTokenNode.asText());
                setAccessToken(accessTokenNode.asText());
            }
        }
    }

    @Test
    @Order(1)
    void insertPostCodeWithoutToken() throws Exception{
        Map<String,Object> postCodeMap = new HashMap<>();
        postCodeMap.put("postcode","FIRST");
        postCodeMap.put("latitude",57.777777);
        postCodeMap.put("longitude",-2.222222);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/app/postcode/add")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(postCodeMap))).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(),status);

    }



    @Test
    @Order(2)
    void insertPostCode() throws Exception {
        Map<String,Object> postCodeMap = new HashMap<>();
        postCodeMap.put("postcode","FIRST");
        postCodeMap.put("latitude",57.777777);
        postCodeMap.put("longitude",-2.222222);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/app/postcode/add")
                .header("Authorization", "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(postCodeMap))).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(HttpStatus.OK.value(),status);
    }

    @Test
    @Order(3)
    void insertDuplicatedPostCode() throws Exception {
        Map<String,Object> postCodeMap = new HashMap<>();
        postCodeMap.put("postcode","FIRST");
        postCodeMap.put("latitude",57.777737);
        postCodeMap.put("longitude",-2.2222452);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/app/postcode/add")
                .header("Authorization", "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(postCodeMap))).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(HttpStatus.CONFLICT.value(),status);
    }

    @Test
    @Order(4)
    void getDataByPostcode() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/app/postcode/get/FIRST")
                .header("Authorization", "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(HttpStatus.OK.value(),status);
    }

    @Test
    @Order(5)
    void getDataByPostcodeWhichDoesNotExists() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/app/postcode/get/SECOND")
                .header("Authorization", "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(),status);
    }

    @Test
    @Order(6)
    void updateNonExistentPostcode() throws Exception {
        Map<String,Double> requestBody = new HashMap<>();
        requestBody.put("latitude",0.000);
        requestBody.put("longitude",0.000);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/app/postcode/update")
                .header("Authorization", "Bearer " + getAccessToken())
                .param("postcode","SECOND")
                .content(mapToJson(requestBody))
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(),status);
    }

    @Test
    @Order(7)
    void updatePostcodeData() throws Exception {
        Map<String,Double> requestBody = new HashMap<>();
        requestBody.put("latitude",0.000);
        requestBody.put("longitude",0.000);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/app/postcode/update")
                .header("Authorization", "Bearer " + getAccessToken())
                .param("postcode","FIRST")
                .content(mapToJson(requestBody))
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(HttpStatus.OK.value(),status);
    }


    // setup for distance calculation
    void addData() throws Exception {
        Map<String,Object> postCodeMap = new HashMap<>();
        postCodeMap.put("postcode","THIRD");
        postCodeMap.put("latitude",50.7926290);
        postCodeMap.put("longitude",-1.1011410);

        mockMvc.perform(MockMvcRequestBuilders.post("/app/postcode/add")
                .header("Authorization", "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(postCodeMap))).andReturn();
    }


    @Test
    @Order(8)
    void distanceCalculatorTest() throws Exception {
        addData();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/app/postcode/distance")
                .header("Authorization", "Bearer " + getAccessToken())
                .param("postcode1","FIRST")
                .param("postcode2","THIRD")
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(HttpStatus.OK.value(),status);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString());

        double distance = jsonNode.get("payload").get("distance").asDouble();
        Assertions.assertEquals(5648.842407350263,distance);


        String location1Postcode = jsonNode.get("payload").get("location1").get("postcode").asText();
        Assertions.assertEquals("FIRST",location1Postcode);

        String location2Postcode = jsonNode.get("payload").get("location2").get("postcode").asText();
        Assertions.assertEquals("THIRD",location2Postcode);

    }


    @AfterAll
    void cleanUp(){
        appUserService.removeUser(USERNAME);
        appPostcodeLatLongService.removePostCodeLatLongByPostcode("FIRST");
    }

}
