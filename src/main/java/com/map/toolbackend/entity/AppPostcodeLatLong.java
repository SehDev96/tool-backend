package com.map.toolbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "app_postcode_lat_long")
public class AppPostcodeLatLong {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "postcode")
    private String postcode;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    public AppPostcodeLatLong() {
    }

    public AppPostcodeLatLong(String csvData) {
        if(csvData.contains(",")){
            String[] dataArray = csvData.split(",");
            if(dataArray.length==4){
                this.setPostcode(dataArray[1]);
                this.setLatitude(Double.parseDouble(dataArray[2]));
                this.setLongitude(Double.parseDouble(dataArray[3]));
            } else {
                // throw exception
            }
        }

    }
}
