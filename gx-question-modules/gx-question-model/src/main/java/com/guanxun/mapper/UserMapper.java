package com.guanxun.mapper;

import com.guanxun.model.*;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("select name, age,password from user where id=#{id}")
    public User findUserInfo(String id);
}
