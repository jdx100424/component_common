/**
 * 
 */
package com.maoshen.component.other;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.StringUtils;

/**
 * @description <br>
 * @author <a href="mailto:wei.jiang@lifesense.com">vakin</a>
 * @date 2015年11月13日
 * @Copyright (c) 2015, lifesense.com
 */
public class LsDigestUtils {

	/**
	 * 
	 */
	private static final String PASSWORD_SALT = "AX33@#%66FGGG";
	private static final String CARTSET_UTF_8 = "UTF-8";
	private static final String MD5_NAME = "MD5";

	/**
	 * MD5加密
	 * 
	 * @param content
	 * @return
	 */
	public static String md5(Object content) {
		String keys = null;
		if (content == null) {
			return null;
		}
		try {
			MessageDigest md = MessageDigest.getInstance(MD5_NAME);
			byte[] bPass = String.valueOf(content).getBytes(CARTSET_UTF_8);
			md.update(bPass);
			keys = bytesToHexString(md.digest());
		} catch (NoSuchAlgorithmException aex) {
			System.out.println(aex);
		} catch (java.io.UnsupportedEncodingException uex) {
			System.out.println(uex);
		}
		return keys.toLowerCase();
	}

	/**
	 * 帐号密码加密<br>
	 * 对传入已加密的md5 值进行二次加密
	 * 
	 * @param password
	 *            经过一轮md5加密的密文
	 * @return
	 * @throws Exception
	 */
	public static String passwordEncode(String password) throws Exception {
		if (StringUtils.isBlank(password) || password.length() != 32) {
			throw new Exception("password validate is null or length not 32");
		}
		String result = md5(PASSWORD_SALT + password);

		return result;
	}

	private static String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2) {
				sb.append(0);
			}
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

	public static boolean checkPassword(String dbPassword, String password) throws Exception {
		String encodePassword = passwordEncode(password);
		return StringUtils.equalsIgnoreCase(dbPassword, encodePassword);
	}

	public static void main(String[] args) throws Exception {
		String md5 = LsDigestUtils.md5("123456");
		System.out.println(passwordEncode(md5));
	}
}
