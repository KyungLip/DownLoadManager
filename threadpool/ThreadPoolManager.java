package com.vshow.vshowapp.threadpool;

/**
 * 线程池的管理者
 * 
 * @author liupeng
 * 
 */
public class ThreadPoolManager {
	private static ThreadPoolManager threadPoolManager = null;
	private ThreadPool threadPoolInstance;
	private ThreadSinglePool threadSinglePool = null;

	private ThreadPoolManager(ThreadPoolOptions threadPoolOptions) {
		threadPoolInstance = ThreadPool.getInstance(threadPoolOptions);
	};

	/**
	 * 
	 * @param threadPoolOptions
	 *            null 的话返回默认的线程池
	 * @return ThreadPoolManager
	 */
	public static ThreadPoolManager getInstance(ThreadPoolOptions threadPoolOptions) {
		if (threadPoolManager == null) {
			threadPoolManager = new ThreadPoolManager(threadPoolOptions);
		}
		return threadPoolManager;
	}

	/**
	 * 获取线程池对象
	 * 
	 * @return
	 */
	public ThreadPool getThreadPool() {
		return threadPoolInstance;
	}

	/**
	 * 获取SingleThreadPool
	 */
	public ThreadSinglePool getSingleThreadPool() {
		if (threadSinglePool == null) {
			threadSinglePool = ThreadSinglePool.getInstance();
		}
		return threadSinglePool;
	}
}
