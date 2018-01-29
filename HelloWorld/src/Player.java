//Player class, represents the player in the game
public class Player {
	//instance variables
	String name;
	int health;
	int maxHealth;
	int level;
	int monstersDefeated ;
	int minDamage;
	int maxDamage;
	int numPotions;
	int gold;
	//Constructor, takes in the name received earlier and defaults the values of all the instance vars
	public Player(String name) {
		this.name = name;
		this.level = 1;
		this.monstersDefeated = 0;
		this.gold = 60;
		this.numPotions = 1;
		this.setHealth();
		this.setDamage();
	}
	//This levels up the user, increasing their min/max damage and max health accordingly
	//this also heals the user to their new max health
	public void levelUp() {
		level += 1;
		this.setHealth();
		this.setDamage();
	}
	//this returns a boolean of whether the user is qualified to level up or not.
	//The user must kill 3 monsters in order to level up
	public boolean canLevelUp() {
		boolean levelUp = monstersDefeated >= 3*level;
		return levelUp;
	}
	//this heals the player for @param health
	public void heal(int health) {
		this.health += health;
	}
	//this sets the min/max damage values for the player
	public void setDamage() {
		minDamage = 10 + level*3;
		maxDamage = 10 + level*6;
	}
	//this sets the maxHealth of the player according to their level and heals them to that health
	public void setHealth() {
		this.maxHealth = 90 + (10*level);
		this.health = maxHealth;
	}
	//this attacks @param target for @param damage
	public void attack(int damage, Monster target) {
		target.damage(damage);
	}
	//this rewards the user after they defeat a monster with 10 gold and increases the monstersDefeated count by 1
	public void getRewards() {
		monstersDefeated += 1;
		gold += 10;
	}
	//this method damages the player for @param damage
	public void damage(int damage) {
		System.out.println("Player took " + damage + " damage!");
		this.health -= damage;
	}
	//this method returns a boolean of if the player is dead or not (ie: their health is <= 0)
	public boolean isDead() {
		boolean dead = health <= 0;
		return dead;
	}
	//getter method for the user's level
	public int getLevel() {
		return level;
	}
	//returns a string representation of the player object
	public String toString() {
		String str = "Player " + name + " Level " + level + "\n";
		str += "Health: " + health + "/" + maxHealth + "\n";
		str += "Number of Health Potions: " + this.numPotions + "\n";
		str += "Power Range: " + minDamage + "-" + maxDamage;
		return str;
	}
}
