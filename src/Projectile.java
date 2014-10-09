/* 
 * Program: Projectile.java
 * Project: MissileDefense
 * Author: J. Ethan Wallace and Michael Gibson
 * Date Written: 10/05/2014 - 10/08/2014
 * Abstract: The class that all projectiles inherit from. It implements movement and angle.
 */

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

public abstract class Projectile extends GameObject {
	// Movement/rotation variables
	protected double moveX, moveY, angle;
	
	// Constructor
	public Projectile(MDGame game, int x, int y, double moveX, double moveY, double angle) {
		this.game = game;
		this.x = x;
		this.y = y;
		this.moveX = moveX;
		this.moveY = moveY;
		this.angle = angle;
	}
	
	// Overriden to allow for movement and destruction when too far outside of the window
	public void update() {
		x += moveX;
		y += moveY;
		
		if (x < -50 || x > game.getWidth()+500 || y < -500 || y > game.getWidth()-game.groundHeight)
			canRemove = true;
	}
	
	// Overridden from the parent so that it can take the rotation into account
	public Area getArea() {
		// Create the shape like normal then rotate it around its x and y
		Shape rect = new Rectangle((int)x, (int)y, height, width);
		AffineTransform transform = AffineTransform.getRotateInstance(angle, x, y);
		
		Area a = new Area(rect);
		a.transform(transform);
		
		return a;
	}
	
	public double getAngle() {
		return angle;
	}
}
