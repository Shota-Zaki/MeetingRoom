package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authz -> authz // HTTPリクエストに対するセキュリティ設定
                .requestMatchers("/css/**", "/js/**", "/img/**").permitAll() // 静的なものは許可
                .requestMatchers("/", "/login", "/toInsert", "/insert").permitAll() // /login へのアクセスは誰でもOK
                .anyRequest().authenticated() // その他はログイン必須
        ).formLogin(login -> login // フォームベースのログイン設定
                .loginPage("/login") // ログインページのURLを指定。GET /login をログイン画面として使う
                .loginProcessingUrl("/login") //POST /login をSpring Securityが処理する
                .failureUrl("/login?error=true") // ログイン失敗で/login?error=trueへリダイレクト
                .defaultSuccessUrl("/topPage", true) // ログイン成功したらトップページへ移動
                
        ).logout(logout -> logout
                .logoutUrl("/logout") //POSTまたはGET /logout でログアウト。
                .logoutSuccessUrl("/") //ログアウト成功でトップページへ
                .invalidateHttpSession(true) //ログアウト時にセッションを完全破棄
        );

        return http.build();
    }

    @Bean
    // パスワードをハッシュ化
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
