package com.imikasa.Mapper;

import com.imikasa.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    User getUserByName(String name);
    List<String> getUserPermission(Integer id);
    int updatePwd(String pwd,String userName);
}
