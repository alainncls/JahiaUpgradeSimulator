package fr.smile.main;

import fr.smile.fiches.FAccueil;
import fr.smile.services.JahiaConfigService;

public class Main {

	private static String context;
	private static String jahiaFolder;
	private static Boolean hidden;

	public static void main(final String[] args) {
		hidden = args.length >= 3 ? true : false;
		context = args.length >= 2 ? args[1] : "ROOT";
		jahiaFolder = args.length >= 1 ? args[0] : "./";

		JahiaConfigService.getInstance().setFolder(jahiaFolder);
		JahiaConfigService.getInstance().setContext(context);
		JahiaConfigService.getInstance().detectJahiaVersion();

		if (hidden) {
			Accueil.main();
		} else {
			FAccueil.main();
		}
	}
}