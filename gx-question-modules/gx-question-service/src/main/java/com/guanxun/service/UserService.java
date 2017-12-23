package com.guanxun.service;

import com.guanxun.mapper.*;
import com.guanxun.model.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User getUserInfo(String usreId) {
        User user = userMapper.load(usreId);
        //User user=null;
        return user;
    }

    public int insert(User user){
        return userMapper.insert(user);
    }

    public int deleteById(int id){
        return userMapper.delete(id);
    }

    public int update(User user){
        return userMapper.update(user);
    }

    public List<User> findList(User user){
        return userMapper.findList(user);
    }
    public List<User> findByNameOrAge(String name, Integer age){
        return userMapper.findByNameOrAge(name, age);
    }
}
