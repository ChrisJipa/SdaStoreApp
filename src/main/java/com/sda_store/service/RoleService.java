package com.sda_store.service;

import com.sda_store.model.Role;

import java.util.*;

public interface RoleService {
    Role create(Role role);
    Role findByName(String name);
    List<Role> findAll();
}
