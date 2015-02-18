package fr.smile.fiches;

import java.awt.Color;

import javax.swing.JButton;

import fr.smile.listeners.RunnableListener;
import fr.smile.main.Patch;
import fr.smile.services.DownloadService;
import fr.smile.services.JahiaConfigService;
import fr.smile.services.PatchService;
import fr.smile.tasks.DownloadTask;
import fr.smile.tasks.PatchTask;

public class ActionButton extends JButton implements RunnableListener {

	private static final long serialVersionUID = 1L;

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
	private Patch patch;

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
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		if (patch.isFixApplier()) {
			status = APPLY;
		} else {
			status = APPLY_MANUALLY;
		}
		
		this.status = status;
		
		switch (status) {
		case DOWNLOAD:
			setBackground(null);
			setText("Download");
			setEnabled(true);
			break;
		case DOWNLOADING:
			setBackground(Color.BLUE);
			setText("Downloading...");
			setEnabled(false);
			break;
		case APPLY:
			setBackground(Color.GREEN);
			setText("Apply");
			setEnabled(true);
			break;
		case APPLY_MANUALLY:
			setBackground(Color.GREEN);
			setText("Apply Manually");
			setEnabled(true);
			break;
		case APPLYING:
			setBackground(Color.BLUE);
			setText("Running...");
			setEnabled(false);
			break;
		case DONE:
			setBackground(Color.GREEN);
			setText("Done !");
			setEnabled(false);
			break;
		case ERROR_DOWNLOAD:
		case ERROR_APPLY:
			setBackground(Color.RED);
			setText("Error, try again");
			setEnabled(true);
			break;
		case WAITING:
			setBackground(Color.YELLOW);
			setText("Waiting..");
			setEnabled(false);
			break;
		default:
			break;
		}
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
		if (status != DOWNLOAD && status != ERROR_DOWNLOAD)
			return;
		setStatus(WAITING);
		DownloadService.getInstance().download(patch, this);
	}

	public void doApply() {
		if (status == APPLY || status == ERROR_APPLY) {
			setStatus(WAITING);
			PatchService.getInstance().apply(patch, this);
		} else if (status == APPLY_MANUALLY) {
			JahiaConfigService.getInstance().detectJahiaVersion();
		}
	}

	@Override
	public void notifyComplete(Runnable runnable) {
		if (runnable instanceof DownloadTask) {
			DownloadTask task = (DownloadTask) runnable;
			if (task.getResult() == DownloadTask.OK) {
				System.out.println("Complete download : " + patch.toString());
				setStatus(APPLY);
			} else {
				System.err.println("Error download : " + patch.toString());
				setStatus(ERROR_DOWNLOAD);
			}
		} else if (runnable instanceof PatchTask) {
			PatchTask task = (PatchTask) runnable;
			if (task.getResult() == PatchTask.OK) {
				System.out.println("Complete patch : " + patch.toString());
				setStatus(DONE);
			} else {
				System.err.println("Error patch : " + patch.toString());
				setStatus(ERROR_APPLY);
			}
		}
	}

	@Override
	public void notifyStart(Runnable runnable) {
		if (runnable instanceof DownloadTask) {
			System.out.println("Begin download : " + patch.toString());
			setStatus(DOWNLOADING);
		} else if (runnable instanceof PatchTask) {
			System.out.println("Begin patch : " + patch.toString());
			setStatus(APPLYING);
		}
	}
}
