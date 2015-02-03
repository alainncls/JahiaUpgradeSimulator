package fr.smile.main;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import fr.smile.reader.InstructionsReader;
import fr.smile.reader.VersionsReader;

public class Simulation {
	public String startVersion;
	public String endVersion;
	public int steps;
	public int stepsV;
	public int stepsP;

	private List<String> listVersions;
    private List<Patch> listPatches;

	public Simulation(String startVersion, String endVersion) {
        this.startVersion = startVersion;
        this.endVersion = endVersion;
		this.listVersions = VersionsReader.getInstance().getVersions();
        this.listPatches = new ArrayList<>();
	}

	public String compareVersions() {
		StringBuilder builder = new StringBuilder();
		builder.append("Version Initiale : ");

		steps = 0;
		stepsV = 0;
		stepsP = 0;

		int start = listVersions.indexOf(startVersion);
		int end = listVersions.indexOf(endVersion);

		for (String temp : listVersions.subList(start, end)) {
			if (isNotSpecial(temp) || temp.equals(startVersion)) {

				builder.append(temp);
				if (isProblem(temp)) {
					builder.append(" !!!");
				}
				builder.append("\n").append(temp).append(" - ");
				steps++;
			}
		}
		builder.append(endVersion);

		return builder.toString();
	}

	public boolean isNotSpecial(String v) {
		if (v.equals("6.6.1.0") || v.equals("6.6.0.0")) {
			stepsV++;
			return false;
		}

		return true;
	}

	public boolean isProblem(String v) {
		if (!v.equals(startVersion)
				&& (v.equals("6.6.1.2") || v.equals("6.6.1.4"))) {
			stepsP++;
			return true;
		}

		return false;
	}

	public String getInstructions(String installType)
			throws FileNotFoundException {
		InstructionsReader.create(installType);
		InstructionsReader insRead = InstructionsReader.getInstance();
		return insRead.getInstructions();
	}

	public void setStartVersion(String start) {
		this.startVersion = start;
	}

	public void setEndVersion(String end) {
		this.endVersion = end;
	}
}
