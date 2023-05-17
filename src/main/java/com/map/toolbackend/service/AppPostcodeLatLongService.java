package com.map.toolbackend.service;

import com.map.toolbackend.entity.AppPostcodeLatLong;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AppPostcodeLatLongService {

    AppPostcodeLatLong getPostcodeWithLatLongBytPostcode(String postcode);

    AppPostcodeLatLong insertOrUpdatePostcodeLatLong(AppPostcodeLatLong appPostcodeLatLong);

    int insertPostcodeWithLatLongList(List<AppPostcodeLatLong> appPostcodeLatLongList);

    int insertPostcodeWithLatLongList(MultipartFile file) throws IOException;
}
