package com.qlchat.tools;

/**
 * 表字段类
 * @author Administrator
 *
 */
public class ColumnData {
	private String isPK;
	private String columnName;//// 数据库对应字段20131111
	private String maxFieldName;//大写第一个字母属性名
	private String fieldName; //属性名
//	private String eqFieldName; //=
//	private String likeFieldName; //like
//	
//	private String gtFieldName; //> 大于
//	private String ltFieldName; //< 小于
//	private String egtFieldName; //>= 大于或等于
//	private String eltFieldName; //<= 小于或等于
	
	private String dataType;
	private String jdbcType;
	private String columnComment;
	
	public String getIsPK() {
		return isPK;
	}
	public void setIsPK(String isPK) {
		this.isPK = isPK;
	}
//	public String getEqFieldName() {
//		return eqFieldName;
//	}
//	public void setEqFieldName(String eqFieldName) {
//		this.eqFieldName = eqFieldName;
//	}
//	public String getLikeFieldName() {
//		return likeFieldName;
//	}
//	public void setLikeFieldName(String likeFieldName) {
//		this.likeFieldName = likeFieldName;
//	}
//	public String getGtFieldName() {
//		return gtFieldName;
//	}
//	public void setGtFieldName(String gtFieldName) {
//		this.gtFieldName = gtFieldName;
//	}
//	public String getLtFieldName() {
//		return ltFieldName;
//	}
//	public void setLtFieldName(String ltFieldName) {
//		this.ltFieldName = ltFieldName;
//	}
//	
	public String getJdbcType() {
		return jdbcType;
	}
	public void setJdbcType(String jdbcType) {
		this.jdbcType = jdbcType;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getColumnComment() {
		return columnComment;
	}
	public void setColumnComment(String columnComment) {
		this.columnComment = columnComment;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
//	public String getEgtFieldName() {
//		return egtFieldName;
//	}
//	public void setEgtFieldName(String egtFieldName) {
//		this.egtFieldName = egtFieldName;
//	}
//	public String getEltFieldName() {
//		return eltFieldName;
//	}
//	public void setEltFieldName(String eltFieldName) {
//		this.eltFieldName = eltFieldName;
//	} 
	public String getMaxFieldName() {
		String maxChar=this.fieldName.substring(0, 1).toUpperCase();
		String maxFieldName=maxChar+fieldName.substring(1, fieldName.length());
		return maxFieldName;
	}
	public void setMaxFieldName(String maxFieldName) {
		this.maxFieldName = maxFieldName;
	}
	
}
