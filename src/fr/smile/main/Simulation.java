package fr.smile.main;

import java.text.DecimalFormat;
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
	private Float rawDuration;
	private Float uO;
	private int duration;
	private int cost;
	private int tJM;

	private List<Patch> listPatches;

	public Simulation(String startVersion, String endVersion, Boolean clustered) {
		this.startVersion = startVersion;
		this.endVersion = endVersion;
		this.listPatches = new ArrayList<>();
		this.stepsA = this.stepsP = this.reboots = 0;
		this.error = "";
		this.clustered = clustered;
		this.rawDuration = 0f;
		this.duration = 0;
		this.cost = 0;
		this.uO = 1.8f;
		this.tJM = 750;
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
			calculateRawDuration(p);
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
		calculateTotalDuration(uO);
		calculateCost(tJM);
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

	public void calculateRawDuration(Patch p) {
		rawDuration += p.getComplexity();
	}

	public void calculateTotalDuration(float uO) {
		duration = (int) Math.ceil(rawDuration * uO);
	}

	public void calculateCost(int tJM) {
		cost = duration * tJM;
	}

	public String getChiffrage() {
		DecimalFormat df = new DecimalFormat("####.##");
		StringBuilder builder = new StringBuilder();
		builder.append("<div style=\"padding: 10px 10px 10px 35px; border: 1px solid #F90; background: #CFFFCC;\">");
		builder.append("<center><h1>Estimation coût et durée de la migration</h1>");
		builder.append("<h3>Version " + getStartVersion() + " à version " + getEndVersion() + "</h3></center>");
		builder.append("<ul><li>Durée brute estimée de la migration = <b>" + df.format(getRawDuration()) + " jours</li>");
		builder.append("<li>Durée totale estimée de la migration = <span style = \"color: #F00C1C\">" + getDuration() + " JH</span></li>");
		builder.append("<li>Coût estimé de la migration = <b><span style = \"color: #F00C1C\">" + getCost() + " €</span></b></li></ul>");
		builder.append("</div>");

		return builder.toString();
	}
	
	public Float getRawDuration() {
		return rawDuration;
	}

	public void setRawDuration(Float rawDuration) {
		this.rawDuration = rawDuration;
	}

	public Float getuO() {
		return uO;
	}

	public void setuO(Float uO) {
		this.uO = uO;
	}

	public int gettJM() {
		return tJM;
	}

	public void settJM(int tJM) {
		this.tJM = tJM;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
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
