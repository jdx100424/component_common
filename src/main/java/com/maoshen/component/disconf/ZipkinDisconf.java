package com.maoshen.component.disconf;

import org.springframework.context.annotation.Configuration;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import com.baidu.disconf.client.common.update.IDisconfUpdate;

@Configuration
@DisconfFile(filename = "zipkin.properties")
public class ZipkinDisconf implements IDisconfUpdate {
	private String zipkinUrl;
	private String zipkinService;
	private String zipkinPercent;

	@DisconfFileItem(name = "zipkin.percent", associateField = "zipkinPercent")
	public String getZipkinPercent() {
		return zipkinPercent;
	}

	@DisconfFileItem(name = "zipkin.service", associateField = "zipkinService")
	public String getZipkinService() {
		return zipkinService;
	}

	public void setZipkinPercent(String zipkinPercent) {
		this.zipkinPercent = zipkinPercent;
	}

	public void setZipkinService(String zipkinService) {
		this.zipkinService = zipkinService;
	}

	@DisconfFileItem(name = "zipkin.url", associateField = "zipkinUrl")
	public String getZipkinUrl() {
		return zipkinUrl;
	}

	public void setZipkinUrl(String zipkinUrl) {
		this.zipkinUrl = zipkinUrl;
	}

	@Override
	public void reload() throws Exception {

	}
}
