package fr.smile.reader;

//**** IMPORTS ****
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public enum InstructionsReader {

	INSTANCE;

	// **** ATTRIBUTES ****
	public static String path;
	static StringBuilder builder = new StringBuilder();

	// **** BUILDER ****
	// private InstructionsReader(String installType) throws
	// FileNotFoundException {
	// path = "data/instructions/patch_" + installType + ".txt";
	// readFile(); // Reading the file line by line
	// }

	private InstructionsReader() {

	}

	public static void create(String installType) {
		path = "data/instructions/patch_" + installType + ".txt";
		try {
			readFile();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Reading the file line by line
	}

	public static InstructionsReader getInstance() {
		return INSTANCE;
	}

	// Method to read the file line by line (each line = a bloc)
	public static void readFile() throws FileNotFoundException {
		File file = new File(path);
		FileReader reader = new FileReader(file);
		BufferedReader buff = new BufferedReader(reader);
		Scanner scanner = new Scanner(buff); // Scanner on the buffer
		String scan; // Line read

		while (scanner.hasNext()) { // Wile there is an unread line in the file
			scan = scanner.nextLine(); // Go to next line
			builder.append(scan); // Add it in the right list
		}
		scanner.close(); // Close this scanner
	}

	public String getInstructions() {
		return builder.toString();
	}
}
