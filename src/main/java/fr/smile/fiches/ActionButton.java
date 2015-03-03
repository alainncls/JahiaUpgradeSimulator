package fr.smile.fiches;

import java.awt.Color;

import javax.swing.JButton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.smile.listeners.DownloadServiceListener;
import fr.smile.listeners.JahiaConfigServiceListener;
import fr.smile.listeners.PatchServiceListener;
import fr.smile.models.Patch;
import fr.smile.services.DownloadService;
import fr.smile.services.JahiaConfigService;
import fr.smile.services.PatchService;

@SuppressWarnings("serial")
public class ActionButton extends JButton implements DownloadServiceListener,
        PatchServiceListener, JahiaConfigServiceListener {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ActionButton.class);

    public static final int DOWNLOAD = 0;
    public static final int DOWNLOADING = 1;
    public static final int APPLY = 2;
    public static final int APPLY_MANUALLY = 3;
    public static final int APPLYING = 4;
    public static final int DONE = 5;
    public static final int ERROR_DOWNLOAD = 6;
    public static final int ERROR_APPLY = 7;
    public static final int WAITING = 8;

    private int status;
    private transient Patch patch;

    public ActionButton(Patch patch) {
        this.patch = patch;
        String version = JahiaConfigService.getInstance().getVersion();
        if (version != null && version.compareTo(patch.getStartVersion()) > 0) {
            setStatus(DONE);
        } else if (DownloadService.getInstance().exist(patch)) {
            setStatus(APPLY);
        } else {
            setStatus(DOWNLOAD);
        }
        JahiaConfigService.getInstance().addListener(this);
        DownloadService.getInstance().addListener(this);
        PatchService.getInstance().addListener(this);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        int stat = detectStatus(status);

        switch (stat) {
        case DOWNLOAD:
            setBackground(null);
            setText("Download");
            break;
        case DOWNLOADING:
            setBackground(Color.BLUE);
            setText("Downloading...");
            break;
        case APPLY:
            setBackground(Color.GREEN);
            setText("Apply");
            break;
        case APPLY_MANUALLY:
            setBackground(Color.GREEN);
            setText("Apply Manually");
            break;
        case APPLYING:
            setBackground(Color.BLUE);
            setText("Running...");
            break;
        case DONE:
            setBackground(Color.GREEN);
            setText("Done !");
            break;
        case ERROR_DOWNLOAD:
        case ERROR_APPLY:
            setBackground(Color.RED);
            setText("Error, try again");
            break;
        case WAITING:
            setBackground(Color.YELLOW);
            setText("Waiting..");
            break;
        default:
            break;
        }
    }

    public int detectStatus(int status) {
        int stat = 0;

        if (status == APPLY && !patch.isFixApplier()) {
            this.status = APPLY_MANUALLY;
        } else {
            this.status = status;
        }
        setEnabled(true);

        String version = JahiaConfigService.getInstance().getVersion();
        if (status == APPLY && !patch.getStartVersion().equals(version)) {
            setEnabled(false);
        }

        return stat;
    }

    public Patch getPatch() {
        return patch;
    }

    public void setPatch(Patch patch) {
        this.patch = patch;
    }

    public void doAction() {
        switch (status) {
        case DOWNLOAD:
        case ERROR_DOWNLOAD:
            doDownload();
            break;
        case APPLY:
        case APPLY_MANUALLY:
        case ERROR_APPLY:
            doApply();
            break;
        default:
            break;
        }
    }

    public void doDownload() {
        if (status != DOWNLOAD && status != ERROR_DOWNLOAD) {
            return;
        }
        setStatus(WAITING);
        DownloadService.getInstance().download(patch);
    }

    public void doApply() {
        if (status == APPLY || status == ERROR_APPLY) {
            setStatus(WAITING);
            PatchService.getInstance().apply(patch);
        } else if (status == APPLY_MANUALLY) {
            JahiaConfigService.getInstance().detectJahiaVersion();
        }
    }

    @Override
    public void notifyVersionChange(String version) {
        if (version != null) {
            if (patch.getStartVersion().equals(version) && status == APPLY) {
                setEnabled(true);
            }
            if (patch.getStartVersion().compareTo(version) > 0
                    && status == APPLY) {
                setEnabled(false);
            }
            if (patch.getEndVersion().compareTo(version) <= 0) {
                setStatus(DONE);
            }
        }
    }

    @Override
    public void notifyPatchStart(Patch patch) {
        if (this.patch.equals(patch)) {
            LOGGER.info("Begin apply patch (" + patch.toString() + ")");
            setStatus(APPLYING);
        }
    }

    @Override
    public void notifyPatchComplete(Patch patch, int result) {
        if (this.patch.equals(patch)) {
            if (result == PatchService.OK) {
                LOGGER.info("Complete apply patch (" + patch.toString() + ")");
                setStatus(DONE);
            } else {
                LOGGER.error("Error applying patch (" + patch.toString() + ")");
                setStatus(ERROR_APPLY);
            }
        } else if (result == PatchService.OK && patch.isFollowedBy(this.patch)) {
            setEnabled(true);
        }
    }

    @Override
    public void notifyDownloadStart(Patch patch) {
        if (this.patch.equals(patch)) {
            LOGGER.info("Begin download patch (" + patch.toString() + ")");
            setStatus(DOWNLOADING);
        }
    }

    @Override
    public void notifyDownloadComplete(Patch patch, int result) {
        if (this.patch.equals(patch)) {
            if (result == DownloadService.OK) {
                LOGGER.info("Complete download patch: " + patch.toString());
                setStatus(APPLY);
            } else {
                LOGGER.error("Error download patch: " + patch.toString());
                setStatus(ERROR_DOWNLOAD);
            }
        }
    }

}
