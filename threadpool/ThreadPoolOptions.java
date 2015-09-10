package com.vshow.vshowapp.threadpool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 线程池配置对象
 * 
 * @author liupeng
 * 
 */
public class ThreadPoolOptions {
	private int corePoolSize;// 核心线程数:线程池维护线程的最小数量
	private int maximumPoolSize;// 线程池维护线程的最大数量
	private long keepAliveTime;// 线程池维护的线程允许的空闲时间
	private TimeUnit unit;// 线程池维护的线程允许的空闲时间的单位
	private BlockingQueue<Runnable> threadPoolQueue;// 线程池所使用的缓冲队列
	private ThreadFactory threadFactory;// 创建所用的工厂
	private RejectedExecutionHandler ejectedExecutionHandler;// 线程池对拒绝线程的处理策略

	public ThreadPoolOptions() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ThreadPoolOptions(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> threadPoolQueue, ThreadFactory threadFactory, RejectedExecutionHandler ejectedExecutionHandler) {
		super();
		this.corePoolSize = corePoolSize;
		this.maximumPoolSize = maximumPoolSize;
		this.keepAliveTime = keepAliveTime;
		this.unit = unit;
		this.threadPoolQueue = threadPoolQueue;
		this.threadFactory = threadFactory;
		this.ejectedExecutionHandler = ejectedExecutionHandler;
	}

	/**
	 * 获取线程池维护线程的最小数量
	 * 
	 * @return
	 */
	public int getCorePoolSize() {
		return corePoolSize;
	}

	/**
	 * 设置线程池维护线程的最小数量
	 * 
	 * @param corePoolSize
	 */
	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	public int getMaximumPoolSize() {
		return maximumPoolSize;
	}

	public void setMaximumPoolSize(int maximumPoolSize) {
		this.maximumPoolSize = maximumPoolSize;
	}

	public long getKeepAliveTime() {
		return keepAliveTime;
	}

	public void setKeepAliveTime(long keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}

	public TimeUnit getUnit() {
		return unit;
	}

	public void setUnit(TimeUnit unit) {
		this.unit = unit;
	}

	public BlockingQueue<Runnable> getThreadPoolQueue() {
		return threadPoolQueue;
	}

	public void setThreadPoolQueue(BlockingQueue<Runnable> threadPoolQueue) {
		this.threadPoolQueue = threadPoolQueue;
	}

	public ThreadFactory getThreadFactory() {
		return threadFactory;
	}

	public void setThreadFactory(ThreadFactory threadFactory) {
		this.threadFactory = threadFactory;
	}

	public RejectedExecutionHandler getEjectedExecutionHandler() {
		return ejectedExecutionHandler;
	}

	public void setEjectedExecutionHandler(RejectedExecutionHandler ejectedExecutionHandler) {
		this.ejectedExecutionHandler = ejectedExecutionHandler;
	}

	@Override
	public String toString() {
		return "ThreadPoolOptions [corePoolSize=" + corePoolSize + ", maximumPoolSize=" + maximumPoolSize + ", keepAliveTime=" + keepAliveTime + ", unit=" + unit + ", threadPoolQueue=" + threadPoolQueue + ", threadFactory=" + threadFactory + ", ejectedExecutionHandler=" + ejectedExecutionHandler + "]";
	}

}
