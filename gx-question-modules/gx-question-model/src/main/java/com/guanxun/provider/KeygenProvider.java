package com.guanxun.provider;

import com.guanxun.common.utils.GxUtils;
import com.guanxun.model.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class KeygenProvider {
    private final String TABLENAME = "gx_keygen";
    private final String COLUMNS = "table_name 'tableName',last_used_id 'lastUsedId'";

    public String load(java.lang.String tableName){
        return new SQL(){{
            SELECT(COLUMNS);
            FROM(TABLENAME);
            WHERE("table_name=#{tableName}");
        }}.toString();
    }

    public String insert(final Keygen keygen) {
        return new SQL(){{
            INSERT_INTO(TABLENAME);
                    if(GxUtils.isNotEmpty(keygen.getTableName())){
                VALUES("table_name","#{tableName}");
            }
                    if(GxUtils.isNotEmpty(keygen.getLastUsedId())){
                VALUES("last_used_id","#{lastUsedId}");
            }
                }}.toString();
    }

    public String update(final Keygen keygen) {
        return new SQL(){{
            UPDATE(TABLENAME);
                    if(GxUtils.isNotEmpty(keygen.getTableName())){
                SET("table_name=#{tableName}");
            }
                    if(GxUtils.isNotEmpty(keygen.getLastUsedId())){
                SET("last_used_id=#{lastUsedId}");
            }
                    WHERE("table_name=#{tableName}");
        }}.toString();
    }

    public String delete(java.lang.String tableName) {
        return new SQL(){{
            DELETE_FROM(TABLENAME);
            WHERE("table_name=#{tableName}");
        }}.toString();
    }

    public String findList(final Keygen keygen) {
        return new SQL(){{
            SELECT(COLUMNS);
            FROM(TABLENAME);
                    if(GxUtils.isNotEmpty(keygen.getTableName())){
                WHERE("table_name=#{tableName}");
            }
                    if(GxUtils.isNotEmpty(keygen.getLastUsedId())){
                WHERE("last_used_id=#{lastUsedId}");
            }
                }}.toString();
    }
}
