package com.sinoparasoft.storage.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;

/**
 * HTTP客户端工具类
 *
 * @author 袁涛
 * @date 2018.08.31
 */
public class HttpClientUtils {
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

	/**
	 * 发送HTTP GET请求
	 *
	 * @param urlPath
	 *            访问URL地址
	 * @param properties
	 *            请求参数
	 * @return HTTP响应
	 */
	public static String doGet(String urlPath, Properties properties) {
		return HttpClientUtils.doRequest(urlPath, properties, "GET");
	}

	/**
	 * 发送HTTP GET请求
	 *
	 * @param urlPath
	 *            请求地址
	 * @return 请求响应
	 */
	public static String doGet(String urlPath) {
		return HttpClientUtils.doRequest(urlPath, "GET");
	}

	/**
	 * 发送HTTP POST请求
	 *
	 * @param urlPath
	 *            访问URL地址
	 * @param properties
	 *            请求参数
	 * @return HTTP响应
	 */
	public static String doPost(String urlPath, Properties properties) {
		return HttpClientUtils.doRequest(urlPath, properties, "POST");
	}

	/**
	 * 发送HTTP POST请求
	 *
	 * @param urlPath
	 *            请求地址
	 * @return 请求响应
	 */
	public static String doPost(String urlPath) {
		return HttpClientUtils.doRequest(urlPath, "POST");
	}

	/**
	 * 发送HTTP PUT请求
	 *
	 * @param urlPath
	 *            访问URL地址
	 * @param properties
	 *            请求参数
	 * @return HTTP响应
	 */
	public static String doPut(String urlPath, Properties properties) {
		return HttpClientUtils.doRequest(urlPath, properties, "PUT");
	}

	/**
	 * 发送HTTP PUT请求
	 *
	 * @param urlPath
	 *            请求地址
	 * @return 请求响应
	 */
	public static String doPut(String urlPath) {
		return HttpClientUtils.doRequest(urlPath, "PUT");
	}

	/**
	 * 发送HTTP DELETE请求
	 *
	 * @param urlPath
	 *            访问URL地址
	 * @param properties
	 *            请求参数
	 * @return HTTP响应
	 */
	public static String doDelete(String urlPath, Properties properties) {
		return HttpClientUtils.doRequest(urlPath, properties, "DELETE");
	}

	/**
	 * 发送HTTP DELETE请求
	 *
	 * @param urlPath
	 *            请求地址
	 * @return 请求响应
	 */
	public static String doDelete(String urlPath) {
		return HttpClientUtils.doRequest(urlPath, "DELETE");
	}

	/**
	 * 发送HTTP请求
	 *
	 * @param urlPath
	 *            请求地址
	 * @param properties
	 *            请求参数
	 * @param method
	 *            请求方法
	 * @return 请求响应
	 */
	public static String doRequest(String urlPath, Properties properties, String method) {
		String retValue = null;

		// 检查参数
		if (StringUtils.isBlank(urlPath)) {
			String message = "url path is blank.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		if (StringUtils.isBlank(method)) {
			String message = "method is blank.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		// 请求参数类型转换
		String parameters = HttpClientUtils.getRequestParameters(properties);
		// 生成最终访问地址
		urlPath = HttpClientUtils.getRequestUrl(urlPath, parameters, method);

		HttpURLConnection httpURLConnection = null;
		try {
			// 打开HTTP连接
			URL url = new URL(urlPath);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod(method);
			if ("POST".equals(method)) {
				if (!parameters.isEmpty()) {
					httpURLConnection.setDoOutput(true);
					OutputStreamWriter out = new OutputStreamWriter(httpURLConnection.getOutputStream());
					BufferedWriter writer = new BufferedWriter(out);
					// 添加参数到目标服务器
					writer.write(parameters);
					writer.flush();
					writer.close();
					out.close();
				}
			}
			// 读取响应
			if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				StringBuffer content = new StringBuffer();
				String line = "";
				BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
				while ((line = in.readLine()) != null) {
					content.append(line);
				}
				return content.toString();
			} else {
				String message = "send http " + method + " request is failed, url = " + urlPath + ", parameters = "
						+ parameters;
				message += ", response code = " + httpURLConnection.getResponseCode();
				logger.error(message);
				throw new RuntimeException(message);
			}
		} catch (IOException e) {
			String message = "send http " + method + " failed, url = " + urlPath + ", parameters = " + parameters;
			logger.error(message, e);
		} finally {
			try {
				if (httpURLConnection != null) {
					httpURLConnection.disconnect();
				}
			} catch (Exception e) {
				String message = "disconnect http url connection failed, url = " + urlPath + ", parameters = "
						+ parameters;
				logger.error(message, e);
			}
		}

		return retValue;
	}

	/**
	 * 发送HTTP请求
	 *
	 * @param urlPath
	 *            请求地址
	 * @param method
	 *            请求方法
	 * @return 请求响应
	 */
	public static String doRequest(String urlPath, String method) {
		return HttpClientUtils.doRequest(urlPath, null, method);
	}

	/**
	 * 根据访问地址、请求参数和方法获取最终的请求访问地址
	 *
	 * @param urlPath
	 *            访问地址
	 * @param parameters
	 *            请求参数
	 * @param method
	 *            请求方法
	 * @return 最终的请求访问地址
	 */
	public static String getRequestUrl(String urlPath, String parameters, String method) {
		// 请求参数
		if ("GET".equals(method) || "PUT".equals(method) || "DELETE".equals(method)) {
			if (!parameters.isEmpty()) {
				if (urlPath.contains("?")) {
					urlPath += "&" + parameters;
				} else {
					urlPath += "?" + parameters;
				}
			}
		}

		return urlPath;
	}

	/**
	 * 根据访问地址、请求参数和方法获取最终的请求访问地址
	 *
	 * @param urlPath
	 *            访问地址
	 * @param properties
	 *            请求参数
	 * @param method
	 *            请求方法
	 * @return 最终的请求访问地址
	 */
	public static String getRequestUrl(String urlPath, Properties properties, String method) {
		// 请求参数类型转换
		String parameters = HttpClientUtils.getRequestParameters(properties);
		// 生成最终访问地址
		urlPath = HttpClientUtils.getRequestUrl(urlPath, parameters, method);

		return urlPath;
	}

	/**
	 * 请求参数转换为字符串
	 *
	 * @param properties
	 *            请求参数
	 * @return 请求字符串
	 */
	public static String getRequestParameters(Properties properties) {
		String retValue = "";
		try {
			if (properties != null && !properties.isEmpty()) {
				Iterator<Entry<Object, Object>> iterator = properties.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<Object, Object> entry = iterator.next();
					String key = (String) entry.getKey();
					String value = (String) entry.getValue();
					String parameter = key + "=" + URLEncoder.encode(value, "UTF-8");
					if (retValue.isEmpty()) {
						retValue += parameter;
					} else {
						retValue += "&" + parameter;
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			String message = "http request parameters UTF-8 encode failed.";
			logger.error(message);
			throw new RuntimeException(message);
		}

		return retValue;
	}

}
