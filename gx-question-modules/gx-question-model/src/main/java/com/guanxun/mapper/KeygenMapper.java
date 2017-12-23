package com.guanxun.mapper;

import com.guanxun.model.*;
import com.guanxun.provider.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface KeygenMapper extends BaseMapper<java.lang.String, Keygen>{
    @SelectProvider(type = KeygenProvider.class, method = "load")
    public Keygen load(java.lang.String tableName);

    @InsertProvider(type = KeygenProvider.class, method = "insert")
    @Options(keyProperty="tableName",keyColumn="tableName",useGeneratedKeys=true)
    public int insert(Keygen keygen);

    @UpdateProvider(type = KeygenProvider.class, method = "delete")
    public int delete(java.lang.String tableName);

    @UpdateProvider(type = KeygenProvider.class, method = "update")
    public int update(Keygen keygen);

    @SelectProvider(type=KeygenProvider.class, method="findList")
    public List<Keygen> findList(Keygen keygen);

}
