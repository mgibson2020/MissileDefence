import java.awt.Graphics;

import javax.swing.JPanel;

public abstract class GameObject {
	private int maxHealth = 1;
	private int health = getMaxHealth();
	
	private void onDeath() {
		
	}
	
	private void updLook() {
		
	}
	
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public void remHealth() {
		
	}
	
	public void update(int u) {
		
	}
	
	public void render(Graphics g) {
		
	}
	
}
