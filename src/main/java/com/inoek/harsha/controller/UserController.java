package com.inoek.harsha.controller;

import com.inoek.harsha.model.User;
import com.inoek.harsha.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/user")
    public User userName(@RequestParam(value="name", defaultValue="World") String name) throws Exception{
        User user = new User(name);
        userRepository.save(user);
        return user;
    }
}
