package fr.smile.services;

//**** IMPORTS ****

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.smile.listened.Listened;
import fr.smile.listeners.PatchServiceListener;
import fr.smile.listeners.RunnableListener;
import fr.smile.models.Patch;
import fr.smile.tasks.PatchTask;

public class PatchService extends Listened<PatchServiceListener> implements
RunnableListener {

    public static final String PATH = "/versions.json";
    private List<String> listVersion;
    private List<Patch> listPatch;
    private ExecutorService pool;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(PatchService.class);

    private static PatchService instance;

    private PatchService() {

        pool = Executors.newSingleThreadExecutor();
        listVersion = new ArrayList<>();
        listPatch = new ArrayList<>();
        try {
            readFile(); // Reading the file line by line
        } catch (IOException e) {
            LOGGER.error("", e);
        }
    }

    public static synchronized PatchService getInstance() {
        if (instance == null) {
            instance = new PatchService();
        }
        return instance;
    }

    // Method to read the file line by line (each line = a bloc)
    public void readFile() throws IOException {

        InputStream is = getClass().getResourceAsStream(PATH);

        StringWriter writer = new StringWriter();
        IOUtils.copy(is, writer);
        String json = writer.toString();
        is.close();
        writer.close();

        JSONArray a = jsonParser(json);

        String version, endVersion, url, instructions, instructionsCluster, warning;
        Boolean reboot, license;

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
                instructions = (String) jsonPatch.get("instructions");
                instructionsCluster = (String) jsonPatch
                        .get("instructionsCluster");
                warning = (String) jsonPatch.get("warning");
                String r = (String) jsonPatch.get("reboot");
                reboot = r == null || "1".equals(r);
                String l = (String) jsonPatch.get("license");
                license = l != null && "1".equals(l);

                patch = Patch.builder().startVersion(version)
                        .endVersion(endVersion).url(url)
                        .instructionsCluster(instructionsCluster)
                        .instructions(instructions).warning(warning)
                        .reboot(reboot).license(license).build();

                listPatch.add(patch);
            }
        }
    }

    private JSONArray jsonParser(String json) {
        JSONParser parser = new JSONParser();
        JSONArray a = null;
        try {
            a = (JSONArray) parser.parse(json);
        } catch (ParseException e) {
            LOGGER.error("", e);
        }
        return a;
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
                        || patches.get(0).getEndVersion()
                        .compareTo(p.getEndVersion()) < 0) {
                    patches.add(0, p);
                } else {
                    patches.add(p);
                }
            }
        }
        return patches;
    }

    public void apply(Patch patch) {
        PatchTask task = new PatchTask(patch);
        task.addListener(this);
        pool.execute(task);
    }

    @Override
    public void notifyRunnableStart(Runnable runnable) {
        Patch p = ((PatchTask) runnable).getPatch();
        LOGGER.info("PatchTask launched (" + p.toString() + ")");
        for (PatchServiceListener listener : listeners) {
            listener.notifyPatchStart(p);
        }
    }

    @Override
    public void notifyRunnableComplete(Runnable runnable, int result) {
        // NO IDEA WHAT IM DOING
        // DELETE 'RES' IF PROBLEMS
        int res = OK;
        Patch p = ((PatchTask) runnable).getPatch();
        if (result == PatchTask.OK) {
            JahiaConfigService.getInstance().detectJahiaVersion();
            if (!JahiaConfigService.getInstance().getVersion()
                    .equals(p.getEndVersion())) {
                LOGGER.error("Error while applying patch, please check logs");
                res = ERROR;
            }
        }
        LOGGER.info("PatchTask ended  (" + p.toString() + ") : "
                + (result + res));
        for (PatchServiceListener listener : listeners) {
            listener.notifyPatchComplete(p, result + res);
        }
    }
}
