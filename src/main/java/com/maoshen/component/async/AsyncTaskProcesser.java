package com.maoshen.component.async;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.DefaultManagedAwareThreadFactory;

/**
 * 
 * @description <br>
 * @author <a href="mailto:wei.jiang@lifesense.com">vakin</a>
 * @date 2016年4月7日
 * @Copyright (c) 2015, lifesense.com
 */
public abstract class AsyncTaskProcesser {
	
	private final static Logger logger = LoggerFactory.getLogger(AsyncTaskProcesser.class);

	protected ThreadPoolExecutor executor;

	public AsyncTaskProcesser(int nThreads) {
		
		executor = new ThreadPoolExecutor(nThreads, nThreads, 60, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(), //
				new DefaultManagedAwareThreadFactory(),new MyRejectedExecutionHandler()) {
			protected void afterExecute(Runnable r, Throwable t) {
				super.afterExecute(r, t);
				printException(r, t);
			}
		};
		
	}
	
	/**
	 * 线程池内异常处理
	 * @param r
	 * @param t
	 */
	private static void printException(Runnable r, Throwable t) {
		if (t == null && r instanceof Future<?>) {
			try {
				Future<?> future = (Future<?>) r;
				if (future.isDone())
					future.get();
			} catch (CancellationException ce) {
				t = ce;
			} catch (ExecutionException ee) {
				t = ee.getCause();
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt(); // ignore/reset
			}
		}
		if (t != null)
			logger.error(t.getMessage(), t);
	}
	

	protected class MyRejectedExecutionHandler implements RejectedExecutionHandler {
	 
	    @Override
	    public void rejectedExecution(Runnable task, ThreadPoolExecutor executor) {

            if (!executor.isShutdown()) {
            	task.run();
            }
        
	    }
	}
}
