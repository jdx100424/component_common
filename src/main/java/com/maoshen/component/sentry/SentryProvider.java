package com.maoshen.component.sentry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.maoshen.component.other.ResourceUtils;

import io.sentry.Sentry;
import io.sentry.event.EventBuilder;

public class SentryProvider {
	private static boolean isInit = false;
	private static final String PROJECT_NAME = "projectName";

	public static void sendLog(String projectName, String message, io.sentry.event.Event.Level event,
			org.slf4j.Logger logger) {
		sendLog(projectName, message, event, logger, null);
	}

	public static void sendLog(String projectName, String message, io.sentry.event.Event.Level event,
			org.slf4j.Logger logger, Exception e) {
		if (isInit) {
			EventBuilder eventBuilder = new EventBuilder().withMessage(message).withLevel(event).withLogger(logger.getName());
			Sentry.getContext().addTag(PROJECT_NAME, projectName);
			if (e != null) {
				Sentry.getContext().addExtra("exception", ExceptionUtils.getStackTrace(e));
			}
			/*
			switch (event) {
			case DEBUG:
				logger.debug(message);
			case INFO:
				logger.info(message);
			case WARNING:
				logger.warn(message);
			case ERROR:
				logger.error(message);
			default:
				logger.error(message);
			}*/
			Sentry.capture(eventBuilder);
		}
	}

	public static void init() {
		String dsn = ResourceUtils.get("dsn", "");
		if (StringUtils.isNotBlank(ResourceUtils.get("dsn", ""))) {
			Sentry.init(dsn);
			isInit = true;
		}
	}
}
