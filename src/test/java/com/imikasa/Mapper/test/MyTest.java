package com.imikasa.Mapper.test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class MyTest {
    public static void main(String[] args) {
        String root = new BCryptPasswordEncoder().encode("wangpwd");

        System.out.println(root);
    }
}
