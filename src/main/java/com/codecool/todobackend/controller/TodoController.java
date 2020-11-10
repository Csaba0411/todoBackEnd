package com.codecool.todobackend.controller;


import com.codecool.todobackend.model.UserData;
import com.codecool.todobackend.model.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
@CrossOrigin("*")
public class TodoController {

    @Autowired
    UserDataRepository userDataRepository;

    @GetMapping("/allUser")
    public List<UserData> getUsers() {
        return userDataRepository.findAll();
    }


}
