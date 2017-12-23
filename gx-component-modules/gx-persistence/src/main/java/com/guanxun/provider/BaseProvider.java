package com.guanxun.provider;

import org.apache.ibatis.jdbc.SQL;

import java.io.Serializable;

public abstract class BaseProvider <PK extends Serializable, PO>{
    public abstract String load(final PK id);
    public abstract SQL insert(PO po);
    public abstract String update(PO po);
    public abstract String delete(final PK id);
    public abstract String findList(PO po);
}
