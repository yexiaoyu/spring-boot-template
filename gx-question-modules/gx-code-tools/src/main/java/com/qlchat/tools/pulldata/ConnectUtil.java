package com.qlchat.tools.pulldata;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.sql.*;
import java.util.List;

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

    public static void insertInto(String insertSql, List<Object> params) throws SQLException {
        Connection conn = ConnectUtil.getConn();
        PreparedStatement pstmt = (PreparedStatement)conn.prepareStatement(insertSql);
        if(params != null && params.size() > 0){
            int i = 0;
            for (Object param : params) {
                pstmt.setObject(++i, param);
            }
        }
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

    public static String httpGet(String url,String cookie) throws Exception {
        if(StringUtils.isEmpty(cookie)){
            cookie = "HqNL_ef65_saltkey=xAfETZMO; HqNL_ef65_lastvisit=1516019841; _csrf=e1b65184f4f07b2833d346eab9457fb555f9465a2b093c703dfbfa03bd45aa96a%3A2%3A%7Bi%3A0%3Bs%3A5%3A%22_csrf%22%3Bi%3A1%3Bs%3A32%3A%22abvbrpxnPYJaNUSetf7Lz795fuxe4V-2%22%3B%7D; Hm_lvt_5d70f3704df08b4bfedf4a7c4fb415ef=1515204849,1515205926,1516023478,1516371723; chid=d79688f1e7866722fd16866f40a252116287a86b4e6e2213e3150af3ba96e038a%3A2%3A%7Bi%3A0%3Bs%3A4%3A%22chid%22%3Bi%3A1%3Bs%3A1%3A%222%22%3B%7D; xd=bbc5bced806f427ff9aca7cf1cf56a37c9016539f1da160ae385126fb0d1cc2fa%3A2%3A%7Bi%3A0%3Bs%3A2%3A%22xd%22%3Bi%3A1%3Bs%3A1%3A%223%22%3B%7D; Hm_lpvt_5d70f3704df08b4bfedf4a7c4fb415ef=1516546304";
        }
        //获取请求连接
        org.jsoup.Connection con = Jsoup.connect(url).ignoreContentType(true);
        //请求头设置，特别是cookie设置
        con.header("Accept", "text/html, application/xhtml+xml, */*");
        con.header("Content-Type", "application/x-www-form-urlencoded");
        con.header("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0))");
        con.header("Cookie", cookie);
        //解析请求结果
        Document doc=con.get();
        return doc.body().toString();

    }
}

