package fr.smile.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ShowStreamTask implements Runnable {
	
	private final InputStream is;
	
	public ShowStreamTask(InputStream is) {
		this.is = is;
	}
	
	private BufferedReader getBufferedReader(InputStream is) {
        return new BufferedReader(new InputStreamReader(is));
    }
	
	@Override
	public void run() {
		BufferedReader br = getBufferedReader(is);
        String ligne = "";
        try {
            while ((ligne = br.readLine()) != null) {
                System.out.println(ligne);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

}
