package com.maoshen.component.other;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 日期处理工具类
 * @description <br>
 * @author <a href="mailto:wei.jiang@lifesense.com">vakin</a>
 * @date 2015年10月27日
 * Copyright (c) 2015, lifesense.com
 */
public class DateUtils {
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_PATTERN = "HH:mm";
    /**
     * 解析日期<br>
     * 支持格式：<br>
     * generate by: vakin jiang at 2012-3-1
     * 
     * @param dateStr
     * @return
     */
    public static Date parseDate(String dateStr) {
        SimpleDateFormat format = null;
        if (StringUtils.isBlank(dateStr))
            return null;
        String _dateStr = dateStr.trim();
        try {
            if (_dateStr.matches("\\d{1,2}[A-Z]{3}")) {
                _dateStr = _dateStr
                        + (Calendar.getInstance().get(Calendar.YEAR) - 2000);
            }
            // 01OCT12
            if (_dateStr.matches("\\d{1,2}[A-Z]{3}\\d{2}")) {
                format = new SimpleDateFormat("ddMMMyy", Locale.ENGLISH);
            } else if (_dateStr.matches("\\d{1,2}[A-Z]{3}\\d{4}.*")) {// 01OCT2012
                // ,01OCT2012
                // 1224,01OCT2012
                // 12:24
                _dateStr = _dateStr.replaceAll("[^0-9A-Z]", "")
                        .concat("000000").substring(0, 15);
                format = new SimpleDateFormat("ddMMMyyyyHHmmss", Locale.ENGLISH);
            } else {
                StringBuffer sb = new StringBuffer(_dateStr);
                String[] tempArr = _dateStr.split("\\s+");
                tempArr = tempArr[0].split("-|\\/");
                if (tempArr.length == 3) {
                    if (tempArr[1].length() == 1) {
                        sb.insert(5, "0");
                    }
                    if (tempArr[2].length() == 1) {
                        sb.insert(8, "0");
                    }
                }
                _dateStr = sb.append("000000").toString().replaceAll("[^0-9]",
                        "").substring(0, 14);
                if (_dateStr.matches("\\d{14}")) {
                    format = new SimpleDateFormat("yyyyMMddHHmmss");
                }
            }
            Date date = format.parse(_dateStr);
            return date;
        } catch (Exception e) {
            throw new RuntimeException("无法解析日期字符[" + dateStr + "]");
        }
    }
    /**
     * 解析日期字符串转化成日期格式<br>
     * generate by: vakin jiang at 2012-3-1
     * 
     * @param dateStr
     * @param pattern
     * @return
     */
    public static Date parseDate(String dateStr, String pattern) {
        try {
            SimpleDateFormat format = null;
            if (StringUtils.isBlank(dateStr))
                return null;
            if (StringUtils.isNotBlank(pattern)) {
                format = new SimpleDateFormat(pattern);
                return format.parse(dateStr);
            }
            return parseDate(dateStr);
        } catch (Exception e) {
            throw new RuntimeException("无法解析日期字符[" + dateStr + "]");
        }
    }
    /**
     * 获取一天开始时间<br>
     * generate by: vakin jiang at 2011-12-23
     * 
     * @param date
     * @return
     */
    public static Date getDayBegin(Date date) {
        String format = DateFormatUtils.format(date, DATE_PATTERN);
        return parseDate(format.concat(" 00:00:00"));
    }
    /**
     * 获取一天结束时间<br>
     * generate by: vakin jiang at 2011-12-23
     * 
     * @param date
     * @return
     */
    public static Date getDayEnd(Date date) {
        String format = DateFormatUtils.format(date, DATE_PATTERN);
        return parseDate(format.concat(" 23:59:59"));
    }
    /**
     * 时间戳格式转换为日期（年月日）格式<br>
     * generate by: vakin jiang at 2011-12-23
     * 
     * @param date
     * @return
     */
    public static Date timestamp2Date(Date date) {
        return formatDate(date, DATE_PATTERN);
    }
    
     /**
     * 格式化日期格式为：ddMMMyy<br>
     * generate by: vakin jiang
     *                    at 2012-10-17
     * @param dateStr
     * @return
     */
    public static String format2ddMMMyy(Date date){
        SimpleDateFormat format = new SimpleDateFormat("ddMMMyy", Locale.ENGLISH);
        return format.format(date).toUpperCase();
    }
    
     /**
     * 格式化日期格式为：ddMMMyy<br>
     * generate by: vakin jiang
     *                    at 2012-10-17
     * @param dateStr
     * @return
     */
    public static String format2ddMMMyy(String dateStr){
        SimpleDateFormat format = new SimpleDateFormat("ddMMMyy", Locale.ENGLISH);
        return format.format(DateUtils.parseDate(dateStr)).toUpperCase();
    }
    /**
     * 格式化日期字符串<br>
     * generate by: vakin jiang at 2012-3-7
     * 
     * @param dateStr
     * @param patterns
     * @return
     */
    public static String formatDateStr(String dateStr, String... patterns) {
        String pattern = TIMESTAMP_PATTERN;
        if (patterns != null && patterns.length > 0
                && StringUtils.isNotBlank(patterns[0])) {
            pattern = patterns[0];
        }
        return DateFormatUtils.format(parseDate(dateStr), pattern);
    }
    /**
     * 格式化日期为日期字符串<br>
     * generate by: vakin jiang at 2012-3-7
     * 
     * @param orig
     * @param patterns
     * @return
     */
    public static String format(Date date, String... patterns) {
        if (date == null)
            return "";
        String pattern = TIMESTAMP_PATTERN;
        if (patterns != null && patterns.length > 0
                && StringUtils.isNotBlank(patterns[0])) {
            pattern = patterns[0];
        }
        return DateFormatUtils.format(date, pattern);
    }
    public static String format2DateStr(Date date) {
        return format(date, DATE_PATTERN);
    }
    /**
     * 格式化日期为指定格式<br>
     * generate by: vakin jiang at 2012-3-7
     * 
     * @param orig
     * @param patterns
     * @return
     */
    public static Date formatDate(Date orig, String... patterns) {
        String pattern = TIMESTAMP_PATTERN;
        if (patterns != null && patterns.length > 0
                && StringUtils.isNotBlank(patterns[0])) {
            pattern = patterns[0];
        }
        return parseDate(DateFormatUtils.format(orig, pattern));
    }
    
    /**
     * 比较两个时间相差多少秒
     * */
    public static long getDiffSeconds(Date d1, Date d2) {
        return Math.abs((d2.getTime() - d1.getTime()) / 1000);
    }
    
    /**
     * 比较两个时间相差多少分钟
     * */
    public static long getDiffMinutes(Date d1, Date d2) {
        long diffSeconds = getDiffSeconds(d1, d2);
        return diffSeconds/60;
    }
    /**
     * 比较两个时间相差多少天
     * */
    public static long getDiffDay(Date d1, Date d2) {
        long between = Math.abs((d2.getTime() - d1.getTime()) / 1000);
        long day = between / 60 / 60 / 24;
        return (long) Math.floor(day);
    }
    /**
     * 返回传入时间月份的最后一天
     * */
    public static Date lastDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int value = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.DAY_OF_MONTH, value);
        return cal.getTime();
    }
    /**
     * 返回传入时间月份的第一天
     * */
    public static Date firstDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int value = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.DAY_OF_MONTH, value);
        return cal.getTime();
    }

	/**
	 * 返回传入时间所在礼拜的第一天（周日）
	 * */
	public static Date firstDayOfWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int value = cal.getActualMinimum(Calendar.DAY_OF_WEEK);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.DAY_OF_WEEK, value);
		return cal.getTime();
	}
    /**
     * 获取两个时间相差月份
     * */
    public static int getDiffMonth(Date start, Date end) {
//        Calendar startCalendar = Calendar.getInstance();
//        startCalendar.setTime(start);
//        Calendar endCalendar = Calendar.getInstance();
//        endCalendar.setTime(end);
//        Calendar temp = Calendar.getInstance();
//        temp.setTime(end);
//        temp.add(Calendar.DATE, 1);
//        int year = endCalendar.get(Calendar.YEAR)
//                - startCalendar.get(Calendar.YEAR);
//        int month = endCalendar.get(Calendar.MONTH)
//                - startCalendar.get(Calendar.MONTH);
//        if ((startCalendar.get(Calendar.DATE) == 1)
//                && (temp.get(Calendar.DATE) == 1)) {
//            return year * 12 + month + 1;
//        } else if ((startCalendar.get(Calendar.DATE) != 1)
//                && (temp.get(Calendar.DATE) == 1)) {
//            return year * 12 + month + 1;
//        } else if ((startCalendar.get(Calendar.DATE) == 1)
//                && (temp.get(Calendar.DATE) != 1)) {
//            return year * 12 + month + 1;
//        } else {
//            return (year * 12 + month - 1) < 0 ? 0 : (year * 12 + month);
//        }
    	Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(start);
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(end);
		return (endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR)) * 12
				+ endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
    }
    /**
     * 计算并格式化消耗时间<br>
     * generate by: vakin jiang at 2012-2-16
     * 
     * @param startPoint
     * @return
     */
    public static String formatTimeConsumingInfo(long startPoint) {
        StringBuffer buff = new StringBuffer();
        long totalMilTimes = System.currentTimeMillis() - startPoint;
        int hour = (int) Math.floor(totalMilTimes / (60*60*1000));
        int mi = (int) Math.floor(totalMilTimes / (60*1000));
        int se = (int) Math.floor((totalMilTimes - 60000 * mi) / 1000);
        if(hour > 0)buff.append(hour).append("小时");
        if(mi > 0)buff.append(mi).append("分");
        if(hour == 0)buff.append(se).append("秒");
        return buff.toString();
    }
    /**
     * 判断是否为闰年<br>
     * generate by: zengqw at 2012-9-26
     */
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
    
    public static Date add(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }

	public static Date addDays(final Date date, final int amount) {
		return add(date, Calendar.DAY_OF_MONTH, amount);
	}
	
	
	/**
	 * 计算某一天在一年中的那一周
	 * 
	 */
	public static int getWeekNoForYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);// 星期一作为每周的第一天
		cal.setTime(date);
		int weekno = cal.get(Calendar.WEEK_OF_YEAR);
		return weekno;
	}
	public static int getYear(Date date) {
		Calendar cal = new GregorianCalendar();
		if (date != null) {
			cal.setTime(date);
		}
		int year = cal.get(Calendar.YEAR);
		return year;
	}

	public static int getMonth(Date date) {
		Calendar cal = new GregorianCalendar();
		if (date != null) {
			cal.setTime(date);
		}
		int month = cal.get(Calendar.MONTH) + 1;
		return month;
	}

	public static int getDay(Date date) {
		Calendar cal = new GregorianCalendar();
		if (date != null) {
			cal.setTime(date);
		}
		int day = cal.get(Calendar.DAY_OF_MONTH);
		return day;
	}
	public static Date getFirstDateOfWeek(Date date, int firstDayOfWeek) {
		Calendar cal = Calendar.getInstance();

		cal.setTime(date);

		cal.setFirstDayOfWeek(firstDayOfWeek);

		cal.set(7, firstDayOfWeek);

		return getBeginDate(cal.getTime());
	}
	public static Date getBeginDate(Date date1) {
		Calendar cal = Calendar.getInstance();

		cal.setTime(date1);

		cal.set(11, cal.getActualMinimum(11));

		cal.set(12, cal.getActualMinimum(12));

		cal.set(13, cal.getActualMinimum(13));

		cal.set(14, cal.getActualMinimum(14));

		return cal.getTime();
	}

	public static Date getEndDate(Date date1) {
		Calendar cal = Calendar.getInstance();

		cal.setTime(date1);

		cal.set(11, cal.getActualMaximum(11));

		cal.set(12, cal.getActualMaximum(12));

		cal.set(13, cal.getActualMaximum(13));

		cal.set(14, cal.getActualMaximum(14));

		return cal.getTime();
	}

	/**
	 * 计算某个时间之前的多少天
	 * 
	 */
	public static Date getBeforeDate(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - days);
		return calendar.getTime();
	}
	/**
	 * 计算年龄
	 * 
	 * @param birthDay
	 * @param currentDate
	 * @return
	 */
	public static int getAge(Date birthDay, Date currentDate) {
		if(birthDay == null){
			birthDay = new Date();
		}
		
		if (currentDate.after(birthDay)) {
			Calendar birth = Calendar.getInstance();
			birth.setTime(birthDay);
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentDate);
			cal.set(Calendar.MONTH, birth.get(Calendar.MONTH));
			cal.set(Calendar.DAY_OF_MONTH, birth.get(Calendar.DAY_OF_MONTH));
			int birthYear = birth.get(Calendar.YEAR);
			int currentYear = cal.get(Calendar.YEAR);
			int age = currentYear - birthYear;
			if (currentDate.before(cal.getTime())) {
				age = age - 1;
			}
			return (age);
		} else {
			Calendar birth = Calendar.getInstance();
			birth.setTime(birthDay);
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentDate);
			cal.set(Calendar.MONTH, birth.get(Calendar.MONTH));
			cal.set(Calendar.DAY_OF_MONTH, birth.get(Calendar.DAY_OF_MONTH));
			int birthYear = birth.get(Calendar.YEAR);
			int currentYear = cal.get(Calendar.YEAR);
			int age = currentYear - birthYear;
			if (currentDate.after(cal.getTime())) {
				age = age + 1;
			}
			return (age);
		}
	}
	/**
	 * 取出某个时间所在周的第一天的日期数据
	 * 
	 * @param date
	 *            某个时间
	 * @param firstDayOfWeek
	 *            一周从哪天开始
	 * @return
	 */
	public static Date getFirstDayOfWeek(Date date, int firstDayOfWeek) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return (cal.getTime());
	}

	/**
	 * 某周的最后一天时间
	 * 
	 * @param date
	 * @param firstDayOfWeek
	 * @return
	 */
	public static Date getEndDayOfWeek(Date date, int firstDayOfWeek) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return (cal.getTime());
	}
	/**
	 * 计算某个时间之后的多少天
	 * 
	 */
	public static Date getAfterDate(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + days);
		return calendar.getTime();
	}
	
	/**
	 * 根据模板生成月、日、时、分字符串
	 * 
	 * @param date
	 * @param template
	 *            模板，如{0}月{1}日，{2}点{3}分
	 * @return
	 */
	public static String formatTemplateDate(Date date, String template) {
		String val = "";
		try {
			if (null == date) {
				date = new Date();
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			val = MessageFormat.format(template, calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), get_hh(calendar), get_mm(calendar));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return val;
	}
	/**
	 * formatTemplateDate
	 * @param calendar
	 * @return
	 */
	public static String get_hh(Calendar calendar) {
	    Integer hour=calendar.get(Calendar.HOUR_OF_DAY);//小时   
        String hs = hour.toString();
        if (hour < 10) {
            hs = "0" + hs;
        }
        return hs;
    }
	
	/************************ 模板 时间 end ************************/
	
	/**
	 * formatTemplateDate
	 * @param calendar
	 * @return
	 */
	public static String get_mm(Calendar calendar) {
		int minute=calendar.get(Calendar.MINUTE);//分       
        if(minute<10){
        	return "0"+minute;
        }
        return String.valueOf(minute);
    }

}
