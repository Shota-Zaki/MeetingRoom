package com.example.demo.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.form.UserForm;
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
	public void addUser(UserForm form) {
		//パスワードをハッシュ化してUserフィールドにセット
		User user = new User();
		
		user.setId(form.getId());
		user.setName(form.getName());
		user.setPassword(passwordEncoder.encode(form.getPassword()));
		user.setAddress(form.getAddress());
		user.setAdminflag(form.getAdminflag());
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
	public void updateUser(UserForm form) {
		// パスワードをハッシュ化してUserフィールドにセット
		User user = new User();
		
		user.setId(form.getId());
		user.setName(form.getName());
		user.setPassword(passwordEncoder.encode(form.getPassword()));
		user.setAddress(form.getAddress());
		user.setAdminflag(form.getAdminflag());
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
