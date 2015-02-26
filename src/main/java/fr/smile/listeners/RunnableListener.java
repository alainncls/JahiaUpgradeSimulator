package fr.smile.listeners;

public interface RunnableListener {
	void notifyComplete(final Runnable runnable);

	void notifyStart(final Runnable runnable);
}
