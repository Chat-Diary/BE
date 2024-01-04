package com.kuit.chatdiary.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class UserController {

    @GetMapping("/chat")
    public String chat() {
        return "redirect:/chat.html";
    }
    @GetMapping("/user")
    public String user() {
        return "user";
    }
}
