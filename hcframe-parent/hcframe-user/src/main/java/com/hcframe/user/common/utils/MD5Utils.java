package com.hcframe.user.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具类类
 *
 * @author 袁涛
 * @date 2018.08.16
 */
public class MD5Utils {

	/**
	 * 加密
	 *
	 * @param data
	 *            原始数据
	 * @return 加密后的数据
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String encode(String data) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest digest = MessageDigest.getInstance("MD5");
		digest.update(data.getBytes("UTF8"));
		return StringUtils.bytesToString(digest.digest());
	}
}