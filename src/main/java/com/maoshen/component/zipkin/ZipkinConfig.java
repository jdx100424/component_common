package com.maoshen.component.zipkin;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.EmptySpanCollectorMetricsHandler;
import com.github.kristofa.brave.Sampler;
import com.github.kristofa.brave.SpanCollector;
import com.github.kristofa.brave.http.DefaultSpanNameProvider;
import com.github.kristofa.brave.okhttp.BraveOkHttpRequestResponseInterceptor;
import com.github.kristofa.brave.servlet.BraveServletFilter;
import com.maoshen.component.disconf.ZipkinDisconf;

import okhttp3.OkHttpClient;

@Configuration
public class ZipkinConfig {
	private final static Float DEFAULT_FLOAT = 0.0001F;
	@Autowired
	private ZipkinDisconf zipkinDisconf;
	
	// span（一次请求信息或者一次链路调用）信息收集器
	@Bean
	public SpanCollector spanCollector() throws Exception {
		BaseHttpSpanCollector.Config config = BaseHttpSpanCollector.Config.builder().compressionEnabled(false)// 默认false，span在transport之前是否会被gzipped
				.connectTimeout(5000).flushInterval(1).readTimeout(6000).build();
		if(zipkinDisconf==null){
			throw new Exception("zipkinDisconf is not allow null");
		}
		
		String zipkinUrl = zipkinDisconf.getZipkinUrl();
		if(StringUtils.isBlank(zipkinUrl)){
			throw new Exception("zipkinDisconf.zipkinUrl is not allow null");
		}
		return BaseHttpSpanCollector.create(zipkinUrl, config, new EmptySpanCollectorMetricsHandler());
	}

	// 作为各调用链路，只需要负责将指定格式的数据发送给zipkin
	@Bean
	public Brave brave(SpanCollector spanCollector) throws Exception {
		String serviceName = zipkinDisconf.getZipkinService();
		if(StringUtils.isBlank(serviceName)){
			throw new Exception("zipkinDisconf.serviceName is not allow null");
		}
		com.github.kristofa.brave.Brave.Builder builder = new com.github.kristofa.brave.Brave.Builder(serviceName);// 指定serviceName
		builder.spanCollector(spanCollector);
		
		Float percent = DEFAULT_FLOAT;
		String percentStr = zipkinDisconf.getZipkinPercent();
		if(StringUtils.isNotBlank(percentStr)){
			try{
				percent = Float.parseFloat(percentStr);
			}catch(Exception e){
				
			}
		}
		builder.traceSampler(Sampler.create(percent));// 采集率
		return  builder.build();
	}

	// 设置server的（服务端收到请求和服务端完成处理，并将结果发送给客户端）过滤器
	@Bean
	public BraveServletFilter braveServletFilter(Brave brave) {
		BraveServletFilter filter = new BraveServletFilter(brave.serverRequestInterceptor(),
				brave.serverResponseInterceptor(), new BaseSpanNameProvider());
		return filter;
	}

	// 设置client的（发起请求和获取到服务端返回信息）拦截器
	@Bean
	public OkHttpClient okHttpClient(Brave brave) {
		OkHttpClient httpClient = new OkHttpClient.Builder()
				.addInterceptor(new BraveOkHttpRequestResponseInterceptor(brave.clientRequestInterceptor(),
						brave.clientResponseInterceptor(), new DefaultSpanNameProvider()))
				.build();
		return httpClient;
	}

	@Bean
	public BaseMySQLStatementInterceptor initJdxMySQLStatementInterceptor(Brave brave) {
		BaseMySQLStatementInterceptor jdxMySQLStatementInterceptor = new BaseMySQLStatementInterceptor();
		BaseMySQLStatementInterceptor.setClientTracer(brave.clientTracer());
		return jdxMySQLStatementInterceptor;
	}
}
