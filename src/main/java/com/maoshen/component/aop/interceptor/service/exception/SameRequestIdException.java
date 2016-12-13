package com.maoshen.component.aop.interceptor.service.exception;

/**
 * 拦截器相同请求ID异常
 * 
 * @author daxian.jianglifesense.com
 *
 */
@SuppressWarnings("serial")
public class SameRequestIdException extends Exception {
	public SameRequestIdException() {
		super();
	}

	public SameRequestIdException(String str) {
		super(str);
	}
}
