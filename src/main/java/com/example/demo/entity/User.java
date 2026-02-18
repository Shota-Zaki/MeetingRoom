package com.example.demo.entity;

import lombok.Data;

@Data
public class User {
	
	//フィールド
	/**
	 * 利用者ID
	 */
	private String id;
		
	/**
	 * パスワード
	 */
	private String password;
	
	/**
	 * 氏名
	 */
	private String name;
	
	/**
	 * 住所
	 */
	private String address;
	
	/**
	 * 住所
	 */
	private boolean admin;
}
