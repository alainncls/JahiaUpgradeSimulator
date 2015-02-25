package fr.smile.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.smile.fiches.FAccueil;
import fr.smile.services.JahiaConfigService;

public class Main {

	private static String context;
	private static String jahiaFolder;
	private static Boolean hidden;

	static final Logger logger = LogManager.getLogger(FAccueil.class.getName());

	public static void main(final String[] args) {
		hidden = args.length >= 3 ? true : false;
		context = args.length >= 2 ? args[1] : "ROOT";
		jahiaFolder = args.length >= 1 ? args[0] : "./";

		logger.debug("Hello world.");
		logger.info("What a beatiful day.");
		logger.warn("What a beatiful day.");

		logger.debug(context);
		logger.debug(jahiaFolder);

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
