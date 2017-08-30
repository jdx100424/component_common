package com.maoshen.component.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSONObject;
import com.maoshen.component.base.dto.ResponseResultDto;
import com.maoshen.component.base.errorcode.BaseErrorCode;
import com.maoshen.component.json.JsonpUtil;
import com.maoshen.component.rest.UserRestContext;
import com.maoshen.component.rpc.filter.constant.DubboContextFilterConstant;

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
	        
			Map<String,String> map = new HashMap<String,String>();
			map.put(DubboContextFilterConstant.RPC_USER_REST_CONTEXT, JSONObject.toJSONString(userRestContext));
			RpcContext.getContext().setAttachments(map);
			
			UserHttpServletResponseWrapper responseWrapper = new UserHttpServletResponseWrapper((HttpServletResponse)response);  
			chain.doFilter(request, responseWrapper);
			int status = responseWrapper.getStatus();  
			if(LOGGER.isDebugEnabled()){
				LOGGER.error("status:{}",status);
			}
			if(status == 404){
				ResponseResultDto<Object> result = new ResponseResultDto<Object>();
				result.setCode(BaseErrorCode.URL_NOT_FOUND.getCode());
				result.setMessage(BaseErrorCode.URL_NOT_FOUND.getMsg());
				responseWrapper.getResponse().getWriter().write(JsonpUtil.restJsonp(request.getParameter("callback"), result));
			}
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
