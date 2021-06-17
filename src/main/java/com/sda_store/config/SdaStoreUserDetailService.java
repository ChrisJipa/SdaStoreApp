package com.sda_store.config;

import com.sda_store.exception.ResourceNotFoundInDatabase;
import com.sda_store.model.Role;
import com.sda_store.model.User;
import com.sda_store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class SdaStoreUserDetailService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public SdaStoreUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(s);
        if (user == null){
            throw new ResourceNotFoundInDatabase(String.format("User with email %s not found", s));
        }
        return mapUserToUserDetails(user);
    }

    public UserDetails mapUserToUserDetails(User user){
        List<GrantedAuthority> grantedAuthorityList = mapRoleToGrantedAuthority(user.getRole());
        UserDetails userDetails =  new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), grantedAuthorityList);
        return userDetails;
    }

    public List<GrantedAuthority> mapRoleToGrantedAuthority(Set<Role> roles){
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        for (Role role: roles){
            grantedAuthorityList.add(new SimpleGrantedAuthority(role.getName()));
        }
        return grantedAuthorityList;
    }
}
