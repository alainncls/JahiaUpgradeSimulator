package fr.smile.main;

public class Patch {

	private String startVersion, endVersion, url, instructions,
			instructionsCluster, warning;
	private Boolean reboot;
	private Float complexity;

	public Patch() {
		startVersion = null;
		endVersion = null;
		url = null;
		instructions = null;
		warning = null;
		reboot = null;
		complexity = 0.2f;
	}

	public Boolean getReboot() {
		return reboot;
	}

	public void setReboot(Boolean reboot) {
		this.reboot = reboot;
	}

	public String getStartVersion() {
		return startVersion;
	}

	public void setStartVersion(String startVersion) {
		this.startVersion = startVersion;
	}

	public String getEndVersion() {
		return endVersion;
	}

	public void setEndVersion(String endVersion) {
		this.endVersion = endVersion;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getInstructions() {
		if(instructions==null){
			return "<div style=\"padding: 10px 10px 10px 35px; border: 1px solid #F90; background: #F5533D;\">"
					+ "<p>Error, no instruction found...</p></div>";
		}
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public String getWarning() {
		return warning;
	}

	public void setWarning(String warning) {
		this.warning = warning;
	}

	public String getInstructionsCluster() {
		if(instructionsCluster==null){
			return "<div style=\"padding: 10px 10px 10px 35px; border: 1px solid #F90; background: #F5533D;\">"
					+ "<p>Error, no instruction for clustered installation found...</p></div>";
		}
		return instructionsCluster;
	}

	public void setInstructionsCluster(String instructionsCluster) {
		this.instructionsCluster = instructionsCluster;
	}
	
	public Float getComplexity() {
		return complexity;
	}

	public void setComplexity(Float complexity) {
		this.complexity = complexity;
	}

	public String getName() {
		return url.substring(url.lastIndexOf("/") + 1);
	}
	
	public String getInstructions(boolean cluster) {
		if(instructionsCluster==null && instructions==null){
			return null;
		}
		if(cluster){
			return getInstructionsCluster() + "<br/><hr/><br/>" + getInstructions();
		}
		return getInstructions();
	}
	
	public Boolean isFixApplier() {
		return url.endsWith(".jar");
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private Patch patch;

		private Builder() {
			patch = new Patch();
		}

		public Builder startVersion(String initVersion) {
			patch.setStartVersion(initVersion);
			return this;
		}

		public Builder endVersion(String endVersion) {
			patch.setEndVersion(endVersion);
			return this;
		}

		public Builder url(String url) {
			patch.setUrl(url);
			return this;
		}

		public Builder instructions(String instructions) {
			patch.setInstructions(instructions);
			return this;
		}

		public Builder warning(String warning) {
			patch.setWarning(warning);
			return this;
		}

		public Builder instructionsCluster(String instructionsCluster) {
			patch.setInstructionsCluster(instructionsCluster);
			return this;
		}
		
		public Builder reboot(Boolean reboot) {
			patch.setReboot(reboot);
			return this;
		}
		
		public Builder complexity(Float complexity) {
			patch.setComplexity(complexity);
			return this;
		}

		public Patch build() {
			return patch;
		}
	}

	@Override
	public String toString() {
		return "Version " + startVersion + " to " + endVersion;
	}

	public boolean isProblem() {
		return warning != null;
	}
}
