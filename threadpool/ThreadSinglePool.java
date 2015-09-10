package com.vshow.vshowapp.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadSinglePool {

	private static ThreadSinglePool threadSinglePool = null;
	private ExecutorService newSingleThreadExecutor;

	private ThreadSinglePool() {
		newSingleThreadExecutor = Executors.newSingleThreadExecutor(Executors.defaultThreadFactory());
	};

	public static ThreadSinglePool getInstance() {
		if (threadSinglePool == null) {
			threadSinglePool = new ThreadSinglePool();
		}
		return threadSinglePool;

	}

	/**
	 * 添加任务
	 * 
	 * @param task
	 */
	public void addTask(Runnable task) {
		if (task != null) {
			newSingleThreadExecutor.execute(task);
		}
	}

}
