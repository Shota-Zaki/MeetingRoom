package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.form.LoginForm;

@Controller
public class LoginController {
	@GetMapping("/login")
	public String moveLogin() {
		return "login";
	}
	
	@PostMapping("/login")
	public String login(@ModelAttribute LoginForm login) {
		
		
		if() {
			return "menu";
		}else {
			return "/";
		}
	} 
}
