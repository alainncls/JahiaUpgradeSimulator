package fr.smile.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.swing.JOptionPane;

public class ShowStreamTask implements Runnable {
	
	private final Set<RunnableListener> listeners = new CopyOnWriteArraySet<RunnableListener>();
	
	public static final Integer OK = 0;
	public static final Integer ERROR = 1;
	
	public static final Integer INPUT_STREAM = 0;
	public static final Integer OUTPUT_STREAM = 1;
	public static final Integer ERROR_STREAM = 2;
	
	private Integer result;
	private Integer type;
	
	private final InputStream is;
	
	public ShowStreamTask(InputStream is, Integer type) {
		this.is = is;
		this.type = type;
		result = OK;
	}
	
	private BufferedReader getBufferedReader(InputStream is) {
        return new BufferedReader(new InputStreamReader(is));
    }
	
	@Override
	public void run() {
		try {
			notifyStart();
			showStream();
		} finally {
			notifyComplete();
		}
	}
	
	private void showStream() {
		BufferedReader br = getBufferedReader(is);
        String line = "";
        try {
            while ((line = br.readLine()) != null) {
            	//System.out.println(line);
            	if(type == ERROR_STREAM){
            		JOptionPane.showMessageDialog(null, line, "Error", JOptionPane.ERROR_MESSAGE);
                	result = ERROR;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = ERROR;
        }
	}
	
	public int getResult() {
		return result;
	}
	
	public InputStream getStream() {
		return is;
	}
	
	public final void addListener(final RunnableListener listener) {
		listeners.add(listener);
	}

	public final void removeListener(final RunnableListener listener) {
		listeners.remove(listener);
	}

	private final void notifyComplete() {
		for (RunnableListener listener : listeners) {
			listener.notifyComplete(this);
		}
	}
	
	private final void notifyStart() {
		for (RunnableListener listener : listeners) {
			listener.notifyStart(this);
		}
	}

}
