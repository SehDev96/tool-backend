package com.map.toolbackend.repository;

import com.map.toolbackend.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUser,UUID> {

    Optional<AppUser> findUserByUsername(String username);

    void deleteByUsername(String username);
}