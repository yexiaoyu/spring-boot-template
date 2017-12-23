package com.guanxun.mapper;

import com.guanxun.model.*;
import com.guanxun.provider.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ${className}Mapper extends BaseMapper<${pk.keyJavaType}, ${className}>{
    @SelectProvider(type = ${className}Provider.class, method = "load")
    public ${className} load(${pk.keyJavaType} $pk.keyFieldName);

    @InsertProvider(type = ${className}Provider.class, method = "insert")
    @Options(keyProperty="$pk.keyFieldName",keyColumn="$pk.keyFieldName",useGeneratedKeys=true)
    public int insert(${className} ${lowerName});

    @UpdateProvider(type = ${className}Provider.class, method = "delete")
    public int delete(${pk.keyJavaType} $pk.keyFieldName);

    @UpdateProvider(type = ${className}Provider.class, method = "update")
    public int update(${className} ${lowerName});

    @SelectProvider(type=${className}Provider.class, method="findList")
    public List<${className}> findList(${className} ${lowerName});

}
