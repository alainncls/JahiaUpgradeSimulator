package fr.smile.services;

//**** IMPORTS ****

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import fr.smile.main.Patch;

public enum PatchService {

	INSTANCE;

	// **** ATTRIBUTES ****
	public final String path = "/versions.json";
	private List<String> listVersion;
	private List<Patch> listPatch;

	// **** BUILDER ****
	private PatchService() {
		listVersion = new ArrayList<>();
		listPatch = new ArrayList<>();
		try {
			readFile(); // Reading the file line by line
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static PatchService getInstance() {
		return INSTANCE;
	}

	// Method to read the file line by line (each line = a bloc)
	public void readFile() throws IOException, ParseException {
		InputStream is = getClass().getResourceAsStream(path);
		StringWriter writer = new StringWriter();
		IOUtils.copy(is, writer);
		String json = writer.toString();
		is.close();
		writer.close();

		JSONParser parser = new JSONParser();
		JSONArray a = (JSONArray) parser.parse(json);

		String version, endVersion, url, instructions, instructionsCluster, warning;
		Boolean reboot;

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
				if (((String) jsonPatch.get("reboot")).equals("0")) {
					reboot = false;
				} else {
					reboot = true;
				}
				patch = Patch.builder().startVersion(version)
						.endVersion(endVersion).url(url)
						.instructionsCluster(instructionsCluster)
						.instructions(instructions).warning(warning)
						.reboot(reboot).build();

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
}
