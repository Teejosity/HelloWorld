import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Random;
import java.lang.Thread;

public class TextRPG {
	Scanner scan;
	Player player;
	int errorLevel;
	Random randomizer = new Random();

	/**
	 * Constructs a new TextRPG utilizing scan for input
	 * 
	 * @param scan
	 *            The scanner and input type to be used
	 */
	public TextRPG(Scanner scan) {
		System.out.println("Player, please enter your name");
		this.scan = scan;
		String name = "";
		if (scan.hasNextLine()) {
			name = scan.nextLine();
		} else {

		}
		errorLevel = 0;
		player = new Player(name);
	}

	public void run() {
		System.out.println("Welcome to my RPG!");
		System.out.println(
				"You will be faced with monsters, and you will only have the opportunity to buy items inbetween fights.");
		while (!player.isDead() && errorLevel == 0) {
			this.battle();
		}
		if (errorLevel == 1) {
			return;
		}
		System.out.println("Unfortunately, you have failed.");
		System.out.println("Game over.");
		System.out.println("Score: " + ((player.level * 100) + (player.monstersDefeated * 5)));
	}

	@SuppressWarnings("static-access")
	public void battle() {
		Monster enemy = new Monster(player.getLevel());
		int playerDefence = 0;
		System.out.println("The battle has begun!");
    
    // Main combat loop
		while (!enemy.isDead() && !player.isDead() && errorLevel == 0) {
			System.out.println("Your opponent is: " + enemy.toString());
			
      runPlayerTurn();
      
			if (errorLevel == 1) {
				return;
			}
      
			runEnemyTurn();
			System.out.println("Turn Complete!");
			/*
			 * waiting 5 seconds so the user isn't completely flooded with information
			 * allows the user to look at what happened before loop continues
			 */
			try {
				Thread.currentThread().sleep(4000);
			} catch (InterruptedException e) {

			}
			playerDefence = 0;
			if (errorLevel == 1) {
				return;
			}
		}
    
    // Determine outcome of battle
		if (enemy.isDead() && errorLevel == 0) {
			System.out.println("Congrats, you have won!");
			System.out.println("You earned 10 gold.");
			player.getRewards();
			if (player.canLevelUp()) {
				System.out.println("Level up!");
				System.out.println("Health has been restored, and stats have been increased!");
				player.levelUp();
			}
		}
    
    // TODO (maybe?) might want to clean up how player death is handled, as I think it isn't as clear as it could be
    
		if (errorLevel == 0) {
			this.shop();
		}
    
		// allowing the user to quit
		System.out.println("If you would like to quit, please type 'quit'. Otherwise, press enter to continue.");
		String quit = "";
		if (scan.hasNextLine()) {
			quit = scan.nextLine();
		} else {
			System.out.println("No input found. Ending Game.");
			quit = "quit";
		}
		if (quit.equalsIgnoreCase("quit")) {
			player.damage(9999);
			return;
		}

		if (player.health < (int) (player.maxHealth / 4.0)) {
			// Healing to 25% of max health
			player.health = (int) ((1.0 / 4.0) * player.maxHealth);
		}
	}
  
  /**
   * In a combat scenario run the player's turn
   *     If a fatal error occurred errorLevel will be set to 1 (this should be changed to raising an exception)
   */
  private void runPlayerTurn() {
    boolean invalid = false;
    do {
      String userChoice = "";
      System.out
          .println("Would you like to heal, attack, or defend? You may see your stats by typing 'stats'");
      if (scan.hasNext()) {
        userChoice = scan.nextLine();
      } else {
        System.out.println("No input found. Ending Game.");
        player.damage(9999);
        errorLevel = 1; // TODO switch this from an errorLevel (which is a C-style thing) to raising an exception (which is Java-style)
        return;
      }
      
      invalid = runCommand(userChoice, enemy);
    } while (invalid == true);
  }
  
  /**
   * Run the given command while fighting the given enemy
   * @param userChoice The command entered by the user
   * @param enemy The enemy the user is facing
   * @return true if the entered command was invalid
   */
  private void runCommand(String userChoice, Monster enemy) {
    int power = randomizer.nextInt((player.getMaxDamage() - player.minDamage) + 1) + player.minDamage;
    if (userChoice.equalsIgnoreCase("Attack")) {
      System.out.println("The enemy's defence is: " + enemy.getDefence());
      if (enemy.getDefence() > power) {
        player.attack(0, enemy);
      } else {
        player.attack(power - enemy.getDefence(), enemy);
      }
    } else if (userChoice.equalsIgnoreCase("Defend")) {
      playerDefence = power;
    } else if (userChoice.equalsIgnoreCase("Heal")) {
      if (player.numPotions > 0) {
        // Heals 20%
        player.heal((int) ((1.0 / 5.0) * player.health));
        if (player.health > player.maxHealth) {
          player.setHealth();
        }
        System.out.println("You have been healed!");
        player.numPotions--;
        return true;
      } else {
        System.out.println("You do not have any potions!");
        return true;
      }
    } else if (userChoice.equalsIgnoreCase("Stats")) {
      System.out.println(player.toString());
      return true;
    } else {
      System.out.println("That was not a valid argument. Please try again.");
      System.out.println(userChoice);
      return true;
    }
    
    return false;
  }
  
  /**
   * In a combat scenario runs the given enemies turn 
   */
  private void runEnemyTurn(Monster enemy) {
    enemy.setDefence(0);
    boolean computerAttacks = randomizer.nextBoolean();
    int computerPower = randomizer.nextInt((enemy.getMaxDamage() - enemy.minDamage) + 1) + enemy.minDamage;
    if (computerAttacks == true) {
      System.out.println("The enemy has chosen to attack!");
      System.out.println("Your defence is: " + playerDefence); // TODO change playerDefence like monsterDefence was changed (add getters and setters)
      if (playerDefence > computerPower) {
        enemy.attack(0, player);
      } else {
        enemy.attack(computerPower - playerDefence, player);
      }
    } else {
      System.out.println("The enemy has chosen to defend!");
      enemy.setDefence(computerPower);
    }
  }

	/**
	 * Allows the user to buy health potions using the gold they earn from killing
	 * monsters
	 */
	public void shop() {
		System.out.println("You now have " + player.gold + " gold.");
		System.out.println("Health Potions restore 20% of your health.");
		System.out.println("They cost 20 gold.");
		boolean isValid = true;
		Integer numPotionsBought = 0;
		int purchasePrice = 0;
		do {
			System.out.println("How many would you like to buy?");
			try {
				isValid = true;
				numPotionsBought = 0;
				if (scan.hasNext()) {
					try {
						numPotionsBought = Integer.valueOf(scan.nextLine());
					} catch (NumberFormatException e) {
						System.out.println("The value is invalid. Please try again.");
						isValid = false;
					}
				} else {
					System.out.println("No input found. Ending Game.");
					player.damage(9999);
					return;
				}
			} catch (InputMismatchException e) {
				System.out.println("That was not an integer. Please try again.");
				isValid = false;
			}
			if (isValid = true) {
				purchasePrice = 20 * numPotionsBought;
				if (player.gold < purchasePrice) {
					System.out.println("You do not have enough gold for that purchase.");
				}
			}
		} while (isValid == false || player.gold < purchasePrice);
		player.numPotions += numPotionsBought;
		player.gold -= purchasePrice;
		System.out.println("You now have " + player.gold + " gold.");
	}
}
