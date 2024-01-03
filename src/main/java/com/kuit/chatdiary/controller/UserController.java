package com.kuit.chatdiary.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class UserController {
    @GetMapping("/user")
    public String user() {
        return "forward:/user.html";
    }
}

