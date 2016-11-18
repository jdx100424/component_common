package com.maoshen.component.disconf;

import org.springframework.context.annotation.Configuration;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import com.baidu.disconf.client.common.update.IDisconfUpdate;

@Configuration
@DisconfFile(filename = "redis.properties")
// @DisconfUpdateService(classes = {RedisDisconf.class})
public class RedisDisconf implements IDisconfUpdate {
	private String redisIp;
	private String redisPort;

	@DisconfFileItem(name = "redis.ip", associateField = "redisIp")
	public String getRedisIp() {
		return redisIp;
	}

	public void setRedisIp(String redisIp) {
		this.redisIp = redisIp;
	}

	@DisconfFileItem(name = "redis.port", associateField = "redisPort")
	public String getRedisPort() {
		return redisPort;
	}

	public void setRedisPort(String redisPort) {
		this.redisPort = redisPort;
	}

	@Override
	public void reload() throws Exception {
		// TODO Auto-generated method stub

	}
}
