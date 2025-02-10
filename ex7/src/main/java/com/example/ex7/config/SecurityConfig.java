package com.example.ex7.config;

import com.example.ex7.security.handler.CustomAuthenticationFailureHandler;
import com.example.ex7.security.handler.CustomLoginSuccessHandler;
import com.example.ex7.security.handler.CustomLogoutSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
  private static final String[] AUTH_WHITELIST = {
      "/"
  };

  // spring security의 세션방식 기반으로 대부분의 설정 가능
  @Bean
  protected SecurityFilterChain config(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.authorizeHttpRequests(new Customizer<AuthorizeHttpRequestsConfigurer<org.springframework.security.config.annotation.web.builders.HttpSecurity>.AuthorizationManagerRequestMatcherRegistry>() {
      @Override
      public void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth.requestMatchers(AUTH_WHITELIST).permitAll()  //모두 수용 복수 지정때는 String배열로 적용
            .requestMatchers("/sample/all/**").permitAll()//모두 수용 단독 지정때는 주소와 "**" 덧붙임.
            .requestMatchers("/sample/admin").hasRole("ADMIN") //권한이 단수일 때
            .requestMatchers("/sample/manager").access(  //권한이 복수일 때
                new WebExpressionAuthorizationManager(
                    "hasRole('MANAGER') or hasRole('ADMIN') "
                )
            )
        ;
      }
    });

    // config() 메서드를 작성하는 순간 자동으로 생성되던 login페이지도 선언을해줘야 동작함.
    httpSecurity.formLogin(new Customizer<FormLoginConfigurer<HttpSecurity>>() {
      @Override
      public void customize(FormLoginConfigurer<HttpSecurity> httpSecurityFormLoginConfigurer) {
        // 내용은 없어도 기본적으로 /login 페이지로 이동 가능

        // 커스텀 로그인 페이지로 이동할 때
        //httpSecurityFormLoginConfigurer.loginPage("/sample/login");

        // 로그인을 성공했을 때 이동할 페이지 지정
        //httpSecurityFormLoginConfigurer.loginProcessingUrl("/sample/main");

        // 로그인 한 후 성공과 실패 후 지정 페이지를 고정하려고 할 때
        //httpSecurityFormLoginConfigurer.defaultSuccessUrl("/");
        //httpSecurityFormLoginConfigurer.failureForwardUrl("/")

        // 로그인 성공 또는 실패할 때 상황별 이동을 정의하려고 할 때
        //httpSecurityFormLoginConfigurer.successHandler(getAuthenticationSuccessHandler());
        //httpSecurityFormLoginConfigurer.failureHandler(getAuthenticationFailureHandler());

      }
    });

    httpSecurity.logout(new Customizer<LogoutConfigurer<HttpSecurity>>() {
      @Override
      public void customize(LogoutConfigurer<HttpSecurity> httpSecurityLogoutConfigurer) {
        httpSecurityLogoutConfigurer
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .logoutSuccessHandler(getCustomLogoutSuccessHandler())
            .invalidateHttpSession(true)
        ;
      }
    });

    return httpSecurity.build();  //SecurityFilterChain 타입의 프록시 객체가 리턴
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationSuccessHandler getAuthenticationSuccessHandler() {
    return new CustomLoginSuccessHandler();
  }

  @Bean
  public AuthenticationFailureHandler getAuthenticationFailureHandler() {
    return new CustomAuthenticationFailureHandler();
  }

  @Bean
  public LogoutSuccessHandler getCustomLogoutSuccessHandler() {
    return new CustomLogoutSuccessHandler();
  }

  // InMemory 방식으로 UserDetailsService(인증 관리 객체) 사용
  @Bean
  public UserDetailsService userDetailsService() {
    UserDetails user1 = User.builder()
        .username("user1")
        .password("$2a$10$XGw3jOo9mQSoij4/so.6H.BtSRWpgPze6ZWMuc7ntyFFWqVNbcmBe")
        .roles("USER")
        .build();
    UserDetails manager = User.builder()
        .username("manager")
        .password("$2a$10$AEHcuzENZx7OLeA.s8e.t.CvhE/a/GZf.ZKTPEBIKLv8g03zChnD2")
        .roles("MANAGER")
        .build();
    UserDetails admin = User.builder()
        .username("admin")
        .password(passwordEncoder().encode("1"))
        .roles("ADMIN", "MANAGER")
        .build();
    List<UserDetails> list = new ArrayList<>();
    list.add(user1);
    list.add(manager);
    list.add(admin);
    return new InMemoryUserDetailsManager(list);

  }

}