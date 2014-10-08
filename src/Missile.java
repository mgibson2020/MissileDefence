import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;


public class Missile extends Projectile {
	
	public Missile(MDGame game, int x, int y, double moveX, double moveY, double angle) {
		super(game, x, y, moveX, moveY, angle);

		height = 28;
		width = 6;
		
		color = new Color(255,200,150);
	}
	
	public void update() {
		super.update();
		
		if (!canRemove)
			canRemove = game.checkCollisions(this);
	}
	
	
}
