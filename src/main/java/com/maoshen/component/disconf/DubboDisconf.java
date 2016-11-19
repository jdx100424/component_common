package com.maoshen.component.disconf;

import org.springframework.context.annotation.Configuration;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import com.baidu.disconf.client.common.update.IDisconfUpdate;

@Configuration
@DisconfFile(filename = "dubbo.properties")
// @DisconfUpdateService(classes = {KafkaDisconf.class})
public class DubboDisconf implements IDisconfUpdate {
	private String dubboPort;

	@DisconfFileItem(name = "dubbo.port", associateField = "dubboPort")
	public String getDubboPort() {
		return dubboPort;
	}

	public void setDubboPort(String dubboPort) {
		this.dubboPort = dubboPort;
	}

	@Override
	public void reload() throws Exception {
		// TODO Auto-generated method stub

	}
}
