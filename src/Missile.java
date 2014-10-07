import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;


public class Missile extends Projectile {
	
	public Missile(MissileDefence game, int x, int y, double moveX, double moveY, double angle) {
		super(game, x, y, moveX, moveY, angle);
	}

	private void collision() {
		
	}
	
	public void update() {
		super.update();
		
		angle+=Math.PI/100;
	}
	
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		
		AffineTransform oldXForm = g2d.getTransform();
		
		Rectangle shell = new Rectangle(-28,-3,28,6);
		
		g2d.setColor(new Color(255,200,150));
		g2d.translate(x,y);
		g2d.rotate(angle);
		g2d.draw(shell);
		g2d.fill(shell);
		
		g2d.setTransform(oldXForm);
	}
}
