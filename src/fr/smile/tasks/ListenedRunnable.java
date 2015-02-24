package fr.smile.tasks;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import fr.smile.listeners.RunnableListener;

public abstract class ListenedRunnable implements Runnable {

	protected final Set<RunnableListener> listeners = new CopyOnWriteArraySet<RunnableListener>();

	public static final Integer OK = 0;
	public static final Integer ERROR = 1;

	protected Integer result = OK;

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public final void addListener(final RunnableListener listener) {
		listeners.add(listener);
	}

	public final void removeListener(final RunnableListener listener) {
		listeners.remove(listener);
	}

	protected final void notifyComplete() {
		for (RunnableListener listener : listeners) {
			listener.notifyComplete(this);
		}
	}

	protected final void notifyStart() {
		for (RunnableListener listener : listeners) {
			listener.notifyStart(this);
		}
	}
}
