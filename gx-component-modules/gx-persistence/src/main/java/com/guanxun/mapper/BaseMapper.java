package com.guanxun.mapper;

import java.io.Serializable;
import java.util.List;

public interface BaseMapper <PK extends Serializable, PO>{
    public PO load(PK id);
    public int insert(PO po);
    public int update(PO po);
    public int delete(PK id);
    public List<PO> findList(PO po);
}
