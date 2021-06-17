package com.sda_store.service;

import com.sda_store.model.User;

public interface UserService {
    User create(User user);
    User findByEmail(String email);
}
