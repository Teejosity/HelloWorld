package hello;

import java.io.File;

public class HelloWorld implements Runnable{
/**
 * Starts the game using a file specified by args; otherwise, it uses console input
 * @param args The path to the file to be used for input (If there is one)
 */
	private static TextRPG rpg;
	
	public static void main(String[] args) {
		
		if(args.length < 1) {
				rpg = new TextRPG();
				Application app = new Application(rpg);
				Thread t = new Thread(app);
				t.start();
				rpg.run();
			}
		else {	
			File file = new File(args[0]); 
			TextRPG rpg;
				rpg = new TextRPG(file);
				Application app = new Application(rpg);
				Thread t = new Thread(app);
				t.start();
				rpg.run();
		}	
	}
	
	public void run() {
		rpg.forceUpdate();
	}
}
