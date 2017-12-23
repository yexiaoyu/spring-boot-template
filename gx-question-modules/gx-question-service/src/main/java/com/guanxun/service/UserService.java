package com.guanxun.service;

import com.guanxun.mapper.*;
import com.guanxun.model.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.*;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Cacheable(value = "userCache")
    public User load(String usreId) {
        return userMapper.load(usreId);
    }

    public int insert(User user){
        return userMapper.insert(user);
    }

    public int delete(int id){
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
