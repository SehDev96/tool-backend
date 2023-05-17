package com.map.toolbackend.service;

import com.map.toolbackend.entity.AppPostcodeLatLong;
import com.map.toolbackend.repository.AppPostcodeLatLongRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppPostcodeLatLongServiceImpl implements AppPostcodeLatLongService {


    @Autowired
    private AppPostcodeLatLongRepository appPostcodeLatLongRepository;

    @Override
    public AppPostcodeLatLong getPostcodeWithLatLongBytPostcode(String postcode) {
        return appPostcodeLatLongRepository.findByPostcode(postcode).orElse(null);
    }

    @Override
    public AppPostcodeLatLong insertOrUpdatePostcodeLatLong(AppPostcodeLatLong appPostcodeLatLong) {
        return appPostcodeLatLongRepository.save(appPostcodeLatLong);
    }

    @Override
    public int insertPostcodeWithLatLongList(List<AppPostcodeLatLong> appPostcodeLatLongList) {
        // We save data in a batch of 100
        final int batchSize = 1000;
        int insertedDataSize = 0;
        for (int i = 0; i < appPostcodeLatLongList.size(); i += batchSize) {
            List<AppPostcodeLatLong> batchList = appPostcodeLatLongList.subList(i, Math.min(i + batchSize, appPostcodeLatLongList.size()));
            batchList = appPostcodeLatLongRepository.saveAll(batchList);
            insertedDataSize = insertedDataSize + batchList.size();
        }
        return insertedDataSize;
    }

    @Override
    public int insertPostcodeWithLatLongList(MultipartFile file) throws IOException {
        // execution time for 499999 data is about 15.5 mins
        long startTime = System.currentTimeMillis();
        final int BATCH_SIZE = 1000;
        List<AppPostcodeLatLong> appPostcodeLatLongList = new ArrayList<>();
        int processedData = 0;
        try (InputStream inputStream = file.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String strLine;
            boolean isFirstLine = true;
            while ((strLine = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                AppPostcodeLatLong appPostcodeLatLong = new AppPostcodeLatLong(strLine);
                appPostcodeLatLongList.add(appPostcodeLatLong);

                if(appPostcodeLatLongList.size() == BATCH_SIZE){
                    processLine(appPostcodeLatLongList);
                    processedData = processedData + appPostcodeLatLongList.size();
                    appPostcodeLatLongList = new ArrayList<>();
                    System.out.println("Total Processed Data: "+processedData);
                }
            }

            if(appPostcodeLatLongList.size()>0){
                processLine(appPostcodeLatLongList);
                processedData = processedData + appPostcodeLatLongList.size();
                System.out.println("Total Processed Data: "+processedData);

            }
        }
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("Execution time: " + executionTime + " milliseconds");
        return processedData;
    }

    private void processLine(List<AppPostcodeLatLong> appPostcodeLatLongList){
        appPostcodeLatLongRepository.saveAll(appPostcodeLatLongList);
    }
}
