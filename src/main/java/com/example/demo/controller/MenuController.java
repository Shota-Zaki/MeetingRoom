package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    @GetMapping("/topPage")
    public String home() {
        return "login/menu";
    }

    @GetMapping("/admin/top")
    public String admin() {
        return "admin/admin";
    }
}
