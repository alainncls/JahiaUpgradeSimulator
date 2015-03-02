package fr.smile.listened;

import fr.smile.listeners.RunnableListener;

public abstract class ListenedRunnable extends Listened<RunnableListener>
        implements Runnable {

    protected final void notifyStart() {
        for (RunnableListener listener : listeners) {
            listener.notifyRunnableStart(this);
        }
    }

    protected final void notifyComplete() {
        for (RunnableListener listener : listeners) {
            listener.notifyRunnableComplete(this, result);
        }
    }

}
