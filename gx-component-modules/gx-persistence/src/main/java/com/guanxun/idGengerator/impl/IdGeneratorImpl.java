package com.guanxun.idGengerator.impl;

import com.guanxun.common.utils.GxUtils;
import com.guanxun.idGengerator.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class IdGeneratorImpl implements IdGenerator {
    Logger logger = LoggerFactory.getLogger(IdGeneratorImpl.class);
    @Resource
    private com.alibaba.druid.pool.DruidDataSource dataSource;

    private int size = 10000;// 每次读取段大小
    private Long default_start = 10000000L;

    private static final String keyGenTable = "gx_keygen";

    private Map<String, IdHolder> holderMap = new ConcurrentHashMap<String, IdHolder>();

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public long generate(String tableName, String columnName) {
        return generate(tableName, columnName, this.size);
    }

    @Override
    public long generate(String tableName, String columnName, int onceGenSize) {
        synchronized (keyGenTable) {
            IdHolder holder = this.holderMap.get(tableName);
            if (holder == null) {
                holder = new IdHolder();
                this.holderMap.put(tableName, holder);
            }
            if (holder.needAlloc()) {
                long lastUsedId = alloc(tableName, columnName, onceGenSize);
                holder.currentId = (lastUsedId + 1L);
                holder.limit = (lastUsedId + onceGenSize);
            } else {
                holder.currentId += 1L;
            }
            return holder.currentId;
        }
    }

    private long alloc(String tableName, String columnName, int size) {
        long result = 0L;
        Connection con = null;
        boolean oldAutoCommit = false;
        try {
            con = this.dataSource.getConnection();
            oldAutoCommit = con.getAutoCommit();
            con.setAutoCommit(false);

            int updateCount = this.updateLastUsedId(con, tableName, columnName, size);

            if (updateCount == 0) {
                this.initIdTable(con, tableName, columnName);
            }
            result = this.getLastUsedId(con, tableName, columnName);
            con.commit();
            String ip = GxUtils.getLinuxLocalIp();
            logger.info("获取ID生成段, IP={}, last_id_={}, SQL=update {} set last_used_id = last_used_id + {} where table_name = {}", ip, result, keyGenTable, size, tableName);
        } catch (Exception e) {
            try {
                if (con != null)
                    con.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(oldAutoCommit);
                    con.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        return result;
    }

    private long getLastUsedId(Connection con, String tableName, String columnName) throws SQLException {
        long result = 0;
        PreparedStatement ps = con.prepareStatement("select last_used_id from " + keyGenTable + " where table_name = ?");
        ps.setString(1, tableName);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            result = rs.getLong("last_used_id");
        }
        rs.close();
        ps.close();
        return result;
    }

    private int updateLastUsedId(Connection con, String tableName, String columnName, int size)
            throws SQLException {
        PreparedStatement ps = con.prepareStatement("update " + keyGenTable + " set last_used_id = last_used_id + ? where table_name = ?");
        ps.setInt(1, size);
        ps.setString(2, tableName);
        int result = ps.executeUpdate();
        ps.close();
        return result;
    }

    private void initIdTable(Connection con, String tableName, String columnName) throws SQLException {
        PreparedStatement ps = con.prepareStatement("select IFNULL(max(" + columnName + "), " + default_start + ") from " + tableName);
        ResultSet rs = ps.executeQuery();
        rs.next();
        long maxId = rs.getLong(1);
        rs.close();
        ps.close();
        String ip = GxUtils.getLinuxLocalIp();
        logger.info("初始化ID表, IP={}, SQL=insert into {} (table_name,last_used_id) values ({}, {})", ip, keyGenTable, tableName, maxId);
        ps = con.prepareStatement("insert into " + keyGenTable + " (table_name,last_used_id) values (?,?)");
        ps.setString(1, tableName);
        ps.setLong(2, maxId);
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public String genSid() {
        return String.valueOf(genLid());
    }

    @Override
    public Long genLid() {
        return this.generate(keyGenTable, "last_used_id");
    }

    static class IdHolder {
        long currentId;
        long limit;

        boolean needAlloc() {
            return this.currentId >= this.limit;
        }
    }
}
