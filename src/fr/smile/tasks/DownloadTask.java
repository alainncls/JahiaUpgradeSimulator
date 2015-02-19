package fr.smile.tasks;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.commons.io.FileUtils;

import fr.smile.listeners.RunnableListener;
import fr.smile.models.Patch;

public class DownloadTask implements Runnable {

	private final Set<RunnableListener> listeners = new CopyOnWriteArraySet<RunnableListener>();

	public static final Integer OK = 0;
	public static final Integer ERROR = 1;

	private Patch patch;
	private String path;
	private Integer result;

	public DownloadTask(Patch patch, String path) {
		this.patch = patch;
		this.path = path;
		result = OK;
	}

	public int getResult() {
		return result;
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
			notifyStart();
			download();
		} finally {
			notifyComplete();
		}
	}

	private void download() {
		try {
			File file = new File(path + patch.getName());
			URL url = new URL(patch.getUrl());
			FileUtils.copyURLToFile(url, file, 10000, 10000);
		} catch (IOException e) {
			result = ERROR;
		}
	}

	public final void addListener(final RunnableListener listener) {
		listeners.add(listener);
	}

	public final void removeListener(final RunnableListener listener) {
		listeners.remove(listener);
	}

	private final void notifyComplete() {
		for (RunnableListener listener : listeners) {
			listener.notifyComplete(this);
		}
	}

	private final void notifyStart() {
		for (RunnableListener listener : listeners) {
			listener.notifyStart(this);
		}
	}
}
