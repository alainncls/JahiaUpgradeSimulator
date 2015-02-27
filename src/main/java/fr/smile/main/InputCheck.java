package fr.smile.main;

import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputCheck {
	private static Scanner keyboard = new Scanner(System.in);
	private static final Logger LOGGER = LoggerFactory
			.getLogger(InputCheck.class);

	public String askInput(String informationText) {
		System.out.print(informationText);
		String stringTemp = keyboard.nextLine();
		return stringTemp;
	}

	public String askInput(String informationText, String defaultValue) {
		String stringTemp = askInput(informationText);
		if ("".equals(stringTemp)) {
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
		String userInp = askInput(informationText, null, "double");
		return Double.parseDouble(userInp);
	}

	public double askInputDouble(String informationText, double defaultValue) {
		String userInp = askInput(informationText,
				String.valueOf(defaultValue), "double");
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
		String userInp = askInput(informationText, null, "float");
		return Float.parseFloat(userInp);
	}

	public float askInputFloat(String informationText, float defaultValue) {
		String userInp = askInput(informationText,
				String.valueOf(defaultValue), "float");
		return Float.parseFloat(userInp);
	}

	/**
	 * A method to repeatedly ask the user for input until the input is valid.
	 * If condition is used, input is measured against it.
	 *
	 * @param informationText
	 *            The information text to prompt to the user.
	 * @return Returns the final value of the accepted input, as an int.
	 */
	public int askInputInt(String informationText) {
		String userInp = askInput(informationText, null, "int");
		return Integer.parseInt(userInp);
	}

	public int askInputInt(String informationText, int defaultValue) {
		String userInp = askInput(informationText,
				String.valueOf(defaultValue), "int");
		return Integer.parseInt(userInp);
	}

	private String askInput(String informationText, String defaultValue,
			String type) {

		String userInp = askInput(informationText, defaultValue);
		while (!isType(userInp, type)) {
			System.err.println("Error: must be a type " + type);
			userInp = askInput(informationText, defaultValue);
		}
		return userInp;
	}

	public String askInputVersion(String informationText,
			List<String> listVersions, String versionDefault) {

		String tempVersion = askInput(informationText, versionDefault);
		while (!listVersions.contains(tempVersion)) {
			System.err.println("Wrong input, version not found.");
			tempVersion = askInput(informationText, versionDefault);
		}
		return tempVersion;
	}

	public String warnWrongInput(String type) {
		return "Wrong input, " + type + " expected !";
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
	private Boolean isType(String testStr, String type) {
		try {
			if ("float".equalsIgnoreCase(type)) {
				Float.parseFloat(testStr);
			} else if ("int".equalsIgnoreCase(type)) {
				Integer.parseInt(testStr);
			} else if ("double".equalsIgnoreCase(type)) {
				Double.parseDouble(testStr);
			}
			return true;
		} catch (Exception e) {
			LOGGER.error("", e);
			return false;
		}
	}
}