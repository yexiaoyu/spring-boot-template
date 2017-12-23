package com.guanxun.provider;

import com.guanxun.common.utils.GxUtils;
import com.guanxun.model.User;
import org.apache.ibatis.jdbc.SQL;

public class UserProvider {
    private String tableName = "user";
    private String columns = "id,name,age,password";

    public String load(String id){
        return new SQL(){{
            SELECT(columns);
            FROM(tableName);
            WHERE("id=#{id}");
        }}.toString();
    }

    public String insert(final User user) {
        return new SQL(){{
            INSERT_INTO(tableName);
            if(GxUtils.isNotEmpty(user.getId())){
                VALUES("id","#{id}");
            }
            if(GxUtils.isNotEmpty(user.getAge())){
                VALUES("age","#{age}");
            }
            if(GxUtils.isNotEmpty(user.getName())){
                VALUES("name","#{name}");
            }
            if(GxUtils.isNotEmpty(user.getPassword())){
                VALUES("password","#{password}");
            }

        }}.toString();
    }

    public String update(final User user) {
        return new SQL(){{
            UPDATE(tableName);
            if(GxUtils.isNotEmpty(user.getAge())){
                SET("age=#{age}");
            }
            if(GxUtils.isNotEmpty(user.getName())){
                SET("name=#{name}");
            }
            if(GxUtils.isNotEmpty(user.getPassword())){
                SET("password=#{password}");
            }
            WHERE("id=#{id}");
        }}.toString();
    }

    public String delete(String id) {
        return new SQL(){{
            DELETE_FROM(tableName);
            WHERE("id=#{id}");
        }}.toString();
    }

    public String findList(final User user) {
        return new SQL(){{
            SELECT(columns);
            FROM(tableName);
            if(GxUtils.isNotEmpty(user.getName())){
                WHERE("name=#{name}");
            }
            if(GxUtils.isNotEmpty(user.getAge())){
                WHERE("age=#{age}");
            }
        }}.toString();
    }


}
