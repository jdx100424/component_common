package com.maoshen.component.other;

public class SystemUtils {

	private static final String ENV_ONLINE = "online";

	public final static String getEnv() {
		return System.getProperty("disconf.env");
	}

	public static boolean isOnlineEnv() {
		return ENV_ONLINE.equals(getEnv());
	}

	/**
	 * 是否测试环境判断
	 * @return
	 */
	public static boolean isTestEnv() {
    return !ENV_ONLINE.equals(getEnv());
	}
	
}
