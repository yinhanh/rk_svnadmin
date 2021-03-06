package com.rekoe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.repo.Base64;

/**
 * 加密工具
 */
public class EncryptUtil {

	private final static Log log = Logs.get();
	/**
	 * 
	 */
	private static final String cvt = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz0123456789#@$";

	/**
	 * 
	 */
	private static final int fillchar = '*';

	/**
	 * 加密
	 * 
	 * @param str
	 *            明文
	 * @return 密文
	 */
	public static String encrypt(String str) {
		byte[] data = str.getBytes();
		int c;
		int len = data.length;
		StringBuffer ret = new StringBuffer(((len / 3) + 1) * 4);
		for (int i = 0; i < len; ++i) {
			c = (data[i] >> 2) & 0x3f;
			ret.append(cvt.charAt(c));
			c = (data[i] << 4) & 0x3f;
			if (++i < len) {
				c |= (data[i] >> 4) & 0x0f;
			}
			ret.append(cvt.charAt(c));
			if (i < len) {
				c = (data[i] << 2) & 0x3f;
				if (++i < len) {
					c |= (data[i] >> 6) & 0x03;
				}
				ret.append(cvt.charAt(c));
			} else {
				++i;
				ret.append((char) fillchar);
			}
			if (i < len) {
				c = data[i] & 0x3f;
				ret.append(cvt.charAt(c));
			} else {
				ret.append((char) fillchar);
			}
		}
		return ret.toString();
	}

	public static void main(String[] args) {
		String str = "abc";
		System.out.println(encrypt(str));
		System.out.println(decrypt(encrypt(str)));
	}
	/**
	 * 解密
	 * 
	 * @param str
	 *            密文
	 * @return 明文
	 */
	public static String decrypt(String str) {
		byte[] data = str.getBytes();
		int c, c1;
		int len = data.length;
		StringBuffer ret = new StringBuffer((len * 3) / 4);
		for (int i = 0; i < len; ++i) {
			c = cvt.indexOf(data[i]);
			++i;
			c1 = cvt.indexOf(data[i]);
			c = ((c << 2) | ((c1 >> 4) & 0x3));
			ret.append((char) c);
			if (++i < len) {
				c = data[i];
				if (fillchar == c) {
					break;
				}
				c = cvt.indexOf((char) c);
				c1 = ((c1 << 4) & 0xf0) | ((c >> 2) & 0xf);
				ret.append((char) c1);
			}
			if (++i < len) {
				c1 = data[i];
				if (fillchar == c1) {
					break;
				}
				c1 = cvt.indexOf((char) c1);
				c = ((c << 6) & 0xc0) | c1;
				ret.append((char) c);
			}
		}
		return ret.toString();
	}

	/**
	 * apache SHA1 加密
	 * 
	 * @param str
	 *            明文
	 * @return 密文
	 */
	public static String encriptSHA1(String str) {
		try {
			return Base64.encodeToString(MessageDigest.getInstance("SHA1").digest(str.getBytes()), false);
		} catch (NoSuchAlgorithmException e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

}
