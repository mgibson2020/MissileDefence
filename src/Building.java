import java.awt.Color;
import java.awt.Graphics;

public class Building extends GameObject {
	protected int maxHealth = 1;
	protected int health = 1;
	protected int x;
	protected int y = 0;
	
	protected int width = 35;
	protected int height = 75;
	
	public Building() {}
	
	public Building(Game game, int x) {
		this.game = game;
		this.x = x;

		maxHealth = health = 3;
		height = health*15;
	}
	
	public void update() {
		
		
		y = game.getHeight() - game.groundHeight;
	}
	
	public void render(Graphics g) {
		g.setColor(new Color(150,200,255));		
		g.fillRect(x-width/2, y-height, width, height);
	}
}
