package com.maoshen.component.spring;

import javax.servlet.ServletContextEvent;

import com.maoshen.component.base.util.ServerLauncherStatus;


public class BaseContextLoaderListener extends org.springframework.web.context.ContextLoaderListener{
	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		ServerLauncherStatus.get().started();//启动完成标志
	}
}
