import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;


public class Building extends GameObject {
	
	private int x;
	private int y;
	
	public Building(int x, int y) {
		setHealth(3);
		setMaxHealth(3);
		this.x = x;
		this.y = y;
	}
	
	private void updLook() {
		
	}
	
	public void update(int u) {
		
	}
	
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);		
		g2d.fillRect(x, y, 50, 50);
	}
}
