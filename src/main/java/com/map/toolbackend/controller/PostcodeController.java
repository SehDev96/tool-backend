package com.map.toolbackend.controller;

import com.map.toolbackend.entity.AppPostcodeLatLong;
import com.map.toolbackend.model.ErrorResponseModel;
import com.map.toolbackend.model.ResponseModel;
import com.map.toolbackend.service.AppPostcodeLatLongService;
import com.map.toolbackend.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/app/postcode")
@RestController
public class PostcodeController {

    @Autowired
    private AppPostcodeLatLongService appPostcodeLatLongService;

    @PostMapping("/add")
    public ResponseEntity<?> addPostcodeLatLong(@RequestBody AppPostcodeLatLong appPostcodeLatLong){
        if(appPostcodeLatLong.getPostcode().isBlank()){
            return new ResponseEntity<>(new ErrorResponseModel(
                    HttpStatus.BAD_REQUEST.value(),
                    "Bad Request",
                    "Postcode field is empty"
            ),HttpStatus.BAD_REQUEST);
        }

        if(appPostcodeLatLongService.getPostcodeWithLatLongBytPostcode(appPostcodeLatLong.getPostcode()) != null){
            return new ResponseEntity<>(new ErrorResponseModel(
                    HttpStatus.CONFLICT.value(),
                    "Conflict",
                    "Postcode: "+appPostcodeLatLong.getPostcode()+" already exists"
            ),HttpStatus.CONFLICT);
        }

        appPostcodeLatLong = appPostcodeLatLongService.insertPostcodeWithLatLong(appPostcodeLatLong);

        return new ResponseEntity<>(new ResponseModel(
                HttpStatus.OK.value(),
                "Successfully added postcode data",
                appPostcodeLatLong
        ),HttpStatus.OK);
    }

    @PostMapping("/uploadcsv")
    public ResponseEntity<?> uploadCsvFile(@RequestParam(value = "file1",required = false) MultipartFile file1,
                                           @RequestParam(value = "file2",required = false) MultipartFile file2
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

        if(fileList.size()<1 || insertedPostcodeSize == 0){
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
}
