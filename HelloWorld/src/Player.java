public class Player {
	String name;
	int health;
	int maxHealth;
	int level;
	int monstersDefeated;
	int minDamage;
	private int maxDamage;
	int numPotions;
	int gold;

	/**
	 * Sets health, damage, gold, and the number of potions to default values
	 * 
	 * @param name
	 *            The name of the player
	 */
	public Player(String name) {
		this.name = name;
		this.level = 1;
		this.monstersDefeated = 0;
		this.gold = 60;
		this.numPotions = 1;
		this.updateHealth();
		this.setDamage();
	}

	/**
	 * This levels up the user, increasing their min/max damage and max health
	 * accordingly, as well as healing the user
	 */
	public void levelUp() {
		level += 1;
		this.updateHealth();
		this.setDamage();
	}

	/**
	 * Checks if the user can level up; the user must have killed 3 monsters to
	 * level up
	 * 
	 * @return levelUp The boolean of whether the player can level up or not
	 */
	public boolean canLevelUp() {
		boolean levelUp = monstersDefeated >= 3 * level;
		return levelUp;
	}

	/**
	 * This heals the player for health amount
	 * 
	 * @param health
	 *            The amount of health the player regains
	 */
	public void heal(int health) {
		this.health += health;
	}

	/**
	 * This sets the min/max damage/power based upon the level
	 */
	public void setDamage() { // TODO: rename, as set'X' should be reserved for setters. See Monster.setHealth(int health)
		minDamage = 10 + level * 3;
		maxDamage = 10 + level * 6;
	}

	/**
	 * This sets the max health according to the level and heals the player to full
	 * health
	 */
	public void updateHealth() { 
		this.maxHealth = 90 + (10 * level);
		this.health = maxHealth;
	}

	// this attacks @param target for @param damage
	/**
	 * Attacks the target for damage amount
	 * 
	 * @param damage
	 *            The amount of damage to deal
	 * @param target
	 *            The target
	 */
	public void attack(int damage, Monster target) {
		target.damage(damage);
	}

	/**
	 * This rewards the user after a victorious battle
	 */
	public void getRewards() {
		monstersDefeated += 1;
		gold += 10;
	}

	/**
	 * This damages the player for damage amount
	 * 
	 * @param damage
	 *            The amount of damage to deal
	 */
	public void damage(int damage) {
		System.out.println("Player took " + damage + " damage!");
		this.health -= damage;
	}
  
  public int getMaxDamage()() {
    return this.maxDamage;
  }

	public boolean isDead() {
		boolean dead = health <= 0;
		return dead;
	}

	public int getLevel() {
		return level;
	}

	@Override
	public String toString() {
		String str = "Player " + name + " Level " + level + "\n";
		str += "Health: " + health + "/" + maxHealth + "\n";
		str += "Number of Health Potions: " + this.numPotions + "\n";
		str += "Power Range: " + minDamage + "-" + getMaxDamage();
		return str;
	}
}
