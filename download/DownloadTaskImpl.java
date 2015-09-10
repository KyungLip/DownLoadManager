package com.vshow.vshowapp.download;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * 基于断点的下载任务实现类
 * 
 * @author liupeng
 * 
 */
public class DownloadTaskImpl extends DownloadBaseTask {

	protected int startIndex;// 起始下载位置
	protected int endIndex;// 结束下载位置
	protected int threadId;// 执行该任务的线程ID
	private File recordFile;// 该文件用于记录当前线程的下载进度
	private DownloadListener<DownloadTaskImpl> downloadListener;// 下载监听
	private int fileLength;// 当先线程下载的文件的长度

	public DownloadTaskImpl(String downloadUrl, String downloadPath, int startIndex, int endIndex, int threadId, DownloadListener<DownloadTaskImpl> downloadListener) {
		super(downloadUrl, downloadPath);
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.threadId = threadId;
		this.downloadListener = downloadListener;
	}

	@Override
	public boolean onPreExecute() {
		if (downloadListener != null) {
			downloadListener.onPreDownload(this);
		}
		fileLength = endIndex - startIndex;
		System.out.println(downloadPath);
		File fileDir = new File(downloadPath);
		if (!fileDir.exists()) {
			fileDir.mkdir();
		}
		recordFile = new File(fileDir, threadId + ".txt");
		return true;
	}

	@Override
	public void onDownload() {
		if (downloadListener != null) {
			downloadListener.onStartDownload(DownloadTaskImpl.this);
		}
		InputStream is = null;
		RandomAccessFile raf = null;
		try {
			int total = 0;// 总的下载长度
			if (recordFile.exists() && recordFile.length() > 0) {
				System.out.println("文件长度:" + recordFile.length());
				FileInputStream fis = new FileInputStream(recordFile);
				InputStreamReader isr = new InputStreamReader(fis);
				BufferedReader bufr = new BufferedReader(isr);
				String currentLength = bufr.readLine();
				int lastTotal = Integer.valueOf(currentLength);
				total += lastTotal;
				startIndex += lastTotal;
				fis.close();
				isr.close();
				bufr.close();

			}
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(downloadUrl);
			httpGet.setHeader("Range", "bytes=" + startIndex + "-" + endIndex);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == 206) {
				is = httpResponse.getEntity().getContent();
				File fileDir = new File(downloadPath);
				if (!fileDir.exists()) {
					fileDir.mkdir();
				}
				File downloadFile = new File(fileDir, fileName);
				raf = new RandomAccessFile(downloadFile, "rw");
				raf.seek(startIndex);
				int len = -1;
				byte[] buffer = new byte[1024 * 8];// 缓存区大小为8K
				while ((len = is.read(buffer)) != -1) {
					RandomAccessFile rf = new RandomAccessFile(recordFile, "rwd");
					total += len;// 记录当前的下载的进度
					rf.write(String.valueOf(total).getBytes());
					raf.write(buffer, 0, len);
					if (downloadListener != null) {
						downloadListener.onUpdateProgress(DownloadTaskImpl.this, total, fileLength);
					}
					rf.close();
				}
				if (downloadListener != null) {
					downloadListener.onFinish(DownloadTaskImpl.this);
				}

			}
		} catch (ClientProtocolException e) {
			if (downloadListener != null) {
				downloadListener.onError(DownloadTaskImpl.this, threadId + "下载出错");
			}
			e.printStackTrace();
		} catch (IOException e) {
			if (downloadListener != null) {
				downloadListener.onError(DownloadTaskImpl.this, threadId + "下载出错");
			}
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (raf != null) {
				try {
					raf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}

	/**
	 * 获取执行当前任务的线程ID
	 * 
	 * @return
	 */
	public int getThreadId() {
		return threadId;
	}

}
