package com.ikeepglove.util.http;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 2014-3-2
 * HttpFormCilent.java
 * 
 */

/**
 * @author ben
 * 
 */
public class HttpFormCilent {
	// 请求方法类型
	private static final String GET = "GET";
	private static final String POST = "POST";
	// 请求内容类型（content-type）
	private static final String ENCTYPE_MULTIPART = "multipart/form-data";
	private static final String ENCTYPE_APPLICATION = "application/x-www-form-urlencoded";
	// 默认请求连接超时时间
	private static final int DEFAULT_CONNECTTIMEOUT = 3000;
	// 默认读入超时时间
	private static final int DEFAULT_READTIMEOUT = 5000;

	// 用于文件上传流
	private static final String TWO_HYPHENS = "--";
	private static final String END_LINE = "\r\n";
	private static final String BOUNDARY = "hehe";
	// 读取文件流的默认缓存大小 10KB
	private static final int DEFAULT_BUFFER_SIZE = 10240;

	// 默认请求编码格式
	private static final String ENCODING = "UTF-8";

	// 请求路径
	private String action;
	// 请求方法类型
	private String method = GET;
	// 请求内容类型
	private String enctype = ENCTYPE_APPLICATION;
	// 请求超时
	private int connectTimeout = DEFAULT_CONNECTTIMEOUT;
	// 读入超时
	private int readTimeout = DEFAULT_READTIMEOUT;
	// 保存请求的普通filed
	private Map<String, LinkedList<String>> textFieldMap = new HashMap<String, LinkedList<String>>();
	// 保存上传文件field
	private LinkedList<FileField> fileFieldList = new LinkedList<HttpFormCilent.FileField>();
	//保存header
	private Map<String,String> headers=new HashMap<String, String>();
	/**
	 * @param connectTimeout
	 *            连接超时时间 ms 如果为0 表示无穷大
	 * @param readTimeout
	 *            等待服务器返回时间 ms
	 *            如果超时，执行方法（doGet,doPost,doPostWithFile,doDownload）会抛出java
	 *            .net.SocketTimeoutException异常
	 */
	public HttpFormCilent(int connectTimeout, int readTimeout) {
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
	}

	/**
	 * 使用默认超时时间配置 connectTimeout=3000 readTimeout=5000
	 */
	public HttpFormCilent() {

	}
	public void addHeader(String name,String value){
		headers.put(name, value);
	}
	/**
	 * 添加请求 field 已存在的field，不会覆盖
	 * 
	 * @author ben
	 * @param name
	 *            名称
	 * @param value
	 *            值
	 */
	public void addField(String name, Object value) {
		if (value != null) {
			LinkedList<String> oldValue = textFieldMap.get(name);
			if (oldValue == null) {
				oldValue = new LinkedList<String>();
				textFieldMap.put(name, oldValue);
			}
			oldValue.add(value.toString());

		}
	}

	/**
	 * 添加文件field
	 * 
	 * @author ben
	 * @param name
	 *            属性名称
	 * @param fileName
	 *            文件名称
	 * @param in
	 *            文件流，文件流在上传完毕后会自动关闭。
	 */
	public void addFileField(String name, String fileName, InputStream in) {
		FileField fileField = new FileField(name, fileName, in);
		fileFieldList.add(fileField);
	}

	/**
	 * 构建请求参数
	 * 
	 * @author ben
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private final String createQueryString()
			throws UnsupportedEncodingException {
		if (textFieldMap.size() == 0) {
			return null;
		}
		StringBuilder queryString = new StringBuilder();
		Set<Entry<String, LinkedList<String>>> entrySet = textFieldMap
				.entrySet();
		Entry<String, LinkedList<String>> entry = null;
		LinkedList<String> values = null;
		for (Iterator<Entry<String, LinkedList<String>>> iterator = entrySet
				.iterator(); iterator.hasNext();) {
			entry = iterator.next();
			values = entry.getValue();
			for (String value : values) {
				queryString.append(entry.getKey()).append('=')
						.append(URLEncoder.encode(value, ENCODING)).append('&');
			}
		}
		queryString.deleteCharAt(queryString.length() - 1);
		return queryString.toString();
	}

	private void validateRequest() {

	}

	/**
	 * 执行get方式请求，文件上传属性忽略。 如果发生连接超时，此方法将抛出java.net.SocketTimeoutException
	 * 
	 * @author ben
	 * @param action
	 *            请求地址
	 * @return
	 * @throws IOException
	 *             请求发生异常
	 */
	public final String doGet(String action) throws IOException {
		HttpURLConnection http = null;
		InputStream in = null;
		try {
			String queryString = createQueryString();
			if (queryString != null) {
				if (action.indexOf('?') != -1) {
					if (action.charAt(action.length() - 1) == '&') {
						action = action + queryString;
					} else {
						action = action + "&" + queryString;
					}
				} else {
					action = action + "?" + queryString;
				}
			}
			http = (HttpURLConnection) new URL(action).openConnection();
			http.setRequestMethod(GET);
			http.setReadTimeout(readTimeout);
			http.setConnectTimeout(connectTimeout);
			http.setDefaultUseCaches(false);
			http.setDoInput(true);
			http.setUseCaches(false);
			http.setDoOutput(false);
			//构建请求头
			
			Set<Entry<String,String>> entrySet=this.headers.entrySet();
			Entry<String, String> entry=null;
			for (Iterator<Entry<String,String>> iterator = entrySet.iterator(); iterator.hasNext();) {
				entry=iterator.next();
				http.setRequestProperty(entry.getKey(),entry.getValue());
			}
			http.setRequestProperty("Connection", "keep-alive");
			http.setRequestProperty("Content-Type", ENCTYPE_APPLICATION);
			
			http.connect();
			int respCode = http.getResponseCode();
			int len = http.getContentLength();
			if (HttpURLConnection.HTTP_OK == respCode) {
				in = http.getInputStream();
				return readIt(in, len);
			}
			String errorMessage = "";
			if ((in = http.getErrorStream()) != null) {
				errorMessage = readIt(in, len);
			}
			throw new IOException("请求异常：" + respCode + " "
					+ http.getResponseMessage() + "\r\n" + errorMessage);

		} finally {
			if (in != null)
				in.close();
		}

	}

	public final InputStream doDownload(String url) throws IOException {
		HttpURLConnection http = (HttpURLConnection) new URL(url).openConnection();
		http.setRequestMethod(GET);
		http.setReadTimeout(readTimeout);
		http.setConnectTimeout(connectTimeout);
		http.setDefaultUseCaches(false);
		http.setDoInput(true);
		http.setUseCaches(false);
		http.setDoOutput(false);
		//请求头
		Set<Entry<String,String>> entrySet=this.headers.entrySet();
		Entry<String, String> entry=null;
		for (Iterator<Entry<String,String>> iterator = entrySet.iterator(); iterator.hasNext();) {
			entry=iterator.next();
			http.setRequestProperty(entry.getKey(),entry.getValue());
		}
		http.setRequestProperty("Connection", "keep-alive");
		http.setRequestProperty("Content-Type", ENCTYPE_APPLICATION);
		http.connect();
		int respCode = http.getResponseCode();
		int len = http.getContentLength();
		InputStream in = null;
		if (HttpURLConnection.HTTP_OK == respCode) {
			in = http.getInputStream();
			return in;
		}
		String errorMessage = "";
		if ((in = http.getErrorStream()) != null) {
			errorMessage = readIt(in, len);
		}
		throw new IOException("请求异常：" + respCode + " "
				+ http.getResponseMessage() + "\r\n" + errorMessage);

	}

	/**
	 * post请求，类似get请求
	 * 
	 * @author ben
	 * @param action
	 *            请求地址
	 * @return
	 * @throws IOException
	 */
	public final String doPost(String action) throws IOException {
		HttpURLConnection http = null;
		InputStream in = null;
		OutputStream out = null;
		try {

			http = (HttpURLConnection) new URL(action).openConnection();
			http.setRequestMethod(POST);
			http.setReadTimeout(readTimeout);
			http.setConnectTimeout(connectTimeout);
			http.setDefaultUseCaches(false);
			http.setDoInput(true);
			http.setUseCaches(false);
			http.setDoOutput(true);
			//请求头
			Set<Entry<String,String>> entrySet=this.headers.entrySet();
			Entry<String, String> entry=null;
			for (Iterator<Entry<String,String>> iterator = entrySet.iterator(); iterator.hasNext();) {
				entry=iterator.next();
				http.setRequestProperty(entry.getKey(),entry.getValue());
			}
			http.setRequestProperty("Connection", "keep-alive");
			http.setRequestProperty("Content-Type", ENCTYPE_APPLICATION);

			out = http.getOutputStream();
			String queryString = createQueryString();
			if (queryString != null) {
				out.write(queryString.getBytes(ENCODING));
			}
			out.flush();
			http.connect();

			int respCode = http.getResponseCode();
			int len = http.getContentLength();
			if (HttpURLConnection.HTTP_OK == respCode) {
				in = http.getInputStream();
				return readIt(in, len);
			}
			String errorMessage = "";
			if ((in = http.getErrorStream()) != null) {
				errorMessage = readIt(in, len);
			}
			throw new IOException("请求异常：" + respCode + " "
					+ http.getResponseMessage() + "\r\n" + errorMessage);

		} finally {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
		}

	}

	/**
	 * 包含文件上传的请求
	 * 
	 * @author ben
	 * @param action
	 *            请求地址
	 * @return
	 * @throws IOException
	 */
	public final String doPostWithFile(String action) throws IOException {
		HttpURLConnection http = null;
		// 输入流
		InputStream in = null;
		// 输出流
		OutputStream out = null;
		// 临时文件流
		InputStream tempIn = null;
		try {

			http = (HttpURLConnection) new URL(action).openConnection();
			http.setRequestMethod(POST);
			http.setConnectTimeout(connectTimeout);
			http.setReadTimeout(readTimeout);
			http.setDefaultUseCaches(false);
			http.setDoInput(true);
			http.setDoOutput(true);
			http.setUseCaches(false);
			//请求头
			Set<Entry<String,String>> entrySet=this.headers.entrySet();
			Entry<String, String> entry=null;
			for (Iterator<Entry<String,String>> iterator = entrySet.iterator(); iterator.hasNext();) {
				entry=iterator.next();
				http.setRequestProperty(entry.getKey(),entry.getValue());
			}
			http.setRequestProperty("Connection", "keep-alive");
			http.setRequestProperty("Content-Type", ENCTYPE_MULTIPART
					+ ";boundary=" + BOUNDARY);
			out = http.getOutputStream();
			// 处理普通的属性
			Set<Entry<String, LinkedList<String>>> textFieldEntrySet = textFieldMap
					.entrySet();
			Entry<String, LinkedList<String>> textFieldentry = null;
			LinkedList<String> values = null;
			for (Iterator<Entry<String, LinkedList<String>>> iterator = textFieldEntrySet
					.iterator(); iterator.hasNext();) {
				textFieldentry = iterator.next();
				values = textFieldentry.getValue();
				for (String value : values) {
					out.write((TWO_HYPHENS + BOUNDARY + END_LINE)
							.getBytes(ENCODING));
					out.write(("Content-Disposition: form-data;name=\""
							+ textFieldentry.getKey() + "\"" + END_LINE)
							.getBytes(ENCODING));
					out.write(END_LINE.getBytes(ENCODING));
					out.write(value.getBytes(ENCODING));
					out.write(END_LINE.getBytes(ENCODING));
				}
			}
			out.flush();
			// 处理文件上传
			LinkedList<FileField> fileFields = fileFieldList;
			// 缓冲
			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			// 已读取长度
			int readed = 0;

			for (FileField fileField : fileFields) {
				out.write((TWO_HYPHENS + BOUNDARY + END_LINE)
						.getBytes(ENCODING));
				out.write(("Content-Disposition: form-data;" + "name=\""
						+ fileField.name + "\";" + "filename=\""
						+ fileField.fileName + "\"" + END_LINE)
						.getBytes(ENCODING));
				out.write(("Content-Type:" + fileField.content_type + END_LINE)
						.getBytes(ENCODING));
				out.write(END_LINE.getBytes(ENCODING));

				tempIn = fileField.in;
				while ((readed = tempIn.read(buffer)) != -1) {
					out.write(buffer, 0, readed);
					out.flush();
				}
				out.write(END_LINE.getBytes(ENCODING));
				// 关闭文件流
				tempIn.close();
			}
			out.write((TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + END_LINE)
					.getBytes(ENCODING));
			out.flush();
			http.connect();
			int respCode = http.getResponseCode();
			int len = http.getContentLength();
			if (HttpURLConnection.HTTP_OK == respCode) {
				in = http.getInputStream();
				return readIt(in, len);
			}
			String errorMessage = "";
			if ((in = http.getErrorStream()) != null) {
				errorMessage = readIt(in, len);
			}
			throw new IOException("请求异常：" + respCode + " "
					+ http.getResponseMessage() + "\r\n" + errorMessage);
		} finally {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
			if (tempIn != null) {
				tempIn.close();
			}
		}

	}

	/**
	 * 转换流为字符串
	 * 
	 * @author ben
	 * @param in
	 *            输入流
	 * @param len
	 *            长度
	 * @return
	 * @throws IOException
	 */
	private static String readIt(InputStream in, int len) throws IOException {
		StringBuilder result = new StringBuilder(len > 0 ? len / 8 : 1024);
		Reader reader = null;
		reader = new InputStreamReader(in, "UTF-8");
		char[] buffer = new char[1024];
		while ((len = reader.read(buffer)) != -1) {
			result.append(new String(buffer, 0, len));
		}
		return result.toString();
	}

	private static boolean isNone(String str) {
		return str == null || str.trim().length() == 0;
	}

	private static class FileField {
		// 属性名称
		public final String name;
		// 文件名
		public final String fileName;
		// 文档类型
		public final String content_type;
		// 文件流
		public final InputStream in;

		/**
		 * @param name
		 * @param path
		 * @param content_type
		 * @param in
		 */
		public FileField(String name, String fileName, InputStream in) {
			if (isNone(name)) {
				throw new IllegalArgumentException("属性名称[name]不可为空");
			}
			if (isNone(fileName)) {
				throw new IllegalArgumentException("文件名[fileName]不可为空");
			}
			if (in == null) {
				throw new IllegalArgumentException("文件流不可为空");
			}
			this.name = name;
			this.fileName = fileName;
			// 目前全部简单处理为2进制流
			this.content_type = "application/octet-stream";
			this.in = in;
		}

	}

	/**
	 * 获取请求超时时间
	 * 
	 * @author ben
	 * @return
	 */
	public int getConnectTimeout() {
		return connectTimeout;
	}

	/**
	 * 设置请求超时时间
	 * 
	 * @author ben
	 * @param connectTimeout
	 */
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	/**
	 * 获取写入超时时间
	 * 
	 * @author ben
	 * @return
	 */
	public int getReadTimeout() {
		return readTimeout;
	}

	/**
	 * 设置写入超时时间
	 * 
	 * @author ben
	 * @param readTimeout
	 */
	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

}
