package com.vshow.vshowapp.download;

/**
 * 下载基础任务类
 * 
 * @author liupeng
 * 
 */
public abstract class DownloadBaseTask implements Runnable {
	protected String downloadUrl;// 下载地址
	protected String downloadPath;// 下载存储的位置
	/**
	 * 下载文件名称
	 */
	protected String fileName;

	public DownloadBaseTask(String downloadUrl, String downloadPath) {
		super();
		this.downloadUrl = downloadUrl;
		this.downloadPath = downloadPath;
		fileName = getFileName(downloadUrl);
	}

	@Override
	public void run() {
		if (onPreExecute()) {
			onDownload();
		}
	}

	public abstract boolean onPreExecute();

	public abstract void onDownload();

	/**
	 * 获取下载文件的文件名
	 */
	private String getFileName(String downloadUrl) {

		int index = downloadUrl.lastIndexOf("/");

		return downloadUrl.substring(index + 1);
	}
}
