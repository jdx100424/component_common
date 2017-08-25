package com.maoshen.component.disconf;

import org.springframework.context.annotation.Configuration;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import com.baidu.disconf.client.common.update.IDisconfUpdate;

@Configuration
@DisconfFile(filename = "zookeeper.properties")
// @DisconfUpdateService(classes = {ZookeeperDisconf.class})
public class ZookeeperDisconf implements IDisconfUpdate {
	private String zookeeperIp;
	private String zookeeperPort;
	private String zookeeperUrl;

	@DisconfFileItem(name = "zookeeper.url", associateField = "zookeeperUrl")
	public String getZookeeperUrl() {
		return zookeeperUrl;
	}

	public void setZookeeperUrl(String zookeeperUrl) {
		this.zookeeperUrl = zookeeperUrl;
	}

	@DisconfFileItem(name = "zookeeper.ip", associateField = "zookeeperIp")
	public String getZookeeperIp() {
		return zookeeperIp;
	}

	public void setZookeeperIp(String zookeeperIp) {
		this.zookeeperIp = zookeeperIp;
	}

	@DisconfFileItem(name = "zookeeper.port", associateField = "zookeeperPort")
	public String getZookeeperPort() {
		return zookeeperPort;
	}

	public void setZookeeperPort(String zookeeperPort) {
		this.zookeeperPort = zookeeperPort;
	}

	@Override
	public void reload() throws Exception {
		// TODO Auto-generated method stub

	}
}
