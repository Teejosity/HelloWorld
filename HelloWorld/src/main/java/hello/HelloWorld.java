package hello;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class HelloWorld {
/**
 * Starts the game using a file specified by args; otherwise, it uses console input
 * @param args The path to the file to be used for input (If there is one)
 */
	public static void main(String[] args) {
		
		if(args.length < 1) {
				Scanner scan = new Scanner(System.in);
				TextRPG rpg = new TextRPG(scan);
				Application app = new Application(rpg);
				Thread t = new Thread(app);
				t.start();
				rpg.run();
			}
		else {	
			File file = new File(args[0]); 
			TextRPG rpg;
			try {
				Scanner scanFile = new Scanner(file);
				scanFile.useDelimiter("\n");
				System.out.println(file.getPath());
				rpg = new TextRPG(scanFile);
				Application app = new Application(rpg);
				Thread t = new Thread(app);
				t.start();
				rpg.run();
			} catch (FileNotFoundException e) {
				System.out.println("No file found.");
			}
		}
		
	}

}
