package fr.smile.fiches;

import java.awt.Color;

import javax.swing.JButton;

import fr.smile.main.Patch;
import fr.smile.services.DownloadService;
import fr.smile.services.DownloadTask;
import fr.smile.services.RunnableCompleteListener;

public class ActionButton extends JButton implements RunnableCompleteListener {

	private static final long serialVersionUID = 1L;

	public static final int DOWNLOAD = 0;
	public static final int DOWNLOADING = 1;
	public static final int APPLY = 2;
	public static final int APPLYING = 3;
	public static final int DONE = 4;
	public static final int ERROR_DOWNLOAD = 5;
	public static final int ERROR_APPLY = 6;

	private int status;
	private Patch patch;

	public ActionButton(Patch patch, int status) {
		this.patch = patch;
		setStatus(status);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
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
		case APPLYING:
			setBackground(Color.BLUE);
			setText("Running...");
			setEnabled(false);
			break;
		case DONE:
			setBackground(Color.GREEN);
			setText("Done !");
			setEnabled(true);
			break;
		case ERROR_DOWNLOAD:
		case ERROR_APPLY:
			setBackground(Color.RED);
			setText("Error, try again");
			setEnabled(true);
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
		case ERROR_APPLY:
			doApply();
			break;
		default:
			break;
		}
	}

	public void doDownload() {
		if (status != DOWNLOAD)
			return;
		DownloadService.getInstance().download(patch, this);
		setStatus(DOWNLOADING);
	}

	public void doApply() {
		if (status != APPLY)
			return;
		setStatus(APPLYING);
	}

	@Override
	public void notifyComplete(Runnable runnable) {
		DownloadTask task;
		if (runnable instanceof DownloadTask) {
			task = (DownloadTask) runnable;
			if (task.getResult() == DownloadTask.OK) {
				setStatus(APPLY);
			} else {
				setStatus(ERROR_DOWNLOAD);
			}
//		} else if (runnable instanceof ApplierTask) {
//			if (task.getResult() == ApplierTask.OK) {
//				setStatus(DONE);
//			} else {
//				setStatus(ERROR_APPLY);
//			}
		}
	}

}