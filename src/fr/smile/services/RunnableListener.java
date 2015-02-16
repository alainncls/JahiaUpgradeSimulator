package fr.smile.services;

public interface RunnableListener {
	void notifyComplete(final Runnable runnable);
	void notifyStart(final Runnable runnable);
}
