package com.maoshen.component.disconf;

import org.springframework.context.annotation.Configuration;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import com.baidu.disconf.client.common.update.IDisconfUpdate;

@Configuration
@DisconfFile(filename = "sentry.properties")
public class SentryDisconf implements IDisconfUpdate {
	private String dsn;
	private String sentryUncaughtHandlerEnabled;
	private String sentryStacktraceAppPackages;
	private String uncaughtHandlerEnabled;
	private String stacktraceAppPackages;
	
	
	@DisconfFileItem(name = "uncaught.handler.enabled", associateField = "uncaughtHandlerEnabled")
	public String getUncaughtHandlerEnabled() {
		return uncaughtHandlerEnabled;
	}

	public void setUncaughtHandlerEnabled(String uncaughtHandlerEnabled) {
		this.uncaughtHandlerEnabled = uncaughtHandlerEnabled;
	}
	@DisconfFileItem(name = "stacktrace.app.packages", associateField = "stacktraceAppPackages")
	public String getStacktraceAppPackages() {
		return stacktraceAppPackages;
	}

	public void setStacktraceAppPackages(String stacktraceAppPackages) {
		this.stacktraceAppPackages = stacktraceAppPackages;
	}

	@Override
	public void reload() throws Exception {
		
	}

	@DisconfFileItem(name = "dsn", associateField = "dsn")
	public String getDsn() {
		return dsn;
	}

	public void setDsn(String dsn) {
		this.dsn = dsn;
	}	

	@DisconfFileItem(name = "sentry.stacktrace.app.packages", associateField = "sentryStacktraceAppPackages")
	public String getSentryStacktraceAppPackages() {
		return sentryStacktraceAppPackages;
	}

	@DisconfFileItem(name = "sentry.uncaught.handler.enabled", associateField = "sentryUncaughtHandlerEnabled")
	public String getSentryUncaughtHandlerEnabled() {
		return sentryUncaughtHandlerEnabled;
	}

	public void setSentryUncaughtHandlerEnabled(String sentryUncaughtHandlerEnabled) {
		this.sentryUncaughtHandlerEnabled = sentryUncaughtHandlerEnabled;
	}

	public void setSentryStacktraceAppPackages(String sentryStacktraceAppPackages) {
		this.sentryStacktraceAppPackages = sentryStacktraceAppPackages;
	}
}
