package fr.smile.listeners;

import fr.smile.models.Patch;

public interface DownloadServiceListener {
    void notifyDownloadStart(final Patch patch);

    void notifyDownloadComplete(final Patch patch, final int result);
}
