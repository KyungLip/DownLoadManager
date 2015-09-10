package com.vshow.vshowapp.threadpool;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池
 * 
 * @author liupeng
 * 
 */
public class ThreadPool {
	private static ThreadPool threadPool;
	private int corePoolSize;// 核心线程数
	private int maximumPoolSize;
	private int keepAliveTime = 1;
	private TimeUnit unit = TimeUnit.HOURS;// 事件单元通俗的就是单位
	private ThreadPoolExecutor executor;

	private ThreadPool(ThreadPoolOptions poolOptions) {
		if (poolOptions == null) {
			corePoolSize = Runtime.getRuntime().availableProcessors() * 2 + 1;// 核心线程数
			maximumPoolSize = corePoolSize;
			executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingDeque<Runnable>(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
		} else {
			executor = new ThreadPoolExecutor(poolOptions.getCorePoolSize(), poolOptions.getMaximumPoolSize(), poolOptions.getKeepAliveTime(), poolOptions.getUnit(), poolOptions.getThreadPoolQueue(), poolOptions.getThreadFactory(), poolOptions.getEjectedExecutionHandler());
		}
	};

	/**
	 * 获取线程池管理实例对象
	 * 
	 * @param threadPoolOptions
	 *            如果参数为null代表使用默认的线程池
	 * @return ThreadPool
	 */
	public static ThreadPool getInstance(ThreadPoolOptions threadPoolOptions) {
		if (threadPool == null) {
			if (threadPoolOptions == null) {
				threadPool = new ThreadPool(null);
			} else {
				threadPool = new ThreadPool(threadPoolOptions);
			}
		}
		return threadPool;
	}

	/**
	 * 添加任务,并执行
	 * 
	 * @param runnable
	 */
	public void addTask(Runnable runnable) {
		if (runnable == null)
			return;
		executor.execute(runnable);
	}

	/**
	 * 移除任务
	 * 
	 * @param runnable
	 */
	public void removeTask(Runnable runnable) {
		if (runnable == null) {
			return;
		} else {
			executor.remove(runnable);
		}
	}

	/**
	 * 关闭线程池
	 * 
	 * @param tag
	 *            false:关闭线程池,但是如果里面有任务的话会执行完任务再关闭.true:立即关闭关闭线程池,如果有等待任务,则移除队列,
	 *            然后尝试停止
	 */
	public void close(boolean tag) {
		if (tag) {
			List<Runnable> runnables = executor.shutdownNow();
			if (runnables != null && runnables.size() > 0) {
				for (int x = 0; x < runnables.size(); x++) {
					Runnable nonExecute = runnables.get(x);
					removeTask(nonExecute);
					nonExecute = null;// 有待商榷
				}
			}
		} else {
			executor.shutdown();

		}

	}
}
