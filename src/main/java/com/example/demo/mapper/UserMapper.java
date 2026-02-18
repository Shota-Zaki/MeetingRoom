package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.User;

@Mapper
public interface UserMapper {
	List<User> getAllUsers();
	
	User getUserById(int id);
	
	void insertUser(User user);
	
	void updateUser(User user);
	
	void deleteUserById(int id);
}
