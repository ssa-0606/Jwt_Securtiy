package com.imikasa.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imikasa.filter.JwtCheckFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private AccessDecision accessDecision;

    @Autowired
    private JwtCheckFilter jwtCheckFilter;

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
        http.addFilterBefore(jwtCheckFilter, UsernamePasswordAuthenticationFilter.class);
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
            // 1.拿去用户数据
            UserDetails principal = (UserDetails) authentication.getPrincipal();
            String username = principal.getUsername();
            // 2.拿起权限
            Collection<? extends GrantedAuthority> authorities = principal.getAuthorities();
            List<String> authorizations = new ArrayList<>(authorities.size() * 2);
            authorities.forEach(e -> authorizations.add(e.getAuthority()));

            // 3.组装jwt
            // 生成jwt
            // 颁发时间
            Date createTime = new Date();
            // 过期时间
            Calendar now = Calendar.getInstance();
            // 设置未来的时间
            now.set(Calendar.MINUTE, 7200);
            Date expireTime = now.getTime();
            // header
            HashMap<String, Object> header = new HashMap<>(4);
            header.put("alg", "HS256");
            header.put("typ", "JWT");
            // 设置载体
            String sign = JWT.create()
                    .withHeader(header)
                    .withIssuedAt(createTime)
                    .withExpiresAt(expireTime)
                    .withClaim("username", username)
                    .withClaim("authorizations", authorizations)
                    .sign(Algorithm.HMAC256("mikasa".getBytes(StandardCharsets.UTF_8)));

            // 把json串写出去
            response.setContentType("application/json;charset=utf-8");
            HashMap<String, Object> map = new HashMap<>();
            map.put("code", 200);
            map.put("msg", "登录成功");
            map.put("access_token", sign);
            // 把用户信息返回给前端 让前端可以保存起来
            redisTemplate.opsForValue().set("jwt:token:"+ sign, sign, Duration.ofMinutes(2700));
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
