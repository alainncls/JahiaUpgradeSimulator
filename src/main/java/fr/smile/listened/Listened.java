package fr.smile.listened;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class Listened<E> {

    public static final Integer OK = 0;
    public static final Integer ERROR = 1;

    protected Integer result = OK;
    protected final Set<E> listeners = new CopyOnWriteArraySet<E>();

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public final void addListener(final E listener) {
        listeners.add(listener);
    }

    public final void removeListener(final E listener) {
        listeners.remove(listener);
    }
}
