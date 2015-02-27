package fr.smile.tasks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.smile.listened.ListenedRunnable;

public class ShowStreamTask extends ListenedRunnable {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ShowStreamTask.class);
	public static final String PATH = "./log/";

	private Integer size;
	private String log;

	private final InputStream inputStream;

	public ShowStreamTask(InputStream inputStream, String log) {
		this.inputStream = inputStream;
		this.log = log;
		this.size = 0;
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
				LOGGER.error("Error while copying stream to log file : " + log
						+ e.getMessage());
			}
		} catch (IOException e) {
			LOGGER.error("Error while creating log file for stream : " + log
					+ e.getMessage());
		}
	}

	public int getSize() {
		return size;
	}

}
