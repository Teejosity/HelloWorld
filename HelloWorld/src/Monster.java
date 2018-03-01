public class Monster {
	private int health;
	private int maxHealth;
	private int level;
	private int minDamage;
	private int maxDamage;
	private int defense;

	/**
	 * sets health and damage values based on given level
	 * 
	 * @param level
	 *            The level of this monster
	 */
	public Monster(int level) {
		this.level = level;
		this.updateHealth();
		this.updateDamage();
		this.defense = 0;
	}

	public void damage(int damage) {
		this.health -= damage;
		System.out.println("Monster took " + damage + " damage!");
	}

	/**
	 * (this is a JavaDoc comment) 
	 * this sets the max health and heals the monster to full health
	 */
	public void updateHealth() {
		maxHealth = health = 90 + (10 * level);
		health = maxHealth;
	}

	public boolean isDead() {
		boolean dead = health <= 0;
		return dead;
	}

	/**
	 * sets the min/max damage values based on the level
	 */
	public void updateDamage() {
		minDamage = 10 + level * 3;
		maxDamage = 10 + level * 6;
	}

	/**
	 * damages target for damage amount
	 * 
	 * @param damage
	 *            The amount of damage to do
	 * @param target
	 *            The target
	 */
	public void attack(int damage, Player target) {
		target.damage(damage);
	}
  
	public int getDefense() {
		return this.defense;
	}
  
	public void setDefense(int d) {
    // Check for valid conditions (this is a note for you, wouldn't normally need this comment)
    if (d < 0) throw new IllegalArgumentException("Defence must be non-negative!");
    
    this.defense = d;
  }

	@Override
	public String toString() {
		String str = "Monster Level " + level + "\n";
		str += "Health: " + health + "/" + maxHealth;
		return str;
	}

	public int getMaxDamage() {
		return maxDamage;
	}

	public int getMinDamage() {
		return this.minDamage;
	}
}