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
import java.util.LinkedList;
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

	public List<Patch> getPatches(String startVersion, String endVersion) {
		List<Patch> patches = new LinkedList<>();
		for (Patch p : listPatch) {
			if (p.getStartVersion().equals(startVersion)
					&& p.getEndVersion().compareTo(endVersion) <= 0) {
				if (patches.isEmpty()
						|| patches.get(0).getEndVersion().compareTo(p.getEndVersion()) < 0) {
					patches.add(0, p);
				}else{
					patches.add(p);
				}
			}
		}
		return patches;
	}
}
