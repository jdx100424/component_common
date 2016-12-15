package com.maoshen.component.aop.interceptor.test;

import java.lang.reflect.Method;

import com.maoshen.component.aop.annotation.AnnotationInterceptor;

public class ServiceInterceptorTest {
	@AnnotationInterceptor
	public void test() {

	}
	public void test2() {

	}
	public void test3() {

	}

	public static void main(String[] s) {
		ServiceInterceptorTest serviceInterceptorTest = new ServiceInterceptorTest();
		Method[] arr = serviceInterceptorTest.getClass().getMethods();
		for (Method m : arr) {
			AnnotationInterceptor annotationInterceptor = m.getAnnotation(AnnotationInterceptor.class);
			if (annotationInterceptor != null) {
				if(m.getName().equals("test")){
					System.out.println(m.getName() + "," + m.getReturnType());
				}
			}
		}
	}
}
