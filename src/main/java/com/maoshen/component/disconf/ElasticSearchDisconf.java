package com.maoshen.component.disconf;

import org.springframework.context.annotation.Configuration;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import com.baidu.disconf.client.common.update.IDisconfUpdate;

@Configuration
@DisconfFile(filename = "elasticSearch.properties")
public class ElasticSearchDisconf implements IDisconfUpdate {
	private String elasticSearchUrl;

	@Override
	public void reload() throws Exception {

	}

	@DisconfFileItem(name = "elasticSearch.url", associateField = "elasticSearchUrl")
	public String getElasticSearchUrl() {
		return elasticSearchUrl;
	}

	public void setElasticSearchUrl(String elasticSearchUrl) {
		this.elasticSearchUrl = elasticSearchUrl;
	}

}
