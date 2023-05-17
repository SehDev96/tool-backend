package com.map.toolbackend.controller;

import com.map.toolbackend.entity.AppPostcodeLatLong;
import com.map.toolbackend.model.ErrorResponseModel;
import com.map.toolbackend.model.ResponseModel;
import com.map.toolbackend.service.AppPostcodeLatLongService;
import com.map.toolbackend.utils.Calculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/app/postcode")
@RestController
public class PostcodeController {

    @Autowired
    private AppPostcodeLatLongService appPostcodeLatLongService;

    @PostMapping("/add")
    public ResponseEntity<?> addPostcodeLatLong(@RequestBody AppPostcodeLatLong appPostcodeLatLong) {
        if (appPostcodeLatLong.getPostcode().isBlank()) {
            return new ResponseEntity<>(new ErrorResponseModel(
                    HttpStatus.BAD_REQUEST.value(),
                    "Bad Request",
                    "Postcode field is empty"
            ), HttpStatus.BAD_REQUEST);
        }

        if (appPostcodeLatLongService.getPostcodeWithLatLongBytPostcode(appPostcodeLatLong.getPostcode()) != null) {
            return new ResponseEntity<>(new ErrorResponseModel(
                    HttpStatus.CONFLICT.value(),
                    "Conflict",
                    "Postcode: " + appPostcodeLatLong.getPostcode() + " already exists"
            ), HttpStatus.CONFLICT);
        }

        appPostcodeLatLong = appPostcodeLatLongService.insertOrUpdatePostcodeLatLong(appPostcodeLatLong);

        return new ResponseEntity<>(new ResponseModel(
                HttpStatus.OK.value(),
                "Successfully added postcode data",
                appPostcodeLatLong
        ), HttpStatus.OK);
    }

    @PostMapping("/uploadcsv")
    public ResponseEntity<?> uploadCsvFile(@RequestParam(value = "file1", required = false) MultipartFile file1,
                                           @RequestParam(value = "file2", required = false) MultipartFile file2
    ) throws IOException {

        List<MultipartFile> fileList = new ArrayList<>();
        if (file1 != null && !file1.isEmpty()) fileList.add(file1);
        if (file2 != null && !file2.isEmpty()) fileList.add(file2);

        int insertedPostcodeSize = 0;

        for (MultipartFile file : fileList) {
            String fileName = file.getOriginalFilename();
            assert fileName != null;
            if (fileName.endsWith(".csv")) {
                int insertedSize = appPostcodeLatLongService.insertPostcodeWithLatLongList(file);
                insertedPostcodeSize = insertedPostcodeSize + insertedSize;

            } else {
                // File format not supported. Please upload a csv file
                return new ResponseEntity<>(new ErrorResponseModel(
                        HttpStatus.BAD_REQUEST.value(),
                        "File format not supported. Please upload a csv file.",
                        "Please upload a csv file"
                ), HttpStatus.BAD_REQUEST);
            }
        }

        if (fileList.size() < 1 || insertedPostcodeSize == 0) {
            return new ResponseEntity<>(new ErrorResponseModel(
                    HttpStatus.BAD_REQUEST.value(),
                    "Data not inserted",
                    "Please check the data in csv file"
            ), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ResponseModel(
                HttpStatus.OK.value(),
                "Successfully uploaded postcodes",
                insertedPostcodeSize
        ), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updatePostcodeData(
            @RequestParam(value = "postcode") String postcode,
            @RequestBody Map<String, Double> locationMap
    ) {
        AppPostcodeLatLong appPostcodeLatLong = appPostcodeLatLongService.getPostcodeWithLatLongBytPostcode(postcode);

        if (appPostcodeLatLong == null) {
            return new ResponseEntity<>(new ErrorResponseModel(
                    HttpStatus.NOT_FOUND.value(),
                    "Data not found",
                    "Postcode: " + postcode + " not found"
            ), HttpStatus.NOT_FOUND);
        }

        if (locationMap.containsKey("latitude")) appPostcodeLatLong.setLatitude(locationMap.get("latitude"));
        if (locationMap.containsKey("longitude")) appPostcodeLatLong.setLongitude(locationMap.get("longitude"));

        appPostcodeLatLong = appPostcodeLatLongService.insertOrUpdatePostcodeLatLong(appPostcodeLatLong);

        return new ResponseEntity<>(new ResponseModel(
                HttpStatus.OK.value(),
                "Successfully updated postcode data",
                appPostcodeLatLong
        ), HttpStatus.OK);
    }


    @GetMapping("/distance")
    public ResponseEntity<?> getDistance(
            @RequestParam(value = "postcode1") String postcode1,
            @RequestParam(value = "postcode2") String postcode2
    ) {

        AppPostcodeLatLong appPostcodeLatLong1 = appPostcodeLatLongService.getPostcodeWithLatLongBytPostcode(postcode1);
        AppPostcodeLatLong appPostcodeLatLong2 = appPostcodeLatLongService.getPostcodeWithLatLongBytPostcode(postcode2);

        if(appPostcodeLatLong1 == null){
            return new ResponseEntity<>(new ErrorResponseModel(
                    HttpStatus.NOT_FOUND.value(),
                    "Data not found",
                    "Postcode: "+postcode1+" not found in database"
            ),HttpStatus.NOT_FOUND);
        }

        if(appPostcodeLatLong2 == null){
            return new ResponseEntity<>(new ErrorResponseModel(
                    HttpStatus.NOT_FOUND.value(),
                    "Data not found",
                    "Postcode: "+postcode2+" not found in database"
            ),HttpStatus.NOT_FOUND);
        }

        if(appPostcodeLatLong1.getLatitude() == null || appPostcodeLatLong1.getLongitude() == null
        || appPostcodeLatLong2.getLatitude() == null || appPostcodeLatLong2.getLongitude() == null) {
            return new ResponseEntity<>(new ErrorResponseModel(
                    HttpStatus.UNPROCESSABLE_ENTITY.value(),
                    "Data error",
                    "Latitude or Longitude data is missing for the given postcode"
            ),HttpStatus.UNPROCESSABLE_ENTITY);
        }

        double result = Calculator.calculateDistance(appPostcodeLatLong1.getLatitude(), appPostcodeLatLong1.getLongitude(),
                appPostcodeLatLong2.getLatitude(), appPostcodeLatLong2.getLongitude()
        );

        Map<String,Object> payloadMap = new HashMap<>();
        payloadMap.put("location1",appPostcodeLatLong1);
        payloadMap.put("location2",appPostcodeLatLong2);
        payloadMap.put("distance",result);
        payloadMap.put("unit","km");

        return new ResponseEntity<>(new ResponseModel(
                HttpStatus.OK.value(),
                "Calculation successful",
                payloadMap
        ),HttpStatus.OK);
    }
}
