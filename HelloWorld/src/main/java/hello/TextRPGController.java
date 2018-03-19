package hello;

import org.springframework.web.bind.annotation.*;
import java.lang.Runnable;

@RestController
public class TextRPGController implements Runnable{
	Player player;
	TextRPG rpg;
	boolean rewardsCollected;
	boolean shopSuccess;
	
	public TextRPGController() {
		this.rpg = Application.getRPG();
		this.player = rpg.getPlayer();
		rewardsCollected = false;
		shopSuccess = false;
	}
	
	@SuppressWarnings("static-access")
	public void run() {
		try {
			Thread.currentThread().sleep(2250);
		} catch (InterruptedException e) {
			System.out.println("Failed to exit program; Thread Interrupted.");
		}
		System.exit(0);
		
	}
    
    @GetMapping("/player")
    public Player getPlayerInfo() {
    	return player;
    }
    
    @GetMapping("/TextRPG")
    public TextRPG getRPG() {
    	return rpg;
    }
    
    /**
     * This method takes a given command from the server and runs it
     * @param action the command to be passed to rpg.runCommand(String)
     * @return a String representing the result of the inputed action
     */
    @PatchMapping("/TextRPG")
    public String inputCommand(@RequestParam(value="action") String action) {
    	String returnStr = "";
    	if(rpg == null) {
    		
    		returnStr += "No RPG instance found.";
    		return returnStr;
    	}
    	if(action.equalsIgnoreCase("quit")) {
    		returnStr += "Unfortunately, you have failed.\n";
    		returnStr += "Game over.\n";
    		returnStr += "Score: " + ((player.getLevel() * 100) + (player.getMonstersDefeated() * 5));
    		System.out.println(returnStr);
    		Thread t = new Thread(this);
    		t.start();
    		return returnStr;
    		
    	}
    	boolean inputIsNumber;
    	try  {
    		Integer.valueOf(action);
    		inputIsNumber = true;
    	}
    	catch(NumberFormatException e) {
    		inputIsNumber = false;
    	}
    	if(!inputIsNumber) {
    		boolean invalid = rpg.runCommand(action);
    		if(invalid == true) {
    			returnStr += "Either command 'status' was excecuted, potions were succesfully bought, or an invalid command was entered.";
    			return returnStr;
    		}
    		else
    		{
    			returnStr +=  "Command '" +action + "' successfully executed.\n";
    		}
    	}
    	if(rpg.getEnemy().isDead()) { 
    		if(!rewardsCollected) {
    			rewardsCollected = true;
    			returnStr += "Congratulations, you have won!\n";
    			player.getRewards();
    			returnStr += "You have recieved 10 gold.\n";
    			returnStr += "You now have " + player.getGold() + " gold.\n";
    			returnStr += "Health potions cost 20 gold each.\n";
    			//rpg.setStatus(1);
    			rpg.setAllowRewards(false);
    			
    			if(player.canLevelUp() && !rewardsCollected) {
    				player.levelUp();
    				returnStr += "You have leveled up!\n";
    			}
    		
    			
    			player.heal(player.getMaxHealth()/4);
    			if(player.getHealth() >= player.getMaxHealth()) player.updateHealth();
    		}
    		try {
    			if(!shopSuccess) {
    				shopSuccess = rpg.shop(Integer.valueOf(action));
    			}
    			if(shopSuccess) {
    				rpg.makeEnemy();
    				rpg.setAllowRewards(true);
    				returnStr += action + " potions were successfully purchased!\n";
    				rewardsCollected = false;
    				shopSuccess = false;
    			}
    			
    		} catch(NumberFormatException e) {
    			if(!action.equals("attack")) {
    				returnStr += "Error while attempting to buy potions: Input could not be read as a number.";
    			}
    			shopSuccess = false;
    		}
    		
    	}
    	
    	
   
    	rpg.forceUpdate();
    	return returnStr;
    }
}
