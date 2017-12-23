package com.guanxun.service;

import com.guanxun.mapper.*;
import com.guanxun.model.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.*;

import java.util.List;

@Service
public class KeygenService {

    @Autowired
    private KeygenMapper keygenMapper;

    @Cacheable(value = "keygenCache")
    public Keygen load(java.lang.String tableName) {
        return keygenMapper.load(tableName);
    }

    public int insert(Keygen keygen){
        return keygenMapper.insert(keygen);
    }

    public int delete(java.lang.String tableName){
        return keygenMapper.delete(tableName);
    }

    public int update(Keygen keygen){
        return keygenMapper.update(keygen);
    }

    public List<Keygen> findList(Keygen keygen){
        return keygenMapper.findList(keygen);
    }

}
