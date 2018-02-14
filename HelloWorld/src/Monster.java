public class Monster {
	int health;
	int maxHealth;
	int level;
	int minDamage;
	int maxDamage;

	/**
	 * sets health and damage values based on given level
	 * @param level The level of this monster
	 */
	public Monster(int level) {
		this.level = level;
		this.setHealth();
		this.setDamage();
	}

	public void damage(int damage) {
		this.health -= damage;
		System.out.println("Monster took " + damage + " damage!");
	}

	/** (this is a JavaDoc comment)
	 * this sets the max health and heals the monster to full health
	 */
	public void setHealth() {
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
	public void setDamage() {
		minDamage = 10 + level * 3;
		maxDamage = 10 + level * 6;
	}

	/**
	 * damages target for damage amount
	 * @param damage The amount of damage to do
	 * @param target The target
	 */
	public void attack(int damage, Player target) {
		target.damage(damage);
	}

	@Override
	public String toString() {
		String str = "Monster Level " + level + "\n";
		str += "Health: " + health + "/" + maxHealth;
		return str;
	}
}