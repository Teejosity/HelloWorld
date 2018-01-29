//Represents the computer enemy the Player will face in the game
public class Monster {
	//instance variables
	int health;
	int maxHealth;
	int level;
	int minDamage;
	int maxDamage;
	//Constructor, sets the level, min/max damage, and health
	public Monster(int level) {
		this.level = level;
		this.setHealth();
		this.setDamage();
	}
	//this method reduces the health of the monster according to @param damage
	public void damage(int damage) {
		this.health -= damage;
		System.out.println("Monster took " + damage + " damage!");
	}
	//this sets the max health and heals the monster to full health
	public void setHealth() {
		maxHealth = health = 90 + (10*level);
		health = maxHealth;
	}
	//this returns a boolean of whether the monster is dead or not
	public boolean isDead() {
		boolean dead = health <= 0;
		return dead;
	}
	//this sets the min/max damage values
	public void setDamage() {
		minDamage = 10 + level*3;
		maxDamage = 10 + level*6;
	}
	//this attacks @param target for @param damage
	public void attack(int damage, Player target) {
		target.damage(damage);
	}
	//returns a string representation of the object
	public String toString() {
		String str = "Monster Level " + level + "\n";
		str += "Health: " + health + "/" + maxHealth;
		return str;
	}
}