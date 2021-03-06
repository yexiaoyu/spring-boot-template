package com.guanxun.controller;

import com.guanxun.model.*;
import com.guanxun.redis.*;
import com.guanxun.service.*;
import io.swagger.annotations.*;
import org.apache.log4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/redis")
@Api("redisController相关api")
public class RedisController {

    private Logger logger = Logger.getLogger(RedisController.class);

    @Autowired
    public ValueRedisTemplate valueRedisTemplate;

    @Autowired
    public UserService userService;

    @ApiOperation("设置缓存")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "key", dataType = "String", required = true, value = "缓存key", defaultValue = "guanxun"),
            @ApiImplicitParam(paramType = "query", name = "value", dataType = "String", required = true, value = "缓存值", defaultValue = "guanxun"),
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "请求参数没填好"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")
    })
    @RequestMapping(value = "/setRedis", method = RequestMethod.POST)
    public User set(String key, String value) {
        User user = userService.load("1");
        valueRedisTemplate.set(key, user);
        return valueRedisTemplate.get(key, User.class);
    }


}
