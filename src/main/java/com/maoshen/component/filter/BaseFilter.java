package com.maoshen.component.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.maoshen.component.base.dto.RequestHeaderDto;
import com.maoshen.component.base.dto.RequestHeaderDtoHolder;

public class BaseFilter implements Filter {
	private static final String REQUEST_ID="requestId";
	FilterConfig filterConfig = null;
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseFilter.class);

	@Override
	public void destroy() {
		 this.filterConfig = null;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//填充RequestHeaderDto相关参数
		RequestHeaderDto dto = new RequestHeaderDto();
		dto.setRequest((HttpServletRequest) request);
		dto.setResponse((HttpServletResponse) response);
		String paramRequestId = request.getParameter(REQUEST_ID);
		if(StringUtils.isNotBlank(paramRequestId)){
			dto.setRequestId(paramRequestId);
			dto.setRequestIdIsAuto(false);
		}
		RequestHeaderDtoHolder.set(dto);
		LOGGER.info("RequestHeaderDto value:{}",JSONObject.toJSONString(dto));
        chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}
}
