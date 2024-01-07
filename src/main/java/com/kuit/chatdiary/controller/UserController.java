package com.kuit.chatdiary.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class UserController {

    @GetMapping("/testChat")
    public String chat() {
        return "redirect:/testChat.html";
    }
    @GetMapping("/user")
    public String user() {
        return "redirect:/user.html";
    }
}
