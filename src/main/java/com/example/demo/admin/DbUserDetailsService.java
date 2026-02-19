package com.example.demo.admin;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
//Spring SecurityのUserクラスを継承
public class DbUserDetailsService implements UserDetailsService {
	 private final UserMapper userMapper;

	    @Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    	
	    	// username = ログイン画面で入力されたID（String）
	        User user = userMapper.getUserById(username);
	        
	        // ユーザー情報を確認。Nullなら弾く
	        if (user == null) {
	            throw new UsernameNotFoundException("ユーザーが存在しません");
	        }

	        // 論理削除してるならここで弾く
	         if (user.getDeleteflag() == 1) {
	             throw new UsernameNotFoundException("ユーザーが存在しません");
	        }

	        String role = (user.getAdminflag() == 1) ? "ROLE_ADMIN" : "ROLE_USER";
	       
	        //UserをSpring Security用の形に整えて返す
	               // DBのUser → Security用UserDetailsへ変換
	        return org.springframework.security.core.userdetails.User
	        		// Spring SecurityでログインIDとして扱う値を設定
	                .withUsername(user.getId())
	                // DBに保存されているハッシュ済みパスワードを設定
	                .password(user.getPassword())
	                //このユーザーが持つ権限を確認
	                .authorities(List.of(new SimpleGrantedAuthority(role)))
	                // Userオブジェクトを作成
	                .build();
	    }
}
