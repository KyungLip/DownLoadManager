package com.vshow.vshowapp.download;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.vshow.vshowapp.threadpool.ThreadPool;
import com.vshow.vshowapp.threadpool.ThreadPoolManager;
import com.vshow.vshowapp.threadpool.ThreadPoolOptions;

/**
 * 下载管理�?
 * 
 * @author liupeng
 * 
 */
public class DownloadManager {

	private static DownloadManager downloadManager = null;
	private ThreadPool threadPool;// 线程池对象
	private int fileTotalLength;// 下载文件的大小
	private int blockSize;// 每一块的大小
	private int threadCount = 3;// 执行下载任务的线程数量
	private int runningCount;// 正在运行的线程数
	private int errorCount;// 错误数量
	// private int firstCount;// 第一线程的下载进度
	// private int secondCount;// 第二线程的下载进度
	// private int thirdCount;// 第三线程的下载进度
	private int totalCount = 0;// 总的下载进度
	private String downloadPath;// 下载路径

	private DownloadManager() {
		this.threadPool = initThreadPool();// 初始化线程池
	}

	public static DownloadManager getInstance() {
		if (downloadManager == null) {
			downloadManager = new DownloadManager();
		}
		return downloadManager;
	}

	/**
	 * 初始化线程池
	 * 
	 * @return
	 */
	private ThreadPool initThreadPool() {
		ThreadPoolOptions threadPoolOptions = new ThreadPoolOptions(3, 3, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
		ThreadPool threadPool = ThreadPoolManager.getInstance(threadPoolOptions).getThreadPool();
		return threadPool;
	}

	/**
	 * 开始下载
	 */
	public void startDownload(String downloadUrl, String downloadPath, DownloadStateListener listener) {
		this.downloadPath = downloadPath;
		this.listener = listener;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(downloadUrl);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			System.out.println("开始执行下载");
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				System.out.println("正在下载中...");
				fileTotalLength = (int) httpResponse.getEntity().getContentLength();
				blockSize = fileTotalLength / 3;
				File fileDir = new File(downloadPath);
				if (!fileDir.exists()) {
					fileDir.mkdir();
				}
				File file = new File(fileDir, getFileName(downloadUrl));

				System.out.println(file.toString());
				RandomAccessFile raf = new RandomAccessFile(file, "rw");
				raf.setLength(fileTotalLength);
				runningCount = threadCount;
				for (int i = 1; i <= threadCount; i++) {
					int startIndex = (i - 1) * blockSize;
					int endIndex = i * blockSize - 1;
					if (i == threadCount) {
						endIndex = fileTotalLength - 1;
					}
					DownloadTaskImpl downloadTaskImpl = new DownloadTaskImpl(downloadUrl, downloadPath, startIndex, endIndex, i, downloadListener);
					threadPool.addTask(downloadTaskImpl);
					System.out.println("开启线程:" + i);
				}

			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取当前的下载进度
	 * 
	 * @return
	 */
	public int getCurrentCount() {
		return totalCount;
	}

	/**
	 * 获取总的进度条的长度
	 * 
	 * @return
	 */
	public int getTotalCount() {
		return fileTotalLength;
	}

	/**
	 * 获取下载文件的文件名
	 */
	private String getFileName(String downloadUrl) {

		int index = downloadUrl.lastIndexOf("/");

		return downloadUrl.substring(index + 1);
	}

	/**
	 * 下载监听
	 */
	private DownloadListener<DownloadTaskImpl> downloadListener = new DownloadListener<DownloadTaskImpl>() {

		@Override
		public void onPreDownload(DownloadTaskImpl task) {
			System.out.println(task.getThreadId() + "下载前操作");
		}

		@Override
		public void onStartDownload(DownloadTaskImpl task) {
			System.out.println(task.getThreadId() + "开始下载");

		}

		@Override
		public void onUpdateProgress(DownloadTaskImpl task, int progress, int maxProgress) {
			int threadId = task.getThreadId();
			switch (threadId) {
			case 1:
				totalCount += progress;
				if (listener != null) {
					listener.onUpdate(totalCount);
				}
				break;
			case 2:
				totalCount += progress;
				if (listener != null) {
					listener.onUpdate(totalCount);
				}
				break;
			case 3:
				totalCount += progress;
				if (listener != null) {
					listener.onUpdate(totalCount);
				}
				break;

			default:
				break;
			}
		}

		@Override
		public void onFinish(DownloadTaskImpl task) {
			runningCount--;
			System.out.println("================" + runningCount);

			if (runningCount == 0) {
				if (listener != null) {
					listener.onSuccess();
					for (int x = 1; x <= 3; x++) {
						File fileDir = new File(downloadPath);
						if (!fileDir.exists()) {
							fileDir.mkdir();
						}
						File file = new File(fileDir, x + ".txt");
						file.delete();
					}
				}
			}
		}

		@Override
		public void onError(DownloadTaskImpl task, String errorMsg) {
			errorCount++;
			if (errorCount > 0) {
				if (listener != null) {
					listener.onFail();
				}
			}

		}
	};
	private DownloadStateListener listener;

	public interface DownloadStateListener {
		void onSuccess();

		void onFail();

		void onUpdate(int totalProgress);

	}
}
