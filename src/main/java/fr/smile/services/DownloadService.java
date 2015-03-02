package fr.smile.services;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.smile.listened.Listened;
import fr.smile.listeners.DownloadServiceListener;
import fr.smile.listeners.RunnableListener;
import fr.smile.models.Patch;
import fr.smile.tasks.DownloadTask;

public class DownloadService extends Listened<DownloadServiceListener>
        implements RunnableListener {

    private static DownloadService instance;

    private ExecutorService pool;
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DownloadService.class);

    public static final String PATH = "./patches/";

    // **** BUILDER ****
    private DownloadService() {
        pool = Executors.newFixedThreadPool(2);
    }

    public static synchronized DownloadService getInstance() {
        if (instance == null) {
            instance = new DownloadService();
        }
        return instance;
    }

    public void download(Patch patch) {
        if (!this.exist(patch)) {
            DownloadTask task = new DownloadTask(patch, PATH);
            task.addListener(this);
            pool.execute(task);
            LOGGER.info("DownloadTask added to pool (" + patch.toString() + ")");
        }
    }

    public boolean exist(Patch patch) {
        File file = new File(PATH + patch.getName());
        if (file.exists()) {
            LOGGER.info("Found Patch in download folder (" + patch.toString()
                    + ")");
            return true;
        }
        return false;
    }

    public void terminate() {
        pool.shutdown();
        try {
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    public void notifyRunnableStart(Runnable runnable) {
        Patch p = ((DownloadTask) runnable).getPatch();
        LOGGER.info("DownloadTask launched (" + p.toString() + ")");
        for (DownloadServiceListener listener : listeners) {
            listener.notifyDownloadStart(p);
        }
    }

    @Override
    public void notifyRunnableComplete(Runnable runnable, int result) {
        Patch p = ((DownloadTask) runnable).getPatch();
        LOGGER.info("DownloadTask ended  (" + p.toString() + ") : " + result);
        for (DownloadServiceListener listener : listeners) {
            listener.notifyDownloadComplete(p, result);
        }
    }
}
