import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;


public class Shell extends Projectile{
	
	public Shell(MissileDefence game, int x, int y, double moveX, double moveY) {
		super(game, x, y, moveX, moveY);
	}

	private void collision() {
		
	}
	
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		
		AffineTransform oldXForm = g2d.getTransform();
		
		Rectangle shell = new Rectangle(-2,-2,4,4);
		
		g2d.setColor(Color.WHITE);
		g2d.translate(x,y);
		g2d.rotate(game.shootAngle);
		g2d.draw(shell);
		g2d.fill(shell);
		
		g2d.setTransform(oldXForm);
	}
}
