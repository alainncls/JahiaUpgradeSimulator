package fr.smile.main;

import java.util.ArrayList;
import java.util.List;

import fr.smile.services.PatchService;

public class Simulation {
	private String startVersion;
	private String endVersion;
	private int stepsA;
	private int stepsP;
	private String error;
	private int reboots;
	private Boolean clustered;

	private List<Patch> listPatches;

	public Simulation(String startVersion, String endVersion, Boolean clustered) {
		this.startVersion = startVersion;
		this.endVersion = endVersion;
		this.listPatches = new ArrayList<>();
		this.stepsA = this.stepsP = this.reboots = 0;
		this.error = "";
		this.clustered = clustered;
		this.runSimulation();
	}

	private void runSimulation() {
		String currentVersion = startVersion;
		List<Patch> pl;
		Patch p;
		while (!(pl = PatchService.getInstance().getPatches(currentVersion,
				endVersion)).isEmpty()) {
			p = pl.get(0);
			listPatches.add(p);
			currentVersion = p.getEndVersion();
			stepsA += pl.size() - 1;
			stepsP += p.isProblem() ? 1 : 0;
			if (p.getReboot()) {
				reboots++;
			}
		}
		if (!currentVersion.equals(endVersion)) {
			error = "Version " + endVersion
					+ " unreachable\nMaximum upgrade available to "
					+ currentVersion;
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append("Version Initiale : ").append(startVersion);
		for (Patch p : listPatches) {
			builder.append("\n").append(p.getStartVersion()).append(" to ")
					.append(p.getEndVersion());
			if (p.isProblem())
				builder.append(" !!!");
		}
		return builder.toString();
	}

	public Boolean getClustered() {
		return clustered;
	}

	public void setClustered(Boolean clustered) {
		this.clustered = clustered;
	}

	public int getReboots() {
		return reboots;
	}

	public void setReboots(int reboots) {
		this.reboots = reboots;
	}

	public void setStartVersion(String start) {
		this.startVersion = start;
	}

	public void setEndVersion(String end) {
		this.endVersion = end;
	}

	public int getSteps() {
		return listPatches.size();
	}

	public int getStepsA() {
		return stepsA;
	}

	public void setStepsA(int stepsA) {
		this.stepsA = stepsA;
	}

	public int getStepsP() {
		return stepsP;
	}

	public void setStepsP(int stepsP) {
		this.stepsP = stepsP;
	}

	public List<Patch> getListPatches() {
		return listPatches;
	}

	public void setListPatches(List<Patch> listPatches) {
		this.listPatches = listPatches;
	}

	public String getStartVersion() {
		return startVersion;
	}

	public String getEndVersion() {
		return endVersion;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
