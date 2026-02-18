package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminUserController {
	private final UserMapper userMapper;
	
	@GetMapping("/admin/user")
	public String index(Model model) {
		model.addAttribute("users",userMapper.getAllUsers());
		return "user/list";
	}
	
	@GetMapping("/admin/detail/{id}")
	public String detail(@PathVariable int id,Model model) {
		model.addAttribute("user",userMapper.getUserById(id));
		return "user/detail";
	}
	
	@GetMapping("/admin/create")
	public String create(@ModelAttribute User user) {
		return "user/create";
	}
	
	@PostMapping("/admin/create")
	public String create(@ModelAttribute User user,Model model) {
		userMapper.insertUser(user);
		return "user/list";
	}
	
	@GetMapping("/admin/update/{id}")
	public String update(@PathVariable int id,Model model) {
		model.addAttribute("user",userMapper.getUserById(id));
		return "user/edit";
	}
	
	@PostMapping("/admin/update")
	public String update(@ModelAttribute User user,Model model) {
		userMapper.updateUser(user);
		model.addAttribute("users",userMapper.getAllUsers());
		return "user/list";
	}
	
	@GetMapping("/admin/delete/{id}")
	public String delete(@PathVariable int id,Model model) {
		userMapper.deleteUserById(id);
		model.addAttribute("users",userMapper.getAllUsers());
		return "user/list";
	}
}
