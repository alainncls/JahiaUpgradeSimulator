package fr.smile.main;

public class Patch {

    private String startVersion;
    private String endVersion;
    private String type;
    private String url;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

        public Builder type(String type) {
            patch.setType(type);
            return this;
        }

        public Patch build() {
            return patch;
        }
    }

    @Override
    public String toString() {
        return "Version " + startVersion +
                " to " + endVersion +
                (type!=null?", " + type:"");
    }

    public boolean isNeeded(String startVersion, String endVersion) {
        if(startVersion.compareTo(this.startVersion)>0)
            return false;
        if(startVersion.compareTo(this.endVersion)>0)
            return false;
        if(endVersion.compareTo(this.endVersion)<0)
            return false;
        if(endVersion.compareTo(this.startVersion)<0)
            return false;
        return true;
    }

	public boolean isProblem() {
		return false;
	}
}
