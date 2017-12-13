package com.maoshen.component.proxy.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class LogHandler implements InvocationHandler {

	private Object targetObject; // 将要代理的对象保存为成员变量
	// 将被代理的对象传进来，通过这个方法生成代理对象

	public Object newProxyInstance(Object targetObject) {
		this.targetObject = targetObject;
		return Proxy.newProxyInstance(targetObject.getClass().getClassLoader(), targetObject.getClass().getInterfaces(),
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
			ret = method.invoke(targetObject, args);
			System.out.println("success-->>" + method.getName()); // 方法执行后操作
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error-->>" + method.getName());// 出现异常时的操作
			throw e;
		}
		return ret;
	}

	public static void main(String[] args) {
		LogHandler logHandler = new LogHandler();
		TestDao testDao = (TestDao) logHandler.newProxyInstance(new TestDaoImpl());
		// userManager.addUser("0001", "张三");
		String name = testDao.sayHello("jdx");
		System.out.println("Client.main() --- " + name);
	}
}
