package com.map.toolbackend.service;

import com.map.toolbackend.entity.AppUser;

public interface AppUserService {

    AppUser insertUser(AppUser appUser);

    AppUser getUserByUsername(String username);
}
