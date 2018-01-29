//importing for input, randomization
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Random;
import java.lang.Thread;
//main class, controls the game
public class TextRPG {
	//scanner obj for gathering input
	Scanner scan = new Scanner(System.in);
	//Player obj representing the player
	Player player;
	//randomizer for randomizing damage and defense
	Random randomizer = new Random();
	//Constructor
	public TextRPG() {
		//Initialize player object
		//getting player name
		System.out.println("Player, please enter your name");
		String name = scan.nextLine();
		player = new Player(name);
	}
	//sets up and ends the game
	public void run() {
		//setup text
		System.out.println("Welcome to my RPG!");
		System.out.println("You will be faced with monsters, and you will only have the opportunity to buy items inbetween fights.");
		//the player battles until they die, or if they ask to quit after beating an enemy
		while(!player.isDead()) 
		{
			//calling the main function, pits user against computer opponent
			this.battle();
		}
		//ending the game and calculating score
		System.out.println("Unfortunately, you have failed.");
		System.out.println("Game over.");
		System.out.println("Score: " + ((player.level*100) + (player.monstersDefeated*5)));
	}
	@SuppressWarnings("static-access")
	//Main RPG method, handles the battle between the player and computer
	public void battle() {
		//insance vars
		//the enemy that the player will battle, has its level set to the player's level
		Monster enemy = new Monster(player.getLevel());
		//important defense variables used in the loop, need to be declared outside of loop
		int playerDefence = 0;
		int cpuDefence = 0;
		//looping over the loop until someone dies
		System.out.println("The battle has begun!");
		while(!enemy.isDead() && !player.isDead()) 
		{
			//printing information about the opponent
			System.out.println("Your opponent is: " + enemy.toString());
			boolean invalid = false;
			do 
			{
				//getting user input and saving it to userChoice
				System.out.println("Would you like to heal, attack, or defend? You may see your stats by typing 'stats'");
				String userChoice = scan.nextLine();
				//randomizing the attack/defense values according to the player's power range
				int power = randomizer.nextInt((player.maxDamage - player.minDamage) +1) + player.minDamage;
				//attacking
				if(userChoice.equalsIgnoreCase("Attack")) 
				{
					//alerting the user of the enemy's defence (if any)
					System.out.println("The enemy's defence is: " + cpuDefence);
					//dealing damage
					if(cpuDefence > power) 
					{
						player.attack(0,  enemy);
					} else {
						player.attack(power-cpuDefence, enemy);
					}
					
					invalid = false;
				}
				//defends, saving the power variable to the playerDefense variable declared earlier
				//this will be used to reduce the damage dealt by the opponent when they attack
				//this value resets after every cycle, and does not carry over
				else if(userChoice.equalsIgnoreCase("Defend")) 
				{
					playerDefence = power;
					invalid = false;
				}
				//healing the user 20% of their health if they have potions remaining
				else if(userChoice.equalsIgnoreCase("Heal"))
				{
					if(player.numPotions > 0)
					{
						player.heal((int) ((1.0/5.0) * player.health));
						if(player.health > player.maxHealth) {
							player.setHealth();
						}
						invalid = true;
					}
					else {
						System.out.println("You do not have any potions!");
						invalid = true;
					}
				}
				//displaying the user's stats
				else if(userChoice.equalsIgnoreCase("Stats"))
				{
					System.out.println(player.toString());
					invalid = true;
				}
				//otherwise, we loop until we receive a valid input
				else
				{
					System.out.println("That was not a valid argument. Please try again.");
					invalid = true;
				}
			} while(invalid == true);
			//resetting cpuDefence to avoid carryover from last cycle
			cpuDefence = 0;
			//Randomly deciding the computer's move and attack/defense value
			boolean computerAttacks = randomizer.nextBoolean();
			int computerPower = randomizer.nextInt((enemy.maxDamage - enemy.minDamage) +1) + enemy.minDamage;
			//attacking the player, any player defense value is applied here to reduce damage
			if(computerAttacks == true) 
			{
				//alerying the user of the enemy's choice
				System.out.println("The enemy has chosen to attack!");
				if(playerDefence > computerPower) 
				{
					enemy.attack(0,  player);
				}
				else 
				{
					enemy.attack(computerPower - playerDefence, player);
				}
			} else 
			{
				//setting cpuDefence value to reduce damage if the user attacks on their next turn
				System.out.println("The enemy has chosen to defend!");
				cpuDefence = computerPower;
			}
			System.out.println("Turn Complete!");
			//waiting 5 seconds so the user isn't completely flooded with information
			//allows the user to look back at what happened before loop continues
			try
			{
				Thread.currentThread().sleep(4000);
			}
			catch(InterruptedException e) {
				
			}
			//resetting the playerDefence value from earlier
			playerDefence = 0;
		}
		//If the enemy died, the user wins. Even if they both die, the user still wins
		if(enemy.isDead()) 
		{
			//rewarding the user, and leveling them up if they have defeated enough opponents
			System.out.println("Congrats, you have won!");
			System.out.println("You earned 10 gold.");
			player.getRewards();
			if(player.canLevelUp()) 
			{
				System.out.println("Level up!");
				System.out.println("Health has been restored, and stats have been increased!");
				player.levelUp();
			}
		}
		//we call the shop so the user can buy potions to heal with
		this.shop();
		//allowing the user to quit
		System.out.println("If you would like to quit, please type 'quit'. Otherwise, press enter to continue.");
		String quit = scan.nextLine();
		//killing the user to break the loop if they want to quit
		if(quit.equalsIgnoreCase("quit")) 
		{
			player.damage(player.health);
		}
		//healing the user after the battle to 25% of their health if their health is less than 25%
		if(player.health < (int) (player.maxHealth/4.0))
		{
			player.health = (int) ((1.0/4.0) * player.maxHealth);
		}
	}
	//The shop method, allows user to buy health potions using the gold they earn from killing monsters
	public void shop() {
		//telling the user how much money they have, and reminding them of health potions
		System.out.println("You now have " + player.gold + " gold.");
		System.out.println("Health Potions restore 20% of your health.");
		System.out.println("They cost 20 gold.");
		boolean isValid = true;
		int numPotionsBought = 0;
		int purchasePrice = 0;
		//looping over until we get a valid input
		do 
		{
			//prompting
			System.out.println("How many would you like to buy?");
			try 
			{
				//getting the number of potions they want to buy
				numPotionsBought = scan.nextInt();
				scan.nextLine();
				isValid = true;
			} catch(InputMismatchException e) 
			{
				//we catch the Exception here is something other than an integer was passed in
				System.out.println("That was not an integer. Please try again.");
				isValid = false;
			}
			//we also loop over again if the user does not have enough gold
			if(isValid = true) 
			{
				purchasePrice = 20*numPotionsBought;
				if(player.gold < purchasePrice) 
				{
					System.out.println("You do not have enough gold for that purchase.");
				}
			}
			
		} while(isValid == false || player.gold < purchasePrice);
		//updating the number of potions the user has, and deducting the gold used to purchase them
		player.numPotions += numPotionsBought;
		player.gold -= purchasePrice;
	}
}
