package fr.smile.main;

public class Patch {

	private String startVersion;
	private String endVersion;
	private String url;
	private String instructions;
	private String warning;

	public Patch() {
		startVersion = null;
		endVersion = null;
		url = null;
		instructions = null;
		warning = null;
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

		public Patch build() {
			return patch;
		}
	}

	@Override
	public String toString() {
		return "Version " + startVersion + " to " + endVersion;
	}

	public boolean isNeeded(String startVersion, String endVersion) {
		if (startVersion.compareTo(this.startVersion) > 0)
			return false;
		if (startVersion.compareTo(this.endVersion) > 0)
			return false;
		if (endVersion.compareTo(this.endVersion) < 0)
			return false;
		if (endVersion.compareTo(this.startVersion) < 0)
			return false;
		return true;
	}

	public boolean isProblem() {
		return warning != null;
	}
}
