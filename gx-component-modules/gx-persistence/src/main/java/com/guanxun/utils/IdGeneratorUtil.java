package com.guanxun.utils;

import com.guanxun.hepler.SpringHelper;
import com.guanxun.idGengerator.IdGenerator;

/**
 * 
 * @ClassName: IConfigUtil 
 * @Description: TODO(获取配置文件) 
 * @author zhangk
 * @date 2016年11月18日 下午8:57:55 
 *
 */
public class IdGeneratorUtil {

	public final static IdGenerator idGenerator = SpringHelper.getBean(IdGenerator.class);

	/**
	 * 
	 * @Title: getGenSid 
	 * @Description: TODO(获取String唯一ID) 
	 * @return    设定文件 
	 * @return String    返回类型 
	 * @throws
	 */
	public static String getGenSid() {
		return idGenerator.genSid();
	}
	/**
	 * 
	 * @Title: getGenSid 
	 * @Description: TODO(获取Long唯一ID) 
	 * @return    设定文件 
	 * @return String    返回类型 
	 * @throws
	 */
	public static Long getLongGenSid() {
		return idGenerator.genLid();
	}
}
