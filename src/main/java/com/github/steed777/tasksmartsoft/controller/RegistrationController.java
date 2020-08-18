package com.github.steed777.tasksmartsoft.controller;


import com.github.steed777.tasksmartsoft.model.Role;
import com.github.steed777.tasksmartsoft.model.User;
import com.github.steed777.tasksmartsoft.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
    private UserRepository userRepository;

@Autowired
    public RegistrationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

   @GetMapping("/login")
  public String loginPage() {
      return "login";
    }
    @GetMapping("/registration")
    public String registration(){
    return "registration";
    }
    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model) {
        User userFromDb = userRepository.findByUsername(user.getUsername());

        if (userFromDb != null) {
            model.put("message", "Такой пользователь уже существует!");
            return "registration";
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);
        return "redirect:/login";
    }

}
