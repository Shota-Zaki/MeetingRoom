package com.example.demo.service;

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
	
	//ユーザーの新規登録
	public void addUser(User user) {
		//パスワードをハッシュ化してUserフィールドにセット
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userMapper.insertUser(user);
	}
	
	//ユーザー情報の更新
	public void updateUser(User user) {
		// パスワードをハッシュ化してUserフィールドにセット
	    user.setPassword(passwordEncoder.encode(user.getPassword()));
	    userMapper.updateUser(user);
	}
	
	//ユーザー情報の削除(論理削除)
	public void deleteUser(User user) {
		// intに変換したidを使い削除
		userMapper.deleteUserById(user.getId());
	}
}
