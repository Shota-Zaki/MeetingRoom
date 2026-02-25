package com.example.demo.form;

import lombok.Data;

@Data
public class AdminUserForm {
	/*
	 * ログインID
	 * */
	String id;

	/*
	 * ユーザー名
	 * */
	String name;
	
	/*
	 * パスワード
	 * */
	String password;
	
	/*
	 * 住所
	 * */
	String address;
	
	/*
	 * 管理者権限フラグ
	 * */
	int adminflag;

}
