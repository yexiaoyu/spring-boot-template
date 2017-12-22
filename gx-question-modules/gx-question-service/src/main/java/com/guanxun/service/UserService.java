package com.guanxun.service;

import com.guanxun.mapper.*;
import com.guanxun.model.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User getUserInfo(String usreId) {
        User user = userMapper.findUserInfo(usreId);
        //User user=null;
        return user;
    }
}
