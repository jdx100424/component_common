package com.maoshen.component.zipkin;

import com.github.kristofa.brave.http.HttpRequest;
import com.github.kristofa.brave.http.SpanNameProvider;
import com.github.kristofa.brave.servlet.ServletHttpServerRequest;

public class BaseSpanNameProvider  implements SpanNameProvider {

	@Override
	public String spanName(HttpRequest request) {
		if(request instanceof ServletHttpServerRequest){
			ServletHttpServerRequest servletHttpServerRequest = (ServletHttpServerRequest) request;
			//System.out.println(JSONObject.toJSONString(servletHttpServerRequest));
			return servletHttpServerRequest.getUri().toString();
		}
		return request.getHttpMethod();
	}

}
