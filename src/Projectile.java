import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;


public abstract class Projectile extends GameObject {
	protected double moveX, moveY, angle;
	
	public Projectile(MDGame game, int x, int y, double moveX, double moveY, double angle) {
		this.game = game;
		this.x = x;
		this.y = y;
		this.moveX = moveX;
		this.moveY = moveY;
		this.angle = angle;
	}
	
	public void update() {
		x += moveX;
		y += moveY;
		
		if (x < -50 || x > game.getWidth()+500 || y < -500 || y > game.getWidth()-game.groundHeight)
			canRemove = true;
	}
	
	public double getAngle() {
		return angle;
	}
	
	public Area getArea() {
		Shape rect = new Rectangle((int)x, (int)y, height, width);
		AffineTransform transform = AffineTransform.getRotateInstance(angle, x, y);
		
		Area a = new Area(rect);
		a.transform(transform);
		
		return a;
	}
}
