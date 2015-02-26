package fr.smile.listeners;

import fr.smile.models.Patch;

public interface PatchServiceListener {
	void notifyPatchStart(final Patch patch);

	void notifyPatchComplete(final Patch patch, final int result);

}
