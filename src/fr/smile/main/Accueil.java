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

		dispGreet();
		dispMenu();
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
						+ "************************************************** VERSION 0.5 **************************************************");
	}

	private void dispMenu() {
		int choice;
		do {
			sc = new Scanner(System.in);

			System.out.println("\n=== MAIN MENU ===\n");
			System.out.println("1. Configuration Jahia");
			System.out.println("2. Simulate");
			System.out.println("3. Exit");
			System.out.print("Your choice : ");
			choice = sc.nextInt();

			switch (choice) {
			case 1:
				dispConfiguration();
				break;

			case 2:
				dispSimulation();
				break;

			case 3:
				System.out.println("Tchao !");
				break;

			default:
				System.err.println("Wrong input !");
				dispMenu();
			}
		} while (choice != 3);
	}

	private void dispMenuSimul() {
		int choice;
		do {
			sc = new Scanner(System.in);
			System.out.println("\n=== SIMULATION MENU ===\n");
			System.out.println("1. Show patches");
			System.out.println("2. Calculate costs");
			System.out.println("3. Back");
			System.out.print("Your choice : ");
			choice = sc.nextInt();

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
				System.err.println("Wrong input !");
				dispMenu();
			}
		} while (choice != 3);
	}

	private void dispConfiguration() {
		dispWorkInProgress();
	}

	private void dispSimulation() {
		System.out.println("\n--- SIMULATION INPUT ---");

		startVersion = selectVersion("Start", startVersion);
		endVersion = selectVersion("End", endVersion);
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
	}

	private void dispPatches() {
		int i = 0;
		int choice;
		System.out.println("\n");
		System.out.printf("%1s  %-15s   %-15s   %-15s%n", "#", "From version",
				"To version", "Action");
		for (final Patch p : simul.getListPatches()) {
			System.out.printf("%1d  %-15s   %-15s   %-15s%n", i + 1, p
					.getStartVersion(), p.getEndVersion(), listActionButtons
					.get(i).getText());
			i++;
		}
		System.out.print("Your choice : ");
		choice = sc.nextInt();

		listActionButtons.get(choice - 1).doAction();
		dispPatches();
	}

	private void applyPatch(int nb) {
		// Download / Afficher warning - wait / Apply / Reboot - License
		// Apply next
	}

	private void dispWait() {
		System.out.println("Press any key to continue ...");
		sc = new Scanner(System.in);
		sc.nextLine();
	}

	private void dispWorkInProgress() {
		System.err.println("Work in progress...");
		dispWait();
	}

	private String selectVersion(String name, String versionDefault) {
		sc = new Scanner(System.in);
		String tempVersion = "";

		System.out.print(name + " version (" + versionDefault + ") : ");
		tempVersion = sc.nextLine();

		if (tempVersion.equals("")) {
			tempVersion = versionDefault;
		}

		while (!listVersions.contains(tempVersion)) {
			System.err.println("Wrong input, version not found");
			System.out.print(name + " version (" + versionDefault + ") : ");
			tempVersion = sc.nextLine();
			if (tempVersion.equals("")) {
				tempVersion = versionDefault;
			}
		}

		return tempVersion;
	}
}
