package com.maoshen.component.task.util;

import org.apache.commons.lang3.StringUtils;

public class TaskUtil {
	public static boolean isTaskGray(String taskGray){
		boolean isGray;
		if(StringUtils.isBlank(taskGray)){
			isGray = false;
		}else{
			try{
				Boolean taskIsGrap = Boolean.valueOf(taskGray);
				if(taskIsGrap!=null && Boolean.TRUE.equals(taskIsGrap)){
					isGray = true;
				}else{
					isGray = false;
				}
			}catch(Exception e){
				isGray = false;
			}
		}
		return isGray;
	}
}
