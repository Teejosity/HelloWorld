import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
public class HelloWorld {

	public static void main(String[] args) {
		//setting up rpg object and running
		File file = new File(args[0]);
		TextRPG rpg;
		Scanner scan = new Scanner(System.in);
		try
		{
			Scanner scanFile = new Scanner(file);
			scanFile.useDelimiter("\n");
			System.out.println(file.getPath());
			rpg = new TextRPG(scanFile);
		} catch(FileNotFoundException e)
		{
			System.out.println("No file found.");
			rpg = new TextRPG(scan);
		}
		rpg.run();
	}

}
