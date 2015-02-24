package fr.smile.tasks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.smile.listeners.RunnableListener;

public class ShowStreamTask implements Runnable {

	private final Set<RunnableListener> listeners = new CopyOnWriteArraySet<RunnableListener>();
	private static final Logger LOG = LogManager.getLogger();

	public static final String PATH = "./log/";

	public static final Integer OK = 0;
	public static final Integer ERROR = 1;

	private Integer result;
	private Integer size;
	private String log;

	private final InputStream inputStream;

	public ShowStreamTask(InputStream inputStream, String log) {
		this.inputStream = inputStream;
		this.log = log;
		this.size = 0;
		result = OK;
	}

	@Override
	public void run() {
		try {
			notifyStart();
			showStream();
		} finally {
			notifyComplete();
		}
	}

	private void showStream() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		File f = new File(PATH + dateFormat.format(date) + " - " + log);
		f.getParentFile().mkdirs();
		try {
			f.createNewFile();
			try (OutputStream outputStream = new FileOutputStream(f)) {
				this.size = IOUtils.copy(inputStream, outputStream);
			} catch (IOException e) {
				System.err.println("Error while copying stream to log file : "
						+ log);
				LOG.error(e);
			}
		} catch (IOException e) {
			System.err.println("Error while creating log file for stream : "
					+ log);
			LOG.error(e);
		}
	}

	public int getResult() {
		return result;
	}

	public int getSize() {
		return size;
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
