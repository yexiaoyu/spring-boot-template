package com.guanxun.mapper;

import com.guanxun.model.*;
import com.guanxun.provider.UserProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<String, User>{
    /*
     * 这是基于注解的映射方式，实现对数据的增删改查，将sql语句直接写在注解的括号中
     * 这是一个接口，其不需要类去实现它
     * 下边分别是插入，删除，修改，查询一个记录，查询所有的记录
     * */
    @Cacheable(value = "userCache")// 使用缓存
    @SelectProvider(type = UserProvider.class, method = "load")
    public User load(String id);

    @InsertProvider(type = UserProvider.class, method = "insert")
    @Options(keyProperty="id",keyColumn="id",useGeneratedKeys=true)
    public int insert(User user);

    @UpdateProvider(type = UserProvider.class, method = "delete")
    public int delete(int id);

    @UpdateProvider(type = UserProvider.class, method = "update")
    public int update(User user);

    // 对于只有一个参数的情况，可以直接使用
    @SelectProvider(type=UserProvider.class, method="findList")
    public List<User> findList(User user);

    // http://blog.csdn.net/qqlrq/article/details/45721755
    // 在超过一个参数的情况下，@SelectProvide方法必须接受Map<String, Object>做为参数
    // 如果参数使用了@Param注解，那么参数在Map中以@Param的值为key，如下例中的name；
    // 如果参数没有使用@Param注解，那么参数在Map中以参数的顺序为key，如下例中的age
    @SelectProvider(type=UserProvider.class, method="findByNameOrAge")
    public List<User> findByNameOrAge(@Param("name") String name ,Integer age);

}
