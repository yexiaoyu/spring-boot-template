package com.guanxun.mapper;

import com.guanxun.model.*;
import com.guanxun.provider.UserProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<String, User>{
    /*
     * 这是基于注解的映射方式，实现对数据的增删改查，将sql语句直接写在注解的括号中
     * 这是一个接口，其不需要类去实现它
     * 下边分别是插入，删除，修改，查询一个记录，查询所有的记录
     * */

    @SelectProvider(type = UserProvider.class, method = "load")
    public User load(String id);

    @InsertProvider(type = UserProvider.class, method = "insert")
    @Options(keyProperty="id",keyColumn="id",useGeneratedKeys=true)
    public int insert(User user);

    @UpdateProvider(type = UserProvider.class, method = "delete")
    public int delete(int id);

    @UpdateProvider(type = UserProvider.class, method = "update")
    public int update(User user);

    @SelectProvider(type=UserProvider.class, method="findList")
    public List<User> findList(User user);

}
