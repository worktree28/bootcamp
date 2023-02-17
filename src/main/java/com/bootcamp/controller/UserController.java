package com.bootcamp.controller;

import com.bootcamp.model.Note;
import com.bootcamp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    @PostMapping("/push-log")
    public Map<String,String> pushLog(Principal principal){
        return userService.pushLog(principal.getName());
    }
    @GetMapping("/shared")
    public List<Note> getSharedNotes(Principal principal) {
        return userService.getSharedNotes(principal.getName());
    }
//    @Autowired
//    private UserService userService;
//    @GetMapping("/users/{email}")
//    public User getUser(@PathVariable String email) {
//        return userService.getUser(email);
//    }
//    @PostMapping("/users")
//    public void addUser(@RequestBody User user) {
//        userService.addUser(user);
//    }
////    @PutMapping("/users/{email}")
////    public void updateUser(@RequestBody User user, @PathVariable String email) {
////        userService.updateUser(user,email);
////    }
//    @DeleteMapping("/users/{email}")
//    public void deleteUser(@PathVariable String email) {
//        userService.deleteUser(email);
//    }
}
