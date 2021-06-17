package com.sda_store;

import com.sda_store.model.Role;
import com.sda_store.model.RoleEnum;
import com.sda_store.repository.CategoryRepository;
import com.sda_store.service.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class SdaStoreApplication implements CommandLineRunner {

    private RoleService roleService;
    private CategoryRepository categoryRepository;

    public SdaStoreApplication(RoleService roleService, CategoryRepository categoryRepository) {
        this.roleService = roleService;
        this.categoryRepository = categoryRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(SdaStoreApplication.class, args);
    }

    @Override
    public void run(String... args)  {
        List<RoleEnum> rolEnumList = Arrays.asList(RoleEnum.values());
        for(RoleEnum roleEnum: rolEnumList){
            Role role = new Role(roleEnum.name());
            roleService.create(role);
        }
    }
}
