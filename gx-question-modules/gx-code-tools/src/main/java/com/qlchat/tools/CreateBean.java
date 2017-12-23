package com.qlchat.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <br>
 * <b>功能：</b>读取数据库，获取表列<br>
 * <b>作者：</b>张凯<br>
 * <b>日期：</b> 2016-8-16 <br>
 * <b>版权所有：<b>版权所有(C)<br>
 */
public class CreateBean {
	static String url;   
	static String username ;
	static String password ;
	static String rt="\r\t";
	String SQLTables="show tables";
	static{
	try{
		Class.forName("com.mysql.jdbc.Driver");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	public String getKeyJavaType() {
		return keyJavaType;
	}
	public void setKeyJavaType(String keyJavaType) {
		this.keyJavaType = keyJavaType;
	}
	public String getKeySqlType() {
		return keySqlType;
	}
	public void setKeySqlType(String keySqlType) {
		this.keySqlType = keySqlType;
	}
	public String getKeyFieldName() {
		return keyFieldName;
	}
	public void setKeyFieldName(String keyFieldName) {
		this.keyFieldName = keyFieldName;
	}


	private String keyName;
	private String keyFieldName;

	private String keyJavaType;
	private String keySqlType;
	
	
	@SuppressWarnings("static-access")
	public void setMysqlInfo(String url,String username,String password){
		   this.url=url;
		   this.username=username;
		   this.password=password;
	}
	public Connection getConnection() throws SQLException{
		return DriverManager.getConnection(url, username, password);
	}
    
    /**
     * 查询表的字段，封装成List
     * @param tableName
     * @return
     * @throws SQLException
     */
    public List<ColumnData> getColumnDatas(String schema, String tableName) throws SQLException{
    	String SQLColumns="SELECT distinct COLUMN_NAME, DATA_TYPE, COLUMN_COMMENT, COLUMN_KEY FROM information_schema.columns WHERE table_schema='"+schema+"' and table_name =  '"+tableName+"' ";
        Connection con=this.getConnection();
    	PreparedStatement ps=con.prepareStatement(SQLColumns);
    	List<ColumnData> columnList= new ArrayList<ColumnData>();
        ResultSet rs=ps.executeQuery();
 
        while(rs.next()){
        	String name = rs.getString(1); 
        	String fieldName = getColumnNameToFieldName(name);
        	
			String type = rs.getString(2);
			String comment = rs.getString(3);
			type=this.getType(type);
			
			String jdbcType = this.getJdbcType(rs.getString(2));
			
			String key = rs.getString(4).toUpperCase();
			ColumnData cd= new ColumnData();
        	if(key!=null&&key.contains("PRI")){
        		keyFieldName = fieldName;
        		keyName = name;
        		keySqlType = jdbcType;
        		keyJavaType = this.getFullJavaType(rs.getString(2));
        		cd.setIsPK(keyName);
        	}
			
			cd.setJdbcType(jdbcType);
			cd.setColumnName(name);
			cd.setFieldName(fieldName);
			cd.setDataType(type);
			cd.setColumnComment(comment);
			columnList.add(cd);
        }
		rs.close();
		ps.close();
		con.close();
		return columnList;
    }
    public String getJdbcType(String type){
    	String otype = type;
    	type=type.toLowerCase();
    	if("char".equals(type) ){
			return "CHAR";
		}
    	
    	if("varchar".equals(type) || "enum".equals(type)){
			return "VARCHAR";
		}
    	
    	if("text".equals(type) ){
			return "VARCHAR";
		}
  
    	if( type.indexOf("float")>=0){
			return "DOUBLE";
			
		}
    	if( type.indexOf("decimal")>=0){
			return "NUMERIC";
			
		}
    	if( type.indexOf("double")>=0){
			return "DOUBLE";
			
		}
    	if("int".equals(type) ){
			return "INTEGER";
		}
    	
    	if("tinyint".equals(type) ){
			return "TINYINT";
		}
    	
    	if("bigint".equals(type)){
			return "BIGINT";
		}
 
    	if("timestamp".equals(type) || "datetime".equals(type)){
			return "TIMESTAMP"; 
		}
    	
    	if("date".equals(type)){
			return "DATE"; 
		}
    	
    	return otype.toUpperCase();
    }
    
    
    
    public String getType(String type){
    	String otype = type;
    	type=type.toLowerCase();
    	if("char".equals(type) || "varchar".equals(type) || type.indexOf("text")>=0 || "enum".equals(type)){
			return "String";
		}else if( type.indexOf("float")>=0){
			return "Float";
			
		}else if( type.indexOf("decimal")>=0){
			return "java.math.BigDecimal";
			
		}else if( type.indexOf("double")>=0){
			return "Double";
			
		}else if("int".equals(type) || "smallint".equals(type) ){
			return "Integer";
		}else if("tinyint".equals(type) ){
			return "Integer";
		}else if("bigint".equals(type)){
			return "Long";
		}else if("timestamp".equals(type) || "date".equals(type)  || "datetime".equals(type)){
			return "java.util.Date"; 
		}
    	return otype;
    }
    
    public String getFullJavaType(String type){
    	String otype = type;
    	type=type.toLowerCase();
    	if("char".equals(type) || "varchar".equals(type) || type.indexOf("text")>=0){
			return "java.lang.String";
		}else if( type.indexOf("float")>=0){
			return "java.lang.Float";
			
		}else if( type.indexOf("decimal")>=0){
			return "java.math.BigDecimal";
			
		}else if( type.indexOf("double")>=0){
			return "java.lang.Double";
			
		}else if("int".equals(type) ){
			return "java.lang.Integer";
		}else if("tinyint".equals(type) ){
			return "java.lang.Integer";
		}else if("bigint".equals(type)){
			return "java.lang.Long";
		}else if("timestamp".equals(type) || "date".equals(type)  || "datetime".equals(type)){
			return "java.util.Date"; 
			//return "java.sql.Timestamp";
		}
    	return otype;
    }
    
    
    /**
     * 
     * <br>
     * <b>功能：</b>字段名转换成类属性名 每_首字母大写<br>
     * <b>作者：</b><br>
     * @param column
     * @return
     */
    public String getColumnNameToFieldName(String column){
    	String [] split=column.split("_");
    	if(split.length>1){
    		StringBuffer sb=new StringBuffer();
            for(int i=0;i<split.length;i++){
            	String tempTableName=split[i].substring(0, 1).toUpperCase()+split[i].substring(1, split[i].length()).toLowerCase();
                sb.append(tempTableName);
            }
            
            String tempField = sb.toString();
            System.out.println(tempField);
            
            return tempField.substring(0, 1).toLowerCase()+tempField.substring(1, tempField.length());
    	}else{
    		String tempField=split[0].substring(0, 1).toLowerCase()+split[0].substring(1, split[0].length()).toLowerCase();
    		return tempField;
    	}
    }
   
    /**
     * 
     * <br>
     * <b>功能：</b>表名转换成类名 每_首字母大写<br>
     * <b>作者：</b><br>
     * @param tableName
     * @return
     */
    public String getTablesNameToClassName(String tableName){
    	String [] split=tableName.split("_");
    	if(split.length>1){
    		StringBuffer sb=new StringBuffer();
            for(int i=0;i<split.length;i++){
            	String tempTableName=split[i].substring(0, 1).toUpperCase()+split[i].substring(1, split[i].length());
                sb.append(tempTableName);
            }
            System.out.println(sb.toString());
            return sb.toString();
    	}else{
    		String tempTables=split[0].substring(0, 1).toUpperCase()+split[0].substring(1, split[0].length());
    		return tempTables;
    	}
    }
}
