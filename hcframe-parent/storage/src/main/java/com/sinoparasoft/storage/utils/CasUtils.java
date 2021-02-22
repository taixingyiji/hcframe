package com.sinoparasoft.storage.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import org.apache.commons.lang3.StringUtils;

/**
 * 统一身份认证工具类
 *
 * @author 袁涛
 * @date 2017.10.27
 */
public class CasUtils {
	private static final Logger logger = LoggerFactory.getLogger(CasUtils.class);

	/**
	 * 统一身份认证服务器访问地址
	 */
	private String casServerUrl;

	/**
	 * 统一身份认证获取令牌地址
	 */
	private String accessTokenUrl;

	/**
	 * 访问目标地址
	 */
	private String targetUrl;

	public String getCasServerUrl() {
		return casServerUrl;
	}

	public void setCasServerUrl(String casServerUrl) {
		this.casServerUrl = casServerUrl;
	}

	public String getAccessTokenUrl() {
		return accessTokenUrl;
	}

	public void setAccessTokenUrl(String accessTokenUrl) {
		this.accessTokenUrl = accessTokenUrl;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	/**
	 * 获取TGT票据
	 *
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return TGT票据
	 */
	public String getTicketGrantingTicket(String username, String password) {
		String retValue = null;

		// 检查参数
		if (StringUtils.isBlank(this.accessTokenUrl)) {
			String message = "access token url is blank.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		if (StringUtils.isBlank(username)) {
			String message = "username is blank.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		if (StringUtils.isBlank(password)) {
			String message = "password is blank.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		HttpsURLConnection httpsURLConnection = null;
		try {
			httpsURLConnection = (HttpsURLConnection) this.openConnection(this.accessTokenUrl, "POST");
			// 参数
			String parameters = "username=" + URLEncoder.encode(username, "UTF-8");
			parameters += "&password" + "=" + URLEncoder.encode(password, "UTF-8");
			parameters += "&service" + "=" + URLEncoder.encode(this.targetUrl, "UTF-8");

			OutputStreamWriter out = new OutputStreamWriter(httpsURLConnection.getOutputStream());
			BufferedWriter writer = new BufferedWriter(out);
			// 添加参数到目标服务器
			writer.write(parameters);
			writer.flush();
			writer.close();
			out.close();

			// 获取token
			String tgt = httpsURLConnection.getHeaderField("location");
			// 获取返回值
			if (tgt != null && httpsURLConnection.getResponseCode() == 201) {
				tgt = tgt.substring(tgt.lastIndexOf("/") + 1);
			}
			retValue = tgt;
		} catch (IOException e) {
			String message = "get ticket granting ticket failed, username = " + username + ", password = " + password;
			logger.error(message, e);
		} finally {
			try {
				if (httpsURLConnection != null) {
					httpsURLConnection.disconnect();
				}
			} catch (Exception e) {
				String message = "disconnect https url connection failed, username = " + username + ", password = "
						+ password;
				logger.error(message, e);
			}
		}

		return retValue;
	}

	/**
	 * 打开URL连接
	 *
	 * @param url
	 *            访问地址
	 * @param method
	 *            访问方法
	 * @return URL连接
	 * @throws IOException
	 */
	public URLConnection openConnection(String url, String method) throws IOException {
		// 检查参数
		if (StringUtils.isBlank(url)) {
			String message = "url is blank.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		if (StringUtils.isBlank(method)) {
			String message = "method is blank.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		URL uRL = new URL(url);
		HttpsURLConnection httpsURLConnection = (HttpsURLConnection) uRL.openConnection();
		httpsURLConnection.setDoInput(true);
		httpsURLConnection.setDoOutput(true);
		httpsURLConnection.setRequestMethod(method);

		return httpsURLConnection;
	}

	/**
	 * 根据TGT获取ST票据
	 *
	 * @param ticketGrantingTicket
	 *            TGT票据
	 * @return ST票据
	 */
	public String getServiceTicket(String ticketGrantingTicket) {
		String retValue = null;

		// 检查参数
		if (StringUtils.isBlank(this.accessTokenUrl)) {
			String message = "access token url is blank.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		if (StringUtils.isBlank(ticketGrantingTicket)) {
			String message = "ticket granting ticket is blank.";
			logger.error(message);
			throw new RuntimeException(message);
		}

		HttpsURLConnection httpsURLConnection = null;
		try {
			httpsURLConnection = (HttpsURLConnection) this
					.openConnection(this.accessTokenUrl + "/" + ticketGrantingTicket, "POST");

			// 需要访问的目标网站
			String parameters = "service=" + URLEncoder.encode(this.targetUrl, "UTF-8");
			OutputStreamWriter out = new OutputStreamWriter(httpsURLConnection.getOutputStream());
			BufferedWriter writer = new BufferedWriter(out);
			// 添加参数到目标服务器
			writer.write(parameters);
			writer.flush();
			writer.close();
			out.close();

			// 获取返回的ticket票据
			BufferedReader in = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
			retValue = in.readLine();
		} catch (IOException e) {
			String message = "get ticket granting ticket failed, ticketGrantingTicket = " + ticketGrantingTicket;
			logger.error(message, e);
		} finally {
			try {
				if (httpsURLConnection != null) {
					httpsURLConnection.disconnect();
				}
			} catch (Exception e) {
				String message = "disconnect https url connection failed, ticketGrantingTicket = "
						+ ticketGrantingTicket;
				logger.error(message, e);
			}
		}

		return retValue;
	}

	/**
	 * 根据用户名和密码获取ST票据
	 *
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return ST票据
	 */
	public String getServiceTicket(String username, String password) {
		String retValue = null;

		// 获取TGT票据
		String ticketGrantingTicket = this.getTicketGrantingTicket(username, password);
		if (StringUtils.isBlank(ticketGrantingTicket)) {
			String message = "get ticket granting ticket failed, username = " + username + ", password = " + password;
			logger.error(message);
			throw new RuntimeException(message);
		}
		retValue = this.getServiceTicket(ticketGrantingTicket);

		return retValue;
	}
}
