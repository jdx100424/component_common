package com.maoshen.component.spring;

import javax.servlet.ServletContextEvent;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.maoshen.component.base.util.ServerLauncherStatus;
import com.maoshen.component.other.ResourceUtils;
import com.maoshen.component.rest.echo.EchoInfoController;
import com.maoshen.component.sentry.SentryProvider;

import io.sentry.Sentry;


public class BaseContextLoaderListener extends org.springframework.web.context.ContextLoaderListener{
	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		
		//自动注册echoInfoController
		WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext());
		DefaultListableBeanFactory acf = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
		String beanName = EchoInfoController.class.getName();
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(EchoInfoController.class);
		acf.registerBeanDefinition(beanName, builder.getRawBeanDefinition());

		//sentry
		SentryProvider.init();
		
		ServerLauncherStatus.get().started();//启动完成标志
	}
}
