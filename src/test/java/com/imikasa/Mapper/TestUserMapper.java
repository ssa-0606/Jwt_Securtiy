package com.imikasa.Mapper;

import com.imikasa.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestUserMapper {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void getUser(){
        User mikasa = userMapper.getUserByName("mikasa");
        System.out.println(mikasa);
        List<String> userPermission = userMapper.getUserPermission(mikasa.getId());
        System.out.println(userPermission);
    }

}
