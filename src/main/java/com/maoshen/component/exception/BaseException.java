package com.maoshen.component.exception;

import org.apache.commons.lang3.StringUtils;

import com.maoshen.component.base.errorcode.BaseErrorCode;

public class BaseException extends Exception{
	private static final long serialVersionUID = -3589913809566407965L;

	private int errorCode;

	private String serviceNameStr;

	public BaseException() {
		super();
	}

	public BaseException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public BaseException(String serviceNameStr, int errorCode, String message) {
		this(errorCode, message);
		this.serviceNameStr = serviceNameStr;
	}

	public BaseException(String serviceNameStr, BaseErrorCode resultCode) {
		this(resultCode.getCode(), resultCode.getMsg());
		this.serviceNameStr = serviceNameStr;
	}

	public String getServiceNameStr() {
		return serviceNameStr;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage4Log() {
		StringBuilder buf = new StringBuilder();
		buf.append("服务[").append(StringUtils.isEmpty(serviceNameStr) ? "unknown" : serviceNameStr)
				.append("]");
		buf.append("错误码[").append(errorCode).append("]").append(getMessage());
		return buf.toString();
	}
}
