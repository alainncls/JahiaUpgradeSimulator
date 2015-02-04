package fr.smile.reader;

//**** IMPORTS ****
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public enum InstructionsReader {

	INSTANCE;

	// **** ATTRIBUTES ****
	public String path;
	static StringBuilder builder = new StringBuilder();

	// **** BUILDER ****
	// private InstructionsReader(String installType) throws
	// FileNotFoundException {
	// path = "data/instructions/patch_" + installType + ".txt";
	// readFile(); // Reading the file line by line
	// }

	private InstructionsReader() {

	}

	public void create(String installType) {
		path = "/patch_" + installType + ".txt";
		try {
			readFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Reading the file line by line
	}

	public static InstructionsReader getInstance() {
		return INSTANCE;
	}

	// Method to read the file line by line (each line = a bloc)
	public void readFile() throws IOException {
		InputStream is = getClass().getResourceAsStream(path);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader buff = new BufferedReader(isr);
		Scanner scanner = new Scanner(buff); // Scanner on the buffer
		String scan; // Line read

		while (scanner.hasNext()) { // Wile there is an unread line in the file
			scan = scanner.nextLine(); // Go to next line
			builder.append(scan); // Add it in the right list
		}
		scanner.close(); // Close this scanner

		buff.close();
		isr.close();
		is.close();

	}

	public String getInstructions() {
		return builder.toString();
	}
}
