package com.guanxun.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.guanxun.idGengerator.IdGenerator;
import com.guanxun.model.*;
import com.guanxun.service.*;
import com.guanxun.utils.IdGeneratorUtil;
import io.swagger.annotations.*;
import org.apache.log4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Api("userController相关api")
public class UserController {

    private Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    private UserService userService;


    @ApiOperation("获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", dataType = "String", required = true, value = "用户的姓名", defaultValue = "zhaojigang"),
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "请求参数没填好"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")
    })
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
    public User getUserInfoById(String userId) {
        User user = userService.getUserInfo(userId);

        if(user!=null){
            System.out.println("user.getName():"+user.getName());
            logger.info("user.getAge():"+user.getAge());
        }
        return user;
    }

    @ApiOperation("新增用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "name", dataType = "String", required = true, value = "用户的姓名", defaultValue = "zhaojigang"),
            @ApiImplicitParam(paramType = "query", name = "age", dataType = "int", required = true, value = "用户的年龄", defaultValue = "30"),
            @ApiImplicitParam(paramType = "query", name = "password", dataType = "String", required = true, value = "用户的姓名", defaultValue = "7777"),
    })
    @RequestMapping(value = "/insertUser", method = RequestMethod.POST)
    public int insertUser(String name, int age, String password) {
        User user = new User();
        user.setAge(age);
        user.setId(IdGeneratorUtil.getGenSid());
        user.setName(name);
        user.setPassword(password);
        return userService.insert(user);
    }

    @ApiOperation("修改用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", dataType = "String", required = true, value = "用户ID", defaultValue = "2"),
            @ApiImplicitParam(paramType = "query", name = "name", dataType = "String", required = true, value = "用户的姓名", defaultValue = "zhaojigang"),
            @ApiImplicitParam(paramType = "query", name = "age", dataType = "int", required = true, value = "用户的年龄", defaultValue = "30"),
            @ApiImplicitParam(paramType = "query", name = "password", dataType = "String", required = true, value = "用户的密码", defaultValue = "888"),
    })
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public int updateUser(String id, String name, int age, String password) {
        User user = new User();
        user.setAge(age);
        user.setId(id);
        user.setName(name);
        user.setPassword(password);
        return userService.update(user);
    }
    @ApiOperation("查询用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "name", dataType = "String", required = true, value = "用户的姓名", defaultValue = "zhaojigang"),
            @ApiImplicitParam(paramType = "query", name = "age", dataType = "int", required = true, value = "用户的年龄", defaultValue = "30"),
    })
    @RequestMapping(value = "/findUser", method = RequestMethod.POST)
    public List<User> findUser(String name, int age) {
        User user = new User();
        user.setAge(age);
        user.setName(name);
        // 分页查询:只有紧跟在 PageHelper.startPage 方法后的第一个 MyBatis 的查询(select)方法会被分页。
        PageHelper.startPage(1, 1, false);
        List<User> userList = userService.findList(user);
        PageInfo pageInfo = new PageInfo(userList);
        Page page = (Page) userList;
        System.out.println(pageInfo);
        System.out.println(page);
        return userList;

    }
}
