package com.maoshen.component.filter;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maoshen.component.rest.UserRestContext;

public class UserFilter implements Filter {
	FilterConfig filterConfig = null;
	private static final Logger LOGGER = LoggerFactory.getLogger(UserFilter.class);

	@Override
	public void destroy() {
		 this.filterConfig = null;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try{
			UserRestContext userRestContext = UserRestContext.get();
			userRestContext.setAccessToken(request.getParameter("accessToken"));
			userRestContext.setRequestId(StringUtils.isBlank(request.getParameter("requestId"))?UUID.randomUUID().toString():request.getParameter("requestId"));
			//LOGGER.info("userRestContext value:{}",JSONObject.toJSONString(userRestContext));
	        chain.doFilter(request, response);
		}catch(Exception e){
			LOGGER.error(e.getMessage(),e);
		}finally{
			UserRestContext.clear();
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}
}
