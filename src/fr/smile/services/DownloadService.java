package fr.smile.services;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import fr.smile.main.Patch;

public enum DownloadService {

	INSTANCE;

	private ExecutorService pool;

	public static final String PATH = "./patches/";

	public static DownloadService getInstance() {
		return INSTANCE;
	}

	// **** BUILDER ****
	private DownloadService() {
		pool = Executors.newFixedThreadPool(2);
	}

	public void download(Patch patch, RunnableListener listener) {
		if (!this.exist(patch)) {
			DownloadTask task = new DownloadTask(patch, PATH);
			task.addListener(listener);
			pool.execute(task);
		}
	}

	public boolean exist(Patch patch) {
		File file = new File(PATH + patch.getName());
		if(file.exists()){
			System.out.println("Found Patch in download folder : "+patch.toString());
			return true;
		}
		return false;
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
