package fr.smile.services;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import fr.smile.main.Patch;

public enum DownloadService {

	INSTANCE;

	private ExecutorService pool;

	public static DownloadService getInstance() {
		return INSTANCE;
	}

	// **** BUILDER ****
	private DownloadService() {
		pool = Executors.newFixedThreadPool(2);
	}

	public void download(Patch p, RunnableCompleteListener listener) {
		System.out.println("Download launched : " + p.toString());
		DownloadTask task = new DownloadTask(p);
		task.addListener(listener);
		pool.execute(task);
	}

	public void terminate() {
		pool.shutdown();
		try {
			pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
