package fr.smile.main;

import java.util.ArrayList;
import java.util.List;

import fr.smile.reader.VersionsReader;

public class Simulation {
	private String startVersion;
	private String endVersion;
	private int stepsA;
	private int stepsP;
	private String error;

    private List<Patch> listPatches;

	public Simulation(String startVersion, String endVersion) {
        this.startVersion = startVersion;
        this.endVersion = endVersion;
        this.listPatches = new ArrayList<>();
        this.stepsA = this.stepsP = 0;
        this.error = "";
        this.runSimulation();
	}
	
	private void runSimulation() {
		String currentVersion = startVersion;
		List<Patch> pl;
		Patch p;
		while(!(pl=VersionsReader.getInstance().getPatches(currentVersion, endVersion)).isEmpty()){
			p = pl.get(0);
			listPatches.add(p);
			currentVersion = p.getEndVersion();
			stepsA += pl.size()-1;
			stepsP += p.isProblem()?1:0;
		}
		if(!currentVersion.equals(endVersion)){
			error = "Version "+endVersion+" unreachable\nMaximum upgrade available to "+currentVersion;
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("Version Initiale : ").append(startVersion);
		for(Patch p : listPatches){
			builder.append("\n").append(p.getStartVersion()).append(" to ").append(p.getEndVersion());
			if(p.isProblem()) builder.append(" !!!");
		}
		return builder.toString();
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
