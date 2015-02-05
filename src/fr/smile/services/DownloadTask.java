package fr.smile.services;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.commons.io.FileUtils;

import fr.smile.main.Patch;

public class DownloadTask implements Runnable {

	private final Set<RunnableCompleteListener> listeners = new CopyOnWriteArraySet<RunnableCompleteListener>();
	
	public static final Integer OK = 0;
	public static final Integer ERROR = 1;
	
	private Patch patch;
	private Integer result;

	public DownloadTask(Patch patch) {
		this.patch = patch;
		result = OK;
	}
	
	public int getResult() {
		return result;
	}
	
	public Patch getPatch() {
		return patch;
	}

	@Override
	public void run() {
		try {
			download();
		} finally {
			notifyListeners();
		}
	}

	private void download() {
		try {
			String name = "./patches/" + patch.getUrl().substring(patch.getUrl().lastIndexOf("/") + 1);
			FileUtils.copyURLToFile(new URL(patch.getUrl()), new File(name), 10000, 10000);
		} catch (IOException e) {
			result = ERROR;
		}
	}

	public final void addListener(final RunnableCompleteListener listener) {
		listeners.add(listener);
	}

	public final void removeListener(final RunnableCompleteListener listener) {
		listeners.remove(listener);
	}

	private final void notifyListeners() {
		for (RunnableCompleteListener listener : listeners) {
			listener.notifyComplete(this);
		}
	}
}
