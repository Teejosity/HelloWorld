public class Player {
	private String name;
	private int health;
	private int maxHealth;
	private int level;
	private int monstersDefeated;
	private int minDamage;
	private int maxDamage;
	private int numPotions;
	private int gold;
	private int defense;

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
		this.updateDamage();
		this.defense = 0;
	}

	/**
	 * This levels up the user, increasing their min/max damage and max health
	 * accordingly, as well as healing the user
	 */
	public void levelUp() {
		level += 1;
		this.updateHealth();
		this.updateDamage();
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
	public void updateDamage() {
		minDamage = 10 + (level * 3);
		maxDamage = 10 + (level * 6);
	}

	/**
	 * This sets the max health according to the level and heals the player to full
	 * health
	 */
	public void updateHealth() { 
		this.maxHealth = 90 + (10 * level);
		this.health = maxHealth;
	}
	
	public int getDefense() {
		return this.defense;
	}
	
	public void setDefense(int d) {
		if (d < 0) throw new IllegalArgumentException("Defence must be non-negative!");
		
		this.defense = d;
	}
	
	public void setNumPotions(int p) {
		if(p < 0) throw new IllegalArgumentException("Potion amount must be non-negative!");
		
		this.numPotions = p;
	}
	
	public int getGold() {
		return this.gold;
	}
	
	public void setGold(int g) {
		if (g < 0) throw new IllegalArgumentException("Gold amount must be non-negative!");
		
		this.gold = g;
	}
	
	public int getMaxHealth() {
		return this.maxHealth;
	}

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
  
  public int getMaxDamage() {
    return this.maxDamage;
  }
  
  public int getMinDamage() {
	  return this.minDamage;
  }
  
  public int getNumPotions() {
	  return this.numPotions;
  }

	public boolean isDead() {
		boolean dead = health <= 0;
		return dead;
	}

	public int getLevel() {
		return level;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getHealth() {
		return this.health;
	}
	
	public void forceKill() {
		this.health = 0;
	}

	@Override
	public String toString() {
		String str = "Player " + this.getName() + " Level " + this.getLevel() + "\n";
		str += "Health: " + this.getHealth() + "/" + maxHealth + "\n";
		str += "Number of Health Potions: " + this.getNumPotions() + "\n";
		str += "Power Range: " + minDamage + "-" + getMaxDamage();
		return str;
	}

	public int getMonstersDefeated() {
		return this.monstersDefeated;
	}
	
}
