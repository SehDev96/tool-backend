package com.map.toolbackend.service;

import com.map.toolbackend.entity.AppUser;
import com.map.toolbackend.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AppUserServiceImpl implements AppUserService{

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public AppUser insertUser(AppUser appUser) {
        return appUserRepository.save(appUser);
    }

    @Override
    public AppUser getUserByUsername(String username) {
        return appUserRepository.findUserByUsername(username).orElse(null);
    }

    @Override
    public void removeUser(String username) {
        appUserRepository.deleteByUsername(username);
    }
}
