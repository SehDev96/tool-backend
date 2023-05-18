package com.map.toolbackend.repository;

import com.map.toolbackend.entity.AppPostcodeLatLong;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppPostcodeLatLongRepository extends JpaRepository<AppPostcodeLatLong,Integer>{
    Optional<AppPostcodeLatLong> findByPostcode(String postcode);

    void deleteByPostcode(String postcode);
}
