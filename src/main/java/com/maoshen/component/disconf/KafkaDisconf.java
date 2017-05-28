package com.maoshen.component.disconf;

import org.springframework.context.annotation.Configuration;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import com.baidu.disconf.client.common.update.IDisconfUpdate;

@Configuration
@DisconfFile(filename = "kafka.properties")
// @DisconfUpdateService(classes = {KafkaDisconf.class})
public class KafkaDisconf implements IDisconfUpdate {
	private String kafkaIp;
	private String kafkaPort;
	private String kafkaServer;

	@DisconfFileItem(name = "kafka.server", associateField = "kafkaServer")
	public String getKafkaServer() {
		return kafkaServer;
	}

	public void setKafkaServer(String kafkaServer) {
		this.kafkaServer = kafkaServer;
	}

	@DisconfFileItem(name = "kafka.ip", associateField = "kafkaIp")
	public String getKafkaIp() {
		return kafkaIp;
	}

	public void setKafkaIp(String kafkaIp) {
		this.kafkaIp = kafkaIp;
	}

	@DisconfFileItem(name = "kafka.port", associateField = "kafkaPort")
	public String getKafkaPort() {
		return kafkaPort;
	}

	public void setKafkaPort(String kafkaPort) {
		this.kafkaPort = kafkaPort;
	}

	@Override
	public void reload() throws Exception {
		// TODO Auto-generated method stub

	}
}
