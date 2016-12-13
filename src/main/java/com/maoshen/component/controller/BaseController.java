/**   
 * @Description:(用一句话描述该类做什么)
 * @author Daxian.jiang
 * @Email  Daxian.jiang@vipshop.com
 * @Date 2015年9月17日 上午10:58:30
 * @Version V1.0   
 */
package com.maoshen.component.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.maoshen.component.base.dto.ResponseResultDto;
import com.maoshen.component.base.errorcode.BaseErrorCode;
import com.maoshen.component.json.JsonpUtil;

public abstract class BaseController {
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

	@ExceptionHandler
	@ResponseBody
	protected void handleException(HttpServletRequest request, HttpServletResponse response, Throwable thr) {
		ResponseResultDto<Object> result = new ResponseResultDto<Object>();
		result.setCode(BaseErrorCode.SERVICE_EXCEPTION.getCode());
		result.setMessage(BaseErrorCode.SERVICE_EXCEPTION.getMsg());

		LOGGER.error("{}_service running error {}", getServiceName(), thr.getMessage(), thr);
		response.setHeader("Content-type", "application/javascript;charset=UTF-8");
		try {
			response.getWriter().write(JsonpUtil.restJsonp(request.getParameter("callback"), result));
		} catch (Exception e) {
			LOGGER.error("{}_service response error {}", getServiceName(), thr.getMessage(), thr);
		}
	}

	/**
	 * 服务名称
	 * 
	 * @return
	 */
	public abstract String getServiceName();
}