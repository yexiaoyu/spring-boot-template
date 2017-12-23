package com.guanxun.service;

import com.guanxun.mapper.*;
import com.guanxun.model.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.*;

import java.util.List;

@Service
public class ${className}Service {

    @Autowired
    private ${className}Mapper ${lowerName}Mapper;

    @Cacheable(value = "${lowerName}Cache")
    public ${className} load(${pk.keyJavaType} $pk.keyFieldName) {
        return ${lowerName}Mapper.load($pk.keyFieldName);
    }

    public int insert(${className} ${lowerName}){
        return ${lowerName}Mapper.insert(${lowerName});
    }

    public int delete(${pk.keyJavaType} $pk.keyFieldName){
        return ${lowerName}Mapper.delete($pk.keyFieldName);
    }

    public int update(${className} ${lowerName}){
        return ${lowerName}Mapper.update(${lowerName});
    }

    public List<${className}> findList(${className} ${lowerName}){
        return ${lowerName}Mapper.findList(${lowerName});
    }

}
