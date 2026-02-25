package com.example.demo.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	// 各クラスをDI
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	
	
	
	
	//---------------管理者機能------------------------
	
	/*
	 * ユーザーの新規登録
	 * */
	public void addUser(User user) {
		//パスワードをハッシュ化してUserフィールドにセット
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userMapper.insertUser(user);
	}
	
	public List<User> getAllUsers() {
		return userMapper.getAllUsers();
	}
	
	/*
	 * ユーザーIDからユーザー情報を取得して返す。
	 * */
	public User getUserById(String id) {
		return userMapper.getUserById(id);
	}
	
	
	/*
	 * ユーザー情報の更新
	 * */
	public void updateUser(User user) {
		// パスワードをハッシュ化してUserフィールドにセット
	    user.setPassword(passwordEncoder.encode(user.getPassword()));
	    userMapper.updateUser(user);
	}
	
	/*
	 * ユーザー情報の削除(論理削除)
	 * */
	public void deleteUser(String id) {
		// intに変換したidを使い削除
		userMapper.deleteUserById(id);
	}
}
