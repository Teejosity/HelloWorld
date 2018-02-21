import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class HelloWorld {
/**
 * Starts the game using a file specified by args; otherwise, it uses console input
 * @param args The path to the file to be used for input (If there is one)
 */
	public static void main(String[] args) {
    // TODO does this still work if you have no arguments? I don't think it will
    // hint: use a conditional to check the length of args before trying to access it
		File file = new File(args[0]); 
		TextRPG rpg;
		Scanner scan = new Scanner(System.in);
		try {
			Scanner scanFile = new Scanner(file);
			scanFile.useDelimiter("\n");
			System.out.println(file.getPath());
			rpg = new TextRPG(scanFile);
		} catch (FileNotFoundException e) {
			System.out.println("No file found.");
			rpg = new TextRPG(scan);
		}
		rpg.run();
	}

}
