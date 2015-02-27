package fr.smile.tasks;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.smile.listened.ListenedRunnable;
import fr.smile.models.Patch;

public class DownloadTask extends ListenedRunnable {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DownloadTask.class);

	private Patch patch;
	private String path;

	public DownloadTask(Patch patch, String path) {
		this.patch = patch;
		this.path = path;
	}

	public Patch getPatch() {
		return patch;
	}

	public String getPath() {
		return path;
	}

	@Override
	public void run() {
		try {
			this.notifyStart();
			download();
		} finally {
			this.notifyComplete();
		}
	}

	private void download() {
		try {
			File file = new File(path + patch.getName());
			URL url = new URL(patch.getUrl());
			FileUtils.copyURLToFile(url, file, 10000, 10000);
		} catch (IOException e) {
			result = ERROR;
			LOGGER.error("Download error", e);
		}
	}
}
