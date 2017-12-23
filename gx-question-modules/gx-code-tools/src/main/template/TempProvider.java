package com.guanxun.provider;

import com.guanxun.common.utils.GxUtils;
import com.guanxun.model.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class ${className}Provider {
    private final String TABLENAME = "${tableName}";
    private final String COLUMNS = "${columnName}";

    public String load(${pk.keyJavaType} $pk.keyFieldName){
        return new SQL(){{
            SELECT(COLUMNS);
            FROM(TABLENAME);
            WHERE("$pk.keyFieldName=#{$pk.keyFieldName}");
        }}.toString();
    }

    public String insert(final ${className} ${lowerName}) {
        return new SQL(){{
            INSERT_INTO(TABLENAME);
        #foreach($item in $!{columnDatas})
            if(GxUtils.isNotEmpty(${lowerName}.get${item.maxFieldName}())){
                VALUES("$item.fieldName","#{$item.fieldName}");
            }
        #end
        }}.toString();
    }

    public String update(final ${className} ${lowerName}) {
        return new SQL(){{
            UPDATE(TABLENAME);
        #foreach($item in $!{columnDatas})
            if(GxUtils.isNotEmpty(${lowerName}.get${item.maxFieldName}())){
                SET("$item.fieldName=#{$item.fieldName}");
            }
        #end
            WHERE("$pk.keyFieldName=#{$pk.keyFieldName}");
        }}.toString();
    }

    public String delete(${pk.keyJavaType} $pk.keyFieldName) {
        return new SQL(){{
            DELETE_FROM(TABLENAME);
            WHERE("$pk.keyFieldName=#{$pk.keyFieldName}");
        }}.toString();
    }

    public String findList(final ${className} ${lowerName}) {
        return new SQL(){{
            SELECT(COLUMNS);
            FROM(TABLENAME);
        #foreach($item in $!{columnDatas})
            if(GxUtils.isNotEmpty(${lowerName}.get${item.maxFieldName}())){
                WHERE("$item.fieldName=#{$item.fieldName}");
            }
        #end
        }}.toString();
    }
}
