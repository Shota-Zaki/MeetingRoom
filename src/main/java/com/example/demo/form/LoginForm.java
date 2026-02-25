package com.example.demo.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginForm {
	
	/*
	 * ログインID
	 * */
	@NotBlank(message="ログインIDを入力してください。")
	private String username;
	
	/*
	 * パスワード
	 * */
	@NotBlank(message="パスワードを入力してください。")
	private String password;
}
