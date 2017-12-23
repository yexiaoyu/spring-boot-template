package com.qlchat.tools;


import org.apache.velocity.VelocityContext;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 
 * <br>
 * <b>功能：</b>生成代码<br>
 * <b>作者：</b>张凯<br>
 * <b>日期：</b> 2016-8-16 <br>
 * <b>版权所有：<b>版权所有(C)<br>
 */
public class CreateJava {
    public static String schema;
    public static String model; 
    public static String createAdminFind; 
    public static List<String> doTypeList; 
    public static String tableComment;//手动写表名
    public static String workSpaceUrl; 
    
    static{
		workSpaceUrl = "/Users/kai.zhang/IdeaProjects/spring-boot-template/";//配置工作空间
		schema = "question";// 数据库名字
    	doTypeList = new ArrayList<>();//需要生成哪些代码
    	doTypeList.add("model");
        doTypeList.add("mapper");
		doTypeList.add("provider");
		doTypeList.add("service");
    	//doTypeList.add("controller");//admin的controller
    	//doTypeList.add("view"); //admin的js，ftl
    }
    
	public static void main(String[] args) {
		String url = "jdbc:mysql://127.0.0.1:3306/" + schema + "?characterEncoding=UTF-8";
		String username = "root";
		String passWord = "root123";
		CreateBean createBean = new CreateBean();
		createBean.setMysqlInfo(url, username, passWord);
		/* key=表名，value=表注释 */
		Map<String, String> tableMap = new HashMap<String, String>();
		tableMap.put("gx_keygen", "自媒体版直播间转载话题中间表");

		for (Map.Entry<String, String> entry : tableMap.entrySet()) {
			tableComment = entry.getValue();
			create(entry.getKey(), "C");// type 是支持可以删除已生成的文件：C=生成，D=删除
		}
	}
	
	public static void create(String tableName, String type) {
		
		CreateBean createBean = new CreateBean();

//		int qianzhui = 9;
		int qianzhui = tableName.indexOf("_")+1;
		String className = createBean.getTablesNameToClassName(tableName.substring(qianzhui));

		String lowerName = className.substring(0, 1).toLowerCase()
				+ className.substring(1, className.length());

		String modelPackage = "com.guanxun.model";

		String modelBasePath = workSpaceUrl + "/gx-question-modules/gx-question-model/src/main/java/com/guanxun/model/";
		String mapperBasePath = workSpaceUrl + "/gx-question-modules/gx-question-model/src/main/java/com/guanxun/mapper/";
		String providerBasePath = workSpaceUrl + "/gx-question-modules/gx-question-model/src/main/java/com/guanxun/provider/";
		String serviceBasePath = workSpaceUrl + "/gx-question-modules/gx-question-service/src/main/java/com/guanxun/service/";
//		String controllerBasePath = workSpaceUrl + "/skg-app-root/modules/app-admin/src/main/java/com/qlchat/"+model+"/";
		

		VelocityContext context = new VelocityContext();
		context.put("className", className); //
		context.put("lowerName", lowerName);
		context.put("tableComment", tableComment);
		context.put("tableName", tableName);
		context.put("modelPackage", modelPackage);
		context.put("model", model);
		context.put("createAdminFind", createAdminFind);
		try {
			List<ColumnData> columnDatas = createBean.getColumnDatas(schema, tableName);
			context.put("columnDatas", columnDatas); // 生成bean
			context.put("pk", createBean);
			String columnName = "";
			for(ColumnData columnData : columnDatas){
				columnName += columnData.getColumnName() + ",";
			}
			context.put("columnName", columnName.substring(0, columnName.length() - 1));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//-------------------生成文件代码---------------------/
		for(String doType : doTypeList){
			if("model".equals(doType)){
				String modelName = className + ".java";
				if("D".equals(type)){
					deleteFile(modelBasePath, modelName);
				}else{
					CommonPageParser.WriterPage(context, "TempTbl.java", modelBasePath, modelName);
				}
			}else if("mapper".equals(doType)){
				String mapperName = className + "Mapper.java";
				if("D".equals(type)){
					deleteFile(mapperBasePath, mapperName);
				}else{
					CommonPageParser.WriterPage(context, "TempMapper.java", mapperBasePath, mapperName);
				}
			}else if("provider".equals(doType)){
				String modelName = className + "Provider.java";
				if("D".equals(type)){
					deleteFile(providerBasePath, modelName);
				}else{
					CommonPageParser.WriterPage(context, "TempProvider.java", providerBasePath, modelName);
				}
			}else if("service".equals(doType)){
				String modelName = className + "Service.java";
				if("D".equals(type)){
					deleteFile(serviceBasePath, modelName);
				}else{
					CommonPageParser.WriterPage(context, "TempService.java", serviceBasePath, modelName);
				}
			}


		}
	}
	
	public static void deleteFile(String basePath ,String filePath){
		File file = new File(basePath+filePath); 
		if(file.exists()){
			System.out.println("删除文件"+file.getAbsolutePath());
			file.delete();
		}
	}
	/**
	 * 获取项目的路径
	 * @return
	 */
	public static String getRootPath(){
		String rootPath ="";
		try{
			 File file = new File(CommonPageParser.class.getResource("/").getFile());
			//web项目使用下面这个！ 因为web类库地址 WebRoot\WEB-INF\classes
			rootPath = file.getParentFile().getParentFile().getAbsolutePath()+"/";
			//普通java项目使用这个！
			//rootPath = file.getParentFile().getAbsolutePath()+"/";
			rootPath = java.net.URLDecoder.decode(rootPath,"utf-8");
			return rootPath;
		}catch(Exception e){
			e.printStackTrace();
		}
		return rootPath;
	}
}
