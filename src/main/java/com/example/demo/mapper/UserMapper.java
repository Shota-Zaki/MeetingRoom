package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.User;

@Mapper
public interface UserMapper {
	List<User> getAllUsers();
	
	User getUserById(@Param("id") String id);
	
	void insertUser(User user);
	
	void updateUser(User user);
	
	void deleteUserById(String id);
}
