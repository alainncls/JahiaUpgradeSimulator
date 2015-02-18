package fr.smile.services;

import java.io.File;
import java.io.FileFilter;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.regex.Pattern;

import fr.smile.listeners.JahiaConfigListener;

public enum JahiaConfigService {

	INSTANCE;

	private String folder;
	private String context;
	private String version;

	private final Set<JahiaConfigListener> listeners = new CopyOnWriteArraySet<JahiaConfigListener>();

	// **** BUILDER ****
	private JahiaConfigService() {
		version = null;
		folder = "./";
		context = "ROOT";
	}

	public static JahiaConfigService getInstance() {
		return INSTANCE;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getPatchFolder() {
		return folder + "tomcat/webapps/" + context;
	}

	public void detectJahiaVersion() {
		String old = version;
		try {
			File[] files = listFilesMatching(new File(getPatchFolder()
					+ "/WEB-INF/lib/"), "jahia-impl-(\\d\\.){4}jar");
			if (files.length > 0) {
				version = files[0].getName().substring(11, 18);
				System.out.println("Detected Version : " + version);
			} else {
				System.err.println("WARNING : Version not found");
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println("Fail to detect");
		}
		if (old != null && version != null && !old.equals(version)) {
			notifyVersionChange();
		}
	}

	private File[] listFilesMatching(File root, String regex) {
		if (!root.isDirectory()) {
			throw new IllegalArgumentException(root + " is no directory.");
		}
		final Pattern p = Pattern.compile(regex); // careful: could also throw
													// an exception!
		return root.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return p.matcher(file.getName()).matches();
			}
		});
	}

	public void addListener(final JahiaConfigListener listener) {
		listeners.add(listener);
	}

	public void removeListener(final JahiaConfigListener listener) {
		listeners.remove(listener);
	}

	private void notifyVersionChange() {
		for (JahiaConfigListener listener : listeners) {
			listener.notifyVersionChange();
		}
	}
}
