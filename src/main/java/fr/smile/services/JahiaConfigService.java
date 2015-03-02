package fr.smile.services;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.smile.listened.Listened;
import fr.smile.listeners.JahiaConfigServiceListener;

public class JahiaConfigService extends Listened<JahiaConfigServiceListener> {

    private String folder;
    private String context;
    private String version;
    private Boolean clustered;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(JahiaConfigService.class);

    private static JahiaConfigService instance;

    private JahiaConfigService() {
        version = null;
        folder = "./";
        context = "ROOT";
        clustered = false;
    }

    public static synchronized JahiaConfigService getInstance() {
        if (instance == null) {
            instance = new JahiaConfigService();
        }
        return instance;
    }

    public Boolean getClustered() {
        return clustered;
    }

    public void setClustered(Boolean clustered) {
        this.clustered = clustered;
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
        if (!folder.endsWith("/")) {
            this.folder = folder + "/";
        } else {
            this.folder = folder;
        }
        this.detectJahiaVersion();
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
                LOGGER.info("Detected Version : " + version);
            } else {
                LOGGER.warn("WARNING : Version not found");
            }
        } catch (Exception e) {
            LOGGER.error("Fail to detect", e);
        }
        if (version != null && !version.equals(old)) {
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

    private void notifyVersionChange() {
        for (JahiaConfigServiceListener listener : listeners) {
            listener.notifyVersionChange(version);
        }
    }
}
