package com.maoshen.component.other;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数值类型处理
 * 
 */
public class NumericalUtils {

	static DecimalFormat df_0 = new DecimalFormat("###.0");
	static DecimalFormat df_00 = new DecimalFormat("###.00");

	/**
	 * 格式化 浮点数
	 * 
	 * @param number
	 * @return
	 */
	public static float format(float number) {
		return Float.parseFloat(df_00.format(number));
	}

	public static Double format(Double value) {
		return Double.parseDouble(df_00.format(value));
	}

	/**
	 * Double 2 Integer
	 * 
	 * @param x
	 * @return
	 */
	public static Integer DoubleToInteger(Double x) {

		return Integer.parseInt(new java.text.DecimalFormat("0").format(x));
	}

	/**
	 * 产生随机数 传入 最大 最小 范围
	 * 
	 * @param max
	 * @param min
	 * @return
	 */
	public static Integer RandomInt(Integer max, Integer min) {

		Random random = new Random();
		int s = random.nextInt(max) % (max - min + 1) + min;
		return s;
	}

	/**
	 * 截取数字
	 * 
	 * @param content
	 * @return
	 */
	public static int getNumbers(String content) {
		if (content == null || content.equals(""))
			return 0;
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {

			return Integer.parseInt(matcher.group(0));

		}
		return 0;
	}

	public static void main(String[] args) {

		Double x = 1.0;
		System.out.println(DoubleToInteger(x * 1000));

		// System.out.println(RandomInt(200, 60));

	}

}