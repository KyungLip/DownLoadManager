package com.vshow.vshowapp.download;

/**
 * 下载监听�?
 * 
 * @author liupeng
 * 
 */
public interface DownloadListener<T extends DownloadBaseTask> {

	/**
	 * 下载前
	 * 
	 * @param task
	 */
	public void onPreDownload(T task);

	/**
	 * 下载开始
	 * 
	 * @param task
	 */
	public void onStartDownload(T task);

	/**
	 * 下载中更新下载进度
	 * 
	 * @param task
	 * @param progress
	 *            当前进度
	 * @param maxProgress
	 *            最大进度
	 */
	public void onUpdateProgress(T task, int progress, int maxProgress);

	/**
	 * 下载完成
	 * 
	 * @param task
	 */
	public void onFinish(T task);

	/**
	 * 下载出错
	 * 
	 * @param task
	 */
	public void onError(T task, String errorMsg);

}
