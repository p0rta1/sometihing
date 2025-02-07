package com.example.ex7.config;

import com.example.ex7.security.handler.CustomAuthenticationFailureHandler;
import com.example.ex7.security.handler.CustomLoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
  private static final String[] AUTH_WHITELIST = {
      "/", "/login" // "/" <- 이것만 했을때도 index로 간다.
  };

  // spring security의 세션방식의 기반으로 대부분의 설정 가능
  @Bean
  protected SecurityFilterChain config(HttpSecurity httpSecurity) throws  Exception {
    httpSecurity.authorizeHttpRequests(new Customizer<AuthorizeHttpRequestsConfigurer<org.springframework.security.config.annotation.web.builders.HttpSecurity>.AuthorizationManagerRequestMatcherRegistry>() {
      @Override
      public void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth.requestMatchers(AUTH_WHITELIST).permitAll()
            .requestMatchers("/sample/all/**").permitAll();
      }
    });
  // config() 메서드를 작성하는 순간 자동으로 생성되던 login 페이지도 내용은 없어도 선언을 해줘야 동작함
    httpSecurity.formLogin(new Customizer<FormLoginConfigurer<HttpSecurity>>() {
      @Override
      public void customize(FormLoginConfigurer<HttpSecurity> httpSecurityFormLoginConfigurer) {
        // 내용은 없어도 기본적으로 /login 페이지로 이동 가능

        // 커스텀 로그인 페이지로 이동할 때
        // httpSecurityFormLoginConfigurer.loginPage("/sample/login");

        // 로그인을 성공했을 때 이동할 페이지 지정
        // httpSecurityFormLoginConfigurer.loginProcessingUrl("/sample/main");

        // 로그인을 한 후 성공과 실패 후지정 페이지를 고정하려고 할 때
        // httpSecurityFormLoginConfigurer.defaultSuccessUrl("/");
        // httpSecurityFormLoginConfigurer.failureForwardUrl("/");

        // 로그인 성공또는 실패할 때 상황별로 페이지를 정의하려고 할 때
        // httpSecurityFormLoginConfigurer.successHandler(getAuthenticationSuccessHandler());
        // httpSecurityFormLoginConfigurer.failureHandler(getAuthenticationFailureHandler());

      }
    });
    return httpSecurity.build();
  }

  @Bean
  public AuthenticationSuccessHandler getAuthenticationSuccessHandler() {
    return new CustomLoginSuccessHandler();
  }

  @Bean
  public AuthenticationFailureHandler getAuthenticationFailureHandler() {
    return new CustomAuthenticationFailureHandler();
  }
}
