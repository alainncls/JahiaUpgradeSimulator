package fr.smile.tasks;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.smile.listened.ListenedRunnable;
import fr.smile.models.Patch;
import fr.smile.services.JahiaConfigService;

public class PatchTask extends ListenedRunnable {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PatchTask.class);

	private Patch patch;

	public PatchTask(Patch patch) {
		this.patch = patch;
	}

	public Patch getPatch() {
		return patch;
	}

	@Override
	public void run() {
		notifyStart();
		String version = JahiaConfigService.getInstance().getVersion();
		if (patch.getStartVersion().equals(version)) {
			applyPatch();
		} else {
			LOGGER.error("Wrong Patch, Expected start version " + version
					+ " : got " + patch.getStartVersion());
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
					this.patch.toString() + ".output.log");
			fluxErreur = new ShowStreamTask(process.getErrorStream(),
					this.patch.toString() + ".error.log");

			new Thread(fluxSortie).start();
			new Thread(fluxErreur).start();

			process.waitFor();

			JahiaConfigService.getInstance().detectJahiaVersion();

			if (fluxErreur.getSize() != 0
					|| !JahiaConfigService.getInstance().getVersion()
					.equals(patch.getEndVersion())) {
				LOGGER.error("Error while applying patch, please check logs");
				result = ERROR;
			}
		} catch (IOException | InterruptedException e) {
			LOGGER.error(e.getMessage());
			result = ERROR;
		}
	}
}
