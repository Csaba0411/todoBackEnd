package com.codecool.todobackend.security;

import com.codecool.todobackend.model.UserData;
import com.codecool.todobackend.model.UserDataRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private UserDataRepository userDataRepository;

    public CustomUserDetailsService(UserDataRepository users) {
        this.userDataRepository = users;
    }

    /**
     * Loads the user from the DB and converts it to Spring Security's internal User object.
     * Spring will call this code to retrieve a user upon login from the DB.
     */
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        UserData user = userDataRepository.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("Username: " + name + " not found"));

        return new User(user.getName(), user.getPassword(),
                user.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
    }
}