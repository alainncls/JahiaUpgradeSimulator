package fr.smile.services;

//**** IMPORTS ****

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import fr.smile.listeners.RunnableListener;
import fr.smile.models.Patch;
import fr.smile.tasks.PatchTask;

public enum PatchService {

	INSTANCE;

	// **** ATTRIBUTES ****
	public static final String PATH = "/versions.json";
	private List<String> listVersion;
	private List<Patch> listPatch;
	private ExecutorService pool;

	// private Logger logger = LogManager.getLogger();

	// **** BUILDER ****
	private PatchService() {

		pool = Executors.newSingleThreadExecutor();
		listVersion = new ArrayList<>();
		listPatch = new ArrayList<>();
		try {
			readFile(); // Reading the file line by line
		} catch (IOException e) {
			// logger.error(e);
		} catch (ParseException e) {
			// logger.error(e);
		}
	}

	public static PatchService getInstance() {
		return INSTANCE;
	}

	// Method to read the file line by line (each line = a bloc)
	public void readFile() throws IOException, ParseException {
		// File file = new File(PATH);
		// InputStream is = new FileInputStream(file);
		InputStream is = getClass().getResourceAsStream(PATH);
		if (is == null) {
			System.out.println("plop");
		}

		StringWriter writer = new StringWriter();
		IOUtils.copy(is, writer);
		String json = writer.toString();
		is.close();
		writer.close();

		JSONParser parser = new JSONParser();
		JSONArray a = (JSONArray) parser.parse(json);

		String version, endVersion, url, instructions, instructionsCluster, warning;
		Boolean reboot, license;

		Patch patch;

		for (Object o : a) {
			JSONObject jsonVersion = (JSONObject) o;

			version = (String) jsonVersion.get("version");
			listVersion.add(version);

			JSONArray patches = (JSONArray) jsonVersion.get("patches");

			for (Object p : patches) {
				JSONObject jsonPatch = (JSONObject) p;
				endVersion = (String) jsonPatch.get("endVersion");
				url = (String) jsonPatch.get("url");
				instructions = (String) jsonPatch.get("instructions");
				instructionsCluster = (String) jsonPatch
						.get("instructionsCluster");
				warning = (String) jsonPatch.get("warning");
				String r = (String) jsonPatch.get("reboot");
				reboot = r == null || r.equals("1");
				String l = (String) jsonPatch.get("license");
				license = l != null && l.equals("1");

				patch = Patch.builder().startVersion(version)
						.endVersion(endVersion).url(url)
						.instructionsCluster(instructionsCluster)
						.instructions(instructions).warning(warning)
						.reboot(reboot).license(license).build();

				listPatch.add(patch);
			}
		}
	}

	public List<String> getVersions() {
		return listVersion;
	}

	public List<Patch> getPatches() {
		return listPatch;
	}

	public List<Patch> getPatches(String startVersion, String endVersion) {
		List<Patch> patches = new LinkedList<>();
		for (Patch p : listPatch) {
			if (p.getStartVersion().equals(startVersion)
					&& p.getEndVersion().compareTo(endVersion) <= 0) {
				if (patches.isEmpty()
						|| patches.get(0).getEndVersion()
								.compareTo(p.getEndVersion()) < 0) {
					patches.add(0, p);
				} else {
					patches.add(p);
				}
			}
		}
		return patches;
	}

	public void apply(Patch patch, RunnableListener listener) {
		PatchTask task = new PatchTask(patch);
		task.addListener(listener);
		pool.execute(task);
	}
}
