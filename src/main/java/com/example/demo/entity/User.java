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
	 * 管理者権限(0=通常,1=管理者)
	 */
	private int adminflag;
	
	/**
	 * デリートフラグ(論路削除 0=通常,1=削除)
	 */
	private int deleteflag;
}
