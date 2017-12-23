package com.guanxun.idGengerator;

public interface IdGenerator {

	public long generate(String tableName, String columnName);

	public long generate(String tableName, String columnName, int onceGenSize);

	public String genSid();

	public Long genLid();
}
