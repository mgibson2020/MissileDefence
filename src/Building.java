import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;


public class Building extends GameObject {
	
	public Building() {
		setHealth(3);
		setMaxHealth(3);
	}
	
	private void updLook() {
		
	}
	
	public void update(int u) {
		
	}
	
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);		
		g2d.fillRect(50, 0, 30, 30);
		g2d.drawRect(550, 50, 50, 50);

		g2d.draw(new Ellipse2D.Double(0, 100, 30, 30));
	}
}
