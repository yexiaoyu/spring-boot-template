package com.guanxun.model;

import com.alibaba.fastjson.JSON;
import java.io.*;

public class Keygen implements Serializable {
	
	protected String tableName; 		/**/
	protected Long lastUsedId; 		/**/

	/*为了兼容主键不是ID时，create方法自动生成主键注入*/
	public void setId(String tableName) {
		this.tableName = tableName;
	}
	public String getId() {
		return tableName;
	}	
	public void setTableName(String tableName){
		this.tableName = tableName;
	}
	/***/
	public String getTableName(){
		return this.tableName;
	}
	public void setLastUsedId(Long lastUsedId){
		this.lastUsedId = lastUsedId;
	}
	/***/
	public Long getLastUsedId(){
		return this.lastUsedId;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
