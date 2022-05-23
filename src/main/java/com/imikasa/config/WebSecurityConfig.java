package com.imikasa.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.PrintWriter;
import java.util.HashMap;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private AccessDecision accessDecision;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.formLogin()
                .loginProcessingUrl("/doLogIn")
                .successHandler(authenticationSuccessHandler())
                .failureHandler(authenticationFailureHandler());
        http.exceptionHandling().accessDeniedHandler(accessDecision);
        http.sessionManagement().disable();
        http.authorizeRequests().anyRequest().permitAll();
    }


    /**
     * 登录成功的 handle
     *
     * @return
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request,response,authentication) -> {
            System.out.println("登录成功了");
            // 把json串写出去
            response.setContentType("application/json;charset=utf-8");
            HashMap<String, Object> map = new HashMap<>(8);
            map.put("code", 200);
            map.put("msg", "登录成功");
            // 把用户信息返回给前端 让前端可以保存起来
            map.put("data", authentication);
            ObjectMapper objectMapper = new ObjectMapper();
            String s = objectMapper.writeValueAsString(map);
            // 写出去
            PrintWriter writer = response.getWriter();
            writer.write(s);
            // 刷新流 关闭流
            writer.flush();
            writer.close();
        };
    }

    /**
     * 登录失败的json
     *
     * @return
     */
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            response.setContentType("application/json;charset=utf-8");
            // 写出去
            HashMap<String, Object> map = new HashMap<>(4);
            map.put("code", 401);
            if (exception instanceof LockedException) {
                map.put("msg", "账户被锁定，登陆失败！");
            } else if (exception instanceof BadCredentialsException) {
                map.put("msg", "账户或者密码错误，登陆失败！");
            } else if (exception instanceof DisabledException) {
                map.put("msg", "账户被禁用，登陆失败！");
            } else if (exception instanceof AccountExpiredException) {
                map.put("msg", "账户已过期，登陆失败！");
            } else if (exception instanceof CredentialsExpiredException) {
                map.put("msg", "密码已过期，登陆失败！");
            } else {
                map.put("msg", "登陆失败！");
            }
            ObjectMapper objectMapper = new ObjectMapper();
            String s = objectMapper.writeValueAsString(map);
            PrintWriter writer = response.getWriter();
            writer.write(s);
            writer.flush();
            writer.close();
        };
    }

}
