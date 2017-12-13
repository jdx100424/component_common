package com.maoshen.component.proxy.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 没有实现类的情况下动态代理，例如mybatis和netty
 * @author daxian.jianglifesense.com
 *
 */
public class ProxyNotImpl implements InvocationHandler {

	private Object targetObject; // 将要代理的对象保存为成员变量
	// 将被代理的对象传进来，通过这个方法生成代理对象

	@SuppressWarnings("unchecked")
	public <T> T newProxyInstance(final Class<?> targetObject) {
		this.targetObject = targetObject;
		return  (T) Proxy.newProxyInstance(targetObject.getClassLoader(), new Class<?>[]{targetObject},
				this);
	}

	// 代理模式内部要毁掉的方法
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("start-->>" + method.getName());// 方法执行前的操作
		for (int i = 0; i < args.length; i++) {
			System.out.println(args[i]);
		}
		Object ret = null;
		try {
			// 调用目标方法，如果目标方法有返回值，返回ret,如果没有抛出异常
			System.out.println("success-->>" + method.getName()); // 方法执行后操作
			if("sayHello".equals(method.getName())){
				ret = "调用方法：sayHello";
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error-->>" + method.getName());// 出现异常时的操作
			throw e;
		}
		return ret;
	}

	public static void main(String[] args) {
		ProxyNotImpl logHandler = new ProxyNotImpl();
		TestDao testDao = logHandler.newProxyInstance(TestDao.class);
		// userManager.addUser("0001", "张三");
		String name = testDao.sayHello("jdx");
		System.out.println("Client.main() --- " + name);
	}
}
