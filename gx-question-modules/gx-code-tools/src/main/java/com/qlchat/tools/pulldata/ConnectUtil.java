package com.qlchat.tools.pulldata;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.sql.*;

public class ConnectUtil {
    private static Connection connection;
    public static Connection getConn() {

        if(connection != null){
//            System.out.println("返回旧数据库连接");
            return connection;
        }
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/question?useUnicode=true&characterEncoding=UTF-8";
        String username = "root";
        String password = "root123";
        try {
            Class.forName(driver); //classLoader,加载对应驱动
            connection = (Connection) DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(connection == null){
            System.out.println("没有拿到数据库链接");
            return getConn();
        }
        return connection;
    }

    public static void insertInto(String insertSql) throws SQLException {
        Connection conn = ConnectUtil.getConn();
        PreparedStatement pstmt = (PreparedStatement)conn.prepareStatement(insertSql);
        pstmt.executeUpdate();
    }

    public static JSONArray handleToJson(ResultSet rs)
            throws SQLException {
        //创建一个JSONArray对象
        JSONArray jsonArray = new JSONArray();
        //获得ResultSetMeataData对象
        ResultSetMetaData rsmd = rs.getMetaData();
        while (rs.next()) {
            //定义json对象
            JSONObject obj = new JSONObject();
            //判断数据类型&获取value
            getType(rs, rsmd, obj);
            //将对象添加到JSONArray中
            jsonArray.add(obj);
        }
        return jsonArray;
    }
    public static void getType(ResultSet rs, ResultSetMetaData rsmd,
                         JSONObject obj) throws SQLException {
        int total_rows = rsmd.getColumnCount();
        for (int i = 0; i < total_rows; i++) {
            String columnName = rsmd.getColumnLabel(i + 1);
            if (obj.containsKey(columnName)) {
                columnName += "1";
            }
            try {
                switch (rsmd.getColumnType(i + 1)) {
                    case java.sql.Types.ARRAY:
                        obj.put(columnName, rs.getArray(columnName));
                        break;
                    case java.sql.Types.BIGINT:
                        obj.put(columnName, rs.getInt(columnName));
                        break;
                    case java.sql.Types.BOOLEAN:
                        obj.put(columnName, rs.getBoolean(columnName));
                        break;
                    case java.sql.Types.BLOB:
                        obj.put(columnName, rs.getBlob(columnName));
                        break;
                    case java.sql.Types.DOUBLE:
                        obj.put(columnName, rs.getDouble(columnName));
                        break;
                    case java.sql.Types.FLOAT:
                        obj.put(columnName, rs.getFloat(columnName));
                        break;
                    case java.sql.Types.INTEGER:
                        obj.put(columnName, rs.getInt(columnName));
                        break;
                    case java.sql.Types.NVARCHAR:
                        obj.put(columnName, rs.getNString(columnName));
                        break;
                    case java.sql.Types.VARCHAR:
                        obj.put(columnName, rs.getString(columnName));
                        break;
                    case java.sql.Types.TINYINT:
                        obj.put(columnName, rs.getInt(columnName));
                        break;
                    case java.sql.Types.SMALLINT:
                        obj.put(columnName, rs.getInt(columnName));
                        break;
                    case java.sql.Types.DATE:
                        obj.put(columnName, rs.getDate(columnName));
                        break;
                    case java.sql.Types.TIMESTAMP:
                        obj.put(columnName, rs.getTimestamp(columnName));
                        break;
                    default:
                        obj.put(columnName, rs.getObject(columnName));
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

