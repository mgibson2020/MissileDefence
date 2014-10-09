import java.awt.Color;

public class Building extends GameObject {
	protected int maxHealth = 1;
	protected int health = 1;
	
	public Building() {}
	
	public Building(MDGame game, int x) {
		this.game = game;
		this.x = x;

		maxHealth = health = 3;
		width = 35;
		height = health*15;
		
		color = new Color(150,200,255);
	}
	
	public void update() {
		height = health*15;
		y = game.getHeight() - game.groundHeight;
	}
	
	public void hit() {
		health--;
		
		if (health < 1)
			onDeath();
	}
}
