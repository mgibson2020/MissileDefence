import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

public class Shell extends Projectile{
	
	public Shell(MDGame game, int x, int y, double moveX, double moveY, double angle) {
		super(game, x, y, moveX, moveY, angle);
		
		height = 4;
		width = 4;
	}
	
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		
		AffineTransform oldXForm = g2d.getTransform();
		
		Rectangle shell = new Rectangle(-height,-width/2,height,width);
		
		g2d.setColor(Color.WHITE);
		g2d.translate(x,y);
		g2d.rotate(angle);
		g2d.draw(shell);
		g2d.fill(shell);
		
		g2d.setTransform(oldXForm);
		
		if (game.showBoundingBox) {
			g2d.setColor(Color.RED);
			g2d.draw(getArea());
		}
	}
}
