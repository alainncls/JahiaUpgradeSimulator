package fr.smile.listeners;

public interface RunnableListener {

	void notifyRunnableStart(final Runnable runnable);

	void notifyRunnableComplete(final Runnable runnable, final int result);

}
