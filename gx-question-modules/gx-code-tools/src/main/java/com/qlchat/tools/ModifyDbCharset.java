package com.qlchat.tools;


import java.sql.*;

public class ModifyDbCharset {
    public static void main(String[] args) {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1:3306/question?characterEncoding=UTF-8";
        String username = "root";
        String passWord = "root123";


        try{
            Class.forName(driver);
            Connection conn= DriverManager.getConnection(url,username,passWord);
            if(!conn.isClosed())
                System.out.println("Connection Succeed!");
            Statement statement = conn.createStatement();
            String sql = "show tables";
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                String tableName = rs.getString(1);

                //System.out.println("ALTER TABLE `"+tableName+"` DEFAULT CHARACTER SET utf8mb4;");
//                Statement lieStatement = conn.createStatement();
//                ResultSet lieRs = lieStatement.executeQuery("SHOW FULL COLUMNS FROM " + tableName);
//                while(lieRs.next()){
//                    String name = lieRs.getString(1);
//                    String dataType = lieRs.getString(2);
//                    String collation = lieRs.getString(3);
//                    if(collation != null && collation.contains("latin1")){
//                        System.out.println("ALTER TABLE `"+tableName+"` CHANGE `"+name+"` `"+name+"` "+dataType+" CHARACTER SET utf8mb4;");
//                    }
//                }

                Statement createStatement = conn.createStatement();
                ResultSet createRs = createStatement.executeQuery("SHOW CREATE TABLE " + tableName);
                while(createRs.next()){
                    System.out.println(createRs.getString(2) + ";");
                }
            }
            rs.close();
            conn.close();
        }catch(ClassNotFoundException e){
            System.out.println("sorry,can't find the driver");
            e.printStackTrace();
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
