package com.sinoparasoft.storage.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * 文件摘要计算工具类
 *
 * @author 袁涛
 * @date 2017.10.27
 */
public class DigestUtils {
	private static final Logger logger = LoggerFactory.getLogger(DigestUtils.class);

	/**
	 * MD5算法
	 */
	public static final String MD5 = "MD5";

	/**
	 * SHA算法
	 */
	public static final String SHA = "SHA";

	/**
	 * 文件摘要计算缓冲区大小
	 */
	private static final int BUFFER_SISE = 8192;

	/**
	 * 文件摘要计算
	 *
	 * @param filePath
	 *            文件路径
	 * @param algorithm
	 *            算法
	 * @return 摘要
	 */
	public static String calculate(String filePath, String algorithm) {
		String retValue = "";

		try {
			MessageDigest digest = MessageDigest.getInstance(algorithm);
			byte[] buffer = new byte[DigestUtils.BUFFER_SISE];
			File file = new File(filePath);
			FileInputStream in = new FileInputStream(file);
			int length = -1;
			while ((length = in.read(buffer)) != -1) {
				digest.update(buffer, 0, length);
			}
			in.close();
			retValue = StringUtils.bytesToString(digest.digest());
		} catch (NoSuchAlgorithmException e) {
			String msg = "calculate file digest failed, no digest algorithm, algorithm = " + algorithm;
			logger.error(msg);

			return retValue;
		} catch (FileNotFoundException e) {
			String msg = "calculate file digest failed, file not found, file path = " + filePath;
			logger.error(msg);

			return retValue;
		} catch (IOException e) {
			String msg = "calculate file digest failed, file read failed, file path = " + filePath;
			logger.error(msg);

			return retValue;
		}

		return retValue;
	}

	/**
	 * 文件列表摘要计算
	 *
	 * @param filePathList
	 *            文件路径列表
	 * @param algorithm
	 *            算法
	 * @return 摘要
	 */
	public static String calculate(List<String> filePathList, String algorithm) {
		String retValue = "";

		try {
			MessageDigest digest = MessageDigest.getInstance(algorithm);
			byte[] buffer = new byte[DigestUtils.BUFFER_SISE];
			for (String filePath : filePathList) {
				File file = new File(filePath);
				FileInputStream in = new FileInputStream(file);
				int length = -1;
				while ((length = in.read(buffer)) != -1) {
					digest.update(buffer, 0, length);
				}
				in.close();
			}

			retValue = StringUtils.bytesToString(digest.digest());
		} catch (NoSuchAlgorithmException e) {
			String msg = "calculate file digest failed, no digest algorithm, algorithm = " + algorithm;
			logger.error(msg);

			return retValue;
		} catch (FileNotFoundException e) {
			String msg = "calculate file digest failed, file not found, file path list = " + filePathList.toString();
			logger.error(msg);

			return retValue;
		} catch (IOException e) {
			String msg = "calculate file digest failed, file read failed, file path list = " + filePathList.toString();
			logger.error(msg);

			return retValue;
		}

		return retValue;
	}

}
