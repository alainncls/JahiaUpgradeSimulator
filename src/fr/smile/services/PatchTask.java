package fr.smile.services;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import fr.smile.main.Patch;

public class PatchTask implements Runnable {

	private final Set<RunnableListener> listeners = new CopyOnWriteArraySet<RunnableListener>();

	public static final Integer OK = 0;
	public static final Integer ERROR = 1;

	private int result;
	private Patch patch;

	public PatchTask(Patch patch) {
		this.patch = patch;
		result = OK;
	}

	public int getResult() {
		return result;
	}

	@Override
	public void run() {
		notifyStart();
		if (patch.getStartVersion().equals(
				JahiaConfigService.getInstance().getVersion())) {
			applyPatch();
		} else {
			System.err.println("[ERROR] Patch failed : " + patch.toString()
					+ " : Expected start version "
					+ JahiaConfigService.getInstance().getVersion());
			result = ERROR;
		}
		notifyComplete();
	}

	private void applyPatch() {
		Process process;
		ProcessBuilder pb;
		ShowStreamTask fluxSortie, fluxErreur;
		try {
			pb = new ProcessBuilder("java", "-jar",
					System.getProperty("user.dir") + "/patches/"
							+ patch.getName(), "-y");
			pb.directory(new File(JahiaConfigService.getInstance()
					.getPatchFolder()));
			process = pb.start();

			fluxSortie = new ShowStreamTask(process.getInputStream(),
					ShowStreamTask.OUTPUT_STREAM);
			fluxErreur = new ShowStreamTask(process.getErrorStream(),
					ShowStreamTask.ERROR_STREAM);

			new Thread(fluxSortie).start();
			new Thread(fluxErreur).start();

			process.waitFor();
			
			JahiaConfigService.getInstance().detectJahiaVersion();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
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
