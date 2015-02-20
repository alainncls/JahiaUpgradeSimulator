package fr.smile.main;

import java.util.List;
import java.util.Scanner;

public class InputCheck {
	private static Scanner keyboard = new Scanner(System.in);

	public int askInputInt(String informationText) {
		Boolean error = false;
		String userInp = "";
		do {
			userInp = askInputString(informationText);
			if (!this.isType(userInp, "int")) {
				error = true;
				System.err.println("Error: must be a whole number.");
			} else {
				error = false;
			}
		} while (error);
		return Integer.parseInt(userInp);
	}

	public String askInputVersion(String informationText,
			List<String> listVersions, String versionDefault) {
		String tempVersion = "";
		Boolean error = false;

		do {
			tempVersion = askInputString(informationText, versionDefault);

			while (!listVersions.contains(tempVersion)) {
				System.err.println("Wrong input, version not found.");
				tempVersion = askInputString(informationText, versionDefault);
			}
		} while (error == true);

		return tempVersion;
	}

	public String warnWrongInput(String type) {
		return "Wrong input, " + type + " expected !";
	}

	public String askInputString(String informationText) {
		System.out.print(informationText);
		String stringTemp = keyboard.nextLine();

		return stringTemp;
	}

	public String askInputString(String informationText, String defaultValue) {
		String stringTemp = askInputString(informationText);

		if (stringTemp.equals("")) {
			stringTemp = defaultValue;
		}

		return stringTemp;
	}

	/**
	 * A method to repeatedly ask the user for input until the input is valid.
	 * If condition is used, input is measured against it.
	 *
	 * @param informationText
	 *            The information text to prompt to the user.
	 * @return Returns the final value of the accepted input, as a double.
	 */
	public double askInputDouble(String informationText) {
		Boolean error = false;
		String userInp = "";
		do {
			userInp = askInputString(informationText);
			if (!this.isType(userInp, "double")) {
				System.err.println("Error: must be a number.");
				error = true;
			} else {
				error = false;
			}

		} while (error == true);
		return Double.parseDouble(userInp);
	}

	/**
	 * A method to repeatedly ask the user for input until the input is valid.
	 * If condition is used, input is measured against it.
	 *
	 * @param informationText
	 *            The information text to prompt to the user.
	 * @return Returns the final value of the accepted input, as a float.
	 */
	public float askInputFloat(String informationText) {
		Boolean error = false;
		String userInp = "";
		do {
			userInp = askInputString(informationText);
			// validate:
			if (!this.isType(userInp, "float")) {
				System.err.println("Error: must be a number.");
				error = true;
			} else {
				error = false;
			}

		} while (error == true);
		return Float.parseFloat(userInp);
	}

	/**
	 * Tests if a specific input can be converted to a specific type.
	 *
	 * @param input
	 *            The input to test. Accepts String, int, double or float.
	 * @param type
	 *            Which type to test against. Accepts 'int','float' or 'double'.
	 * @return Boolean True if can be transformed to requested type. False
	 *         otherwise.
	 */
	public Boolean isType(String testStr, String type) {
		try {
			if (type.equalsIgnoreCase("float")) {
				Float.parseFloat(testStr);
			} else if (type.equalsIgnoreCase("int")) {
				Integer.parseInt(testStr);
			} else if (type.equalsIgnoreCase("double")) {
				Double.parseDouble(testStr);
			}
			return true;
		} catch (Exception e) {
			return false;
		}

	}

}