package fr.smile.services;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import fr.smile.main.Patch;

public class PatchTask implements Runnable, RunnableListener {
	
	private final Set<RunnableListener> listeners = new CopyOnWriteArraySet<RunnableListener>();
	
	public static final Integer OK = 0;
	public static final Integer ERROR = 1;
	
	private int result;
	private Patch patch;
	
	private int complete;
	
	public PatchTask(Patch patch) {
		this.patch = patch;
		result = OK;
		complete = 0;
	}
	
	public int getResult() {
		return result;
	}
	
	@Override
	public void run() {
		notifyStart();
		if(patch.getStartVersion().equals(JahiaConfigService.getInstance().getVersion())){
			applyPatch();
		}else{
			result = ERROR;
			notifyComplete();
		}
	}
	
	private void applyPatch() {
		Process process;
        ProcessBuilder pb;
        ShowStreamTask fluxSortie, fluxErreur;
		try {
			pb = new ProcessBuilder("java", "-jar", System.getProperty("user.dir")+"/patches/"+patch.getName(), "-y");
            pb.directory(new File( JahiaConfigService.getInstance().getPatchFolder()));
            process = pb.start();
            
            fluxSortie = new ShowStreamTask(process.getInputStream(), ShowStreamTask.OUTPUT_STREAM);
	        fluxErreur = new ShowStreamTask(process.getErrorStream(), ShowStreamTask.ERROR_STREAM);
	        fluxSortie.addListener(this);
	        fluxErreur.addListener(this);

	        new Thread(fluxSortie).start();
	        new Thread(fluxErreur).start();

	        process.waitFor();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = ERROR;
		} finally {
			JahiaConfigService.getInstance().detectJahiaVersion();
		}
	}
	
	public final void addListener(final RunnableListener listener) {
		listeners.add(listener);
	}

	public final void removeListener(final RunnableListener listener) {
		listeners.remove(listener);
	}

	private final void notifyComplete() {
		System.out.println("Patch applied : "+patch.toString());
		for (RunnableListener listener : listeners) {
			listener.notifyComplete(this);
		}
	}
	
	private final void notifyStart() {
		System.out.println("Patch started : "+patch.toString());
		for (RunnableListener listener : listeners) {
			listener.notifyStart(this);
		}
	}

	@Override
	public void notifyComplete(Runnable runnable) {
		if (runnable instanceof ShowStreamTask) {
			complete++;
			if(complete==2){
				notifyComplete();
			}
		}
	}

	@Override
	public void notifyStart(Runnable runnable) {
	}

}
