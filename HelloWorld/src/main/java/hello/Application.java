package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements Runnable {

	static TextRPG rpg;
	
	public Application() {
		
	}
	
	
    public Application(TextRPG rpg) {
		Application.rpg = rpg;
	}

	public static void main(String[] args) {
        HelloWorld.main(args);
    }
	
	public static TextRPG getRPG() {
		return Application.rpg;
	}

	@Override
	public void run() {
		SpringApplication.run(Application.class);
	}
}

