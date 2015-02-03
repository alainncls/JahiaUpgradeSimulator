package fr.smile.reader;

//**** IMPORTS ****

import fr.smile.main.Patch;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public enum VersionsReader {

	INSTANCE;

	// **** ATTRIBUTES ****
	public final String path = "data/versions/versions.json";
	private List<String> listVersion;
	private List<Patch> listPatch;

	// **** BUILDER ****
	private VersionsReader() {
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

	public static VersionsReader getInstance() {
		return INSTANCE;
	}

	// Method to read the file line by line (each line = a bloc)
	public void readFile() throws IOException, ParseException {
		JSONParser parser = new JSONParser();
		JSONArray a = (JSONArray) parser.parse(new FileReader(path));
		String version, endVersion, url, type;
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
				type = (String) jsonPatch.get("type");
				patch = Patch.builder().startVersion(version)
						.endVersion(endVersion).type(type).url(url).build();
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

	public Patch getBestPatch(String startVersion, String endVersion) {
		Patch best = null;
		for (Patch p : listPatch) {
			if (p.getStartVersion().equals(startVersion)
					&& p.getEndVersion().compareTo(endVersion) <= 0) {
				if (best == null
						|| best.getEndVersion().compareTo(p.getEndVersion()) < 0) {
					best = p;
				}
			}
		}
		return best;
	}
}
