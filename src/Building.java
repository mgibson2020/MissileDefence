import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

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
	}
	
	public void update() {
		height = health*15;
		y = game.getHeight() - game.groundHeight;
	}
	
	public void hit() {
		health--;
		//height = health*15;
		
		if (health < 1)
			onDeath();
	}
	
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		
		g.setColor(new Color(150,200,255));		
		g.fillRect((int)x-width/2, (int)y-height, width, height);
		
		if (game.showBoundingBox) {
			g.setColor(Color.RED);	
			g2d.draw(getArea().getBounds());
		}
	}
}
