package fr.smile.tasks;

import java.io.File;
import java.io.IOException;

import fr.smile.models.Patch;
import fr.smile.services.JahiaConfigService;

public class PatchTask extends ListenedRunnable {

	private Patch patch;

	public PatchTask(Patch patch) {
		this.patch = patch;
	}

	@Override
	public void run() {
		notifyStart();
		String version = JahiaConfigService.getInstance().getVersion();
		if (patch.getStartVersion().equals(version)) {
			applyPatch();
		} else {
			System.err.println("Wrong Patch, Expected start version " + version
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
				System.err
						.println("Error while applying patch, please check logs");
				result = ERROR;
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			result = ERROR;
		}
	}
}
