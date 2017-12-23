/**
 * 
 */
package com.guanxun.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author zhangk 工具类
 */
public class GxUtils {
	private static final Logger logger = LoggerFactory.getLogger(GxUtils.class);


	/**
	 * 随机从list里面取出来n个元素
	 * @param list
	 * @param n
	 * @return
	 */
	public static List createRandomList(List list, int n) {
		if (list == null || list.size() <= n) {
			return list;
		} else {
			List tempList = new ArrayList();
			List result = new ArrayList();
			tempList.addAll(list);
			while (result.size() < n && tempList.size() != 0){
				int random = (int) (Math.random() * tempList.size());
				result.add(tempList.get(random));
				tempList.remove(random);
			}
			return result;
		}
	}

	public static boolean isEmpty(List<?> list) {

		if (list == null || list.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean isEmpty(Set<?> set) {
		if (set == null || set.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean isEmpty(Long number) {
		if (number == null) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isEmpty(Object[] arr) {
		if (arr == null || arr.length==0) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str) || "null".equals(str) || "undefined".equals(str)) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean isEmpty(Integer number) {
		if (number == null) {
			return true;
		} else {
			return false;
		}
	}
	/** 
	 * @Title: isEmpty 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param @param configMap
	 * @param @return    设定文件 
	 * @return boolean    返回类型 
	 * @throws 
	 */ 
	public static boolean isEmpty(Map<?, ?> map) {
		if (map == null || map.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean isNotEmpty(List<?> list){
		return !isEmpty(list);
	}
	public static boolean isNotEmpty(Set<?> set){
		return !isEmpty(set);
	}
	public static boolean isNotEmpty(Object[] arr){
		return !isEmpty(arr);
	}
	public static boolean isNotEmpty(String str){
		return !isEmpty(str);
	}
	public static boolean isNotEmpty(Integer number){
		return !isEmpty(number);
	}
	public static boolean isNotEmpty(Long number){
		return !isEmpty(number);
	}
	public static boolean isNotEmpty(Map<?, ?> map){
		return !isEmpty(map);
	}
	public static char getRandomChar(){
        char[] code = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's',
                't', 'u', 'v', 'w', 'x', 'y', 'z'};
        //0<=return<=25
        int result = (int)(Math.random() * code.length);
        return code[result];
    }
    public static char getRandomInteger(){
        char[] code = {'2', '3', '4', '5', '6', '7', '8', '9'};
        //0<=return<=7
        int result = (int)(Math.random() * code.length);
        return code[result];
    }
    
  //拿请求ip地址， 通过反向代理后，会加ip地址，只有拿第一个ip才是正确的，但不是绝对100%
  	public static String getConnectIps(HttpServletRequest request) {
  		String ip = request.getHeader("x-forwarded-for");
  		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
  			ip = request.getHeader("Proxy-Client-IP");
  		}
  		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
  			ip = request.getHeader("WL-Proxy-Client-IP");
  		}
  		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
  			ip = request.getRemoteAddr();
  		}
  		return ip;
  	}
  	
  	
  	/**
	 * 
	 * @Title: 代替 tab符 和 换行符 ，主要用于日志记录时；
	 * @param content
	 * @return    
	 * @return String
	 */
	public static String replaceTabAndLine(String content){
		if (content != null) {
			char newLineChar = 0x0A;// 换行符ASC码
			content = content.replaceAll(newLineChar + "", " ");// 去掉换行符
			content = content.replaceAll("\r", " "); // 去掉换行符
			content = content.replaceAll("\n", " "); // 去掉换行符
			content = content.replaceAll("\t", " "); // 去掉 tab符
		}
		return content;
	}

	/**
     * 获取两个时间相差,在秒级别内输出秒;在分钟内输出分钟;在小时内输出小时
     * <1000ms 算1s; 59.6s算1min; 59.6min算1h
     * @param minDate
     * @param maxDate
     * @return
     */
    public static String getBetweenTimeStr(Date minDate, Date maxDate){
        String finish = "";
        long tms = maxDate.getTime() - minDate.getTime();//ms
        long oneDay = 24*60*60*1000;
        if(tms <= 1000){
            finish = "1秒";
        }else if(tms <= oneDay){
            long ts = tms/1000;
            if(ts >= 60){//超过1min
                long tmin = ts / 60;
                if(tmin >= 60){//超过1h
                    long th = tmin / 60;
                    if(tmin % 60 > 0 && th < 24){
                        finish = (th+1)+"小时";
                    }else {
                        finish = (th)+"小时";
                    }
                }else{
                    if(ts % 60 > 0){
                        if(tmin < 59){
                            finish = (tmin + 1)+"分钟";
                        }else{
                            finish = "1小时";
                        }
                    }else{
                        finish = (tmin)+"分钟";
                    }
                }
            }else{
                if(tms % 1000 > 0){
                    if(ts < 59){
                        finish = (ts + 1)+"秒";
                    }else{
                        finish = "分钟";
                    }
                }else{
                    finish = (ts)+"秒";
                }
            }
        }else{
        	long ts = tms/oneDay;
        	finish = ts + "天";
        }
        return finish;
    }

    public static String getSerialNum() {
		Random r = new Random();
		int num = 10000 + r.nextInt(90000);
		Date d = new Date();
		String s = d.getTime() + "";
		return s + num;
	}

    /**
	 * 获得指定长度,随机字符串, size为长度
	 * 去掉i l 1
	 **/
	public static String getRandomCode(int size) {
		String sourcesChar = "abcdefghjkmnopqrstuvwxyz023456789";
		int sourcesCharLength = sourcesChar.length();
		Random rand = new Random();
		StringBuilder randomChar = new StringBuilder(size);
		for (int i = 0; i < size; i++) {
			randomChar.append(sourcesChar.charAt(rand.nextInt(sourcesCharLength)));
		}
		return randomChar.toString();
	}

    /**
	 *
	 * @Title: filterSpecialCharts
	 * @Description: TODO(过滤特殊字符)
	 * @return
	 * @throws PatternSyntaxException    设定文件
	 * @return String    返回类型
	 * @throws
	 */
	public static String filterSpecialCharts(String real,String replacement) throws PatternSyntaxException {
		if(real == null){
			return "";
		}
		// 只允许字母和数字
		// String regEx = "[^a-zA-Z0-9]";
		// 清除掉所有特殊字符
		String rCharts = "";
		if(isNotEmpty(replacement)){
			rCharts = replacement;
		}
		//String regEx = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		String regEx = "[`~^*]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(real);
		return m.replaceAll(rCharts).trim();
	}

	private static String linuxLocalIp;
	public static String getLinuxLocalIp() {
		if(GxUtils.isNotEmpty(linuxLocalIp)){
			return linuxLocalIp;
		}
		String innerIp = "";
		//String outIp = "";
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				String name = intf.getName();
				if (!name.contains("docker") && !name.contains("lo")) {
					for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
						InetAddress inetAddress = enumIpAddr.nextElement();

						if (!inetAddress.isLoopbackAddress()) {
							String ipaddress = inetAddress.getHostAddress().toString();
							logger.debug("getLinuxLocalIp>>>>>>>>{},{},{}", name, ipaddress, inetAddress.getHostName());
							if (!ipaddress.contains("::") && !ipaddress.contains("0:0:") && !ipaddress.contains("fe80")) {
								if(name.equals("eth0")){
									innerIp = ipaddress;
									linuxLocalIp = ipaddress;
								}/*else if(name.equals("eth1")){
									outIp = ipaddress;
								}*/
							}
						}
					}
				}
			}
		} catch (SocketException ex) {
			innerIp = "127.0.0.1";
			ex.printStackTrace();
		}
		return innerIp;
	}

	public static String getHostName(){
        try {
            return (InetAddress.getLocalHost()).getHostName();
        } catch (UnknownHostException uhe) {
            String host = uhe.getMessage(); // host = "hostname: hostname"
            if (host != null) {
                int colon = host.indexOf(':');
                if (colon > 0) {
                    return host.substring(0, colon);
                }
            }
            return "UnknownHost";
        }
    }

	public static List<Map.Entry<String,Integer>> getDescSortedListByMapValue(Map<String,Integer> map) {
		List<Map.Entry<String,Integer>> entryArrayList = new ArrayList<Map.Entry<String,Integer>>(map.entrySet());
		Collections.sort(entryArrayList, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return o2.getValue() - o1.getValue();
			}
		});
		return entryArrayList;
	}

	public static List<Map.Entry<String,Long>> getDescSortedListByMapValueLong(Map<String,Long> map) {
		List<Map.Entry<String,Long>> entryArrayList = new ArrayList<Map.Entry<String,Long>>(map.entrySet());
		Collections.sort(entryArrayList, new Comparator<Map.Entry<String, Long>>() {
			public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
				Long l = o2.getValue() - o1.getValue();
				return l.intValue();
			}
		});
		return entryArrayList;
	}

	public static String replaceUnicode(String str) {
		return str.replaceAll("[\\u007f-\\u009f]|\\u00ad|[\\u0483-\\u0489]|[\\u0559-\\u055a]|\\u058a|[\\u0591-\\u05bd]|\\u05bf|[\\u05c1-\\u05c2]|[\\u05c4-\\u05c7]|[\\u0606-\\u060a]|[\\u063b-\\u063f]|\\u0674|[\\u06e5-\\u06e6]|\\u070f|[\\u076e-\\u077f]|\\u0a51|\\u0a75|\\u0b44|[\\u0b62-\\u0b63]|[\\u0c62-\\u0c63]|[\\u0ce2-\\u0ce3]|[\\u0d62-\\u0d63]|\\u135f|[\\u200b-\\u200f]|[\\u2028-\\u202e]|\\u2044|\\u2071|[\\uf701-\\uf70e]|[\\uf710-\\uf71a]|\\ufb1e|[\\ufc5e-\\ufc62]", "");
	}

	public static String addUrlParam(String url,String param,String value){
		if(GxUtils.isEmpty(url)|| GxUtils.isEmpty(param)|| GxUtils.isEmpty(value)){
			return url;
		}
		Map<String,String> paramsMap = urlRequest(url);
		if(GxUtils.isEmpty(paramsMap)){
			url = url.replaceAll("[?]","");
			return  url +"?"+ param +"="+value;
		}
		if(paramsMap.containsKey(param)){
			return url;
		}
		return url + "&" + param + "=" + value;
	}
	/**
	 * 去掉url中的路径，留下请求参数部分
	 *
	 * @param strURL url地址
	 * @return url请求参数部分
	 */
	private static String truncateUrlPage(String strURL) {
		String strAllParam = null;
		String[] arrSplit = null;
		strURL = strURL.trim().toLowerCase();
		arrSplit = strURL.split("[?]");
		if (strURL.length() > 1) {
			if (arrSplit.length > 1) {
				if (arrSplit[1] != null) {
					strAllParam = arrSplit[1];
				}
			}
		}
		return strAllParam;
	}

	/**
	 * 解析出url参数中的键值对
	 * 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
	 *
	 * @param url url地址
	 * @return url请求参数部分
	 */
	public static Map<String, String> urlRequest(String url) {
		Map<String, String> mapRequest = new HashMap<String, String>();
		String[] arrSplit = null;
		String strUrlParam = truncateUrlPage(url);
		if (strUrlParam == null) {
			return mapRequest;
		}
		//每个键值为一组
		arrSplit = strUrlParam.split("[&]");
		for (String strSplit : arrSplit) {
			String[] arrSplitEqual = null;
			arrSplitEqual = strSplit.split("[=]");
			//解析出键值
			if (arrSplitEqual.length > 1) {
				//正确解析
				mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
			} else {
				if (arrSplitEqual[0] != "") {
					//只有参数没有值，不加入
					mapRequest.put(arrSplitEqual[0], "");
				}
			}
		}
		return mapRequest;
	}


	/**
	 * 删除html标签
	 * @param htmlStr
	 * @return
	 */
	public static String delHTMLTag(String htmlStr){
		String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
		String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
		String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式

		Pattern p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);
		Matcher m_script=p_script.matcher(htmlStr);
		htmlStr=m_script.replaceAll(""); //过滤script标签

		Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
		Matcher m_style=p_style.matcher(htmlStr);
		htmlStr=m_style.replaceAll(""); //过滤style标签

		Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
		Matcher m_html=p_html.matcher(htmlStr);
		htmlStr=m_html.replaceAll(""); //过滤html标签

		return htmlStr.trim(); //返回文本字符串
	}
}
