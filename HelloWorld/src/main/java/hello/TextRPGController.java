package hello;

import org.springframework.web.bind.annotation.*;

@RestController
public class TextRPGController {
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
     * @return a String representing the result of the inputted action
     */
    @PatchMapping("/TextRPG")
    public String inputCommand(@RequestParam(value="action") String action) {
    	String returnStr = "";
    	if(rpg == null) {
    		
    		returnStr += "No RPG instance found.";
    		return returnStr;
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
    			returnStr += "Error while attempting to buy potions: Input could not be read as a number.";
    			shopSuccess = false;
    			return returnStr;
    		}
    		
    	}
    	
    	if(action.equalsIgnoreCase("quit")) {
    		returnStr += "Unfortunately, you have failed.\n";
    		returnStr += "Game over.\n";
    		returnStr += "Score: " + ((player.getLevel() * 100) + (player.getMonstersDefeated() * 5));
    		rpg.setStatus(1);
    		System.out.println(returnStr);
    		System.exit(0);
    	}
    	
    	
    	boolean invalid = rpg.runCommand(action);
    	if(invalid == false) {
    		rpg.runEnemyTurn();
    	}
    	if(invalid == true) {
    		returnStr += "Either command 'status' was excecuted, potions were succesfully bought, or an invalid command was entered.";
    	}
    	else
    	{
    		returnStr +=  "Command '" +action + "' successfully executed.";
    	}
    	return returnStr;
    }
}
