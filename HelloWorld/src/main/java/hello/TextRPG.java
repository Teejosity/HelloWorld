package hello;

import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Random;
import java.lang.Thread;

public class TextRPG {
	private Scanner scan;
	private Player player;
	private Monster enemy;
	private Random randomizer = new Random();
	private final long id;
	boolean gameOver = false;
	private boolean rewardsAllowed;

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
		id = 0;
		player = new Player(name);
		rewardsAllowed = true;
	}
	
	public TextRPG(long id, Player p) {
		System.out.println("Player, please enter your name");
		scan = new Scanner(System.in);
		this.player = p;
		this.id = id;
		enemy = new Monster(player.getLevel());
		rewardsAllowed = true;
		//this.run();
	}
	
	public long getId() {
		return this.id;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Monster getEnemy() {
		return enemy;
	}
	
	public void makeEnemy() {
		if(!gameOver) {
			this.enemy = new Monster(player.getLevel());
		}
	}
	
	public void run() {
		if(!gameOver) {
			System.out.println("Welcome to my RPG!");
			System.out.println("You will be faced with monsters, and you will only have the opportunity to buy items in-between fights.");
			while (!player.isDead()) {
				this.battle();
				makeEnemy();
			}
			System.out.println("Unfortunately, you have failed.");
			System.out.println("Game over.");
			System.out.println("Score: " + ((player.getLevel() * 100) + (player.getMonstersDefeated() * 5)));
			System.exit(0);
		}
	}

	@SuppressWarnings("static-access")
	public void battle() {
		if(!gameOver) {
			enemy = new Monster(player.getLevel());
			this.setAllowRewards(true);
			System.out.println("The battle has begun!");
    
			// Main combat loop
			while (!enemy.isDead() && !player.isDead() && !gameOver) {
				System.out.println("Your opponent is: " + enemy.toString());
				runPlayerTurn();
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
			}
    
		//Determine outcome of battle
			if(enemy.isDead() && !gameOver && rewardsAllowed) {
				System.out.println("Congrats, you have won!");
				System.out.println("You earned 10 gold.");
				player.getRewards();
				if (player.canLevelUp()) {
					System.out.println("Level up!");
					System.out.println("Health has been restored, and stats have been increased!");
					player.levelUp();
				}
				this.setAllowRewards(false);
			}
    
    
			this.shop();
    
			// allowing the user to quit
		
			System.out.println("If you would like to quit, please type 'quit'. Otherwise, press enter to continue.");
			String quit = "";
			if (scan.hasNextLine()) {
				quit = scan.nextLine();
			} else {
				throw new NoInputFoundException();
			}
			if (quit.equalsIgnoreCase("quit")) {
				player.forceKill();
				return;
			}

			if (player.getHealth() < (int) (player.getMaxHealth() / 4.0)) {
				// Healing to 25% of max health
				player.heal((int) ((1.0 / 4.0) * player.getMaxHealth()));
			}
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
			System.out.println("Would you like to heal, attack, or defend? You may see your stats by typing 'stats'");
			if(enemy.isDead()) {
				return;
			}
			if (scan.hasNext()) {
				userChoice = scan.nextLine();
			} else {
				System.out.println("No input found. Ending Game.");
				player.forceKill();
				throw new NoInputFoundException();
			}
      
			invalid = runCommand(userChoice);
		} while (invalid == true);
	}
  
	/**
	 * Run the given command while fighting the given enemy
	 * @param userChoice The command entered by the user
	 * @param enemy The enemy the user is facing
	 * @return true if the entered command was invalid
	 */
	public boolean runCommand(String userChoice) {
		System.out.println(userChoice);
		player.setDefense(0);
		int power = randomizer.nextInt((player.getMaxDamage() - player.getMinDamage()) + 1) + player.getMinDamage();
		if (userChoice.equalsIgnoreCase("Attack")) {
			System.out.println("The enemy's defence is: " + enemy.getDefense());
			if (enemy.getDefense() > power) {
				player.attack(0, enemy);
			} else {
				player.attack(power - enemy.getDefense(), enemy);
			}
		} else if (userChoice.equalsIgnoreCase("Defend")) {
			player.setDefense(power);
		} else if (userChoice.equalsIgnoreCase("Heal")) {
			if (player.getNumPotions() > 0) {
				// Heals 20%
				player.heal((int) ((1.0 / 5.0) * player.getHealth()));
				if (player.getHealth() > player.getMaxHealth()) {
					player.updateHealth();
				}
				System.out.println("You have been healed!");
				player.setNumPotions(player.getNumPotions()-1);
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
			//System.out.println(userChoice);
			return true;
		}
    
		return false;
	}
  
	/**
	 * In a combat scenario runs the given enemies turn 
	 */
	public void runEnemyTurn() {
		enemy.setDefense(0);
		boolean computerAttacks = randomizer.nextBoolean();
		int computerPower = randomizer.nextInt((enemy.getMaxDamage() - enemy.getMinDamage()) + 1) + enemy.getMinDamage();
		if (computerAttacks == true) {
			System.out.println("The enemy has chosen to attack!");
			System.out.println("Your defence is: " + player.getDefense());
			if (player.getDefense() > computerPower) {
				enemy.attack(0, player);
			} else {
				enemy.attack(computerPower - player.getDefense(), player);
			}
		} else {
			System.out.println("The enemy has chosen to defend!");
			enemy.setDefense(computerPower);
		}
	}

	/**
	 * Allows the user to buy health potions using the gold they earn from killing
	 * monsters
	 */
	public void shop() {
		System.out.println("You now have " + player.getGold() + " gold.");
		System.out.println("Health Potions restore 20% of your health.");
		System.out.println("They cost 20 gold.");
		boolean isValid = false;
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
					player.forceKill();
					return;
				}
			} catch (InputMismatchException e) {
				System.out.println("That was not an integer. Please try again.");
				isValid = false;
			}
			if (isValid == true) {
				purchasePrice = 20 * numPotionsBought;
				if (player.getGold() < purchasePrice) {
					System.out.println("You do not have enough gold for that purchase.");
					isValid = false;
				}
			}
		} while (isValid == false || player.getGold() < purchasePrice);
		player.setNumPotions(player.getNumPotions() + numPotionsBought);
		player.setGold(player.getGold() - purchasePrice);
		System.out.println("You now have " + player.getGold() + " gold.");
	}
	
	public boolean shop(Integer i) {
		if(i*20 > player.getGold()) {
			return false;
		} 
		else {
			player.setGold(player.getGold()-(i*20));
			return true;
		}
	}

	public void setStatus(int i) {
		if(i == 1)
		{
			this.gameOver = true;
		}
		else {
			this.gameOver = false;
		}
		
	}

	public void setAllowRewards(boolean b) {
		this.rewardsAllowed = b;
		
	}
}
