package com.imikasa.config;

import com.imikasa.Mapper.UserMapper;
import com.imikasa.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Configuration
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userMapper.getUserByName(username);
//        userMapper.updatePwd(new BCryptPasswordEncoder().encode(user.getPwd()),username);
//        user = userMapper.getUserByName(username);
        if(user != null){
            List<String> userPermissions = userMapper.getUserPermission(user.getId());
            user.setAuthors(userPermissions);
        }
//        String encode = new BCryptPasswordEncoder().encode(user.getPwd());
//        user.setPwd(encode);
        System.out.println(user);
        return user;
    }
}
