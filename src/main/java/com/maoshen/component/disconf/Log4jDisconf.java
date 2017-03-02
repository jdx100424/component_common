package com.maoshen.component.disconf;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfUpdateService;
import com.baidu.disconf.client.common.update.IDisconfUpdate;


@Configuration
@DisconfFile(filename = "log4j.properties")
@DisconfUpdateService(classes = { Log4jDisconf.class })
public class Log4jDisconf implements IDisconfUpdate {
	private static final Logger LOGGER = LoggerFactory.getLogger(Log4jDisconf.class);
	@Override
	public void reload() throws Exception {
		//自动重新读取配置  
		String log4jPath = this.getClass().getClassLoader().getResource("log4j.properties").getPath();  
        PropertyConfigurator.configureAndWatch(log4jPath, 1);  
		LOGGER.error("log4j.disconf.reload");
	}
}
