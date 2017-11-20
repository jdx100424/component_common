package com.maoshen.component.elasticsearch;

import java.net.InetAddress;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.maoshen.component.disconf.ElasticSearchDisconf;
import com.maoshen.component.disconf.ZipkinDisconf;

@Configuration
public class BaseElasticSearchClientHandle {
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseElasticSearchClientHandle.class);
	private static Client client;
	private static boolean isInit = false;
	@Autowired
	private ElasticSearchDisconf elasticSearchDisconf;

	@Bean
	public Client initClient() throws Exception {
		if (elasticSearchDisconf != null) {
			Client client = TransportClient.builder().build()
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(elasticSearchDisconf.getElasticSearchUrl()), Integer.parseInt(elasticSearchDisconf.getElasticSearchPort())));
			BaseElasticSearchClientHandle.client = client;
			isInit = true;
			return client;
		} else {
			throw new Exception("elasticSearchDisconf is not allow null");
		}
	}

	public static Client getClient() throws Exception {
		if (isInit) {
			return client;
		} else {
			throw new Exception("client is not init");
		}
	}

	public static void deleteById(String index, String type, String id) throws Exception {
		if (isInit) {
			client.prepareDelete(index, type, id).execute().actionGet();
		} else {
			throw new Exception("client is not init");
		}
	}

	public static void deleteByIndex(String index) throws Exception {
		if (isInit) {
			IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(index);
			IndicesExistsResponse inExistsResponse = client.admin().indices().exists(inExistsRequest).actionGet();
			if (inExistsResponse.isExists()) {
				client.admin().indices().prepareDelete(index).execute().actionGet();
			}
		} else {
			throw new Exception("client is not init");
		}
	}

	public static void insert(List<String> list, String index, String type) throws Exception {
		if (isInit) {
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					IndexResponse response = client.prepareIndex(index, type).setSource(list.get(i)).get();
					if (response.isCreated() == false) {
						LOGGER.warn("list {} isCreated error", i);
					}
				}
			}
		} else {
			throw new Exception("client is not init");
		}
	}

	public static SearchHits select(String index, String type, String selectContent, String... name) throws Exception {
		if (isInit) {
			QueryBuilder qb = QueryBuilders.multiMatchQuery(selectContent, name);
			SearchResponse response = client.prepareSearch(index).setTypes(type).setQuery(qb).execute().actionGet();
			SearchHits hits = response.getHits();
			return hits;
		} else {
			throw new Exception("client is not init");
		}
	}

	public static void update(String index, String type, String id, Map<String, Object> updateMap) throws Exception {
		if (isInit) {
			client.prepareUpdate(index, type, id).setId(id).setDoc(updateMap).execute().actionGet();
		} else {
			throw new Exception("client is not init");
		}
	}
}
