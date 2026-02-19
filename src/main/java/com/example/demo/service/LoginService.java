package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService {
	private final UserMapper userMapper;
	
	public boolean loginCheck(String id, String password) {
		User user= userMapper.getUserById(id);
		if(user != null) {
			return true;
		}else {
			return false;
		}
	}
}
