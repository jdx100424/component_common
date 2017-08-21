package com.maoshen.component.base.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 服务启动状态
 * @author tim
 *
 */
public class ServerLauncherStatus {
	private static final int WAIT_TIME = 2 * 60;
	private static ServerLauncherStatus status;
	
	private CountDownLatch latch;
	
	public ServerLauncherStatus(){
		latch = new CountDownLatch(1);
	}
	
	public static ServerLauncherStatus get(){
		if(status==null){
			synchronized (ServerLauncherStatus.class) {
				if(status==null){
					status=new ServerLauncherStatus();
				}
			}
		}
		return status;
	}
	
	/**
	 * 正在启动
	 */
	public void starting(){
		
	}
	/**
	 * 启动完成
	 */
	public void started(){
		latch.countDown();
	}
	
	/**
	 * 等待启动完成
	 */
	public void waitStarted(){
		try {
			latch.await(WAIT_TIME, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void launcherError(Throwable t){
		
	}

	public static void main(String []s){
		System.out.println("start");
		ServerLauncherStatus.get().started();
		ServerLauncherStatus.get().waitStarted();
		System.out.println("end");
	}
}
