package com.maoshen.component.base.dto;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class RequestHeaderDto {
	private String requestId = UUID.randomUUID().toString();

	private Long userId;

	private HttpServletRequest request;

	private HttpServletResponse response;
	
	private boolean requestIdIsAuto = true;

	public boolean isRequestIdIsAuto() {
		return requestIdIsAuto;
	}

	public void setRequestIdIsAuto(boolean requestIdIsAuto) {
		this.requestIdIsAuto = requestIdIsAuto;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
}
