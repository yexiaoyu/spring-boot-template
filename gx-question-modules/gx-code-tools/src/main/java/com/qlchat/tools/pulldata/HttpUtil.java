package com.qlchat.tools.pulldata;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class HttpUtil {

	private static Logger log = LoggerFactory.getLogger(HttpUtil.class);
	
	private static int bufferSize= 1024;

	private static volatile HttpUtil instance;
	
	private ConnectionConfig connConfig;

	private SocketConfig socketConfig;

	private ConnectionSocketFactory plainSF;

	private KeyStore trustStore;

	private SSLContext sslContext;

	private LayeredConnectionSocketFactory sslSF;

	private Registry<ConnectionSocketFactory> registry;

	private PoolingHttpClientConnectionManager connManager;

	private volatile HttpClient client;

	public static String defaultEncoding = "UTF-8";

	private static List<NameValuePair> paramsConverter(Map<String, String> params) {
		List<NameValuePair> nvps = new LinkedList<NameValuePair>();
		Set<Entry<String, String>> paramsSet = params.entrySet();
		for (Entry<String, String> paramEntry : paramsSet) {
			nvps.add(new BasicNameValuePair(paramEntry.getKey(), paramEntry.getValue()));
		}
		return nvps;
	}

	public static String readStream(InputStream in, String encoding) {
		if (in == null){
			return null;
		}
		try {
			InputStreamReader inReader = null;
			if (encoding == null){
				inReader = new InputStreamReader(in, defaultEncoding);
			}else{
				inReader = new InputStreamReader(in, encoding);
			}
			char[] buffer = new char[bufferSize];
			int readLen = 0;
			StringBuffer sb = new StringBuffer();
			while((readLen = inReader.read(buffer)) != -1){
				sb.append(buffer, 0, readLen);
			}
			inReader.close();
			return sb.toString();
		} catch (IOException e) {
			log.error("读取返回内容出错", e);
		}
		return null;
	}

	private HttpUtil(){
		//设置连接参数
		connConfig = ConnectionConfig.custom().setCharset(Charset.forName(defaultEncoding)).build();
		socketConfig = SocketConfig.custom().setSoTimeout(5000).build();
		RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();
		plainSF = new PlainConnectionSocketFactory();
		registryBuilder.register("http", plainSF);
		//指定信任密钥存储对象和连接套接字工厂
		try {
			trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, new AnyTrustStrategy()).build();
			sslSF = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			registryBuilder.register("https", sslSF);
		} catch (KeyStoreException e) {
			throw new RuntimeException(e);
		} catch (KeyManagementException e) {
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		registry = registryBuilder.build();
		//设置连接管理器
		connManager = new PoolingHttpClientConnectionManager(registry);
		connManager.setDefaultConnectionConfig(connConfig);
		connManager.setDefaultSocketConfig(socketConfig);
		//构建客户端
		client= HttpClientBuilder.create().setConnectionManager(connManager).build();
	}

	public static HttpUtil getInstance(){
		synchronized (HttpUtil.class) {
			if (HttpUtil.instance == null){
				instance = new HttpUtil();
			}
			return instance;
		}
	}

	public InputStream doGet(String url) throws URISyntaxException, ClientProtocolException, IOException{
		HttpResponse response = this.doGet(url, null);
		return response != null ? response.getEntity().getContent() : null;
	}

	public String doGetForString(String url){
		try {
			String result =  HttpUtil.readStream(this.doGet(url), null);
			return result;
		} catch (Exception e) {
			log.error("GET异常" + url,e);
		}
		return null;
	}

	private InputStream doGetForStream(String url, Map<String, String> queryParams) throws URISyntaxException, ClientProtocolException, IOException{
		HttpResponse response = this.doGet(url, queryParams);
		return response != null ? response.getEntity().getContent() : null;
	}

	public String doGetForString(String url, Map<String, String> queryParams){
		try {
			String result = HttpUtil.readStream(this.doGetForStream(url, queryParams), null);
			return result;
		} catch (Exception e) {
			log.error("GET异常" + url,e);
		}
		return null;
	}

	/**
	 * 基本的Get请求
	 * @param url 请求url
	 * @param queryParams 请求头的查询参数
	 * @return
	 * @throws URISyntaxException 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	private HttpResponse doGet(String url, Map<String, String> queryParams) throws URISyntaxException, ClientProtocolException, IOException {
		HttpGet gm = new HttpGet();
		URIBuilder builder = new URIBuilder(url);
		//填入查询参数
		if (queryParams != null && !queryParams.isEmpty()) {
			builder.setParameters(HttpUtil.paramsConverter(queryParams));
		}
		//请求头设置，特别是cookie设置
		gm.addHeader("Accept", "text/html, application/xhtml+xml, */*");
		gm.addHeader("Content-Type", "application/x-www-form-urlencoded");
		gm.addHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0))");
		String cookie = "HqNL_ef65_saltkey=xAfETZMO; HqNL_ef65_lastvisit=1516019841; _csrf=e1b65184f4f07b2833d346eab9457fb555f9465a2b093c703dfbfa03bd45aa96a%3A2%3A%7Bi%3A0%3Bs%3A5%3A%22_csrf%22%3Bi%3A1%3Bs%3A32%3A%22abvbrpxnPYJaNUSetf7Lz795fuxe4V-2%22%3B%7D; chid=bd8313dfafcee0d97949afd2be099a6e5a44437eef1f94d21b0a7c33a4321f98a%3A2%3A%7Bi%3A0%3Bs%3A4%3A%22chid%22%3Bi%3A1%3Bs%3A1%3A%224%22%3B%7D; xd=bbc5bced806f427ff9aca7cf1cf56a37c9016539f1da160ae385126fb0d1cc2fa%3A2%3A%7Bi%3A0%3Bs%3A2%3A%22xd%22%3Bi%3A1%3Bs%3A1%3A%223%22%3B%7D; bookversion_paper=4402293cf304fa3622da6b6fa04af775e3fe5252600db238e573dc06440b8955a%3A2%3A%7Bi%3A0%3Bs%3A17%3A%22bookversion_paper%22%3Bi%3A1%3Ba%3A1%3A%7Bi%3A4%3Ba%3A1%3A%7Bi%3A3%3Bs%3A5%3A%2211356%22%3B%7D%7D%7D; nianji_paper=df055cb3646a3f40328511ee7b7ca80d2a5b9f852d147b9dcbbc65a144b55b71a%3A2%3A%7Bi%3A0%3Bs%3A12%3A%22nianji_paper%22%3Bi%3A1%3Ba%3A1%3A%7Bi%3A4%3Ba%3A1%3A%7Bi%3A3%3Bs%3A4%3A%221992%22%3B%7D%7D%7D; Hm_lvt_5d70f3704df08b4bfedf4a7c4fb415ef=1515205926,1516023478,1516371723,1516759609; Hm_lpvt_5d70f3704df08b4bfedf4a7c4fb415ef=1516765751";
		gm.addHeader("Cookie", cookie);
		gm.setURI(builder.build());
		return client.execute(gm);
	}

	private InputStream doPostForStream(String url, Map<String, String> queryParams) throws URISyntaxException, ClientProtocolException, IOException {
		HttpResponse response = this.doPost(url, queryParams, null, null);
		return response != null ? response.getEntity().getContent() : null;
	}

	public String doPostForString(String url, Map<String, String> queryParams){
		try {
			String result = HttpUtil.readStream(this.doPostForStream(url, queryParams), null);
			return result;
		} catch (Exception e) {
			log.error("POST异常" + url,e);
		}
		return null;
	}

	private InputStream doPostForStream(String url, Map<String, String> queryParams, Map<String, String> formParams) throws URISyntaxException, ClientProtocolException, IOException{
		HttpResponse response = this.doPost(url, queryParams, formParams, null);
		return response != null ? response.getEntity().getContent() : null;
	}

	public String doPostForString(String url, Map<String, String> queryParams, Map<String, String> formParams){
		try {
			String result = HttpUtil.readStream(this.doPostForStream(url, queryParams, formParams), null);
			return result;
		} catch (Exception e) {
			log.error("POST异常" + url,e);
		}
		return null;
	}

	public InputStream doPostForStream(String url, Map<String, String> queryParams, String postBody) throws URISyntaxException, ClientProtocolException, IOException{
		HttpResponse response = this.doPost(url, queryParams, null, postBody);
		return response != null ? response.getEntity().getContent() : null;
	}

	public String doPostForString(String url, Map<String, String> queryParams, String postBody){
		try {
			String result = HttpUtil.readStream(this.doPostForStream(url, queryParams, postBody), null);
			return result;
		} catch (Exception e) {
			log.error("POST异常" + url,e);
		}
		return null;
	}

	/**
	 * 基本的Post请求
	 * @param url
	 * @param queryParams
	 * @param formParams
	 * @param postBody
	 * @return
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private HttpResponse doPost(String url, Map<String, String> queryParams, Map<String, String> formParams, String postBody) throws URISyntaxException, ClientProtocolException, IOException{
		HttpPost pm = new HttpPost();
		URIBuilder builder = new URIBuilder(url);
		//填入查询参数
		if (queryParams != null && !queryParams.isEmpty()) {
			builder.setParameters(HttpUtil.paramsConverter(queryParams));
		}
		pm.setURI(builder.build());

		//填入表单参数
		if (formParams != null && !formParams.isEmpty()) {
			pm.setEntity(new UrlEncodedFormEntity(HttpUtil.paramsConverter(formParams)));
		}

		//填入消息体
		if (postBody != null) {
			pm.setEntity(new StringEntity(postBody, defaultEncoding));
		}
        pm.setHeader("Content-Type", "application/json");
		return client.execute(pm);
	}

	/**
	 * 多块Post请求
	 * @param url 请求url
	 * @param queryParams 请求头的查询参数
	 * @param formParts post表单的参数,支持字符串-文件(FilePart)和字符串-字符串(StringPart)形式的参数
	 * @return
	 * @throws URISyntaxException 
	 * @throws ClientProtocolException 
	 * @throws HttpException
	 * @throws IOException
	 */
	private HttpResponse multipartPost(String url, Map<String, String> queryParams, List<FormBodyPart> formParts) throws URISyntaxException, ClientProtocolException, IOException{
		HttpPost pm= new HttpPost();
		URIBuilder builder = new URIBuilder(url);
		//填入查询参数
		if (queryParams!=null && !queryParams.isEmpty()) {
			builder.setParameters(HttpUtil.paramsConverter(queryParams));
		}
		pm.setURI(builder.build());
		//填入表单参数
		if (formParts != null && !formParts.isEmpty()) {
			MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
			entityBuilder = entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			for (FormBodyPart formPart : formParts) {
				entityBuilder = entityBuilder.addPart(formPart.getName(), formPart.getBody());
			}
			pm.setEntity(entityBuilder.build());
		}
		return client.execute(pm);
	}

    public String doPostJson(String url, String postBody){
        try {
            HttpPost pm = new HttpPost();
            URIBuilder builder = new URIBuilder(url);

            pm.setURI(builder.build());

            //填入消息体
            if (postBody != null) {
                pm.setEntity(new StringEntity(postBody, defaultEncoding));
            }
            pm.setHeader("Content-Type", "application/json");
            HttpResponse response = client.execute(pm);
            if(response != null){
                return HttpUtil.readStream(response.getEntity().getContent(), null);
            }
        } catch (Exception e) {
            log.error("POST异常" + url,e);
        }
        return null;
    }

	class AnyTrustStrategy implements TrustStrategy {
		@Override
		public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			return true;
		}
	}

	public static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
		}
		return output.toByteArray();
	}
}
