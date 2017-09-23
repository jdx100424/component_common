package com.maoshen.component.sentry;

import org.apache.commons.lang3.StringUtils;

import com.maoshen.component.other.ResourceUtils;

import io.sentry.Sentry;
import io.sentry.event.EventBuilder;

public class SentryProvider {
	private static boolean isInit = false;
	private static final String PROJECT_NAME = "projectName";
	public static void sendLog(String projectName,String message,io.sentry.event.Event.Level event,org.slf4j.Logger logger) {
		if(isInit){
			EventBuilder eventBuilder = new EventBuilder()
                    .withMessage(message)
                    .withLevel(event)
                    .withLogger(logger.getName());
			Sentry.getContext().addTag(PROJECT_NAME, projectName);
			Sentry.capture(eventBuilder);
		}
	}
	public static void init(){
		String dsn = ResourceUtils.get("dsn" , "");
		if(StringUtils.isNotBlank(ResourceUtils.get("dsn" , ""))){
			Sentry.init(dsn);
			isInit = true;
		}
	}
}
