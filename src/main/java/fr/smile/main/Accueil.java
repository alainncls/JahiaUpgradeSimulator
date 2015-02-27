package fr.smile.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import fr.smile.fiches.ActionButton;
import fr.smile.models.Patch;
import fr.smile.models.Simulation;
import fr.smile.services.JahiaConfigService;
import fr.smile.services.PatchService;

public class Accueil {
	private Simulation simul;
	private String startVersion, endVersion, detectedVersion;
	private List<String> listVersions;
	private List<ActionButton> listActionButtons;
	private Scanner sc;

	public Accueil() {
		simul = null;

		detectedVersion = JahiaConfigService.getInstance().getVersion();
		listVersions = PatchService.getInstance().getVersions();

		if (detectedVersion != null) {
			startVersion = detectedVersion;
		} else {
			startVersion = listVersions.get(0);
		}

		endVersion = listVersions.get(listVersions.size() - 1);

	}

	private void dispGreet() {
		System.out
				.println("        ____.        .__     .__           ____ ___                                      .___          \n"
						+ "       |    |_____   |  |__  |__|_____    |    |   \\______     ____  _______ _____     __| _/  ____    \n"
						+ "       |    |\\__  \\  |  |  \\ |  |\\__  \\   |    |   /\\____ \\   / ___\\ \\_  __ \\\\__  \\   / __ | _/ __ \\   \n"
						+ "   /\\__|    | / __ \\_|   Y  \\|  | / __ \\_ |    |  / |  |_> > / /_/  > |  | \\/ / __ \\_/ /_/ | \\  ___/   \n"
						+ "   \\________|(____  /|___|  /|__|(____  / |______/  |   __/  \\___  /  |__|   (____  /\\____ |  \\___  >  \n"
						+ "                  \\/      \\/          \\/            |__|    /_____/               \\/      \\/      \\/   \n"
						+ "                     _________.__                 .__             __                                   \n"
						+ "                    /   _____/|__|  _____   __ __ |  |  _____   _/  |_   ____  _______                 \n"
						+ "                    \\_____  \\ |  | /     \\ |  |  \\|  |  \\__  \\  \\   __\\ /  _ \\ \\_  __ \\                \n"
						+ "                    /        \\|  ||  Y Y  \\|  |  /|  |__ / __ \\_ |  |  (  <_> ) |  | \\/                \n"
						+ "                   /_______  /|__||__|_|  /|____/ |____/(____  / |__|   \\____/  |__|                   \n"
						+ "                           \\/           \\/                   \\/                                        \n");
		System.out
				.println(""
						+ "************************************************** VERSION 0.6 **************************************************");
	}

	private void dispMenu() {
		int choice = 0;
		InputCheck input = new InputCheck();

		do {
			sc = new Scanner(System.in);

			System.out.println("\n=== MAIN MENU ===\n");
			System.out.println("1. Configuration Jahia");
			System.out.println("2. Simulate");
			System.out.println("3. Exit");

			choice = input.askInputInt("Your choice : ");

			switch (choice) {
			case 1:
				dispConfiguration();
				break;

			case 2:
				dispSimulation();
				break;

			case 3:
				System.out.println("Bye !");
				break;

			default:
				System.err.println(input
						.warnWrongInput("integer between 1 & 3"));
				dispMenu();
			}
		} while (choice != 3);
	}

	private void dispMenuSimul() {
		int choice;
		InputCheck input = new InputCheck();

		do {
			sc = new Scanner(System.in);
			System.out.println("\n=== SIMULATION MENU ===\n");
			System.out.println("1. Show patches");
			System.out.println("2. Calculate costs");
			System.out.println("3. Back");

			choice = input.askInputInt("Your choice : ");

			switch (choice) {
			case 1:
				dispPatches();
				break;

			case 2:
				dispChiffrage();
				break;
			case 3:
				break;
			default:
				System.err.println(input
						.warnWrongInput("integer between 1 & 3"));
				dispMenu();
			}
		} while (choice != 3);
	}

	private void dispMenuChiffrage() {
		int choice;
		InputCheck input = new InputCheck();

		do {
			sc = new Scanner(System.in);
			System.out.println("\n=== COSTS MENU ===\n");
			System.out.println("1. Change 'UO'");
			System.out.println("2. Change 'TJM'");
			System.out.println("3. Back");

			choice = input.askInputInt("Your choice : ");

			switch (choice) {
			case 1:
				dispChangeUO();
				break;

			case 2:
				dispChangeTJM();
				break;
			case 3:
				break;
			default:
				System.err.println(input
						.warnWrongInput("integer between 1 & 3"));
				dispMenu();
			}
		} while (choice != 3);
	}

	private void dispChangeUO() {
		InputCheck input = new InputCheck();
		simul.calculateTotalDuration(input.askInputFloat(
				"UO value : (" + simul.getuO() + ") ", simul.getuO()));
		simul.calculateCost(simul.gettJM());
		dispChiffrage();
	}

	private void dispChangeTJM() {
		InputCheck input = new InputCheck();
		simul.settJM(input.askInputInt(
				"TJM value : (" + simul.gettJM() + "€) ", simul.gettJM()));
		simul.calculateCost(simul.gettJM());
		dispChiffrage();
	}

	private void dispConfiguration() {
		InputCheck input = new InputCheck();
		String defaultFolder = JahiaConfigService.getInstance().getFolder();
		String defaultContext = JahiaConfigService.getInstance().getContext();

		JahiaConfigService.getInstance().setFolder(
				input.askInput("Path to Jahia : (" + defaultFolder + ")",
						defaultFolder));
		JahiaConfigService.getInstance().setContext(
				input.askInput("Jahia context : (" + defaultContext + ")",
						defaultContext));
		JahiaConfigService.getInstance().detectJahiaVersion();
		detectedVersion = JahiaConfigService.getInstance().getVersion();
		if (detectedVersion != null) {
			startVersion = detectedVersion;
		}
	}

	private void dispSimulation() {
		InputCheck input = new InputCheck();

		System.out.println("\n--- SIMULATION INPUT ---");

		startVersion = input.askInputVersion("Start version (" + startVersion
				+ ") : ", listVersions, startVersion);
		endVersion = input.askInputVersion("End version (" + endVersion
				+ ") : ", listVersions, endVersion);

		simul = new Simulation(startVersion, endVersion, JahiaConfigService
				.getInstance().getClustered());

		if (simul.getError() != "") {
			System.err.println(simul.getError());
			dispSimulation();
		} else {
			listActionButtons = new ArrayList<ActionButton>();
			for (Patch p : simul.getListPatches()) {
				listActionButtons.add(new ActionButton(p));
			}
			dispMenuSimul();
		}
	}

	private void dispChiffrage() {
		System.out.println(simul.getChiffrage());
		dispWait();
		dispMenuChiffrage();
	}

	private void dispPatches() {
		int i, choice;
		InputCheck input = new InputCheck();

		i = choice = 0;

		StringBuilder tab = new StringBuilder();
		tab.append("\n");
		tab.append(String.format("%2s  %-15s   %-15s   %-15s%n", "#",
				"From version", "To version", "Action"));

		for (final Patch p : simul.getListPatches()) {
			tab.append(String.format("%2d  %-15s   %-15s   %-15s%n", i + 1, p
					.getStartVersion(), p.getEndVersion(), listActionButtons
					.get(i).getText()));
			i++;
		}

		System.out.println(tab.toString());

		choice = input.askInputInt("Your choice : ");

		if (choice <= i && choice > 0) {
			applyPatch(choice - 1);
		} else {
			System.err.println(input.warnWrongInput("integer between 1 & "
					+ listActionButtons.size()));
		}

		dispPatches();
	}

	private void applyPatch(int nb) {
		Patch p = listActionButtons.get(nb).getPatch();

		listActionButtons.get(nb).doAction(); // Download

		if (p.getWarning() != null) {
			dispWarning(p); // Display warning - wait
		}

		if (p.needLicense() != null) {
			dispLicense(p); // Display license
		}

		// TODO : Apply next
	}

	private void dispWait() {
		System.out.println("Press any key to continue ...");
		sc = new Scanner(System.in);
		sc.nextLine();
	}

	private void dispWarning(Patch p) {
		HtmlCleaner html = new HtmlCleaner();
		System.err.print("WARNING : ");
		System.out.println(html.warningCleaner(p.getWarning()));
		dispWait();
	}

	private void dispLicense(Patch p) {
		System.err.print("WARNING : ");
		System.out.println("License needed to go to version "
				+ p.getEndVersion());
		dispWait();
	}

	public static void main() {
		Accueil accueil = new Accueil();
		accueil.dispGreet();
		accueil.dispMenu();
	}
}
